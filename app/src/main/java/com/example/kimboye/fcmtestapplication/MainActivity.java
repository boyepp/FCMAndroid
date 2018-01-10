package com.example.kimboye.fcmtestapplication;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";

    RequestQueue requestQueue;

    TextView text;
    EditText editText;
    Button button;

    @Override
    protected void onStop() {
        super.onStop();

        //cancel request
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences 저장
        SharedPreferences preferences=getSharedPreferences("OWLER", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("id", "user id");
        editor.putString("pw", "user pw");
        editor.putBoolean("push", true);
        editor.putString("view schedule", "Weekly");
        editor.putString("view child", "Boye");
        editor.putInt("bedge", 0);
        editor.commit();

        //SharedPreferences 불러오기
        String id=preferences.getString("id", "none");
        Toast.makeText(this, "id is " + id, Toast.LENGTH_SHORT).show();


        requestQueue=Volley.newRequestQueue(this);

        text=findViewById(R.id.text);
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String URL="http://172.30.1.5:3000/send";
                    JSONObject jsonBody=new JSONObject();
                    jsonBody.put("name", "boye");
                    jsonBody.put("body", editText.getText().toString());
                    final String requestBody=jsonBody.toString();

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY response", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY error", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString="";
                            if (response != null) {
                                responseString=String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }






//                //POST로 보내기2
//                StringRequest postRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("VOLLEY Response", response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("VOLLEY Error.Response", error.toString());
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params=new HashMap<String, String>();
//                        params.put("name", "boye");
//                        params.put("body", editText.getText().toString());
//
//                        return params;
//                    }
//                };
//                requestQueue.add(postRequest);

            }
        });

        String refreshedToken=FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // 알림센터에서 푸시누르면 Intent에 data담겨있음. 이거 실행됨
        // data Intent에 담겨있음
        if (getIntent().getExtras() != null) {

            String message=getIntent().getExtras().getString("msg");
            text.append(message);

        }


    }


}
