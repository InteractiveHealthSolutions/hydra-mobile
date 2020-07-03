package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Nabil shafi on 2/25/2019.
 */

@Entity
public class PatientIdentifier {

    @Id(autoincrement = true)
    private long id;

    private Long identifierTypeId;
    @ToOne(joinProperty = "identifierTypeId")
    private IdentifierType identifierType;

    private Long patientId;
    @ToOne(joinProperty = "patientId")
    private Patient patient;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1475289709)
    private transient PatientIdentifierDao myDao;
    @Generated(hash = 1198937282)
    public PatientIdentifier(long id, Long identifierTypeId, Long patientId) {
        this.id = id;
        this.identifierTypeId = identifierTypeId;
        this.patientId = patientId;
    }
    @Generated(hash = 1663422294)
    public PatientIdentifier() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Long getIdentifierTypeId() {
        return this.identifierTypeId;
    }
    public void setIdentifierTypeId(Long identifierTypeId) {
        this.identifierTypeId = identifierTypeId;
    }
    public Long getPatientId() {
        return this.patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    @Generated(hash = 521490543)
    private transient Long identifierType__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 435030026)
    public IdentifierType getIdentifierType() {
        Long __key = this.identifierTypeId;
        if (identifierType__resolvedKey == null
                || !identifierType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IdentifierTypeDao targetDao = daoSession.getIdentifierTypeDao();
            IdentifierType identifierTypeNew = targetDao.load(__key);
            synchronized (this) {
                identifierType = identifierTypeNew;
                identifierType__resolvedKey = __key;
            }
        }
        return identifierType;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1283144745)
    public void setIdentifierType(IdentifierType identifierType) {
        synchronized (this) {
            this.identifierType = identifierType;
            identifierTypeId = identifierType == null ? null
                    : identifierType.getId();
            identifierType__resolvedKey = identifierTypeId;
        }
    }
    @Generated(hash = 391381774)
    private transient Long patient__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1923256233)
    public Patient getPatient() {
        Long __key = this.patientId;
        if (patient__resolvedKey == null || !patient__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PatientDao targetDao = daoSession.getPatientDao();
            Patient patientNew = targetDao.load(__key);
            synchronized (this) {
                patient = patientNew;
                patient__resolvedKey = __key;
            }
        }
        return patient;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 459152036)
    public void setPatient(Patient patient) {
        synchronized (this) {
            this.patient = patient;
            patientId = patient == null ? null : patient.getId();
            patient__resolvedKey = patientId;
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
    @Generated(hash = 1907444096)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPatientIdentifierDao() : null;
    }
}
