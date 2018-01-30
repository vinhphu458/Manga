package dvp.manga

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dvp.manga.extensions.getAll
import dvp.manga.extensions.toast
import dvp.manga.extensions.update
import dvp.manga.model.BaseManga
import dvp.manga.model.Manga
import dvp.manga.model.TruyenTranhMoi
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscriber


class MainActivity : AppCompatActivity() {

    lateinit var manga: BaseManga;
    val realm: Realm by lazy { Realm.getDefaultInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        manga = TruyenTranhMoi()
        loadWebSite.setOnClickListener { view->
            view.isEnabled = false
        }
        loadWebSite.setOnClickListener {
            Log.d("START REQ", System.currentTimeMillis().toString())
//            LoadSite("https://truyenqq.com/danh-sach-truyen-tranh.html", this).start()

//            LoadSite(String.format("http://2.truyentranhmoi.com/moi-cap-nhat/page/%d", page), this).start()
//            TruyenTranhMoi().getSearchedMangas("one", 1)
            manga.getMangas(1)
        }

        val list = Manga().getAll(subscriber)
        toast("Size: ${list.size}")

        val test = realm.where(Manga::class.java).equalTo("uuid", "Hôn Trộm 55 Lần".hashCode()).findFirst()
        Log.d("TEST", "OLD: ${test.toString()}")
        Manga().update(Manga("Hôn Trộm 55 Lần", "", "", ""))
        val test2 = realm.where(Manga::class.java).equalTo("uuid", "Hôn Trộm 55 Lần".hashCode()).findFirst()
        Log.d("TEST", "NEW: ${test2.toString()}")

        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, 1)
        }

    }

    val subscriber = Observable.ob
}