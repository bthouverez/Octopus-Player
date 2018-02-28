package com.octopus.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.octopus.R;
import com.octopus.database.AlbumRepository;

public class LibraryActivity_album extends Activity {
	
	private static final String TAG = "LIBRARY_ALBUM";
	//private OctopusService octopusService;
	private LibraryActivity libraryActivity;
	private List<String> listAlbum;
	
	private ListView listView;
	
	Button backButton;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Changement de vue : liste album");
		
		setContentView(R.layout.tablibrary);
		listView = (ListView) findViewById(R.id.libraryActivityList);
		
		// Gestion Intance d'activité
		super.onCreate(savedInstanceState);
		libraryActivity = ((LibraryActivity)this.getParent());
		//octopusService = libraryActivity.getService();
		
		AlbumRepository albumRepo = new AlbumRepository(this);
		// On verifie si l'utilisateur a choisi un artiste //
		if(libraryActivity.getCurrentArtist() != null)
			listAlbum = albumRepo.getAllFromArtist(libraryActivity.getCurrentArtist());
		else 
			listAlbum = albumRepo.getAll();
		// On crée un adapter pour afficher les album //
		listAlbum.add(0, "Tous les albums");
		AlbumAdapter adapter = new AlbumAdapter(this, listAlbum);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				Intent intent = new Intent(l.getContext(),LibraryActivity_samples.class);
				if(position==0){
					libraryActivity.setCurrentAlbum(null);
				} else {
					libraryActivity.setCurrentAlbum((String)l.getItemAtPosition(position));
				}
				libraryActivity.replaceContentView("Musics view", intent);
			}});
		
		backButton = (Button)findViewById(R.id.button_library);
		backButton.setText(R.string.back);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(libraryActivity.getCurrentArtist()!=null){
					libraryActivity.setCurrentArtist(null);
					Intent intentToArtist = new Intent(getApplicationContext(),LibraryActivity_artist.class);
					libraryActivity.replaceContentView("Artist view", intentToArtist);
					
				} else {
					libraryActivity.onCreate(null);
				}
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		/* Interception de l'evenemennom de domaine monténét touche retour pour envoie à LibraryActivity */
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private class AlbumAdapter extends BaseAdapter {
		
		private List<String> data;
		private LayoutInflater inflater;
		
		public AlbumAdapter(Context context, List<String> arg) {
			inflater = LayoutInflater.from(context);
			this.data = arg;
		}

		public int getCount() {
			return data.size();
		}

		public String getItem(int arg0) {
			return data.get(arg0);
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int index, View arg1, ViewGroup arg2) {
			ViewHolder viewHolder;
			if(arg1 == null) {
				viewHolder = new ViewHolder();
				arg1 = inflater.inflate(R.layout.adapter_default_menu, null);
				viewHolder.title = (TextView)arg1.findViewById(R.id.adapterDefaultMenuTitle);
				arg1.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder)arg1.getTag();
			}
			viewHolder.title.setText(getItem(index));
			return arg1;
		}
		private class ViewHolder {
			TextView title;
			//ImageView icon;
		}		
	}
}
