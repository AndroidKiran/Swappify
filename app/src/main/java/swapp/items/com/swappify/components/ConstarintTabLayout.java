package swapp.items.com.swappify.components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import swapp.items.com.swappify.R;


public class ConstarintTabLayout extends FrameLayout {

    private ViewPager viewpager;
    private AppCompatImageView appCompatImageView1;
    private AppCompatImageView appCompatImageView2;
    private AppCompatImageView appCompatImageView3;

    public ConstarintTabLayout(Context context) {
        this(context, null, 0);
    }

    public ConstarintTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConstarintTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        appCompatImageView1 = new AppCompatImageView(context);
        appCompatImageView1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vc_add_a_photo));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT| Gravity.START;
        addView(appCompatImageView1, params);


        appCompatImageView2 = new AppCompatImageView(context);
        appCompatImageView2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vc_cross_white));
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        addView(appCompatImageView2, params1);


        appCompatImageView3 = new AppCompatImageView(context);
        appCompatImageView3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vc_arrow_forward));
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.RIGHT| Gravity.END;
        addView(appCompatImageView3, params2);

    }

    public void setViewPager(ViewPager viewPager) {
        this.viewpager = viewPager;
        if (viewpager != null && viewpager.getAdapter() != null) {
            viewpager.removeOnPageChangeListener(internalPageChangeListener);
            viewpager.addOnPageChangeListener(internalPageChangeListener);
            internalPageChangeListener.onPageSelected(viewpager.getCurrentItem());
        }
    }

    private int pageWidth;
    private int pageWidthTimesPosition;
    private int absPosition;

    private final ViewPager.OnPageChangeListener internalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pageWidth = getWidth();
            pageWidthTimesPosition = pageWidth * position;
            absPosition = Math.abs(position);

            appCompatImageView1.setTranslationX(pageWidthTimesPosition / 4f);
            appCompatImageView2.setTranslationX(pageWidthTimesPosition / 4f);
            appCompatImageView3.setTranslationX(pageWidthTimesPosition / 4f);

        }

        @Override
        public void onPageSelected(int position) {
            updateConstraints(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void updateConstraints(int position) {
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams)appCompatImageView1.getLayoutParams();
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams)appCompatImageView2.getLayoutParams();
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams)appCompatImageView3.getLayoutParams();

        /*TransitionManager.beginDelayedTransition(this, new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeImageTransform()));*/

        /*switch (position) {
            case 0:
                layoutParams1.gravity = Gravity.CENTER;
                appCompatImageView1.setLayoutParams(layoutParams1);

                layoutParams2.gravity = Gravity.RIGHT| Gravity.END;
                appCompatImageView2.setLayoutParams(layoutParams2);

                appCompatImageView3.setVisibility(GONE);

                break;

            case 1:

                layoutParams1.gravity = Gravity.LEFT| Gravity.START;
                appCompatImageView1.setVisibility(VISIBLE);
                appCompatImageView1.setLayoutParams(layoutParams1);

                layoutParams2.gravity = Gravity.CENTER;
                appCompatImageView2.setVisibility(VISIBLE);
                appCompatImageView2.setLayoutParams(layoutParams2);

                layoutParams3.gravity = Gravity.RIGHT| Gravity.END;
                appCompatImageView3.setVisibility(VISIBLE);
                appCompatImageView3.setLayoutParams(layoutParams3);

                break;

            case 2:
                appCompatImageView1.setVisibility(GONE);

                layoutParams2.gravity = Gravity.LEFT| Gravity.START;
                appCompatImageView2.setLayoutParams(layoutParams2);

                layoutParams3.gravity = Gravity.CENTER;
                appCompatImageView3.setLayoutParams(layoutParams3);

                break;
        }*/
    }
}
