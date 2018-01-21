package dvp.manga

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * @author Zero on 1/18/2018.
 */
class LoadSite(private val url:String) {

    fun getBody(): Element {
        return GetWebsiteTask().execute(url).get()
    }

    private class GetWebsiteTask : AsyncTask<String, Void, Element>() {
        override fun doInBackground(vararg params: String?): Element? {
            return Jsoup.connect(params[0]).get().body()
        }
    }
}