package dvp.manga

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * @author Zero on 1/18/2018.
 */
class LoadSite(private val url: String, private val callback: Callback) : AsyncTask<String, Void, Element>() {
    override fun doInBackground(vararg params: String?): Element {
        return Jsoup.connect(url).get().body()
    }

    override fun onPostExecute(result: Element?) {
        callback.onFinished(result!!)
    }

    interface Callback {
        fun onFinished(result: Element)
    }
}