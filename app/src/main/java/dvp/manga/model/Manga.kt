package dvp.manga.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * @author Zero on 1/21/2018.
 */

@RealmClass
open class Manga(var title: String? = null,
                 var cover_url: String? = null,
                 var href: String? = null,
                 var last_chap: String? = null) : RealmModel {

    override fun toString(): String {
        return String.format("title: %s, href: %s, cover: %s, last_chap: %s}", title, href, cover_url, last_chap)
    }
}
