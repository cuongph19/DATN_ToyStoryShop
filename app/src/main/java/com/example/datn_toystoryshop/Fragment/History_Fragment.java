package com.example.datn_toystoryshop.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.datn_toystoryshop.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link History_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerMonth;
    private Spinner spinnerYear;
    public History_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static History_Fragment newInstance(String param1, String param2) {
        History_Fragment fragment = new History_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Khởi tạo các Spinner
        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        spinnerYear = view.findViewById(R.id.spinnerYear);

        // Thiết lập Adapter cho Spinner tháng
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.months_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        // Thiết lập Adapter cho Spinner năm
        ArrayList<String> years = new ArrayList<>();
        for (int i = 2020; i <= 2030; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        return view;
    }
}