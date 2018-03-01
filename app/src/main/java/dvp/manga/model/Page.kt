package dvp.manga.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * @author Zero on 1/21/2018.
 */
@RealmClass
open class Page(var chap_id: Int = 0, var src: String? = null) : RealmModel {
    @PrimaryKey
    var id: Int = src?.hashCode() ?: 0

    override fun toString(): String {
        return String.format("title: %s, src: %s}", chap_id, src)
    }
}