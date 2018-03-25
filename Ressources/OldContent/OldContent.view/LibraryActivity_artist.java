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
import com.octopus.database.ArtistRepository;

public class LibraryActivity_artist extends Activity {
	
	private static final String TAG = "LIBRARY_ARTIST";
	//private OctopusService octopusService;
	private LibraryActivity libraryActivity;
	
	private List<String> listArtist;
	private ListView listView;
	
	Button backButton;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Changement de vue : liste artistes");
		setContentView(R.layout.tablibrary);
		
		listView = (ListView) findViewById(R.id.libraryActivityList);
		
		// Gestion Intance d'activité //
		super.onCreate(savedInstanceState);
		libraryActivity = ((LibraryActivity)this.getParent());
		//octopusService = libraryActivity.getService();
		
		// On recupere tous les artistes //
		ArtistRepository repository = new ArtistRepository(this);
		listArtist = repository.getAll();
		
		// On definit un adapter pour afficher la liste des artistes //		
		ArtistAdapter adapter = new ArtistAdapter(this, listArtist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				// On definit l'artiste actu dans LibraryActivity //
				libraryActivity.setCurrentArtist(listArtist.get(position));
				
				// On crée un Intent avec une valeur attachée : le nom de l'artiste //
				Intent intent = new Intent(l.getContext(),LibraryActivity_album.class);
				// On charge la vue des album de cet artiste //
				libraryActivity.replaceContentView("Albums view", intent);
			}
			
		});
		
		backButton = (Button)findViewById(R.id.button_library);
		backButton.setText(R.string.back);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				libraryActivity.setCurrentArtist(null);
				libraryActivity.onCreate(null);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		/* Interception de l'evenement touche retour pour envoie à LibraryActivity */
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private class ArtistAdapter extends BaseAdapter {
		
		private List<String> data;
		private LayoutInflater inflater;
		
		public ArtistAdapter(Context context, List<String> arg) {
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
