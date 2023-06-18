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
import com.example.app_phone_store_manager_du_an_1.dao.DaoCTHD;
import com.example.app_phone_store_manager_du_an_1.dao.DaoSanPham;
import com.example.app_phone_store_manager_du_an_1.model.ChiTietHoaDon;
import com.example.app_phone_store_manager_du_an_1.model.HoaDon;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemHoaDonClick;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class HoaDonNhapAdapter extends RecyclerView.Adapter<HoaDonNhapAdapter.ViewHolder> {
    private List<HoaDon> list;
    private Context context;
    private DaoSanPham daoSanPham;
    private DaoCTHD daoCTHD;
    private ItemHoaDonClick imgDelete;
    private ItemHoaDonClick itemClick;

    public HoaDonNhapAdapter(List<HoaDon> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setImgDelete(ItemHoaDonClick imgDelete) {
        this.imgDelete = imgDelete;
    }

    public void setItemClick(ItemHoaDonClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_hd_nhap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        HoaDon hoaDon = list.get(position);
        if (hoaDon == null) {
            return;
        }
        holder.tvMaHD.setText("Mã HĐ: " + hoaDon.getMaHD());
        holder.tvNgayNhap.setText("Ngày: " + hoaDon.getNgay());

        daoSanPham = new DaoSanPham(context);
        daoSanPham.open();
        daoCTHD = new DaoCTHD(context);
        daoCTHD.open();
        if (daoCTHD.checkCTHD(hoaDon.getMaHD()) > 0){
            ChiTietHoaDon chiTietHoaDon = daoCTHD.getMaHD(hoaDon.getMaHD());

            String maSP = chiTietHoaDon.getMaSP();
            if (daoSanPham.checkMaSP(maSP) > 0){
                String tenSP = daoSanPham.getMaSP(maSP).getTenSP();
                holder.tvTenSP.setText("Sản phẩm: " + tenSP);
                int ttSP = daoSanPham.getMaSP(maSP).getTinhTrang();
                if (daoSanPham.getMaSP(maSP).getHinhAnh() == null) {
                    TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(tenSP.substring(0, 1).toUpperCase(), getRandomColor());
                    holder.imgSP.setImageDrawable(textDrawable);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(daoSanPham.getMaSP(maSP).getHinhAnh(), 0, daoSanPham.getMaSP(maSP).getHinhAnh().length);
                    holder.imgSP.setImageBitmap(bitmap);
                }
                switch (ttSP) {
                    case 0:
                        holder.tvTTSP.setText("Tình trạng: Like new 99%");
                        break;
                    case 1:
                        holder.tvTTSP.setText("Tình trạng: Mới 100%");
                        break;
                    default:
                        holder.tvTTSP.setText("Tình trạng: Cũ");
                        break;
                }
            }else {
                TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect("N".toUpperCase(), getRandomColor());
                holder.imgSP.setImageDrawable(textDrawable);
                holder.tvTenSP.setText("Sản phẩm: ngừng kinh doanh");
                holder.tvTenSP.setTextColor(context.getResources().getColor(R.color.red));
                holder.tvTTSP.setText("Tình trạng: null");
            }
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            holder.tvDonGia.setText(formatter.format(chiTietHoaDon.getDonGia()) + " đ");
            holder.tvSL.setText("Số lượng: " + chiTietHoaDon.getSoLuong());
            double thanhTien = chiTietHoaDon.getDonGia() * chiTietHoaDon.getSoLuong();
            holder.tvThanhTien.setText(formatter.format(thanhTien) + " đ");
        }else {
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect("N".toUpperCase(), getRandomColor());
            holder.imgSP.setImageDrawable(textDrawable);
            holder.tvDonGia.setText("null");
            holder.tvTenSP.setText("Sản phẩm: ngừng kinh doanh");
            holder.tvTenSP.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvTTSP.setText("Tình trạng: null");
            holder.tvSL.setText("Số lượng: null");
            holder.tvThanhTien.setText("null");
        }

        daoCTHD.close();
        daoSanPham.close();

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDelete.ItemClick(hoaDon);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.ItemClick(hoaDon);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void filter(List<HoaDon> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHD, tvTenSP, tvTTSP, tvSL, tvNgayNhap, tvDonGia, tvThanhTien;
        ImageView imgDelete, imgSP;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvMaHD = itemView.findViewById(R.id.tvMaHDNItem);
            tvTenSP = itemView.findViewById(R.id.tvTenSPNItem);
            tvTTSP = itemView.findViewById(R.id.tvTTSPNItem);
            tvSL = itemView.findViewById(R.id.tvSLNhapItem);
            tvNgayNhap = itemView.findViewById(R.id.tvNgayNhapItem);
            tvDonGia = itemView.findViewById(R.id.tvDonGiaItem);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTienItem);
            imgDelete = itemView.findViewById(R.id.imgDeleteHDNItem);
            imgSP = itemView.findViewById(R.id.imgSPHDNItem);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
