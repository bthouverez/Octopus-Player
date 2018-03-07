/**
 * Base DAO class
 */

package com.octopus.database;

import java.util.ArrayList;
import java.util.List;

import com.octopus.R;
import com.octopus.model.Music;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class Repository {
	
	// Attributs protégés //
	protected static final String TAG = "Repository";
	protected SQLiteDatabase dataBase;
	protected DataBaseOpenHelper dataBaseOpenHelper;
	protected Resources resources;
	protected Context context;
	
	// Constructeur //
	public Repository(Context context) {
		this.resources = context.getResources();
		this.dataBaseOpenHelper = new DataBaseOpenHelper(context, null);
		this.context = context;
	}
	
	// Methodes Gestion dataBase //
	protected void openDataBase() {
		if(dataBase==null || !dataBase.isOpen()){
			dataBase = dataBaseOpenHelper.getWritableDatabase();
		}
	}
	protected void closeDataBase() {
		if(dataBase!=null && dataBase.isOpen()){
			dataBase.close();
		}
	}
	
	// Methodes de gestion de curseur //
	protected List<String> convertCursorToListString(Cursor cursor) {
		// Attention : gestion des elements avec valeur vide
		List<String> liste = new ArrayList<String>();
		// Si la liste est vide
		if (cursor.getCount() == 0)
			return liste;

		cursor.moveToFirst();
		do {
			if( cursor.getString(0).length()==0 )
				liste.add(resources.getString(R.string.unknown));
			else
				liste.add(cursor.getString(0));
		} while (cursor.moveToNext());
		cursor.close();

		return liste;
	}
	protected List<Integer> convertCursorToListInteger(Cursor cursor) {
		// Attention : gestion des elements avec valeur vide
		List<Integer> liste = new ArrayList<Integer>();
		// Si la liste est vide
		if (cursor.getCount() == 0)
			return liste;

		cursor.moveToFirst();
		do {
			liste.add(cursor.getInt(0));
		} while (cursor.moveToNext());
		cursor.close();

		return liste;
	}
	protected List<Music> convertCursorToListObject(Cursor c) {
		// Gestion des objets Cursor en sortie de SQLiteDataBase.query
		List<Music> liste = new ArrayList<Music>();
		// Si la liste est vide
		if (c.getCount() == 0)
			return liste;
		c.moveToFirst();
		do {
			Music item = convertCursorToObject(c);
			liste.add(item);
		} while (c.moveToNext());
		c.close();
		return liste;
	}
	protected Music convertCursorToObject(Cursor c) {
		Music item = new Music(c.getInt(0), // musicId
				c.getString(1), // musicTitle
				c.getString(2), // artistName
				c.getString(3), // albumName
				c.getString(4), // musicPath
				c.getString(5), // genreName
				c.getInt(6), // musicDuration
				c.getString(7), // musicYear
				c.getString(8), // musicTrack
				c.getString(9) // musicComment
		);
		return item;
	}
}
