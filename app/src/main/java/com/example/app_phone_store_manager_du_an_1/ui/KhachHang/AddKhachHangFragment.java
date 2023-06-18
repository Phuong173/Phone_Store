package com.example.app_phone_store_manager_du_an_1.ui.KhachHang;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.app_phone_store_manager_du_an_1.dao.DaoKhachHang;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;


public class AddKhachHangFragment extends Fragment {
    private NavController navController;
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private DaoKhachHang dao;
    private EditText edMaKH, edHoTenKH, edDienThoaiKH, edDiaChiKH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_khach_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        edMaKH = view.findViewById(R.id.edMaKhachHang);
        edHoTenKH = view.findViewById(R.id.edHoTenKH);
        edDienThoaiKH = view.findViewById(R.id.edSoDTKH);
        edDiaChiKH = view.findViewById(R.id.edDiachiKH);

        appCompatActivity = (AppCompatActivity) getActivity();

        appCompatActivity.getSupportActionBar().setTitle("Thêm Khách Hàng");
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        dao = new DaoKhachHang(getActivity());
        dao.open();
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
                navController.navigate(R.id.action_addKH_to_listKH);
                return true;
            case R.id.menu_reset:
                edMaKH.setText("");
                edHoTenKH.setText("");
                edDienThoaiKH.setText("");
                edDiaChiKH.setText("");
                return true;
            case R.id.menu_save:
                if (valiDate()) {
                    if (dao.checkMaKH(edMaKH.getText().toString()) > 0) {
                        Toast.makeText(appCompatActivity, "Đã có mã khách hàng trong hệ thống!", Toast.LENGTH_SHORT).show();
                    } else {
                        KhachHang khachHang = new KhachHang();
                        khachHang.setMaKH(edMaKH.getText().toString().replaceAll(" ",""));
                        khachHang.setHoTen(edHoTenKH.getText().toString());
                        khachHang.setDienThoai(edDienThoaiKH.getText().toString());
                        khachHang.setDiaChi(edDiaChiKH.getText().toString());
                        long kq = dao.addKH(khachHang);
                        if (kq > 0) {
                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_addKH_to_listKH);
                        } else {
                            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean valiDate() {
        if (edMaKH.getText().length() == 0 ||
                edHoTenKH.getText().length() == 0 ||
                edDienThoaiKH.getText().length() == 0 ||
                edDiaChiKH.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn cần nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaKH.getText().length() < 6 || edMaKH.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã khách hàng có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edMaKH.getText().toString().substring(0, 1).toUpperCase().equals(edMaKH.getText().toString().substring(0, 1)) ||
                !edHoTenKH.getText().toString().substring(0, 1).toUpperCase().equals(edHoTenKH.getText().toString().substring(0, 1)) ||
                !edDiaChiKH.getText().toString().substring(0, 1).toUpperCase().equals(edDiaChiKH.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên mã, họ tên, địa chỉ \n" +
                    "của nhân viên phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edDienThoaiKH.getText().length() < 10 ||
                edDienThoaiKH.getText().length() > 11 ||
                Pattern.matches("[a-zA-Z]+", edDienThoaiKH.getText().toString())) {
            Toast.makeText(appCompatActivity, "Sai độ dài số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}