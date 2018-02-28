package com.octopus;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.octopus.database.MusicRepository;
import com.octopus.model.Music;
import com.octopus.view.PlayerActivity;

public class OctopusService extends Service {
	
	// * VARIABLE DE PARPAMETRE * //
	private boolean loopPlaylist = false;
	private boolean randomPlaylist = false;
	// Attributs //
	private static final String TAG = "OCTO_SERVICE";
	// Création de l'objet Binder du service
	private final IBinder octoBinder = new OctopusServiceBinder();
	// L'objet contenant les music
	private MediaPlayer mediaPlayer;
	// Variable d'etats du player
	private boolean isPlaying;
	private boolean isPaused;
	// Gestion de la playlist actu
	private List<Music> currentPlaylist;
	private ListIterator<Music> musicIterator;
	private Music currentMusic;
	private MusicRepository musicRepo;
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	public void setLoopPlaylist(boolean loopPlaylist) {
		this.loopPlaylist = loopPlaylist;
	}
	public void setRandomPlaylist(boolean randomPlaylist) {
		this.randomPlaylist = randomPlaylist;
		if(currentPlaylist!=null)
		{
			if(randomPlaylist){
				Collections.shuffle(currentPlaylist);
			} else {
				Collections.sort(currentPlaylist);
			}
		}
	}
	// Methodes liées à la gestion du service //
	@Override
	public void onCreate() {
		Log.i(TAG, "Service lancé");
		
		// Initialisation des variables du service //
		
		// Variables d'etat du player
		isPlaying = false;
		isPaused = false;
		// currentPlaylist
		musicRepo=new MusicRepository(this);
		currentPlaylist = musicRepo.getAllMusic();
		this.musicIterator = currentPlaylist.listIterator(0);
		currentMusic = musicIterator.next();
		mediaPlayer = new MediaPlayer();
	}
	@Override
	public IBinder onBind(Intent arg) {
		return octoBinder;
	}
	public class OctopusServiceBinder extends Binder {
		OctopusService getService() {
			return OctopusService.this;
		}
	}
	
