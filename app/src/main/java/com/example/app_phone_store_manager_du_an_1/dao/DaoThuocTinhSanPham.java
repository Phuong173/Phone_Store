package com.example.app_phone_store_manager_du_an_1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_phone_store_manager_du_an_1.database.DbHelper;
import com.example.app_phone_store_manager_du_an_1.model.ThuocTinhSanPham;

import java.util.ArrayList;
import java.util.List;

public class DaoThuocTinhSanPham {
    SQLiteDatabase database;
    DbHelper dbHelper;

    public DaoThuocTinhSanPham(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long add(ThuocTinhSanPham thuocTinhSanPham) {
        ContentValues values = new ContentValues();

        values.put(ThuocTinhSanPham.TB_COL_ID_SP, thuocTinhSanPham.getMaSP());
        values.put(ThuocTinhSanPham.TB_COL_MEMORY, thuocTinhSanPham.getBoNho());
        values.put(ThuocTinhSanPham.TB_COL_RAM, thuocTinhSanPham.getRAM());
        values.put(ThuocTinhSanPham.TB_COL_CHIP, thuocTinhSanPham.getChipSet());
        values.put(ThuocTinhSanPham.TB_COL_OS, thuocTinhSanPham.getHeDieuHanh());
        values.put(ThuocTinhSanPham.TB_COL_SCREEN, thuocTinhSanPham.getManHinh());
        values.put(ThuocTinhSanPham.TB_COL_BATTERY, thuocTinhSanPham.getDungLuongPin());
        values.put(ThuocTinhSanPham.TB_COL_TYPE, thuocTinhSanPham.getCongSac());
        values.put(ThuocTinhSanPham.TB_COL_LOAI, thuocTinhSanPham.getLoaiPhuKien());

        return database.insert(ThuocTinhSanPham.TB_NAME, null, values);
    }

    public int delete(int maTT) {
        return database.delete(ThuocTinhSanPham.TB_COL_ID_SP, "maTT = ?", new String[]{maTT + ""});
    }

    public int update(ThuocTinhSanPham thuocTinhSanPham) {
        ContentValues values = new ContentValues();

        values.put(ThuocTinhSanPham.TB_COL_ID_SP, thuocTinhSanPham.getMaTT());
        values.put(ThuocTinhSanPham.TB_COL_ID_SP, thuocTinhSanPham.getMaSP());
        values.put(ThuocTinhSanPham.TB_COL_MEMORY, thuocTinhSanPham.getBoNho());
        values.put(ThuocTinhSanPham.TB_COL_RAM, thuocTinhSanPham.getRAM());
        values.put(ThuocTinhSanPham.TB_COL_CHIP, thuocTinhSanPham.getChipSet());
        values.put(ThuocTinhSanPham.TB_COL_OS, thuocTinhSanPham.getHeDieuHanh());
        values.put(ThuocTinhSanPham.TB_COL_SCREEN, thuocTinhSanPham.getManHinh());
        values.put(ThuocTinhSanPham.TB_COL_BATTERY, thuocTinhSanPham.getDungLuongPin());
        values.put(ThuocTinhSanPham.TB_COL_TYPE, thuocTinhSanPham.getCongSac());
        values.put(ThuocTinhSanPham.TB_COL_LOAI, thuocTinhSanPham.getLoaiPhuKien());

        return database.update(ThuocTinhSanPham.TB_NAME, values, "maTT = ?", new String[]{thuocTinhSanPham.getMaTT() + ""});
    }

    public List<ThuocTinhSanPham> getAll() {
        String sql = "SELECT * FROM ThuocTinhSanPham";
        List<ThuocTinhSanPham> list = getData(sql);
        return list;
    }

    public int checkTTMaSP(String maSP) {
        String sql = "SELECT * FROM ThuocTinhSanPham WHERE maSP = ? ";
        List<ThuocTinhSanPham> list = getData(sql, maSP);
        return list.size() == 0 ? -1 : 1;
    }

    public ThuocTinhSanPham getMaSP(String maSP) {
        String sql = "SELECT * FROM ThuocTinhSanPham WHERE maSP = ? ";
        List<ThuocTinhSanPham> list = getData(sql, maSP);
        return list.get(0);
    }

    public List<ThuocTinhSanPham> getData(String sql, String... args) {
        List<ThuocTinhSanPham> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            ThuocTinhSanPham thuocTinhSanPham = new ThuocTinhSanPham();

            thuocTinhSanPham.setMaTT(cursor.getInt(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_ID_TT)));
            thuocTinhSanPham.setMaSP(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_ID_SP)));
            thuocTinhSanPham.setBoNho(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_MEMORY)));
            thuocTinhSanPham.setRAM(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_RAM)));
            thuocTinhSanPham.setChipSet(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_CHIP)));
            thuocTinhSanPham.setHeDieuHanh(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_OS)));
            thuocTinhSanPham.setManHinh(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_SCREEN)));
            thuocTinhSanPham.setDungLuongPin(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_BATTERY)));
            thuocTinhSanPham.setCongSac(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_TYPE)));
            thuocTinhSanPham.setLoaiPhuKien(cursor.getString(cursor.getColumnIndex(ThuocTinhSanPham.TB_COL_LOAI)));

            list.add(thuocTinhSanPham);
        }
        return list;
    }
}
