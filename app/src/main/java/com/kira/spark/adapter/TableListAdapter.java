package com.kira.spark.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kira.spark.ListTableActivity;
import com.kira.spark.MainActivity;
import com.kira.spark.R;

import java.util.HashMap;
import java.util.Map;

public class TableListAdapter extends BaseAdapter {

    String [] tableNo;
    String [] count;
    Integer [] tableColor;
    String []tableStatus;
    String [] inCharge;
    String [] checkInTime;
    Context context;
    private View showDialogView;
    private static LayoutInflater inflater=null;
    public TableListAdapter(ListTableActivity tableActivity, String[] tableNo, String[] count,Integer[] tableColor, String[] tableStatus, String[] checkInTime) {

        this.tableNo = tableNo;
        this.count = count;
        this.tableColor = tableColor;
        this.tableStatus = tableStatus;
        context=tableActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return tableNo.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tableNo;
        ImageView imageID;
        TextView count;

    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.table_grid_view, null);
        holder.tableNo =(TextView) rowView.findViewById(R.id.tablename);
        holder.imageID =(ImageView) rowView.findViewById(R.id.table_images);
        holder.count =(TextView) rowView.findViewById(R.id.count);
        holder.tableNo.setText(tableNo[position]);
        holder.imageID.setImageResource(tableColor[position]);
        holder.count.setText(count[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("A" == tableStatus[position]) {
                    final LayoutInflater li = LayoutInflater.from(context);
                    showDialogView = li.inflate(R.layout.show_dialog, null);
                    final View view = inflater.inflate(R.layout.show_dialog, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(view);
                    alertDialogBuilder.setTitle("You are Booking table " + tableNo[position]);
                    alertDialogBuilder
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    EditText name = (EditText)view.findViewById(R.id.name);
                                    EditText email = (EditText)view.findViewById(R.id.emailid);
                                    EditText phone = (EditText)view.findViewById(R.id.phoneno);
                                    EditText count = (EditText)view.findViewById(R.id.count);
                                    lockTable(name.getText().toString(),email.getText().toString(),phone.getText().toString(),count.getText().toString(),context);
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setCancelable(false)
                            .create();
                    alertDialogBuilder.show();
                }
                else
                {
                    Toast.makeText(context,"",Toast.LENGTH_SHORT);

                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("Table_Name",tableNo[position]);
                    context.startActivity(i);
                }

            }
        });

        return rowView;
    }

    public  void lockTable(final String name, final String email, final String no, final String count, final Context context) {

        Toast.makeText(context,name,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ravikiki.com/api/lockTable.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error",error.getMessage());
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("no", no);
                params.put("count",count);
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




}
