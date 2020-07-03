package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by Naveed Iqbal on 11/8/2017.
 * Email: h.naveediqbal@gmail.com
 */

@Entity
public class Option implements Serializable {
    public static final long serialVersionUID = 1l;

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String uuid;
    private String value;
    private String optionTag;
    @Generated(hash = 1878798731)
    public Option(Long id, String uuid, String value, String optionTag) {
        this.id = id;
        this.uuid = uuid;
        this.value = value;
        this.optionTag = optionTag;
    }
    @Generated(hash = 104107376)
    public Option() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getOptionTag() {
        return this.optionTag;
    }
    public void setOptionTag(String optionTag) {
        this.optionTag = optionTag;
    }


}
