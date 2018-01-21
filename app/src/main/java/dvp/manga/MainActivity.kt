package dvp.manga

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dvp.manga.model.TruyenTranhMoi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadWebSite.setOnClickListener {
            Log.d("START REQ", System.currentTimeMillis().toString())
//            LoadSite("https://truyenqq.com/danh-sach-truyen-tranh.html", this).start()

//            LoadSite(String.format("http://2.truyentranhmoi.com/moi-cap-nhat/page/%d", page), this).start()
            TruyenTranhMoi().getSearchedMangas("one", 1)
        }
    }
}
