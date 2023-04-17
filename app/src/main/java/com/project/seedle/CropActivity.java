package com.project.seedle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.canhub.cropper.CropImageActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class CropActivity extends AppCompatActivity {

    public CropImageView cropImageView;
    Bitmap bitmap, croppedBitmap;
    Uri imageUri, croppedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropImageView = findViewById(R.id.cropImageView);



        if (getIntent().hasExtra("imageUri")) {
            String uriString = getIntent().getStringExtra("imageUri");
            String content = getIntent().getStringExtra("content");
            imageUri = Uri.parse(uriString);
            if(content.equals("profile"))
            {
                int width2 = 132, height2 = 132 ;
                float scale2 = getResources().getDisplayMetrics().density;
                int widthInPixels2 = (int) (width2 * scale2 + 0.5f);
                int heightInPixels2 = (int) (height2 * scale2 + 0.5f);
                cropImageView.setAspectRatio(widthInPixels2, heightInPixels2);

            } else if (content.equals("status")) {
                int width = 410, height = 350 ;
                float scale = getResources().getDisplayMetrics().density;
                int widthInPixels = (int) (width * scale + 0.5f);
                int heightInPixels = (int) (height * scale + 0.5f);
                cropImageView.setAspectRatio(widthInPixels, heightInPixels);


            }
        }

        try {
            bitmap = uriToBitmap(imageUri);
            cropImageView.setImageBitmap(bitmap);


            if (bitmap.getWidth() == 0) {
                throw new IllegalArgumentException("Image has zero width");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_crop) {
            croppedBitmap = cropImageView.getCroppedImage();

            try {
                croppedUri = bitmapToUri(croppedBitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("croppedImageUri", croppedUri.toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap uriToBitmap(Uri uri) throws IOException {
        Bitmap bitmap = null;
        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        return bitmap;
    }

    public Uri bitmapToUri(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}
