package com.ihsinformatics.dynamicformsgenerator.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Owais on 12/8/2017.
 */
public class ImageUtils {
    public static Bitmap scaleBitmapWithAspectRatio(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static String encodeBitmapToBase64String(Bitmap bm) {
        String encodedImage = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        //new code for performance optimization
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

      /*  byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);*/
        return encodedImage;
    }

    public static Bitmap decodeBase64StringToBitmap(String imageFile) {
        byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static Bitmap decodeSampledBitmapFromResource(String imageFile, int reqWidth, int reqHeight) {
        //new code for performance optimization
        byte[] imageAsBytes = null;
        try {
            imageAsBytes = Base64.decode(URLDecoder.decode(imageFile, "UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static String saveBitmapToFileSystem(Bitmap bitmap, String path) {
        final String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImageWidget/";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String imgcurTime = dateFormat.format(new Date());
        File imageDirectory = new File(imagePath);
        imageDirectory.mkdirs();
        String _path = imagePath + imgcurTime + ".jpg";
        try {
            FileOutputStream out = new FileOutputStream(_path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _path;
    }

    public Bitmap retrieveBitmapFromFilePath(String path) {
        FileInputStream fs = null;
        Bitmap bm = null;
        try {
            fs = new FileInputStream(new File(path));
            if (fs != null) {
                bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public static byte[] getBytesFromEncodedString(String imageString) {
        byte[] imageAsBytes = null;
        try {
            imageAsBytes = Base64.decode(URLDecoder.decode(imageString, "UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return imageAsBytes;
    }

    public static String getEncodedStringFromBytes(byte[] imageBytes) {
        String encodedImage = "";
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(imageBytes, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    public static String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
}
