package com.example.kimboye.fcmtestapplication;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class MainActivity extends AppCompatActivity {

    //BOYE3 BRANCH.
    //boye5 branch

    private static final String TAG="MainActivity";

    RequestQueue queue;

    TextView text;
    EditText editText;
    Button button;

    @Override
    protected void onStop() {
        super.onStop();

        //cancel request
        if(queue != null){
            queue.cancelAll(TAG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue=Volley.newRequestQueue(this);

        text=findViewById(R.id.text);
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String url="http://172.30.1.60:3000/send/boye/" + editText.getText().toString();


                // initialize Request
                StringRequest request =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                });

                // TAG를 붙이는 이유는 onStop()에서 cancel 작업 시 cancel할 수 있도록 하기 위함.
                request.setTag(TAG);

                // add a request to the queue
                queue.add(request);


//                // prepare the Request
//                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // display response
//                                Log.d("Response", response.toString());
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("Error.Response", error.toString());
//                            }
//                        }
//                );
//
//                // add it to the RequestQueue
//                queue.add(getRequest);


                //POST로 보내기
//                final String url = "http://172.30.1.2:3000/send/kim/hihihi";
//                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>()
//                        {
//                            @Override
//                            public void onResponse(String response) {
//                                // response
//                                Log.d("Response", response);
//                            }
//                        },
//                        new Response.ErrorListener()
//                        {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // error
//                                Log.d("Error.Response", error.toString());
//                            }
//                        }
//                ) {
//                    @Override
//                    protected Map<String, String> getParams()
//                    {
//                        Map<String, String>  params = new HashMap<String, String>();
//                        params.put("name", "boye");
//                        params.put("body", editText.getText().toString());
//
//                        return params;
//                    }
//                };
//                queue.add(postRequest);

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
