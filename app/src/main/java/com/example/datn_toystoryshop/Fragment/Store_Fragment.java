package com.example.datn_toystoryshop.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datn_toystoryshop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Store_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Store_Fragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap myMap;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        // Khởi tạo SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;


        // Thiết lập vị trí mặc định cho bản đồ
        LatLng fpoly = new LatLng(21.038572831040383, 105.74770566103412);
        myMap.addMarker(new MarkerOptions().position(fpoly).title("Fpoly"));
        float zoomLevel = 15.0f; // Giá trị zoom
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fpoly, zoomLevel));
    }
}