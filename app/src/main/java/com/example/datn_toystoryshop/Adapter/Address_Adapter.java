package com.example.datn_toystoryshop.Adapter;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.R;

import java.util.List;

public class Address_Adapter extends RecyclerView.Adapter<Address_Adapter.AddressViewHolder> {
    private List<Address> addressList;
    private Context context;

    public Address_Adapter(List<Address> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
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
//        holder.addressName.setText(address.getName());
//        holder.addressDetail.setText(address.getDetail());

        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, AddressDetailActivity.class);
//            intent.putExtra("address_name", address.getName());
//            intent.putExtra("address_detail", address.getDetail());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        //TextView addressName, addressDetail;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
          //  addressName = itemView.findViewById(R.id.address_name);
           // addressDetail = itemView.findViewById(R.id.address_detail);
        }
    }
}