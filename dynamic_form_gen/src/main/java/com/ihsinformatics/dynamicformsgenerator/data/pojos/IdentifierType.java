package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Nabil shafi on 2/25/2019.
 */

@Entity
public class IdentifierType {

    @Id(autoincrement = true)
    private long id;

    @Property
    @NotNull
    private String typeName;

    @Generated(hash = 161152978)
    public IdentifierType(long id, @NotNull String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    @Generated(hash = 959844165)
    public IdentifierType() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
