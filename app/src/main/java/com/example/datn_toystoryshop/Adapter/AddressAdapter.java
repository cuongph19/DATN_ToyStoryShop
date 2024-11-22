package com.example.datn_toystoryshop.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Model.Address;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
//    private Context context;
    private List<Address> addressList;
    private int selectedPosition = -1;
    public AddressAdapter(List<Address> addressList) {
        this.addressList = (addressList != null) ? addressList : new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).isDefault()) {
                selectedPosition = i;  // Đặt vị trí của địa chỉ mặc định
                break;
            }
        }
    }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);

        // Set dữ liệu cho các trường cần thiết
        holder.nameTextView.setText(address.getName() != null ? address.getName() : "Chưa có tên");
        holder.phoneTextView.setText(address.getPhone() != null ? address.getPhone() : "Chưa có số điện thoại");
        holder.addressTextView.setText(address.getAddress() != null ? address.getAddress() : "Chưa có địa chỉ");
        holder.addressDetailTextView.setText(address.getAddressDetail() != null ? address.getAddressDetail() : "Chưa có chi tiết địa chỉ");
        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            updateSelectedPosition(holder.getAdapterPosition());
        });

        holder.radioButton.setOnClickListener(v -> {
            updateSelectedPosition(holder.getAdapterPosition());
        });

        holder.itemView.setOnLongClickListener(v -> {
            // Sử dụng đúng context từ itemView
            showEditDialog(holder.itemView.getContext(), address);
            return true;
        });


        // Nếu có trường isDefault, có thể thêm dấu sao hoặc thay đổi màu sắc
//        if (address.isDefault()) {
//            holder.nameTextView.setTextColor(Color.RED); // Ví dụ thay đổi màu chữ cho địa chỉ mặc định
//        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView, addressTextView, addressDetailTextView;
        RadioButton radioButton;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.address_name);
            phoneTextView = itemView.findViewById(R.id.address_phone);
            addressTextView = itemView.findViewById(R.id.address_basic);
            addressDetailTextView = itemView.findViewById(R.id.address_detail);
            radioButton = itemView.findViewById(R.id.address_selected);
        }
    }

    private void updateSelectedPosition(int newPosition) {
        int previousPosition = selectedPosition;
        selectedPosition = newPosition;

        // Chỉ cập nhật hai item thay đổi, không cần refresh toàn bộ
        notifyItemChanged(previousPosition);
        notifyItemChanged(selectedPosition);

    }
    public Address getSelectedAddress() {
        if (selectedPosition >= 0 && selectedPosition < addressList.size()) {
            return addressList.get(selectedPosition);
        }
        return null;
    }
    private void showEditDialog(Context context, Address address) {
        // Kiểm tra context
        if (context == null) {
            Log.e("AddressAdapter", "Context is null");
            return; // Nếu context là null, không tiếp tục tạo Dialog
        }

        // Tạo một Dialog mới
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_address_update);  // Layout của dialog mà bạn cung cấp
        dialog.setCancelable(false);  // Cho phép người dùng thoát khi nhấn ngoài

        // Ánh xạ các views trong dialog
        EditText etName = dialog.findViewById(R.id.etName);
        EditText etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);
        EditText etAddress = dialog.findViewById(R.id.etAddress);
        EditText etAddressDetail = dialog.findViewById(R.id.etAddressDetail);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageView btnBack = dialog.findViewById(R.id.btnBack);

        // Điền thông tin vào các trường trong dialog từ đối tượng address
        etName.setText(address.getName());
        etPhoneNumber.setText(address.getPhone());
        etAddress.setText(address.getAddress());
        etAddressDetail.setText(address.getAddressDetail());

        // Khi nhấn nút quay lại, đóng dialog
        btnBack.setOnClickListener(v -> dialog.dismiss());

        // Khi nhấn nút Lưu, cập nhật địa chỉ
//        btnSave.setOnClickListener(v -> {
//            // Lấy dữ liệu từ các trường
//            String name = etName.getText().toString();
//            String phone = etPhoneNumber.getText().toString();
//            String newAddress = etAddress.getText().toString();
//            String addressDetail = etAddressDetail.getText().toString();
//
//            APIService apiService = RetrofitClient.getAPIService();  // Sử dụng phương thức getAPIService của RetrofitClient
//            Call<Address> call = apiService.updateAddress(address.get_id(), name, phone, newAddress, addressDetail);
//            call.enqueue(new Callback<Address>() {
//                @Override
//                public void onResponse(Call<Address> call, Response<Address> response) {
//                    if (response.isSuccessful()) {
//                        addressList.set(addressList.indexOf(address), response.body());
//                        notifyDataSetChanged();
//                        dialog.dismiss();
//                    } else {
//                        try {
//                            String error = response.errorBody().string();
//                            Log.e("API Error", error);  // In ra lỗi để kiểm tra chi tiết
//                            Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Address> call, Throwable t) {
//                    // Xử lý lỗi khi gọi API thất bại
//                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;  // Chiều rộng full màn hình
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  // Chiều cao tự động theo nội dung
        dialog.getWindow().setAttributes(layoutParams);
        // Hiển thị dialog
        dialog.show();
    }



}

