package com.george888.mina.hereguide.tasks;

/**
 * Created by minageorge on 1/29/18.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.george888.mina.hereguide.pojo.ResultsPlace;

import org.json.JSONArray;
import org.json.JSONObject;

public class TypeResultsLoader extends AsyncTaskLoader<List<ResultsPlace>> {
    private List<ResultsPlace> cachedData;
    private Bundle mBundle;
    private int status;
    private String Data;
    private List<ResultsPlace> dataList = new ArrayList<>();
    private static String TAG = TypeResultsLoader.class.getSimpleName();
    private HttpURLConnection httpURLConnection = null;

    public TypeResultsLoader(Context context, Bundle bundle) {
        super(context);
        this.mBundle = bundle;
    }

    @Override
    protected void onStartLoading() {
        if (cachedData == null) {
            forceLoad();
        } else {
            deliverResult(cachedData);
        }
    }

    @Override
    public List<ResultsPlace> loadInBackground() {
        try {
            URL url = new URL(mBundle.getString("url"));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
            status = httpURLConnection.getResponseCode();
            if (status == 200) {
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                Data = StreamToString(in);
                httpURLConnection.disconnect();
                JSONObject jsonObject = new JSONObject(Data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    ResultsPlace place = new ResultsPlace();
                    if (obj.has("photos")) {
                        place.setLat(obj.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                        place.setLng(obj.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                        place.setName(obj.getString("name"));
                        place.setIcon(obj.getString("icon"));
                        place.setId(obj.getString("place_id"));

                        if (obj.has("rating")) {
                            place.setRate(obj.getString("rating"));
                        }

                        if (obj.has("price_level")) {
                            place.setPrice(obj.getString("price_level"));
                        }
                        if (obj.has("opening_hours"))
                            if (obj.getJSONObject("opening_hours").has("open_now")) {
                                place.setOpen(obj.getJSONObject("opening_hours").getString("open_now"));

                            }
                        JSONArray jsonArray2 = obj.getJSONArray("photos");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject obj2 = jsonArray2.getJSONObject(j);
                            place.setPhoto_reference(obj2.getString("photo_reference"));
                        }
                        dataList.add(place);
                    }
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }

        return dataList;
    }

    @Override
    public void deliverResult(List<ResultsPlace> data) {
        super.deliverResult(data);
        cachedData = dataList;
    }

    public String StreamToString(InputStream inputStream) {
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
