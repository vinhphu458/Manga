package dvp.manga

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.vicpin.krealmextensions.queryAll
import dvp.manga.model.BaseManga
import dvp.manga.model.Manga
import dvp.manga.model.TruyenTranhMoi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var manga: BaseManga;
    val realm by lazy { Realm.getDefaultInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manga = TruyenTranhMoi()
        loadWebSite.setOnClickListener {
            Log.d("START REQ", System.currentTimeMillis().toString())
//            LoadSite("https://truyenqq.com/danh-sach-truyen-tranh.html", this).start()

//            LoadSite(String.format("http://2.truyentranhmoi.com/moi-cap-nhat/page/%d", page), this).start()
//            TruyenTranhMoi().getSearchedMangas("one", 1)
            manga.getMangas(1)
        }
        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, 1)
        }

        val mangas = Manga().queryAll()
        for(manga in mangas){
            Log.d("TEST", manga.title)
        }
    }
}