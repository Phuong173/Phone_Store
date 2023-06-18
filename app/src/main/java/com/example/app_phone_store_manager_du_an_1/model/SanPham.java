package com.example.app_phone_store_manager_du_an_1.model;

public class SanPham {
    private String maSP;
    private String maHang;
    private String tenSP;
    private byte[] hinhAnh;
    private int phanLoai;
    private int tinhTrang;
    private double giaTien;
    private int trangThai;
    private String moTa;

    public static final String TB_NAME = "SanPham";
    public static final String TB_COL_ID_SP = "maSP";
    public static final String TB_COL_ID_HANG = "maHang";
    public static final String TB_COL_NAME = "tenSP";
    public static final String TB_COL_IMAGE = "hinhAnh";
    public static final String TB_COL_CLASSIFY = "phanLoai";
    public static final String TB_COL_STATE = "tinhTrang";
    public static final String TB_COL_MONEY = "giaTien";
    public static final String TB_COL_STATUS = "trangThai";
    public static final String TB_COL_NOTE = "moTa";

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getPhanLoai() {
        return phanLoai;
    }

    public void setPhanLoai(int phanLoai) {
        this.phanLoai = phanLoai;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
