package com.example.app_phone_store_manager_du_an_1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemKhachHangClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChonKHAdapter extends RecyclerView.Adapter<ChonKHAdapter.ViewHolder> {
    private List<KhachHang> list;
    private int checkedPositon = -1;
    private ItemKhachHangClick itemKhachHangClick;


    public ChonKHAdapter(List<KhachHang> list) {
        this.list = list;
    }

    public void setItemKhachHangClick(ItemKhachHangClick itemKhachHangClick) {
        this.itemKhachHangClick = itemKhachHangClick;
    }

    public void setCheckedPositon(int checkedPositon) {
        this.checkedPositon = checkedPositon;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_chon_kh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        KhachHang khachHang = list.get(position);
        if (khachHang == null){
            return;
        }
        holder.tvTenKH.setText(khachHang.getHoTen());
        holder.tvSDT.setText(khachHang.getDienThoai());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPositon = position;
                notifyDataSetChanged();
                itemKhachHangClick.OnItemClick(khachHang);
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
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKH, tvSDT;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTenKH = itemView.findViewById(R.id.tvTenKHItem);
            tvSDT = itemView.findViewById(R.id.tvSDTKHItem);
        }
    }
}
