package com.example.datn_toystoryshop.Server;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.datn_toystoryshop.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    private WebSocket webSocket;

    private NotificationManager notificationManager;
    private Context context;

    public WebSocketClient(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;
    }

    public void start() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.16.101:8080").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.d(TAG, "WebSocket connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "WebSocket11111111111 Received: " + text);
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    String orderId = jsonObject.getString("_id");
                    String orderStatus = jsonObject.getString("orderStatus");

                    // Gửi thông báo cho người dùng khi trạng thái đơn hàng thay đổi
                    sendNotification(orderId, orderStatus);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON", e);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.e(TAG, "Error: " + t.getMessage());
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket closed");
            }
        });
    }

    private void sendNotification(String orderId, String orderStatus) {
        // Tạo thông báo cho người dùng khi có thay đổi trong trạng thái đơn hàng
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "home_notification_channel")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("ToyStory Shop thông báo!")
                .setContentText("Đơn hàng của bạn " + orderId + " hiện đang " + orderStatus)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    public void stop() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }

}
