package com.example.dethithu;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dethithu.Adapter.AdapterSinhVien;
import com.example.dethithu.Model.Response;
import com.example.dethithu.Model.SinhVien;
import com.example.dethithu.Service.HttpRequest;
import com.example.dethithu.Service.OnClickListen;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    ArrayList<SinhVien> list = new ArrayList<>();
    RecyclerView recyclerView;
    Toolbar toolbar;
    HttpRequest httpRequest = new HttpRequest();
    AdapterSinhVien adapter;
    ArrayList<SinhVien> listSeacrch = new ArrayList<>();
    android.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhxa();
        handleCallData();
    }
    private void anhxa() {
        recyclerView = findViewById(R.id.rcv);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");
        getSupportActionBar().setSubtitle("Quản lý sinh viên");
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_them) {
            Intent intent = new Intent(MainActivity.this, AddSinhVien.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterList(String text) {
        if (!text.equals("")) {
            listSeacrch.clear();
            httpRequest.callAPI().searchSinhVien(text).enqueue(new Callback<Response<ArrayList<SinhVien>>>() {
                @Override
                public void onResponse(Call<Response<ArrayList<SinhVien>>> call, retrofit2.Response<Response<ArrayList<SinhVien>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                            getData(listSeacrch);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<ArrayList<SinhVien>>> call, Throwable t) {

                }
            });
        } else {
            handleCallData();
        }
    }
    private void handleCallData() {
        httpRequest.callAPI().getSinhVien().enqueue(new Callback<Response<ArrayList<SinhVien>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<SinhVien>>> call, retrofit2.Response<Response<ArrayList<SinhVien>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        list = response.body().getData();
                        getData(list);
                        Log.d(TAG, "onResponse: " + list);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<SinhVien>>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
    private void getData(ArrayList<SinhVien> list) {
        adapter = new AdapterSinhVien(MainActivity.this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListen(new OnClickListen() {
            @Override
            public void Update(SinhVien sinhVien) {
                Intent intent = new Intent(MainActivity.this, AddSinhVien.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", sinhVien.get_id());
                bundle.putString("ma", sinhVien.getMa_sinh_vien_ph44315());
                bundle.putString("ten", sinhVien.getTen_ph44315());
                bundle.putString("ngaysinh", sinhVien.getNgay_sinh_ph44315().substring(0, 10));
                bundle.putString("gioitinh", sinhVien.getGioi_tinh_ph44315());
                bundle.putString("khoahoc", sinhVien.getKhoa_hoc_ph44315());
                bundle.putString("lop", sinhVien.getLop_ph44315());
                bundle.putInt("type", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void Delete(SinhVien sinhVien) {
                Dialod_Delete(sinhVien);
            }

            @Override
            public void ChiTiet(SinhVien sinhVien) {
                Dialog_ChiTiet(sinhVien);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleCallData();
    }

    private void Dialod_Delete(SinhVien sinhVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
        builder.setIcon(R.drawable.warning).setTitle("Cảnh Báo");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                httpRequest.callAPI().deleteSinhVien(sinhVien.get_id()).enqueue(new Callback<Response<Void>>() {
                    @Override
                    public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                handleCallData();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Void>> call, Throwable t) {

                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }
    private void Dialog_ChiTiet(SinhVien sinhVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_chitiet, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_id = view.findViewById(R.id.tv_id);
        TextView tv_ten = view.findViewById(R.id.tv_ten_sv);
        TextView tv_ma = view.findViewById(R.id.tv_ma_sv);
        TextView tv_gt = view.findViewById(R.id.tv_gioi_tinh);
        TextView tv_ns = view.findViewById(R.id.tv_ngay_sinh);
        TextView tv_kh = view.findViewById(R.id.tv_khoa_hoc);
        TextView tv_lop = view.findViewById(R.id.tv_lop);

        tv_id.setText("ID: " + sinhVien.get_id());
        tv_ma.setText("Mã sinh viên: " + sinhVien.getMa_sinh_vien_ph44315());
        tv_ten.setText("Tên sinh viên: " + sinhVien.getTen_ph44315());
        tv_gt.setText("Giới tính: " + sinhVien.getGioi_tinh_ph44315());
        tv_ns.setText("Ngày sinh: " + sinhVien.getNgay_sinh_ph44315().substring(0, 10));
        tv_kh.setText("Khóa học: " + sinhVien.getKhoa_hoc_ph44315());
        tv_lop.setText("Lớp: " + sinhVien.getLop_ph44315());

        view.findViewById(R.id.btn_dong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}