package com.example.wallz;

import android.app.WallpaperManager;
import android.content.pm.PackageManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FullImageActivity extends AppCompatActivity {
    ImageView fullImage;
    ProgressBar progressBar;
    Button save, setBack;
    String image_url;

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 100;

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
                checkStoragePermissionAndSave();
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
                        Toast.makeText(FullImageActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermissionAndSave() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            saveImageToDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImageToDevice();
            } else {
                Toast.makeText(this, "Storage permission denied. Cannot save image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToDevice() {
        BitmapDrawable drawable = (BitmapDrawable) fullImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Create directory if it doesn't exist
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Wallpapers");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate file name
        String fileName = "wallpaper_" + System.currentTimeMillis() + ".jpg";

        // Create file
        File file = new File(directory, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}
