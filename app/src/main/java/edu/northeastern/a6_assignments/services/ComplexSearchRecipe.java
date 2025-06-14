package edu.northeastern.a6_assignments.services;

import android.os.Handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ComplexSearchRecipe implements Runnable {

    public interface ComplexSearchCallBack {

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
            URL url = new URL("https://api.spoonacular.com/recipes/complexSearch");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
