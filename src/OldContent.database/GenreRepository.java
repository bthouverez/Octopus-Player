package com.octopus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GenreRepository extends Repository {

	// Attributs privés //
	private static final String TAG = "ArtistRepository";
	
	// Constructeurs //
	public GenreRepository(Context context) {
		super(context);
	}
	
	// Methodes publiques //
	public int add(String genreName) {
		Log.d(TAG, "Tentative ajout genre \"" + genreName + "\"...");
		// On cherche si le genre existe bien //
		openDataBase();
		Cursor cursorVerif = dataBase.query("Genre", new String[] { "genreId" }, "genreName=?", new String[] {genreName}, null, null, null, null);
		if(cursorVerif.getCount() != 0) {
			// Il existe bien au moins un genre avec ce nom //
			Log.d(TAG, "Genre "+genreName+" déjà existant");
			if(cursorVerif.getCount() > 1) { // Si il en existe plus d'un //
				Log.w(TAG, "Il existe plusieurs genre avec la même valeur 'genreName'");
			}
			cursorVerif.moveToFirst();
			int result = cursorVerif.getInt(0);
			cursorVerif.close();
			closeDataBase();
			Log.v(TAG, "L'id de "+genreName+" est "+result);
			return result;
		}
		else {
			// Aucun genre ne possede ce nom, on l'insere //
			Log.d(TAG, "Genre inconnu, insertion dans la base");
			ContentValues data = new ContentValues();
			data.put("genreName", genreName);
			long result;
			openDataBase();
			result = dataBase.insert("Genre", null, data);
			Log.i(TAG, "Insertion du genre \""+genreName+"\" avec id=" + result);
			if(result==-1)
				Log.e(TAG, "Erreur d'insertion du genre "+genreName);
			closeDataBase();
			return (int) result;
		}
	}
}
