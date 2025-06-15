package edu.northeastern.a6_assignments.services;

import android.os.Handler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.northeastern.a6_assignments.BuildConfig;

public class ComplexSearchRecipe implements Runnable {

    public interface ComplexSearchCallBack {
        String getRequestParameters();
        void createObjectFromResponse(String response);

    }
    private final ComplexSearchCallBack searchCallBack;
    private final Handler apiCallHandler;
    public ComplexSearchRecipe(ComplexSearchCallBack searchCallBack, Handler apiCallHandler) {
        this.searchCallBack = searchCallBack;
        this.apiCallHandler = apiCallHandler;
    }

    @Override
    public void run() {
        try {
            String baseUrl = "https://api.spoonacular.com/recipes/complexSearch";
            String apiKey = BuildConfig.SPOONACULAR_API_KEY;
            URL url = new URL(baseUrl + "?apiKey=" + apiKey + "&"+searchCallBack.getRequestParameters());
           /* String apiKey = BuildConfig.SPOONACULAR_API_KEY;
            URL url = new URL("https://api.spoonacular.com/recipes/complexSearch"+"?apiKey=" + apiKey + "&"+"query=chicken");*/
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            InputStream stream = httpURLConnection.getInputStream();
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String jsonResponse = result.toString();
            apiCallHandler.post(()-> {
                    searchCallBack.createObjectFromResponse(jsonResponse);
            });

            httpURLConnection.disconnect();
            stream.close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
