package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.ArtStoryModel;
import com.example.datn_toystoryshop.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ArtStoryAdapter extends RecyclerView.Adapter<ArtStoryAdapter.ViewHolder> {
    private Context context;
    private List<ArtStoryModel> artStoryList;

    public ArtStoryAdapter(Context context, List<ArtStoryModel> artStoryList) {
        this.context = context;
        this.artStoryList = artStoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artstory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArtStoryModel artStory = artStoryList.get(position);
        holder.title.setText(artStory.getTitle());
        holder.author.setText(artStory.getAuthor());
        holder.description.setText(artStory.getDescription());
        holder.content.setText((artStory.getContent()));
        // Chuyển đổi ngày giờ từ MongoDB thành định dạng dễ đọc
        if (artStory.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(artStory.getDate());
            holder.date.setText(formattedDate);  // Hiển thị ngày giờ
        }

        Glide.with(context)
                .load(artStory.getImageUrl())
                .override(120, 120) // Thiết lập kích thước ảnh tải về để phù hợp với `ImageView`
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return artStoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, description, content,date;

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitle);
            author = itemView.findViewById(R.id.articleAuthor);
            description = itemView.findViewById(R.id.articleDescription);
            image = itemView.findViewById(R.id.articleImage);
            content = itemView.findViewById(R.id.articleContent);
            date = itemView.findViewById(R.id.articleDate);
        }
    }
}
