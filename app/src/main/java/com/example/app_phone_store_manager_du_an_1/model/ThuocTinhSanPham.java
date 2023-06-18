package com.example.app_phone_store_manager_du_an_1.model;

public class ThuocTinhSanPham {
    private int maTT;
    private String maSP;
    private String boNho;
    private String RAM;
    private String chipSet;
    private String heDieuHanh;
    private String manHinh;
    private String dungLuongPin;
    private String congSac;
    private String loaiPhuKien;

    public static final String TB_NAME = "ThuocTinhSanPham";
    public static final String TB_COL_ID_TT = "maTT";
    public static final String TB_COL_ID_SP = "maSP";
    public static final String TB_COL_MEMORY = "boNho";
    public static final String TB_COL_RAM = "RAM";
    public static final String TB_COL_CHIP = "chipSet";
    public static final String TB_COL_OS = "heDieuHanh";
    public static final String TB_COL_SCREEN = "manHinh";
    public static final String TB_COL_BATTERY = "dungLuongPin";
    public static final String TB_COL_TYPE = "congSac";
    public static final String TB_COL_LOAI = "loaiPhuKien";

    public int getMaTT() {
        return maTT;
    }

    public void setMaTT(int maTT) {
        this.maTT = maTT;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getBoNho() {
        return boNho;
    }

    public void setBoNho(String boNho) {
        this.boNho = boNho;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getChipSet() {
        return chipSet;
    }

    public void setChipSet(String chipSet) {
        this.chipSet = chipSet;
    }

    public String getHeDieuHanh() {
        return heDieuHanh;
    }

    public void setHeDieuHanh(String heDieuHanh) {
        this.heDieuHanh = heDieuHanh;
    }

    public String getManHinh() {
        return manHinh;
    }

    public void setManHinh(String manHinh) {
        this.manHinh = manHinh;
    }

    public String getDungLuongPin() {
        return dungLuongPin;
    }

    public void setDungLuongPin(String dungLuongPin) {
        this.dungLuongPin = dungLuongPin;
    }

    public String getCongSac() {
        return congSac;
    }

    public void setCongSac(String congSac) {
        this.congSac = congSac;
    }

    public String getLoaiPhuKien() {
        return loaiPhuKien;
    }

    public void setLoaiPhuKien(String loaiPhuKien) {
        this.loaiPhuKien = loaiPhuKien;
    }
}
