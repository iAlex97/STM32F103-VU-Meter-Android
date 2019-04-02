package com.ialex.stmvumeter.data.remote;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ialex on 15.02.2017.
 */

public interface Api {
    String URL_PROD_BASE = "http://ec2-35-176-220-121.eu-west-2.compute.amazonaws.com:3000/";
    String URL_PROD_TEST = URL_PROD_BASE/*"http://192.168.0.105/"*/;

    String GOV_DATA_FOOD_SEARCH = "https://api.nal.usda.gov/ndb/search/?format=json&api_key=4shze9uq9WwG79ma8DjLzElCiQd2GJxWHogfeQ3I";
    String GOV_DATA_ITEM_DETAILS = "https://api.nal.usda.gov/ndb/reports/?type=f&format=json&api_key=4shze9uq9WwG79ma8DjLzElCiQd2GJxWHogfeQ3I";


    /*@GET
    Observable<SearchFoodResponse> searchFood(@Url String base,
                                              @Query("q") String query,
                                              @Query("max") int max,
                                              @Query("offset") int offset);
    @GET
    Observable<ItemDetailsResponse> getFoodDetails(@Url String base,
                                                   @Query("ndbno") String dbNumber);

    @POST("/register")
    Observable<AuthResponse> register(@Body RegisterBody body);

    @POST("/login")
    Observable<AuthResponse> login(@Body LoginBody body);

    @GET("/users/me")
    Observable<UserResponse> getProfile();

    @PUT("/users")
    Call<Void> updateProfile(@Body ProfileBody body);

    @POST("/meals")
    Call<Void> addMeal(@Body MealBody body);

    @GET("/days/me")
    Observable<DaysResponse> getMealsReport();

    @GET("/meals/me/today")
    Observable<MealsResponse> getTodaysMeals();

    @DELETE("/meals/{mealId}")
    Observable<Response<Void>> deleteMeal(@Path("mealId") Integer mealId);*/
}
