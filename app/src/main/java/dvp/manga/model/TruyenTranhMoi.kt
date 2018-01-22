package dvp.manga.model

import android.util.Log
import dvp.manga.LoadSite

/**
 * @author Zero on 1/21/2018.
 */
class TruyenTranhMoi : BaseManga() {

    private var baseUrl = "http://2.truyentranhmoi.com/"

    override fun getMangas(page: Int?): List<Manga> {
        val mangas: MutableList<Manga> = mutableListOf()
        val body = LoadSite(baseUrl + "moi-cap-nhat/page/" + page).getBody()
        val list = body.select("div.box.chap-list.truyen-list  > ul").select("li")
        for (item in list) {
            val a = item.select("a")
            val coverUrl = a[0].select("img").attr("src")
            val href = a[1].attr("href")
            val title = a[1].text()
            val lastChap = a[2].text()
            Log.d("TEST", Manga(title, coverUrl, href, lastChap).toString())
//            mangas.add(Manga(title, coverUrl, href, lastChap))
        }
        return mangas
    }

    override fun getChaps(mangaHref: String): List<Chapter> {
        val chaps: MutableList<Chapter> = mutableListOf()
        val body = LoadSite(mangaHref).getBody()
        val list = body.select("div.box.chap-list  > ul").select("li")
        for (item in list) {
            val a = item.select("a")
            val href = a.attr("href")
            val title = a.text()
            chaps.add(Chapter(title, href))
        }
        return chaps
    }

    override fun getPages(chapHref: String): List<Page> {
        val pages: MutableList<Page> = mutableListOf()
        val body = LoadSite(chapHref).getBody()
        val list = body.select("div.image-chap").select("img")
        for (item in list) {
            val title = item.attr("title")
            val src = item.attr("src")
            pages.add(Page(title, src))
        }
        return pages
    }

    override fun getSearchedMangas(query: String, page: Int): List<Manga> {
        val url = baseUrl + "page/" + page + "/?s=" + query
        val mangas: MutableList<Manga> = mutableListOf()
        val body = LoadSite(url).getBody()
        val list = body.select("div.box.chap-list.truyen-list  > ul").select("li")
        for (item in list) {
            val a = item.select("a")
            val coverUrl = a[0].select("img").attr("src")
            val href = a[1].attr("href")
            val title = a[1].text()
            mangas.add(Manga(title, coverUrl, href, ""))
        }
        return mangas
    }

}