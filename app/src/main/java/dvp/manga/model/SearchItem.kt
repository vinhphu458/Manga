package dvp.manga.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * @author Zero on 2/25/2018.
 */

@RealmClass
open class SearchItem(var name: String? = null) : RealmModel {
    @PrimaryKey
    var id = name?.hashCode() ?: 0

    var time_tamp = System.currentTimeMillis()
}