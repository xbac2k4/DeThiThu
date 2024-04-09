package com.example.dethithu.Service;

import com.example.dethithu.Model.Response;
import com.example.dethithu.Model.SinhVien;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String ipv4 = "192.168.1.118";
    String DOMAIN = "http://"+ ipv4 +":3000/api/";

    @GET("get-list-sinhvien")
    Call<Response<ArrayList<SinhVien>>> getSinhVien();
    @GET("search-sinhvien")
    Call<Response<ArrayList<SinhVien>>> searchSinhVien(@Query("ten_ph44315") String ten);
    @DELETE("delete-sinhvien/{id}")
    Call<Response<Void>> deleteSinhVien(@Path("id") String id);
    @POST("add-sinhvien")
    Call<Response<SinhVien>> addSinhVien(@Body SinhVien sinhVien);
    @PUT("update-sinhvien/{id}")
    Call<Response<Void>> updateSinhVien(@Path("id") String id, @Body SinhVien sinhVien);

}
