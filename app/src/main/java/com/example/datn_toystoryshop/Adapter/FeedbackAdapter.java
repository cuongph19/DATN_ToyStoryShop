package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Model.Feeback_Model;
import com.example.datn_toystoryshop.R;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private Context context;
    private List<Feeback_Model> feedbackList;

    public FeedbackAdapter(Context context, List<Feeback_Model> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feeback_Model feedback = feedbackList.get(position);

        holder.userName.setText(feedback.getCusId()); // Hoặc thay bằng tên khách hàng thực
        holder.tvContent.setText(feedback.getContent());
        holder.tvDateFeed.setText(feedback.getDateFeed().substring(0, 10)); // Chỉ lấy ngày
        holder.bindStars(feedback.getStart());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView userName, tvContent, tvDateFeed;
        ImageView[] stars;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDateFeed = itemView.findViewById(R.id.tvDateFeed);

            stars = new ImageView[]{
                    itemView.findViewById(R.id.star1),
                    itemView.findViewById(R.id.star2),
                    itemView.findViewById(R.id.star3),
                    itemView.findViewById(R.id.star4),
                    itemView.findViewById(R.id.star5)
            };
        }

        public void bindStars(double rating) {
            for (int i = 0; i < stars.length; i++) {
                if (i < rating) {
                    stars[i].setImageResource(R.drawable.ic_star1); // Đã đánh giá
                } else {
                    stars[i].setImageResource(R.drawable.ic_star1); // Chưa đánh giá
                }
            }
        }
    }
}
