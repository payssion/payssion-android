package com.payssion.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.payssion.android.sdk.PaymentWebActivity;
import com.payssion.android.sdk.Payssion;
import com.payssion.android.sdk.PayssionActivity;
import com.payssion.android.sdk.constant.PPaymentState;
import com.payssion.android.sdk.model.GetDetailRequest;
import com.payssion.android.sdk.model.GetDetailResponse;
import com.payssion.android.sdk.model.PayRequest;
import com.payssion.android.sdk.model.PayResponse;
import com.payssion.android.sdk.model.PayssionResponse;
import com.payssion.android.sdk.model.PayssionResponseHandler;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(this.getClass().getSimpleName(), "onCreate");
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageButton payBtn = (ImageButton)findViewById(R.id.payBtn);
		payBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				double amount = 0.01;
              String currency = "USD";
              String ref = "";
              Intent intent = new Intent(MainActivity.this,
                     PayssionActivity.class);

              intent.putExtra(
                      PayssionActivity.ACTION_REQUEST,
                      new PayRequest()
		                      .setLiveMode(false)//false if you are using sandbox environment
		                      .setAPIKey("916937a82dd7af5a")//Your API Key
		                      .setAmount(amount)
		                      .setCurrency(currency)
		                      .setDescription("Demo Payment")
		                      .setOrderId("123")// your order id
		                      .setSecretKey("demo456")
		                      .setPayerEmail("habertlee@mail.com")
		                      .setPayerRef(ref).setPayerName("habert lee"));
              MainActivity.this.startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(this.getClass().getSimpleName(), "onActivityResult");
		switch (resultCode) {//4009105668
		case PayssionActivity.RESULT_OK:
			if (null != data) {
				PayResponse response = (PayResponse)data.getSerializableExtra(PayssionActivity.RESULT_DATA);
				if (null != response) {
                    String transId = response.getTransactionId(); //get Payssion transaction id
					String orderId = response.getOrderId(); //get your order id
                    //you will have to query the payment state with the transId or orderId from your server
                    //as we will notify you server whenever there is a payment state change

					//you can query in the following way if you don't own a server
					Payssion.getDetail(new GetDetailRequest()
					.setAPIKey("5963a4c1c35c2a8e1")
					.setSecretKey("286a0b747c946e3d902f017cf75d3bd1")
					.setTransactionId(transId)
					.setOrderId(orderId), new PayssionResponseHandler() {
						@Override
						public void onError(int arg0, String arg1,
								Throwable arg2) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(PayssionResponse response) {
							if (response.isSuccess()) {
								GetDetailResponse detail = (GetDetailResponse)response;
								if (null != detail) {
									Toast.makeText(MainActivity.this, detail.getStateStr(), Toast.LENGTH_SHORT).show();
									if (PPaymentState.COMPLETED == detail.getState()) {
										//the payment was paid successfully
									} else if (PPaymentState.FAILED == detail.getState()) {
										//the payment was failed
									} else if (PPaymentState.PENDING == detail.getState()) {
										//the payment was still pending, please save the transId and orderId
										//and recheck the state later as the payment may not be confirmed instantly
									}
								}
							} else {
								Toast.makeText(MainActivity.this, response.getDescription(), Toast.LENGTH_SHORT).show();
							}
						}
					});
				} else {
					//should never go here
				}
			}
			break;
		case PayssionActivity.RESULT_CANCELED:
			Log.v(this.getClass().getSimpleName(), "RESULT_CANCELED"); 
			break;
		case PayssionActivity.RESULT_ERROR:
			if (null != data) {
				String des = data.getStringExtra(PayssionActivity.RESULT_DESCRIPTION);
				Log.v(this.getClass().getSimpleName(), "RESULT_ERROR" + des);   
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v(this.getClass().getSimpleName(), "onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v(this.getClass().getSimpleName(), "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(this.getClass().getSimpleName(), "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(this.getClass().getSimpleName(), "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v(this.getClass().getSimpleName(), "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(this.getClass().getSimpleName(), "onDestroy");
	}
}
