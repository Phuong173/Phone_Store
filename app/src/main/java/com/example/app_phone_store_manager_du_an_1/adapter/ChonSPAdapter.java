package com.example.app_phone_store_manager_du_an_1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;
import com.example.app_phone_store_manager_du_an_1.model.ThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemSanPhamClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class ChonSPAdapter extends RecyclerView.Adapter<ChonSPAdapter.Viewholder> {
    private List<SanPham> list;
    private Context context;
    private DaoThuocTinhSanPham daoTT;
    private ItemSanPhamClick itemSanPhamClick;
    private int checkedPositon = -1;

    public ChonSPAdapter(List<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setItemSanPhamClick(ItemSanPhamClick itemSanPhamClick) {
        this.itemSanPhamClick = itemSanPhamClick;
    }

    public void setCheckedPositon(int checkedPositon) {
        this.checkedPositon = checkedPositon;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_chon_sp, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {

        SanPham sanPham = list.get(position);
        if (sanPham == null) {
            return;
        }
        if (sanPham.getHinhAnh() == null) {
            String ten = sanPham.getTenSP();
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(ten.substring(0, 1).toUpperCase(), getRandomColor());
            holder.imgAnh.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sanPham.getHinhAnh(), 0, sanPham.getHinhAnh().length);
            holder.imgAnh.setImageBitmap(bitmap);
        }

        daoTT = new DaoThuocTinhSanPham(context);
        daoTT.open();
        ThuocTinhSanPham ttSP = daoTT.getMaSP(sanPham.getMaSP());

        if (sanPham.getPhanLoai() > 0) {
            holder.tvTen.setText("Phụ kiện: " + sanPham.getTenSP());
            holder.tvChiTiet.setText("Loại : " + ttSP.getLoaiPhuKien());
        } else {
            holder.tvTen.setText("Điện thoại: " + sanPham.getTenSP());
            if (ttSP.getRAM() == null) {
                holder.tvChiTiet.setText("Bộ nhớ :" + ttSP.getBoNho());
            } else if (ttSP.getBoNho() == null) {
                holder.tvChiTiet.setText("RAM : " + ttSP.getBoNho());
            } else {
                holder.tvChiTiet.setText("RAM: " + ttSP.getRAM() + "    Bộ nhớ: " + ttSP.getBoNho());
            }
        }

        daoTT.close();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPositon = position;
                notifyDataSetChanged();
                itemSanPhamClick.ItemClick(sanPham);
            }
        });

        if (checkedPositon == position) {
            holder.itemView.setBackgroundResource(R.color.selected);
        } else {
            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        ImageView imgAnh;
        TextView tvTen, tvChiTiet;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.imgChonSP);
            tvTen = itemView.findViewById(R.id.tvChonTenSP);
            tvChiTiet = itemView.findViewById(R.id.tvChonSPCT);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
