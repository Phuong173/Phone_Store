package com.example.app_phone_store_manager_du_an_1.ui.Hang;

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
import com.example.app_phone_store_manager_du_an_1.adapter.HangAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentListHangBinding;
import com.example.app_phone_store_manager_du_an_1.model.Hang;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemHangClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ListHangFragment extends Fragment {
    private NavController navController;
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private SearchView searchView;
    private FragmentListHangBinding binding;
    private DaoHang dao;
    private HangAdapter adapter;
    private List<Hang> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListHangBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();

        binding.tlbHang.inflateMenu(R.menu.menu_seach);
        drawable = getActivity().getDrawable(R.drawable.ic_menu);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Hãng");
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);

        spinnerFilter();

        MenuItem menu = binding.tlbHang.getMenu().findItem(R.id.menu_extra_seach);
        searchView = (SearchView) menu.getActionView();
        searchView.setQueryHint("Tên hãng, Mã hãng");

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

        dao = new DaoHang(appCompatActivity);
        dao.open();

        list = new ArrayList<>();
        list = dao.getAll();

        adapter = new HangAdapter(list);
        binding.rvHang.setAdapter(adapter);
        binding.rvHang.setLayoutManager(new LinearLayoutManager(appCompatActivity));

        adapter.setImgDeleteOnClick(new ItemHangClick() {
            @Override
            public void ItemClick(Hang hang) {
                dialogDelete(hang);
            }
        });
        adapter.setItemOnClick(new ItemHangClick() {
            @Override
            public void ItemClick(Hang hang) {
                Bundle bundle = new Bundle();
                bundle.putString("maHang", hang.getMaHang());
                navController.navigate(R.id.listHang_to_chiTietHang, bundle);
            }
        });
    }

    private void spinnerFilter() {
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(appCompatActivity, R.array.filter, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);
        binding.spListHangFilter.setAdapter(spAdapter);

        binding.spListHangFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        list.addAll(dao.getAllSXTen());
                        adapter.filter(list);
                        break;
                    case 2:
                        list.clear();
                        list.addAll(dao.getAllSXMa());
                        adapter.filter(list);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filter(String newText) {
        List<Hang> filterList = new ArrayList<>();
        for (Hang item : list) {
            if (item.getMaHang().toLowerCase().contains(newText.toLowerCase()) ||
                    item.getTenHang().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapter.filter(filterList);
    }

    public void dialogDelete(Hang hang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle("Xóa");
        builder.setMessage("Sẽ xóa hết dữ liệu liên quan đến hãng này.\nBạn có chắc chắn muốn xóa không!");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int kq = dao.delete(hang.getMaHang());
                if (kq > 0) {
                    Toast.makeText(appCompatActivity, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list.addAll(dao.getAll());
                    adapter.filter(list);
                } else {
                    Toast.makeText(appCompatActivity, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
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
                navController.navigate(R.id.listHang_to_addHang);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}