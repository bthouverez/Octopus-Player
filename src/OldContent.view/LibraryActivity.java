package com.octopus.view;

import java.util.List;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.octopus.OctopusActivity;
import com.octopus.OctopusService;
import com.octopus.R;
import com.octopus.database.MusicRepository;
import com.octopus.model.Music;

public class LibraryActivity extends ActivityGroup {
	private static final String TAG = "LIBRARY";
	protected static LocalActivityManager localActivityManager;
	private Context context;
	
	private OctopusService octopusService;
	private int currentMenu;
	private String currentArtist;
	private String currentAlbum;
	private ListView listView;
	
	private List<Music> listMusic;
	private MusicRepository musicRepo;
	
	Button LibraryButton;
	
	
	public OctopusService getService() {
		return octopusService;
	}
	public Context getContext() {
		return context;
	}
	
	public int getCurrentMenu() {
		return currentMenu;
	}
	public void setCurrentMenu(int currentMenu) {
		Log.i(TAG,"On rentre dans le menu "+currentMenu);
		this.currentMenu = currentMenu;
	}
	public String getCurrentArtist() {
		return currentArtist;
	}
	public void setCurrentArtist(String currentArtist) {
		Log.i(TAG,"Liste des album de "+currentArtist);
		this.currentArtist = currentArtist;
	}
	public String getCurrentAlbum() {
		return currentAlbum;
	}

	public void setCurrentAlbum(String currentAlbum) {
		Log.i(TAG,"Liste des morceaux de l'album "+currentAlbum);
		this.currentAlbum = currentAlbum;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Gestion du bouton précédent //
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.i(TAG,"Evenement bouton precedent, valeur currentMenu:"+currentMenu+",currentArtist:"+currentArtist+", currentAlbum:"+currentAlbum);
			if (currentAlbum!=null){
				currentAlbum=null;
				Intent intentToAlbum = new Intent(getApplicationContext(),LibraryActivity_album.class);
				replaceContentView("Album view", intentToAlbum);
				return true;
			} else if(currentArtist!=null) {
				// Si on est actuellement 
				currentArtist = null;
				Intent intentToArtist = new Intent(getApplicationContext(),LibraryActivity_artist.class);
				replaceContentView("Artist view", intentToArtist);
				return true;
			} else if(currentMenu!=0) {
				onCreate(null);
				return true;
			}		        
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		/* Gestion Intance d'activité */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablibrary);
		context = this;
		Log.i(TAG, "Lancement onglet library");
		
		listView = (ListView) findViewById(R.id.libraryActivityList);
		
		currentArtist = null;
		currentAlbum = null;
		currentMenu = 0;
		
		/* Liaison avec le service */
		Log.i(TAG, "Demande de connexion au service");
		octopusService = ((OctopusActivity) this.getParent()).getService();
		
		Resources res = getResources();
		
		/* Création des libellé de menu et des icons associés */
		String[] lib = new String[] { (String)res.getText(R.string.artists), (String)res.getText(R.string.albums), (String)res.getText(R.string.tracks) };
		int[] icons = new int[] { R.drawable.ic_tab_library_selected,
				R.drawable.ic_tab_modules_selected,
				R.drawable.ic_tab_player_selected };
		/* Création d'un adapter avec ces données */
		DefaultMenuAdapter adapter = new DefaultMenuAdapter(this, lib, icons);
		/* Envoie de l'adapter à la vue courante */
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent;
				switch (arg2) {
				case 0:
					currentMenu = 1;
					intent = new Intent(arg0.getContext(),
							LibraryActivity_artist.class);
					replaceContentView("Artists view", intent);
					break;
				case 1:
					currentMenu = 2;
					intent = new Intent(arg0.getContext(),
							LibraryActivity_album.class);
					replaceContentView("Albums view", intent);
					break;
				case 2:
					currentMenu = 3;
					intent = new Intent(arg0.getContext(),
							LibraryActivity_samples.class);
					replaceContentView("Samples view", intent);
					break;
				default:
					break;
				}
			}
		});
		musicRepo = new MusicRepository(this);
		
		LibraryButton = (Button)findViewById(R.id.button_library);
		LibraryButton.setText(R.string.playAll);
		LibraryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				updateMusicList();
				octopusService.setCurrentPlaylist(listMusic);
				octopusService.stop();
				octopusService.play();
				((OctopusActivity)getParent()).switchToTab(0);
			}
		});
	}
	public void replaceContentView(String id, Intent newIntent) {
		View view = getLocalActivityManager().startActivity(id,
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		this.setContentView(view);
	}
	void updateMusicList(){
		listMusic = musicRepo.getAllMusic();
	}
}