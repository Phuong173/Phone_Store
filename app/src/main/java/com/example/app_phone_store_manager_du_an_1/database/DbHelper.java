package com.example.app_phone_store_manager_du_an_1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "MobileManager";
    public static final int VER_SION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VER_SION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tạo bảng Nhân Viên
        String createTableNhanVien = "CREATE TABLE NhanVien(" +
                "maNV TEXT NOT NULL UNIQUE PRIMARY KEY," +
                "hoTen TEXT NOT NULL," +
                "dienThoai TEXT NOT NULL," +
                "diaChi TEXT," +
                "namSinh TEXT," +
                "taiKhoan TEXT NOT NULL," +
                "matKhau TEXT NOT NULL," +
                "hinhAnh BLOB)";
        db.execSQL(createTableNhanVien);
        //Tạo bảng Khách hàng
        String createTableKhachHang = "CREATE TABLE KhachHang(" +
                "maKH TEXT NOT NULL UNIQUE PRIMARY KEY," +
                "hoTen TEXT NOT NULL ," +
                "dienThoai TEXT NOT NULL," +
                "diaChi TEXT NOT NULL)";
        db.execSQL(createTableKhachHang);
        //Tạo bảng Hãng
        String createTableHang = "CREATE TABLE Hang(" +
                "maHang TEXT NOT NULL UNIQUE PRIMARY KEY," +
                "tenHang TEXT NOT NULL," +
                "hinhAnh BLOB)";
        db.execSQL(createTableHang);
        //Tạo bảng Sản Phẩm
        String createTableSanPham = "CREATE TABLE SanPham(" +
                "maSP TEXT NOT NULL UNIQUE PRIMARY KEY," +
                "maHang TEXT NOT NULL REFERENCES Hang(maHang)ON DELETE CASCADE ON UPDATE CASCADE," +
                "tenSP TEXT NOT NULL," +
                "hinhAnh BOLD," +
                "phanLoai INTEGER NOT NULL," +
                "tinhTrang INTEGER NOT NULL," +
                "giaTien TEXT NOT NULL," +
                "trangThai INTERGER NOT NULL," +
                "moTa TEXT)";
        db.execSQL(createTableSanPham);
        // Tạo bảng thuộc tính SP
        String createTableThuocTinhSanPham = "CREATE TABLE ThuocTinhSanPham(" +
                "maTT INTEGER PRIMARY KEY AUTOINCREMENT," +
                "maSP TEXT NOT NULL REFERENCES SanPham(maSP) ON DELETE CASCADE ON UPDATE CASCADE," +
                "boNho TEXT," +
                "RAM TEXT," +
                "chipSet TEXT," +
                "heDieuHanh TEXT," +
                "manHinh TEXT," +
                "dungLuongPin TEXT," +
                "congSac TEXT," +
                "loaiPhuKien TEXT)";
        db.execSQL(createTableThuocTinhSanPham);
        //Tạo bảng hóa đơn
        String createTableHoaDon = "CREATE TABLE HoaDon(" +
                "maHD TEXT NOT NULL UNIQUE PRIMARY KEY," +
                "maNV TEXT NOT NULL REFERENCES NhanVien(maNV) ON DELETE CASCADE ON UPDATE CASCADE," +
                "maKH TEXT REFERENCES KhachHang(maKH) ON DELETE CASCADE ON UPDATE CASCADE," +
                "phanLoai INTEGER NOT NULL," +
                "trangThai INTEGER NOT NULL," +
                "ngay TEXT NOT NULL)";
        db.execSQL(createTableHoaDon);
        //Tạo bảng Chi tiết HĐ
        String createTableChiTietHoaDon = "CREATE TABLE ChiTietHoaDon(" +
                "maCTHD INTEGER PRIMARY KEY AUTOINCREMENT," +
                "maHD NOT NULL REFERENCES HoaDon(maHD) ON DELETE CASCADE ON UPDATE CASCADE," +
                "maSP NOT NULL REFERENCES SanPham(maSP) ON DELETE CASCADE ON UPDATE CASCADE," +
                "soLuong INTEGER NOT NULL," +
                "giamGia INTEGER," +
                "donGia TEXT NOT NULL," +
                "baoHanh INTEGER)";
        db.execSQL(createTableChiTietHoaDon);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newVS) {
        // Xóa bảng khi update VERSION

        String dropTableNhanVien = "DROP TABLE IF EXISTS NhanVien";
        db.execSQL(dropTableNhanVien);

        String dropTableKhachHang = "DROP TABLE IF EXISTS KhachHang";
        db.execSQL(dropTableKhachHang);

        String dropTableHang = "DROP TABLE IF EXISTS Hang";
        db.execSQL(dropTableHang);

        String dropTableSanPham = "DROP TABLE IF EXISTS SanPham";
        db.execSQL(dropTableSanPham);

        String dropTableThuocTinhSanPham = "DROP TABLE IF EXISTS ThuocTinhSanPham";
        db.execSQL(dropTableThuocTinhSanPham);

        String dropTableHoaDon = "DROP TABLE IF EXISTS HoaDon";
        db.execSQL(dropTableHoaDon);

        String dropTableChiTietHoaDon = "DROP TABLE IF EXISTS ChiTietHoaDon";
        db.execSQL(dropTableChiTietHoaDon);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON");
        }
    }
}
