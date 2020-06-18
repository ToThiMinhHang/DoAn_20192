package com.hang.doan.readbooks.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hang.doan.readbooks.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class PaymentActivity extends Activity {
    TextView payment_storyname;
    TextView payment_storychaptername;
    TextView payment_storychapterprice;
    Button paymomo;

    private String merchantName = "App đọc truyện Minh Hằng";
    private String merchantCode = "MOMOXNTR20200418";
    private String description = "Thanh toan truyen\n";

    String ADDMSG_URL = "https://us-central1-doan20192-33247.cloudfunctions.net/addMessage";
    String MERCHAN_URL = "https://secret-taiga-14580.herokuapp.com/payment";
    //String MERCHAN_URL = "https://us-central1-doan20192-33247.cloudfunctions.net/payment/payment";
    //String MERCHAN_URL = "http://localhost:5000/doan20192-33247/us-central1/payment/payment";

    String storyID;
    String storyName;
    String userID;
    String storyChapterName;
    int storyChapter;
    int storyChapterPrice;


    String recv_message;
    String recv_phonenumber;
    String recv_data;

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
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description + "\"" + storyName + "\"\n" + "Chương " + storyChapter);

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
                        int status = data.getIntExtra("status",0 );
                        Log.d(TAG, "data:  " + data.getStringExtra("data"));
                        recv_message = data.getStringExtra("message") ;
                        recv_phonenumber = data.getStringExtra("phonenumber") ;
                        recv_data =  data.getStringExtra("data") ;

                        if(status == 0) {
                            sendToMerchanServer();
                        }

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
    public void sendToMerchanServer() {

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("orderId", merchantName + "_" + storyID + "_" + storyChapter);
            //object.put("orderId", merchantName + "_1");
            object.put("customerNumber", recv_phonenumber);
            object.put("data", recv_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MERCHAN_URL, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(Login_screen.this,"String Response : "+ response.toString(),Toast.LENGTH_LONG).show();
                        Log.d(TAG, "sendToMerchanServer onResponse: " + response.toString());
                        //postData();
//                        Intent intent = new Intent(PaymentActivity.this, ReadBook.class);
//                        intent.putExtra("INDEX", String.valueOf(storyChapter));
//                        intent.putExtra("id_tac_pham", storyID);
//                        startActivityForResult(intent, 0);
//                        onBackPressed();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADDMSG_URL, object,
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
