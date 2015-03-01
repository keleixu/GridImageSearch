package com.example.kelei.gridimagesearch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kelei.gridimagesearch.R;


import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kelei on 2/28/15.
 */
public class ImageItemView extends LinearLayout {
    public @InjectView(R.id.ivImage) ImageView ivImage;
    public @InjectView(R.id.tvTitle) TextView tvTitle;

    public ImageItemView(Context context) {
        this(context, null);
    }

    public ImageItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, this, true);
        setOrientation(VERTICAL);
        ButterKnife.inject(this);
    }
}
