package com.example.app_phone_store_manager_du_an_1.ui.HoaDonXuat;

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

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoKhachHang;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.dao.DaoSanPham;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentChiTietHoaDonXuatBinding;
import com.example.app_phone_store_manager_du_an_1.model.ChiTietHoaDon;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;


public class ChiTietHoaDonXuatFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private DaoCTHD daoCTHD;
    private DaoHD daoHD;
    private DaoKhachHang daoKH;
    private DaoNhanVien daoNhanVien;
    private DaoSanPham daoSanPham;
    private String maHD;
    FragmentChiTietHoaDonXuatBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChiTietHoaDonXuatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkBundle();
        anhXa(view);
        openData();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        HoaDon hoaDon = daoHD.getMaHD(maHD);
        NhanVien nhanVien = daoNhanVien.getMaNV(hoaDon.getMaNV());
        KhachHang khachHang = daoKH.getMaKH(hoaDon.getMaKH());
        List<ChiTietHoaDon> hoaDonList = daoCTHD.getListMaHD(hoaDon.getMaHD());
        String tenSP = "";
        String soLuong = "";
        String donGia = "";
        double tien = 0;
        int khuyenMai = 0;
        int baohanh = 0;
        for (ChiTietHoaDon x : hoaDonList) {
            SanPham sanPham = daoSanPham.getMaSP(x.getMaSP());
            if (tenSP.equals("")) {
                tenSP = sanPham.getTenSP();
                soLuong = sanPham.getTenSP() + ": " + x.getSoLuong();
                donGia = sanPham.getTenSP() + ": " + formatter.format(x.getDonGia()) + " đ";
                tien = x.getDonGia() * x.getSoLuong();
                khuyenMai = x.getGiamGia();
                baohanh = x.getBaoHanh();

            } else {
                tenSP += " , " + sanPham.getTenSP();
                soLuong += " , " + sanPham.getTenSP() + ": " + x.getSoLuong();
                donGia += " , " + sanPham.getTenSP() + ": " + formatter.format(x.getDonGia()) + " đ";
                tien += (x.getDonGia() * x.getSoLuong());
            }
        }

        binding.tvKHXuat.setText(khachHang.getHoTen());
        binding.tvNgayTaoXuat.setText(hoaDon.getNgay());
        binding.tvNvBanXuat.setText(nhanVien.getHoTen());
        binding.tvNguoiTaoXuat.setText(nhanVien.getHoTen());

        switch (hoaDon.getTrangThai()){
            case 0:
                binding.tvTrangThaiXuat.setText("Đã được xử lý");
                break;
            case 1:
                binding.tvTrangThaiXuat.setText("Vận chuyển");
                break;
            case 2:
                binding.tvTrangThaiXuat.setText("Hoàn thành");
                break;
            default:
                binding.tvTrangThaiXuat.setText("Đang xử lý");
                break;
        }

        binding.tvTenSpXuat.setText(tenSP);
        binding.tvSlXuat.setText(soLuong);
        binding.tvDonGiaXuat.setText(donGia);

        switch (baohanh){
            case 0:
                binding.tvBaoHanhXuat.setText("6 tháng BH");
                break;
            case 1:
                binding.tvBaoHanhXuat.setText("12 tháng BH");
                break;
            default:
                binding.tvBaoHanhXuat.setText("Không bảo hành");
                break;
        }
        switch (khuyenMai) {
            case 0:
                binding.tvGiamGiaXuat.setText("Không khuyến mãi");
                binding.tvThanhTienXuat.setText(formatter.format(tien)+" đ");
                break;
            case 1:
                binding.tvGiamGiaXuat.setText("5%");
                binding.tvThanhTienXuat.setText(formatter.format(tien - tien * 0.05) + " đ");
                break;
            case 2:
                binding.tvGiamGiaXuat.setText("10%");
                binding.tvThanhTienXuat.setText(formatter.format(tien - tien * 0.1) + " đ");
                break;
            case 3:
                binding.tvThanhTienXuat.setText(formatter.format(tien - tien * 0.15) + " đ");
                binding.tvGiamGiaXuat.setText("15%");
                break;
            case 4:
                binding.tvGiamGiaXuat.setText("20%");
                binding.tvThanhTienXuat.setText(formatter.format(tien - tien * 0.2) + " đ");
                break;
            case 5:
                binding.tvGiamGiaXuat.setText("25%");
                binding.tvThanhTienXuat.setText(formatter.format(tien - tien * 0.25) + " đ");
                break;
            case 6:
                binding.tvGiamGiaXuat.setText("30%");
                binding.tvThanhTienXuat.setText(formatter.format(tien - tien * 0.3) + " đ");
                break;
        }

    }
    private void anhXa(View view){
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle(maHD);
    }

    public void checkBundle() {
        if (getArguments() != null) {
            maHD = getArguments().getString("maHD");
        }
    }
    private void openData() {
        daoHD = new DaoHD(appCompatActivity);
        daoHD.open();

        daoCTHD = new DaoCTHD(appCompatActivity);
        daoCTHD.open();

        daoSanPham = new DaoSanPham(appCompatActivity);
        daoSanPham.open();

        daoNhanVien = new DaoNhanVien(appCompatActivity);
        daoNhanVien.openNV();
        daoKH = new DaoKhachHang(appCompatActivity);
        daoKH.open();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigate(R.id.chiTietHDX_to_listHDX);
                return true;
            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putString("maHDEdit", maHD);
                navController.navigate(R.id.chiTietHDX_to_editHDX, bundle);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoHD.close();
        daoCTHD.close();
        daoKH.close();
        daoSanPham.close();
        daoNhanVien.closeNV();
    }
}