package com.hang.doan.readbooks.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hang.doan.readbooks.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class PaymentActivity extends Activity {
    TextView payment_storyname;
    TextView payment_storychaptername;
    TextView payment_storychapterprice;
    Button paymomo;

    private String merchantName = "Minh Hang";
    private String merchantCode = "MOMOXNTR20200418";
    private String description = "Thanh toan truyen";

    String URL_BASE = "https://us-central1-doan20192-33247.cloudfunctions.net/addMessage";

    String storyID;
    String storyName;
    String userID;
    String storyChapterName;
    int storyChapter;
    int storyChapterPrice;



    String TAG = "HANG_DEBUG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payment_storyname = findViewById(R.id.payment_storyname);
        payment_storychaptername = findViewById(R.id.payment_storychaptername);
        payment_storychapterprice = findViewById(R.id.payment_storychapterprice);
        paymomo = findViewById(R.id.btnPayMoMo);
        paymomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });


        Bundle data = getIntent().getExtras();

        storyID = data.getString("storyID");
        storyName = data.getString("storyName");
        userID = data.getString("userID");
        storyChapter = data.getInt("storyChapter");
        storyChapterName = data.getString("storyChapterName");
        storyChapterPrice = data.getInt("storyChapterPrice");


        payment_storyname.setText(storyName);
        payment_storychaptername.setText(storyChapterName);
        payment_storychapterprice.setText(String.valueOf(storyChapterPrice));

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

    }


    //example payment
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);


        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, storyChapterPrice);
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);

        //client Optional
//        eventValue.put(MoMoParameterNamePayment.FEE, fee);
//        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);
//
//        eventValue.put(MoMoParameterNamePayment.REQUEST_ID,  merchantCode+"-"+ UUID.randomUUID().toString());
//        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, "MH001");


        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("storyID", storyID);
            objExtraData.put("storyName", storyName);
            objExtraData.put("storyChapter", storyChapter);
            objExtraData.put("storyChapterName", storyChapterName);
            objExtraData.put("price", storyChapterPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));

                    if(data.getStringExtra("data") != null && !data.getStringExtra("data").equals("")) {
                        // TODO:
//                        int status = data.getIntExtra("status",0 );
//                        Log.d(TAG, "" + status);
//                        Log.d(TAG, data.getStringExtra("message") );
//                        Log.d(TAG, data.getStringExtra("phonenumber") );
//                        Log.d(TAG, data.getStringExtra("data") );

                        postData();

                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                        Toast.makeText(this, "message: " + this.getString(R.string.not_receive_info), Toast.LENGTH_SHORT).show();
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
//                    tvMessage.setText("message: " + message);
                    Toast.makeText(this, "message: " + message, Toast.LENGTH_SHORT).show();

                } else if(data.getIntExtra("status", -1) == 2) {
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    Toast.makeText(this, "message: " + this.getString(R.string.not_receive_info), Toast.LENGTH_SHORT).show();

                } else {
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    Toast.makeText(this, "message: " + this.getString(R.string.not_receive_info), Toast.LENGTH_SHORT).show();

                }
            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                Toast.makeText(this, "message: " + this.getString(R.string.not_receive_info), Toast.LENGTH_SHORT).show();

            }
        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
            Toast.makeText(this, "message: " + this.getString(R.string.not_receive_info_err), Toast.LENGTH_SHORT).show();

        }
    }

    // Post Request For JSONObject
    public void postData() {

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("storyID", storyID);
            object.put("storyChapter", storyChapter);
            object.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_BASE, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(Login_screen.this,"String Response : "+ response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            Log.d(TAG, String.valueOf(response));
                            String Error = response.getString("httpStatus");
                            if (Error.equals("")||Error.equals(null)){

                            }else if(Error.equals("OK")){
                                JSONObject body = response.getJSONObject("body");

                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        resultTextView.setText("String Response : "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}
