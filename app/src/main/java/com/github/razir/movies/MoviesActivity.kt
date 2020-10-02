package com.github.razir.movies

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Surface
import android.view.View
import android.view.View.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.razir.movies.utils.GridItemDecorator
import com.github.razir.movies.utils.getTestData
import kotlinx.android.synthetic.main.activity_movies.*
import android.view.Window
import android.widget.Toast
import androidx.core.view.*


class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.argb(128, 0, 0, 0)))

        if(hidden) supportActionBar?.hide()
        setContentView(R.layout.activity_movies)

        initView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            moviesRootLayout.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        if(insetsPadding.isEmpty()) {
            insetsPadding = mutableMapOf(
                Surface.ROTATION_0 to
                        mutableMapOf(
                            "top" to pxFromDp(81f),
                            "left" to 0,
                            "bottom" to pxFromDp(48f),
                            "right" to 0
                        ),
                Surface.ROTATION_90 to
                        mutableMapOf(
                            "top" to pxFromDp(81f),
                            "left" to 0,
                            "bottom" to 0,
                            "right" to pxFromDp(48f)
                        ),
                Surface.ROTATION_180 to // not always called, have to enable setting first
                        mutableMapOf(
                            "top" to pxFromDp(81f),
                            "left" to 0,
                            "bottom" to pxFromDp(48f),
                            "right" to 0
                        ),
                Surface.ROTATION_270 to
                        mutableMapOf(
                            "top" to pxFromDp(81f),
                            "left" to pxFromDp(48f),
                            "bottom" to 0,
                            "right" to 0
                        )

            )

            hiddenInsetsPadding = mutableMapOf(
                Surface.ROTATION_0 to
                        mutableMapOf(
                            "top" to pxFromDp(24f),
                            "left" to 0,
                            "bottom" to pxFromDp(48f),
                            "right" to 0
                        ),
                Surface.ROTATION_90 to
                        mutableMapOf(
                            "top" to pxFromDp(24f),
                            "left" to 0,
                            "bottom" to 0,
                            "right" to pxFromDp(48f)
                        ),
                Surface.ROTATION_180 to // not always called, have to enable setting first
                        mutableMapOf(
                            "top" to pxFromDp(24f),
                            "left" to 0,
                            "bottom" to pxFromDp(48f),
                            "right" to 0
                        ),
                Surface.ROTATION_270 to
                        mutableMapOf(
                            "top" to pxFromDp(24f),
                            "left" to pxFromDp(48f),
                            "bottom" to 0,
                            "right" to 0
                        )

            )
        }

        if(dm == null) {
            refreshPaddings()

            val dl = object : DisplayManager.DisplayListener {
                override fun onDisplayAdded(p0: Int) {

                }

                override fun onDisplayRemoved(p0: Int) {

                }

                override fun onDisplayChanged(p0: Int) {
                    Toast.makeText(this@MoviesActivity, "Current orientation: "+windowManager.defaultDisplay.rotation, Toast.LENGTH_SHORT).show()
                    refreshPaddings()
                }
            }

            dm = getSystemService(DISPLAY_SERVICE) as DisplayManager
            dm?.registerDisplayListener(dl,h)
        }
    }

    fun refreshPaddings() {
        hidden = !hidden
        toggleActionBar(null)
    }

    private fun initView() {
        moviesRecyclerView.apply {
            addItemDecoration(GridItemDecorator(ContextCompat.getDrawable(context, R.drawable.movies_divider)!!))
            layoutManager = GridLayoutManager(context, 2)
            adapter = MoviesAdapter(getTestData())
        }
    }

    private fun pxFromDp(dp: Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun toggleActionBar(unused: View?) {
        if(hidden)
            supportActionBar?.show()
        else
            supportActionBar?.hide()
        hidden = !hidden

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val degrees = windowManager.defaultDisplay.rotation

            listOf(hideActionBarButton, moviesRecyclerView).forEach {
                if (hidden)
                    it.updatePadding(
                        top = hiddenInsetsPadding[degrees]!!["top"]!!,
                        left = hiddenInsetsPadding[degrees]!!["left"]!!,
                        bottom = hiddenInsetsPadding[degrees]!!["bottom"]!!,
                        right = hiddenInsetsPadding[degrees]!!["right"]!!
                    )
                else
                    it.updatePadding(
                        top = insetsPadding[degrees]!!["top"]!!,
                        left = insetsPadding[degrees]!!["left"]!!,
                        bottom = insetsPadding[degrees]!!["bottom"]!!,
                        right = insetsPadding[degrees]!!["right"]!!
                    )
            }
        }
    }

    companion object {
        var hidden = false
        var insetsPadding : MutableMap<Int, MutableMap<String, Int>> = mutableMapOf()
        var hiddenInsetsPadding : MutableMap<Int, MutableMap<String, Int>> = mutableMapOf()
        val h = Handler()
        var dm: DisplayManager? = null
    }
}
