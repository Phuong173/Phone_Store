package com.example.app_phone_store_manager_du_an_1.model;

public class Hang {
    private String maHang;
    private String tenHang;
    private byte[] hinhAnh;

    public static final String TB_NAME = "Hang";
    public static final String TB_COL_ID= "maHang";
    public static final String TB_COL_NAME = "tenHang";
    public static final String TB_COL_IMAGE = "hinhAnh";

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
