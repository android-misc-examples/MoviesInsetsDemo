package com.github.razir.movies

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.razir.movies.utils.GridItemDecorator
import com.github.razir.movies.utils.getTestData
import kotlinx.android.synthetic.main.activity_movies.*
import android.view.Window
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

        hiddenInsetsPadding = mutableMapOf("top" to moviesRecyclerView.paddingTop + pxFromDp(24f),
                                        "bottom" to moviesRecyclerView.paddingBottom + pxFromDp(48f))
        actionBarInsetsPadding = mutableMapOf("top" to moviesRecyclerView.paddingTop + pxFromDp(81f),
                                        "bottom" to moviesRecyclerView.paddingBottom + pxFromDp(48f))

        hidden = !hidden
        toggleActionBar(null)
    }

    var hiddenInsetsPadding : MutableMap<String, Int> = mutableMapOf()
    var actionBarInsetsPadding : MutableMap<String, Int> = mutableMapOf()

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            listOf(hideActionBarButton, moviesRecyclerView).forEach{
                if(hidden)
                    it.updatePadding(top=hiddenInsetsPadding["top"]!!,bottom=hiddenInsetsPadding["bottom"]!!)
                else
                    it.updatePadding(top=actionBarInsetsPadding["top"]!!,bottom=actionBarInsetsPadding["bottom"]!!)
            }
    }

    companion object {
        var hidden = true
    }
}
