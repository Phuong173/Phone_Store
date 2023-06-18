package com.example.app_phone_store_manager_du_an_1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.app_phone_store_manager_du_an_1.R;
import com.example.app_phone_store_manager_du_an_1.dao.DaoThuocTinhSanPham;
import com.example.app_phone_store_manager_du_an_1.model.SanPham;
import com.example.app_phone_store_manager_du_an_1.model.ThuocTinhSanPham;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChonSanPhamAdapter extends RecyclerView.Adapter<ChonSanPhamAdapter.ViewHolder> {
    private Context context;
    private List<SanPham> list;
    List<SanPham> listSelected = new ArrayList<>();
    List<SanPham> listData = new ArrayList<>();
    private DaoThuocTinhSanPham daoTT;
    private boolean setcheked = false;
    public List<SanPham> getListSelected() {
        return listSelected;
    }

    public ChonSanPhamAdapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_muti_chon_sp, parent, false);
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
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(ten.substring(0, 1).toUpperCase(), getRandomColor());
            holder.imgAnhSP.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sanPham.getHinhAnh(), 0, sanPham.getHinhAnh().length);
            holder.imgAnhSP.setImageBitmap(bitmap);
        }

        daoTT = new DaoThuocTinhSanPham(context);
        daoTT.open();
        ThuocTinhSanPham ttSP = daoTT.getMaSP(sanPham.getMaSP());

        if (sanPham.getPhanLoai() > 0) {
            holder.tvTenSP.setText("Phụ kiện: " + sanPham.getTenSP());
            holder.tvCTSP.setText("Loại : " + ttSP.getLoaiPhuKien());
        } else {
            holder.tvTenSP.setText("Điện thoại: " + sanPham.getTenSP());
            if (ttSP.getRAM() == null) {
                holder.tvCTSP.setText("Bộ nhớ :" + ttSP.getBoNho());
            } else if (ttSP.getBoNho() == null) {
                holder.tvCTSP.setText("RAM : " + ttSP.getBoNho());
            } else {
                holder.tvCTSP.setText("RAM: " + ttSP.getRAM() + "    Bộ nhớ: " + ttSP.getBoNho());
            }
        }
        daoTT.close();
        holder.lnlChonSL.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcheked = true;
                if (listSelected.contains(sanPham)){
                    holder.itemView.setBackgroundResource(R.color.white);
                    listSelected.remove(sanPham);
                    holder.lnlChonSL.setVisibility(View.GONE);
                }else {
                    holder.itemView.setBackgroundResource(R.color.selected);
                    holder.lnlChonSL.setVisibility(View.VISIBLE);
                    holder.imgTru.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int sl = Integer.parseInt(holder.tvSL.getText().toString());
                            sl -= 1;
                            if (sl < 1) {
                                sl = 1;
                            }
                            holder.tvSL.setText(sl + "");
                            if(Integer.parseInt(holder.tvSL.getText().toString())>0){
                                sanPham.setTinhTrang(Integer.parseInt(holder.tvSL.getText().toString()));
                            }
                        }
                    });
                    holder.imgCong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int sl = Integer.parseInt(holder.tvSL.getText().toString());
                            sl += 1;
                            holder.tvSL.setText(sl + "");
                            if(Integer.parseInt(holder.tvSL.getText().toString())>0){
                                sanPham.setTinhTrang(Integer.parseInt(holder.tvSL.getText().toString()));
                            }
                        }
                    });

                    listSelected.add(sanPham);
                }
                if (listSelected.size() == 0){
                    setcheked = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnhSP, imgCong, imgTru;
        TextView tvTenSP, tvCTSP, tvSL;
        LinearLayout lnlChonSL;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgAnhSP = itemView.findViewById(R.id.imgSPSelected);
            imgTru = itemView.findViewById(R.id.imgDown);
            imgCong = itemView.findViewById(R.id.imgUp);
            tvTenSP = itemView.findViewById(R.id.tvTenSPSelected);
            tvCTSP = itemView.findViewById(R.id.tvCTSPSelected);
            tvSL = itemView.findViewById(R.id.tvSLChonSP);
            lnlChonSL = itemView.findViewById(R.id.lnlSLSelected);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
