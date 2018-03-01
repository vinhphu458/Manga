package dvp.manga

import android.app.Application
import android.content.Context
import android.os.Environment
import dvp.manga.model.MangaModule
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File


/**
 * @author dvphu on 1/22/2018.
 */

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun context(): Context {
            return instance!!.applicationContext
        }
    }


    override fun onCreate() {
        super.onCreate()
        initRealm()
    }


    private fun initRealm() {
        Realm.init(this)
        val sdCard = Environment.getExternalStorageDirectory()
        val directory = File(sdCard.absolutePath)
        val config = RealmConfiguration.Builder()
//                .directory(directory)
                .name("manga.realm")
                .schemaVersion(8)
                .modules(MangaModule())
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}