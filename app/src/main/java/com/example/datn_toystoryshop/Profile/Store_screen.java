package com.example.datn_toystoryshop.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Store_screen extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private Spinner spinnerCity, spinnerDistrict;
    private ArrayAdapter<CharSequence> districtAdapter;
    private EditText searchLocation;
    private ImageView btnBack;
    // Vị trí của các cửa hàng
    private static final LatLng HANOI_STORE_1 = new LatLng(21.0386, 105.7477);
    private static final LatLng HANOI_STORE_2 = new LatLng(20.9395, 105.9754);
    private static final LatLng SAIGON_STORE = new LatLng(10.8231, 106.6297);
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_screen);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Chặn cuộn khi tương tác với mapFragment
        findViewById(R.id.mapFragment).setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        // Khởi tạo Spinner cho thành phố và quận/huyện
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        searchLocation = findViewById(R.id.searchLocation);
        btnBack = findViewById(R.id.btnBack);
        if (nightMode) {
            btnBack.setImageResource(R.drawable.back_icon);
        } else {
            btnBack.setImageResource(R.drawable.back_icon_1);
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Thiết lập Adapter cho Spinner thành phố
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                this,
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
                    case 0:
                        cityLocation = new LatLng(21.0285, 105.8542); // Hà Nội
                        break;
                    case 1:
                        cityLocation = new LatLng(20.5835, 105.92299); // Hà Nam
                        break;
                    case 2:
                        cityLocation = new LatLng(10.8231, 106.6297); // TP. Hồ Chí Minh
                        break;
                }

                if (cityLocation != null) {
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 12.0f));
                    showNearestStore(cityLocation);
                }

                if (position == 0) {
                    districtAdapter = ArrayAdapter.createFromResource(
                            Store_screen.this,
                            R.array.district_list_hanoi,
                            android.R.layout.simple_spinner_item
                    );
                } else if (position == 1) {
                    districtAdapter = ArrayAdapter.createFromResource(
                            Store_screen.this,
                            R.array.district_list_hanam,
                            android.R.layout.simple_spinner_item
                    );
                } else if (position == 2) {
                    districtAdapter = ArrayAdapter.createFromResource(
                            Store_screen.this,
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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng defaultLocation = new LatLng(21.038572831040383, 105.74770566103412);
        float zoomLevel = 15.0f;
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, zoomLevel));

        myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = LayoutInflater.from(Store_screen.this).inflate(R.layout.custom_info_window, null);
                ImageView icon = infoWindow.findViewById(R.id.info_window_icon);
                icon.setImageResource(R.drawable.ic_logo);
                return infoWindow;
            }
        });

        myMap.setOnInfoWindowClickListener(marker -> {
            LatLng destination = marker.getPosition();
            String uri = "http://maps.google.com/maps?daddr=" + destination.latitude + "," + destination.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }

    private void geocodeAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                updateSpinnerLocation(location);
                showNearestStore(userLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSpinnerLocation(Address address) {
        String city = address.getAdminArea();
        String district = address.getSubAdminArea();

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

    private void setDistrictSpinner(int districtArrayResId, String district) {
        districtAdapter = ArrayAdapter.createFromResource(
                this,
                districtArrayResId,
                android.R.layout.simple_spinner_item
        );
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        if (district != null) {
            int position = districtAdapter.getPosition(district);
            if (position >= 0) {
                spinnerDistrict.setSelection(position);
            }
        }
    }

    private void showNearestStore(LatLng userLocation) {
        LatLng nearestStore = findNearestStore(userLocation);
        if (nearestStore != null) {
            myMap.addMarker(new MarkerOptions().position(nearestStore)).showInfoWindow();
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nearestStore, 14.0f));
        }
    }

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

    private double calculateDistance(LatLng point1, LatLng point2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(point2.latitude - point1.latitude);
        double dLng = Math.toRadians(point2.longitude - point1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(point1.latitude)) * Math.cos(Math.toRadians(point2.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return earthRadius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
