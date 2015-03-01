package com.example.kelei.gridimagesearch.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.kelei.gridimagesearch.R;
import com.example.kelei.gridimagesearch.model.ImageResult;
import com.example.kelei.gridimagesearch.view.ImageItemView;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by kelei on 2/28/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context c, List<ImageResult> images) {
        super(c, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);
        if (convertView == null) {
            convertView = new ImageItemView(getContext());
        }
        ImageItemView imageItemView = (ImageItemView) convertView;
        imageItemView.ivImage.setImageResource(0);
        imageItemView.tvTitle.setText(Html.fromHtml(imageResult.title));
        Picasso.with(getContext()).load(imageResult.thumbUrl).placeholder(R.drawable.placeholder_tomatofish).into(
            imageItemView.ivImage);
        return imageItemView;
    }
}
