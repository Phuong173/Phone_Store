package com.example.app_phone_store_manager_du_an_1.ui.KhachHang;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import com.example.app_phone_store_manager_du_an_1.adapter.KhachHangAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoKhachHang;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentListKhachHangBinding;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemKhachHangClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListKhachHangFragment extends Fragment {
    private NavController navController;
    private FragmentListKhachHangBinding binding;// lớp liên kết trực tiếp đến XML
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private SearchView searchView;
    private List<KhachHang> list;
    private KhachHangAdapter adapter;
    private DaoKhachHang dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListKhachHangBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();

        appCompatActivity.getSupportActionBar().setTitle("Khách Hàng");
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawable = appCompatActivity.getDrawable(R.drawable.ic_menu);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);

        binding.tlbKhachHang.inflateMenu(R.menu.menu_header);

        MenuItem menuItem = binding.tlbKhachHang.getMenu().findItem(R.id.menu_search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Tên Khách Hàng, Mã Khách Hàng");
        searchView.setMaxWidth(Integer.MAX_VALUE);

        EditText edSeach = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        edSeach.setTextColor(Color.BLACK);
        edSeach.setHintTextColor(Color.LTGRAY);

        ImageView iconSeach = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_button);
        ImageView iconClose = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconSeach.setColorFilter(Color.BLACK);
        iconClose.setColorFilter(Color.BLACK);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(appCompatActivity, R.array.filter, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);

        binding.spListFilter.setAdapter(spAdapter);
        binding.spListFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        list.clear();
                        list.addAll(dao.getAll());
                        adapter.filter(list);
                        break;
                    case 1:
                        list.clear();
                        list.addAll(dao.getAllSXTenKH());
                        adapter.filter(list);
                        break;
                    case 2:
                        list.clear();
                        list.addAll(dao.getAllSXMaKH());
                        adapter.filter(list);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        binding.tlbKhachHang.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_loc:
                        return true;
                    default:
                        return false;
                }
            }
        });

        dao = new DaoKhachHang(getActivity());
        dao.open();

        list = new ArrayList<>();
        list = dao.getAll();

        adapter = new KhachHangAdapter(list);

        binding.rvKH.setAdapter(adapter);
        binding.rvKH.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setImgCallClick(new ItemKhachHangClick() {
            @Override
            public void OnItemClick(KhachHang khachHang) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + khachHang.getDienThoai()));
                startActivity(intent);
            }
        });
        adapter.setImgDeleteClick(new ItemKhachHangClick() {
            @Override
            public void OnItemClick(KhachHang khachHang) {
                dialogDelete(khachHang);
            }
        });
        adapter.setItemClick(new ItemKhachHangClick() {
            @Override
            public void OnItemClick(KhachHang khachHang) {
                Bundle bundle = new Bundle();
                bundle.putString("maKH", khachHang.getMaKH());
                navController.navigate(R.id.action_listKH_to_chitietKH, bundle);
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
                navController.navigate(R.id.action_listKH_to_addKH);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void dialogDelete(KhachHang khachHang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Sẽ xóa hết dữ liệu liên quan đến khách hàng này.\nBạn có chắc chắn muốn xóa không?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int kq = dao.deleteKH(khachHang);
                if (kq > 0) {
                    Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list.addAll(dao.getAll());
                    adapter.filter(list);
                } else {
                    Toast.makeText(getContext(), "Xóa thất Bại", Toast.LENGTH_SHORT).show();
                }
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }

}