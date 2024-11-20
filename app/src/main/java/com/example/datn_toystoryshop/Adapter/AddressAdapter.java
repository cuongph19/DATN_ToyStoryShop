package com.example.datn_toystoryshop.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Model.Address;
import com.example.datn_toystoryshop.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addressList;

    public AddressAdapter(List<Address> addressList) {
        this.addressList = (addressList != null) ? addressList : new ArrayList<>();
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

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.address_name);
            phoneTextView = itemView.findViewById(R.id.address_phone);
            addressTextView = itemView.findViewById(R.id.address_basic);
            addressDetailTextView = itemView.findViewById(R.id.address_detail);
        }
    }
}

