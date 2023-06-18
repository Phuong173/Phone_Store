package com.example.app_phone_store_manager_du_an_1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.model.ChiTietHoaDon;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class DoanhThuAdapter extends RecyclerView.Adapter<DoanhThuAdapter.DoanhThuViewHoler> {
    private Context context;
    private List<HoaDon> list;
    private DaoCTHD daoCTHD;

    public DoanhThuAdapter(Context context, List<HoaDon> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public DoanhThuViewHoler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_item_doanhthu, parent, false);
        return new DoanhThuViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DoanhThuViewHoler holder, int position) {
        HoaDon hoaDon = list.get(position);
        if (hoaDon == null) {
            return;
        }
        holder.tvNgay.setText(hoaDon.getNgay());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        daoCTHD = new DaoCTHD(context);
        daoCTHD.open();

        if (hoaDon.getPhanLoai() > 0) {
            holder.tvPL.setText("Xuất hàng");
            List<ChiTietHoaDon> hoaDonList = daoCTHD.getListMaHD(hoaDon.getMaHD());
            double tien = 0;
            int khuyenMai = 0;
            for (ChiTietHoaDon x : hoaDonList) {
                tien += (x.getDonGia() * x.getSoLuong());
                khuyenMai = x.getGiamGia();
            }
            switch (khuyenMai) {
                case 0:
                    holder.tvTien.setText(formatter.format(tien) + " đ");
                    break;
                case 1:
                    holder.tvTien.setText(formatter.format(tien - tien * 0.05) + " đ");
                    break;
                case 2:
                    holder.tvTien.setText(formatter.format(tien - tien * 0.1) + " đ");
                    break;
                case 3:
                    holder.tvTien.setText(formatter.format(tien - tien * 0.15) + " đ");
                    break;
                case 4:
                    holder.tvTien.setText(formatter.format(tien - tien * 0.2) + " đ");
                    break;
                case 5:
                    holder.tvTien.setText(formatter.format(tien - tien * 0.25) + " đ");
                    break;
                case 6:
                    holder.tvTien.setText(formatter.format(tien - tien * 0.3) + " đ");
                    break;
            }
        } else {
            holder.tvPL.setText("Nhập hàng");
            double tong = 0;
            ChiTietHoaDon chiTietHoaDon = daoCTHD.getMaHD(hoaDon.getMaHD());
            tong = chiTietHoaDon.getDonGia() * chiTietHoaDon.getSoLuong();
            holder.tvTien.setText(formatter.format(tong) + " đ");
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }
    public void filter(List<HoaDon> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public class DoanhThuViewHoler extends RecyclerView.ViewHolder {
        TextView tvNgay, tvTien, tvPL;

        public DoanhThuViewHoler(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNgay = itemView.findViewById(R.id.tvDateDT);
            tvTien = itemView.findViewById(R.id.tvDT);
            tvPL = itemView.findViewById(R.id.tvPlDT);
        }
    }
}
