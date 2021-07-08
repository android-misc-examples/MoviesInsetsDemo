package com.github.razir.movies

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.razir.movies.utils.GridItemDecorator
import com.github.razir.movies.utils.getTestData
import kotlinx.android.synthetic.main.activity_movies.*


class MoviesActivity : Activity() {

    lateinit var paddingManager: PaddingManager
    lateinit var viewList: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)

        paddingManager = PaddingManager(this)

        setContentView(R.layout.activity_movies)

        initView()
        viewList = listOf(moviesRecyclerView,hideActionBarButton)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            moviesRootLayout.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        paddingManager.registerDisplayListener(viewList)
    }

    private fun initView() {
        moviesRecyclerView.apply {
            addItemDecoration(GridItemDecorator(ContextCompat.getDrawable(context, R.drawable.movies_divider)!!))
            layoutManager = GridLayoutManager(context, 2)
            adapter = MoviesAdapter(getTestData())
        }
    }

    fun toggleActionBar(unused: View?) {
        PaddingManager.hidden = !PaddingManager.hidden

        paddingManager.adjustPaddings(viewList)
    }
}
