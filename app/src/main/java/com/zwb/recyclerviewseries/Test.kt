package com.zwb.recyclerviewseries

import androidx.annotation.IntDef

/**
 * @ author : zhouweibin
 * @ time: 2019/10/22 16:31.
 * @ desc:
 **/
public class Test {

    companion object {

        @IntDef(SLOW, NORMAL, FAST)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Speed

        const val SLOW = 0
        const val NORMAL = 1
        const val FAST = 2
    }

    @Speed
    private var speed: Int=SLOW

    public fun setSpeed(@Speed speed: Int) {
        this.speed = speed
    }
}