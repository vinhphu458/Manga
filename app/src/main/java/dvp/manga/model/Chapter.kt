package dvp.manga.model

/**
 * @author Zero on 1/21/2018.
 */
data class Chapter(private val title: String, private val href: String){
    override fun toString(): String {
        return String.format("title: %s, href: %s}", title, href)
    }
}