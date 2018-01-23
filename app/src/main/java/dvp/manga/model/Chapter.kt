package dvp.manga.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * @author Zero on 1/21/2018.
 */
@RealmClass
open class Chapter(open var title: String? = null, open var href: String? = null) : RealmModel {
    override fun toString(): String {
        return String.format("title: %s, href: %s}", title, href)
    }
}