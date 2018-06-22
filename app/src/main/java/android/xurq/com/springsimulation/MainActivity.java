package android.xurq.com.springsimulation;

import android.support.animation.SpringAnimation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    //XY坐标
    private float downX, downY;
    //调整参数的SeekBar
    private SeekBar dampingSeekBar, stiffnessSeekBar;
    //X/Y方向速度相关的帮助类
    private VelocityTracker velocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stiffnessSeekBar = findViewById(R.id.stiffness);
        dampingSeekBar = findViewById(R.id.damping);
        velocityTracker = VelocityTracker.obtain();
        View box = findViewById(R.id.box);
        View content = findViewById(R.id.content);
        content.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    velocityTracker.addMovement(event);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    box.setTranslationX(event.getX() - downX);
                    box.setTranslationY(event.getY() - downY);
                    velocityTracker.addMovement(event);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    content.performClick();
                    velocityTracker.computeCurrentVelocity(1000);
                    if (box.getTranslationX() != 0) {
                        SpringAnimation animX = new SpringAnimation(box, SpringAnimation.TRANSLATION_X, 0);
                        animX.getSpring().setStiffness(getStiffnessSeekBar());
                        animX.getSpring().setDampingRatio(getDampingSeekBar());
                        animX.setStartVelocity(velocityTracker.getXVelocity());
                        animX.start();
                    }
                    if (box.getTranslationY() != 0) {
                        SpringAnimation animY = new SpringAnimation(box, SpringAnimation.TRANSLATION_Y, 0);
                        animY.getSpring().setStiffness(getStiffnessSeekBar());
                        animY.getSpring().setDampingRatio(getDampingSeekBar());
                        animY.setStartVelocity(velocityTracker.getYVelocity());
                        animY.start();
                    }
                    velocityTracker.clear();
                    return true;
            }
            return false;
        });
    }

    /**
     * 从SeekBar获取自定义的强度
     *
     * @return 强度float
     */
    private float getStiffnessSeekBar() {
        return Math.max(stiffnessSeekBar.getProgress(), 1f);
    }

    /**
     * 从SeekBar获取自定义的阻尼
     *
     * @return 阻尼float
     */
    private float getDampingSeekBar() {
        return dampingSeekBar.getProgress() / 100f;
    }
}
