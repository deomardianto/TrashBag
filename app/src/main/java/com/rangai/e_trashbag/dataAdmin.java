package com.rangai.e_trashbag;

public class dataAdmin {
    String nama, phone, TypeOrder, timeStamp, profile, LinkMaps, sudahBelum;

    public dataAdmin() {
    }

    public dataAdmin(String nama, String phone, String typeOrder, String timeStamp, String profile, String linkMaps, String sudahBelum) {
        this.nama = nama;
        this.phone = phone;
        TypeOrder = typeOrder;
        this.timeStamp = timeStamp;
        this.profile = profile;
        LinkMaps = linkMaps;
        this.sudahBelum = sudahBelum;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypeOrder() {
        return TypeOrder;
    }

    public void setTypeOrder(String typeOrder) {
        TypeOrder = typeOrder;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLinkMaps() {
        return LinkMaps;
    }

    public void setLinkMaps(String linkMaps) {
        LinkMaps = linkMaps;
    }

    public String getSudahBelum() {
        return sudahBelum;
    }

    public void setSudahBelum(String sudahBelum) {
        this.sudahBelum = sudahBelum;
    }
}
