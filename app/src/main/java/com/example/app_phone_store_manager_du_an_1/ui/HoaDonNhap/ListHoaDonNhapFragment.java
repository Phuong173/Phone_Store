package com.example.app_phone_store_manager_du_an_1.ui.HoaDonNhap;

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
import com.example.app_phone_store_manager_du_an_1.adapter.HoaDonNhapAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentListHoaDonNhapBinding;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemHoaDonClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ListHoaDonNhapFragment extends Fragment {
    private NavController navController;
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private SearchView searchView;
    private FragmentListHoaDonNhapBinding binding;
    private List<HoaDon> list;
    private HoaDonNhapAdapter adapter;
    private DaoHD daoHD;
    private DaoCTHD daoCTHD;
    private DaoHang daoHang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListHoaDonNhapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tbCustom(view);
        openData();
        setViewData();
        spinnerFilter();
    }

    private void tbCustom(View view) {
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        binding.tlbHDNhap.inflateMenu(R.menu.menu_seach);
        drawable = getActivity().getDrawable(R.drawable.ic_menu);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Hoá Đơn Nhập");
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        MenuItem menu = binding.tlbHDNhap.getMenu().findItem(R.id.menu_extra_seach);
        searchView = (SearchView) menu.getActionView();
        searchView.setQueryHint("Mã hóa đơn ...");
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
    }

    public void openData() {
        daoHD = new DaoHD(appCompatActivity);
        daoHD.open();

        daoCTHD = new DaoCTHD(appCompatActivity);
        daoCTHD.open();

        daoHang = new DaoHang(appCompatActivity);
        daoHang.open();
    }

    private void setViewData() {
        list = new ArrayList<>();
        list = daoHD.getAllNhap();
        adapter = new HoaDonNhapAdapter(list, appCompatActivity);

        binding.rvHDN.setAdapter(adapter);
        binding.rvHDN.setLayoutManager(new LinearLayoutManager(appCompatActivity));

        adapter.setImgDelete(new ItemHoaDonClick() {
            @Override
            public void ItemClick(HoaDon hoaDon) {
                dialogDelete(hoaDon);
            }
        });
        adapter.setItemClick(new ItemHoaDonClick() {
            @Override
            public void ItemClick(HoaDon hoaDon) {
                if (daoCTHD.checkCTHD(hoaDon.getMaHD()) >0){
                    Bundle bundle = new Bundle();
                    bundle.putString("maHD", hoaDon.getMaHD());
                    navController.navigate(R.id.listHDNhap_to_ChiTietHDNhap, bundle);
                }else {
                    dialogDeleteErr(hoaDon);
                }
            }
        });
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
    public void dialogDeleteErr(HoaDon hoaDon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle("Xóa");
        builder.setMessage("Sản phẩm đã ngừng kinh doanh.\nBạn nên xóa hóa đơn để tối ưu bộ nhớ!");
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
                    list.addAll(daoHD.getAllNhap());
                    adapter.filter(list);
                } else {
                    Toast.makeText(appCompatActivity, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
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
                    list.addAll(daoHD.getAllNhap());
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
        binding.spListHDNFilter.setAdapter(spAdapter);

        binding.spListHDNFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        list.clear();
                        list.addAll(daoHD.getDays());
                        adapter.filter(list);
                        break;
                    case 2:
                        list.clear();
                        list.addAll(daoHD.getWeek());
                        adapter.filter(list);
                        break;
                    case 3:
                        list.clear();
                        list.addAll(daoHD.getMonth());
                        adapter.filter(list);
                        break;
                    case 0:
                        list.clear();
                        list.addAll(daoHD.getAllNhap());
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
                navController.navigate(R.id.listHDNhap_to_addHDNhap);
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
        daoHang.close();
    }
}