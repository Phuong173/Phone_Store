package com.example.app_phone_store_manager_du_an_1.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.model.KhachHang;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemKhachHangClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> implements Filterable {
    private List<KhachHang> list;
    private ItemKhachHangClick itemClick;
    private ItemKhachHangClick imgCallClick;
    private ItemKhachHangClick imgDeleteClick;
    private List<KhachHang> mlistKHold;

    public void setItemClick(ItemKhachHangClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setImgCallClick(ItemKhachHangClick imgCallClick) {
        this.imgCallClick = imgCallClick;
    }

    public void setImgDeleteClick(ItemKhachHangClick imgDeleteClick) {
        this.imgDeleteClick = imgDeleteClick;
    }

    public KhachHangAdapter(List<KhachHang> list) {
        this.list = list;
        this.mlistKHold = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_khach_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        KhachHang khachHang = list.get(position);
        if (khachHang == null) {
            return;
        } else {
            holder.tvMaKH.setText("Mã KH: " + khachHang.getMaKH());
            holder.tvHoTenKH.setText("Họ tên: " + khachHang.getHoTen());
            holder.tvPhoneKH.setText("Số điện thoại: " + khachHang.getDienThoai());
            holder.tvDiaChiKH.setText("Địa chỉ: " + khachHang.getDiaChi());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.OnItemClick(khachHang);
                }
            });

            holder.imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgCallClick.OnItemClick(khachHang);
                }
            });

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgDeleteClick.OnItemClick(khachHang);
                }
            });
        }
    }
    public void filter(List<KhachHang> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String serKH = constraint.toString();
                if (serKH.isEmpty()) {
                    list = mlistKHold;
                } else {
                    List<KhachHang> listkh = new ArrayList<>();
                    for (KhachHang khachHang : mlistKHold) {
                        if (khachHang.getMaKH().toLowerCase().contains(serKH.toLowerCase()) ||
                                khachHang.getHoTen().toLowerCase().contains(serKH.toLowerCase())) {
                            listkh.add(khachHang);
                        }
                    }
                    list = listkh;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<KhachHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMaKH, tvHoTenKH, tvPhoneKH, tvDiaChiKH;
        private ImageView imgCall, imgDelete;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvMaKH = itemView.findViewById(R.id.tvMaKH);
            tvHoTenKH = itemView.findViewById(R.id.tvHoTenKH);
            tvPhoneKH = itemView.findViewById(R.id.tvDienThoaiKH);
            tvDiaChiKH = itemView.findViewById(R.id.tvDiaChiKH);
            imgCall = itemView.findViewById(R.id.imgPhoneKH);
            imgDelete = itemView.findViewById(R.id.imgDelKH);
        }
    }
}
