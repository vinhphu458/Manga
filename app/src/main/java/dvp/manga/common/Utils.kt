package dvp.manga.common

/**
 * @author dvphu on 1/24/2018.
 */
object Utils {
    fun hash(string: String?): Int {
        return string?.hashCode() ?: 0
    }
}