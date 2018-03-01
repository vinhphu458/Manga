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
@Parcelize
@RealmClass
open class Manga(var page: Int = 0,
                 var title: String? = null,
                 var cover_url: String? = null,
                 var href: String? = null,
                 var last_chap: String = "0",
                 var updated: Long = 0) : RealmModel, Parcelable {

    @PrimaryKey
    var id: Int = title?.hashCode() ?: 0


    fun getLastChap(): String {
        return last_chap.replace(title!!, "")
    }


//    companion object {
//        val lastNumber = Regex(pattern = "[0-9]+$")
//    }
//
//    init {
//        last_chap = lastNumber.find(last_chap)?.value.orEmpty()
//    }

}
