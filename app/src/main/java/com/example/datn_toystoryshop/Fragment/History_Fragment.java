package com.example.datn_toystoryshop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datn_toystoryshop.Adapter.OrderHistoryAdapter;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import com.example.datn_toystoryshop.history.ConfirmFragment;
import com.example.datn_toystoryshop.history.DeliveryFragment;
import com.example.datn_toystoryshop.history.EvaluateFragment;
import com.example.datn_toystoryshop.history.GetGoodsFragment;
import com.example.datn_toystoryshop.history.History_purchase_screen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History_Fragment extends Fragment {
    private TextView currentSelectedTextView;
    private Spinner spinnerMonth, spinnerYear;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private String documentId;
    private LinearLayout history_purchase, confirm, get_goods, delivery, evaluate;
    private TextView confirmText, getGoodsText, deliveryText, evaluateText;
    private static final float DEFAULT_TEXT_SIZE = 10f; // Kích thước mặc định (dp)
    private static final float HIGHLIGHTED_TEXT_SIZE = 12f; // Kích thước khi được chọn (dp)

    public History_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Khởi tạo Spinner
        history_purchase = view.findViewById(R.id.history_purchase);
        confirm = view.findViewById(R.id.confirm);
        get_goods = view.findViewById(R.id.get_goods);
        delivery = view.findViewById(R.id.delivery);
        evaluate = view.findViewById(R.id.evaluate);
        confirmText = view.findViewById(R.id.confirmText);
        getGoodsText = view.findViewById(R.id.getGoodsText);
        deliveryText = view.findViewById(R.id.deliveryText);
        evaluateText = view.findViewById(R.id.evaluateText);

        currentSelectedTextView = confirmText; // Mặc định chọn Confirm
        highlightTextView(confirmText); // Tô màu ban đầu

        history_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), History_purchase_screen.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        // Xử lý sự kiện nhấn
        confirm.setOnClickListener(v -> {
            replaceFragment(new ConfirmFragment());
            highlightTextView(confirmText);
        });

        get_goods.setOnClickListener(v -> {
            replaceFragment(new GetGoodsFragment());
            highlightTextView(getGoodsText);
        });

        delivery.setOnClickListener(v -> {
            replaceFragment(new DeliveryFragment());
            highlightTextView(deliveryText);
        });

        evaluate.setOnClickListener(v -> {
            replaceFragment(new EvaluateFragment());
            highlightTextView(evaluateText);
        });

//        spinnerMonth = view.findViewById(R.id.spinnerMonth);
//        spinnerYear = view.findViewById(R.id.spinnerYear);
//        setUpSpinners();
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            documentId = bundle.getString("documentId");
//            Log.e("OrderHistoryAdapter", "j66666666666666666History_Fragment" + documentId);
//
//        }
//        // Khởi tạo RecyclerView và Adapter
//        recyclerView = view.findViewById(R.id.rvOrderHistory);
//        APIService apiService = RetrofitClient.getAPIService();
//        adapter = new OrderHistoryAdapter(getContext(), filteredOrderList, apiService);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Gọi API để lấy danh sách đơn hàng
//        fetchOrders();
        // Xử lý sự kiện khi người dùng chọn tháng hoặc năm
//        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                filterOrders(); // Lọc lại danh sách khi chọn tháng
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {}
//        });
//
//        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                filterOrders(); // Lọc lại danh sách khi chọn năm
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {}
//        });

        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Nếu muốn cho phép quay lại Fragment trước
        fragmentTransaction.commit();
    }
    private void highlightTextView(TextView textView) {
        if (currentSelectedTextView != null) {
            // Reset màu và kích thước của TextView trước đó
            currentSelectedTextView.setTextColor(getResources().getColor(android.R.color.black));
            currentSelectedTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
        }

        // Đổi màu và kích thước TextView được chọn
        textView.setTextColor(getResources().getColor(R.color.highlight_color));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, HIGHLIGHTED_TEXT_SIZE);
        currentSelectedTextView = textView;
    }
//    private void setUpSpinners() {
//        // Thiết lập Adapter cho Spinner tháng
//        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.months_array, android.R.layout.simple_spinner_item);
//        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerMonth.setAdapter(monthAdapter);
//
//        // Thiết lập Adapter cho Spinner năm
//        ArrayList<String> years = new ArrayList<>();
//        for (int i = 2024; i <= 2030; i++) {
//            years.add(String.valueOf(i));
//        }
//        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_item, years);
//        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerYear.setAdapter(yearAdapter);
//    }
//
//    private void fetchOrders() {
//        String cusId = documentId;
//
//        if (cusId == null || cusId.isEmpty()) {
//            Log.e("FavoriteScreen", "cusId không được để trống");
//            return;
//        }
//        APIService apiService = RetrofitClient.getAPIService();
//        Call<List<Order_Model>> call = apiService.getOrders(cusId);
//        call.enqueue(new Callback<List<Order_Model>>() {
//            @Override
//            public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    orderList.clear();
//                    orderList.addAll(response.body());
//                    filteredOrderList.clear();
//                    filteredOrderList.addAll(orderList);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void filterOrders() {
//        String selectedMonth = spinnerMonth.getSelectedItem().toString();
//        String selectedYear = spinnerYear.getSelectedItem().toString();
//
//        // Kiểm tra nếu người dùng chưa chọn tháng (chọn "...")
//        if (selectedMonth.equals("...")) {
//            // Nếu chưa chọn tháng, không lọc mà hiển thị tất cả đơn hàng
//            filteredOrderList.clear();
//            filteredOrderList.addAll(orderList);
//        } else {
//            // Chuyển đổi tên tháng sang số (01, 02, ..., 12)
//            String monthNumber = convertMonthNameToNumber(selectedMonth);
//            filteredOrderList.clear();
//
//            for (Order_Model order : orderList) {
//                String orderDate = order.getOrderDate(); // Giả sử orderDate là chuỗi định dạng yyyy-MM-dd
//                String orderMonth = orderDate.substring(5, 7); // Lấy tháng từ chuỗi (index 5-6)
//                String orderYear = orderDate.substring(0, 4); // Lấy năm từ chuỗi (index 0-3)
//
//                // Kiểm tra nếu tháng và năm khớp
//                if (orderMonth.equals(monthNumber) && orderYear.equals(selectedYear)) {
//                    filteredOrderList.add(order);
//                }
//            }
//        }
//
//        // Cập nhật lại dữ liệu cho Adapter
//        adapter.notifyDataSetChanged();
//    }
//
//
//    // Phương thức chuyển đổi tên tháng sang số
//    private String convertMonthNameToNumber(String monthName) {
//        switch (monthName) {
//            case "Tháng 1":
//                return "01";
//            case "Tháng 2":
//                return "02";
//            case "Tháng 3":
//                return "03";
//            case "Tháng 4":
//                return "04";
//            case "Tháng 5":
//                return "05";
//            case "Tháng 6":
//                return "06";
//            case "Tháng 7":
//                return "07";
//            case "Tháng 8":
//                return "08";
//            case "Tháng 9":
//                return "09";
//            case "Tháng 10":
//                return "10";
//            case "Tháng 11":
//                return "11";
//            case "Tháng 12":
//                return "12";
//            default:
//                return "01"; // Mặc định trả về "01" nếu không hợp lệ
//        }
//    }

}
