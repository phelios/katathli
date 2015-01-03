package com.cyros.phelios.katathli;

import android.app.Activity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by phelios on 12/29/14.
 */
public class Helper {

    private Activity context;

    public Helper(Activity context){
        this.context = context;
    }

    public void flashNotify(String notification){
        final TextView flashArea = (TextView) context.findViewById(R.id.flashNotification);
        flashArea.setText(notification);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);

        Animation.AnimationListener flashEnd = new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {};
            public void onAnimationRepeat(Animation animation) {};
            public void onAnimationEnd(Animation anim){
                flashArea.setText("");
            }
        };

        anim.setAnimationListener(flashEnd);

        flashArea.startAnimation(anim);

    }
}
