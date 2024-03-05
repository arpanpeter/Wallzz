package com.example.wallz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallz.Api.RetrofitClient;
import com.example.wallz.models.Wallpaper;
import com.example.wallz.models.wallpaperResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {
    RecyclerView recyclerView;
    private final String API_KEY = " VwzP6jRCkFKOIF5toLlKES2HaSks7OM1VjgQIbnz6lHotvscFOCMw1EP";
    List<Wallpaper> dataList;
    RecyclerViewClickInterface recyclerViewClickInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewClickInterface = this;
        getData();
    }

    private void getData() {
        Call<wallpaperResponse> wallpaperResponseCall = RetrofitClient
                .getInstance()
                .getApi()
                .getWallpaper(API_KEY);

        wallpaperResponseCall.enqueue(new Callback<wallpaperResponse>() {
            @Override
            public void onResponse(Call<wallpaperResponse> call, Response<wallpaperResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    dataList = response.body().getPhotosList();
                    WallpaperAdapter wallpaperAdapter = new WallpaperAdapter(getApplication(), dataList, recyclerViewClickInterface);
                    recyclerView.setAdapter(wallpaperAdapter);
                    wallpaperAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<wallpaperResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
@Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), fullImageActivity.class);
        intent.putExtra("imageUrl", dataList.get(position).getSrc().getLarge());
        startActivity(intent);

    }
}
