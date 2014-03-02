package com.fieldwire.ImageSearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;

/**
 * A feed is a single instance of querying the google images api.
 */
class Feed extends AsyncTask<String, Void, Bitmap[]> {

    private ImageView imageView;
    private ImagesLayout linearLayout;
    private Activity activity;

    public Feed(ImageView imageView, ImagesLayout linearLayout, Activity activity) {
        this.imageView = imageView;
        this.linearLayout = linearLayout;
        this.activity = activity;
    }

    /**
     * Runs the actual searching. Only needs one url, to run multiple, spawn multiple feeds.
     * @param urls The first url will be searched on.
     * @return The bitmap array of search results.
     */
    protected Bitmap[] doInBackground(String... urls) {
        Bitmap[] retVal = new Bitmap[1];
        try {
            URL url= new URL(urls[0]);
            InputStream is = (InputStream)url.getContent();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String theString = s.hasNext() ? s.next() : "";
            s.close();
            JSONObject jsonObject = new JSONObject(theString);
            JSONArray array = jsonObject.getJSONObject("responseData").getJSONArray("results");

            retVal = new Bitmap[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String imageUrl = object.getString("tbUrl");
                InputStream imageStream = (InputStream)new URL(imageUrl).getContent();
                retVal[i] =  BitmapFactory.decodeStream(imageStream);
               imageStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;

    }

    /**
     * This is what actually updates the scrolling images window.
     * @param feed The bitmaps that will be added to the scrolling window.
     */
    protected void onPostExecute(Bitmap[] feed) {
        for (Bitmap bitmap : feed) {
            ImageView view = new ImageView(activity);
            view.setImageBitmap(bitmap);
            view.setOnClickListener(new PictureClickListener());
            linearLayout.addImage(view);
        }
    }
    private class PictureClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Drawable drawable = ((ImageView)view).getDrawable();
            imageView.setImageBitmap(((BitmapDrawable)drawable).getBitmap());
        }
    }
}