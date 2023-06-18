package com.example.app_phone_store_manager_du_an_1.ui.TaiKhoan;

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
import com.example.app_phone_store_manager_du_an_1.adapter.NhanVienAdapter;
import com.example.app_phone_store_manager_du_an_1.dao.DaoNhanVien;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentListHangBinding;
import com.example.app_phone_store_manager_du_an_1.databinding.FragmentListTaiKhoanBinding;
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemNhanVienClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ListTaiKhoanFragment extends Fragment {
    private NavController navController;
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private SearchView searchView;
    private FragmentListTaiKhoanBinding binding;
    private NhanVienAdapter adapter;
    private DaoNhanVien dao;
    private List<NhanVien> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListTaiKhoanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();

        binding.tlbTaiKhoan.inflateMenu(R.menu.menu_seach);
        drawable = getActivity().getDrawable(R.drawable.ic_menu);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Tài Khoản");
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);

        MenuItem menu = binding.tlbTaiKhoan.getMenu().findItem(R.id.menu_extra_seach);
        searchView = (SearchView) menu.getActionView();
        searchView.setQueryHint("Mã nhân viên, Họ tên, Tài khoản");
        searchView.setMaxWidth(Integer.MAX_VALUE);

        EditText edSeach = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        edSeach.setTextColor(Color.BLACK);
        edSeach.setHintTextColor(Color.LTGRAY);

        ImageView iconSeach = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_button);
        ImageView iconClose = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconSeach.setColorFilter(Color.BLACK);
        iconClose.setColorFilter(Color.BLACK);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(appCompatActivity, R.array.filterNV, R.layout.custom_item_sp);
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
                        list.addAll(dao.getAllSXTen());
                        adapter.filter(list);
                        break;
                    case 2:
                        list.clear();
                        list.addAll(dao.getAllSXMa());
                        adapter.filter(list);
                        break;
                        case 3:
                        list.clear();
                        list.addAll(dao.getAllSXTK());
                        adapter.filter(list);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dao = new DaoNhanVien(getContext());
        dao.openNV();

        list = new ArrayList<>();
        list = dao.getAll();

        adapter = new NhanVienAdapter(list);

        binding.rclNhanVien.setAdapter(adapter);
        binding.rclNhanVien.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setItemClick(new ItemNhanVienClick() {
            @Override
            public void OnItemClick(NhanVien nhanVien) {
                Bundle bundle = new Bundle();
                bundle.putString("maNV", nhanVien.getMaNV());
                navController.navigate(R.id.listTk_to_ChiTietTk, bundle);
            }
        });
        adapter.setImgCallClick(new ItemNhanVienClick() {
            @Override
            public void OnItemClick(NhanVien nhanVien) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + nhanVien.getDienThoai()));
                startActivity(intent);
            }
        });
        adapter.setImgDelClick(new ItemNhanVienClick() {
            @Override
            public void OnItemClick(NhanVien nhanVien) {
                deleteDialogNV(nhanVien);
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
                navController.navigate(R.id.listTk_to_add_Tk);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteDialogNV(NhanVien nhanVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xóa");
        builder.setMessage("Sẽ xóa hết dữ liệu liên quan đến nhân viên này.\nBạn có chắc chắn muốn xóa không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                int kq = dao.deleteNV(nhanVien);
                if (kq > 0) {
                    Toast.makeText(getContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list.addAll(dao.getAll());
                    adapter.filter(list);
                } else {
                    Toast.makeText(getContext(), "Xóa Thất Bại", Toast.LENGTH_SHORT).show();
                }
                dialogInterface.cancel();
            }

        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.closeNV();
    }
}