package com.example.app_phone_store_manager_du_an_1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_phone_store_manager_du_an_1.database.DbHelper;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;

import java.util.ArrayList;
import java.util.List;

public class DaoHD {
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DaoHD(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long add(HoaDon hoaDon) {
        ContentValues values = new ContentValues();
        values.put(HoaDon.TB_COL_ID_HD, hoaDon.getMaHD());
        values.put(HoaDon.TB_COL_ID_NV, hoaDon.getMaNV());
        values.put(HoaDon.TB_COL_ID_KH, hoaDon.getMaKH());
        values.put(HoaDon.TB_COL_CLASS, hoaDon.getPhanLoai());
        values.put(HoaDon.TB_COL_DATE, hoaDon.getNgay());
        values.put(HoaDon.TB_COL_STATUS, hoaDon.getTrangThai());
        return database.insert(HoaDon.TB_NAME, null, values);

    }

    public int delete(String maHD) {
        return database.delete(HoaDon.TB_NAME, "maHD = ?", new String[]{maHD});
    }

    public int update(HoaDon hoaDon, String maHD) {
        ContentValues values = new ContentValues();

        values.put(HoaDon.TB_COL_ID_HD, hoaDon.getMaHD());
        values.put(HoaDon.TB_COL_ID_NV, hoaDon.getMaNV());
        values.put(HoaDon.TB_COL_ID_KH, hoaDon.getMaKH());
        values.put(HoaDon.TB_COL_CLASS, hoaDon.getPhanLoai());
        values.put(HoaDon.TB_COL_DATE, hoaDon.getNgay());
        values.put(HoaDon.TB_COL_STATUS, hoaDon.getTrangThai());

        return database.update(HoaDon.TB_NAME, values, "maHD = ?", new String[]{maHD});
    }

    public List<HoaDon> getAllNhap() {
        String sql = "SELECT * FROM HoaDon WHERE phanLoai = 0";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getAllXuat() {
        String sql = "SELECT * FROM HoaDon WHERE phanLoai = 1";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getNgay(String start, String end) {
        String sql = "SELECT * FROM HoaDon WHERE ngay >= ? AND ngay <= ?";
        List<HoaDon> list = getData(sql, start, end);
        return list;
    }

    public HoaDon getMaHD(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        List<HoaDon> list = getData(sql, maHD);
        return list.get(0);
    }

    public int checkMaHD(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        List<HoaDon> list = getData(sql, maHD);
        return list.size() == 0 ? -1 : 1;
    }

    public List<HoaDon> getDaysXuat() {
        String sql = "SELECT * FROM HoaDon WHERE ngay >= DATETIME('now', '-1 day') AND phanLoai = 1";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getWeekXuat() {
        String sql = "SELECT * FROM HoaDon WHERE ngay > DATETIME('now', '-7 day') AND phanLoai = 1 ";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getMonthXuat() {
        String sql = "SELECT * FROM HoaDon WHERE ngay > DATETIME('now', '-30 day') AND phanLoai = 1";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getDays() {
        String sql = "SELECT * FROM HoaDon WHERE ngay >= DATETIME('now', '-1 day') AND phanLoai = 0";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getWeek() {
        String sql = "SELECT * FROM HoaDon WHERE ngay > DATETIME('now', '-7 day') AND phanLoai = 0 ";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getMonth() {
        String sql = "SELECT * FROM HoaDon WHERE ngay > DATETIME('now', '-30 day') AND phanLoai = 0";
        List<HoaDon> list = getData(sql);
        return list;
    }

    public List<HoaDon> getData(String sql, String... args) {

        List<HoaDon> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, args);

        while (cursor.moveToNext()) {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHD(cursor.getString(cursor.getColumnIndex(HoaDon.TB_COL_ID_HD)));
            hoaDon.setMaNV(cursor.getString(cursor.getColumnIndex(HoaDon.TB_COL_ID_NV)));
            hoaDon.setMaKH(cursor.getString(cursor.getColumnIndex(HoaDon.TB_COL_ID_KH)));
            hoaDon.setPhanLoai(cursor.getInt(cursor.getColumnIndex(HoaDon.TB_COL_CLASS)));
            hoaDon.setNgay(cursor.getString(cursor.getColumnIndex(HoaDon.TB_COL_DATE)));
            hoaDon.setTrangThai(cursor.getInt(cursor.getColumnIndex(HoaDon.TB_COL_STATUS)));
            list.add(hoaDon);
        }
        return list;
    }
}
