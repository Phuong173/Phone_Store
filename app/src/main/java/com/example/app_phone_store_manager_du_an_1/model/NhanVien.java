package com.example.app_phone_store_manager_du_an_1.model;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String dienThoai;
    private String diaChi;
    private String namSinh;
    private String taiKhoan;
    private String matKhau;
    private byte[] hinhAnh;

    public static final String TB_NAME = "NhanVien";
    public static final String TB_COL_ID = "maNV";
    public static final String TB_COL_NAME = "hoTen";
    public static final String TB_COL_PHONE = "dienThoai";
    public static final String TB_COL_LOCATION = "diaChi";
    public static final String TB_COL_DATE = "namSinh";
    public static final String TB_COL_USER = "taiKhoan";
    public static final String TB_COL_PASS = "matKhau";
    public static final String TB_COL_IMAGE = "hinhAnh";

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
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

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
