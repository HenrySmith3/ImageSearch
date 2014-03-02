package com.fieldwire.ImageSearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.*;
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
    HorizontalScrollView horizontalScrollView;
    LinearLayout linearLayout;
    Activity activity;//TODO: THIS IS BAD. There seriously must be a better way to do this.
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = this;
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        button = (Button)findViewById(R.id.button);
        text = (TextView)findViewById(R.id.editText);
        imageView = (ImageView)findViewById(R.id.imageView);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        linearLayout = new LinearLayout(this);
        horizontalScrollView.addView(linearLayout);
        button.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

        @Override
        public void onClick(View view) {
            linearLayout.removeAllViews();
            for (int i = 0; i < 20; i += 4) {
                feed = new Feed();
                feed.setImageViews(imageView, linearLayout, activity);
                AsyncTask<String, Void, Bitmap[]> task = feed.execute(url + text.getText().toString() +
                    "&start=" + i);
            }

        }
    }
}
