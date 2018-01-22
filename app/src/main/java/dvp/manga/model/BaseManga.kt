package dvp.manga.model

/**
 * @author dvphu on 1/22/2018.
 */
abstract class BaseManga {
    abstract fun getMangas(page: Int?): List<Manga>
    abstract fun getChaps(mangaHref: String): List<Chapter>
    abstract fun getPages(chapHref: String): List<Page>
    abstract fun getSearchedMangas(query: String, page: Int): List<Manga>
}