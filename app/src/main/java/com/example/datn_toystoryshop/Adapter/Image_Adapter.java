package com.example.datn_toystoryshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.datn_toystoryshop.R;
import java.util.List;

public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ViewHolder> {

    private List<Integer> imageList;
    private List<String> textList; // Danh sách chứa các nội dung text tương ứng

    public Image_Adapter(List<Integer> imageList, List<String> texts) {
        // Kiểm tra xem imageList và texts có giá trị null không
        if (imageList == null || texts == null || imageList.size() != texts.size()) {
            throw new IllegalArgumentException("Image list and text list must not be null and must have the same size");
        }
        this.imageList = imageList;
        this.textList = texts;  // Khởi tạo danh sách text
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
