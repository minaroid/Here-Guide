package com.george888.mina.hereguide.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.george888.mina.hereguide.pojo.PlaceInfo;
import com.george888.mina.hereguide.pojo.Review;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minageorge on 2/21/18.
 */

public class PlaceInfoLoader extends AsyncTaskLoader<List<PlaceInfo>> {

    private Bundle mBundle;
    private int status;
    private String Data;
    private List<PlaceInfo> datalist = new ArrayList<>();
    private Context mContext;

    public PlaceInfoLoader(Context context, Bundle bundle) {
        super(context);
        this.mBundle = bundle;
        this.mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<PlaceInfo> loadInBackground() {
            try {
                URL url = new URL(mBundle.getString("url"));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();
                status = httpURLConnection.getResponseCode();
                if (status == 200) {
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    Data = Stream2String(in);
                    httpURLConnection.disconnect();
                    JSONObject jsonObject = new JSONObject(Data);
                    JSONObject obj = jsonObject.getJSONObject("result");
                    PlaceInfo pi = new PlaceInfo();
                    if (obj.has("international_phone_number")) {
                        pi.setPhone(obj.getString("international_phone_number"));
                    }
                    if (obj.has("formatted_address")) {
                        pi.setAddress(obj.getString("formatted_address"));
                    }
                    if (obj.has("website")) {
                        pi.setWebsite(obj.getString("website"));
                    }
                    if (obj.has("opening_hours")) {
                        if (obj.getJSONObject("opening_hours").has("weekday_text")) {
                            JSONArray array = obj.getJSONObject("opening_hours").getJSONArray("weekday_text");
                            ArrayList<String> weekdays = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++) {
                                weekdays.add(array.getString(i));
                            }
                            pi.setWeekdays(weekdays);
                        }
                    }

                    if (obj.has("photos")) {
                        JSONArray array = obj.getJSONArray("photos");
                        ArrayList<String> reference = new ArrayList<String>();
                        for (int i = 0; i < array.length(); i++) {
                            reference.add(array.getJSONObject(i).getString("photo_reference"));
                        }
                        pi.setPhotos(reference);
                    }
                    if (obj.has("reviews")) {
                        JSONArray array = obj.getJSONArray("reviews");
                        ArrayList<Review> reviews = new ArrayList<Review>();
                        for (int i = 0; i < array.length(); i++) {
                            reviews.add(new Review(array.getJSONObject(i).getString("author_name"),
                                    array.getJSONObject(i).getString("rating"),
                                    array.getJSONObject(i).getString("profile_photo_url"),
                                    array.getJSONObject(i).getString("text"),
                                    array.getJSONObject(i).getString("relative_time_description")
                            ));
                        }
                        pi.setReviews(reviews);
                    }

                    datalist.add(pi);
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        return datalist;
    }

    @Override
    public void deliverResult(List<PlaceInfo> data) {
        super.deliverResult(data);
    }

    public String Stream2String(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String Text = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                Text += line;
            }
            inputStream.close();
        } catch (Exception e) {
        }
        return Text;
    }
}
