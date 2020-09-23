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
import android.util.TypedValue
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
        setupInsets()

        Thread {
            Thread.sleep(10000)
            runOnUiThread {
                hidden = !hidden
                val x = intent
                finish()
                startActivity(x)
            }
        }.start()
    }

    fun getInsetPaddingFn(baseMoviesPadding: Int): (View, WindowInsetsCompat) -> WindowInsetsCompat {
        if(!hidden)
            return {view: View, insets: WindowInsetsCompat ->
                moviesRecyclerView.updatePadding(
                    top = insets.systemWindowInsetTop + baseMoviesPadding,
                    bottom = insets.systemWindowInsetBottom)
//                hideActionBarButton.updatePadding(
//                    top = insets.systemWindowInsetTop + baseMoviesPadding,
//                    bottom = insets.systemWindowInsetBottom)
                insets}
        else
            return {view: View, insets: WindowInsetsCompat ->
                moviesRecyclerView.updatePadding(
                    top = baseMoviesPadding,
                    bottom = insets.systemWindowInsetBottom)
//                hideActionBarButton.updatePadding(
//                    top = insets.systemWindowInsetTop + baseMoviesPadding,
//                    bottom = insets.systemWindowInsetBottom)
                insets}
    }

    private fun setupInsets() {
        val baseMoviesPadding = pxFromDp(24f)
        var toolbarHeight = 0

        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            toolbarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            moviesRootLayout.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        ViewCompat.setOnApplyWindowInsetsListener(moviesRecyclerView, getInsetPaddingFn(baseMoviesPadding))
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

    fun toggleActionBar() {
        if(hidden)
            supportActionBar?.show()
        else
            supportActionBar?.hide()
        hidden = !hidden
    }

    companion object {
        var hidden = true
    }
}
