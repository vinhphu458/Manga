package dvp.manga.model

/**
 * @author Zero on 1/21/2018.
 */
data class Manga(val title: String, val cover_url: String, val href: String, val last_chap: String)  {
    override fun toString(): String {
        return String.format("title: %s, href: %s, cover: %s, last_chap: %s}", title, href, cover_url, last_chap)
    }
}
