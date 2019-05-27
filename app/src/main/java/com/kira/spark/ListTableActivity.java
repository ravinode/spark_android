package com.kira.spark;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kira.spark.adapter.TableListAdapter;
import com.kira.spark.util.SparkSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListTableActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    GridView gridview;
    private static final String TAG = "ListTableActivity";
    private static final String JSON_OBJECT_REQUEST_URL = "http://ravikiki.com/api/getTableList.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        volleyJsonObjectRequest(JSON_OBJECT_REQUEST_URL);
        setContentView(R.layout.activity_list_table);
    }

    public void volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "ListTableActivity";
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        List<String> list = new ArrayList<>();

                        gridview = (GridView) findViewById(R.id.tablelist);
                        String tableID[] = null;
                        String count[] = null;
                        String status[] = null;
                        Integer tableColor[] = null;
                        String tableStatus[] = null;
                        try {
                            JSONArray array = response.getJSONArray("result");
                            tableID = new String[array.length()];
                            tableColor = new Integer[array.length()];
                            count = new String[array.length()];
                            tableStatus = new String[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                list.add(array.getJSONObject(i).getString("TABLE_ID"));
                                tableID[i] = array.getJSONObject(i).getString("TABLE_NAME");
                                if(array.getJSONObject(i).getString("STATUS").equalsIgnoreCase("0")) {
                                    tableColor[i] = R.drawable.greent;
                                    tableStatus[i] = "A";
                                    count[i] = array.getJSONObject(i).getString("COUNT");
                                }
                                else {
                                    tableColor[i] = R.drawable.redt;
                                    tableStatus[i] = "B";
                                    count[i] = array.getJSONObject(i).getString("S_COUNT");
                                }

                            }
                        }

                        catch (JSONException e)
                        {
                            e.getStackTrace();
                        }
                        gridview.setAdapter(new TableListAdapter(ListTableActivity.this, tableID, count,tableColor,tableStatus,tableID));
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        jsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SparkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }

}
