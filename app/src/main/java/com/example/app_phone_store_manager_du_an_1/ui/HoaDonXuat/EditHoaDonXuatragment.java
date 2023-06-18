package com.example.app_phone_store_manager_du_an_1.ui.HoaDonXuat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.adapter.ChonKHAdapter;
import com.example.app_phone_store_manager_du_an_1.adapter.ChonSanPhamAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoKhachHang;
import com.example.app_phone_store_manager_du_an_1.dao.DaoSanPham;
import com.example.app_phone_store_manager_du_an_1.model.ChiTietHoaDon;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemKhachHangClick;
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


public class EditHoaDonXuatragment extends Fragment {
    private Drawable drawable;
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private RecyclerView rvSP;
    private TextView tvTitleDL;
    private DecimalFormat formatter;
    private Button btnSave, btnCancel;
    private RadioButton cbKoBH, cbBH6T, cbBH12T;
    private EditText edMaHD, edNgay, edSP, edKH, edSL, edDonGia, edThanhTien;
    private Spinner spinnerTT, spinnerKM;
    private DaoSanPham daoSP;
    private DaoCTHD daoCTHD;
    private DaoKhachHang daoKH;
    private DaoHD daoHD;
    private ChonSanPhamAdapter adapter;
    private ChonKHAdapter adapterKH;
    private List<SanPham> listSP;
    private List<KhachHang> listKH;
    private String tenSP;
    private String donGia;
    private double thanhTien;
    private String maHD;
    private String maHDOld;
    private String soLuong;
    private int khuyenMai;
    private int baoHanh;
    private HoaDon hoaDon;
    private int position;
    private String maKH;
    private String tenKH;
    private String ngay;
    private int trangThai;
    private List<SanPham> listChon = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_hoa_don_xuat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
        checkBundle();
        hoaDon = daoHD.getMaHD(maHD);
        maKH = hoaDon.getMaKH();
        setForm();
        eventDialog();
    }

    private void setForm() {
        setSpinner();
        setSpinnerKM();

        edMaHD.setText(hoaDon.getMaHD());
        edNgay.setText(hoaDon.getNgay());
        KhachHang khachHang = daoKH.getMaKH(hoaDon.getMaKH());
        tenKH = khachHang.getHoTen();
        List<ChiTietHoaDon> hoaDonList = daoCTHD.getListMaHD(hoaDon.getMaHD());
        String tenSPDF = "";
        String soLuongDF = "";
        String donGiaDF = "";
        double tienDF = 0;
        int khuyenMaiDF = 0;
        int baohanhDF = 0;
        for (ChiTietHoaDon x : hoaDonList) {
            SanPham sanPham = daoSP.getMaSP(x.getMaSP());
            sanPham.setTinhTrang(x.getSoLuong());
            listChon.add(sanPham);
            if (tenSPDF.equals("")) {
                tenSPDF = sanPham.getTenSP();
                soLuongDF = sanPham.getTenSP() + ": " + x.getSoLuong();
                donGiaDF = sanPham.getTenSP() + ": " + formatter.format(x.getDonGia()) + " đ";
                tienDF = x.getDonGia() * x.getSoLuong();
                khuyenMaiDF = x.getGiamGia();
                baohanhDF = x.getBaoHanh();

            } else {
                tenSPDF += " , " + sanPham.getTenSP();
                soLuongDF += " , " + sanPham.getTenSP() + ": " + x.getSoLuong();
                donGiaDF += " , " + sanPham.getTenSP() + ": " + formatter.format(x.getDonGia()) + " đ";
                tienDF += (x.getDonGia() * x.getSoLuong());
            }
        }
        edKH.setText(khachHang.getHoTen());
        edSP.setText(tenSPDF);
        edSL.setText(soLuongDF);
        edDonGia.setText(donGiaDF);
        thanhTien = tienDF;
        switch (khuyenMaiDF) {
            case 0:
                spinnerKM.setSelection(0);
                break;
            case 1:
                spinnerKM.setSelection(1);
                break;
            case 2: ;
                spinnerKM.setSelection(2);
                break;
            case 3:
                spinnerKM.setSelection(3);
                break;
            case 4:
                spinnerKM.setSelection(4);
                break;
            case 5:
                spinnerKM.setSelection(5);
                break;
            case 6:
                spinnerKM.setSelection(6);
                break;
        }

        switch (hoaDon.getTrangThai()) {
            case 0:
                spinnerTT.setSelection(1);
                break;
            case 1:
                spinnerTT.setSelection(2);
                break;
            case 2:
                spinnerTT.setSelection(3);
                break;
            default:
                spinnerTT.setSelection(0);
                break;
        }
        switch (baohanhDF) {
            case 0:
                cbBH6T.setChecked(true);
                break;
            case 1:
                cbBH12T.setChecked(true);
                break;
            default:
                cbKoBH.setChecked(true);
                break;
        }
    }

    private void anhXa(View view) {
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Cập nhập HĐ Xuất");

        edMaHD = view.findViewById(R.id.edMaHDXuatChange);
        edNgay = view.findViewById(R.id.edNgayXuatChange);
        edSP = view.findViewById(R.id.edSpXuatChange);
        edKH = view.findViewById(R.id.edKhXuatChange);
        edSL = view.findViewById(R.id.edSoLuongXuatChange);
        edDonGia = view.findViewById(R.id.edDonGiaXuatChange);
        edThanhTien = view.findViewById(R.id.edThanhTienChange);

        cbKoBH = view.findViewById(R.id.cbKoBHChange);
        cbBH6T = view.findViewById(R.id.cbBH6TChange);
        cbBH12T = view.findViewById(R.id.cbBH12TChange);

        spinnerTT = view.findViewById(R.id.spTTHDXChange);
        spinnerKM = view.findViewById(R.id.spKMHDXChange);

        daoSP = new DaoSanPham(appCompatActivity);
        daoSP.open();

        daoHD = new DaoHD(appCompatActivity);
        daoHD.open();

        daoCTHD = new DaoCTHD(appCompatActivity);
        daoCTHD.open();

        daoKH = new DaoKhachHang(appCompatActivity);
        daoKH.open();

        cbKoBH.setChecked(true);
        formatter = new DecimalFormat("###,###,###");
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

    private void dialogChonKH() {
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
        tvTitleDL.setText("Chọn khách hàng");

        listKH = new ArrayList<>();
        listKH = daoKH.getAll();


        adapterKH = new ChonKHAdapter(listKH);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(appCompatActivity);
        manager.setJustifyContent(JustifyContent.CENTER);
        manager.setAlignItems(AlignItems.CENTER);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setFlexWrap(FlexWrap.WRAP);
        rvSP.setLayoutManager(manager);
        rvSP.setAdapter(adapterKH);

        if (edKH.length() > 0) {
            checkSelected();
            adapterKH.setCheckedPositon(position);
        }

        adapterKH.setItemKhachHangClick(new ItemKhachHangClick() {
            @Override
            public void OnItemClick(KhachHang khachHang) {
                if (edKH.length() == 0) {
                    tenKH = khachHang.getHoTen();
                    maKH = khachHang.getMaKH();
                    edKH.setText(tenKH);
                    Toast.makeText(appCompatActivity, "Chọn khách hàng thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    tenKH = khachHang.getHoTen();
                    maKH = khachHang.getMaKH();
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
                edKH.setText(tenKH);
                Toast.makeText(appCompatActivity, "Chọn Khách hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void checkSelected() {
        for (KhachHang x : listKH) {
            if (x.getHoTen().equals(tenKH)) {
                position = listKH.indexOf(x);
            }
        }
        adapterKH.setCheckedPositon(position);

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

    public void formatBaoHanh() {
        if (cbKoBH.isChecked()) {
            baoHanh = -1;
        }
        if (cbBH6T.isChecked()) {
            baoHanh = 0;
        }
        if (cbBH12T.isChecked()) {
            baoHanh = 1;
        }
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(appCompatActivity, R.array.tinhTrang, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);
        spinnerTT.setAdapter(spAdapter);
        spinnerTT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        trangThai = 0;
                        break;
                    case 2:
                        trangThai = 1;
                        break;
                    case 3:
                        trangThai = 2;
                        break;
                    case 0:
                        trangThai = -1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setSpinnerKM() {
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(appCompatActivity, R.array.khuyenMai, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);
        spinnerKM.setAdapter(spAdapter);

        spinnerKM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        khuyenMai = 1;
                        edThanhTien.setText(formatter.format(thanhTien - (thanhTien * 0.05)) + " đ");
                        break;
                    case 2:
                        khuyenMai = 2;
                        edThanhTien.setText(formatter.format(thanhTien - (thanhTien * 0.1)) + " đ");
                        break;
                    case 3:
                        khuyenMai = 3;
                        edThanhTien.setText(formatter.format(thanhTien - (thanhTien * 0.15)) + " đ");
                        break;
                    case 4:
                        khuyenMai = 4;
                        edThanhTien.setText(formatter.format(thanhTien - (thanhTien * 0.2)) + " đ");
                        break;
                    case 5:
                        khuyenMai = 5;
                        edThanhTien.setText(formatter.format(thanhTien - (thanhTien * 0.25)) + " đ");
                        break;
                    case 6:
                        khuyenMai = 6;
                        edThanhTien.setText(formatter.format(thanhTien - (thanhTien * 0.3)) + " đ");
                        break;
                    case 0:
                        edThanhTien.setText(formatter.format(thanhTien * 1) + " đ");
                        khuyenMai = 0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        listSP = new ArrayList<>();
        listSP = daoSP.getAllSPBan();
        adapter = new ChonSanPhamAdapter(appCompatActivity, listSP);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(appCompatActivity);
        manager.setJustifyContent(JustifyContent.CENTER);
        manager.setAlignItems(AlignItems.CENTER);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setFlexWrap(FlexWrap.WRAP);
        rvSP.setLayoutManager(manager);
        rvSP.setAdapter(adapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edSP.getText().length() == 0 && edSL.getText().length() == 0) {
                    spinnerKM.setSelection(0);
                    listChon = adapter.getListSelected();
                    double giaTien;
                    double sl;
                    double tien = 0;
                    for (SanPham x : listChon) {
                        if (x.getTrangThai() > 0) {
                            if (tenSP == null) {
                                donGia = x.getTenSP() + ": " + formatter.format(x.getGiaTien()) + " đ";
                                soLuong = x.getTenSP() + ": " + x.getTinhTrang() + "";
                                tenSP = x.getTenSP();
                                giaTien = x.getGiaTien();
                                sl = x.getTinhTrang();
                                tien = giaTien * sl;
                            } else if (tenSP.equals("")) {
                                donGia = x.getTenSP() + ": " + formatter.format(x.getGiaTien());
                                tenSP = x.getTenSP();
                                soLuong = x.getTenSP() + ": " + x.getTinhTrang() + "";
                                giaTien = x.getGiaTien();
                                sl = x.getTinhTrang();
                                tien = giaTien * sl;
                            } else {
                                tenSP += " , " + x.getTenSP();
                                donGia += " , " + x.getTenSP() + ": " + formatter.format(x.getGiaTien()) + " đ";
                                soLuong += " , " + x.getTenSP() + ": " + x.getTinhTrang() + "";
                                giaTien = x.getGiaTien();
                                sl = x.getTinhTrang();
                                tien += (giaTien * sl);
                            }
                        }
                    }
                    thanhTien = tien;
                    edSP.setText(tenSP);
                    edSL.setText(soLuong + "");
                    edDonGia.setText(donGia);
                    edThanhTien.setText(formatter.format(tien) + " đ");
                    dialog.dismiss();
                } else {
                    thanhTien = 0;
                    tenSP = "";
                    donGia = "";
                    soLuong = "";
                    double giaTien;
                    double sl;
                    double tien = 0;
                    spinnerKM.setSelection(0);
                    listChon = adapter.getListSelected();
                    for (SanPham x : listChon) {
                        if (x.getTrangThai() > 0) {
                            if (tenSP == null) {
                                donGia = x.getTenSP() + ": " + formatter.format(x.getGiaTien()) + " đ";
                                soLuong = x.getTenSP() + ": " + x.getTinhTrang() + "";
                                tenSP = x.getTenSP();
                                giaTien = x.getGiaTien();
                                sl = x.getTinhTrang();
                                tien = giaTien * sl;
                            } else if (tenSP.equals("")) {
                                donGia = x.getTenSP() + ": " + formatter.format(x.getGiaTien());
                                tenSP = x.getTenSP();
                                soLuong = x.getTenSP() + ": " + x.getTinhTrang() + "";
                                giaTien = x.getGiaTien();
                                sl = x.getTinhTrang();
                                tien = giaTien * sl;
                            } else {
                                tenSP += " , " + x.getTenSP();
                                donGia += " , " + x.getTenSP() + ": " + formatter.format(x.getGiaTien()) + " đ";
                                soLuong += " , " + x.getTenSP() + ": " + x.getTinhTrang() + "";
                                giaTien = x.getGiaTien();
                                sl = x.getTinhTrang();
                                tien += (giaTien * sl);
                            }
                        }
                    }
                    thanhTien = tien;
                    edSP.setText(tenSP);
                    edSL.setText(soLuong + "");
                    edDonGia.setText(donGia);
                    edThanhTien.setText(formatter.format(tien) + " đ");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void resetFrom() {
        tenSP = "";
        donGia = "";
        thanhTien = 0;
        soLuong = "";
        edMaHD.setText("");
        edNgay.setText("");
        edSP.setText("");
        edKH.setText("");
        cbKoBH.setChecked(true);
        spinnerTT.setSelection(0);
        spinnerKM.setSelection(0);
        edSL.setText("");
        edDonGia.setText("");
        edThanhTien.setText("");
    }

    private void eventDialog() {
        edMaHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(appCompatActivity, "Không được sửa mã hóa đơn", Toast.LENGTH_SHORT).show();
            }
        });
        edSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChonSP();
            }
        });
        edNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDate();
            }
        });
        edKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChonKH();
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
    }

    public void checkBundle() {
        if (getArguments() != null) {
            maHD = getArguments().getString("maHDEdit");
            maHDOld = maHD;
        }
    }
    private boolean checkAddCTHD(HoaDon hoaDon){
        for (SanPham x : listChon) {
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setMaHD(hoaDon.getMaHD());
            chiTietHoaDon.setMaSP(x.getMaSP());
            chiTietHoaDon.setSoLuong(x.getTinhTrang());
            chiTietHoaDon.setDonGia(x.getGiaTien());
            chiTietHoaDon.setGiamGia(khuyenMai);
            chiTietHoaDon.setBaoHanh(baoHanh);
            long kqCTHD = daoCTHD.add(chiTietHoaDon);

            if (kqCTHD < 0){
                return false;
            }
        }
        return true;
    }

    private boolean validate() {
        if (edMaHD.getText().length() == 0 ||
                edNgay.getText().length() == 0 ||
                edSP.getText().length() == 0 ||
                edSL.getText().length() == 0 ||
                edKH.getText().length() == 0 ||
                edDonGia.getText().length() == 0 ||
                edThanhTien.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaHD.getText().length() < 6 || edMaHD.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã hóa đơn có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean deleteCTHD(String maHD){
        List<ChiTietHoaDon> listDelete = daoCTHD.getListMaHD(maHD);
        for (ChiTietHoaDon delete : listDelete){
            int kqDelete = daoCTHD.delete(delete);
            if (kqDelete < 0){
                return false;
            }
        }
        return true;
    }
    private void dialogBack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle("Thoát cập nhập");
        builder.setMessage("Bạn có chắc chắn muốn thoát không. \nDữ liệu sẽ không bị thay đổi!");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (maHDOld != null){
                    Bundle bundle = new Bundle();
                    bundle.putString("maHD", hoaDon.getMaHD());
                    navController.navigate(R.id.editHDX_to_chiTietHDX, bundle);
                }else {
                    navController.navigate(R.id.editHDX_to_listHDX);
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dialogBack();
                return true;
            case R.id.menu_reset:
                resetFrom();
                return true;
            case R.id.menu_save:
                if (validate()){
                    maHD = edMaHD.getText().toString().replaceAll(" ","");
                    ngay = edNgay.getText().toString();
                    formatBaoHanh();
                    hoaDon.setMaHD(maHD);
                    hoaDon.setNgay(ngay);
                    hoaDon.setMaKH(maKH);
                    hoaDon.setTrangThai(trangThai);

                    int update = daoHD.update(hoaDon, maHDOld);
                    if (update > 0){
                        if (deleteCTHD(hoaDon.getMaHD())){
                            if (checkAddCTHD(hoaDon)){
                                Toast.makeText(appCompatActivity, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.editHDX_to_listHDX);
                            }else {
                                Toast.makeText(appCompatActivity, "Tạo chi tiết hóa đơn thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(appCompatActivity, "Cập nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(appCompatActivity, "Cập nhập thất bại", Toast.LENGTH_SHORT).show();
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
        daoKH.close();
    }
}