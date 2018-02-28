package com.octopus;

import java.io.File;
import java.util.Vector;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.octopus.database.MusicRepository;
import com.octopus.model.Music;

public class MusicFilesScanne {
	
	// Constantes //
//	private static final ArrayList<String> extAllowed = new ArrayList<String>(
//			Arrays.asList("mp3", "ogg", "wma"));
	
	// Attributs //
	private static final String TAG = "MUSIC_SCANNER";
	private Context context;
//	private File musicFolder;
	private int nbFiles;
	private Vector<Music> vectFiles;
	
	private MusicRepository musicRepo;
	private ProgressDialog progressDialog;
//	private int compteur=0;//juste pour les tests

	// Constructeur //
	MusicFilesScanne(Context cont, String dir, ProgressDialog dialog) {
		vectFiles = new Vector<Music>();
		nbFiles = 0;
//		musicFolder = new File(dir);
		context = cont;
		progressDialog = dialog;
		musicRepo = new MusicRepository(context);
	}
	
	// Methodes publiques //
	public Vector<Music> getVector() {
		return vectFiles;
	}
	public int getNbFiles() {
		return nbFiles;
	}
	public boolean scanneFiles() {
		progressDialog.setIndeterminate(true);
//		// Verification de l'existence du chemin //
//		if (!musicFolder.exists()) {
//			// Tentative de création du chemin //
//			if (!musicFolder.mkdir()) {
//				Log.e(TAG,
//						"Impossible de créé \"" + musicFolder.getAbsolutePath()
//								+ " : " + musicFolder.exists() + "\"");
//				return false;
//			} else {
//				Log.i(TAG,
//						"Création du dossier \""
//								+ musicFolder.getAbsolutePath() + "\"");
//			}
//		}
//		// Verification de la nature du chemin //
//		if (!musicFolder.isDirectory()) {
//			Log.e(TAG, "\"" + musicFolder.getAbsolutePath()
//					+ "\" n'est pas un dossier !");
//			return false;
//		}
//		
//		// Scanne rapide du système de fichier, Vecteur de "path" //
//		recurScann(musicFolder);
//		nbFiles = vectFiles.size();
//		Log.i(TAG, "Scanne système fichier terminé (" + nbFiles + " fichiers)");
//		
//		// On enleve les fichiers déjà analysés du vectFiles //
//		Vector<String> tmpVect = musicRepo.searchNewFiles(vectFiles);
//		vectFiles = tmpVect;
//		nbFiles = vectFiles.size();
//		Log.i(TAG, "Recherche index des fichiers terminé (" + nbFiles + " fichiers à insérer)");
//		
//		// Analyse des resultats du scanne, récupération des informations //
//		if(nbFiles != 0) {
//			Log.i(TAG, "Analyse des " + nbFiles + " fichiers...");
//			analyseFiles();
//			Log.i(TAG, "Analyse terminée");
//			//FIXME : Toast.makeText(context, nbFiles + context.getResources().getString(R.string.toast_new_files_scann), Toast.LENGTH_SHORT);
//		}
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	    final String[] cursor_cols = {
	            MediaStore.Audio.Media._ID,
	            MediaStore.Audio.Media.ARTIST,
	            MediaStore.Audio.Media.ALBUM,
	            MediaStore.Audio.Media.TITLE,
	            MediaStore.Audio.Media.DURATION,
	            MediaStore.Audio.Media.IS_MUSIC,
	            MediaStore.Audio.Media.TRACK,
	            MediaStore.Audio.Media.YEAR,
	            MediaStore.Audio.Media.DATA
	    };
	    final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
	    Log.v(TAG, "Querying the mediastore.");
	    final Cursor cursor = context.getContentResolver().query(uri, cursor_cols, where, null, null);
	    while(cursor.moveToNext()){
	    	String filename = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
		    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
		    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
		    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
		    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
		    String track = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
		    String year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
		    
		    Music music = new Music(
		    		title,
		    		album,
		    		artist, 
		    		filename,
					duration,
					"",
					track,
					year);
		    if(new File(filename).exists()){
			    vectFiles.add(music);
				Log.v(TAG, "Indexed music : " + filename);
		    }
	    }
	    nbFiles = vectFiles.size();
	    vectFiles = musicRepo.updateFilesList(vectFiles);
		// Fin de la fonction scanneFiles //
		return true;
	}
	
	
//	// Methodes privée //
//	private void recurScann(File tmpUnknow) {
//		//if(compteur<20){
//			if (tmpUnknow.isDirectory()) {
//				File[] list = tmpUnknow.listFiles();
//				if (list != null) {
//					for (int i = 0; i < list.length; i++) {
//						recurScann(list[i]);
//					}
//				}
//			} else {
//				// on récupère l'extension du fichier à coup de regex //
//				Pattern p = Pattern.compile(".+\\.(.+)");
//				Matcher m = p.matcher(tmpUnknow.getName());
//				if (m.find()) {
//					String s = m.group(1);
//					/*
//					 * si l'extension est dans la liste des extensions supportées on
//					 * stocke le nom du fichier
//					 */
//					if (extAllowed.contains(s)) {
//						compteur++;
//						vectFiles.add(tmpUnknow.getAbsolutePath());
//						Log.v(TAG, "Fichier indéxé : " + tmpUnknow.getPath());
//					}
//				}
//			}
//		//}
//	}
//
//	private void analyseFiles() {
//		AudioFileIO audioFileIO = null;
//		AudioFile audioFile = null;
//		File file = null;
//
//		// Definition de la valeur maximal pour la boite de dialogue //
//		progressDialog.setIndeterminate(false);
//		progressDialog.setMax(nbFiles);
//		int dialogIndex = 0;
//		
//		// Scanne du vecteur en entrée //
//		for (String path : vectFiles) {
//			audioFileIO = new AudioFileIO();
//			file = new File(path);
//			try {
//				audioFile = audioFileIO.readFile(file);
//				if (file.canRead()) {
//					Tag fileTags = audioFile.getTag();
//					Log.v(TAG,
//							"Fichier analysé : " + file.getPath() + " / "
//									+ fileTags.getFirstTitle() + " / "
//									+ fileTags.getFirstArtist() + " / "
//									+ fileTags.getFirstAlbum());
//					String trackValue = (fileTags.getFirstTrack().length()>0)?fileTags.getFirstTrack():"";
//
//					Music music = new Music(
//							fileTags.getFirstTitle(),
//							fileTags.getFirstAlbum(),
//							fileTags.getFirstArtist(), 
//							file.getAbsolutePath(),
//							audioFile.getLength(),
//							fileTags.getFirstGenre(),
//							fileTags.getFirstTrack(),
//							fileTags.getFirstYear());
//					if(	musicRepo.add(music)==-1 ) {
//						Log.e(TAG,"Impossible d'insérer dans la BD : \""+file.getAbsolutePath()+"\"");
//					}
//					progressDialog.setProgress(dialogIndex);
//					dialogIndex++;
//				} else
//					throw (new CannotReadException());
//			} catch (CannotReadException e) {
//				Log.e(TAG, "Fichier illisible : " + file.getPath());
//				e.printStackTrace();
//			}
//		}
//	}
}
