package dvp.manga.common

import android.os.AsyncTask
import com.vicpin.krealmextensions.equalToValue
import com.vicpin.krealmextensions.querySorted
import com.vicpin.krealmextensions.saveAll
import dvp.manga.GetDataCallback
import dvp.manga.model.Manga
import io.realm.Sort

/**
 * @author Zero on 2/12/2018.
 */
object RealmTasker {

    class SaveDBTask(val list: List<Manga>) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            list.saveAll()
            return null
        }
    }

    class QueryTask(private val callback: GetDataCallback, private val page: Int) : AsyncTask<Void, Void, List<Manga>>() {
        override fun doInBackground(vararg params: Void?): List<Manga> {
            return Manga().querySorted("updated", Sort.ASCENDING) { equalToValue("page", page) }
        }

        override fun onPostExecute(result: List<Manga>?) {
            callback.onGetMangaFinished(result!!)
        }
    }
}