package com.octopus.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ArtistRepository extends Repository {
	
	// Attributs privés //
	private static final String TAG = "ArtistRepository";
	
	// Constructeurs //
	public ArtistRepository(Context context) {
		super(context);
	}
	
	// Methodes publiques //
	
	public List<String> getAll() {
		openDataBase();
		Cursor cursor = dataBase.query("Artist", new String[] { "artistName" },
				null, null, null, null, "artistName");
		List<String> result = convertCursorToListString(cursor);
		closeDataBase();
		return result;
	}
	public int add(String artistName) {
		Log.d(TAG, "Tentative ajout artiste \"" + artistName + "\"...");
		// On cherche si l'artiste existe bien //
		openDataBase();
		Cursor cursorVerif = dataBase.query("Artist", new String[] { "artistId" }, "artistName=?", new String[] {artistName}, null, null, null, null);
		if(cursorVerif.getCount() != 0) {
			// Il existe bien au moins un artiste avec ce nom //
			Log.d(TAG, "Artiste "+artistName+" déjà existant");
			if(cursorVerif.getCount() > 1) { // Si il en existe plus d'un //
				Log.w(TAG, "Il existe plusieurs artistes avec la même valeur 'artistName'");
			}
			cursorVerif.moveToFirst();
			int result = cursorVerif.getInt(0);
			cursorVerif.close();
			closeDataBase();
			Log.v(TAG, "L'id de "+artistName+" est "+result);
			return result;
		}
		else {
			// Aucun artiste ne possede ce nom, on l'insere //
			Log.d(TAG, "Artist inconnu, insertion dans la base");
			ContentValues data = new ContentValues();
			data.put("artistName", artistName);
			long result;
			openDataBase();
			result = dataBase.insert("Artist", null, data);
			Log.i(TAG, "Insertion de l'artiste \""+artistName+"\" avec id=" + result);
			if(result==-1)
				Log.e(TAG, "Erreur d'insertion de l'artiste "+artistName);
			closeDataBase();
			return (int) result;
		}
	}

}
