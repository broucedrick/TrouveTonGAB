package com.example.trouvetongab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private ArrayList<String> dataset;
    private Context ctxt;

    public ListAdapter(Context context, ArrayList<String> data){
        dataset = data;
        ctxt = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView img;
        TextView imgname;
        CardView parentLayout;
        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imageView);
            imgname = v.findViewById(R.id.imgName);
            parentLayout = v.findViewById(R.id.parentLayout);
        }
    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, final int position) {
        holder.imgname.setText(dataset.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL_REGISTER = "http://10.0.2.2/androidapp/register.php";
                Toast.makeText(ctxt, dataset.get(position), Toast.LENGTH_SHORT).show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ctxt, "Connection Success..."+response, Toast.LENGTH_SHORT).show();
                                //ctxt.startActivity(new Intent(ctxt, ListGab.class));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ctxt, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user", "cedrick");
                        params.put("user_name", "cedd");
                        params.put("uset_pass", "1234");
                        return params;
                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(ctxt);
                requestQueue.add(stringRequest);

            }

        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
