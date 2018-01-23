package com.zongke.hapilolauncher.window;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.applist.AppListActivity;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class SuspensionWindowLayout extends RelativeLayout {
    private Context context;
    private int layoutId;
    private static final int defaultId = R.layout.item_message;
    public static int widget_width = 0;
    public static int widget_height = 0;
    /**
     * statusbar系统状态栏的高度
     */
    private int statusbarHeight = 0;
    /**
     * 窗口管理器
     */
    public WindowManager windowManager;
    public View childView;


    public SuspensionWindowLayout(Context context, int defaultLayoutId) {
        super(context);
        this.context = context;
        this.layoutId = defaultLayoutId != 0 ? defaultLayoutId : defaultId;
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE) ;
        createChildView();
    }

    private void createChildView() {
        childView= View.inflate(context,layoutId,this);
        if (layoutId==defaultId){
            widget_width = childView.findViewById(R.id.suspension_window_layout).getLayoutParams().width;
            widget_height = childView.findViewById(R.id.suspension_window_layout).getLayoutParams().height;
        }else{
            widget_width=childView.findViewById(R.id.window_dialog_test_layout).getLayoutParams().width;
            widget_height=childView.findViewById(R.id.window_dialog_test_layout).getLayoutParams().height;
        }
    }

    public void  showSpecifiedContent(String content){
       TextView textView=childView.findViewById(R.id.window_dialog_test_layout_show_tv);
                textView.setText(content);
    }
    /**
     *    按下屏幕时手指在x,y轴上的坐标
     */
    private  float down_x = 0.0f;
    private  float  down_y = down_x;
    /**
     *    移动时候的手指在x,y轴上的坐标
     */
    private  float move_x = down_x;
    private  float  move_y = down_x;
    /**
     * 按下屏幕时候，控件在x，y轴位置
     */
    private  float widget_x = down_x;
    private  float widget_y = down_x;
    /**
     * 重写处理拖动事件
     */
    @Override
     public boolean onTouchEvent( MotionEvent event) {
        switch (event.getAction()) {
            case  MotionEvent.ACTION_DOWN :{
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                widget_x = event.getX();
                widget_y = event.getX();
                //没有移动，down->up，点击事件
                down_x = event.getRawX();
                down_y = event.getRawY() - getStatusBarHeight();
                move_x = event.getRawX();
                move_y = event.getRawY() - getStatusBarHeight();
            }
            case  MotionEvent.ACTION_MOVE :{
                // 手指移动的时候更新小悬浮窗的位置
                move_x = event.getRawX();
                move_y = event.getRawY() - getStatusBarHeight();
                updateWidgetPosition();
            }
            case  MotionEvent.ACTION_UP : {//
                //坐标没有改变，是点击动作
                if (move_x == down_x && move_y == down_y) {
                    AppListActivity.openActivity(context);
                    SuspensionWindowManagerUtils.removeSuspensionWindow(context);
                }
            }
            default:
                break;

        }
        return true;
    }

    /**
     * 更新控件位置,在x，y轴的的位置
     */
    public void  updateWidgetPosition() {
        WindowManager.LayoutParams   layoutParams = SuspensionWindowManagerUtils.getWidgetLayoutParams();
        layoutParams.x = (int) (move_x - widget_x);
        layoutParams.y = (int) (move_y - widget_y);
        windowManager.updateViewLayout(this, layoutParams);

    }




    /**
     * 获取系统状态栏，返回状态栏高度的像素值
     */
    int getStatusBarHeight() {
        if (statusbarHeight == 0) {
            statusbarHeight = getResources().getDimensionPixelSize(com.zongke.hapilolauncher.utils.ViewUtils.getStatusBarHeight());
        }
        return statusbarHeight;
    }
}
