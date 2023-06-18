package com.example.app_phone_store_manager_du_an_1.ui.SanPham;

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
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.adapter.ChonHangAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.dao.DaoSanPham;
import com.example.app_phone_store_manager_du_an_1.dao.DaoThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentAddSanPhamBinding;
import com.example.app_phone_store_manager_du_an_1.model.Hang;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;
import com.example.app_phone_store_manager_du_an_1.model.ThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemHangClick;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class AddSanPhamFragment extends Fragment {
    private Drawable drawable;
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private FragmentAddSanPhamBinding binding;
    private DaoSanPham daoSanPham;
    private RecyclerView rvHang;
    private int position;
    private Button btnCancel, btnSave;
    private ChonHangAdapter adapter;
    private List<Hang> listHang;
    private DaoHang daoHang;
    private DaoThuocTinhSanPham daoTTSP;
    private Bitmap bitmapOld, bitmapNew;
    private LinearLayout lnlChupAnh, lnlChonFile;
    private ActivityResultLauncher<Intent> launcherCamera;
    private ActivityResultLauncher<Intent> launcherFlie;
    private String maSP;
    private String maHang;
    private String tenHang;
    private String tenSP;
    private byte[] hinhAnh;
    private int phanLoai;
    private int tinhTrang;
    private double giaTien;
    private int trangThai;
    private String moTa;
    private String boNho;
    private String RAM;
    private String chipSet;
    private String heDieuHanh;
    private String manHinh;
    private String dungLuongPin;
    private String congSac;
    private String loaiPhuKien;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        anhXa(view);

        laucherSelected();

        phanLoaiSP();

        bitmapOld = ((BitmapDrawable) binding.imgSP.getDrawable()).getBitmap();
        binding.imgSelectPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomShet();
            }
        });

        openData();

        binding.edHangSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogChonHang();

            }
        });
    }

    private void dialogChonHang() {
        LayoutInflater inflater = (LayoutInflater) appCompatActivity
                .getSystemService(appCompatActivity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dialog_chon_hang, null);

        AlertDialog dialog = new AlertDialog.Builder(appCompatActivity).create();
        dialog.setView(view);
        dialog.setCancelable(false);

        rvHang = (RecyclerView) view.findViewById(R.id.rvChonHang);
        btnCancel = (Button) view.findViewById(R.id.btnCancelChonHang);
        btnSave = (Button) view.findViewById(R.id.btnSaveChonHang);

        listHang = new ArrayList<>();
        listHang = daoHang.getAll();


        adapter = new ChonHangAdapter(listHang);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(appCompatActivity);
        manager.setJustifyContent(JustifyContent.CENTER);
        manager.setAlignItems(AlignItems.CENTER);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setFlexWrap(FlexWrap.WRAP);
        rvHang.setLayoutManager(manager);
        rvHang.setAdapter(adapter);

        if (binding.edHangSP.length() > 0) {
            checkSelected();
            adapter.setCheckedPositon(position);
        }

        adapter.setItemHangClick(new ItemHangClick() {
            @Override
            public void ItemClick(Hang hang) {
                if (binding.edHangSP.length() == 0) {
                    maHang = hang.getMaHang();
                    tenHang = hang.getTenHang();
                    binding.edHangSP.setText(tenHang);
                    Toast.makeText(appCompatActivity, "Chọn hãng thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    maHang = hang.getMaHang();
                    tenHang = hang.getTenHang();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                binding.edHangSP.setText(tenHang);
                Toast.makeText(appCompatActivity, "Chọn hãng thành công", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void laucherSelected() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            binding.imgSP.setImageBitmap(bitmap);
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
                                binding.imgSP.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void checkSelected() {
        for (Hang x : listHang) {
            if (x.getTenHang().equals(tenHang)) {
                position = listHang.indexOf(x);
            }
        }
        adapter.setCheckedPositon(position);
    }

    private void anhXa(@NotNull View view) {
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Thêm sản phẩm");
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    private void openData() {
        daoSanPham = new DaoSanPham(appCompatActivity);
        daoTTSP = new DaoThuocTinhSanPham(appCompatActivity);
        daoHang = new DaoHang(appCompatActivity);

        daoSanPham.open();
        daoTTSP.open();
        daoHang.open();
    }

    public void phanLoaiSP() {
        binding.cbSPDienThoai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.tilLoaiPKSP.setVisibility(View.GONE);
                } else {
                    binding.tilLoaiPKSP.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cbSPPhuKien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.tilBoNhoSP.setVisibility(View.GONE);
                    binding.tilRAMSP.setVisibility(View.GONE);
                    binding.tilChipSetSP.setVisibility(View.GONE);
                    binding.tilOSSP.setVisibility(View.GONE);
                    binding.tilMaHinhSP.setVisibility(View.GONE);
                    binding.tilPinSP.setVisibility(View.GONE);
                    binding.tilTypeSP.setVisibility(View.GONE);
                } else {
                    binding.tilBoNhoSP.setVisibility(View.VISIBLE);
                    binding.tilRAMSP.setVisibility(View.VISIBLE);
                    binding.tilChipSetSP.setVisibility(View.VISIBLE);
                    binding.tilOSSP.setVisibility(View.VISIBLE);
                    binding.tilMaHinhSP.setVisibility(View.VISIBLE);
                    binding.tilPinSP.setVisibility(View.VISIBLE);
                    binding.tilTypeSP.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cbSPDienThoai.setChecked(true);
        binding.cbSPNew.setChecked(true);
        binding.cbChuaLKSP.setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddSanPhamBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
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
                navController.navigate(R.id.addSP_to_listSP);
                return true;
            case R.id.menu_reset:
                resetFrom();
                return true;
            case R.id.menu_save:
                if (checkSP()) {
                    bitmapNew = ((BitmapDrawable) binding.imgSP.getDrawable()).getBitmap();
                    if (bitmapNew != bitmapOld) {
                        convertImage();
                    }
                    formatPhanLoai();
                    formatTinhTrang();
                    formatTrangThai();

                    maSP = binding.edMaSP.getText().toString();
                    tenSP = binding.edTenSP.getText().toString();
                    giaTien = Double.parseDouble(binding.edGiaTienSP.getText().toString());
                    moTa = binding.edMoTaSP.getText().toString();
                    boNho = binding.edBoNhoSP.getText().toString();
                    RAM = binding.edRAMSP.getText().toString();
                    chipSet = binding.edChipSetSP.getText().toString();
                    heDieuHanh = binding.edOSSP.getText().toString();
                    manHinh = binding.edManHinhSP.getText().toString();
                    dungLuongPin = binding.edPinSP.getText().toString();
                    congSac = binding.edTypeSP.getText().toString();
                    loaiPhuKien = binding.edLoaiPKSP.getText().toString();
                    if (daoSanPham.checkMaSP(maSP) > 0) {
                        Toast.makeText(appCompatActivity, "Đã có mã sản phẩm trong hệ thống", Toast.LENGTH_SHORT).show();
                    } else {
                        SanPham sanPham = new SanPham();
                        sanPham.setMaSP(maSP);
                        sanPham.setMaHang(maHang);
                        sanPham.setTenSP(tenSP);
                        sanPham.setHinhAnh(hinhAnh);
                        sanPham.setPhanLoai(phanLoai);
                        sanPham.setTinhTrang(tinhTrang);
                        sanPham.setGiaTien(giaTien);
                        sanPham.setTrangThai(trangThai);
                        sanPham.setMoTa(moTa);

                        long kq = daoSanPham.add(sanPham);
                        if (kq > 0) {
                            ThuocTinhSanPham thuocTinhSanPham = new ThuocTinhSanPham();
                            thuocTinhSanPham.setMaSP(maSP);

                            if (phanLoai == 0) {
                                thuocTinhSanPham.setBoNho(boNho);
                                thuocTinhSanPham.setRAM(RAM);
                                thuocTinhSanPham.setChipSet(chipSet);
                                thuocTinhSanPham.setHeDieuHanh(heDieuHanh);
                                thuocTinhSanPham.setManHinh(manHinh);
                                thuocTinhSanPham.setDungLuongPin(dungLuongPin);
                                thuocTinhSanPham.setCongSac(congSac);
                            } else {
                                thuocTinhSanPham.setLoaiPhuKien(loaiPhuKien);
                            }
                            long kqTTSP = daoTTSP.add(thuocTinhSanPham);
                            if (kqTTSP > 0) {
                                Toast.makeText(appCompatActivity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.addSP_to_listSP);
                            } else {
                                Toast.makeText(appCompatActivity, "Thêm thuộc tính sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            }

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

    private void resetFrom() {
        drawable = appCompatActivity.getDrawable(R.drawable.image_defaut);
        binding.imgSP.setImageDrawable(drawable);
        binding.edMaSP.setText("");
        binding.edTenSP.setText("");
        binding.edHangSP.setText("");
        binding.cbSPDienThoai.setChecked(true);
        binding.cbSPNew.setChecked(true);
        binding.edGiaTienSP.setText("");
        binding.cbChuaLKSP.setChecked(true);
        binding.edBoNhoSP.setText("");
        binding.edRAMSP.setText("");
        binding.edChipSetSP.setText("");
        binding.edOSSP.setText("");
        binding.edManHinhSP.setText("");
        binding.edPinSP.setText("");
        binding.edLoaiPKSP.setText("");
        binding.edMoTaSP.setText("");
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


    public boolean checkSP() {

        if (binding.cbSPDienThoai.isChecked()) {
            if (binding.edMaSP.getText().length() == 0 ||
                    binding.edTenSP.getText().length() == 0 ||
                    binding.edHangSP.getText().length() == 0 ||
                    binding.edGiaTienSP.getText().length() == 0 ||
                    binding.edMoTaSP.getText().length() == 0 ||
                    binding.edBoNhoSP.getText().length() == 0 ||
                    binding.edRAMSP.getText().length() == 0 ||
                    binding.edChipSetSP.getText().length() == 0 ||
                    binding.edOSSP.getText().length() == 0 ||
                    binding.edManHinhSP.getText().length() == 0 ||
                    binding.edPinSP.getText().length() == 0 ||
                    binding.edTypeSP.getText().length() == 0) {
                Toast.makeText(appCompatActivity, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (binding.edMaSP.getText().length() == 0 ||
                    binding.edTenSP.getText().length() == 0 ||
                    binding.edHangSP.getText().length() == 0 ||
                    binding.edGiaTienSP.getText().length() == 0 ||
                    binding.edMoTaSP.getText().length() == 0 ||
                    binding.edLoaiPKSP.getText().length() == 0) {
                Toast.makeText(appCompatActivity, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (binding.edMaSP.getText().length() < 6 || binding.edMaSP.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã sản phẩm có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.edTenSP.getText().toString().substring(0, 1).toUpperCase().equals(binding.edTenSP.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên sản phảm phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.edGiaTienSP.getText().length() > 0) {
            try {
                giaTien = Double.parseDouble(binding.edGiaTienSP.getText().toString());
                if (giaTien <= 0) {
                    Toast.makeText(appCompatActivity, "Giá tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                Toast.makeText(appCompatActivity, "Giá tiền phải là số", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;

    }

    public void formatPhanLoai() {
        if (binding.cbSPDienThoai.isChecked()) {
            phanLoai = 0;
        }
        if (binding.cbSPPhuKien.isChecked()) {
            phanLoai = 1;
        }
    }

    public void formatTrangThai() {
        if (binding.cbChuaLKSP.isChecked()) {
            trangThai = 0;
        }
        if (binding.cbDaLKSP.isChecked()) {
            trangThai = 1;
        }
    }

    public void formatTinhTrang() {
        if (binding.cbSPOld.isChecked()) {
            tinhTrang = -1;
        }
        if (binding.cbSPLikeNew.isChecked()) {
            tinhTrang = 0;
        }
        if (binding.cbSPNew.isChecked()) {
            tinhTrang = 1;
        }
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgSP.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        hinhAnh = byteArray.toByteArray();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoSanPham.close();
        daoTTSP.close();
        daoHang.close();
    }
}