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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Search extends Activity {

    Feed feed;
    Button button;
    AutoCompleteTextView text;
    ImageView imageView;
    HorizontalScrollView horizontalScrollView;
    ImagesLayout linearLayout;
    Activity activity;//TODO: this seems bad but I don't know what else to do.
    ArrayList<String> suggestions;
    final int IMAGES_TO_RETRIEVE = 20;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        suggestions = new ArrayList<String>();
        activity = this;
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        button = (Button)findViewById(R.id.button);
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        imageView = (ImageView)findViewById(R.id.imageView);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        linearLayout = new ImagesLayout(this);
        horizontalScrollView.addView(linearLayout);
        button.setOnClickListener(new ClickListener());
    }

    private class ClickListener implements View.OnClickListener {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

        @Override
        public void onClick(View view) {
            if (text.getText().toString().isEmpty()) return;
            suggestions.add(text.getText().toString());
            text.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, suggestions));
            linearLayout.clearViews();
            for (int i = 0; i <= IMAGES_TO_RETRIEVE; i += 4) {
                feed = new Feed(imageView, linearLayout, activity);
                feed.execute(url + "'" + text.getText().toString().replace(" ", "%20") + "'" + "&start=" + i);
            }

        }
    }
}
