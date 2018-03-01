package dvp.manga.model

import android.annotation.SuppressLint
import android.os.Parcelable
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

/**
 * @author Zero on 1/21/2018.
 */
@SuppressLint("ParcelCreator")
@RealmClass
@Parcelize
open class Chapter(var title: String? = null, var href: String? = null, var post_date: String? = null, var manga_id: Int = 0) : RealmModel, Parcelable {

    @PrimaryKey
    var id: Int = href?.hashCode() ?: 0

    override fun toString(): String {
        return String.format("title: %s, href: %s}", title, href)
    }
}