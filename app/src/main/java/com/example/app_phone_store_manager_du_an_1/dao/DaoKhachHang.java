package com.example.app_phone_store_manager_du_an_1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_phone_store_manager_du_an_1.database.DbHelper;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DaoKhachHang {
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DaoKhachHang(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addKH(KhachHang khachHang) {
        ContentValues values = new ContentValues();
        values.put(KhachHang.TB_COL_ID, khachHang.getMaKH());
        values.put(KhachHang.TB_COL_NAME, khachHang.getHoTen());
        values.put(KhachHang.TB_COL_PHONE, khachHang.getDienThoai());
        values.put(KhachHang.TB_COL_LOCALE, khachHang.getDiaChi());
        return database.insert(KhachHang.TB_NAME, null, values);
    }

    public int updateKH(KhachHang khachHang, String maOld) {
        ContentValues values = new ContentValues();
        values.put(KhachHang.TB_COL_ID, khachHang.getMaKH());
        values.put(KhachHang.TB_COL_NAME, khachHang.getHoTen());
        values.put(KhachHang.TB_COL_PHONE, khachHang.getDienThoai());
        values.put(KhachHang.TB_COL_LOCALE, khachHang.getDiaChi());
        return database.update(KhachHang.TB_NAME, values, "maKH = ? ", new String[]{maOld});
    }

    public int deleteKH(KhachHang khachHang) {
        return database.delete(KhachHang.TB_NAME, "maKH = ? ", new String[]{khachHang.getMaKH()});
    }

    public List<KhachHang> getAll() {
        String sql = "SELECT * FROM KhachHang";
        List<KhachHang> list = getData(sql);
        return list;
    }

    public KhachHang getMaKH(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        List<KhachHang> list = getData(sql, maKH);
        return list.get(0);
    }

    public int checkMaKH(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        List<KhachHang> list = getData(sql, maKH);
        return list.size() == 0 ? -1 : 1;
    }

    public int getCountKH() {
        String sql = "SELECT COUNT(*) AS SoLuong FROM KhachHang";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("SoLuong"));
    }

    public int getCountSP() {
        String sql = "SELECT COUNT(*) AS SoLuong FROM SanPham";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("SoLuong"));
    }

    public int getCountHDN() {
        String sql = "SELECT COUNT(maHD) AS SoLuong FROM HoaDon WHERE phanLoai='0'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("SoLuong"));
    }

    public int getCountHDX() {
        String sql = "SELECT COUNT(maHD) AS SoLuong FROM HoaDon WHERE phanLoai='1'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("SoLuong"));
    }

    public int getSUM(String pl) {
        String doanhThu = "SELECT SUM(donGia * soLuong) AS tongTien FROM ChiTietHoaDon INNER JOIN HoaDon ON ChiTietHoaDon.maHD = HoaDon.maHD WHERE phanLoai = ? ";
        Cursor cursor = database.rawQuery(doanhThu, new String[]{pl});
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("tongTien"));
    }

    public List<HashMap<String, Integer>> getNhap(String pl) {
        List<HashMap<String, Integer>> list = new ArrayList<>();
        String doanhThu = "SELECT donGia , soLuong, giamGia FROM ChiTietHoaDon INNER JOIN HoaDon ON ChiTietHoaDon.maHD = HoaDon.maHD WHERE phanLoai = ? ";
        Cursor cursor = database.rawQuery(doanhThu, new String[]{pl});
        while (cursor.moveToNext()) {
            HashMap<String, Integer> data = new HashMap<>();
            data.put("donGia", cursor.getInt(cursor.getColumnIndex("donGia")));
            data.put("soLuong", cursor.getInt(cursor.getColumnIndex("soLuong")));
            data.put("giamGia", cursor.getInt(cursor.getColumnIndex("giamGia")));
            list.add(data);
        }
        return list;
    }

    public int getTongsl() {
        String sql = "SELECT Sum(soLuong) as tongsl FROM ChiTietHoaDon WHERE giamGia !='0'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("tongsl"));
    }

    public List<KhachHang> getAllSXTenKH() {
        String sql = "SELECT * FROM KhachHang ORDER BY hoTen ASC";
        List<KhachHang> list = getData(sql);
        return list;
    }

    public List<KhachHang> getAllSXMaKH() {
        String sql = "SELECT * FROM KhachHang ORDER BY maKH ASC";
        List<KhachHang> list = getData(sql);
        return list;
    }

    public List<KhachHang> getData(String sql, String... args) {
        List<KhachHang> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            KhachHang khachHang = new KhachHang();
            khachHang.setMaKH(cursor.getString(cursor.getColumnIndex(KhachHang.TB_COL_ID)));
            khachHang.setHoTen(cursor.getString(cursor.getColumnIndex(KhachHang.TB_COL_NAME)));
            khachHang.setDienThoai(cursor.getString(cursor.getColumnIndex(KhachHang.TB_COL_PHONE)));
            khachHang.setDiaChi(cursor.getString(cursor.getColumnIndex(KhachHang.TB_COL_LOCALE)));
            list.add(khachHang);
        }
        return list;
    }
}
