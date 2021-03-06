package io.customerly.utils.ui

/*
 * Copyright (C) 2017 Customerly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import io.customerly.R
import io.customerly.sxdependencies.SXContextCompat
import io.customerly.sxdependencies.SXRecyclerView
import io.customerly.sxdependencies.SXRecyclerViewItemDecoration
import io.customerly.sxdependencies.SXRecyclerViewLayoutParams
import io.customerly.sxdependencies.annotations.SXColorInt
import io.customerly.sxdependencies.annotations.SXColorRes
import io.customerly.sxdependencies.annotations.SXIntDef
import io.customerly.utils.ggkext.dp2px
import kotlin.math.max

/**
 * Created by Gianni on 24/06/15.
 * Project: Customerly Android SDK
 */

//internal const val RVDIVIDER_H_LEFT = 0
//internal const val RVDIVIDER_H_CENTER = 1
//internal const val RVDIVIDER_H_RIGHT = 2
//internal const val RVDIVIDER_H_BOTH = 3
//
//@SXIntDef(RVDIVIDER_H_LEFT, RVDIVIDER_H_CENTER, RVDIVIDER_H_RIGHT, RVDIVIDER_H_BOTH)
//@Retention(AnnotationRetention.SOURCE)
//internal annotation class RvDividerHorizontal

internal const val RVDIVIDER_V_TOP = 0
internal const val RVDIVIDER_V_CENTER = 1
internal const val RVDIVIDER_V_BOTTOM = 2
internal const val RVDIVIDER_V_BOTH = 3

@SXIntDef(RVDIVIDER_V_TOP, RVDIVIDER_V_CENTER, RVDIVIDER_V_BOTTOM, RVDIVIDER_V_BOTH)
@Retention(AnnotationRetention.SOURCE)
internal annotation class RvDividerVertical

private val dp1 = max(1f, 1f.dp2px)

@SXColorRes
private val DEFAULT_COLOR_RES = R.color.io_customerly__grey_cc

internal sealed class RvDividerDecoration(@SXColorInt colorInt: Int) : SXRecyclerViewItemDecoration() {
    protected val paint : Paint = Paint().apply {
        color = colorInt
        style = Paint.Style.FILL
    }

    internal class Vertical(
            @SXColorInt colorInt: Int,
            @RvDividerVertical private val where : Int = RVDIVIDER_V_CENTER
    ) : RvDividerDecoration(colorInt = colorInt) {

        internal constructor(
                context: Context,
                @SXColorRes colorRes: Int = DEFAULT_COLOR_RES,
                @RvDividerVertical where : Int = RVDIVIDER_V_CENTER
        ): this(colorInt = SXContextCompat.getColor(context, colorRes), where = where)

        override fun onDrawOver(c: Canvas, parent: SXRecyclerView) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            (0 until if (this.where == RVDIVIDER_V_CENTER) {
                parent.childCount - 1
            } else {
                parent.childCount
            })
                    .asSequence()
                    .map { parent.getChildAt(it) }
                    .forEach { child ->
                        when (this.where) {
                            RVDIVIDER_V_BOTTOM, RVDIVIDER_V_CENTER -> {
                                val top = child.bottom + (child.layoutParams as SXRecyclerViewLayoutParams).bottomMargin
                                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), top + dp1, this.paint)
                            }
                            RVDIVIDER_V_BOTH -> {
                                var top = child.bottom + (child.layoutParams as SXRecyclerViewLayoutParams).bottomMargin
                                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), top + dp1, this.paint)
                                top = child.top - (child.layoutParams as SXRecyclerViewLayoutParams).topMargin
                                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), top + dp1, this.paint)
                            }
                            /*RVDIVIDER_V_TOP,*/else -> {
                                val top = child.top - (child.layoutParams as SXRecyclerViewLayoutParams).topMargin
                                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), top + dp1, this.paint)
                            }
                        }
                    }
        }
    }

//    internal class Horizontal(
//            @SXColorInt colorInt: Int,
//            @RvDividerHorizontal private val where : Int = RVDIVIDER_H_CENTER
//    ) : RvDividerDecoration(colorInt = colorInt) {
//
//        internal constructor(
//                context: Context,
//                @SXColorRes colorRes: Int = DEFAULT_COLOR_RES,
//                @RvDividerHorizontal where : Int = RVDIVIDER_H_CENTER
//        ): this(colorInt = ContextCompat.getColor(context, colorRes), where = where)
//
//        override fun onDrawOver(c: Canvas, parent: RecyclerView) {
//            val top = parent.paddingTop
//            val bottom = parent.height - parent.paddingBottom
//
//            (0 until if (this.where == RVDIVIDER_H_CENTER) {
//                parent.childCount - 1
//            } else {
//                parent.childCount
//            })
//                    .asSequence()
//                    .map { parent.getChildAt(it) }
//                    .forEach { child ->
//                        when (this.where) {
//                            RVDIVIDER_H_RIGHT, RVDIVIDER_H_CENTER -> {
//                                val left = child.right + (child.layoutParams as SXRecyclerViewLayoutParams).rightMargin
//                                c.drawRect(left.toFloat(), top.toFloat(), left + dp1, bottom.toFloat(), this.paint)
//                            }
//                            RVDIVIDER_H_BOTH -> {
//                                var left = child.right + (child.layoutParams as SXRecyclerViewLayoutParams).rightMargin
//                                c.drawRect(left.toFloat(), top.toFloat(), left + dp1, bottom.toFloat(), this.paint)
//                                left = child.left - (child.layoutParams as SXRecyclerViewLayoutParams).leftMargin
//                                c.drawRect(left.toFloat(), top.toFloat(), left + dp1, bottom.toFloat(), this.paint)
//                            }
//                            /*RVDIVIDER_H_LEFT,*/else -> {
//                                val left = child.left - (child.layoutParams as SXRecyclerViewLayoutParams).leftMargin
//                                c.drawRect(left.toFloat(), top.toFloat(), left + dp1, bottom.toFloat(), this.paint)
//                            }
//                        }
//                    }
//        }
//    }
}