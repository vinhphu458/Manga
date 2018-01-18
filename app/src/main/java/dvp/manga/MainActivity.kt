package dvp.manga

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.nodes.Document

class MainActivity : AppCompatActivity(), OnLoadPageListener {

    override fun onFinish(doc: Document) {
        Log.d("TEST", doc.body().text())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadWebSite.setOnClickListener {
            LoadSite("http://truyentranhpro.com", this).start()
        }
    }
}
