package dvp.manga.model

import com.vicpin.krealmextensions.*
import dvp.manga.GetDataCallback
import dvp.manga.LoadSite
import dvp.manga.common.RealmTasker
import dvp.manga.common.Utils
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * @author Zero on 2/13/2018.
 */
class VnSharing : BaseManga() {

    private var baseUrl = "http://truyen.vnsharing.site/index/"

    override fun getMangas(page: Int, callback: GetDataCallback) {
        if (Utils.isOnline()) {
            try {
                LoadSite(baseUrl + "KhamPha/update/" + page, object : LoadSite.Callback {
                    val mangas = mutableListOf<Manga>()
                    override fun onFinished(result: Element) {
                        val list = result.select("ul#browse_result_wrap > li")
                        if (list.isNotEmpty())
                            list.removeAt(0)
                        list.mapTo(mangas) { parseManga(it, page) }
                        callback.onGetMangaFinished(mangas)
                        RealmTasker.SaveDBTask(mangas).execute()
                    }
                }).execute()
            } catch (ex: Exception) {
                getMangas(page, callback)
            }
        } else {
            RealmTasker.QueryTask(callback, page).execute()
        }
    }

    override fun getMangasByGenre(url: String, page: Int, callback: GetDataCallback) {
        if (Utils.isOnline()) {
            try {
                LoadSite(url.replace("TimKiem/[0-9]+".toRegex(), "TimKiem/" + page), object : LoadSite.Callback {
                    val mangas = mutableListOf<Manga>()
                    override fun onFinished(result: Element) {
                        val list = result.select("ul#browse_result_wrap > li")
                        if (list.isNotEmpty())
                            list.removeAt(0)
                        list.mapTo(mangas) { parseManga(it, -1) }
                        callback.onGetMangaFinished(mangas)
                    }
                }).execute()
            } catch (ex: Exception) {
                getMangas(page, callback)
            }
        }
    }

    private fun parseManga(item: Element, page: Int): Manga {
        val coverUrl = item.select("img.lazy").attr("data-src")
        val href = item.select("a.title").attr("href").replace(baseUrl, "")
        val title = item.select("a.title").text()
        val lastChap = item.select("a.chapter").text()
        return Manga(page = page, title = title, cover_url = coverUrl, href = href, last_chap = lastChap, updated = System.currentTimeMillis())
    }

    override fun getChaps(manga: Manga, callback: GetDataCallback) {
        if (Utils.isOnline()) {
            LoadSite(baseUrl + manga.href, object : LoadSite.Callback {
                val chapters = mutableListOf<Chapter>()
                override fun onFinished(result: Element) {
                    val list = result.select("div#manga-chaplist > ul > li")
                    for (item in list) {
                        val a = item.select("a.title")
                        val href = a.attr("href").replace(baseUrl, "")
                        val title = a.text().replace(manga.title!!, "")
                        val posted_date = item.select("a.author").text()
                        chapters.add(Chapter(title = title, href = href, post_date = posted_date, manga_id = manga.id))
                    }
                    callback.onGetChapterFinished(chapters)
                    chapters.saveAll()
                    saveMangaInfo(manga.id, result, callback)
                }
            }).execute()
        } else {
            callback.onGetChapterFinished((Chapter().query { equalTo("manga_id", manga.id) }))
            val info = getMangaInfo(manga.id)
            if (info != null)
                callback.onGetMangaInfoFinished(info)
        }
    }

    private fun getMangaInfo(mangaId: Int): MangaInfo? {
        return MangaInfo().queryFirst { equalToValue("manga_id", mangaId) }
    }

    private fun saveMangaInfo(mangaId: Int, result: Element, callback: GetDataCallback) {
        val origin = getMangaInfo(mangaId)
        if (origin == null) {
            val info = parseInfo(mangaId, result)
            info.save()
            callback.onGetMangaInfoFinished(info)
        } else {
            val info = parseInfo(mangaId, result)
            origin.author = info.author
            origin.categories = info.categories
            origin.views = info.views
            origin.content = info.content
            origin.status = info.status
            origin.save()
            callback.onGetMangaInfoFinished(origin)
        }
    }

    private fun parseInfo(mangaId: Int, result: Element): MangaInfo {
        val elements = result.select("div#manga_detail.info_more > ul > li")
        val tag = elements.select("b").eachText()

        return MangaInfo(
                manga_id = mangaId,
                author = (elements[tag.indexOf("Tác giả:")].childNode(1) as TextNode).text(),
                categories = elements[tag.indexOf("Thể loại:")].select("a").outerHtml(),
                views = Regex("[0-9]+").find(result.select("div.info_stats > ul > li")[2].text())?.value.orEmpty(),
                content = (elements[tag.indexOf("Giới thiệu truyện:")].childNode(1) as TextNode).text(),
                status = (elements[tag.indexOf("Trạng thái:")].childNode(1) as TextNode).text())
    }

    override fun getPages(chapHref: String, callback: GetDataCallback) {
        if (Utils.isOnline()) {
            LoadSite(baseUrl + chapHref, object : LoadSite.Callback {
                val pages = mutableListOf<Page>()
                override fun onFinished(result: Element) {
                    val list = result.select("img#manga_page")
                    list.map { it.attr("src") }
                            .mapTo(pages) { Page(chap_id = chapHref.hashCode(), src = it) }
                    callback.onGetMangaPageFinished(pages)
                    pages.saveAll()
                }
            }).execute()
        } else {
            val pages = Page().query { equalTo("chap_id", chapHref.hashCode()) }
            callback.onGetMangaPageFinished(pages)
        }
    }

    override fun getSearchedMangas(query: String, page: Int, callback: GetDataCallback) {
        if (Utils.isOnline()) {
            val url = baseUrl + "TimKiem/" + 1 + "/key::" + query
            LoadSite(url, object : LoadSite.Callback {
                val mangas = mutableListOf<Manga>()
                override fun onFinished(result: Element) {
                    val list = result.select("li.browse_result_item")
                    if (list.isNotEmpty())
                        list.removeAt(0)
                    for (item in list) {
                        mangas.add(parseManga(item, page))
                    }
                    callback.onSearchMangaFinish(mangas)
                }
            }).execute()
        } else {
            callback.onSearchMangaFinish(null)
        }
    }

}