package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.datn_toystoryshop.Adapter.AddressAdapter;
import com.example.datn_toystoryshop.Model.Address;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressList_Screen extends AppCompatActivity {
    private RecyclerView recyclerViewAddress;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    ImageView imgBack;
    LinearLayout linAdd;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra mã yêu cầu và mã kết quả
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Gọi lại API để tải lại danh sách địa chỉ sau khi thêm mới
            getAddressesFromAPI();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list_screen);  // Đảm bảo layout này là layout của bạn

        imgBack = findViewById(R.id.imgBack);
        linAdd = findViewById(R.id.bottomAddAddress);


        // Khởi tạo RecyclerView
        recyclerViewAddress = findViewById(R.id.recyclerViewAddress);
        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this));  // Sử dụng LinearLayoutManager cho RecyclerView

        // Gọi API để lấy danh sách địa chỉ
        getAddressesFromAPI();

        imgBack.setOnClickListener(v -> {
            Address selectedAddress = addressAdapter.getSelectedAddress();
            if (selectedAddress != null) {
                Intent intent = new Intent(AddressList_Screen.this, Order_screen.class);
                intent.putExtra("selectedAddressId", selectedAddress.get_id());
                intent.putExtra("selectedAddressName", selectedAddress.getName());
                intent.putExtra("selectedAddressPhone", selectedAddress.getPhone());
                intent.putExtra("selectedAddress", selectedAddress.getAddress());
                intent.putExtra("selectedAddressDetail", selectedAddress.getAddressDetail());
                startActivity(intent);
            } else {
                // Xử lý trường hợp không có địa chỉ nào được chọn
                Log.e("Address Error", "No address selected!");
            }
        });


        linAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng startActivityForResult() thay vì startActivity()
                Intent intent = new Intent(AddressList_Screen.this, Add_address_screen.class);
                startActivityForResult(intent, 1);  // Request code 1
            }
        });

    }

    private void getAddressesFromAPI() {
        // Khởi tạo Retrofit client
        APIService apiService = RetrofitClient.getAPIService();

        // Gọi API để lấy danh sách địa chỉ
        Call<List<Address>> call = apiService.getAllAddresses();

        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.isSuccessful()) {
                    // Nhận dữ liệu từ API
                    addressList = response.body();
                    // Khởi tạo và gắn Adapter cho RecyclerView
                    addressAdapter = new AddressAdapter(addressList);
                    recyclerViewAddress.setAdapter(addressAdapter);
                } else {
                    // Nếu API trả về lỗi
                    Log.e("API Error", "Lỗi khi nhận dữ liệu từ API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                // Nếu có lỗi trong quá trình gọi API
                Log.e("API Error", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
