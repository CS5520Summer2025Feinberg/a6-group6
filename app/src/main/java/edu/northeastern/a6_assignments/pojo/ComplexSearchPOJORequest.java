package edu.northeastern.a6_assignments.pojo;

import java.util.ArrayList;
import java.util.List;

public class ComplexSearchPOJORequest {
    private String query;
    private List<Cuisine> cuisine;
    private List<Cuisine> excludeCuisine;
    private Diet diet;
    private List<Intolerance> intolerance;
    private MealTypes mealTypes;
    private int maxReadyTime;
    private int maxServings;
    private int minServings;
    private int numberOfRecipes;


    private ComplexSearchPOJORequest(ComplexSearchPOJORequestBuilder complexSearchPOJORequestBuilder) {
        this.cuisine = complexSearchPOJORequestBuilder.cuisine;
        this.diet = complexSearchPOJORequestBuilder.diet;
        this.excludeCuisine = complexSearchPOJORequestBuilder.excludeCuisine;
        this.maxReadyTime = complexSearchPOJORequestBuilder.maxReadyTime;
        this.maxServings = complexSearchPOJORequestBuilder.maxServings;
        this.intolerance = complexSearchPOJORequestBuilder.intolerance;
        this.numberOfRecipes = complexSearchPOJORequestBuilder.numberOfRecipes;
        this.mealTypes = complexSearchPOJORequestBuilder.mealTypes;
        this.minServings = complexSearchPOJORequestBuilder.minServings;
        this.query = complexSearchPOJORequestBuilder.query;
    }

    public String getRequestParameters() {
        StringBuilder request = new StringBuilder();

        addStringParam(request, "query", query);
        addListParam(request, "cuisine", cuisine);
        addListParam(request, "excludeCuisine", excludeCuisine);
        addStringParam(request, "diet", diet != null ? diet.getValue() : null);
        addListParam(request, "intolerances", intolerance);
        addStringParam(request, "type", mealTypes != null ? mealTypes.getValue() : null);
        addIntParam(request, "maxReadyTime", maxReadyTime);
        addIntParam(request, "maxServings", maxServings);
        addIntParam(request, "minServings", minServings);
        addIntParam(request, "number", numberOfRecipes);

        return request.toString();
    }

    private void addStringParam(StringBuilder request, String paramName, String value) {
        if (value != null && !value.trim().isEmpty()) {
            if (request.length() > 0) request.append("&");
            request.append(paramName).append("=");
            String[] parts = value.split(" ");
            if(parts.length != 1) {
                request.append("\\\"");
                request.append(value);
                request.append("\\\"");
            } else {
                request.append(value);
            }
        }
    }

    private void addIntParam(StringBuilder request, String paramName, int value) {
        if (value > 0) {
            if (request.length() > 0) request.append("&");
            request.append(paramName).append("=").append(value);
        }
    }

    private <T> void addListParam(StringBuilder request, String paramName, List<T> list) {
        if (list != null && !list.isEmpty()) {
            if (request.length() > 0) request.append("&");
            request.append(paramName).append("=");

            boolean firstFlag = true;
            for (T item : list) {
                if (!firstFlag) {
                    request.append(",");
                }


                if (item instanceof Cuisine) {
                    String cuisine = ((Cuisine) item).getValue();
                    String[] parts = cuisine.split(" ");
                    if(parts.length != 1) {
                        request.append("\\\"");
                        request.append(cuisine);
                        request.append("\\\"");
                    }
                    else { request.append(cuisine); }
                } else if (item instanceof Intolerance) {
                    String intolerance = ((Intolerance) item).getValue();
                    String[] parts = intolerance.split(" ");
                    if(parts.length != 1) {
                        request.append("\\\"");
                        request.append(intolerance);
                        request.append("\\\"");
                    }
                    else { request.append(intolerance); }
                }
                firstFlag = false;
            }
        }
    }

    public static class ComplexSearchPOJORequestBuilder {
        private String query;
        private List<Cuisine> cuisine;
        private List<Cuisine> excludeCuisine;
        private Diet diet;
        private List<Intolerance> intolerance;
        private MealTypes mealTypes;
        private int maxReadyTime;
        private int maxServings;
        private int minServings;
        private int numberOfRecipes;

        public ComplexSearchPOJORequestBuilder(String query){
            if(!query.isEmpty()){
                this.query = query;
            }
        }

        public ComplexSearchPOJORequestBuilder setCuisine(List<String> cuisine) {
            List<Cuisine> cus = new ArrayList<>();
            if(!cuisine.isEmpty()) {
                for (String cui : cuisine) {
                    cus.add(Cuisine.valueOf(cui));
                }
            }
            this.cuisine = cus;
            return this;
        }

        public ComplexSearchPOJORequestBuilder setExcludeCuisine(List<String> excludeCuisine) {
            List<Cuisine> excluCus = new ArrayList<>();
            if(!excludeCuisine.isEmpty()) {;
                for (String cui : excludeCuisine) {
                    excluCus.add(Cuisine.valueOf(cui));
                }
            }
            this.excludeCuisine = excluCus;
            return this;
        }

        public ComplexSearchPOJORequestBuilder setDiet(String diet) {
            if(!diet.isEmpty()) {
                this.diet = Diet.valueOf(diet);
            }

            return this;
        }

        public ComplexSearchPOJORequestBuilder setIntolerance(List<String> intolerance) {
            List<Intolerance> intole = new ArrayList<>();
            if(!intolerance.isEmpty()) {
                for (String intoler : intolerance) {
                    intole.add(Intolerance.valueOf(intoler));
                }
            }
            this.intolerance = intole;
            return this;
        }

        public ComplexSearchPOJORequestBuilder setMealTypes(String mealTypes) {
            if(!mealTypes.isEmpty()) {
                this.mealTypes = MealTypes.valueOf(mealTypes);
            }
            return this;
        }

        public ComplexSearchPOJORequestBuilder setMaxReadyTime(int maxReadyTime) {
            this.maxReadyTime = maxReadyTime;
            return this;
        }

        public ComplexSearchPOJORequestBuilder setMaxServings(int maxServings) {
            this.maxServings = maxServings;
            return this;
        }

        public ComplexSearchPOJORequestBuilder setMinServings(int minServings) {
            this.minServings = minServings;
            return this;
        }

        public ComplexSearchPOJORequestBuilder setNumberOfRecipes(int numberOfRecipes) {
            this.numberOfRecipes = numberOfRecipes;
            return this;
        }

        public ComplexSearchPOJORequest build() {
            return new ComplexSearchPOJORequest(this);
        }
    }
}