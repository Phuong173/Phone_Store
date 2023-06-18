package com.example.app_phone_store_manager_du_an_1.ui.Hang;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.model.Hang;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditHangFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private DaoHang dao;
    private String maHang;
    private ImageView imgHangChange, imgChonAnh;
    private EditText edMaHangChange, edTenHangChange;
    private byte[] hinhAnh;
    private Bitmap bitmapOld, bitmapNew;
    private LinearLayout lnlChupAnh, lnlChonFile;
    private Hang hang;
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
        return inflater.inflate(R.layout.fragment_edit_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        maHang = getArguments().getString("maHang");
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        imgHangChange = view.findViewById(R.id.imgHangChange);
        imgChonAnh = view.findViewById(R.id.imgChonAnhHangChange);
        edMaHangChange = view.findViewById(R.id.edMaHangChange);
        edTenHangChange = view.findViewById(R.id.edTenHangChange);

        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            imgHangChange.setImageBitmap(bitmap);
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
                                imgHangChange.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Cập nhập hãng");

        dao = new DaoHang(appCompatActivity);
        dao.open();

        hang = dao.getMaHang(maHang);

        if (hang.getHinhAnh() == null) {
            drawable = appCompatActivity.getDrawable(R.drawable.image_defaut);
            imgHangChange.setImageDrawable(drawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhAnh(), 0, hang.getHinhAnh().length);
            imgHangChange.setImageBitmap(bitmap);
        }
        edMaHangChange.setText(hang.getMaHang());
        edTenHangChange.setText(hang.getTenHang());
        bitmapOld = ((BitmapDrawable) imgHangChange.getDrawable()).getBitmap();

        imgChonAnh.setOnClickListener(new View.OnClickListener() {
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
                        Bundle bundle = new Bundle();
                        bundle.putString("maHang", maHang);
                        navController.navigate(R.id.editHang_to_chiTietHang, bundle);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            case R.id.menu_reset:
                drawable = appCompatActivity.getDrawable(R.drawable.image_defaut);
                imgHangChange.setImageDrawable(drawable);
                edTenHangChange.setText("");
                edMaHangChange.setText("");
                return true;
            case R.id.menu_save:
                bitmapNew = ((BitmapDrawable) imgHangChange.getDrawable()).getBitmap();
                if (valueDate()) {
                    bitmapNew = ((BitmapDrawable) imgHangChange.getDrawable()).getBitmap();
                    hang.setTenHang(edTenHangChange.getText().toString());
                    hang.setMaHang(edMaHangChange.getText().toString().replaceAll(" ",""));
                    if (bitmapOld != bitmapNew) {
                        convertImage();
                        hang.setHinhAnh(hinhAnh);
                    }
                    int kq = dao.update(hang, maHang);
                    if (kq > 0) {
                        Toast.makeText(appCompatActivity, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.editHang_to_listHang);
                    } else {
                        Toast.makeText(appCompatActivity, "Cập nhập thất bại", Toast.LENGTH_SHORT).show();
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
        if (edMaHangChange.getText().length() == 0 || edTenHangChange.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn cần nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaHangChange.getText().length() < 6 || edMaHangChange.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã hãng có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edTenHangChange.getText().toString().substring(0, 1).toUpperCase().equals(edTenHangChange.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên hãng phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgHangChange.getDrawable();
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