package com.example.datn_toystoryshop.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.ArtStoryAdapter;
import com.example.datn_toystoryshop.Model.ArtStoryModel;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArtStory_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtStoryAdapter adapter;

    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_art_story, container, false);
        sharedPreferences = requireContext().getSharedPreferences("Settings", requireContext().MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        // Lấy documentId từ Bundle
        if (getArguments() != null) {
            documentId = getArguments().getString("documentId", "");
            Log.e("ArtStoryFragment", "Received documentId: " + documentId);
        }
        recyclerView = view.findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        fetchArtStories();

        return view;
    }

    private void fetchArtStories() {
        APIService apiService = RetrofitClient.getAPIService();

        apiService.getArtStories().enqueue(new Callback<List<ArtStoryModel>>() {
            @Override
            public void onResponse(Call<List<ArtStoryModel>> call, Response<List<ArtStoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ArtStoryAdapter(getContext(), response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ArtStoryModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
