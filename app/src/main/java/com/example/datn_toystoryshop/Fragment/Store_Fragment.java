package com.example.datn_toystoryshop.Fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.datn_toystoryshop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Store_Fragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap myMap;
    private Spinner spinnerCity, spinnerDistrict;
    private ArrayAdapter<CharSequence> districtAdapter;
    private EditText searchLocation;

    // Vị trí của các cửa hàng
    private static final LatLng HANOI_STORE_1 = new LatLng(21.0386, 105.7477); // Tọa độ cửa hàng 1
    private static final LatLng HANOI_STORE_2 = new LatLng(20.9395, 105.9754); // Tọa độ cửa hàng 2
    private static final LatLng SAIGON_STORE = new LatLng(10.8231, 106.6297); // Tọa độ cửa hàng Saigon

    public Store_Fragment() {
        // Required empty public constructor
    }

    public static Store_Fragment newInstance(String param1, String param2) {
        Store_Fragment fragment = new Store_Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Thêm Listener để chặn cuộn khi tương tác với mapFragment
        view.findViewById(R.id.mapFragment).setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        // Khởi tạo Spinner cho thành phố và quận/huyện
        spinnerCity = view.findViewById(R.id.spinnerCity);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        searchLocation = view.findViewById(R.id.searchLocation);

        // Thiết lập Adapter cho Spinner thành phố
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.city_list,
                android.R.layout.simple_spinner_item
        );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        // Lắng nghe sự kiện chọn thành phố
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LatLng cityLocation = null;
                switch (position) {
                    case 0: // Hà Nội
                        cityLocation = new LatLng(21.0285, 105.8542); // Tọa độ Hà Nội
                        break;
                    case 1: // Hà Nam
                        cityLocation = new LatLng(20.5835, 105.92299); // Tọa độ Hà Nam
                        break;
                    case 2: // TP. Hồ Chí Minh
                        cityLocation = new LatLng(10.8231, 106.6297); // Tọa độ TP. Hồ Chí Minh
                        break;
                }

                if (cityLocation != null) {
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 12.0f));
                    showNearestStore(cityLocation);
                }
                if (position == 0) { // Nếu chọn Hà Nội
                    districtAdapter = ArrayAdapter.createFromResource(
                            getContext(),
                            R.array.district_list_hanoi,
                            android.R.layout.simple_spinner_item
                    );
                } else if (position == 1) { // Nếu chọn Hà Nam
                    districtAdapter = ArrayAdapter.createFromResource(
                            getContext(),
                            R.array.district_list_hanam,
                            android.R.layout.simple_spinner_item
                    );
                } else if (position == 2) { // Nếu chọn TP. Hồ Chí Minh
                    districtAdapter = ArrayAdapter.createFromResource(
                            getContext(),
                            R.array.district_list_saigon,
                            android.R.layout.simple_spinner_item
                    );
                }
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrict.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý
            }
        });

        // Thêm TextWatcher cho ô tìm kiếm
        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = searchLocation.getText().toString();
                if (!address.isEmpty()) {
                    geocodeAddress(address);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Thiết lập vị trí mặc định cho bản đồ
        LatLng defaultLocation = new LatLng(21.038572831040383, 105.74770566103412); // Tọa độ mẫu
        float zoomLevel = 15.0f; // Giá trị zoom
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, zoomLevel));

        // Thiết lập InfoWindowAdapter tùy chỉnh
        myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Không cần xử lý, dùng getInfoContents
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate layout tùy chỉnh cho InfoWindow
                View infoWindow = LayoutInflater.from(getContext()).inflate(R.layout.custom_info_window, null);

                // Thiết lập các thành phần trong layout
                ImageView icon = infoWindow.findViewById(R.id.info_window_icon);

                icon.setImageResource(R.drawable.ic_logo); // Đặt icon của cửa hàng (bạn thay bằng hình ảnh cửa hàng)

                return infoWindow;
            }
        });
        // Lắng nghe sự kiện nhấp vào InfoWindow
        myMap.setOnInfoWindowClickListener(marker -> {
            // Lấy vị trí của marker đã chọn
            LatLng destination = marker.getPosition();

            // Tạo một Intent để mở Google Maps với điểm đến là vị trí của marker
            String uri = "http://maps.google.com/maps?daddr=" + destination.latitude + "," + destination.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            // Kiểm tra xem thiết bị có ứng dụng Google Maps không
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }


    // Phương thức để xử lý geocoding và cập nhật Spinner
    private void geocodeAddress(String address) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                updateSpinnerLocation(location); // Cập nhật Spinner
                showNearestStore(userLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Phương thức để cập nhật Spinner theo địa chỉ
    private void updateSpinnerLocation(Address address) {
        String city = address.getAdminArea(); // Lấy tên thành phố
        String district = address.getSubAdminArea(); // Lấy tên quận/huyện

        // Đồng bộ Spinner thành phố dựa vào tên thành phố
        if (city != null) {
            if (city.equalsIgnoreCase("Hà Nội")) {
                spinnerCity.setSelection(0);
                setDistrictSpinner(R.array.district_list_hanoi, district);
            } else if (city.equalsIgnoreCase("Hà Nam")) {
                spinnerCity.setSelection(1);
                setDistrictSpinner(R.array.district_list_hanam, district);
            } else if (city.equalsIgnoreCase("Hồ Chí Minh")) {
                spinnerCity.setSelection(2);
                setDistrictSpinner(R.array.district_list_saigon, district);
            }
        }
    }

    // Phương thức để chọn quận/huyện trong Spinner quận
    private void setDistrictSpinner(int districtArrayResId, String district) {
        districtAdapter = ArrayAdapter.createFromResource(
                getContext(),
                districtArrayResId,
                android.R.layout.simple_spinner_item
        );
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        // Tìm và chọn quận/huyện trong Spinner
        if (district != null) {
            int position = districtAdapter.getPosition(district);
            if (position >= 0) {
                spinnerDistrict.setSelection(position);
            }
        }
    }


    // Phương thức tính toán và hiển thị cửa hàng gần nhất
    private void showNearestStore(LatLng userLocation) {
        LatLng nearestStore = findNearestStore(userLocation);
        if (nearestStore != null) {
            myMap.addMarker(new MarkerOptions()
                    .position(nearestStore)
//                    .title("525 Quang Trung, P.10, Quận Gò Vấp") // Tên cửa hàng
//                    .snippet("525 Quang Trung, P.10, Quận Gò Vấp") // Địa chỉ chi tiết
            ).showInfoWindow();
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nearestStore, 14.0f));
        }
    }

    // Tính toán khoảng cách và tìm cửa hàng gần nhất
    private LatLng findNearestStore(LatLng userLocation) {
        LatLng[] stores = {HANOI_STORE_1, HANOI_STORE_2, SAIGON_STORE};
        LatLng nearestStore = null;
        double shortestDistance = Double.MAX_VALUE;
        for (LatLng store : stores) {
            double distance = calculateDistance(userLocation, store);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestStore = store;
            }
        }
        return nearestStore;
    }

    // Công thức Haversine để tính khoảng cách giữa hai điểm LatLng
    private double calculateDistance(LatLng point1, LatLng point2) {
        double earthRadius = 6371; // tính bằng km
        double dLat = Math.toRadians(point2.latitude - point1.latitude);
        double dLng = Math.toRadians(point2.longitude - point1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(point1.latitude)) * Math.cos(Math.toRadians(point2.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
