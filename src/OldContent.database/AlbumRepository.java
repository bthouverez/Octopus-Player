package com.octopus.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlbumRepository extends Repository {
	
	// Attributs privés //
	private static final String TAG = "AlbumRepository";
	
	// Constructeurs //
	public AlbumRepository(Context context) {
		super(context);
	}
	
	// Methodes publiques //
	public List<String> getAll() {
		openDataBase();
		Cursor cursor = dataBase.query("Album", new String[] { "albumName" },
				null, null, null, null, "albumName");
		List<String> result = convertCursorToListString(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}
	
	public List<String> getAllFromArtist(String artistName) {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView",
				new String[] { "DISTINCT albumName" }, "artistName='"
						+ artistName + "'", null, null, null, "albumName");
		List<String> result = convertCursorToListString(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}
	
	public int add(String albumName) {
		Log.d(TAG, "Tentative ajout album \"" + albumName + "\"...");
		// On cherche si l'album existe bien //
		openDataBase();
		Cursor cursorVerif = dataBase.query("Album", new String[] { "albumId" }, "albumName=?", new String[] {albumName}, null, null, null, null);
		if(cursorVerif.getCount() != 0) {
			// Il existe bien au moins un album avec ce nom //
			Log.d(TAG, "Album "+albumName+" déjà existant");
			if(cursorVerif.getCount() > 1) { // Si il en existe plus d'un //
				Log.w(TAG, "Il existe plusieurs album avec la même valeur 'albumName'");
			}
			cursorVerif.moveToFirst();
			int result = cursorVerif.getInt(0);
			cursorVerif.close();
			closeDataBase();
			Log.v(TAG, "L'id de "+albumName+" est "+result);
			return result;
		}
		else {
			// Aucun album ne possede ce nom, on l'insere //
			Log.d(TAG, "Album inconnu, insertion dans la base");
			ContentValues data = new ContentValues();
			data.put("albumName", albumName);
			long result;
			openDataBase();
			result = dataBase.insert("Album", null, data);
			Log.i(TAG, "Insertion de l'album \""+albumName+"\" avec id=" + result);
			if(result==-1)
				Log.e(TAG, "Erreur d'insertion de l'album "+albumName);
			closeDataBase();
			return (int) result;
		}
	}

}
