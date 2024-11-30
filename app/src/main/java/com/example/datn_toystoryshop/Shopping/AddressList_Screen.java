package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

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
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        imgBack = findViewById(R.id.imgBack);
        linAdd = findViewById(R.id.bottomAddAddress);

        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }
        // Khởi tạo RecyclerView
        recyclerViewAddress = findViewById(R.id.recyclerViewAddress);

        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this));  // Sử dụng LinearLayoutManager cho RecyclerView

        // Gọi API để lấy danh sách địa chỉ
        getAddressesFromAPI();

        imgBack.setOnClickListener(v -> onBackPressed());


        linAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressList_Screen.this, Add_address_screen.class);
                startActivityForResult(intent, 1);  // Request code 1
            }
        });


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Không hỗ trợ kéo thả
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Address address = addressList.get(position);

                // Hiển thị dialog xác nhận xóa
                showDeleteConfirmationDialog(address, position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewAddress);
    }

    private void getAddressesFromAPI() {
        APIService apiService = RetrofitClient.getAPIService();

        // Gọi API để lấy danh sách địa chỉ
        Call<List<Address>> call = apiService.getAllAddresses();
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.isSuccessful()) {
                    addressList = response.body();
                    addressAdapter = new AddressAdapter(addressList);

                    // Đăng ký listener để nhận thông báo khi địa chỉ được cập nhật
                    addressAdapter.setOnAddressUpdatedListener(new AddressAdapter.OnAddressUpdatedListener() {
                        @Override
                        public void onAddressUpdated() {
                            // Tải lại dữ liệu sau khi địa chỉ được cập nhật
                            getAddressesFromAPI();  // Hoặc bạn có thể gọi lại API nếu cần
                        }
                        @Override
                        public void onAddressSelected(Address selectedAddress) {
                            // Khi địa chỉ được chọn, chuẩn bị dữ liệu để gửi về
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("selectedAddressId", selectedAddress.get_id());
                            resultIntent.putExtra("selectedAddressName", selectedAddress.getName());
                            resultIntent.putExtra("selectedAddressPhone", selectedAddress.getPhone());
                            resultIntent.putExtra("selectedAddress", selectedAddress.getAddress());
                            resultIntent.putExtra("selectedAddressDetail", selectedAddress.getAddressDetail());
                            setResult(RESULT_OK, resultIntent);
                            finish(); // Kết thúc màn hình hiện tại và trả kết quả
                        }
                    });

                    recyclerViewAddress.setAdapter(addressAdapter);
                } else {
                    Log.e("API Error", "Lỗi khi nhận dữ liệu từ API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                Log.e("API Error", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showDeleteConfirmationDialog(Address address, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi API xóa địa chỉ
            deleteAddress(address, position);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> {
            addressAdapter.notifyItemChanged(position); // Hủy hành động swipe, phục hồi item
        });

        builder.show();
    }

    private void deleteAddress(Address address, int position) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<Void> call = apiService.deleteAddress(address.get_id());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa item trong danh sách và cập nhật RecyclerView
                    addressList.remove(position);
                    addressAdapter.notifyItemRemoved(position);
                    Toast.makeText(AddressList_Screen.this, "Địa chỉ đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddressList_Screen.this, "Lỗi khi xóa địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddressList_Screen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
