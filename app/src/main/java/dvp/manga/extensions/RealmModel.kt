package dvp.manga.extensions

/**
 * @author dvphu on 1/24/2018.
 */

//fun <T : RealmModel> T.save() {
//    Realm.getDefaultInstance().executeTransaction { realm ->
//        realm.copyToRealmOrUpdate(this)
//    }
//}
//
//fun <T : RealmModel> T.update(item: T) {
//    Realm.getDefaultInstance().executeTransaction { realm ->
//        realm.copyToRealmOrUpdate(item)
//    }
//}
//
//inline fun <reified T : RealmModel> T.getAll(): List<T> {
//    var list: List<T> = mutableListOf()
//    Realm.getDefaultInstance().executeTransaction { realm ->
//        list = realm.where(T::class.java).findAll()
//    }
//    return list
//}
