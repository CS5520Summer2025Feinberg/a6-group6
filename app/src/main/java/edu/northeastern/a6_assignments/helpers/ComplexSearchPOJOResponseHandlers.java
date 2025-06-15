package edu.northeastern.a6_assignments.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_assignments.pojo.ComplexSearchResponseElement;

public class ComplexSearchPOJOResponseHandlers {

    public static List<ComplexSearchResponseElement> handleComplexSearchResponse(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
            JSONArray resultList = (JSONArray) obj.get("results");
            List<ComplexSearchResponseElement> listResponse = new ArrayList<>();
            for(int i=0;i<resultList.length();i++) {
                ComplexSearchResponseElement ele= new ComplexSearchResponseElement();
                JSONObject resultEle = (JSONObject) resultList.get(i);
                ele.setId(Integer.parseInt(resultEle.get("id").toString()));
                ele.setImage(resultEle.get("image").toString());
                ele.setTitle(resultEle.get("title").toString());
                ele.setImageType(resultEle.get("imageType").toString()); // Can convert to enum but not sure type of imageType as couldn't find swagger
                listResponse.add(ele);
            }
            return listResponse;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
