package dvp.manga.model

/**
 * @author Zero on 1/21/2018.
 */
//class TruyenTranhMoi : BaseManga() {
//
//    private var baseUrl = "http://2.truyentranhmoi.com/"
//
//    override fun getMangas(page: Int, callback: OnDownloadDocumentFinish<Manga>) {
//        if (Utils.isOnline()) {
//            LoadSite(baseUrl + "moi-cap-nhat/page/" + page, object : LoadSite.Callback {
//                val mangas = mutableListOf<Manga>()
//                override fun onFinished(result: Element) {
//                    val list = result.select("div.box.chap-list.truyen-list  > ul").select("li")
//                    for (item in list) {
//                        val a = item.select("a")
//                        val coverUrl = a[0].select("img").attr("src")
//                        val href = a[1].attr("href").replace(baseUrl, "")
//                        val title = a[1].text()
//                        val lastChap = a[2].text()
//                        mangas.add(Manga(page = page, title = title, cover_url = coverUrl, href = href, last_chap = lastChap, updated = System.currentTimeMillis()))
//                    }
//                    callback.onFinish(mangas)
//                    RealmTasker.SaveDBTask(mangas).execute()
//                }
//            }).execute()
//        } else {
//            RealmTasker.QueryTask(callback, page).execute()
//        }
//    }
//
//    override fun getChaps(manga: Manga, callback: OnDownloadDocumentFinish<Chapter>) {
//        if (Utils.isOnline()) {
//            LoadSite(baseUrl + manga.href, object : LoadSite.Callback {
//                val chapters = mutableListOf<Chapter>()
//                override fun onFinished(result: Element) {
//                    val list = result.select("div.box.chap-list  > ul").select("li")
//                    for (item in list) {
//                        val a = item.select("a")
//                        val href = a.attr("href").replace(baseUrl, "")
//                        val title = a.text()
//                        val posted_date = item.select("p")[0].text()
//                        chapters.add(Chapter(title = title, href = href, post_date = posted_date))
//                    }
//                    callback.onFinish(chapters)
//                    updateManga(result)
//                }
//            }).execute()
//        }
//    }
//
//    private fun updateManga(result: Element) {
////        val mangaName = result.select("ul.truyen-info.entry-header > li")[1].text()
////        val item = Manga().queryFirst { equalTo("uuid", mangaName.hashCode()) }
////        if (item != null) {
////            item.author = parseAuthor(result)
////            item.categories = parseCategories(result)
////            item.views = parseViews(result)
////            item.content = parseContent(result)
////            item.save()
////        }
//    }
//
//    private fun parseAuthor(result: Element): String {
//        val authors = result.select("ul.truyen-info.entry-header > li")[2].select("span > a")
//        return authors.map { it.text() }.joinToString(", ")
//    }
//
//    private fun parseCategories(result: Element): RealmList<Category> {
//        val list: RealmList<Category> = RealmList()
//        val categories = result.select("ul.truyen-info.entry-header > li")[4].select("a")
//        return categories.mapTo(list) { Category(title = it.text(), href = it.attr("href")) }
//    }
//
//    private fun parseViews(result: Element): String {
//        return Regex("[0-9]+$").find(result.select("ul.truyen-info.entry-header > li")[6].text().replace(",", ""))?.value.orEmpty()
//    }
//
//    private fun parseContent(result: Element): String {
//        return result.select("div.info-toltip").text()
//    }
//
//    override fun getPages(chapHref: String, callback: OnDownloadDocumentFinish<Page>) {
//        if (Utils.isOnline()) {
//            LoadSite(baseUrl + chapHref, object : LoadSite.Callback {
//                val pages = mutableListOf<Page>()
//                override fun onFinished(result: Element) {
//                    val list = result.select("div.image-chap").select("img")
//                    for (item in list) {
//                        val title = item.attr("title")
//                        val src = item.attr("src")
////                        pages.add(Page(title = title, src = src))
//                    }
//                    callback.onFinish(pages)
//                }
//            }).execute()
//        }
//    }
//
//    override fun getSearchedMangas(query: String, page: Int, callback: OnDownloadDocumentFinish<Manga>) {
//        if (Utils.isOnline()) {
//            val url = baseUrl + "page/" + page + "/?s=" + query
//            LoadSite(url, object : LoadSite.Callback {
//                val mangas = mutableListOf<Manga>()
//                override fun onFinished(result: Element) {
//                    val list = result.select("div.box.chap-list.truyen-list  > ul").select("li")
//                    for (item in list) {
//                        val a = item.select("a")
//                        val coverUrl = a[0].select("img").attr("src")
//                        val href = a[1].attr("href")
//                        val title = a[1].text()
//                        mangas.add(Manga())
//                    }
//                    callback.onFinish(mangas)
//                }
//            }).execute()
//        }
//    }
//
//}
