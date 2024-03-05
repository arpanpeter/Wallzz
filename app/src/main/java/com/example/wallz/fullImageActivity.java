package com.example.wallz;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.wallz.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class fullImageActivity extends AppCompatActivity {
    ImageView fullImage;
    ProgressBar progressBar;
    Button save, setBack;
    String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        fullImage = findViewById(R.id.full_image);
        progressBar = findViewById(R.id.progressBar);
        save = findViewById(R.id.save);
        setBack = findViewById(R.id.setBack);
        image_url = getIntent().getStringExtra("imageUrl");
        loadImage(image_url);
        setBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeScreen();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveImageToDevice();
            }
        });
    }

    private void loadImage(String image_url) {
        Glide.with(this)
                .load(image_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(fullImage);
    }

    private void setHomeScreen() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = ((BitmapDrawable) fullImage.getDrawable()).getBitmap();
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImageToDevice() {
        BitmapDrawable drawable = (BitmapDrawable) fullImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/Wallpapers");
        directory.mkdirs(); // Create if not exists

        File file = new File(directory, "wallpaper_" + System.currentTimeMillis() + ".jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
