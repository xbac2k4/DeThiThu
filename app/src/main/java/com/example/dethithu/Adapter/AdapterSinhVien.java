package com.example.dethithu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dethithu.Model.SinhVien;
import com.example.dethithu.R;
import com.example.dethithu.Service.OnClickListen;

import java.util.ArrayList;

public class AdapterSinhVien extends RecyclerView.Adapter<AdapterSinhVien.ViewHolder>{
    private Context context;
    private ArrayList<SinhVien> list;
    OnClickListen onClickListen;

    public void setOnClickListen(OnClickListen onClickListen) {
        this.onClickListen = onClickListen;
    }

    public AdapterSinhVien(Context context, ArrayList<SinhVien> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sinhVien = list.get(position);
        holder.tvMa.setText("Mã sinh viên: " + sinhVien.getMa_sinh_vien_ph44315());
        holder.tvTen.setText("Tên: " + sinhVien.getTen_ph44315());
        holder.tvGioiTinh.setText("Giới tính: " + sinhVien.getGioi_tinh_ph44315());
        holder.tvNgaySinh.setText("Ngày sinh: " + sinhVien.getNgay_sinh_ph44315().substring(0, 10));
        holder.tvKhoaHoc.setText("Khóa học: " + sinhVien.getKhoa_hoc_ph44315());
        holder.tvLop.setText("Lớp: " + sinhVien.getLop_ph44315());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListen.Update(sinhVien);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListen.Delete(sinhVien);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListen.ChiTiet(sinhVien);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa, tvTen, tvGioiTinh, tvNgaySinh, tvKhoaHoc, tvLop;
        ImageButton edit, delete;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMa = itemView.findViewById(R.id.tv_ma_sv);
            tvTen =  itemView.findViewById(R.id.tv_ten_sv);
            tvNgaySinh = itemView.findViewById(R.id.tv_ngay_sinh);
            tvGioiTinh = itemView.findViewById(R.id.tv_gioi_tinh);
            tvKhoaHoc = itemView.findViewById(R.id.tv_khoa_hoc);
            tvLop = itemView.findViewById(R.id.tv_lop);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
            layout = itemView.findViewById(R.id.layout_item);
        }
    }
}
