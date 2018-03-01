package dvp.manga

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author Zero on 1/27/2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        setUpViews()
        loadData()
    }

    abstract fun getLayout(): Int

    abstract fun setUpViews()

    abstract fun loadData()

}