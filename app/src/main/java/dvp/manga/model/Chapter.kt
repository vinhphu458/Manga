package dvp.manga.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @author Zero on 1/21/2018.
 */
data class Chapter(@PrimaryKey val id: Int, private val title: String, private val href: String) : RealmObject(){
    override fun toString(): String {
        return String.format("title: %s, href: %s}", title, href)
    }
}