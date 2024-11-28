package com.example.datn_toystoryshop.history;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.datn_toystoryshop.Adapter.FeedbackAdapter;
import com.example.datn_toystoryshop.Model.Feeback_Model;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluateFragment extends Fragment {

    private Spinner spinnerMonth, spinnerYear;
    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<Feeback_Model> feedbackList = new ArrayList<>();
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluate, container, false);
        sharedPreferences = requireContext().getSharedPreferences("Settings", requireContext().MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        recyclerView = view.findViewById(R.id.rvOrderHistory);

        apiService = RetrofitClient.getAPIService();
        setUpSpinners();

        // Khởi tạo RecyclerView và Adapter cho feedback
        feedbackAdapter = new FeedbackAdapter(getContext(), feedbackList);
        recyclerView.setAdapter(feedbackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy feedback
        getFeedbackData();

        // Xử lý sự kiện khi người dùng chọn tháng hoặc năm
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterOrders(); // Lọc lại danh sách khi chọn tháng
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterOrders(); // Lọc lại danh sách khi chọn năm
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        return view;
    }

    private void setUpSpinners() {
        // Thiết lập Adapter cho Spinner tháng
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.months_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        // Thiết lập Adapter cho Spinner năm
        ArrayList<String> years = new ArrayList<>();
        for (int i = 2024; i <= 2030; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    // Hàm gọi API để lấy feedback
    private void getFeedbackData() {
        apiService.getFeedbacks().enqueue(new Callback<List<Feeback_Model>>() {
            @Override
            public void onResponse(Call<List<Feeback_Model>> call, Response<List<Feeback_Model>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("EvaluateFragment", "Feedbacks received successfully");
                        Log.d("EvaluateFragment", "Feedback List size: " + response.body().size());

                        feedbackList.clear();
                        feedbackList.addAll(response.body());
                        feedbackAdapter.notifyDataSetChanged();

                        // Log nội dung của feedback
                        for (Feeback_Model feedback : feedbackList) {
                            Log.d("EvaluateFragment", "Feedback: " + feedback.getContent());
                        }
                    } else {
                        Log.e("EvaluateFragment", "Response body is empty");
                    }
                } else {
                    Log.e("EvaluateFragment", "Failed to load feedback. Response code: " + response.code());
                    Log.e("EvaluateFragment", "Error message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Feeback_Model>> call, Throwable t) {
                Log.e("EvaluateFragment", "Error: " + t.getMessage());
                // Kiểm tra thêm chi tiết lỗi nếu có
                t.printStackTrace();
            }
        });
    }




    // Hàm lọc đơn hàng theo tháng và năm
    private void filterOrders() {
        String selectedMonth = spinnerMonth.getSelectedItem().toString();
        String selectedYear = spinnerYear.getSelectedItem().toString();

        // Kiểm tra nếu người dùng chưa chọn tháng (chọn "...")
        if (selectedMonth.equals("...")) {
            // Nếu chưa chọn tháng, không lọc mà hiển thị tất cả đơn hàng
            filteredOrderList.clear();
            filteredOrderList.addAll(orderList);
        } else {
            // Chuyển đổi tên tháng sang số (01, 02, ..., 12)
            String monthNumber = convertMonthNameToNumber(selectedMonth);
            filteredOrderList.clear();

            for (Order_Model order : orderList) {
                String orderDate = order.getOrderDate(); // Giả sử orderDate là chuỗi định dạng yyyy-MM-dd
                String orderMonth = orderDate.substring(5, 7); // Lấy tháng từ chuỗi (index 5-6)
                String orderYear = orderDate.substring(0, 4); // Lấy năm từ chuỗi (index 0-3)

                // Kiểm tra nếu tháng và năm khớp
                if (orderMonth.equals(monthNumber) && orderYear.equals(selectedYear)) {
                    filteredOrderList.add(order);
                }
            }
        }

        // Cập nhật lại dữ liệu cho Adapter
        feedbackAdapter.notifyDataSetChanged();
    }

    // Phương thức chuyển đổi tên tháng sang số
    private String convertMonthNameToNumber(String monthName) {
        switch (monthName) {
            case "Tháng 1":
                return "01";
            case "Tháng 2":
                return "02";
            case "Tháng 3":
                return "03";
            case "Tháng 4":
                return "04";
            case "Tháng 5":
                return "05";
            case "Tháng 6":
                return "06";
            case "Tháng 7":
                return "07";
            case "Tháng 8":
                return "08";
            case "Tháng 9":
                return "09";
            case "Tháng 10":
                return "10";
            case "Tháng 11":
                return "11";
            case "Tháng 12":
                return "12";
            default:
                return "01"; // Mặc định trả về "01" nếu không hợp lệ
        }
    }
}
