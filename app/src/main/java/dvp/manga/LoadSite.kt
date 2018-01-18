package dvp.manga

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * @author Zero on 1/18/2018.
 */
class LoadSite(private val url:String, private val callback: OnLoadPageListener) {

    fun start() {
        val doc = GetWebsiteTask().execute(url).get()
        callback.onFinish(doc)
    }

    private class GetWebsiteTask : AsyncTask<String, Void, Document>() {
        override fun doInBackground(vararg params: String?): Document? {
            return Jsoup.connect(params[0]).get()
        }
    }
}