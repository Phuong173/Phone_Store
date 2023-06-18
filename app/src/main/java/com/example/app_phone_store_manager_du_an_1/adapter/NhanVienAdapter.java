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
import com.example.app_phone_store_manager_du_an_1.model.NhanVien;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemNhanVienClick;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.Viewholder> implements Filterable {
    private List<NhanVien> list;
    private List<NhanVien> mlistOld;
    private ItemNhanVienClick itemClick;
    private ItemNhanVienClick imgDelClick;
    private ItemNhanVienClick imgCallClick;

    public void setItemClick(ItemNhanVienClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setImgDelClick(ItemNhanVienClick imgDelClick) {
        this.imgDelClick = imgDelClick;
    }

    public void setImgCallClick(ItemNhanVienClick imgCallClick) {
        this.imgCallClick = imgCallClick;
    }

    public NhanVienAdapter(List<NhanVien> list) {
        this.list = list;
        this.mlistOld = list;
    }

    @NonNull
    @NotNull
    @Override
    public NhanVienAdapter.Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_nhan_vien, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NhanVienAdapter.Viewholder holder, int position) {
        NhanVien nhanVien = list.get(position);
        if (nhanVien == null) {
            return;

        } else {
            holder.tvMaNV.setText("Mã NV: " + nhanVien.getMaNV());
            holder.tvHoTenNV.setText("Họ Tên: " + nhanVien.getHoTen());
            holder.tvDienThoaiNV.setText("Số Điện Thoại: " + nhanVien.getDienThoai());
            holder.tvTaiKhoanNV.setText("Tài Khoản: " + nhanVien.getTaiKhoan());
            holder.tvDiaChiNV.setText("Địa Chỉ: " + nhanVien.getDiaChi());
            holder.tvNamSinhNV.setText("Năm Sinh: " + nhanVien.getNamSinh());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.OnItemClick(nhanVien);
            }
        });
        holder.imgDelNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDelClick.OnItemClick(nhanVien);
            }
        });
        holder.imgCallNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCallClick.OnItemClick(nhanVien);
            }
        });

    }

    public void filter(List<NhanVien> list) {
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
                String srchNV = constraint.toString();
                if (srchNV.isEmpty()) {
                    list = mlistOld;
                } else {
                    List<NhanVien> listnv = new ArrayList<>();
                    for (NhanVien nhanVien : mlistOld) {
                        if (nhanVien.getMaNV().toLowerCase().contains(srchNV.toLowerCase()) ||
                                nhanVien.getHoTen().toLowerCase().contains(srchNV.toLowerCase()) ||
                                nhanVien.getTaiKhoan().toLowerCase().contains(srchNV.toLowerCase())) {
                            listnv.add(nhanVien);
                        }
                    }
                    list = listnv;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<NhanVien>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvMaNV, tvHoTenNV, tvDienThoaiNV,
                tvDiaChiNV, tvTaiKhoanNV, tvNamSinhNV,
                tvMatKhauNV;
        private ImageView imgDelNV, imgCallNV;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvMaNV = itemView.findViewById(R.id.tvMaNV);
            tvHoTenNV = itemView.findViewById(R.id.tvHoTenNV);
            tvDienThoaiNV = itemView.findViewById(R.id.tvDienThoaiNV);
            tvTaiKhoanNV = itemView.findViewById(R.id.tvTaiKhoanNV);
            tvDiaChiNV = itemView.findViewById(R.id.tvDiaChiNV);
            tvNamSinhNV = itemView.findViewById(R.id.tvNamsinhNV);
            imgDelNV = itemView.findViewById(R.id.imgDelNV);
            imgCallNV = itemView.findViewById(R.id.imgPhoneNV);
        }
    }
}
