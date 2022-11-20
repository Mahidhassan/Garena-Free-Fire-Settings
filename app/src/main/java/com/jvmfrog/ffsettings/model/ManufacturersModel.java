package com.jvmfrog.ffsettings.model;

import androidx.annotation.Keep;

@Keep
public class ManufacturersModel {
    String name, collection;
    Boolean showInDashboard, isAvailable;

    public ManufacturersModel() {};

    public ManufacturersModel(String name, String collection, Boolean showInDashboard, Boolean isAvailable) {
        this.name = name;
        this.collection = collection;
        this.showInDashboard = showInDashboard;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Boolean getShowInDashboard() {
        return showInDashboard;
    }

    public void setShowInDashboard(Boolean showInDashboard) {
        this.showInDashboard = showInDashboard;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        isAvailable = isAvailable;
    }
}
