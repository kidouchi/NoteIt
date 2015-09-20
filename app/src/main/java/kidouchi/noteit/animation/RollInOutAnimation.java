package kidouchi.noteit.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by iuy407 on 9/19/15.
 */
public class RollInOutAnimation {

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;

    public RollInOutAnimation(Context context) {
        this.mContext = context;
        this.mDisplayMetrics = mContext.getResources().getDisplayMetrics();
    }

    public AnimatorSet rollOutFirst(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX",
                0f, mDisplayMetrics.widthPixels * 0.12f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(200);
        set.playTogether(rotate, translateX);
        return set;
    }

    public AnimatorSet rollInFirst(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX",
                mDisplayMetrics.widthPixels * 0.12f, 0f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 360f, 0f);

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(200);
        set.playTogether(rotate, translateX);
        return set;
    }

    public AnimatorSet rollOutSecond(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX",
                0f, mDisplayMetrics.widthPixels * 0.24f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 720f);

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(450);
        set.playTogether(rotate, translateX);
        return set;
    }

    public AnimatorSet rollInSecond(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX",
                mDisplayMetrics.widthPixels * 0.24f, 0f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 720f, 0f);

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(450);
        set.playTogether(rotate, translateX);
        return set;
    }

    public AnimatorSet rollOutThird(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX",
                0f, mDisplayMetrics.widthPixels * 0.36f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 1080f);

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(700);
        set.playTogether(rotate, translateX);
        return set;
    }

    public AnimatorSet rollInThird(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX",
                mDisplayMetrics.widthPixels * 0.36f, 0f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 1080f, 0f);

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(700);
        set.playTogether(rotate, translateX);
        return set;
    }

}
