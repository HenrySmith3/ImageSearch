package com.fieldwire.ImageSearch;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * The main class of this app. Runs the Feeds and manages most of the logic.
 */
public class Search extends Activity {

    Feed feed;
    Button button;
    AutoCompleteTextView text;
    ImageView imageView;
    HorizontalScrollView horizontalScrollView;
    ImagesLayout linearLayout;
    Activity activity;//TODO: this seems bad but I don't know what else to do.
    ArrayList<String> suggestions;
    //We'll retrieve 40 images when the user hits the button.
    final int IMAGES_TO_RETRIEVE =40;
    //We don't need to try to retrieve that many as they're typing. Let's just do 8 instead.
    final int IMAGES_TO_RETRIEVE_WHILE_TYPING = 8;

    ArrayList<Feed> feeds;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        feeds = new ArrayList<Feed>();
        suggestions = new ArrayList<String>();
        activity = this;
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);
        button = (Button)findViewById(R.id.button);
        text = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        text.setOnKeyListener(new SearchPreviewListener());
        imageView = (ImageView)findViewById(R.id.imageView);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        linearLayout = new ImagesLayout(this);
        horizontalScrollView.addView(linearLayout);
        button.setOnClickListener(new ClickListener());
    }

    /**
     * Runs the search both for clicking the button and for the live suggest.
     * @param addToSuggestions If true, the search term is added to the dictionary of past suggestions.
     */
    public void runSearch(boolean addToSuggestions) {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
        if (text.getText().toString().isEmpty()) return;
        if (addToSuggestions) {
            if (!suggestions.contains(text.getText().toString())) {
                suggestions.add(text.getText().toString());
            }
            text.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, suggestions));
        }
        linearLayout.clearViews();
        String currentUrl = url + "'" + text.getText().toString().replace(" ", "%20") + "'";
        //cancel anything from before that didn't finish.
        for (Feed feed : feeds) {
            feed.cancel(true);
        }
        //Multiple feeds will be running concurrently.
        for (int i = 0; i < IMAGES_TO_RETRIEVE; i += 4) {
            feed = new Feed(imageView, linearLayout, activity);
            feeds.add(feed);
            //I could pass in more than one URL at a time, but I'd rather get one started then pass in the next.
            feed.execute(currentUrl + "&start=" + i);
        }
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            runSearch(true);
        }
    }

    private class SearchPreviewListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int j, KeyEvent keyEvent) {
            runSearch(false);
            return false;
        }
    }
}
