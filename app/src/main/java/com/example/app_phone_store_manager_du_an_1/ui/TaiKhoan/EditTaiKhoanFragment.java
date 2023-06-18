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
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class EditTaiKhoanFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private String maNV;
    private EditText edMaNV, edHoTenNV, edNamSinhNV, edDiaChiNV, edTaiKhoanNV, edDienThoaiNV;
    private DaoNhanVien dao;
    private NhanVien nhanVien;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_tk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        edMaNV = view.findViewById(R.id.edMaNvTkChange);
        edHoTenNV = view.findViewById(R.id.ed_HoTen_TkChange);
        edDienThoaiNV = view.findViewById(R.id.ed_Sdt_TkChange);
        edTaiKhoanNV = view.findViewById(R.id.ed_Tk_TkChange);
        edDiaChiNV = view.findViewById(R.id.ed_DiaChi_TkChange);
        edNamSinhNV = view.findViewById(R.id.ed_NamSinh_TkChange);

        maNV = getArguments().getString("maNV");

        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Cập nhập Tài Khoản");

        dao = new DaoNhanVien(getActivity());
        dao.openNV();

        nhanVien = dao.getMaNV(maNV);

        edMaNV.setText(nhanVien.getMaNV());
        edHoTenNV.setText(nhanVien.getHoTen());
        edDienThoaiNV.setText(nhanVien.getDienThoai());
        edTaiKhoanNV.setText(nhanVien.getTaiKhoan());
        edDiaChiNV.setText(nhanVien.getDiaChi());
        edNamSinhNV.setText(nhanVien.getNamSinh());

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
                AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
                builder.setTitle("Thoát cập nhập");
                builder.setMessage("Bạn có chắc chắn muốn thoát không. \nDữ liệu sẽ không bị thay đổi!");
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (maNV != null){
                            Bundle bundle = new Bundle();
                            bundle.putString("maNV", maNV);
                            navController.navigate(R.id.editTk_to_ChiTietTk, bundle);
                        }else {
                            navController.navigate(R.id.editTk_to_ListTk);
                        }
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.menu_reset:
                edMaNV.setText("");
                edHoTenNV.setText("");
                edDienThoaiNV.setText("");
                edTaiKhoanNV.setText("");
                edDiaChiNV.setText("");
                edNamSinhNV.setText("");
                return true;
            case R.id.menu_save:
                if (valiDate()) {
                    if (nhanVien.getMaNV().equals(edMaNV.getText().toString()) &&
                            nhanVien.getHoTen().equals(edHoTenNV.getText().toString()) &&
                            nhanVien.getDienThoai().equals(edDienThoaiNV.getText().toString()) &&
                            nhanVien.getTaiKhoan().equals(edTaiKhoanNV.getText().toString()) &&
                            nhanVien.getNamSinh().equals(edNamSinhNV.getText().toString()) &&
                            nhanVien.getDiaChi().equals(edDiaChiNV.getText().toString())) {
                        Toast.makeText(appCompatActivity, "Không có thay đổi để cập nhập!", Toast.LENGTH_SHORT).show();
                    } else {
                        nhanVien.setMaNV(edMaNV.getText().toString());
                        nhanVien.setHoTen(edHoTenNV.getText().toString());
                        nhanVien.setDienThoai(edDienThoaiNV.getText().toString());
                        nhanVien.setDiaChi(edDiaChiNV.getText().toString());
                        nhanVien.setTaiKhoan(edTaiKhoanNV.getText().toString());
                        nhanVien.setNamSinh(edNamSinhNV.getText().toString());
                        int kq = dao.updateNV(nhanVien, maNV);
                        if (kq > 0) {
                            Toast.makeText(getContext(), "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.editTk_to_ListTk);
                        } else {
                            Toast.makeText(getContext(), "Cập nhập thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean valiDate() {
        if (edMaNV.getText().length() == 0 ||
                edHoTenNV.getText().length() == 0 ||
                edTaiKhoanNV.getText().length() == 0 ||
                edDienThoaiNV.getText().length() == 0 ||
                edDiaChiNV.getText().length() == 0 ||
                edNamSinhNV.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn cần nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaNV.getText().length() < 6 || edMaNV.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã nhân viên có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edMaNV.getText().toString().substring(0, 1).toUpperCase().equals(edMaNV.getText().toString().substring(0, 1)) ||
                !edHoTenNV.getText().toString().substring(0, 1).toUpperCase().equals(edHoTenNV.getText().toString().substring(0, 1)) ||
                !edDiaChiNV.getText().toString().substring(0, 1).toUpperCase().equals(edDiaChiNV.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên hãng phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edDienThoaiNV.getText().length() < 10 ||
                edDienThoaiNV.getText().length() > 11 ||
                Pattern.matches("[a-zA-Z]+", edDienThoaiNV.getText().toString())) {
            Toast.makeText(appCompatActivity, "Sai độ dài số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.closeNV();
    }
}