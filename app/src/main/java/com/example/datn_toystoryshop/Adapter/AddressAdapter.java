package com.example.datn_toystoryshop.Adapter;

import android.app.Dialog;
import android.content.Context;
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

import com.example.datn_toystoryshop.Model.Address_model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private OnAddressUpdatedListener listener;
    private List<Address_model> addressModelList;
    private int selectedPosition = -1;

    public void setOnAddressUpdatedListener(OnAddressUpdatedListener listener) {
        this.listener = listener;
    }

    public AddressAdapter(List<Address_model> addressModelList) {
        this.addressModelList = (addressModelList != null) ? addressModelList : new ArrayList<>();
        for (int i = 0; i < addressModelList.size(); i++) {
            if (addressModelList.get(i).isDefault()) {
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
        Address_model addressModel = addressModelList.get(position);

        // Set dữ liệu cho các trường cần thiết
        holder.nameTextView.setText(addressModel.getName() != null ? addressModel.getName() : "Chưa có tên");
        holder.phoneTextView.setText(addressModel.getPhone() != null ? addressModel.getPhone() : "Chưa có số điện thoại");
        holder.addressTextView.setText(addressModel.getAddress() != null ? addressModel.getAddress() : "Chưa có địa chỉ");
        holder.addressDetailTextView.setText(addressModel.getAddressDetail() != null ? addressModel.getAddressDetail() : "Chưa có chi tiết địa chỉ");
        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            updateSelectedPosition(holder.getAdapterPosition());
        });

        holder.radioButton.setOnClickListener(v -> {
            updateSelectedPosition(holder.getAdapterPosition());

        });

        holder.itemView.setOnLongClickListener(v -> {
            // Sử dụng đúng context từ itemView
            showEditDialog(holder.itemView.getContext(), addressModel);
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
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
        Address_model selectedAddressModel = getSelectedAddress();
        if (selectedAddressModel != null && listener != null) {
            listener.onAddressSelected(selectedAddressModel); // Gửi địa chỉ được chọn
        }
    }

    public Address_model getSelectedAddress() {
        if (selectedPosition >= 0 && selectedPosition < addressModelList.size()) {
            return addressModelList.get(selectedPosition);
        }
        return null;
    }

    private void showEditDialog(Context context, Address_model addressModel) {
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
        etName.setText(addressModel.getName());
        etPhoneNumber.setText(addressModel.getPhone());
        etAddress.setText(addressModel.getAddress());
        etAddressDetail.setText(addressModel.getAddressDetail());

        // Khi nhấn nút quay lại, đóng dialog
        btnBack.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các EditText
                String name = etName.getText().toString().trim();
                String phone = etPhoneNumber.getText().toString().trim();
                String addressText = etAddress.getText().toString().trim();
                String addressDetail = etAddressDetail.getText().toString().trim();

                // Kiểm tra dữ liệu hợp lệ (bạn có thể thêm logic kiểm tra như rỗng, không hợp lệ...)
                if (name.isEmpty() || phone.isEmpty() || addressText.isEmpty() || addressDetail.isEmpty()) {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cập nhật đối tượng Address (không phải String)
                addressModel.setName(name);  // Gọi setName trên đối tượng Address
                addressModel.setPhone(phone);  // Gọi setPhone trên đối tượng Address
                addressModel.setAddress(addressText);  // Gọi setAddress trên đối tượng Address
                addressModel.setAddressDetail(addressDetail);  // Gọi setAddressDetail trên đối tượng Address

                // Gọi API PUT để cập nhật
                updateAddress(dialog.getContext(), addressModel);  // Gọi phương thức updateAddress
                dialog.dismiss(); // Đóng dialog sau khi lưu
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;  // Chiều rộng full màn hình
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  // Chiều cao tự động theo nội dung
        dialog.getWindow().setAttributes(layoutParams);
        // Hiển thị dialog
        dialog.show();
    }

    private void updateAddress(Context context, Address_model addressModel) {
        // Lấy APIService từ Retrofit
        APIService apiService = RetrofitClient.getAPIService();

        // Gọi API PUT (Cập nhật địa chỉ)
        Call<Address_model> call = apiService.updateAddress(addressModel.get_id(), addressModel);

        // Thực hiện yêu cầu và xử lý phản hồi
        call.enqueue(new Callback<Address_model>() {
            @Override
            public void onResponse(Call<Address_model> call, Response<Address_model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Address_model updatedAddressModel = response.body();

                    // Nếu ID trả về là null, sử dụng ID gốc
                    String addressId = updatedAddressModel.get_id() != null ? updatedAddressModel.get_id() : addressModel.get_id();

                    int position = getAddressPositionById(addressId);
                    if (position != -1) {
                        addressModelList.set(position, updatedAddressModel);  // Cập nhật địa chỉ trong list
                        notifyDataSetChanged();  // Cập nhật item trong RecyclerView
                        Toast.makeText(context, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();

                        // Gọi lại listener để thông báo đã cập nhật
                        if (listener != null) {
                            listener.onAddressUpdated();  // Gọi phương thức cập nhật
                        }
                    } else {
                        Toast.makeText(context, "Không tìm thấy địa chỉ trong danh sách", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Address_model> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getAddressPositionById(String id) {
        if (id == null) {
            return -1;  // Nếu id là null, không thể so sánh, trả về -1
        }
        for (int i = 0; i < addressModelList.size(); i++) {
            Log.d("AddressAdapter", "Checking ID: " + addressModelList.get(i).get_id());  // In ra các id trong danh sách
            if (addressModelList.get(i).get_id() != null && addressModelList.get(i).get_id().equals(id)) {
                return i;  // Trả về vị trí nếu tìm thấy
            }
        }
        return -1;  // Không tìm thấy
    }

    public interface OnAddressUpdatedListener {
        void onAddressUpdated(); // Cập nhật địa chỉ
        void onAddressSelected(Address_model selectedAddressModel); // Khi địa chỉ được chọn
    }


}

