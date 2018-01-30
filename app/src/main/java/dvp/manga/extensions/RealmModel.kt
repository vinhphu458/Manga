package dvp.manga.extensions

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import org.reactivestreams.Subscriber

/**
 * @author dvphu on 1/24/2018.
 */

fun <T : RealmModel> T.save() {
    Realm.getDefaultInstance().executeTransaction { realm ->
        realm.copyToRealmOrUpdate(this)
    }
}

fun <T : RealmModel> T.update(item: T) {
    Realm.getDefaultInstance().executeTransaction { realm ->
        realm.copyToRealmOrUpdate(item)
    }
}

inline fun <reified T : RealmModel> T.getAll(subscriber: Subscriber<T>) {
    Realm.getDefaultInstance().use { realm ->
        realm.where(T::class.java)
                .findAllAsync()
                .asFlowable()
                .flatMap { item -> Flowable.fromIterable<T>(item) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn()
    }
}

inline fun <reified T : RealmModel> T.loadAll(): Disposable {
    Realm.getDefaultInstance().use { realm ->
        val list = realm.where(T::class.java).findAll()
        return Observable.just(realm.copyFromRealm(list))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

}