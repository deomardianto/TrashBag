package com.rangai.e_trashbag;

public class dataHistory {
    String nama, TypeOrder,timeStamp;

    public dataHistory() {
    }

    public dataHistory(String nama, String typeOrder, String timeStamp) {
        this.nama = nama;
        TypeOrder = typeOrder;
        this.timeStamp = timeStamp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
}
