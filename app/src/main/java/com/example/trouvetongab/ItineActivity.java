package com.example.trouvetongab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itine);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        //Toast.makeText(ItineActivity.this, url, Toast.LENGTH_LONG).show();

        /*String html = "<style>img{display: inline;height: auto;max-width: 100%;}</style>"+"<iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\""+url+"\" ></iframe>";
*/
        WebView web = (WebView) findViewById(R.id.webview);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadDataWithBaseURL(null, "<html>\n" +
                "    <head>\n" +
                "\n" +
                "    </head>\n" +
                "    <body style=\"padding: 0\">\n" +
                "        <iframe src=\""+url+"\" width=\"100%\" height=\"100%\"/>\n" +
                "    </body>\n" +
                "</html>", "text/html", null, "UTF-8");

        /*String url = "file:///android_asset/website.html";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ItineActivity.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ListGab.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);*/
    }

        /*if(url.contains("iframe")){
            Matcher matcher = Pattern.compile("src=\"([^\"]+)\"").matcher(url);
            matcher.find();
            String src = matcher.group(1);
            url=src;

            try {
                URL myURL = new URL(src);
                web.loadUrl(src);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else {

            web.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + url, "text/html", "UTF-8", null);
        }*/



        /*TextView tv = findViewById(R.id.textView);
        String content = "<iframe src=\""+url+"\" width=\"100%\" height=\"300\" frameborder=\"0\" style=\"border:0;\" allowfullscreen=\"\" aria-hidden=\"false\" tabindex=\"0\"></iframe>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else
            tv.setText(Html.fromHtml(content));
        }*/

}
