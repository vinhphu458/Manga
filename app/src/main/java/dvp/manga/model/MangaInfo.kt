package dvp.manga.model

import android.annotation.SuppressLint
import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * @author Zero on 2/13/2018.
 */
@SuppressLint("ParcelCreator")
@RealmClass
open class MangaInfo(var manga_id: Int = 0,
                     var author: String? = null,
                     var content: String? = null,
                     var categories: String? = null,
                     var views: String? = "0",
                     var status: String? = "") : RealmModel