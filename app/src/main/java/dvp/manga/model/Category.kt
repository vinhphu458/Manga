package dvp.manga.model

import android.annotation.SuppressLint
import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * @author Zero on 2/13/2018.
 */
@SuppressLint("ParcelCreator")
@RealmClass
open class Category(var href: String? = null, var title: String? = null) : RealmModel {
    override fun toString(): String {
        return String.format("title: %s, href: %s}", title, href)
    }
}