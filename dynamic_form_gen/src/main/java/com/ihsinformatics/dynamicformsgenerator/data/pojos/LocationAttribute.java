package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by Naveed Iqbal on 11/9/2017.
 * Email: h.naveediqbal@gmail.com
 */

@Entity
public class LocationAttribute implements Serializable {
    public static final long serialVersionUID = 1l;

    @Id(autoincrement = true)
    private Long id;
    private String value;
    @Unique
    private String uuid;

    private Long locationId;
    @ToOne(joinProperty = "locationId")
    private Location location;

    private Long locationAttributeTypeId;
    @ToOne(joinProperty = "locationAttributeTypeId")
    private LocationAttributeType locationAttributeType;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 385090444)
    private transient LocationAttributeDao myDao;
    @Generated(hash = 1124971914)
    public LocationAttribute(Long id, String value, String uuid, Long locationId,
            Long locationAttributeTypeId) {
        this.id = id;
        this.value = value;
        this.uuid = uuid;
        this.locationId = locationId;
        this.locationAttributeTypeId = locationAttributeTypeId;
    }
    @Generated(hash = 1290262820)
    public LocationAttribute() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Long getLocationId() {
        return this.locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    public Long getLocationAttributeTypeId() {
        return this.locationAttributeTypeId;
    }
    public void setLocationAttributeTypeId(Long locationAttributeTypeId) {
        this.locationAttributeTypeId = locationAttributeTypeId;
    }
    @Generated(hash = 1068795426)
    private transient Long location__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1449045962)
    public Location getLocation() {
        Long __key = this.locationId;
        if (location__resolvedKey == null || !location__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationDao targetDao = daoSession.getLocationDao();
            Location locationNew = targetDao.load(__key);
            synchronized (this) {
                location = locationNew;
                location__resolvedKey = __key;
            }
        }
        return location;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 340671030)
    public void setLocation(Location location) {
        synchronized (this) {
            this.location = location;
            locationId = location == null ? null : location.getId();
            location__resolvedKey = locationId;
        }
    }
    @Generated(hash = 564944821)
    private transient Long locationAttributeType__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1627156313)
    public LocationAttributeType getLocationAttributeType() {
        Long __key = this.locationAttributeTypeId;
        if (locationAttributeType__resolvedKey == null
                || !locationAttributeType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationAttributeTypeDao targetDao = daoSession
                    .getLocationAttributeTypeDao();
            LocationAttributeType locationAttributeTypeNew = targetDao.load(__key);
            synchronized (this) {
                locationAttributeType = locationAttributeTypeNew;
                locationAttributeType__resolvedKey = __key;
            }
        }
        return locationAttributeType;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 648288495)
    public void setLocationAttributeType(
            LocationAttributeType locationAttributeType) {
        synchronized (this) {
            this.locationAttributeType = locationAttributeType;
            locationAttributeTypeId = locationAttributeType == null ? null
                    : locationAttributeType.getId();
            locationAttributeType__resolvedKey = locationAttributeTypeId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 736059672)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLocationAttributeDao() : null;
    }

}
