package com.payssion.example;

import java.util.ArrayList;
import java.util.List;

import com.payssion.android.sdk.Payssion;
import com.payssion.android.sdk.PayssionActivity;
import com.payssion.android.sdk.constant.PPaymentState;
import com.payssion.android.sdk.model.PayRequest;
import com.payssion.android.sdk.model.PayResponse;
import com.payssion.android.sdk.model.PayssionResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView mPMListView;
	private PMListAdapter mPMListAdapter;
	private List<PMItem> mPMList;
	
	private View mPMSelectLayout;
	private View mGameLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(this.getClass().getSimpleName(), "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPMSelectLayout = findViewById(R.id.pmSelectLayout);
		mGameLayout = findViewById(R.id.gameLayout);
		
		ImageButton payBtn = (ImageButton)findViewById(R.id.payBtn);
		payBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPMSelectLayout.setVisibility(View.VISIBLE);
				mGameLayout.setVisibility(View.GONE);
			}
		});

		mPMListView = (ListView) findViewById(R.id.pmList);
		mPMListAdapter = new PMListAdapter(this);
		mPMListView.setAdapter(mPMListAdapter);
		mPMListView
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PMItem item = mPMList.get(position);
				String currency = item.getCurrency();
				if (null == currency) {
					currency = "USD";
				}
				double amount = 1;
				if ("MYR" == currency) {
					amount = 5;
				}
				String ref = "";
				String pmid = item.getPMId();
				if ("qiwi" == pmid) {
					ref = "tel:+12819784237";
				} else if (null!= pmid && pmid.endsWith("_br")){
					ref = "00003456789";
				}
				Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
				intent.putExtra(PayssionActivity.ACTION_REQUEST, 
						new PayRequest()
				        .setAPIKey("5963a4c1c35c2a8e")
				        .setAmount(amount)
				        .setCurrency(currency)
				        .setPMId(item.getPMId())
				        .setDescription("test")
				        .setSecretKey("286a0b747c946e3d902f017cf75d3bd1")
				        .setPayerEmail("habertlee@mail.com")
				        .setPayerRef(ref)
				        .setPayerName("habert lee")
				        );
				MainActivity.this.startActivityForResult(intent, 0);
				mPMSelectLayout.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mGameLayout.setVisibility(View.VISIBLE);
						mPMSelectLayout.setVisibility(View.GONE);
					}
				}, 500);
			}
		});
		
		mPMList = new ArrayList<PMItem>();
		mPMList.add(new PMItem().setItemId(0).setPMId("cashu").setPMName("cashu"));
		mPMList.add(new PMItem().setItemId(0).setPMId("onecard").setPMName("onecard"));
		mPMList.add(new PMItem().setItemId(0).setPMId("paysafecard").setPMName("paysafecard"));
		mPMList.add(new PMItem().setItemId(0).setPMId("neosurf").setPMName("neosurf").setCurrency("EUR"));
		mPMList.add(new PMItem().setItemId(0).setPMId("polipayment").setPMName("polipayment").setCurrency("AUD"));
		mPMList.add(new PMItem().setItemId(0).setPMId("cherrycredits").setPMName("cherrycredits"));
		mPMList.add(new PMItem().setItemId(0).setPMId("maybank2u_my").setPMName("maybank2u"));
		mPMList.add(new PMItem().setItemId(0).setPMId("fpx_my").setPMName("fpx"));
		mPMList.add(new PMItem().setItemId(0).setPMId("molpay").setPMName("molpay"));
		mPMList.add(new PMItem().setItemId(0).setPMId("molpoints").setPMName("molpoints"));
		mPMList.add(new PMItem().setItemId(0).setPMId("sofort").setPMName("sofortbanking").setCurrency("EUR"));
		mPMList.add(new PMItem().setItemId(0).setPMId("qiwi").setPMName("qiwi"));
		mPMList.add(new PMItem().setItemId(0).setPMId("boleto_br").setPMName("Boleto"));
		mPMList.add(new PMItem().setItemId(0).setPMId("bradesco_br").setPMName("Bradesco"));
		mPMList.add(new PMItem().setItemId(0).setPMId("caixa_br").setPMName("Caixa"));
		mPMList.add(new PMItem().setItemId(0).setPMId("santander_br").setPMName("Santander"));
		mPMList.add(new PMItem().setItemId(0).setPMId("hsbc_br").setPMName("HSBC"));
		mPMList.add(new PMItem().setItemId(0).setPMId("bancodobrasil_br").setPMName("Bancodobrasil"));
		mPMList.add(new PMItem().setItemId(0).setPMId("itau_br").setPMName("Itau"));
		mPMList.add(new PMItem().setItemId(0).setPMId("visa_br").setPMName("Visa"));
		mPMList.add(new PMItem().setItemId(0).setPMId("mastercard_br").setPMName("Mastercard"));
		mPMList.add(new PMItem().setItemId(0).setPMId("hipercard_br").setPMName("Hipercard"));
		mPMList.add(new PMItem().setItemId(0).setPMId("americanexpress_br").setPMName("American Express"));
		mPMList.add(new PMItem().setItemId(0).setPMId("dinersclub_br").setPMName("Dinersclub"));
		mPMListAdapter.setDataForRefresh(mPMList);	    
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(this.getClass().getSimpleName(), "onActivityResult");
		switch (resultCode) {
		case PayssionActivity.RESULT_OK:
			if (null != data) {
				PayResponse response = (PayResponse)data.getSerializableExtra(PayssionActivity.RESULT_DATA);
				if (null != response) {
					Log.v(this.getClass().getSimpleName(), "RESULT_OK" + response.getTransactionId()); 
					//服务端查询该笔订单状态
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
