package com.octopus.view;

import java.util.ArrayList;
import java.util.List;

import com.octopus.OctopusActivity;
import com.octopus.OctopusService;
import com.octopus.R;
import com.octopus.R.layout;
import com.octopus.database.PlaylistRepository;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaylistActivity extends ActivityGroup {

	// Attributs //
	private static final String TAG = "PLAYLIST";
	private OctopusService octopusService;
	protected static LocalActivityManager localActivityManager;
	
	private Context context;
	private PlaylistRepository playlistRepo;
	// Interface vue playlist
	private Button buttonAddPlaylist;
	private ListView listView;
	// Boite de dialogue addPlaylist
	private AlertDialog.Builder dialogAddPlaylist;
	private EditText editTextPlaylistName;
	// Attribut permettant de savoir ou se trouve l'utilisateur dans le ActivityGroup
	private String currentPlaylist;
	private ArrayList<String> listPlaylist;
	// Methodes //
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Lancement onglet playlist");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabplaylist);
		/* Liaison avec le service */
		Log.i(TAG, "Demande de connexion au service");
		octopusService = ((OctopusActivity) this.getParent()).getService();
		
		context = this;
		currentPlaylist = null;
		
		// Création du Repository //
		playlistRepo = new PlaylistRepository(context);
		
		// Insertion des données dans la ListView list_playlist //
		listView = (ListView) findViewById(R.id.list_playlist);
		listPlaylist = new ArrayList<String>();
		listPlaylist = (ArrayList<String>) playlistRepo.getAllPlaylist();
		PlaylistAdapter adapter = new PlaylistAdapter(this, listPlaylist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				currentPlaylist = listPlaylist.get(arg2);
				Intent intentToSamples = new Intent(context,PlaylistActivity_samples.class);
				replaceContentView("Playlist_samples view", intentToSamples);
			}
		});
		
		// Création de la boite de dialogue d'ajout de playlist //
		dialogAddPlaylist = new AlertDialog.Builder(this);
		editTextPlaylistName = new EditText(this);
		dialogAddPlaylist.setTitle("Nom de la playlist");
		dialogAddPlaylist.setView(editTextPlaylistName);
		dialogAddPlaylist.setPositiveButton("Valider",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = editTextPlaylistName.getText()
								.toString().trim();
						if(playlistRepo.addPlaylist(value)!=-1)
							Toast.makeText(context, "Playlist \""+value+"\" ajoutée !", Toast.LENGTH_SHORT).show();
						else 
							Toast.makeText(context, "La playlist \""+value+"\" existe déjà", Toast.LENGTH_SHORT).show();
						// On recharge l'interface Playlist (pas tres jolie )
						//TODO : mettre a jour la vue playlist par un autre moyen
						onCreate(null);
					}
				});
		dialogAddPlaylist.setNegativeButton("Annuler",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		// Recuperation du bouton addplaylist //
		buttonAddPlaylist = (Button)findViewById(R.id.button_addplaylist);
		buttonAddPlaylist.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialogAddPlaylist.show();
			}
		});
	}
	
	// Gestion de l'adapter pour afficher la liste des Playlist //
	private class PlaylistAdapter extends BaseAdapter {

		private List<String> data;
		private LayoutInflater inflater;

		public PlaylistAdapter(Context context, List<String> arg) {
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
			if (arg1 == null) {
				viewHolder = new ViewHolder();
				arg1 = inflater.inflate(R.layout.adapter_default_menu, null);
				viewHolder.title = (TextView) arg1
						.findViewById(R.id.adapterDefaultMenuTitle);
				arg1.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
			}
			viewHolder.title.setText(getItem(index));
			return arg1;
		}

		private class ViewHolder {
			TextView title;
			ImageView icon;
		}
	}
	public void deleteCurrentPlaylist() {
		playlistRepo.delete(currentPlaylist);
		onCreate(null);
		Toast.makeText(context, "Playlist supprimée !", Toast.LENGTH_SHORT).show();
	}
	public String getCurrentPlaylist() {
		return currentPlaylist;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Gestion du bouton précédent //
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.i(TAG, "Evenement bouton precedent");
			if (currentPlaylist!=null) {
				onCreate(null);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	public void replaceContentView(String id, Intent newIntent) {
		View view = getLocalActivityManager().startActivity(id,
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		this.setContentView(view);
	}
	public OctopusService getService() {
		return octopusService;
	}
}
