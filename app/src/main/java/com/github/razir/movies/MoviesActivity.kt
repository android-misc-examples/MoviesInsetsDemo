package com.github.razir.movies

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.razir.movies.utils.GridItemDecorator
import com.github.razir.movies.utils.getTestData
import kotlinx.android.synthetic.main.activity_movies.*


class MoviesActivity : AppCompatActivity() {

    lateinit var paddingManager: PaddingManager
    lateinit var viewList: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.argb(128, 0, 0, 0)))

        paddingManager = PaddingManager(this)

        if(PaddingManager.hidden) supportActionBar?.hide()
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
        if(PaddingManager.hidden)
            supportActionBar?.show()
        else
            supportActionBar?.hide()
        PaddingManager.hidden = !PaddingManager.hidden

        paddingManager.adjustPaddings(viewList)
    }
}
