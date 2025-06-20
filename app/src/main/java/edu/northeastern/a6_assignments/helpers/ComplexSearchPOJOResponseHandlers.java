package edu.northeastern.a6_assignments.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_assignments.pojo.ComplexSearchResponseElement;

/**
 * ComplexSearchPOJOResponseHandlers is a utility class that handles the parsing of complex search
 * responses from a JSON string into a list of ComplexSearchResponseElement objects.
 */
public class ComplexSearchPOJOResponseHandlers {

  /**
   * Parses a JSON response string and converts it into a list of ComplexSearchResponseElement
   * objects.
   *
   * @param response The JSON response string containing search results.
   * @return A list of ComplexSearchResponseElement objects parsed from the response.
   */
  public static List<ComplexSearchResponseElement> handleComplexSearchResponse(String response) {
    JSONObject obj = null;
    try {
      obj = new JSONObject(response);
      JSONArray resultList = (JSONArray) obj.get("results");
      List<ComplexSearchResponseElement> listResponse = new ArrayList<>();
      for (int i = 0; i < resultList.length(); i++) {
        ComplexSearchResponseElement ele = new ComplexSearchResponseElement();
        JSONObject resultEle = (JSONObject) resultList.get(i);
        ele.setId(Integer.parseInt(resultEle.get("id").toString()));
        ele.setImage(resultEle.get("image").toString());
        ele.setTitle(resultEle.get("title").toString());
        ele.setImageType(resultEle.get("imageType")
            .toString());
        listResponse.add(ele);
      }
      return listResponse;
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }
}
