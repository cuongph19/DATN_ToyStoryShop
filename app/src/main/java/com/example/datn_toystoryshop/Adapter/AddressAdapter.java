package com.example.datn_toystoryshop.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Model.Address;
import com.example.datn_toystoryshop.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
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

}

