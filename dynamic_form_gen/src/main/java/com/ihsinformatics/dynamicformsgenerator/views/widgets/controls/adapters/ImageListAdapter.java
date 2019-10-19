package com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ihsinformatics.dynamicformsgenerator.utils.BitmapWorkerTask;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import java.util.List;

/**
 * Created by Owais on 11/13/2017.
 */
public class ImageListAdapter extends BaseAdapter {
    private Context context;
    private List<String> imgList;
    ImageView imageView;
    int reqWidth = 150;
    int reqHeight = 150;
    public static int counter;

    public ImageListAdapter(Context context, List<String> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    public int getCount() {
        if (imgList != null)
            return imgList.size();
        else
            return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(reqWidth, reqHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }
        try {
            String imageFile = imgList.get(position).toString();
            new BitmapWorkerTask(context, imageView, imageFile).execute(reqWidth, reqHeight);
         //    imageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromResource(imageFile, reqWidth, reqHeight));
        } catch (Exception e) {
            Logger.log(e);
        }
        return imageView;
    }
}
