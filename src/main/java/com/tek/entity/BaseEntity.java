package com.tek.entity;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    String createdOn;
    String lastUpdatedOn;
    String createdByName;
    String lastUpdatedByName;
    String createdByEmail;
    String lastUpdatedByEmail;

    public void setCreatedEntity(String userName, String userEmail) {
        this.createdByEmail = userEmail;
        this.createdByName = userName;
        this.createdOn = CommonUtils.getCurrentTime();
    }

    public void setUpdatedEntity(String userName, String userEmail) {
        this.lastUpdatedByEmail = userEmail;
        this.lastUpdatedByName = userName;
        this.lastUpdatedOn = CommonUtils.getCurrentTime();
    }
}