	// Methodes liées à la gestion de la playlist actuelle //
	public void setCurrentPlaylist(List<Music> listMusic) {
		if(!listMusic.isEmpty()) {
			stop();
			this.currentPlaylist = listMusic;
			if(randomPlaylist){
				Collections.shuffle(listMusic);
			}
			this.musicIterator = currentPlaylist.listIterator(0);
			this.currentMusic = musicIterator.next();
		}
		else {
			Log.e(TAG, "Erreur, la playlist envoyé au service est vide !");
		}
	}
	public void setCurrentMusic(int index) {
		if(index<currentPlaylist.size()) {
			musicIterator = currentPlaylist.listIterator(index);
			currentMusic = musicIterator.next();
		}
		else {
			musicIterator = currentPlaylist.listIterator(0);
			currentMusic = musicIterator.next();
		}
	}
	public Music getCurrentMusic() {
		return currentMusic;
	}
	// Methodes liées à la lecture de musiques (reception d'evenement de PlayerActivity principalement)
	public boolean play() {
		Log.i(TAG,"Action utilisateur : PLAY");	
		// Gestion du cas : rien dans currentPlaylist 
		if(currentPlaylist.size()==0) {
			Log.w(TAG,"Aucune musique dans currentPlayList, impossible de lire");
			Toast.makeText(this, "Aucune musique choisie !", Toast.LENGTH_SHORT).show();
			return false;
		}
		// Gestion du cas : le player est en pause, on relance
		if(isPaused) {
			Log.d(TAG,"Le service est en pause, on relance");
			isPaused = false;
			isPlaying = true;
			mediaPlayer.start();
		}
		// Le service n'est pas en train de jouer une musique, lancement de la musique actuel
		if(!isPlaying) {
			Log.d(TAG,"Le service ne fait rien, on lance");
			if(!new File(currentMusic.getPath().getPath()).exists()){
				musicRepo.delete(currentMusic.getId());
				return next();
			}
			playFile(currentMusic.getPath());
			isPlaying = true;
		}
		return true;
	}
	public boolean pause() {
		Log.i(TAG,"Action utilisateur : PAUSE");
		if(isPlaying) {
			Log.d(TAG,"Le service est en lecture, on met en pause");
			isPlaying = false;
			isPaused = true;
			mediaPlayer.pause();
			return true;
		}
		return false;
	}
	public boolean stop() {
		Log.i(TAG,"Action utilisateur : STOP");
		isPaused = false;
		if(isPlaying){
			isPlaying = false;
			mediaPlayer.stop();
			return true;
		}
		return false;
	}
	public boolean next(){
		Log.i(TAG,"Action utilisateur : NEXT");
		if(currentPlaylist.size()!=0) {
			// On commence par stoper la lecture courante
			stop();
			if(musicIterator.hasNext()) {
				Music temp = currentMusic;
				Log.i(TAG,"Musique Courante : "+currentMusic.toString()+" | appel du suivant...");
				// On verifie qu'il existe bien un element apres celui ci, si oui on le lance
				//FIXME : Il est parfois nécessaire d'appeller 2 fois la methode pour avoir le suivant 
				currentMusic = musicIterator.next();
				Log.i(TAG,"Musique Courante : "+currentMusic.toString()+"");
				if(currentMusic!=temp){
					play();
					return true;
				} else
				{
					return next();
				}
			}
			else {
				// On a atteint la derniere musique de la liste, on remet l'index à 0
				Log.w(TAG, "Fin de currentPlaylist atteint");
				stop();
				musicIterator = currentPlaylist.listIterator(0);
				currentMusic = currentPlaylist.get(0);
				if(loopPlaylist) {
					play();
					return true;
				} else	{
					return false;
				}
			}
		}
		else {
			Log.w(TAG,"currentPlaylist vide");
			return false;
		}
	}
	public boolean prec(){
		Log.i(TAG,"Action utilisateur : PREC");
		if(currentPlaylist.size()!=0) {
			// On stop la lecture courante
			stop();
			// On verifie qu'il existe bien un element avant celui ci, si oui on le lance
			if(musicIterator.hasPrevious()) {
				Music temp = currentMusic;
				Log.i(TAG,"Musique Courante : "+currentMusic.toString()+" | appel du precedent...");
				// On verifie qu'il existe bien un element avant celui ci, si oui on le lance
				//FIXME : Il est parfois nécessaire d'appeller 2 fois la methode pour avoir le precedent
				currentMusic = musicIterator.previous();
				Log.i(TAG,"Musique Courante : "+currentMusic.toString()+"");
				if(currentMusic!=temp){
					play();
					return true;
				} else
				{
					return prec();
				}
			}
			else {
				// On a atteint la premire musique de la liste, on remet l'index à 0
				Log.w(TAG,"Fin de currentPlaylist atteint");
				if(loopPlaylist) {
					musicIterator = currentPlaylist.listIterator(currentPlaylist.size()-1);
					currentMusic = currentPlaylist.get(currentPlaylist.size()-1);
					play();
					return true;
				}
				else {
					musicIterator = currentPlaylist.listIterator(0);
					stop();
					return false;
				}
			}
		}
		else {
			Log.w(TAG,"currentPlaylist vide");
			return false;
		}
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public boolean isPaused() {
		return isPaused;
	}
	
	// Attention : aucune gestion de l'etat du player ici
	private int playFile(Uri uri) {
		Log.i(TAG,"Lancement music "+uri.getLastPathSegment());		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(getApplicationContext(), uri);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Erreur playFile, arguments incorrect");
			e.printStackTrace();
			return 1;
		} catch (SecurityException e) {
			Log.e(TAG, "Erreur playFile, security");
			e.printStackTrace();
			return 1;
		} catch (IllegalStateException e) {
			Log.e(TAG, "Erreur playFile, l'etat actuel pose probleme");
			e.printStackTrace();
			return 1;
		} catch (IOException e) {
			Log.e(TAG, "Erreur playFile, entrée/sortie");
			e.printStackTrace();
			return 1;
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			Log.e(TAG, "Erreur playFile, l'etat actuel pose probleme");
			e.printStackTrace();
			return 1;
		} catch (IOException e) {
			Log.e(TAG, "Erreur playFile, entrée/sortie");
			e.printStackTrace();
			return 1;
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				Log.i(TAG, "End of current track reached. Go to next.");
				Log.i(TAG, "Go to next state : "+(next() ? "1" : "0"));
				PlayerActivity.getInstance().updateMusicValues();
			}
		});
		mediaPlayer.start();
		return 0;
	}
}
