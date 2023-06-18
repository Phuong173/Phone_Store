package com.example.app_phone_store_manager_du_an_1.ui.SanPham;

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
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentEditSanPhamBinding;
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

public class EditSanPhamFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private FragmentEditSanPhamBinding binding;
    private DaoSanPham daoSP;
    private DaoThuocTinhSanPham daoTT;
    private DaoHang daoHang;
    private RecyclerView rvHang;
    private int position;
    private Button btnCancel, btnSave;
    private ChonHangAdapter adapter;
    private List<Hang> listHang;
    private Bitmap bitmapOld, bitmapNew;
    private LinearLayout lnlChupAnh, lnlChonFile;
    private ActivityResultLauncher<Intent> launcherCamera;
    private ActivityResultLauncher<Intent> launcherFlie;
    private SanPham sanPham;
    private String maSP;
    private String maSPOld;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditSanPhamBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkBunldle();

        tbCustom(view);

        laucherSelected();

        openData();

        sanPham = daoSP.getMaSP(maSP);
        maSPOld = sanPham.getMaSP();
        phanLoaiSP(sanPham);
        maHang = sanPham.getMaHang();

        bitmapOld = ((BitmapDrawable) binding.imgSPChange.getDrawable()).getBitmap();
        binding.imgSelectPickChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomShet();
            }
        });

        binding.edHangSPChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChonHang();
            }
        });
    }

    private void tbCustom(@NotNull View view) {
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Cập nhập Sản phẩm");
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

        if (binding.edHangSPChange.length() > 0) {
            checkSelected();
            adapter.setCheckedPositon(position);
        }

        adapter.setItemHangClick(new ItemHangClick() {
            @Override
            public void ItemClick(Hang hang) {
                if (binding.edHangSPChange.length() == 0) {
                    maHang = hang.getMaHang();
                    tenHang = hang.getTenHang();
                    binding.edHangSPChange.setText(tenHang);
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
                binding.edHangSPChange.setText(tenHang);
                Toast.makeText(appCompatActivity, "Chọn hãng thành công", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void checkBunldle() {
        if (getArguments() != null) {
            maSP = getArguments().getString("maSPChange");
        }
    }

    private void checkSelected() {
        for (Hang x : listHang) {
            if (x.getTenHang().equals(tenHang)) {
                position = listHang.indexOf(x);
            }
        }
        adapter.setCheckedPositon(position);
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

    private void openData() {
        daoSP = new DaoSanPham(appCompatActivity);
        daoSP.open();

        daoHang = new DaoHang(appCompatActivity);
        daoHang.open();

        daoTT = new DaoThuocTinhSanPham(appCompatActivity);
        daoTT.open();
    }

    public void phanLoaiSP(SanPham sanPham) {


        binding.cbSPDienThoaiChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.tilLoaiPKSPChange.setVisibility(View.GONE);
                } else {
                    binding.tilLoaiPKSPChange.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cbSPPhuKienChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.tilBoNhoSPChange.setVisibility(View.GONE);
                    binding.tilRAMSPChange.setVisibility(View.GONE);
                    binding.tilChipSetSPChange.setVisibility(View.GONE);
                    binding.tilOSSPChange.setVisibility(View.GONE);
                    binding.tilMaHinhSPChange.setVisibility(View.GONE);
                    binding.tilPinSPChange.setVisibility(View.GONE);
                    binding.tilTypeSPChange.setVisibility(View.GONE);
                } else {
                    binding.tilBoNhoSPChange.setVisibility(View.VISIBLE);
                    binding.tilRAMSPChange.setVisibility(View.VISIBLE);
                    binding.tilChipSetSPChange.setVisibility(View.VISIBLE);
                    binding.tilOSSPChange.setVisibility(View.VISIBLE);
                    binding.tilMaHinhSPChange.setVisibility(View.VISIBLE);
                    binding.tilPinSPChange.setVisibility(View.VISIBLE);
                    binding.tilTypeSPChange.setVisibility(View.VISIBLE);
                }
            }
        });
        if (sanPham.getPhanLoai() > 0) {
            binding.cbSPPhuKienChange.setChecked(true);
            binding.cbSPDienThoaiChange.setChecked(false);
        } else {
            binding.cbSPDienThoaiChange.setChecked(true);
            binding.cbSPPhuKienChange.setChecked(false);
        }

        switch (sanPham.getTinhTrang()) {
            case 0:
                binding.cbSPLikeNewChange.setChecked(true);
                break;
            case 1:
                binding.cbSPNewChange.setChecked(true);
                break;
            default:
                binding.cbSPOldChange.setChecked(true);
                break;
        }
        if (sanPham.getTrangThai() > 0) {
            binding.cbDaLKSPChange.setChecked(true);
        } else {
            binding.cbChuaLKSPChange.setChecked(true);
        }

        binding.edMaSPChange.setText(sanPham.getMaSP());
        binding.edTenSPChange.setText(sanPham.getTenSP());
        binding.edHangSPChange.setText(daoHang.getMaHang(sanPham.getMaHang()).getTenHang());
        binding.edGiaTienSPChange.setText(sanPham.getGiaTien() + "");
        binding.edMoTaSPChange.setText(sanPham.getMoTa());

        if (sanPham.getHinhAnh() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sanPham.getHinhAnh(), 0, sanPham.getHinhAnh().length);
            binding.imgSPChange.setImageBitmap(bitmap);
        }

        ThuocTinhSanPham ttSP = daoTT.getMaSP(sanPham.getMaSP());

        if (sanPham.getPhanLoai() > 0) {
            binding.edLoaiPKSPChange.setText(ttSP.getLoaiPhuKien());
        } else {
            binding.edBoNhoSPChange.setText(ttSP.getBoNho());
            binding.edRAMSPChange.setText(ttSP.getRAM());
            binding.edChipSetSPChange.setText(ttSP.getChipSet());
            binding.edOSSPChange.setText(ttSP.getHeDieuHanh());
            binding.edManHinhSPChange.setText(ttSP.getManHinh());
            binding.edPinSPChange.setText(ttSP.getDungLuongPin());
            binding.edTypeSPChange.setText(ttSP.getCongSac());
        }
    }

    private void laucherSelected() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            binding.imgSPChange.setImageBitmap(bitmap);
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
                                binding.imgSPChange.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


    public boolean checkSP() {

        if (binding.cbSPDienThoaiChange.isChecked()) {
            if (binding.edMaSPChange.getText().length() == 0 ||
                    binding.edTenSPChange.getText().length() == 0 ||
                    binding.edHangSPChange.getText().length() == 0 ||
                    binding.edGiaTienSPChange.getText().length() == 0 ||
                    binding.edMoTaSPChange.getText().length() == 0 ||
                    binding.edBoNhoSPChange.getText().length() == 0 ||
                    binding.edRAMSPChange.getText().length() == 0 ||
                    binding.edChipSetSPChange.getText().length() == 0 ||
                    binding.edOSSPChange.getText().length() == 0 ||
                    binding.edManHinhSPChange.getText().length() == 0 ||
                    binding.edPinSPChange.getText().length() == 0 ||
                    binding.edTypeSPChange.getText().length() == 0) {
                Toast.makeText(appCompatActivity, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (binding.edMaSPChange.getText().length() == 0 ||
                    binding.edTenSPChange.getText().length() == 0 ||
                    binding.edHangSPChange.getText().length() == 0 ||
                    binding.edGiaTienSPChange.getText().length() == 0 ||
                    binding.edMoTaSPChange.getText().length() == 0 ||
                    binding.edLoaiPKSPChange.getText().length() == 0) {
                Toast.makeText(appCompatActivity, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (binding.edMaSPChange.getText().length() < 6 || binding.edMaSPChange.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã sản phẩm có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.edTenSPChange.getText().toString().substring(0, 1).toUpperCase().equals(binding.edTenSPChange.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên sản phảm phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        giaTien = Double.parseDouble(binding.edGiaTienSPChange.getText().toString());
        if (giaTien < 0) {
            Toast.makeText(appCompatActivity, "Giá tiền không phải là số âm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.edGiaTienSPChange.getText().length() > 0) {
            try {
                giaTien = Double.parseDouble(binding.edGiaTienSPChange.getText().toString());
                if (giaTien < 0) {
                    Toast.makeText(appCompatActivity, "Giá tiền không phải là số âm", Toast.LENGTH_SHORT).show();
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
        if (binding.cbSPDienThoaiChange.isChecked()) {
            phanLoai = 0;
        }
        if (binding.cbSPPhuKienChange.isChecked()) {
            phanLoai = 1;
        }
    }

    public void formatTrangThai() {
        if (binding.cbChuaLKSPChange.isChecked()) {
            trangThai = 0;
        }
        if (binding.cbDaLKSPChange.isChecked()) {
            trangThai = 1;
        }
    }

    public void formatTinhTrang() {
        if (binding.cbSPOldChange.isChecked()) {
            tinhTrang = -1;
        }
        if (binding.cbSPLikeNewChange.isChecked()) {
            tinhTrang = 0;
        }
        if (binding.cbSPNewChange.isChecked()) {
            tinhTrang = 1;
        }
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgSPChange.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        hinhAnh = byteArray.toByteArray();
    }

    private void resetFrom() {
        drawable = appCompatActivity.getDrawable(R.drawable.image_defaut);
        binding.imgSPChange.setImageDrawable(drawable);
        binding.edMaSPChange.setText("");
        binding.edTenSPChange.setText("");
        binding.edHangSPChange.setText("");
        binding.cbSPDienThoaiChange.setChecked(true);
        binding.cbSPNewChange.setChecked(true);
        binding.edGiaTienSPChange.setText("");
        binding.cbChuaLKSPChange.setChecked(true);
        binding.edBoNhoSPChange.setText("");
        binding.edRAMSPChange.setText("");
        binding.edChipSetSPChange.setText("");
        binding.edOSSPChange.setText("");
        binding.edManHinhSPChange.setText("");
        binding.edPinSPChange.setText("");
        binding.edLoaiPKSPChange.setText("");
        binding.edMoTaSPChange.setText("");
    }
    private void dialogBack(){
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
                if (maSPOld != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("maSP", maSPOld);
                    navController.navigate(R.id.editSP_to_chitetSP, bundle);
                } else {
                    navController.navigate(R.id.editSP_to_listSP);
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
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
                dialogBack();
                return true;
            case R.id.menu_reset:
                resetFrom();
                return true;
            case R.id.menu_save:
                if (checkSP()) {
                    bitmapNew = ((BitmapDrawable) binding.imgSPChange.getDrawable()).getBitmap();
                    if (bitmapNew != bitmapOld) {
                        convertImage();
                    }
                    formatPhanLoai();
                    formatTinhTrang();
                    formatTrangThai();

                    maSP = binding.edMaSPChange.getText().toString();
                    tenSP = binding.edTenSPChange.getText().toString();
                    giaTien = Double.parseDouble(binding.edGiaTienSPChange.getText().toString());
                    moTa = binding.edMoTaSPChange.getText().toString();
                    boNho = binding.edBoNhoSPChange.getText().toString();
                    RAM = binding.edRAMSPChange.getText().toString();
                    chipSet = binding.edChipSetSPChange.getText().toString();
                    heDieuHanh = binding.edOSSPChange.getText().toString();
                    manHinh = binding.edManHinhSPChange.getText().toString();
                    dungLuongPin = binding.edPinSPChange.getText().toString();
                    congSac = binding.edTypeSPChange.getText().toString();
                    loaiPhuKien = binding.edLoaiPKSPChange.getText().toString();
                    ThuocTinhSanPham thuocTinhSanPham = daoTT.getMaSP(sanPham.getMaSP());

                    sanPham.setMaSP(maSP);
                    sanPham.setMaHang(maHang);
                    sanPham.setTenSP(tenSP);
                    sanPham.setHinhAnh(hinhAnh);
                    sanPham.setPhanLoai(phanLoai);
                    sanPham.setTinhTrang(tinhTrang);
                    sanPham.setGiaTien(giaTien);
                    sanPham.setTrangThai(trangThai);
                    sanPham.setMoTa(moTa);

                    int kq = daoSP.update(sanPham, maSPOld);

                    if (kq > 0) {
                        thuocTinhSanPham.setMaSP(maSP);
                        if (phanLoai == 0) {
                            thuocTinhSanPham.setBoNho(boNho);
                            thuocTinhSanPham.setRAM(RAM);
                            thuocTinhSanPham.setChipSet(chipSet);
                            thuocTinhSanPham.setHeDieuHanh(heDieuHanh);
                            thuocTinhSanPham.setManHinh(manHinh);
                            thuocTinhSanPham.setDungLuongPin(dungLuongPin);
                            thuocTinhSanPham.setCongSac(congSac);
                            thuocTinhSanPham.setLoaiPhuKien("");
                        } else {
                            thuocTinhSanPham.setLoaiPhuKien(loaiPhuKien);
                            thuocTinhSanPham.setBoNho("");
                            thuocTinhSanPham.setRAM("");
                            thuocTinhSanPham.setChipSet("");
                            thuocTinhSanPham.setHeDieuHanh("");
                            thuocTinhSanPham.setManHinh("");
                            thuocTinhSanPham.setDungLuongPin("");
                            thuocTinhSanPham.setCongSac("");
                        }
                        int kqTTSP = daoTT.update(thuocTinhSanPham);
                        if (kqTTSP > 0) {
                            Toast.makeText(appCompatActivity, "Cập nhập sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.editSP_to_listSP);
                        } else {
                            Toast.makeText(appCompatActivity, "Cập nhập thuộc tính sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(appCompatActivity, "Cập nhập thất bại", Toast.LENGTH_SHORT).show();
                    }


                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoSP.close();
        daoTT.close();
        daoHang.close();
    }
}