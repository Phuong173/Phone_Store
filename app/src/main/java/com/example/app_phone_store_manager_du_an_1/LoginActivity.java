package com.example.app_phone_store_manager_du_an_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;


public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnHuy;
    private EditText edUser, edPass;
    private DaoNhanVien dao;
    private Intent intent;
    private CheckBox ckbLuuTK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnHuy = findViewById(R.id.btnReset);
        btnLogin = findViewById(R.id.btnLogin);
        edUser = findViewById(R.id.edUserName);
        edPass = findViewById(R.id.edPassWorld);
        ckbLuuTK = findViewById(R.id.cbRemember);

        dao = new DaoNhanVien(this);
        dao.openNV();
        if (dao.getUserName("admin") < 0) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setMaNV("Admin");
            nhanVien.setHoTen("Admin");
            nhanVien.setDienThoai("0123456789");
            nhanVien.setTaiKhoan("admin");
            nhanVien.setMatKhau("123456");
            nhanVien.setDiaChi("Viet Nam");
            nhanVien.setNamSinh("1234");
            dao.addNV(nhanVien);
        }
        SharedPreferences preferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        edUser.setText(preferences.getString("USER", ""));
        edPass.setText(preferences.getString("PASS", ""));
        ckbLuuTK.setChecked(preferences.getBoolean("REMEMBER", false));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUser.setText("");
                edPass.setText("");
            }
        });

    }

    public void logIn() {
        String user = edUser.getText().toString();
        String pass = edPass.getText().toString();
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getBaseContext(), "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            if (dao.getlogin(user, pass) > 0) {
                rememberUser(user, pass, ckbLuuTK.isChecked());
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user);
                saveInfo(user);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Đăng Nhập Thành Công! ", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(getBaseContext(), "Đăng Nhập Thất Bại!" +
                        "\nSai thông tin tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void rememberUser(String user, String pass, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status) {
            editor.clear();
        } else {
            // Lưu dữ liệu
            editor.putString("USER", user);
            editor.putString("PASS", pass);
            editor.putBoolean("REMEMBER", status);
        }
        // LƯu lại toàn bộ dữ liệu
        editor.commit();
    }

    public void saveInfo(String user) {
        SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        // Lưu dữ liệu
        editor.putString("USER", user);
        // LƯu lại toàn bộ dữ liệu
        editor.commit();
    }


}