package com.ihsinformatics.dynamicformsgenerator.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Owais on 12/8/2017.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private Context context;
    private String data;
    private final WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(Context context, ImageView imageView, String imageFile) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        data = imageFile;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        return ImageUtils.decodeSampledBitmapFromResource(data, params[0], params[1]);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (imageViewReference != null && bitmap != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
