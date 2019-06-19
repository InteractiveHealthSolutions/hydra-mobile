package ihsinformatics.com.hydra_mobile.view.widgets.controls.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import ihsinformatics.com.hydra_mobile.utils.BitmapWorkerTask;
import timber.log.Timber;

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
        } catch (Exception e) {
            Timber.e(e);
        }
        return imageView;
    }
}
