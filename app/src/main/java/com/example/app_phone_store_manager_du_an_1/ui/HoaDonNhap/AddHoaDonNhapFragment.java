package com.example.app_phone_store_manager_du_an_1.ui.HoaDonNhap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.adapter.ChonSPAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.dao.DaoSanPham;
import com.example.app_phone_store_manager_du_an_1.model.ChiTietHoaDon;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemSanPhamClick;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddHoaDonNhapFragment extends Fragment {
    private Drawable drawable;
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private EditText edMaHD, edNgay, edSanPham, edSL, edDonGia, edThanhTien;
    private DaoHD daoHD;
    private DaoCTHD daoCTHD;
    private DaoSanPham daoSP;
    private DaoNhanVien daoNV;
    private List<SanPham> list;
    private RecyclerView rvSP;
    private Button btnCancel, btnSave;
    private ChonSPAdapter adapter;
    private DecimalFormat formatter;
    private int position;
    private String tenSP;
    private String maSP;
    private TextView tvTitleDL;
    private String maHD;
    private String maNV;
    private String ngay;
    private int soLuong;
    private double donGia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_hoa_don_nhap, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        anhXa(view);

        openData();

        eventDialog();

    }

    private void openData() {
        daoHD = new DaoHD(appCompatActivity);
        daoHD.open();

        daoCTHD = new DaoCTHD(appCompatActivity);
        daoCTHD.open();

        daoSP = new DaoSanPham(appCompatActivity);
        daoSP.open();

        daoNV = new DaoNhanVien(appCompatActivity);
        daoNV.openNV();
    }

    private void eventDialog() {

        edNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDate();
            }
        });
        edSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChonSP();
            }
        });

        edDonGia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (thanhTien() != 0) {
                        edThanhTien.setText(formatter.format(thanhTien()) + "");
                    } else {
                        edThanhTien.setText("Null");
                    }
                }
            }
        });

        edSL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (thanhTien() != 0) {
                        edThanhTien.setText(formatter.format(thanhTien()) + "");
                    } else {
                        edThanhTien.setText("Null");
                    }
                }
            }
        });
    }

    public double thanhTien() {
        double thanhTien = 0;
        try {
            double sl = Double.parseDouble(edSL.getText().toString());
            double price = Double.parseDouble(edDonGia.getText().toString());
            thanhTien = price * sl;
        } catch (Exception e) {
            thanhTien = 0;
        }
        return thanhTien;
    }

    private void anhXa(View view) {
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Thêm HD Nhập");
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);

        edMaHD = view.findViewById(R.id.edMaHDNhap);
        edNgay = view.findViewById(R.id.edNgayNhap);
        edSanPham = view.findViewById(R.id.edSpNhap);
        edSL = view.findViewById(R.id.edSlNhap);
        edDonGia = view.findViewById(R.id.edDonGiaNhap);
        edThanhTien = view.findViewById(R.id.edTongTienNhap);

        formatter = new DecimalFormat("###,###,###");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
    }

    private void dialogChonSP() {
        LayoutInflater inflater = (LayoutInflater) appCompatActivity
                .getSystemService(appCompatActivity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dialog_chon_hang, null);

        AlertDialog dialog = new AlertDialog.Builder(appCompatActivity).create();
        dialog.setView(view);
        dialog.setCancelable(false);


        rvSP = (RecyclerView) view.findViewById(R.id.rvChonHang);
        btnCancel = (Button) view.findViewById(R.id.btnCancelChonHang);
        btnSave = (Button) view.findViewById(R.id.btnSaveChonHang);
        tvTitleDL = (TextView) view.findViewById(R.id.tvTitleDL);
        tvTitleDL.setText("Chọn sản phẩm");

        list = new ArrayList<>();
        list = daoSP.getAll();


        adapter = new ChonSPAdapter(list, appCompatActivity);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(appCompatActivity);
        manager.setJustifyContent(JustifyContent.CENTER);
        manager.setAlignItems(AlignItems.CENTER);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setFlexWrap(FlexWrap.WRAP);
        rvSP.setLayoutManager(manager);
        rvSP.setAdapter(adapter);

        if (edSanPham.length() > 0) {
            checkSelected();
            adapter.setCheckedPositon(position);
        }

        adapter.setItemSanPhamClick(new ItemSanPhamClick() {
            @Override
            public void ItemClick(SanPham sanPham) {
                if (edSanPham.length() == 0) {
                    tenSP = sanPham.getTenSP();
                    maSP = sanPham.getMaSP();
                    edSanPham.setText(tenSP);
                    Toast.makeText(appCompatActivity, "Chọn sản phảm thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    tenSP = sanPham.getTenSP();
                    maSP = sanPham.getMaSP();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                edSanPham.setText(tenSP);
                Toast.makeText(appCompatActivity, "Chọn sản phảm thành công", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void dialogDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                edNgay.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void checkSelected() {
        for (SanPham x : list) {
            if (x.getTenSP().equals(tenSP)) {
                position = list.indexOf(x);
            }
        }
        adapter.setCheckedPositon(position);
    }

    private boolean validate() {
        if (edMaHD.getText().length() == 0 ||
                edNgay.getText().length() == 0 ||
                edSanPham.getText().length() == 0 ||
                edSL.getText().length() == 0 ||
                edDonGia.getText().length() == 0 ||
                edThanhTien.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaHD.getText().length() < 6 || edMaHD.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã hóa đơn có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edSL.getText().length() > 0) {
            try {
                double checkSL = Integer.parseInt(edSL.getText().toString());
                if (checkSL <= 0) {
                    Toast.makeText(appCompatActivity, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                Toast.makeText(appCompatActivity, "Số lượng phải là số", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (edDonGia.getText().length() > 0) {
            try {
                double checkSL = Double.parseDouble(edDonGia.getText().toString());
                if (checkSL <= 0) {
                    Toast.makeText(appCompatActivity, "Đơn giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                Toast.makeText(appCompatActivity, "Đơn giá phải là số", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public void getMaNV() {
        SharedPreferences preferences = appCompatActivity.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String taiKhoan = preferences.getString("USER", "");
        maNV = daoNV.gettaiKhoan(taiKhoan).getMaNV();
    }

    private void resetFrom() {
        edMaHD.setText("");
        edNgay.setText("");
        edSanPham.setText("");
        edSL.setText("");
        edDonGia.setText("");
        edThanhTien.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigate(R.id.addHDNhap_to_listHDNhap);
                return true;
            case R.id.menu_reset:
                resetFrom();
                return true;
            case R.id.menu_save:
                if (validate()) {
                    maHD = edMaHD.getText().toString().replaceAll(" ","");
                    getMaNV();
                    ngay = edNgay.getText().toString();
                    soLuong = Integer.parseInt(edSL.getText().toString());
                    donGia = Double.parseDouble(edDonGia.getText().toString());

                    if (daoHD.checkMaHD(maHD) > 0){
                        Toast.makeText(appCompatActivity, "Mã hóa đơn đã tồn tại trong hệ thống!", Toast.LENGTH_SHORT).show();
                    }else {
                        HoaDon hoaDon = new HoaDon();
                        hoaDon.setMaHD(maHD);
                        hoaDon.setMaNV(maNV);
                        hoaDon.setPhanLoai(0);
                        hoaDon.setNgay(ngay);
                        hoaDon.setTrangThai(2);

                        long kq = daoHD.add(hoaDon);
                        if (kq > 0) {

                            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                            chiTietHoaDon.setMaHD(maHD);
                            chiTietHoaDon.setMaSP(maSP);
                            chiTietHoaDon.setSoLuong(soLuong);
                            chiTietHoaDon.setDonGia(donGia);
                            long kqCT = daoCTHD.add(chiTietHoaDon);
                            if (kqCT > 0) {
                                SanPham sanPham = daoSP.getMaSP(chiTietHoaDon.getMaSP());
                                sanPham.setTrangThai(1);
                                int updateSP = daoSP.update(sanPham, chiTietHoaDon.getMaSP());
                                if (updateSP > 0) {

                                    Toast.makeText(appCompatActivity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.addHDNhap_to_listHDNhap);
                                } else {
                                    Toast.makeText(appCompatActivity, "Cập nhập trạng thái Sản Phẩm thất bại", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(appCompatActivity, "Thêm hóa đơn chi tiết thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("kq", kq +"");
                            Toast.makeText(appCompatActivity, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoHD.close();
        daoCTHD.close();
        daoSP.close();
        daoNV.closeNV();
    }
}