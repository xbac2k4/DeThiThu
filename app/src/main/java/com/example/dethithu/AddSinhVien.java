package com.example.dethithu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dethithu.Model.Response;
import com.example.dethithu.Model.SinhVien;
import com.example.dethithu.Service.HttpRequest;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class AddSinhVien extends AppCompatActivity {
    EditText edtTen, edtMa, edtNgaySinh, edtKhoaHoc, edtLop;
    Spinner spnGioiTinh;
    Button btn_submit;
    String strGioiTinh;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    int ngay, thang, nam;
    HttpRequest httpRequest = new HttpRequest();

    String id, ma, ten, gioitinh, ngaysinh, khoahoc, lop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sinh_vien);

        anhxa();
        getIntentMain();
        onClick();
    }
    private void anhxa() {
        edtTen = findViewById(R.id.edt_ten_sv);
        edtMa = findViewById(R.id.edt_ma_sv);
        edtNgaySinh = findViewById(R.id.edt_ngay_sinh);
        edtKhoaHoc = findViewById(R.id.edt_khoa_hoc);
        edtLop = findViewById(R.id.edt_lop);
        spnGioiTinh = findViewById(R.id.spn_gioi_tinh);
        btn_submit = findViewById(R.id.btn_submit);
    }

    private void onClick() {
        ArrayList<String> listGT = new ArrayList<>();
        listGT.add("Nam");
        listGT.add("Nữ");
        listGT.add("Khác");
        ArrayAdapter ad = new ArrayAdapter(
                AddSinhVien.this,
                android.R.layout.simple_spinner_dropdown_item,
                listGT);
        spnGioiTinh.setAdapter(ad);
        spnGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spnGioiTinh.getSelectedItemPosition() == i) {
                    strGioiTinh = listGT.get(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                nam = calendar.get(Calendar.YEAR);
                thang = calendar.get(Calendar.MONTH);
                ngay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(AddSinhVien.this, 0, dateNgayThue, nam, thang, ngay);
                d.show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _ma = edtMa.getText().toString().trim();
                String _ten = edtTen.getText().toString().trim();
                String _gioiTinh = strGioiTinh;
                String _ngaysinh = edtNgaySinh.getText().toString().trim();
                String _khoahoc = edtKhoaHoc.getText().toString().trim();
                String _lop = edtLop.getText().toString().trim();

                SinhVien sinhVien = new SinhVien(_ma, _ten, _gioiTinh, _ngaysinh, _khoahoc, _lop);

                if (getIntent().getExtras().getInt("type") == 0) {
                    Toast.makeText(AddSinhVien.this, "Cập nhật", Toast.LENGTH_SHORT).show();
                    httpRequest.callAPI().updateSinhVien(id, sinhVien).enqueue(new Callback<Response<Void>>() {
                        @Override
                        public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    Toast.makeText(AddSinhVien.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<Void>> call, Throwable t) {

                        }
                    });
                } else {
                    httpRequest.callAPI().addSinhVien(sinhVien).enqueue(new Callback<Response<SinhVien>>() {
                        @Override
                        public void onResponse(Call<Response<SinhVien>> call, retrofit2.Response<Response<SinhVien>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    Toast.makeText(AddSinhVien.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<SinhVien>> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }
    DatePickerDialog.OnDateSetListener dateNgayThue = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            ngay = dayOfMonth;
            thang = month;
            nam = year;
            GregorianCalendar gregorianCalendar = new GregorianCalendar( nam, thang, ngay);
            edtNgaySinh.setText(sdf.format(gregorianCalendar.getTime()));
        }
    };
    private void getIntentMain() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.getInt("type") == 0) {
            id = bundle.getString("id");
            ten = bundle.getString("ten");
            ma = bundle.getString("ma");
            gioitinh = bundle.getString("gioitinh");
            ngaysinh = bundle.getString("ngaysinh");
            khoahoc = bundle.getString("khoahoc");
            lop = bundle.getString("lop");

            if (gioitinh.equals("Nam")) {
                spnGioiTinh.setSelection(0);
            } else if (gioitinh.equals("Nữ")) {
                spnGioiTinh.setSelection(1);
            } else {
                spnGioiTinh.setSelection(2);
            }
            edtMa.setText(ma);
            edtTen.setText(ten);
            edtNgaySinh.setText(ngaysinh);
            edtKhoaHoc.setText(khoahoc);
            edtLop.setText(lop);
        }
    }
}