package com.kira.spark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kira.spark.adapter.KitchenOrderAdapter;
import com.kira.spark.adapter.ListAllAdapter;
import com.kira.spark.adapter.OrderAdapter;
import com.kira.spark.adapter.SearchAdapter;
import com.kira.spark.bean.Item;
import com.kira.spark.bean.Menu;
import com.kira.spark.util.RecyclerTouchListener;
import com.kira.spark.util.SparkCal;
import com.kira.spark.util.SparkSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView kitchenlist;
    private RecyclerView listallmenu;
    private List<Menu> menuList;
    private List<Menu> menuListAll;
    private List<Item> cartList;
    private List<Item> kitchenList;
    private ListAllAdapter adapter;
    SearchAdapter sAdapter;
    OrderAdapter orderAdapter;
    KitchenOrderAdapter kitchenOrderAdapter;
    ProgressDialog progressDialog;
    private View showDialogView;
    private static LayoutInflater inflater = null;
    AlertDialog.Builder builder;
    TextView qty = null;
    TextView table = null;
    TextView total = null;
    TextView tableVal = null;
    String value;
    Button sendtoKitchen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        kitchenlist = findViewById(R.id.kitchenlist);
        tableVal = findViewById(R.id.tableval);
        progressDialog = new ProgressDialog(this);
        cartList = new ArrayList<>();
        listallmenu = (RecyclerView) findViewById(R.id.listallmenu);

        menuList = new ArrayList<>();
        menuListAll = new ArrayList<>();
        orderAdapter = new OrderAdapter(cartList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);
        qty = (TextView) findViewById(R.id.qty_no);
        total = (TextView) findViewById(R.id.total_no);
        sendtoKitchen = (Button) findViewById(R.id.sendtokitchen);
        Intent mIntent = getIntent();
        value = mIntent.getStringExtra("Table_Name");
        tableVal.setText(value);
        getKitchenOrder(value);
        volleyJsonObjectRequest("http://ravikiki.com/api/getAllMenu.php");
        sendtoKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < cartList.size(); i++) {
                    Log.v("Name", cartList.get(i).getId() + "");
                    Log.v("QTY", cartList.get(i).getQty() + "");
                    sendToKitchen(SparkCal.generateUniqueID(), cartList.get(i).getId() + "null", cartList.get(i).getName(), cartList.get(i).getDescription() + "Desc", cartList.get(i).getPrice().toString(), Integer.toString(cartList.get(i).getQty()), "1", "1", value, "1", getApplicationContext());

                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getKitchenOrder(value);
                        cartList.clear();
                        orderAdapter.notifyDataSetChanged();
                    }
                }, 900);
            }
        });
        adapter = new ListAllAdapter(getApplicationContext(), menuListAll);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listallmenu.setLayoutManager(gridLayoutManager);
        listallmenu.setItemAnimator(new DefaultItemAnimator());
        listallmenu.setAdapter(adapter);

        listallmenu.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), listallmenu, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Menu menu= menuList.get(position);
                List<Item> items = new ArrayList<>();
                Item it = new Item();
                it.setId(menu.getId());
                it.setName(menu.getName());
                it.setDescription(menu.getDesc());
                it.setPrice(menu.getPrice());
                it.setQty(1);
                it.setActualPrice(menu.getPrice());
                it.setThumbnail(menu.getThumbnail());
                items.add(it);
                cartList.addAll(items);

                orderAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), menu.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        inflater = (LayoutInflater) getApplicationContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder = new AlertDialog.Builder(this);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Item item = cartList.get(position);
                List<Item> items = new ArrayList<>();
                Item it = new Item();
                it.setId(item.getId());
                it.setName(item.getName());
                int i = 1;
                it.setQty(item.getQty() + i);
                it.setActualPrice(item.getActualPrice());
                it.setPrice(item.getPrice() + item.getActualPrice());
                items.add(it);
                cartList.remove(position);
                cartList.addAll(items);
                //total.setText(SparkCal.getTotalWithoutTaxPrice(cartList));
                //qty.setText(SparkCal.getQty(cartList));
                orderAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), item.getPrice() + " is selected!", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onLongClick(View view, int position) {


                builder.setMessage("Write your message here.");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder.create();
                alert11.show();

            }
        }));



        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                orderAdapter.removeItem(viewHolder.getAdapterPosition());
                //total.setText(SparkCal.getTotalWithoutTaxPrice(cartList));
                //qty.setText(SparkCal.getQty(cartList));


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
    }


    /**
     * method make volley network call and parses json
     */

    public void volleyJsonObjectRequest(String url) {

        String REQUEST_TAG = "ListTableActivity";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        List<String> list = new ArrayList<>();
                        try {
                            JSONArray array = response.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                Menu menu = new Menu();
                                JSONObject e = array.getJSONObject(i);
                                menu.setId(e.getString("MENU_ID"));
                                menu.setName(e.getString("MENU_NAME"));
                                menu.setPrice(Double.parseDouble(e.getString("PRICE")));
                                menu.setActualPrice(Double.parseDouble(e.getString("PRICE")));
                                menu.setThumbnail(e.getString("URL").replace("\\", ""));
                                menuList.add(menu);
                                menuListAll.add(menu);
                            }
                        } catch (JSONException e) {
                            e.getStackTrace();
                        }

                        final AutoCompleteTextView txtSearch;
                        txtSearch = (AutoCompleteTextView) findViewById(R.id.searchmenu);
                        txtSearch.setThreshold(1);
                        sAdapter = new SearchAdapter(getApplicationContext(), R.layout.row_search_menu, R.id.lbl_name, menuList);
                        txtSearch.setAdapter(sAdapter);
                        progressDialog.hide();

                        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                                Menu menu = menuList.get(arg2);
                                List<Item> items = new ArrayList<>();
                                Item it = new Item();
                                txtSearch.setText("");
                                it.setId(menu.getId());
                                it.setName(menu.getName());
                                it.setDescription(menu.getDesc());
                                it.setPrice(menu.getPrice());
                                it.setQty(1);
                                it.setActualPrice(menu.getPrice());
                                it.setThumbnail(menu.getThumbnail());
                                items.add(it);
                                cartList.addAll(items);

                                orderAdapter.notifyDataSetChanged();

                                //total.setText(SparkCal.getTotalWithoutTaxPrice(cartList));
                                //qty.setText(SparkCal.getQty(cartList));

                            }
                        });

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
        SparkSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    public void sendToKitchen(final String orderID, final String itemID, final String itemName, final String desc, final String price, final String qty, final String specialINS, final String prio, final String tableID, final String regID, final Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ravikiki.com/api/placeOrder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        kitchenOrderAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error", error.getMessage());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ORDER_ID", orderID);
                params.put("ITEM_ID", itemID);
                params.put("ITEM_NAME", itemName);
                params.put("ITEM_DESC", desc);
                params.put("PRICE", price);
                params.put("QTY", qty);
                params.put("SPECIAL_INS", specialINS);
                params.put("PRIORITY", prio);
                params.put("TABLE_ID", value);
                params.put("X_TABLE_REG_ID", regID);
                params.put("STATUS", "0");
                return params;
            }

            @Override

            public RetryPolicy getRetryPolicy() {

                return super.getRetryPolicy();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    public void getKitchenOrder(String tableID) {
        Log.v("Get", "get");
        kitchenList = new ArrayList<>();
        kitchenOrderAdapter = new KitchenOrderAdapter(kitchenList);
        kitchenlist.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getApplicationContext());
        kitchenlist.setLayoutManager(mLayout);
        kitchenlist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        kitchenlist.setItemAnimator(new DefaultItemAnimator());
        kitchenlist.setAdapter(kitchenOrderAdapter);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ravikiki.com/api/getKitchenOrder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject e = array.getJSONObject(i);
                                Log.v("TEST", e.getString("ITEM_DESC"));

                                List<Item> items = new ArrayList<>();
                                Item it = new Item();
                                it.setId(e.getString("ITEM_ID"));
                                it.setName(e.getString("ITEM_NAME"));
                                it.setDescription(e.getString("ITEM_DESC"));
                                it.setPrice(Double.parseDouble(e.getString("PRICE")));
                                it.setQty(Integer.parseInt(e.getString("QTY")));
                                it.setActualPrice(1.0);
                                items.add(it);
                                kitchenList.addAll(items);
                                kitchenOrderAdapter.notifyDataSetChanged();
                                total.setText("Total " + SparkCal.getTotalWithoutTaxPrice(kitchenList));

                            }

                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error", error.getMessage());
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("TABLE_ID", value);
                return params;
            }

            @Override

            public RetryPolicy getRetryPolicy() {

                return super.getRetryPolicy();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
