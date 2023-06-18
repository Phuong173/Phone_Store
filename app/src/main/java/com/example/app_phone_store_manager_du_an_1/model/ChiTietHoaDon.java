package com.example.app_phone_store_manager_du_an_1.model;

public class ChiTietHoaDon {
    private int idCTHD;
    private String maHD;
    private String maSP;
    private int soLuong;
    private double donGia;
    private int giamGia;
    private int baoHanh;

    public ChiTietHoaDon() {
    }

    public static final String TB_NAME = "ChiTietHoaDon";
    public static final String TB_COL_ID_HD = "maHD";
    public static final String TB_COL_ID = "maCTHD";
    public static final String TB_COL_ID_SP = "maSP";
    public static final String TB_COL_AMOUNT = "soLuong";
    public static final String TB_COL_SALE = "giamGia";
    public static final String TB_COL_PRICE = "donGia";
    public static final String TB_COL_BH = "baoHanh";

    public int getIdCTHD() {
        return idCTHD;
    }

    public void setIdCTHD(int idCTHD) {
        this.idCTHD = idCTHD;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(int giamGia) {
        this.giamGia = giamGia;
    }

    public int getBaoHanh() {
        return baoHanh;
    }

    public void setBaoHanh(int baoHanh) {
        this.baoHanh = baoHanh;
    }

}
