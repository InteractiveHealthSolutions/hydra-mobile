package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created by Naveed Iqbal on 11/9/2017.
 * Email: h.naveediqbal@gmail.com
 */

@Entity
public class LocationTagMap implements Serializable {
    public static final long serialVersionUID = 1l;

    @Id(autoincrement = true)
    private Long id;

    private Long locationId;
    @ToOne(joinProperty = "locationId")
    private Location location;

    private Long locationTagId;
    @ToOne(joinProperty = "locationTagId")
    private LocationTag locationTag;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 831072611)
    private transient LocationTagMapDao myDao;
    @Generated(hash = 1425028396)
    public LocationTagMap(Long id, Long locationId, Long locationTagId) {
        this.id = id;
        this.locationId = locationId;
        this.locationTagId = locationTagId;
    }
    @Generated(hash = 1767444711)
    public LocationTagMap() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getLocationId() {
        return this.locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    public Long getLocationTagId() {
        return this.locationTagId;
    }
    public void setLocationTagId(Long locationTagId) {
        this.locationTagId = locationTagId;
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
    @Generated(hash = 1462442359)
    private transient Long locationTag__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 857956106)
    public LocationTag getLocationTag() {
        Long __key = this.locationTagId;
        if (locationTag__resolvedKey == null
                || !locationTag__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationTagDao targetDao = daoSession.getLocationTagDao();
            LocationTag locationTagNew = targetDao.load(__key);
            synchronized (this) {
                locationTag = locationTagNew;
                locationTag__resolvedKey = __key;
            }
        }
        return locationTag;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 367114919)
    public void setLocationTag(LocationTag locationTag) {
        synchronized (this) {
            this.locationTag = locationTag;
            locationTagId = locationTag == null ? null : locationTag.getId();
            locationTag__resolvedKey = locationTagId;
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
    @Generated(hash = 383936039)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLocationTagMapDao() : null;
    }

}
