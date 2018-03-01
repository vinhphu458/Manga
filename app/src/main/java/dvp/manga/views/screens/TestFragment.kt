package dvp.manga.views.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * @author Zero on 1/27/2018.
 */
class TestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout resource that'll be returned
//        val rootView = inflater.inflate(R.layout.test_layout, container, false)
//        text_view.text = "jhjj"
        return null
    }


    companion object {
        fun getInstance(postion: Int): TestFragment {
            val f = TestFragment()
            val args = Bundle()
            f.arguments = args
            args.putString("position", "Page " + postion)
            return f
        }
    }
}