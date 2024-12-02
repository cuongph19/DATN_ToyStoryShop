package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Model.Address;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_address_screen extends AppCompatActivity {
    EditText etName, etPhoneNumber, etAddress, etAddressDetail;
    CheckBox cbDefault;
    Button btnSave;
    private ImageView imgBack;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_screen);

        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etAddressDetail = findViewById(R.id.etAddressDetail);
        cbDefault = findViewById(R.id.cbDefault);
        btnSave = findViewById(R.id.btnSave);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        imgBack = findViewById(R.id.btnBack);
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_address_screen.this, AddressList_Screen.class);
                startActivity(intent);
                finish();
            }
        });
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String addressDetail = etAddressDetail.getText().toString().trim();
            boolean isDefault = cbDefault.isChecked();

            // Kiểm tra các trường nhập liệu
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || addressDetail.isEmpty()) {
                Toast.makeText(Add_address_screen.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Gọi API để thêm địa chỉ
                addAddressToServer(name, phone, address, addressDetail, isDefault);
            }
        });
    }

    private void addAddressToServer(String name, String phone, String address, String addressDetail, boolean isDefault) {
        // Khởi tạo Retrofit client
        APIService apiService = RetrofitClient.getAPIService();

        // Tạo đối tượng địa chỉ để gửi đi
        Address newAddress = new Address(name, phone, address, addressDetail, isDefault);

        // Gọi API POST để thêm địa chỉ
        Call<Address> call = apiService.addAddress(newAddress);

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.isSuccessful()) {
                    // Nếu thành công, trả kết quả về màn hình trước
                    setResult(RESULT_OK);  // Trả kết quả thành công
                    finish();  // Quay lại màn hình AddressList_Screen
                } else {
                    // Nếu thất bại, thông báo lỗi
                    Toast.makeText(Add_address_screen.this, "Có lỗi xảy ra khi thêm địa chỉ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(Add_address_screen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}