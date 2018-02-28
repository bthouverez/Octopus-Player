package com.octopus.view;

import java.util.ArrayList;

import com.octopus.R;
import com.octopus.R.id;
import com.octopus.R.layout;
import com.octopus.model.Music;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {
	private static final String TAG = "MUSIC_ADAPTER";
	
	/* Attributs */
	ArrayList<Music> data;
	LayoutInflater inflater;
	Resources resources;
	
	/* Constructeur */
	public MusicAdapter(Context context, ArrayList<Music> arg1) {
		inflater = LayoutInflater.from(context);
		data = arg1;
		resources = context.getResources();
	}
	
	/* Methodes */
	public int getCount() {
		return data.size();
	}

	public Music getItem(int index) {
		return data.get(index);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int index, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.adapter_music, null);
			viewHolder.musicTitle = (TextView)arg1.findViewById(R.id.adapterMusicListTitle);
			viewHolder.musicArtist = (TextView)arg1.findViewById(R.id.adapterMusicListArtist);
			viewHolder.musicAlbum = (TextView)arg1.findViewById(R.id.adapterMusicListAlbum);
			arg1.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)arg1.getTag();
		}
		viewHolder.musicTitle.setText(data.get(index).getTitle());
		viewHolder.musicArtist.setText(data.get(index).getArtist());
		viewHolder.musicAlbum.setText(data.get(index).getAlbum());
		// Gestion des cas ou ces valeurs sont nulles //
		if(data.get(index).getTitle().length()==0)
			viewHolder.musicTitle.setText(resources.getString(R.string.adapterMusicListTitleDefault));
		if(data.get(index).getArtist().length()==0)
			viewHolder.musicArtist.setText(resources.getString(R.string.adapterMusicListTitleDefault));
		if(data.get(index).getAlbum().length()==0)
			viewHolder.musicAlbum.setText(resources.getString(R.string.adapterMusicListTitleDefault));
		return arg1;
	}
	
	/* Classe interne */
	private class ViewHolder {
		TextView musicTitle;
		TextView musicArtist;
		TextView musicAlbum;
	}
}
