package com.example.datn_toystoryshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Integer> imageList;
    private List<String> textList; // Danh sách chứa các nội dung text tương ứng

    public ImageAdapter(List<Integer> imageList, List<String> textList) {
        this.imageList = imageList;
        this.textList = textList;  // Khởi tạo danh sách text
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Hiển thị ảnh
        holder.imageView.setImageResource(imageList.get(position));
        // Hiển thị nội dung text tương ứng
        holder.textView.setText(textList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;  // TextView để hiển thị nội dung

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);  // Liên kết với TextView
        }
    }
}
