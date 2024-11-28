package com.example.datn_toystoryshop.Contact_support;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.datn_toystoryshop.Adapter.Chat_Adapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.ChatHistoryResponse_Model;
import com.example.datn_toystoryshop.Model.ChatMessage_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import com.example.datn_toystoryshop.Shopping.Cart_screen;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chat_contact extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private Chat_Adapter chatAdapter;
    private List<ChatMessage_Model> chatMessageList;
    private ImageView imgBack;
    private String documentId;// ID của khách hàng
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_contact);
        documentId = getIntent().getStringExtra("documentId");
        Log.d("ContactSupport_screen", "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkfffffff " + documentId);


        // Khởi tạo các thành phần giao diện
        chatRecyclerView = findViewById(R.id.recyclerViewChat);
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);
        imgBack = findViewById(R.id.imgBack);
        chatMessageList = new ArrayList<>();
        chatAdapter = new Chat_Adapter(chatMessageList, documentId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }});
        // Gọi API để lấy lịch sử tin nhắn
        getChatHistory();
        // Tự động hiển thị tin nhắn hỗ trợ sau 2 giây
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addSupportMessage("Chào bạn, bạn cần tôi hỗ trợ gì nào?");
            }
        }, 1000); // 2000 milliseconds = 2 seconds
        // Xử lý sự kiện gửi tin nhắn
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    getChatHistory();
                } else {
                    Toast.makeText(Chat_contact.this, "Vui lòng nhập tin nhắn!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addSupportMessage(String message) {
        ChatMessage_Model supportMessage = new ChatMessage_Model(null, documentId, message, "Văn bản");
        chatMessageList.add(supportMessage);
        chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
        chatRecyclerView.scrollToPosition(chatMessageList.size() - 1);
    }
    private void getChatHistory() {
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getChatHistory(documentId, null).enqueue(new Callback<ChatHistoryResponse_Model>() {
            @Override
            public void onResponse(Call<ChatHistoryResponse_Model> call, Response<ChatHistoryResponse_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatMessageList.clear();
                    chatMessageList.addAll(response.body().getData());
                    chatAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Chat", "Lỗi khi lấy lịch sử tin nhắn: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatHistoryResponse_Model> call, Throwable t) {
                Log.e("Chat", "Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void sendMessage(String message) {
        ChatMessage_Model chatMessage = new ChatMessage_Model(documentId, null, message, "Văn bản");
        APIService apiService = RetrofitClient.getAPIService();
        apiService.sendMessage(chatMessage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    chatMessageList.add(chatMessage);
                    chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                    chatRecyclerView.scrollToPosition(chatMessageList.size() - 1);
                    messageInput.setText("");
                } else {
                    Log.e("Chat", "Lỗi khi gửi tin nhắn: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Chat", "Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

