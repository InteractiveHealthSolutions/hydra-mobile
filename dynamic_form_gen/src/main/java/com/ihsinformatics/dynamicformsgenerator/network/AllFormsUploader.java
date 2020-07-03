package com.ihsinformatics.dynamicformsgenerator.network;

import android.content.Context;

import com.ihsinformatics.dynamicformsgenerator.data.pojos.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.SaveableForm;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Naveed Iqbal on 4/6/2017.
 * Email: h.naveediqbal@gmail.com
 */
public class AllFormsUploader implements Sendable {
    private List<SaveableForm> saveableForms;
    private Context context;
    private int uploaded_forms = 0;
    DataAccess dataAccess;

    public AllFormsUploader(Context context) {
        this.context = context;
    }

    public void startUploadingForms() {
        uploaded_forms = 0;
        dataAccess = DataAccess.getInstance();
        saveableForms = dataAccess.getAllFormsByHydraUrl(context, Global.HYRDA_CURRENT_URL);
        if (saveableForms.size() > 0) {
            send(saveableForms.get(0).getFormData(), saveableForms.get(0).getFormId().intValue());
        } else {
            try {
                new AllImagesUploader(context).startUploadingImages();
            } catch (Exception e) {
                Logger.log(e);
            }
        }
    }

    @Override
    public void send(JSONObject data, int respId) {
        new DataSender(context, this, respId).execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        uploaded_forms++;
        try {
            if(resp.has(ParamNames.SERVER_ERROR)) {
                updateFormError(resp, respId);
            } else {
                if (resp.has(ParamNames.SERVER_RESPONSE)) {
                    if(resp.getJSONObject(ParamNames.SERVER_RESPONSE).has(ParamNames.SERVER_ERROR)) {
                        updateFormError(resp, respId);
                    } else if (resp.getJSONObject(ParamNames.SERVER_RESPONSE).has(ParamNames.UUID) && dataAccess.getCountOfImagesById(context, respId) > 0) {
                        // Image image = new Image(respId, resp.getJSONObject(ParamNames.SERVER_RESPONSE).get(ParamNames.UUID).toString(),null,null,null);
                        DataAccess.getInstance().updateFormImage(context, respId, resp.getJSONObject(ParamNames.SERVER_RESPONSE).get(ParamNames.UUID).toString());
                    }
                }
                DataAccess.getInstance().deleteForm(context, respId);
                /* This piece of code can notify via broadcast when forms are uploaded
                if (MainScreen.isActive) {
                    Intent intent = new Intent(ACTION_UPDATE_NOTIFICATION);
                    context.sendBroadcast(intent);
                }*/
            }
        } catch (JSONException e) {
            Logger.log(e);
        }
        if (uploaded_forms < saveableForms.size()) {
            send(saveableForms.get(uploaded_forms).getFormData(), saveableForms.get(uploaded_forms).getFormId().intValue());
        } else {
            try {
                new AllImagesUploader(context).startUploadingImages();
            } catch (Exception e) {
                Logger.log(e);
            }
        }
    }

    private void updateFormError(JSONObject resp, int respId) throws JSONException {
        String value;
        value = resp.getString(ParamNames.SERVER_ERROR);
        DataAccess.getInstance().updateFormError(context, respId, value);
    }
}
