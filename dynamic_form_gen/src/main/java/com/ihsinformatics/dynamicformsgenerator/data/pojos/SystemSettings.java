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
public class SystemSettings implements Serializable {
    public static final long serialVersionUID = 1l;

    @Id(autoincrement = true)
    private Long id;
    private String property;
    private String value;
    @Unique
    private String uuid;
    @Generated(hash = 1949611328)
    public SystemSettings(Long id, String property, String value, String uuid) {
        this.id = id;
        this.property = property;
        this.value = value;
        this.uuid = uuid;
    }
    @Generated(hash = 2069761178)
    public SystemSettings() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProperty() {
        return this.property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
