package com.example.datn_toystoryshop.Contact_support;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.R;

public class Chat_contact extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;
    private LinearLayout chatContainer;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_contact);

        // Khởi tạo các thành phần giao diện
         editTextMessage = findViewById(R.id.editTextMessage);
         buttonSend = findViewById(R.id.buttonSend);
         chatContainer = findViewById(R.id.chatContainer);
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j66666666666666666Chat_contact" + documentId);
        // Tạo phản hồi tự động sau 2 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addSupportMessage("Chào bạn, bạn cần tôi hỗ trợ gì nào?");
            }
        }, 2000); // 2000 milliseconds = 2 seconds

        // Xử lý sự kiện khi nhấn nút "Gửi"
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();

                if (!message.isEmpty()) {
                    addUserMessage(message);

                    // Hiển thị tin nhắn đã gửi bằng Toast
                    Toast.makeText(Chat_contact.this, "Bạn đã gửi: " + message, Toast.LENGTH_SHORT).show();

                    // Xóa nội dung trong EditText sau khi gửi
                    editTextMessage.setText("");

                    // Tạo phản hồi của nhân viên hỗ trợ sau khi người dùng gửi tin nhắn
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addSupportMessage("Đây là phản hồi từ nhân viên hỗ trợ.");
                        }
                    }, 2000);
                } else {
                    Toast.makeText(Chat_contact.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserMessage(String message) {
        TextView messageView = new TextView(Chat_contact.this);
        messageView.setText(message);
        messageView.setBackgroundResource(R.drawable.user_message_background);
        messageView.setPadding(16, 8, 16, 8);
        messageView.setTextColor(getResources().getColor(android.R.color.white));

        // Thiết lập khoảng cách bên trên 10dp
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 15, 0, 0);
        params.gravity = Gravity.START; // Tin nhắn người dùng nằm bên phải
        messageView.setLayoutParams(params);

        // Thêm tin nhắn vào `chatContainer`
        chatContainer.addView(messageView);
    }

    private void addSupportMessage(String message) {
        TextView messageView = new TextView(Chat_contact.this);
        messageView.setText(message);
        messageView.setBackgroundResource(R.drawable.support_message_background);
        messageView.setPadding(16, 8, 16, 8);
        messageView.setTextColor(getResources().getColor(android.R.color.black));

        // Thiết lập khoảng cách bên trên 10dp
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 15, 0, 0);
        params.gravity = Gravity.END; // Tin nhắn nhân viên hỗ trợ nằm bên trái
        messageView.setLayoutParams(params);

        // Thêm tin nhắn vào `chatContainer`
        chatContainer.addView(messageView);
    }
}
