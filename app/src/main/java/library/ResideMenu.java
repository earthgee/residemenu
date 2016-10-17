package library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.earthgee.residemenu.R;

/**
 * Created by earthgee on 16/10/16.
 */
public class ResideMenu extends FrameLayout{

    private ImageView mBackGround;
    private LinearLayout menu_container;
    private View contentView;

    private Context context;
    private LayoutInflater inflater;
    private WindowManager windowManager;

    private int touchSlop;
    private int mScreenWidth;
    private int mScreenHeight;

    private float mPivotX;
    private float mPivotY;
    private float scale=0.5f;

    private boolean isOpen=false;

    public ResideMenu(Context context) {
        super(context);
        init(context,null);
    }

    public ResideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ResideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public ResideMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.reside_basic,this);

        mBackGround= (ImageView) findViewById(R.id.background);
        menu_container= (LinearLayout) findViewById(R.id.menu_container);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.ResideMenu);
        if(a.hasValue(R.styleable.ResideMenu_ptr_background)){
            mBackGround.setBackground(a.getDrawable(R.styleable.ResideMenu_ptr_background));
        }
        a.recycle();
        touchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        this.context=context;
        windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth=windowManager.getDefaultDisplay().getWidth();
        mScreenHeight=windowManager.getDefaultDisplay().getHeight();
        mPivotX= mScreenWidth*1.5f;
        mPivotY= mScreenHeight*0.5f;
    }

    //为menu设置内容
    public void setContentView(int layoutId){
        contentView=inflater.inflate(layoutId, null);
        addView(contentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    closeMenu();
                }else{
                    openMenu();
                }
            }
        });
    }

    //设置menu item
    public void addItemMenu(ResideMenuItem resideMenuItem){
        menu_container.addView(resideMenuItem);
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private boolean isBeingDragged;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("earthgee", "onInterceptTouchEvent=" + ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                mLastMotionX=ev.getX();
                mLastMotionY=ev.getY();
                isBeingDragged=false;
                return false;
            }
            case MotionEvent.ACTION_MOVE:{
                if(isBeingDragged) return true;

                float deltaX=ev.getX()-mLastMotionX;
                float deltaY=ev.getY()-mLastMotionY;
                mLastMotionX=ev.getX();
                mLastMotionY=ev.getY();
                if(Math.abs(deltaX)>touchSlop&&Math.abs(deltaX)>Math.abs(deltaY)){
                    isBeingDragged=true;
                    scaleEvent(deltaX);
                    return true;
                }

                return false;
            }
            case MotionEvent.ACTION_UP:{
                isBeingDragged=false;
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("earthgee", "onTouchEvent=" + event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:{
                float deltaX=event.getX()-mLastMotionX;
                scaleEvent(deltaX);
                mLastMotionX=event.getX();
                mLastMotionY=event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP:{
                //处理过滚动
                float currentScale=contentView.getScaleX();
                if(currentScale>0.75){
                    closeMenu();
                    isOpen=false;
                }else{
                    openMenu();
                    isOpen=true;
                }
                isBeingDragged=false;
                return false;
            }
        }
        return false;
    }

    //连续的滑动效果
    private void scaleEvent(float delta){
        //delta<0 向左划 delta>0 向右划
        contentView.setPivotX(mPivotX);
        contentView.setPivotY(mPivotY);
        float targetScaleX=contentView.getScaleX()-(delta/mScreenWidth);
        float targetScaleY=contentView.getScaleY()-(delta/mScreenWidth);

        if(targetScaleX>=1.0f){
            return;
        }else if(targetScaleX<=0.5f){

            return;
        }

        contentView.setScaleX(targetScaleX);
        contentView.setScaleY(targetScaleY);
    }

    //显示菜单
    private void openMenu(){
        ObjectAnimator scaleXAnimator=ObjectAnimator.ofFloat(contentView,"scaleX",scale);
        ObjectAnimator scaleYAnimator=ObjectAnimator.ofFloat(contentView,"scaleY",scale);
        contentView.setPivotX(mPivotX);
        contentView.setPivotY(mPivotY);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(250);
        animatorSet.playTogether(scaleXAnimator,scaleYAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isOpen=true;
            }
        });
        animatorSet.start();
    }

    //关闭菜单
    private void closeMenu(){
        ObjectAnimator scaleXAnimator=ObjectAnimator.ofFloat(contentView,"scaleX",1.0f);
        ObjectAnimator scaleYAnimator=ObjectAnimator.ofFloat(contentView,"scaleY",1.0f);
        contentView.setPivotY(mPivotY);
        contentView.setPivotX(mPivotX);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(250);
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isOpen=false;
            }
        });
        animatorSet.start();
    }

}
