package dvp.manga

import org.jsoup.nodes.Document

/**
 * @author Zero on 1/18/2018.
 */
interface OnLoadPageListener {
    fun onFinish(doc: Document)
}