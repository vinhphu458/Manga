package dvp.manga.model

import dvp.manga.GetDataCallback


/**
 * @author dvphu on 1/22/2018.
 */
abstract class BaseManga {
    abstract fun getMangas(page: Int, callback: GetDataCallback)
    abstract fun getMangasByGenre(url:String, page: Int, callback: GetDataCallback)
    abstract fun getChaps(manga: Manga, callback: GetDataCallback)
    abstract fun getPages(chapHref: String, callback: GetDataCallback)
    abstract fun getSearchedMangas(query: String, page: Int, callback: GetDataCallback)
}