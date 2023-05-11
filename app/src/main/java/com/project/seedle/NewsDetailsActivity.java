package com.project.seedle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailsActivity extends AppCompatActivity {

    String title,desc,content,imageURL,url;

    private TextView titleTV,subDescTV,contentTV;
    private ImageView newsIV;
    private Button readNewsBtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");


        titleTV = findViewById(R.id.TVTitle);
        subDescTV = findViewById(R.id.idSubDesc);
        contentTV =findViewById(R.id.idDescription);
        newsIV = findViewById(R.id.idIVNewsd);
        readNewsBtn = findViewById(R.id.BtnReadNews);
        titleTV.setText(title);
        subDescTV.setText(desc);
        contentTV.setText(content);
        if (imageURL != null) {
            Glide.with(this)
                    .load(imageURL)
                    .into(newsIV);
        } else {

            String imageUri = "android.resource://" + this.getPackageName() + "/drawable/newsplaceholder";
            Glide.with(this)
                    .load(Uri.parse(imageUri))
                    .into(newsIV);

        }

        readNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });

    }
}