package com.example.app_phone_store_manager_du_an_1.ui.Hang;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.model.Hang;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ChiTietHangFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private String maHang;
    private DaoHang dao;
    private ImageView imgHang;
    private TextView tvHang;
    private TextDrawable textDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chi_tiet_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();
        imgHang = view.findViewById(R.id.imgHangShow);
        tvHang = view.findViewById(R.id.tvTenHangShow);
        if (getArguments() != null){
            maHang = getArguments().getString("maHang");
        }

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle(maHang);

        dao = new DaoHang(appCompatActivity);
        dao.open();

        Hang hang = dao.getMaHang(maHang);
        if (hang.getHinhAnh() == null) {
            textDrawable = TextDrawable.builder().beginConfig().width(100).height(100).endConfig().buildRect(hang.getTenHang().substring(0, 1).toUpperCase(), getRandomColor());
            imgHang.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhAnh(), 0, hang.getHinhAnh().length);
            imgHang.setImageBitmap(bitmap);
        }
        tvHang.setText(hang.getTenHang());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigate(R.id.chiTietHang_to_listHang);
                return true;
            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putString("maHang", maHang);
                navController.navigate(R.id.chiTietHang_to_editHang, bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}