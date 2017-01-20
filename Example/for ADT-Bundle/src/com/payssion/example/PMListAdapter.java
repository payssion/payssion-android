package com.payssion.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PMListAdapter extends BaseAdapter {
	public static final String PM_ICON_PARTH = "payssion/pm/icon";
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private List<PMItem> mPMItemList = new ArrayList<PMItem>();

	public PMListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public int getCount() {
		return mPMItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPMItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		PMItem pmItem = (PMItem)getItem(position);
		if (null != pmItem) {
			return pmItem.getItemId();
		}
		
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.pmitem,
					null);
			viewHolder = new ViewHolder();
			
			viewHolder.pmLogo =(ImageView) convertView.findViewById(R.id.pm_logo);
			viewHolder.pmName = (TextView) convertView.findViewById(R.id.pm_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PMItem item = (PMItem)this.getItem(position);
		Bitmap logo = getImage(item.getPMId());
		if (null != logo) {
			viewHolder.pmLogo.setImageBitmap(logo);
		}
		viewHolder.pmName.setText(item.getPMName());

		return convertView;
	}

	class ViewHolder {
		ImageView pmLogo;
		TextView pmName;
	}

	public void setDataForRefresh(List<PMItem> pmList) {
		mPMItemList.clear();
		mPMItemList.addAll(pmList);

		notifyDataSetChanged();
	}
	
	private Bitmap getImage(String imageName){
		Bitmap image = null;
		final Options options = new Options();
        options.inDensity = mContext.getResources().getDisplayMetrics().densityDpi;
        options.inScaled = true;
        try {
			image = BitmapFactory.decodeStream(mContext.getAssets().open(
					PM_ICON_PARTH + File.separator + imageName + ".png"), null, options);
        } catch (IOException e) {
			e.printStackTrace();
		}
        try {
        	if(image == null){
     			image = BitmapFactory.decodeStream(mContext.getAssets().open(PM_ICON_PARTH + File.separator + "default.png"), null, options);
     		}
        } catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
//	
//	private void setTitleStyle(View convertView, int size,ViewHolder viewholder, boolean isHistoryBank) {
//		viewholder.titleLayout.setVisibility(View.VISIBLE);
//		String title;
//		if (isHistoryBank) {
//			title = mContext.getResources().getString(R.string.myhistorytransferaccount);
//			viewholder.titleLayout.setBackgroundResource(R.drawable.contact_bigtitle_up);
//		} else {
//			title = mContext.getResources().getString(R.string.mywithdrawaccount);
//			viewholder.titleLayout.setBackgroundResource(R.drawable.contact_bigtitle);
//		}
//		viewholder.titleText.setText(title);
//	}
}
