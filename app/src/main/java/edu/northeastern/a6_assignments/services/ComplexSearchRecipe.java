package edu.northeastern.a6_assignments.services;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.northeastern.a6_assignments.BuildConfig;

/**
 * ComplexSearchRecipe is a Runnable class that performs a complex search for recipes using the
 * Spoonacular API. It constructs the request URL with the provided parameters, makes an HTTP GET
 * request, and processes the response.
 */
public class ComplexSearchRecipe implements Runnable {

    /**
     * ComplexSearchCallBack is an interface that defines methods for handling the request
     * parameters and processing the response from the Spoonacular API.
     */
    public interface ComplexSearchCallBack {
        String getRequestParameters();
        void createObjectFromResponse(String response);

    }

    // Instance variables for the callback and handler
    private final ComplexSearchCallBack searchCallBack;
    private final Handler apiCallHandler;

    /**
     * Constructor for ComplexSearchRecipe.
     *
     * @param searchCallBack The callback interface to handle request parameters and response.
     * @param apiCallHandler The handler to post results back to the main thread.
     */
    public ComplexSearchRecipe(ComplexSearchCallBack searchCallBack, Handler apiCallHandler) {
        this.searchCallBack = searchCallBack;
        this.apiCallHandler = apiCallHandler;
    }

    @Override
    public void run() {
        try {
            // Construct the URL for the Spoonacular API complex search endpoint
            String baseUrl = "https://api.spoonacular.com/recipes/complexSearch";
            String apiKey = BuildConfig.SPOONACULAR_API_KEY;
            URL url = new URL(baseUrl + "?apiKey=" + apiKey + "&"+searchCallBack.getRequestParameters());

            // Open a connection to the URL
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            // Check the response code and handle the response
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

            // Close the resources
            httpURLConnection.disconnect();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
