package com.philong.model;

import java.io.Serializable;

/**
 * Created by Long on 08/06/2016.
 */
public class BaiHat implements Serializable {

    private int maBH, yeuThich;
    private String tenBH, tacGia;

    public BaiHat(){

    }

    public BaiHat(int maBH, String tenBH, String tacGia, int yeuThich){
        this.maBH = maBH;
        this.tenBH = tenBH;
        this.tacGia = tacGia;
        this.yeuThich = yeuThich;
    }

    public int getMaBH() {
        return maBH;
    }

    public void setMaBH(int maBH) {
        this.maBH = maBH;
    }

    public int getYeuThich() {
        return yeuThich;
    }

    public void setYeuThich(int yeuThich) {
        this.yeuThich = yeuThich;
    }

    public String getTenBH() {
        return tenBH;
    }

    public void setTenBH(String tenBH) {
        this.tenBH = tenBH;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }
}
