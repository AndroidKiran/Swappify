package swapp.items.com.swappify.components.zoomtab;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import swapp.items.com.swappify.R;
import swapp.items.com.swappify.controller.home.ui.HomePagerAdapter;

public class ViewPagerHeader extends HorizontalScrollView implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener, View.OnClickListener {

    private TextAttr textViewAttr;
    private AppCompatImageView[] imageViews = new AppCompatImageView[0];
    private int headerPerView = 3;
    private int headerWidth;
    private ViewPager viewPager;
    private LinearLayout rootContainer;
    private ArrayList<Drawable> headerIcons;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setCurrentPosition(position, positionOffsetPixels, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        prepareHeaders();
    }

    public ViewPagerHeader(Context context, TextAttr textViewAttr) {
        this(context, null, textViewAttr);
    }

    public ViewPagerHeader(Context context, AttributeSet attrs, TextAttr textViewAttr) {
        this(context, attrs, 0, textViewAttr);
    }

    public ViewPagerHeader(Context context, AttributeSet attrs, int defStyleAttr, TextAttr textViewAttr) {
        super(context, attrs, defStyleAttr);
        this.textViewAttr = textViewAttr;
        defaultSettings();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnAdapterChangeListener(this);
        prepareHeaders();
    }

    private void defaultSettings() {
        setHorizontalScrollBarEnabled(false);
    }

    private void prepareHeaders() {
        HomePagerAdapter adapter = (HomePagerAdapter) viewPager.getAdapter();
        headerIcons = new ArrayList<>();
        headerIcons.add(ContextCompat.getDrawable(getContext(), R.drawable.btn_color_transparent_normal));
        for (int i = 0; i < adapter.getCount(); i++) {
            Drawable drawable = adapter.getIcons().get(i);
            headerIcons.add(drawable);
        }
        headerIcons.add(ContextCompat.getDrawable(getContext(), R.drawable.btn_color_transparent_normal));
        createHeader(headerIcons);
    }

    private void createHeader(List<Drawable> headerIcons) {
        imageViews = new AppCompatImageView[headerIcons.size()];
        headerWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        this.removeAllViews();
        rootContainer = createRootContainer();
        for (int i = 0; i < headerIcons.size(); i++) {
            rootContainer.addView(createHeaderItem(i, headerIcons.get(i)));
        }
        this.addView(rootContainer);
    }

    private LinearLayout createRootContainer() {
        LinearLayout container = new LinearLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.HORIZONTAL);
        return container;
    }

    private AppCompatImageView createHeaderItem(int position, Drawable headerIcon) {

        AppCompatImageView header = new AppCompatImageView(getContext());
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(headerWidth / headerPerView, LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(linearParams);
        header.setScaleX(textViewAttr.getHvMinScale());
        header.setScaleY(textViewAttr.getHvMinScale());
        header.setAlpha(textViewAttr.getHvTextAlpha());
        header.setPadding(0, (int) textViewAttr.getHvPadding(), 0, (int) textViewAttr.getHvPadding());
        header.setImageDrawable(headerIcon);
        header.setId(position);
        header.setOnClickListener(this);
        imageViews[position] = header;

        return header;
    }

    public void setCurrentPosition(int viewPagerPosition, int offset, float scale) {
        int currentPosition = viewPagerPosition + 1;
        updateScale(currentPosition, scale);
        scrollTo(viewPagerPosition * headerWidth / headerPerView + offset / headerPerView, 0);
    }

    private void updateScale(int current, float offset) {
        current = Math.round(current + offset);
        float position = 1 - (offset > 0.5f ? 1 - offset : offset);
        if (imageViews != null && imageViews.length > current) {
            updateTextView(imageViews[current], getScale(position - 0.5f), getAlpha(position - 0.5f), textViewAttr.getHvTextColorActiveTab(), Gravity.CENTER);
            updateNextAndPrev(current);
        }
    }

    private float getAlpha(float scale) {
        float range = textViewAttr.getHvTextAlphaActiveTab() - textViewAttr.getHvTextAlpha();
        return textViewAttr.getHvTextAlpha() + range * scale / 0.5f;
    }

    private float getScale(float scale) {
        float range = textViewAttr.getHvMaxScale() - textViewAttr.getHvMinScale();
        return textViewAttr.getHvMinScale() + range * scale / 0.5f;
    }


    private void updateNextAndPrev(int current) {
        float scale = textViewAttr.getHvMinScale();
        float alpha = textViewAttr.getHvTextAlpha();
        int color = textViewAttr.getHvTextColor();
        if (current > 0 && imageViews != null && imageViews.length > 0) {
            updateTextView(imageViews[current - 1], scale, alpha, color,Gravity.START);
        }

        if (imageViews != null && imageViews.length > current + 1) {
            updateTextView(imageViews[current + 1], scale, alpha, color, Gravity.END);
        }
    }

    private void updateTextView(AppCompatImageView imageView, float scale, float alpha, int color, int gravity) {
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        imageView.setAlpha(alpha);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();

        if (Gravity.CENTER == gravity) {
            Log.d("center ==== ", layoutParams.width+"");
        }

        if (Gravity.START == gravity) {
            Log.d("start ==== ", layoutParams.width+"");
        }

        if (Gravity.END == gravity) {
            Log.d("end ====== ", layoutParams.width+"");
        }

        Log.d("original ====== ", headerWidth+"");


        Drawable drawable = imageView.getDrawable();
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(porterDuffColorFilter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == 0 || v.getId() == headerIcons.size() - 1) {
            return;
        }
        viewPager.setCurrentItem(v.getId(), true);
    }
}
