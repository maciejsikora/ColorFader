package maciejsikora.com.colorfader;

/**
 * Created by Maciej Sikora maciejsikora.com on 11.08.16.
 */

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import java.util.ArrayList;


/**
 * Color background fader
 * Created by Maciej Sikora
 */
public class ColorFader {


    private Integer currentColorPos;//current color position in arraylist
    private final View view;//view in which we change background

    private int from_color;//current from color to animate
    private int to_color;//current to color to animate

    private int duration=1000;//default iterval
    private ValueAnimator colorLoopAnimation;
    private ArrayList<Integer> colors;

    public boolean isLoopOn() {
        return loopOn;
    }

    private boolean loopOn=false;//is looping turned on

    private TypeEvaluator evaluator=new ArgbEvaluator();//default evaluator

    public ColorFader (ArrayList<Integer> colors, View view){

        this.colors=colors;
        this.currentColorPos=0;
        this.view=view;

    }


    public ColorFader setEvaluator(TypeEvaluator e){

        evaluator=e;

        return this;
    }

    /**
     * Optional evaluator
     * Special thanks for https://github.com/mikailsheikh/cogitolearning-examples/tree/master/PropertyAnimations
     */
    public static class HsvEvaluator implements TypeEvaluator<Integer> {
        public Integer evaluate(float fraction,
                                Integer startValue,
                                Integer endValue) {
            float[] startHsv = new float[3];
            float[] endHsv = new float[3];
            float[] currentHsv = new float[3];

            Color.colorToHSV(startValue, startHsv);
            Color.colorToHSV(endValue, endHsv);

            for (int i=0; i<3; i++)
                currentHsv[i] = (1-fraction)*startHsv[i] + fraction*endHsv[i];

            while (currentHsv[0]>=360.0f) currentHsv[0] -= 360.0f;
            while (currentHsv[0]<0.0f) currentHsv[0] += 360.0f;

            return Color.HSVToColor(currentHsv);
        }
    }


    /**
     * Add new color to collection
     * @param color
     * @return
     */
    public ColorFader addColor(Integer color){

        this.colors.add(color);

        return this;
    }

    /**
     * Starts color changing in time
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ColorFader startLoop(){

        if (loopOn)
        return this; //loop is on


        this.loopOn=true;

        runOrResumeAnimation();


        return this;
    }

    public ColorFader endLoop(){

        colorLoopAnimation.cancel();
        this.loopOn=false;

        return this;
    }

    /**
     * Ends color changing in time
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ColorFader stopLoop(){

        colorLoopAnimation.pause();
        this.loopOn=false;
        return this;
    }

    /**
     * fade to color position
     * using method stops looping
     * @param position
     * @return
     */
    public ColorFader fadeToColor(int position){

        stopLoop();


        to_color= colors.get(position);
        currentColorPos=position;

        runAnimation();

        return this;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void runOrResumeAnimation(){

        if (colorLoopAnimation!=null && colorLoopAnimation.isPaused())
            colorLoopAnimation.resume(); //was paused only
        else
            runLoop();//run
    }

    /**
     * initialise loop animation
     */
    private void runAnimation(){

        colorLoopAnimation = ValueAnimator.ofObject(evaluator, from_color,to_color);

        colorLoopAnimation.setDuration(this.duration);

        colorLoopAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });

        colorLoopAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (!loopOn) {
                    positionCalculating();
                    return;
                }

                runLoop();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        colorLoopAnimation.start();

    }


    private void positionCalculating(){

        if (currentColorPos==null)
            currentColorPos=0;

        from_color=colors.get(currentColorPos);

        if (currentColorPos+1==colors.size())
        currentColorPos=0;
        else
            currentColorPos++;

        to_color=colors.get(currentColorPos);

    }

    private void runLoop(){

        positionCalculating();
        runAnimation();

    }

    public ColorFader setDuration( int ms){

        this.duration=ms;

        return this;
    }

}

