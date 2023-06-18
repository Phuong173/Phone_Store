package com.example.app_phone_store_manager_du_an_1.model;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private int phanLoai;
    private String ngay;
    private int trangThai;

    public static final String TB_NAME = "HoaDon";
    public static final String TB_COL_ID_HD = "maHD";
    public static final String TB_COL_ID_NV = "maNV";
    public static final String TB_COL_ID_KH = "maKH";
    public static final String TB_COL_CLASS = "phanLoai";
    public static final String TB_COL_STATUS = "trangThai";
    public static final String TB_COL_DATE = "ngay";

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public int getPhanLoai() {
        return phanLoai;
    }

    public void setPhanLoai(int phanLoai) {
        this.phanLoai = phanLoai;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
