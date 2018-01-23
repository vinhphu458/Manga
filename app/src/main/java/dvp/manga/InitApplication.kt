package dvp.manga

import android.app.Application
import android.os.Environment
import com.vicpin.krealmextensions.RealmConfigStore
import dvp.manga.model.MangaModule
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File


/**
 * @author dvphu on 1/22/2018.
 */

class InitApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val sdCard = Environment.getExternalStorageDirectory()
        val directory = File(sdCard.getAbsolutePath())

        val config = RealmConfiguration.Builder().directory(directory)
                .name("manga.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        RealmConfigStore.initModule(MangaModule::class.java, config)
    }
}