package com.example.app_phone_store_manager_du_an_1.ui.Hang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.model.Hang;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddHangFragment extends Fragment {
    private Drawable drawable;
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private ImageView imgHang, imgChonAnhHang;
    private EditText edTenHang, edMaHang;
    private DaoHang dao;
    private byte[] hinhAnh;
    private Bitmap bitmapOld, bitmapNew;
    private LinearLayout lnlChupAnh, lnlChonFile;
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
        return inflater.inflate(R.layout.fragment_add_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        imgHang = view.findViewById(R.id.imgHang);
        imgChonAnhHang = view.findViewById(R.id.imgChonAnhHang);
        edMaHang = view.findViewById(R.id.edMaHang);
        edTenHang = view.findViewById(R.id.edTenHang);

        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            imgHang.setImageBitmap(bitmap);
                        }
                    }
                });
        launcherFlie = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            try {
                                InputStream inputStream = appCompatActivity.getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imgHang.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Thêm hãng");
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);

        dao = new DaoHang(appCompatActivity);
        dao.open();
        bitmapOld = ((BitmapDrawable) imgHang.getDrawable()).getBitmap();

        imgChonAnhHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomShet();
            }
        });
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
                navController.navigate(R.id.addHang_to_listHang);
                return true;
            case R.id.menu_reset:
                drawable = appCompatActivity.getDrawable(R.drawable.image_defaut);
                imgHang.setImageDrawable(drawable);
                edTenHang.setText("");
                edMaHang.setText("");
                return true;
            case R.id.menu_save:
                if (valueDate()) {
                    if (dao.checkMaHang(edMaHang.getText().toString()) >0){
                        Toast.makeText(appCompatActivity, "Mã hãng đã tồn tại trong hệ thống!", Toast.LENGTH_SHORT).show();
                    }else {
                        bitmapNew = ((BitmapDrawable) imgHang.getDrawable()).getBitmap();
                        Hang hang = new Hang();
                        hang.setTenHang(edTenHang.getText().toString());
                        hang.setMaHang(edMaHang.getText().toString().replaceAll(" ",""));
                        if (bitmapOld != bitmapNew) {
                            convertImage();
                            hang.setHinhAnh(hinhAnh);
                        }
                        long kq = dao.add(hang);
                        if (kq > 0) {
                            Toast.makeText(appCompatActivity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.addHang_to_listHang);
                        } else {
                            Toast.makeText(appCompatActivity, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void clickOpenBottomShet() {
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
    public boolean valueDate() {
        if (edMaHang.getText().length() == 0 || edTenHang.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn cần nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaHang.getText().length() < 6 || edMaHang.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã hãng có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edTenHang.getText().toString().substring(0, 1).toUpperCase().equals(edTenHang.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên hãng phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHang.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        hinhAnh = byteArray.toByteArray();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}