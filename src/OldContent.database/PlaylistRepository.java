package com.octopus.database;

import java.util.ArrayList;
import java.util.List;

import com.octopus.model.Music;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlaylistRepository extends Repository {
	
	
	// Attributs privés //
	private static final String TAG = "PlaylistRepository";
	
	// Constructeurs //
	public PlaylistRepository(Context context) {
		super(context);
	}
	// Methodes publiques //
	public int addPlaylist(String name) {
		openDataBase();
		Cursor cursor = dataBase.query("Playlist", new String[] { "playlistId"}, "playlistName=?", new String[] {name}, null, null, null);
		if(cursor.getCount()==0) {
			ContentValues data = new ContentValues();
			data.put("playlistName", name);
			int result;
			result = (int) dataBase.insert("Playlist", null, data);
			cursor.close();
			closeDataBase();
			Log.i(TAG,"Création de la playlist "+name);
			return result;
		}
		else {
			cursor.close();
			closeDataBase();
			Log.e(TAG, "Une playlist portent déjà le nom "+name);
			return -1;
		}
	}
	public List<String> getAllPlaylist() {
		openDataBase();
		Cursor cursor = dataBase.query("Playlist", new String[] { "playlistName"}, null, null, null, null, "playlistName");
		List<String> result = convertCursorToListString(cursor);
		closeDataBase();
		Log.i(TAG,"Chargement de toutes les playlist...");
		return result;
	}
	public void delete(String name) {
		//TODO : supprimer les valeurs dans la table d'association
		openDataBase();
		int result = dataBase.delete("Playlist", "playlistName=?",
				new String[] { name });
		closeDataBase();
		Log.w(TAG, "Suppression de la playlist "+name);
	}
	
	public int getId(String name) {
		// TODO : mix dans la methode add
		openDataBase();
		Cursor cursor = dataBase.query("Playlist", new String[] { "playlistId"}, "playlistName=?", new String[] {name}, null, null, null);
		if(cursor.getCount()!=0) {
			cursor.moveToFirst();
			int result = cursor.getInt(0);
			cursor.close();
			closeDataBase();
			if(cursor.getCount()!=1) {
				// Plusieurs playlist avec le même nom : problème
				Log.w(TAG, "Plusieurs playlist portent le nom "+name);
			}
			return result;
		}
		else {
			// Aucune playlist ne porte ce nom
			cursor.close();
			closeDataBase();
			return -1;
		}
	}
	public boolean addMusicToPlaylist(Music music, String playlistName) {
		// On recupère l'id de la playlist donnée
		int playlistId = getId(playlistName);
		if(playlistId==-1) {
			// Si la playlist n'existe pas on annule
			Log.e(TAG, "Impossible de trouvée la playlist "+playlistName);
			return false;
		}
		int musicId = music.getId();
		openDataBase();
		Cursor cursorVerif = dataBase.query("PlaylistAssoc", new String[] { "playlistId","musicId" }, "playlistId=? AND musicId=?", new String[] {String.valueOf(playlistId),String.valueOf(musicId)}, null, null, null, null);
		if(cursorVerif.getCount() != 0) {
			// Cette musique est déjà présente dans la playlist //
			Log.d(TAG, "Musique "+music.toString()+" déjà existante dans "+playlistName);
			cursorVerif.close();
			closeDataBase();
			return false;
		}
		else {
			// Cette musique n'est pas dans la playlist, on l'ajoute //
			ContentValues data = new ContentValues();
			data.put("playlistId", playlistId);
			data.put("musicId", musicId);
			int result = (int) dataBase.insert("PlaylistAssoc", null, data);
			closeDataBase();
			Log.i(TAG,"Ajout de \""+music.toString()+"\" dans \""+playlistName+"\"");
			return true;
		}
	}
	public void deleteMusic(int playlistId, int musicId) {
		openDataBase();
		int result = dataBase.delete("PlaylistAssoc", "playlistId=? AND musicId=?",
				new String[] { String.valueOf(playlistId),String.valueOf(musicId) });
		closeDataBase();
	}
}
