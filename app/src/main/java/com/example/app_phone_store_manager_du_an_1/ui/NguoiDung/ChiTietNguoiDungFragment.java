package com.example.app_phone_store_manager_du_an_1.ui.NguoiDung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.app_phone_store_manager_du_an_1.MainActivity;
import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.adapter.NhanVienAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChiTietNguoiDungFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private Button btnChangePass;
    private Drawable drawable;
    private String tenTK;
    private DaoNhanVien dao;
    private NhanVien nhanVien;
    private NhanVienAdapter adapter;
    private TextDrawable textDrawable;
    private ImageView img_nv, img_chonAnh, img_pencle;
    private TextView tvHoTen, tvDienThoai, tvDiaChi, tvNamSinh;
    private LinearLayout lnlChupAnh, lnlChonFile;
    private byte[] hinhAnh;
    private List<NhanVien> list;
    private ActivityResultLauncher<Intent> launcherCamera;
    private ActivityResultLauncher<Intent> launcherFlie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chi_tiet_nguoi_dung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appCompatActivity = (AppCompatActivity) getActivity();
        navController = Navigation.findNavController(view);
        drawable = appCompatActivity.getDrawable(R.drawable.ic_menu);
        img_nv = view.findViewById(R.id.imgNguoiDung);
        img_chonAnh = view.findViewById(R.id.imgChonAnh);
        img_pencle = view.findViewById(R.id.imgUpdateperson);
        tvHoTen = view.findViewById(R.id.tvHoTenNguoiDung);
        tvDienThoai = view.findViewById(R.id.tvPhoneNguoiDung);
        tvDiaChi = view.findViewById(R.id.tvDiacChiNguoiDung);
        tvNamSinh = view.findViewById(R.id.tvNamSinhNguoiDung);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        SharedPreferences preferences = appCompatActivity.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        tenTK = preferences.getString("USER", "");

        launcherFlie = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            try {
                                InputStream inputStream = appCompatActivity.getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                img_nv.setImageBitmap(bitmap);
                                UpdateAnh(nhanVien);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            img_nv.setImageBitmap(bitmap);
                            UpdateAnh(nhanVien);
                        }
                    }
                });
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Thông tin tài khoản");

        dao = new DaoNhanVien(getActivity());
        dao.openNV();

        nhanVien = dao.gettaiKhoan(tenTK);

        if (nhanVien.getHinhAnh() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            img_nv.setImageBitmap(bitmap);
        }
        img_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenfile();
            }
        });
        img_chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenfile();
            }
        });
        setData(nhanVien);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.chiTietNguoiDung_to_doiMatKhau);
            }
        });
        img_pencle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editperson();
            }
        });

    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void clickOpenfile() {
        View viewDialog = appCompatActivity.getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(appCompatActivity);
        bottomSheetDialog.setContentView(viewDialog);

        lnlChupAnh = viewDialog.findViewById(R.id.lnlChupAnh);
        lnlChonFile = viewDialog.findViewById(R.id.lnlChonAnh);

        lnlChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcherCamera.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        lnlChonFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherFlie.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    public void UpdateAnh(NhanVien nhanVien) {

        convertImage();
        nhanVien.setHinhAnh(hinhAnh);
        int kq = dao.updateNV(nhanVien, nhanVien.getMaNV());
        if (kq > 0) {
            Toast.makeText(appCompatActivity, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).setAvatar(nhanVien);
        } else {
            Toast.makeText(appCompatActivity, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public void editperson() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.custom_item_nguoi_dung_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Sửa Thông Tin Cá Nhân");
        EditText edhoten = view.findViewById(R.id.ed_HoTen_Tkedit);
        EditText eddienthoai = view.findViewById(R.id.ed_Sdt_Tkedit);
        EditText eddiachi = view.findViewById(R.id.ed_DiaChi_Tkedit);
        EditText ednamsinh = view.findViewById(R.id.ed_NamSinh_Tkedit);
        edhoten.setText(nhanVien.getHoTen());
        eddienthoai.setText(nhanVien.getDienThoai());
        eddiachi.setText(nhanVien.getDiaChi());
        ednamsinh.setText(nhanVien.getNamSinh());
        list = new ArrayList<>();
        adapter = new NhanVienAdapter(list);
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nhanVien.getHoTen().equals(edhoten.getText().toString()) &&
                        nhanVien.getDienThoai().equals(eddienthoai.getText().toString()) &&
                        nhanVien.getDiaChi().equals(eddiachi.getText().toString()) &&
                        nhanVien.getNamSinh().equals(ednamsinh.getText().toString())) {
                    Toast.makeText(getContext().getApplicationContext(), "Không có gì thay đổi để cập nhập", Toast.LENGTH_SHORT).show();
                } else {
                    nhanVien.setHoTen(edhoten.getText().toString());
                    nhanVien.setDienThoai(eddienthoai.getText().toString());
                    nhanVien.setDiaChi(eddiachi.getText().toString());
                    nhanVien.setNamSinh(ednamsinh.getText().toString());
                    int kq = dao.updateNV(nhanVien, nhanVien.getMaNV());
                    if (kq > 0) {
                        setData(nhanVien);
                        Toast.makeText(getContext(), "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Cập nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_nv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        hinhAnh = byteArray.toByteArray();
    }

    public void setData(NhanVien nhanVien) {
        tvHoTen.setText(nhanVien.getHoTen());
        tvDienThoai.setText(nhanVien.getDienThoai());
        tvDiaChi.setText(nhanVien.getDiaChi());
        tvNamSinh.setText(nhanVien.getNamSinh());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.closeNV();
    }
}