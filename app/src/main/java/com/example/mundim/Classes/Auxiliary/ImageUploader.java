package com.example.mundim.Classes.Auxiliary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageUploader {
    private String upload_URL = "http://68.183.133.221/mundim/uploadfile.php?";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    private Integer compressionRate = 100;
    public void uploadImage(final Bitmap bitmap, final Context context, final String id, final int compressionRate){
        this.compressionRate = compressionRate;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("response.data",new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            Log.d("response.data.JSON",new String(response.data));
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Log.d("response.data.JSON",new String(response.data));
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("response.data.JSON",new String(response.data));

                            jsonObject.toString().replace("\\\\","");

                            if (jsonObject.getString("status").equals("true")) {

                                arraylist = new ArrayList<HashMap<String, String>>();
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                String url = "";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    url = dataobj.optString("pathToFile");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                byte[] data = getFileDataFromDrawable(bitmap);
                Log.d("response.data.DATALEN", data.length + "");
                params.put("filename", new DataPart(id, data));
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rQueue = Volley.newRequestQueue(context);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, this.compressionRate, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

