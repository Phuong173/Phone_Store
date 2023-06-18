package com.example.app_phone_store_manager_du_an_1.model;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String dienThoai;
    private String diaChi;


    public static final String TB_NAME = "KhachHang";
    public static final String TB_COL_ID = "maKH";
    public static final String TB_COL_NAME = "hoTen";
    public static final String TB_COL_PHONE = "dienThoai";
    public static final String TB_COL_LOCALE = "diaChi";

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
