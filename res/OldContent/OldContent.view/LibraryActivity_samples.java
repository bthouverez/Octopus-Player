package com.octopus.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.octopus.OctopusActivity;
import com.octopus.OctopusService;
import com.octopus.R;
import com.octopus.database.MusicRepository;
import com.octopus.database.PlaylistRepository;
import com.octopus.model.Music;

public class LibraryActivity_samples extends Activity {
	
	// Attributs //
	private static final String TAG = "LIBRARY_ALLSAMPLES";
	private OctopusService octopusService;
	private LibraryActivity libraryActivity;
	private Context context;
	
	// Boite de dialogue addPlaylist
	private AlertDialog.Builder dialogBuilderAddToPlaylist;
	private AlertDialog dialogAddToPlaylist = null;
	private PlaylistRepository playlistRepo; 
	private ArrayList<String> listPlaylist;
	private Music musicToPlaylist = null;
	private MusicRepository musicRepo;
	
	private List<Music> listMusic;
	private ListView listViewDialog;
	
	private ListView listView;
	
	Button backButton;
	
	public void onResume() {
		super.onResume();
		updateMusicList();
		Log.i(TAG, "Chargement de la liste des playlist");
		playlistRepo = new PlaylistRepository(this);
		listPlaylist = (ArrayList<String>)playlistRepo.getAllPlaylist();
		listViewDialog.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listPlaylist));
	}
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Changement de vue : liste de tous les morceaux");
		
		setContentView(R.layout.tablibrary);
		listView = (ListView) findViewById(R.id.libraryActivityList);
		
		// Gestion Instance d'activité
		super.onCreate(savedInstanceState);
		libraryActivity = ((LibraryActivity)this.getParent());
		octopusService = libraryActivity.getService();
		context = libraryActivity.getContext();
		listPlaylist = new ArrayList<String>();
		
		// Création de la boite de dialogue d'ajout de playlist //
		// Le chargement de "listPlaylist" se fait dans la methode onResum
		// Si il existe bien des playlist, on crée une boite de dialogue
		Log.i(TAG, "Création de la boitde de dialogue \"addToPlaylist\"");
		// On crée une vue list à laquelle on fixe un adapter de string
		listViewDialog = new ListView(context);
		listViewDialog.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listPlaylist));
		// On ajoute un ecouteur sur le clique d'une playlists
		listViewDialog.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(playlistRepo.addMusicToPlaylist(musicToPlaylist, listPlaylist.get(position))) {
					Toast.makeText(context, "Musique ajoutée à la playlist", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(context, "Impossible d'ajouter la musique à la playlist", Toast.LENGTH_SHORT).show();
				}
				dialogAddToPlaylist.dismiss();
			}
		});
		dialogBuilderAddToPlaylist = new AlertDialog.Builder(context);
		dialogBuilderAddToPlaylist.setTitle("Nom de la playlist");
		dialogBuilderAddToPlaylist.setView(listViewDialog);
		dialogBuilderAddToPlaylist.setNegativeButton("Annuler",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		dialogAddToPlaylist = dialogBuilderAddToPlaylist.create();
		
		// Gestion des musiques //
		musicRepo = new MusicRepository(this);
		updateMusicList();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				octopusService.setCurrentPlaylist(listMusic);
				octopusService.setCurrentMusic(position);
				octopusService.stop();
				octopusService.play();
				((OctopusActivity)libraryActivity.getParent()).switchToTab(0);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(listPlaylist.size()!=0) {
					musicToPlaylist = listMusic.get(position);
					dialogAddToPlaylist.show();
				}
				else
					Toast.makeText(context, "Aucune playlist disponible", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		
		Resources res = getResources();
		
		backButton = (Button)findViewById(R.id.button_library);
		if(libraryActivity.getCurrentArtist()!=null){
			backButton.setText(res.getText(R.string.back)+" to \""+libraryActivity.getCurrentArtist()+"\" albums");
		} else {
			backButton.setText(R.string.back);
		}
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(libraryActivity.getCurrentArtist()!=null){
					
					libraryActivity.setCurrentAlbum(null);
					Intent intentToAlbum = new Intent(getApplicationContext(),LibraryActivity_album.class);
					libraryActivity.replaceContentView("Album view", intentToAlbum);
					
				} else {
					libraryActivity.onCreate(null);
				}
			}
		});
	}
	
	void updateMusicList(){
		String albumName = libraryActivity.getCurrentAlbum();
		String artistName = libraryActivity.getCurrentArtist();
		if(albumName==null){
			if(artistName==null)
				listMusic = musicRepo.getAllMusic();
			else
				listMusic = musicRepo.getMusicFromArtist(artistName);
		} else {
			if(artistName==null)
				listMusic = musicRepo.getMusicFromAlbum(albumName);
			else
				listMusic = musicRepo.getMusicFromArtistAndAlbum(artistName, albumName);
		}
		
		MusicAdapter adapter = new MusicAdapter(this, (ArrayList<Music>)listMusic);
		listView.setAdapter(adapter);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		/* Interception de l'evenement touche retour pour envoie à LibraryActivity */
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
