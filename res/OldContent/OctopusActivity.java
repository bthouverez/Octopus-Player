package com.octopus;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.octopus.view.LibraryActivity;
import com.octopus.view.ModulesActivity;
import com.octopus.view.PlayerActivity;
import com.octopus.view.PlaylistActivity;

public class OctopusActivity extends TabActivity {
	/* ATTENTION : A FAIRE */
	// Préférences (temporairement instanciées)
	private final String musicPath = "music";
	
	// Tag d'activité
	private static final String TAG = "MAIN";
	
	/* Attributs */
	private TabHost octoTabHost;
	private TabSpec[] tabSpec;
	// Boite de dialogue du scanner
	private ProgressDialog progressDialog;
//	private static final int PROGRESS_DIALOG = 0;
	private MusicFilesScanne filesScanner;
	
	/* Lien vers le service pour les activité autre que player */
	private OctopusService octopusService;
	public OctopusService getService() {
		return octopusService;
	}
	
	/* Gestion des connexions au service par l'activité principal et par le player */
	private ServiceConnection octoConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			octopusService = ((OctopusService.OctopusServiceBinder)service).getService();
			/* Gestion de ActivityPlayer différente des autres activités car player déjà lancé lors du démarrage du service */
			playerActivity.linkService(octopusService);
			Log.i(TAG, "Connexion au service ok");
		}
		public void onServiceDisconnected(ComponentName className) {
			octopusService = null;
			Log.i(TAG, "Déconnexion du service");
		}
	};
	/* Reception de PlayerActivity */
	private PlayerActivity playerActivity;
	private ModulesActivity modulesActivity;
	public void registerPlayerActivity(PlayerActivity arg) {
		Log.i(TAG, "Reception du lien PlayerActivity");
		playerActivity = arg;
	}
	public void registerModulesActivity(ModulesActivity arg) {
		Log.i(TAG, "Reception du lien PlayerActivity");
		modulesActivity = arg;
		modulesActivity.linkService(octopusService);
	}
	public void switchToTab(int id){
		octoTabHost.setCurrentTab(id);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Restauration etat activité
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i(TAG, "Lancement de Octopus");
		
		/* AFFICHAGE DES ONGLETS */
		Resources res = getResources();
		// Recuperation du layout
		octoTabHost = getTabHost();
		
		tabSpec = new TabSpec[4];
		// Onglet Player
		tabSpec[0] = octoTabHost.newTabSpec("player");
		tabSpec[0].setIndicator(res.getText(R.string.player_activity_name),res.getDrawable(R.drawable.ic_tab_player));
		tabSpec[0].setContent(new Intent(OctopusActivity.this,PlayerActivity.class));
		octoTabHost.addTab(tabSpec[0]);
		
		// Onglet Playlist
		tabSpec[1] = octoTabHost.newTabSpec("playlist");
		tabSpec[1].setIndicator(res.getText(R.string.playlist_activity_name),res.getDrawable(R.drawable.ic_tab_playlist));
		tabSpec[1].setContent(new Intent(OctopusActivity.this,PlaylistActivity.class));
		octoTabHost.addTab(tabSpec[1]);
		
		// Onglet Library
		tabSpec[2] = octoTabHost.newTabSpec("library");
		tabSpec[2].setIndicator(res.getText(R.string.library_activity_name),res.getDrawable(R.drawable.ic_tab_library));
		tabSpec[2].setContent(new Intent(OctopusActivity.this,LibraryActivity.class));
		octoTabHost.addTab(tabSpec[2]);
		
		// Onglet Modules
		tabSpec[3] = octoTabHost.newTabSpec("modules");
		tabSpec[3].setIndicator(res.getText(R.string.modules_activity_name),res.getDrawable(R.drawable.ic_tab_parameter));
		tabSpec[3].setContent(new Intent(OctopusActivity.this,ModulesActivity.class));
		octoTabHost.addTab(tabSpec[3]);
		
		octoTabHost.setCurrentTab(0);
		
		/* LANCEMENT SCANNER */
		progressDialog = new ProgressDialog(this);
		Log.i(TAG, "Tentative de scanne des fichiers...");
		// Définition du titre, du texte et du style de la boite de dialogue
		progressDialog.setTitle(res.getText(R.string.loading));
		progressDialog.setMessage(res.getText(R.string.scanning));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(true);
		// Affichage de la boite de dialogue
		progressDialog.show();
		
		// Création du thread scanner
		new Thread(new Runnable() {
			private String TAG = "SCANN_THREAD";
			private String scannPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+musicPath;
			public void run() {
				// Création objet MusicFilesScanne
				Log.i(TAG, "Création de l'objet MusicFilesScanne");
				filesScanner = new MusicFilesScanne(getApplicationContext(), scannPath, progressDialog);
				Log.i(TAG, "Lancement du scanne de \""+scannPath+"\"");
				if( filesScanner.scanneFiles() ) {
					Log.i(TAG,"Retour du scanner positif, fermeture boite de dialogue");
				}
				else {
					Log.e(TAG, "Impossible de scanner le dossier \""+scannPath+"\"");
				}
				// Fin du scanne des musiques
				progressDialog.dismiss();
				// Lancement du service
				bindService(new Intent(getApplicationContext(),OctopusService.class),octoConnection,Context.BIND_AUTO_CREATE);
			}
		}).start();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}
	// Gestion rotation du telephone
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
	}
}