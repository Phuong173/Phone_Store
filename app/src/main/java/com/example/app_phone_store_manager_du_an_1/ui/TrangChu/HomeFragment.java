package com.example.app_phone_store_manager_du_an_1.ui.TrangChu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app_phone_store_manager_du_an_1.MainActivity;
import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoKhachHang;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView tv_SLKH, tv_SLSP, tv_SLHDN, tv_SLHDX, tvsoHD, tv_SLBan, tv_tongsl;
    private LinearLayout ln_KH, ln_SP, ln_HDN, ln_HDX;
    private DaoKhachHang daoKH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ln_KH = view.findViewById(R.id.car_KH);
        ln_SP = view.findViewById(R.id.car_SP);
        ln_HDN = view.findViewById(R.id.car_HDN);
        ln_HDX = view.findViewById(R.id.car_HDX);
        tv_SLKH = view.findViewById(R.id.tv_SLKH);
        tv_SLSP = view.findViewById(R.id.tv_SLSP);
        tv_SLHDN = view.findViewById(R.id.tv_SLHDN);
        tv_SLHDX = view.findViewById(R.id.tv_SLHDX);
        tvsoHD = view.findViewById(R.id.tv_soHD);
        tv_SLBan = view.findViewById(R.id.tv_SLBan);
        tv_tongsl = view.findViewById(R.id.tv_bank);
        onClick();
        daoKH = new DaoKhachHang(getContext());
        daoKH.open();
        tv_SLKH.setText(daoKH.getCountKH() + "");
        tv_SLSP.setText(daoKH.getCountSP() + "");
        tv_SLHDN.setText(daoKH.getCountHDN() + "");
        tv_SLHDX.setText(daoKH.getCountHDX() + "");
        tvsoHD.setText(daoKH.getCountHDX() + "");
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        double sumXuat = 0;
        int sl = 0;

        List<HashMap<String, Integer>> listData = daoKH.getNhap(1 + "");
        for (HashMap<String, Integer> x : listData) {
            int donGia = x.get("donGia");
            int soLuong = x.get("soLuong");
            int khuyenMai = x.get("giamGia");
            int giaTien = donGia * soLuong;
            sl += soLuong;
            switch (khuyenMai) {
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
        tv_SLBan.setText(sl + "");
        tv_tongsl.setText(formatter.format(sumXuat) + " đ");
    }

    private void onClick() {
        ln_SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setClick(R.id.nav_sanPham);
            }
        });
        ln_KH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setClick(R.id.nav_khachHang);
            }
        });
        ln_HDX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setClick(R.id.nav_hoaDonXuat);
            }
        });
        ln_HDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setClick(R.id.nav_hoaDonNhap);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoKH.close();
    }
}