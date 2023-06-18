package com.example.app_phone_store_manager_du_an_1.ui.HoaDonXuat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app_phone_store_manager_du_an_1.R;

import com.example.app_phone_store_manager_du_an_1.adapter.HoaDonXuatAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoKhachHang;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentListHoaDonXuatBinding;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemHoaDonClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ListHoaDonXuatFragment extends Fragment  {
    private NavController navController;
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private SearchView searchView;
    private FragmentListHoaDonXuatBinding binding;
    private DaoHD daoHD;
    private List<HoaDon> list;
    private DaoKhachHang daoKH;
    private HoaDonXuatAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListHoaDonXuatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();

        binding.tblHDXuat.inflateMenu(R.menu.menu_seach);
        drawable = getActivity().getDrawable(R.drawable.ic_menu);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Hoá Đơn Xuất");
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        MenuItem menu = binding.tblHDXuat.getMenu().findItem(R.id.menu_extra_seach);
        searchView = (SearchView) menu.getActionView();
        searchView.setQueryHint("Mã Hoá Đơn, Ngày, Số Lượng,Sản Phẩm,Giá Tiền , Trạng Thái");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText edSeach = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        edSeach.setTextColor(Color.BLACK);
        edSeach.setHintTextColor(Color.LTGRAY);

        ImageView iconSeach = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_button);
        ImageView iconClose = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconSeach.setColorFilter(Color.BLACK);
        iconClose.setColorFilter(Color.BLACK);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        daoHD = new DaoHD(appCompatActivity);
        daoHD.open();
        daoKH = new DaoKhachHang(appCompatActivity);
        daoKH.open();

        list = new ArrayList<>();
        list = daoHD.getAllXuat();

        adapter = new HoaDonXuatAdapter(list,appCompatActivity);
        binding.rvHDXuat.setAdapter(adapter);
        binding.rvHDXuat.setLayoutManager(new LinearLayoutManager(appCompatActivity));

        adapter.setImgDelete(new ItemHoaDonClick() {
            @Override
            public void ItemClick(HoaDon hoaDon) {
                dialogDelete(hoaDon);
            }
        });
        adapter.setItemHoaDonClick(new ItemHoaDonClick() {
            @Override
            public void ItemClick(HoaDon hoaDon) {
                Bundle bundle = new Bundle();
                bundle.putString("maHD", hoaDon.getMaHD());
                navController.navigate(R.id.listHDX_to_chiTietHDX, bundle);
            }
        });
        spinnerFilter();
    }
    private void filter(String newText) {
        List<HoaDon> filterList = new ArrayList<>();
        for (HoaDon item : list) {
            if (item.getMaHD().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapter.filter(filterList);
    }
    public void dialogDelete(HoaDon hoaDon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa không!");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int kq = daoHD.delete(hoaDon.getMaHD());
                if (kq > 0) {
                    Toast.makeText(appCompatActivity, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list.addAll(daoHD.getAllXuat());
                    adapter.filter(list);
                } else {
                    Toast.makeText(appCompatActivity, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void spinnerFilter() {
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(appCompatActivity, R.array.date, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);
        binding.spListHDXFilter.setAdapter(spAdapter);

        binding.spListHDXFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        list.clear();
                        list.addAll(daoHD.getDaysXuat());
                        adapter.filter(list);
                        break;
                    case 2:
                        list.clear();
                        list.addAll(daoHD.getWeekXuat());
                        adapter.filter(list);
                        break;
                    case 3:
                        list.clear();
                        list.addAll(daoHD.getMonthXuat());
                        adapter.filter(list);
                        break;
                    case 0:
                        list.clear();
                        list.addAll(daoHD.getAllXuat());
                        adapter.filter(list);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                navController.navigate(R.id.listHDX_to_addHDX);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoHD.close();
    }
}