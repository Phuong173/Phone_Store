package com.example.app_phone_store_manager_du_an_1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.model.Top10SanPham;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.Top10ViewHoler> {
    List<Top10SanPham> list;

    public Top10Adapter(List<Top10SanPham> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public Top10ViewHoler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_top10, parent, false);
        return new Top10ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Top10ViewHoler holder, int position) {
        Top10SanPham top = list.get(position);
        if(top == null){
            return;
        }
        holder.tvTenSpTop.setText("Tên Sản Phẩm: " + top.getTenSP());
        holder.tvSlTop.setText("Số lượng: " + top.getSoLuong());
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class Top10ViewHoler extends RecyclerView.ViewHolder {
        TextView tvTenSpTop, tvSlTop;
        public Top10ViewHoler(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTenSpTop = itemView.findViewById(R.id.tvTenSpTop);
            tvSlTop = itemView.findViewById(R.id.tvSlTop);
        }
    }
}
