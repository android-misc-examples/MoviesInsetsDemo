package com.github.razir.movies

import android.app.Activity
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding

class PaddingManager(val activity: Activity) {

    private fun pxFromDp(dp: Float): Int {
        return (dp * activity.resources.displayMetrics.density).toInt()
    }

    fun createMaps() {
        if(insetsPadding.isEmpty()) {
            if(hasSoftKeys()) {
                insetsPadding = mapOf(
                    Surface.ROTATION_0 to
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to pxFromDp(48f),
                                "right" to 0
                            ),
                    Surface.ROTATION_90 to
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to pxFromDp(48f)
                            ),
                    Surface.ROTATION_180 to // not always called, have to enable setting first
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to pxFromDp(48f),
                                "right" to 0
                            ),
                    Surface.ROTATION_270 to
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to pxFromDp(48f),
                                "bottom" to 0,
                                "right" to 0
                            )

                )

                hiddenInsetsPadding = mapOf(
                    Surface.ROTATION_0 to
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to pxFromDp(48f),
                                "right" to 0
                            ),
                    Surface.ROTATION_90 to
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to pxFromDp(48f)
                            ),
                    Surface.ROTATION_180 to // not always called, have to enable setting first
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to pxFromDp(48f),
                                "right" to 0
                            ),
                    Surface.ROTATION_270 to
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to pxFromDp(48f),
                                "bottom" to 0,
                                "right" to 0
                            )
                )
            }
            else {
                insetsPadding = mapOf(
                    Surface.ROTATION_0 to
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            ),
                    Surface.ROTATION_90 to
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            ),
                    Surface.ROTATION_180 to // not always called, have to enable setting first
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            ),
                    Surface.ROTATION_270 to
                            mapOf(
                                "top" to pxFromDp(81f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            )

                )

                hiddenInsetsPadding = mapOf(
                    Surface.ROTATION_0 to
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            ),
                    Surface.ROTATION_90 to
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            ),
                    Surface.ROTATION_180 to // not always called, have to enable setting first
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            ),
                    Surface.ROTATION_270 to
                            mapOf(
                                "top" to pxFromDp(24f),
                                "left" to 0,
                                "bottom" to 0,
                                "right" to 0
                            )
                )
            }
        }
    }

    fun adjustPaddings(views: List<View>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createMaps()

            val degrees = activity.windowManager.defaultDisplay.rotation

            views.forEach {
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

    fun registerDisplayListener(views: List<View>) {
        adjustPaddings(views)

        val dl = object : DisplayManager.DisplayListener {
            override fun onDisplayAdded(p0: Int) {}

            override fun onDisplayRemoved(p0: Int) {}

            override fun onDisplayChanged(p0: Int) {
                Toast.makeText(activity, "Current orientation: "+activity.windowManager.defaultDisplay.rotation, Toast.LENGTH_SHORT).show()
                adjustPaddings(views)
            }
        }

        dm = activity.getSystemService(AppCompatActivity.DISPLAY_SERVICE) as DisplayManager
        dm.registerDisplayListener(dl, h)
    }

    fun hasSoftKeys(): Boolean {
        val d = activity.windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)

        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0
    }

    lateinit var dm: DisplayManager

    companion object {
        var hidden = false
        var insetsPadding : Map<Int, Map<String, Int>> = mapOf()
        var hiddenInsetsPadding : Map<Int, Map<String, Int>> = mapOf()
        val h = Handler()
    }
}