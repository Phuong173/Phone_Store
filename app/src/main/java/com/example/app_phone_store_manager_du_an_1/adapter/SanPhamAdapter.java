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
import com.example.app_phone_store_manager_du_an_1.dao.DaoHang;
import com.example.app_phone_store_manager_du_an_1.dao.DaoThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.model.Hang;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;
import com.example.app_phone_store_manager_du_an_1.model.ThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.utilities.ItemSanPhamClick;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private List<SanPham> list;
    private Context context;
    private DaoHang daoHang;
    private DaoThuocTinhSanPham daoTTSP;
    private TextDrawable textDrawable;
    private ItemSanPhamClick itemDelete;
    private ItemSanPhamClick itemClick;

    public void setItemDelete(ItemSanPhamClick itemDelete) {
        this.itemDelete = itemDelete;
    }

    public void setItemClick(ItemSanPhamClick itemClick) {
        this.itemClick = itemClick;
    }

    public SanPhamAdapter(List<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_sanpham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        SanPham sanPham = list.get(position);
        if (sanPham == null) {
            return;
        }
        if (sanPham.getHinhAnh() == null) {
            String ten = sanPham.getTenSP();
            setImage(ten, holder.imgSP);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sanPham.getHinhAnh(), 0, sanPham.getHinhAnh().length);
            holder.imgSP.setImageBitmap(bitmap);
        }

        daoHang = new DaoHang(context);
        daoHang.open();
        daoTTSP = new DaoThuocTinhSanPham(context);
        daoTTSP.open();
        if (daoTTSP.checkTTMaSP(sanPham.getMaSP()) > 0) {
            ThuocTinhSanPham ttSP = daoTTSP.getMaSP(sanPham.getMaSP());
            Hang hang = daoHang.getMaHang(sanPham.getMaHang());

            if (hang.getHinhAnh() == null) {
                String tenHang = hang.getTenHang();
                setImage(tenHang, holder.imgHang);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhAnh(), 0, hang.getHinhAnh().length);
                holder.imgHang.setImageBitmap(bitmap);
            }
            holder.tvHang.setText(hang.getTenHang());
            if (sanPham.getPhanLoai() > 0) {
                holder.tvPLSP.setText("Loại: Phụ kiện  ");
                holder.tvCTSP.setText("Loại phụ kiện: " + ttSP.getLoaiPhuKien());
            } else {
                holder.tvPLSP.setText("Loại: Điện thoại  ");
                holder.tvCTSP.setText("RAM: " + ttSP.getRAM() + "  -  Bộ nhớ: " + ttSP.getBoNho());
            }
        } else {
            holder.tvPLSP.setText("Chưa cập nhập");
            holder.tvCTSP.setText("Chưa cập nhập");
        }

        daoTTSP.close();
        daoHang.close();

        holder.tvMaSP.setText("Mã sản phẩm: " + sanPham.getMaSP());
        holder.tvTenSP.setText("Sản phẩm: " + sanPham.getTenSP());
        switch (sanPham.getTinhTrang()) {
            case 0:
                holder.tvTinhTrang.setText("-  Tình trạng: Like new 99%");
                break;
            case 1:
                holder.tvTinhTrang.setText("-  Tình trạng: Mới 100%");
                break;
            default:
                holder.tvTinhTrang.setText("-  Tình trạng: Cũ");
                break;
        }
        holder.tvMTSP.setText("Mô tả: " + sanPham.getMoTa());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvGiaTien.setText(formatter.format(sanPham.getGiaTien()) + " đ");

        if (sanPham.getTrangThai() == 0) {
            holder.tvTrangThai.setText("Chưa lưu kho");
        } else {
            holder.tvTrangThai.setText("Đã lưu kho");
            holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemDelete.ItemClick(sanPham);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.ItemClick(sanPham);
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

    public void filter(List<SanPham> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSP, imgHang, imgDelete;
        TextView tvMaSP, tvTenSP, tvTinhTrang, tvPLSP, tvMTSP, tvCTSP, tvGiaTien, tvTrangThai, tvHang;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgSP = itemView.findViewById(R.id.imgSPItem);
            imgHang = itemView.findViewById(R.id.imgHangSPItem);
            imgDelete = itemView.findViewById(R.id.imgDeleteSPItem);
            tvMaSP = itemView.findViewById(R.id.tvMaSPItem);
            tvTenSP = itemView.findViewById(R.id.tvTenSPItem);
            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrangSPItem);
            tvMTSP = itemView.findViewById(R.id.tvMTSPItem);
            tvPLSP = itemView.findViewById(R.id.tvPhanLoaiSPItem);
            tvCTSP = itemView.findViewById(R.id.tvCTSPItem);
            tvGiaTien = itemView.findViewById(R.id.tvGiaTienSPItem);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiSPItem);
            tvHang = itemView.findViewById(R.id.tvHangSPItem);

        }
    }

    public void setImage(String s, ImageView imageView) {
        textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(s.substring(0, 1).toUpperCase(), getRandomColor());
        imageView.setImageDrawable(textDrawable);
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
