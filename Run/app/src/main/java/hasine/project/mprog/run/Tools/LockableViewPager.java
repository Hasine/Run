package hasine.project.mprog.run.Tools;

/*
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * This pager allows the tab layout to be swipeable or not.
 */
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LockableViewPager extends ViewPager {

    private static boolean swipeable = true;

    public LockableViewPager(Context context) {
        super(context);
    }

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onTouchEvent(event);
        }
        return false;
        }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public static void setSwipeable(boolean swipeable) {

        LockableViewPager.swipeable = swipeable;
    }
}