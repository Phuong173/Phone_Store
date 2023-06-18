package com.example.app_phone_store_manager_du_an_1.ui.NguoiDung;

import android.content.Context;
import android.content.DialogInterface;
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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;

import org.jetbrains.annotations.NotNull;

public class DoiMatKhauFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private Drawable drawable;
    private String maNV;
    private DaoNhanVien daoNhanVien;
    private NhanVien nhanVien;
    private EditText edPassOld,edPassChange,edRePassChange;
    private Button btnReSetPassNguoiDung;
    private String user;
    private String pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edPassOld = view.findViewById(R.id.edPassOld);
        edPassChange= view.findViewById(R.id.edPassChange);
        edRePassChange = view.findViewById(R.id.edRePassChange);
        btnReSetPassNguoiDung = view.findViewById(R.id.btnReSetPassNguoiDung);

        appCompatActivity = (AppCompatActivity) getActivity();
        navController = Navigation.findNavController(view);
        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Đổi mật khẩu");

        daoNhanVien = new DaoNhanVien(getActivity());
        daoNhanVien.openNV();
         user = getActivity().getIntent().getStringExtra("user");
        nhanVien = daoNhanVien.gettaiKhoan(user);


        btnReSetPassNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPass(nhanVien);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doi_mat_khau, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                navController.navigate(R.id.doiMatKhau_to_chiTietNguoiDung);
                return true;

            case R.id.menu_save:
                if(validate()>0) {
                    nhanVien.setMatKhau(edPassChange.getText().toString());
                    pass = nhanVien.getMatKhau();
                    int kq = daoNhanVien.changePassword(nhanVien);
                    if (kq > 0) {
                        loadPref(user,pass);
                        Toast.makeText(getActivity(), "Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("maNV", maNV);
                        navController.navigate(R.id.doiMatKhau_to_chiTietNguoiDung,bundle);
                    } else {
                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT);
                    }
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int validate() {
            int check=1;
        if (edPassOld.getText().length() == 0 || edPassChange.getText().length() == 0 || edRePassChange.getText().length() == 0) {
            Toast.makeText(getContext(), "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return check=-1;
        } else {
            String oldPass = nhanVien.getMatKhau();
            String pass = edPassChange.getText().toString();
            String rePass = edRePassChange.getText().toString();
            if (!oldPass.equals(edPassOld.getText().toString())) {
                Toast.makeText(getContext(), "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                return check=-1;
            }
            if (!pass.equals(rePass)) {
                Toast.makeText(getContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                return check=-1;
            }
            if (pass.length() < 6 || rePass.length() < 6){
                Toast.makeText(getContext(), "Mật khẩu mới tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
                return check=-1;
            }
        }
        return check;
    }
    public void loadPref(String user,String pass) {
        SharedPreferences pref = getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.getBoolean("REMEMBER", false)) {
            editor.clear();
            editor.putString("USER", user);
            editor.putString("PASS", pass);
            editor.putBoolean("REMEMBER", true);
            editor.commit();
        }
    }
    public void ResetPass(NhanVien nhanVien) {
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
                nhanVien.setMatKhau("123456");
                pass = nhanVien.getMatKhau();
                int kq = daoNhanVien.updateNV(nhanVien,nhanVien.getMaNV());
                if (kq > 0) {
                    loadPref(user,pass);
                    Toast.makeText(getContext(), "Resert Mật Khẩu Mặc Định Thành Công", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("maNV", maNV);
                    navController.navigate(R.id.doiMatKhau_to_chiTietNguoiDung,bundle);
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
        daoNhanVien.closeNV();
    }
}