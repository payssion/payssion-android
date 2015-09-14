package com.payssion.example;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PaymentSelectActivity extends Activity {
	
	private ListView mPMListView;
	private PMListAdapter mPMListAdapter;
	private List<PMItem> mPMList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_paymentselect);
		mPMListView = (ListView) findViewById(R.id.pmList);
		mPMListAdapter = new PMListAdapter(this);
		mPMListView.setAdapter(mPMListAdapter);
		mPMListView
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		
		mPMList = new ArrayList<PMItem>();
		mPMList.add(new PMItem().setItemId(0).setPMId("molpay").setPMName("molpay"));
		mPMList.add(new PMItem().setItemId(0).setPMId("molpay").setPMName("molpay"));
		mPMList.add(new PMItem().setItemId(0).setPMId("molpoints").setPMName("molpoints"));
		mPMList.add(new PMItem().setItemId(0).setPMId("boleto_br").setPMName("Boleto"));
		mPMList.add(new PMItem().setItemId(0).setPMId("bradesco_br").setPMName("Bradesco"));
		mPMList.add(new PMItem().setItemId(0).setPMId("caixa_br").setPMName("Caixa"));
		mPMList.add(new PMItem().setItemId(0).setPMId("santander_br").setPMName("Santander"));
		mPMList.add(new PMItem().setItemId(0).setPMId("hsbc_br").setPMName("HSBC"));
		mPMList.add(new PMItem().setItemId(0).setPMId("bancodobrasil_br").setPMName("Bradesco"));
		mPMList.add(new PMItem().setItemId(0).setPMId("itau_br").setPMName("Itau"));
		mPMList.add(new PMItem().setItemId(0).setPMId("visa_br").setPMName("Bradesco"));
		mPMList.add(new PMItem().setItemId(0).setPMId("mastercard_br").setPMName("Bradesco"));
		mPMList.add(new PMItem().setItemId(0).setPMId("hipercard_br").setPMName("Hipercard"));
		mPMList.add(new PMItem().setItemId(0).setPMId("americanexpress_br").setPMName("American Express"));
		mPMList.add(new PMItem().setItemId(0).setPMId("dinersclub_br").setPMName("Dinersclub"));
		mPMListAdapter.setDataForRefresh(mPMList);
	}
}
