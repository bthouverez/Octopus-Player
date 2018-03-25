package com.octopus.view;

import com.octopus.R;
import com.octopus.R.id;
import com.octopus.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DefaultMenuAdapter extends BaseAdapter{
	/* Attributs */
	private int nbMenu;
	private String[] menuLib;
	private int[] menuIcon;
	private LayoutInflater inflater;
	
	/* Constructeurs */
	public DefaultMenuAdapter(Context context, String[] mLib, int[] mIcon) {
		inflater = LayoutInflater.from(context);
		this.nbMenu = mLib.length;
		this.menuLib = mLib;
		this.menuIcon = mIcon;
	}
	public DefaultMenuAdapter(Context context, String[] mLib) {
		inflater = LayoutInflater.from(context);
		this.nbMenu = mLib.length;
		this.menuLib = mLib;
		this.menuIcon = null;
	}
	
	/* Methodes publiques */
	public int getCount() {
		return nbMenu;
	}

	public String getItem(int index) {
		if(index>=nbMenu)
			return null;
		else
			return menuLib[index];
	}
	
	public int getIcon(int index) {
		if(index>=nbMenu)
			return -1;
		else
			return menuIcon[index];
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int index, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.adapter_default_menu, null);
			viewHolder.title = (TextView)arg1.findViewById(R.id.adapterDefaultMenuTitle);
			viewHolder.icon = (ImageView)arg1.findViewById(R.id.adapterDefaultMenuIcon);
			arg1.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)arg1.getTag();
		}
		viewHolder.title.setText(getItem(index));
		viewHolder.icon.setImageResource(getIcon(index));
		return arg1;
	}
	private class ViewHolder {
		TextView title;
		ImageView icon;
	}
}