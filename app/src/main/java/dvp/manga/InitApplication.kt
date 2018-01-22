package dvp.manga

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * @author dvphu on 1/22/2018.
 */

class InitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("manga.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}