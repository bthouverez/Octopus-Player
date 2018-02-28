package com.octopus.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.octopus.OctopusActivity;
import com.octopus.OctopusService;
import com.octopus.R;
import com.octopus.database.MusicRepository;
import com.octopus.database.PlaylistRepository;
import com.octopus.model.Music;

public class PlaylistActivity_samples extends Activity {
	
	private static final String TAG = "PLAYLIST_SAMPLES";
	private OctopusService octopusService;
	private PlaylistActivity playlistActivity;
	private Context context;
	
	private List<Music> listMusic;
	private PlaylistRepository playlistRepo;
	
	// Interface
	Button buttonDelete;
	Button buttonPlay;
	Button buttonBack;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Changement de vue : liste des morceaux");
		
		// Gestion Instance d'activité
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabplaylist_samples);
		playlistActivity = ((PlaylistActivity)this.getParent());
		octopusService = playlistActivity.getService();
		context = this;
		
		// Interface
		buttonPlay = (Button)findViewById(R.id.button_play_playlist);
		buttonPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				octopusService.setCurrentPlaylist(listMusic);
				octopusService.stop();
				octopusService.play();
				((OctopusActivity)playlistActivity.getParent()).switchToTab(0);
			}
		});
		buttonDelete = (Button)findViewById(R.id.button_delete_playlist);
		buttonDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				playlistActivity.deleteCurrentPlaylist();
			}
		});
		buttonBack = (Button)findViewById(R.id.button_back2playlists);
		buttonBack.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				playlistActivity.onCreate(null);
			}
		});
		
		// Chargement des musiques //
		
		// On recupere l'id de la musique
		playlistRepo = new PlaylistRepository(this);
		final int playlistId = playlistRepo.getId(playlistActivity.getCurrentPlaylist());
		// Puis on récupère les musiques associées à cette playlist
		MusicRepository musicRepo = new MusicRepository(this);
		listMusic = new ArrayList<Music>();
		listMusic = musicRepo.getMusicFromPlaylist(playlistId);
		Log.i(TAG,"Affichage des "+listMusic.size()+" musiques...");
		MusicAdapter adapter = new MusicAdapter(this, (ArrayList)listMusic);
		ListView listView = (ListView)findViewById(R.id.list_samples);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				octopusService.setCurrentPlaylist(listMusic);
				octopusService.setCurrentMusic(arg2);
				octopusService.stop();
				octopusService.play();
				((OctopusActivity)playlistActivity.getParent()).switchToTab(0);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				playlistRepo.deleteMusic(playlistId,listMusic.get(arg2).getId());
				onCreate(null);
				Toast.makeText(context, "Musique supprimée de la playlist!", Toast.LENGTH_SHORT).show();
				return true;
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
	

		/*
		octopusService.setCurrentPlaylist(listMusic);
		octopusService.setCurrentMusic(position);
		octopusService.stop();
		octopusService.play();
		((OctopusActivity)libraryActivity.getParent()).switchToTab(0);
		super.onListItemClick(l, v, position, id);
		*/

}
