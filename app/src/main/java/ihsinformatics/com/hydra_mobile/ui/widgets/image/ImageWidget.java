package ihsinformatics.com.hydra_mobile.ui.widgets.image;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.github.chrisbanes.photoview.PhotoView;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.options.RangeOption;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.utils.AppUtility;
import ihsinformatics.com.hydra_mobile.utils.ImageUtils;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.ui.widgets.InputWidget;
import ihsinformatics.com.hydra_mobile.ui.widgets.controls.adapters.ImageListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owais on 11/1/2017.
 */
public class ImageWidget extends InputWidget implements View.OnClickListener, GridView.OnItemLongClickListener {
    //HD 720p
    /*  public static final int maxWidth = 1280;
    public static final int maxHeight = 720;*/
    //FHD 1080p
    public static final int maxWidth = 1920;
    public static final int maxHeight = 1080;
    //QHD 1440p
  /*  public static final int maxWidth = 2560;
    public static final int maxHeight = 1440;*/
    ImageView imageViewCamera, imageViewGallery;
    GridView gridView;
    List<String> listOfImages;
    ImageListAdapter imageListAdapter;
    Uri imageUri;
    private RangeOption rangeOption;
    int maxVal = 999;

    public ImageWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        imageViewCamera = (ImageView) findViewById(R.id.photo_camera);
        imageViewCamera.setOnClickListener(this);
       /* imageViewGallery = (ImageView) findViewById(R.id.photo_gallery);
        imageViewGallery.setOnClickListener(this);*/
        gridView = (GridView) findViewById(R.id.gridviewimg);
        listOfImages = new ArrayList<>(0);
        imageListAdapter = new ImageListAdapter(context, listOfImages);
        gridView.setAdapter(imageListAdapter);
        gridView.setOnItemLongClickListener(this);
        options = null;//DataProvider.getInstance(context).getOptions(question.getQuestionId());
        for (Option option : options) {
            if (option instanceof RangeOption) {
                rangeOption = (RangeOption) option;
            }
        }
        if (rangeOption != null) {
            maxVal = rangeOption.getSkipRange().getValue();
        }
    }

    @Override
    public boolean isValidInput(boolean isMandatory) {
        if (listOfImages.size() > 0 && isMandatory) {
            return true;
        } else if (!isMandatory) {
            return true;
        }
        return false;
    }

    @Override
    public void setOptionsOrHint(Option... data) {
    }

    @Override
    public void onFocusGained() {
        //gridView.requestFocus();
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            dismissMessage();
            addParams(param);
        } else {
            //activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param;
    }

    private void addParams(JSONObject param) throws JSONException {
        JSONArray subParams = new JSONArray();
        for (String s : listOfImages) {
            subParams.put(s);
        }
        param.put(question.getParamName(), subParams);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.photo_camera) {
            if (listOfImages.size() < maxVal) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkCameraPermission();
                } else {
                    callCamera();
                }
            } else {
                //Toasty.error(context, "Only " + maxVal + " images are allowed", Toast.LENGTH_SHORT).show();
            }
        } /*else if (i == R.id.photo_gallery) {
            callGallery();
        }*/
    }

    private void callGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("TAG", "Gallery");
        ((Activity) ImageWidget.this.context).startActivityForResult(intent, getQuestionId());
    }

    public void callCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkStoragePermission();
        } else {
            startCamera();
        }
    }

    public void startCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) ImageWidget.this.context).startActivityForResult(intent, getQuestionId());
    }

    public Uri getImageURI() {
        return imageUri;
    }

    private void checkCameraPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) ImageWidget.this.context,
                    Manifest.permission.CAMERA)) {
                AppUtility.Companion.showMessageOKCancel(context, "For capturing images , You need to provide permission to access your camera",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) ImageWidget.this.context,
                                        new String[]{Manifest.permission.CAMERA},
                                        getQuestionId());
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions((Activity) ImageWidget.this.context,
                    new String[]{Manifest.permission.CAMERA},
                    getQuestionId());
            return;
        }
        callCamera();
    }

    public void checkStoragePermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) ImageWidget.this.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AppUtility.Companion.showMessageOKCancel(context, "For adding images , You need to provide permission to access your files",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) ImageWidget.this.context,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        getQuestionId());
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions((Activity) ImageWidget.this.context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    getQuestionId());
            return;
        }
        startCamera();
    }

    public void setImage(Bitmap bitmap) {
        try {
            Bitmap bm = ImageUtils.scaleBitmapWithAspectRatio(bitmap, maxWidth, maxHeight);
            listOfImages.add(ImageUtils.encodeBitmapToBase64String(bm));
            imageListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }

    private void viewImage(final int position) {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_widget_image_viewer);
        dialog.show();

      /*  ImageView imageView = (ImageView) dialog.findViewById(R.id.image_view);
        String imageFile = listOfImages.get(position);
        new BitmapWorkerTask(context, imageView, imageFile).execute(300, 300);*/

        PhotoView mPhotoView = (PhotoView) dialog.findViewById(R.id.iv_photo);
        String imageFile = listOfImages.get(position);
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(imageFile, 300, 300);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mPhotoView.setImageDrawable(drawable);

        Button buttonDismiss = (Button) dialog.findViewById(R.id.button_dismiss);
        buttonDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void removeImage(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this image?")
                .setTitle("Are you sure?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listOfImages.remove(position);
                        imageListAdapter.notifyDataSetChanged();
                    }
                });
        dialog.show();
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popup = new PopupMenu(context, gridView);
        popup.getMenuInflater().inflate(R.menu.image_widget_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_view) {
                    viewImage(position);
                } else if (id == R.id.action_delete) {
                    removeImage(position);
                }
                return true;
            }
        });
        popup.show();
        return true;
    }
}
