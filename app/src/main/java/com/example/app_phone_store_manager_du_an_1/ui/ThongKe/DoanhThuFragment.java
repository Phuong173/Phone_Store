package com.example.app_phone_store_manager_du_an_1.ui.ThongKe;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.adapter.DoanhThuAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHD;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class DoanhThuFragment extends Fragment {
    EditText edTuNgay, edDenNgay;
    ImageView imgTuNgay, imgDenNgay;
    DaoCTHD daoCTHD;
    DaoHD daoHD;
    Button btnThu;
    DoanhThuAdapter adapter;
    RecyclerView rvPM;
    TextView tvThu, tvChi;
    List<HoaDon> listHD;
    LinearLayout lnlXuat, lnlNhap;
    CardView lnlTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edTuNgay = view.findViewById(R.id.edTuNgay);
        edDenNgay = view.findViewById(R.id.edDenNgay);
        imgTuNgay = view.findViewById(R.id.imgTuNgay);
        imgDenNgay = view.findViewById(R.id.imgDenNgay);
        tvThu = view.findViewById(R.id.tvThu);
        lnlXuat = view.findViewById(R.id.lnlXuat);
        lnlNhap = view.findViewById(R.id.lnlNhap);
        lnlTable = view.findViewById(R.id.lnlTable);
        tvChi = view.findViewById(R.id.tvChi);
        btnThu = view.findViewById(R.id.btnXemThu);
        rvPM = view.findViewById(R.id.rvDoanhThu);

        lnlXuat.setVisibility(View.GONE);
        lnlNhap.setVisibility(View.GONE);
        lnlTable.setVisibility(View.GONE);
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        rvPM.setLayoutManager(manager);

        daoCTHD = new DaoCTHD(getActivity());
        daoCTHD.open();

        daoHD = new DaoHD(getActivity());
        daoHD.open();

        listHD = new ArrayList<>();

        adapter = new DoanhThuAdapter(getActivity(), listHD);
        rvPM.setAdapter(adapter);
        edTuNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(edTuNgay);
            }
        });
        edDenNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(edDenNgay);
            }
        });
        imgTuNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(edTuNgay);
            }
        });
        imgDenNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(edDenNgay);
            }
        });
        btnThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edTuNgay.getText().length() == 0 && edDenNgay.getText().length() == 0) {
                    Toast.makeText(getContext(), "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    String start = edTuNgay.getText().toString();
                    String end = edDenNgay.getText().toString();
                    lnlXuat.setVisibility(View.VISIBLE);
                    lnlNhap.setVisibility(View.VISIBLE);
                    lnlTable.setVisibility(View.VISIBLE);
                    double sumNhap = 0;
                    double sumXuat = 0;
                    List<HashMap<String,Integer>> listData = daoCTHD.getNhap(start,end,1+"");
                    for (HashMap<String, Integer> x : listData){
                        int donGia = x.get("donGia");
                        int soLuong = x.get("soLuong");
                        int khuyenMai = x.get("giamGia");
                        int giaTien = donGia * soLuong;
                        switch (khuyenMai){
                            case 0:
                                sumXuat += giaTien;
                                break;
                            case 1:
                                sumXuat += giaTien - (giaTien * 0.05);
                                break;
                            case 2:
                                sumXuat += giaTien - (giaTien * 0.1);
                                break;
                            case 3:
                                sumXuat += giaTien - (giaTien * 0.15);
                                break;
                            case 4:
                                sumXuat += giaTien - (giaTien * 0.2);
                                break;
                            case 5:
                                sumXuat += giaTien - (giaTien * 0.25);
                                break;
                            case 6:
                                sumXuat += giaTien - (giaTien * 0.3);
                                break;
                        }
                    }
                    sumNhap = daoCTHD.getSUM(start,end,0+"");
                    tvThu.setText(formatter.format(sumNhap) + " đ");
                    tvChi.setText(formatter.format(sumXuat) + " đ");
                    listHD.clear();
                    listHD.addAll(daoHD.getNgay(start, end));
                    adapter.filter(listHD);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doanh_thu, container, false);
    }

    public void datePicker(EditText ed) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                ed.setText(Utilities.dateToString(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoCTHD.close();
        daoHD.close();
    }
}