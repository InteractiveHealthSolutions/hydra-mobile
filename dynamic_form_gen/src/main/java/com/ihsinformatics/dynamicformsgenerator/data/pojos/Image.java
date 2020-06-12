package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Owais on 1/4/2018.
 */
@Entity(nameInDb = "image")
public class Image {
    public static final String COLUMN_FORM_ID = "form_id";
    public static final String COLUMN_FORM_UUID = "form_uuid";
    public static final String COLUMN_IMAGE_ID = "image_id";
    public static final String COLUMN_IMAGE_METADATA = "image_metadata";
    public static final String COLUMN_IMAGE_CONTENT = "image_content";
    public static final String COLUMN_IMAGE_ERROR = "image_error";

    // Added for backward compatibility only, newer calls should use stringFormData
    @Transient
    JSONArray imageMetadata;

    @Id(autoincrement = true)
    private Long imageId;
    @NotNull
    @Property(nameInDb = COLUMN_FORM_ID)
    private long formId;
    @Property(nameInDb = COLUMN_FORM_UUID)
    private String formUUID;
    @Property(nameInDb = COLUMN_IMAGE_METADATA)
    private String stringImageMetadata;
    @NotNull
    @Property(nameInDb = COLUMN_IMAGE_CONTENT)
    private byte[] imageContent;
    @Property(nameInDb = COLUMN_IMAGE_ERROR)
    private String imageError;

    public Image() {
    }

    @Generated(hash = 884862056)
    public Image(Long imageId, long formId, String formUUID, String stringImageMetadata, @NotNull byte[] imageContent,
            String imageError) {
        this.imageId = imageId;
        this.formId = formId;
        this.formUUID = formUUID;
        this.stringImageMetadata = stringImageMetadata;
        this.imageContent = imageContent;
        this.imageError = imageError;
    }

    public Image(long formId, String formUUID, String stringImageMetadata, byte[] imageContent, String imageError) {
        this.formId = formId;
        this.formUUID = formUUID;
        this.stringImageMetadata = stringImageMetadata;
        this.imageContent = imageContent;
        this.imageError = imageError;
    }
    /*
    public Image(long imageId, long formId, String formUUID, String imageMetadata, byte[] imageContent, String imageError) {
        this.imageId = imageId;
        this.formId = formId;
        this.formUUID = formUUID;
        try {
            this.imageMetadata = new JSONArray(imageMetadata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.imageContent = imageContent;
        this.imageError = imageError;
    }*/

    public long getFormId() {
        return formId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getFormUUID() {
        return formUUID;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public JSONArray getImageMetadata() {
        try {
            imageMetadata = new JSONArray(stringImageMetadata);
        } catch (JSONException e) {
            Logger.log(e);
        }
        return imageMetadata;
    }

    public void setImageMetadata(JSONArray imageMetadata) {
        this.imageMetadata = imageMetadata;
        stringImageMetadata = imageMetadata.toString();
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public String getImageError() {
        return imageError;
    }

    public void setImageError(String imageError) {
        this.imageError = imageError;
    }

    public String getStringImageMetadata() {
        return this.stringImageMetadata;
    }

    public void setStringImageMetadata(String stringImageMetadata) {
        this.stringImageMetadata = stringImageMetadata;
    }
}
