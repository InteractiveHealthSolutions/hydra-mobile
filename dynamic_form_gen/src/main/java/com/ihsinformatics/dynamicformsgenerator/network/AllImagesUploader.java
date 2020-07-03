package com.ihsinformatics.dynamicformsgenerator.network;

import android.content.Context;

import com.ihsinformatics.dynamicformsgenerator.data.pojos.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Image;
import com.ihsinformatics.dynamicformsgenerator.utils.AES256Endec;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.ImageUtils;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.crypto.SecretKey;

/**
 * Created by Owais on 1/8/2018.
 */
public class AllImagesUploader implements Sendable {
    private List<Image> imageList;
    private Context context;
    private int uploaded_images = 0;
    DataAccess dataAccess;
    int index;

    public AllImagesUploader(Context context) {
        this.context = context;
    }

    public void startUploadingImages() {
        uploaded_images = 0;
        dataAccess = DataAccess.getInstance();
        imageList = dataAccess.getAllImages(context);
        if (imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                Image image = imageList.get(i);
                if (image.getFormUUID() != null) {
                    index = i + 1;
                    send(getJSONObjectFromPOJO(image), image.getImageId().intValue());
                    break;
                }
            }
        }
    }

    @Override
    public void send(JSONObject data, int respId) {
        new DataSender(context, this, respId).execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        uploaded_images++;
        try {
            if (!resp.has(ParamNames.SERVER_ERROR)) {
                DataAccess.getInstance().deleteImages(context, respId);
            } else {
                String value;
                value = resp.getString(ParamNames.SERVER_ERROR);
                DataAccess.getInstance().updateImageError(context, respId, value);
            }
        } catch (JSONException e) {
            Logger.log(e);
        }
        for (int i = index; i < imageList.size(); i++) {
            Image image = imageList.get(i);
            if (image.getFormUUID() != null) {
                send(getJSONObjectFromPOJO(image), image.getImageId().intValue());
                index = i + 1;
                break;
            }
        }
    }

    private JSONObject getJSONObjectFromPOJO(Image image) {
        JSONArray jsonArray = null;
        JSONObject sendableData = new JSONObject();
        try {
            putAuthenticationData(sendableData);
            String encodedImage = ImageUtils.getEncodedStringFromBytes(image.getImageContent());
            jsonArray = image.getImageMetadata();
            JSONObject imageKey = jsonArray.getJSONObject(2);
            sendableData.put(imageKey.getString(ParamNames.CAPTURE_IMAGE), encodedImage);
            sendableData.put("formUUID", image.getFormUUID());
            sendableData.put(ParamNames.REQUEST_TYPE, ParamNames.CAPTURE_IMAGE);
        } catch (Exception e) {
            Logger.log(e);
        }
        return sendableData;
    }

    private void putAuthenticationData(JSONObject data) throws Exception {
        SecretKey secKey = AES256Endec.getInstance().generateKey();
        String encPassword = AES256Endec.getInstance().encrypt(Global.PASSWORD, secKey);
        JSONObject authenticationJson = new JSONObject();
        authenticationJson.put(ParamNames.USERNAME, Global.USERNAME);
        authenticationJson.put(ParamNames.PASSWORD, encPassword);
        /*String providerUUID = resolveProviderUUID();
        authenticationJson.put(ParamNames.PROVIDER_UUID, providerUUID);*/
        data.put(ParamNames.AUTHENTICATION, authenticationJson);
    }
}
