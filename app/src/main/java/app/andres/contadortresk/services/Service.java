package app.andres.contadortresk.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import app.andres.contadortresk.utilities.VolleyCallback;


public class Service {

    //variables
    public static final int MY_DEFAULT_TIMEOUT = 15000;



    //-------------------------------------------------------------
    //  METODOS PARA GESTIONAR EL API - PHP
    //-------------------------------------------------------------
    // Get Request For JSONObject  METODO : GET
    public void getData(Context context, String url, final VolleyCallback callback ){

        // ejecuta metodo GET con Volley
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // Log.d(" Response Service: ", String.valueOf(response));

                    callback.onSuccessResponse(response); // callback de la Interface

                    String messageResponse = "";

                    try {
                        messageResponse = String.valueOf(response.get("message"));

                    } catch (JSONException e) {
                        Log.i(" MyTag JSONException", String.valueOf(e.getMessage()));
                    }

                    if (!messageResponse.equals("")){ // muestra el mensaje
                       // Toast.makeText(context, messageResponse, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error != null) {
                        // Log.d(" Error Service: ", String.valueOf(error.networkResponse.statusCode));

                        String errorResponse = "";

                        try {

                            if (error.networkResponse == null){
                                Toast.makeText(context, "Error: NO Server Access/Permission", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            errorResponse = data.optString("error");
                            callback.onErrorResponse(errorResponse); // callback de la Interface

                        } catch (UnsupportedEncodingException | JSONException e) {
                            Log.i(" MyTag JSONException", String.valueOf(e.getMessage()));
                        }

                        if (!errorResponse.equals("")) { // muestra el mensaje
                            Toast.makeText(context, errorResponse, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }){

                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> headers = new HashMap<>();
                    // add headers <key,value>
                    String auth = "Basic NzBlYzZmYTRkNmVlMmEyZWFlZDRhODA5NmFiYTJlYWE6M2NlODBmZDA3ZGI2ZTRjYzcyZDE3NDQ5YWIxMzllYjU=";
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);

                    return headers;
                }

            };

            RequestQueue requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
            requestQueue.add(jsonObjectRequest);

    }



    





    // Post Request For JSONObject  METODO : POST
    public void postData(Context context, String url, JSONObject objectJson, final VolleyCallback callback ) {

        // Log.d(" TAG_DATA", "postData() called with: context = [" + context + "], url = [" + url + "], objectJson = [" + objectJson + "]");


        // ejecuta metodo POST con Volley
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url, objectJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Log.d(" Response Service: ", String.valueOf(response));

                        callback.onSuccessResponse(response); // callback de la Interface

                        String messageResponse = "";

                        try {
                                messageResponse = response.getString("message");

                        } catch (JSONException e) {
                            Log.i(" MyTag JSONException", String.valueOf(e.getMessage()));
                        }

                        if (!messageResponse.equals("")){ // muestra el mensaje
                            Toast.makeText(context, messageResponse, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error != null) {

                            // Log.d(" Error Service: ", String.valueOf(error));

                            String errorResponse = "";

                            try {

                                if (error.networkResponse == null){
                                    Toast.makeText(context, "Error: NO Server Access/Permission", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                errorResponse = data.optString("error");
                                callback.onErrorResponse(errorResponse); // callback de la Interface

                            } catch (UnsupportedEncodingException | JSONException e) {
                                Log.i(" MyTag JSONException", String.valueOf(e.getMessage()));
                            }

                            if (!errorResponse.equals("")) { // muestra el mensaje
                                Toast.makeText(context, errorResponse, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                // add headers <key,value>
                String auth = "Basic NzBlYzZmYTRkNmVlMmEyZWFlZDRhODA5NmFiYTJlYWE6M2NlODBmZDA3ZGI2ZTRjYzcyZDE3NDQ5YWIxMzllYjU=";
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }

        };


        // usando el patron Singleton
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();

        jsonObj.setRetryPolicy(new DefaultRetryPolicy( // politica de espera de la respuesta HTTP
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObj);

    }


    // Put Request For JSONObject  METODO : PUT
    public void putData(Context context, String url, JSONObject objectJson, final VolleyCallback callback ) {

        // Log.d(" TAG_DATA", "postData() called with: context = [" + context + "], url = [" + url + "], objectJson = [" + objectJson + "]");


        // ejecuta metodo POST con Volley
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url, objectJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //  Log.d(" Response Service: ", String.valueOf(response));

                        callback.onSuccessResponse(response); // callback de la Interface

                        String messageResponse = "";

                        try {
                            messageResponse = response.getString("message");

                        } catch (JSONException e) {
                            Log.i(" MyTag JSONException", String.valueOf(e.getMessage()));
                        }

                        if (!messageResponse.equals("")){ // muestra el mensaje
                            Toast.makeText(context, messageResponse, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error != null) {

                            // Log.d(" Error Service: ", String.valueOf(error.networkResponse.statusCode));

                            String errorResponse = "";

                            try {

                                if (error.networkResponse == null){
                                    Toast.makeText(context, "Error: NO Server Access/Permission", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                errorResponse = data.optString("error");
                                callback.onErrorResponse(errorResponse); // callback de la Interface

                            } catch (UnsupportedEncodingException | JSONException e) {
                                Log.i(" MyTag JSONException", String.valueOf(e.getMessage()));
                            }

                            if (!errorResponse.equals("")) { // muestra el mensaje
                                Toast.makeText(context, errorResponse, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                // add headers <key,value>
                String auth = "Basic NzBlYzZmYTRkNmVlMmEyZWFlZDRhODA5NmFiYTJlYWE6M2NlODBmZDA3ZGI2ZTRjYzcyZDE3NDQ5YWIxMzllYjU=";
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }

        };


        // usando el patron Singleton
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
        requestQueue.add(jsonObj);

    }



    // --------------------------------------------
    // LOCALSTORAGE SharedPreferences
    // --------------------------------------------

    public void saveLocalData(Context context, String key, String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("count", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public String getLocalData(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("count", 0);
        if (sharedPreferences.contains(key)) {

             String data = sharedPreferences.getString(key, null);
             return data;

        } else {
            return null;
        }
    }

    public void removeLocalData(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("count", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(key);
        editor.apply();


    }


    public void saveLocalData_int(Context context, String key, int value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("count", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public int getLocalData_int(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("count", 0);
        if (sharedPreferences.contains(key)) {

            int data = sharedPreferences.getInt(key, 0);
            return data;

        } else {
            return 0;
        }
    }




} // END class


/**
 *
 * // parametros enviando Strings
 @Nullable
 @Override
 protected Map<String, String> getParams(){

 Map<String, String> params = new HashMap<>();

 params.put("name", nombre);
 params.put("email", correo);
 params.put("password", password_0);

 Log.d("Parametros: ", String.valueOf(params));

 return params;
 }
 */