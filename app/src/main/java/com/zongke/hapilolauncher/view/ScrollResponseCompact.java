package com.zongke.hapilolauncher.view;

/**
 * Created by ${xingen} on 2017/9/7.
 */

/**
 * 滚动相应
 */
public  interface ScrollResponseCompact {

    /**
     * 是否滚动到控件的首端或者尾端。
     *
     * @return
     */
    boolean isScrollBorder();

    /**
     * 当前滚动的模式，可能是最左边，最右边，中间。
     * @return
     */
    int getScrollMode();

    /**
     * 滚动方向，滚动角度
     * @param move_mode
     * @param rotation
     *
     */
   boolean scrollPosition(int move_mode, float rotation);
}