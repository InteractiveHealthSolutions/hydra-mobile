package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by Naveed Iqbal on 11/9/2017.
 * Email: h.naveediqbal@gmail.com
 */

@Entity
public class LocationAttributeType implements Serializable {
    public static final long serialVersionUID = 1l;

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String name;
    @Unique
    private String uuid;
    @Generated(hash = 2127353195)
    public LocationAttributeType(Long id, String name, String uuid) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
    }
    @Generated(hash = 774219940)
    public LocationAttributeType() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
