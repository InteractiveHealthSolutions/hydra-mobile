package ihsinformatics.com.hydra_mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView

import java.lang.ref.WeakReference

/**
 * Created by Owais on 12/8/2017.
 */

class BitmapWorkerTask(val context: Context, imageView: ImageView, val data: String) :
    AsyncTask<Int, Void, Bitmap>() {

    override fun doInBackground(vararg params: Int?): Bitmap {
        return ImageUtils.decodeSampledBitmapFromResource(data, params[0]!!, params[1]!!)
    }

    private val imageViewReference: WeakReference<ImageView>?

    init {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = WeakReference(imageView)
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    // Once complete, see if ImageView is still around and set bitmap.
    override fun onPostExecute(bitmap: Bitmap?) {
        super.onPostExecute(bitmap)
        if (imageViewReference != null && bitmap != null) {
            val imageView = imageViewReference.get()
            imageView?.setImageBitmap(bitmap)
        }
    }
}
