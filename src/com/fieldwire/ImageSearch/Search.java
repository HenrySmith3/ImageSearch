package com.fieldwire.ImageSearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class Search extends Activity {

    Feed feed;
    Button button;
    TextView text;
    ImageView imageView;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        button = (Button)findViewById(R.id.button);
        text = (TextView)findViewById(R.id.editText);
        imageView = (ImageView)findViewById(R.id.imageView);
        feed = new Feed();
        feed.setImageView(imageView);
        button.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

        @Override
        public void onClick(View view) {
            AsyncTask<String, Void, Bitmap> task = feed.execute(url + text.getText().toString());

        }
    }
}
