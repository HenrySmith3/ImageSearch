package com.fieldwire.ImageSearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;

class Feed extends AsyncTask<String, Void, Bitmap[]> {

    private ImageView imageView;
    private LinearLayout linearLayout;
    private Activity activity;

    public Feed(ImageView imageView, LinearLayout linearLayout, Activity activity) {
        this.imageView = imageView;
        this.linearLayout = linearLayout;
        this.activity = activity;
    }

    protected Bitmap[] doInBackground(String... urls) {
        Bitmap[] retVal = new Bitmap[1];
        try {
            URL url= new URL(urls[0]);
            InputStream is = (InputStream)url.getContent();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String theString = s.hasNext() ? s.next() : "";
            JSONObject jsonObject = new JSONObject(theString);
            JSONArray array = jsonObject.getJSONObject("responseData").getJSONArray("results");

            retVal = new Bitmap[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String imageUrl = object.getString("tbUrl");
                retVal[i] =  BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;

    }

    protected void onPostExecute(Bitmap[] feed) {
        for (Bitmap bitmap : feed) {
            ImageView view = new ImageView(activity);
            view.setImageBitmap(bitmap);
            view.setOnClickListener(new PictureClickListener());
            linearLayout.addView(view);
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