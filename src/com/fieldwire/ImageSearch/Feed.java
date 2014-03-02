package com.fieldwire.ImageSearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;

class Feed extends AsyncTask<String, Void, Bitmap> {

    private Exception exception;
    private ImageView imageView;

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        try {
            URL url= new URL(urls[0]);
            InputStream is = (InputStream)url.getContent();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String theString = s.hasNext() ? s.next() : "";
            JSONObject jsonObject = new JSONObject(theString);
            JSONArray array = jsonObject.getJSONObject("responseData").getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String imageUrl = object.getString("tbUrl");
                return BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
            }
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Bitmap feed) {
        imageView.setImageBitmap(feed);
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}