package com.example.app_phone_store_manager_du_an_1.ui.TaiKhoan;

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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;

import org.jetbrains.annotations.NotNull;


public class ChiTietTaiKhoanFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private DaoNhanVien dao;
    private String maNV;
    private TextView tvHoTen, tvTaiKhoan, tvDiaChi, tvDienThoai, tvNamSinh;
    private Button btnResatMK;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chi_tiet_tk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        navController = Navigation.findNavController(view);
        tvHoTen = view.findViewById(R.id.tvTenShow);
        tvDienThoai = view.findViewById(R.id.tvSdtTkShow);
        tvTaiKhoan = view.findViewById(R.id.tvTkShow);
        tvDiaChi = view.findViewById(R.id.tvDiaChiTkShow);
        tvNamSinh = view.findViewById(R.id.tvNamSinhTkShow);
        btnResatMK = view.findViewById(R.id.btnDatMk);
        maNV = getArguments().getString("maNV");

        appCompatActivity = (AppCompatActivity) getActivity();

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("#" + maNV);

        dao = new DaoNhanVien(getContext());
        dao.openNV();
        NhanVien nhanVien = dao.getMaNV(maNV);
        tvHoTen.setText(nhanVien.getHoTen());
        tvDienThoai.setText(nhanVien.getDienThoai());
        tvTaiKhoan.setText(nhanVien.getTaiKhoan());
        tvDiaChi.setText(nhanVien.getDiaChi());
        tvNamSinh.setText(nhanVien.getNamSinh());

        btnResatMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResartMk(nhanVien);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigate(R.id.ChiTietTk_to_ListTk);
                return true;
            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putString("maNV", maNV);
                navController.navigate(R.id.ChiTietTk_to_editTk, bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dialogResartMk(NhanVien nhanVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Đặt Lại Mật Khẩu");
        builder.setMessage("Bạn Muốn Đặt Lại Mật Khẩu Mặc Định?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putString("maNV", maNV);
                nhanVien.setMatKhau("123456");
                int kq = dao.updateNV(nhanVien, maNV);
                if (kq > 0) {
                    Toast.makeText(getContext(), "Resert Mật Khẩu Mặc Định Thành Công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Resert Mật Khẩu Mặc Định Thất Bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.closeNV();
    }
}