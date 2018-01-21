package dvp.manga.model

/**
 * @author Zero on 1/21/2018.
 */
data class Page(val title: String, val src: String){
    override fun toString(): String {
        return String.format("title: %s, src: %s}", title, src)
    }
}