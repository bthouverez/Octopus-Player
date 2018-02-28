package com.octopus.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.octopus.OctopusActivity;
import com.octopus.OctopusService;
import com.octopus.R;
import com.octopus.model.Music;

public class PlayerActivity extends Activity {
	
	private static final String TAG = "PLAYER";
	Context context;
	Resources resources;
	//OrientationEventListener orientation = new OrientationEventListener(cont);
	private OctopusService octopusService;
	public void linkService(OctopusService service) {
		Log.i(TAG,"Liaison avec le service");
		octopusService = service;
		updateMusicValues();
	}
	private static PlayerActivity Instance;
	public static PlayerActivity getInstance(){
		return Instance;
	}
	
	boolean pause = false, firstTap = true;
	int idxVector=0;
	
	// Composants
	private Button buttonPlay, buttonNext, buttonPrevious;
	private SeekBar seekBar;
	public void onConfigurationChanged()
	{
		setContentView(R.layout.tabplayer);
		Toast.makeText(getApplicationContext(), "Rotation", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Lancement onglet player");
		Log.i(TAG, "Liaison avec l'activité OctopusActivity");
		((OctopusActivity)this.getParent()).registerPlayerActivity(this);
		// Gestion Intance d'activité
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabplayer);
		resources = getResources();
		context = this;
	
		
		// Instantiation des composants
		buttonPlay = (Button)findViewById(R.id.buttonPlay);
		
		 /*orientation.onOrientationChanged(90);
			{
				Toast.makeText(getApplicationContext(), "Bouton lecture enfoncé !", Toast.LENGTH_SHORT).show();
				
			}*/
		
		/*Called when the orientation of the device has changed. orientation parameter is in degrees,
		 *  ranging from 0 to 359. orientation is 0 degrees when the device is oriented in its natural position,
		 *  90 degrees when its left side is at the top,
		 *  180 degrees when it is upside down, 
		 *  and 270 degrees when its right side is to the top
		 */
		
		// Gestion du bouton PLAY //
		buttonPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(octopusService == null)
					Log.e(TAG, "Pointeur service null !");
				else
				{
					if (octopusService.isPlaying())
					{
						if(octopusService.pause()) {
							buttonPlay.setBackgroundDrawable(resources.getDrawable(R.drawable.playnb));
						}
					}
					else {
						if(octopusService.play()) {
							buttonPlay.setBackgroundDrawable(resources.getDrawable(R.drawable.pausenb));
						}
					}	
				}
			}
		});
		
		// Gestion du bouton SUIVANT //
		buttonNext = (Button)findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(octopusService == null)
					Log.e(TAG, "Pointeur service null !");
				else
				{
					if(!octopusService.next()) {
						Toast.makeText(context, "Fin de la playlist actuelle", Toast.LENGTH_SHORT).show();
					}
					updateMusicValues();
				}
			}
		});
		
		buttonPrevious = (Button)findViewById(R.id.buttonPrevious);
		buttonPrevious.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(octopusService == null)
					Log.e(TAG, "Pointeur service null !");
				else
				{
					if(!octopusService.prec()) {
						Toast.makeText(context, "Fin de la playlist actuelle", Toast.LENGTH_SHORT).show();
					}
					updateMusicValues();
				}
			}
		});
		seekBar = ((SeekBar)findViewById(R.id.seekBar1));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser)
					octopusService.getMediaPlayer().seekTo(progress);
			}
		});
		seekBar.setEnabled(false);
		Instance=this;
	}
	public void onResume() {
		super.onResume();
		updateMusicValues();
	}
	private TimerTask updateProgress= new TimerTask() {
		
		@Override
		public void run() {
			seekBar.setProgress(octopusService.getMediaPlayer().getCurrentPosition());
		}
	};
	private boolean timerOn=false;
	private Timer timer=new Timer();
	public void updateMusicValues() {
		if(octopusService != null) {
			if(octopusService.getCurrentMusic() != null) {
				if(octopusService.isPlaying()) {
					buttonPlay.setBackgroundDrawable(resources.getDrawable(R.drawable.pausenb));
					if(!timerOn){
						seekBar.setEnabled(true);
						timer.scheduleAtFixedRate(updateProgress, 100, 100);
						timerOn=true;
					}
				}
				if(!octopusService.isPlaying() || octopusService.isPaused()) {
					buttonPlay.setBackgroundDrawable(resources.getDrawable(R.drawable.playnb));
				}
				Music currentMusic = octopusService.getCurrentMusic();
				((TextView)findViewById(R.id.musicName)).setText(currentMusic.getTitle());
				((TextView)findViewById(R.id.artistName)).setText(currentMusic.getArtist());
				((TextView)findViewById(R.id.albumName)).setText(currentMusic.getAlbum());
				seekBar.setMax(octopusService.getMediaPlayer().getDuration());
				seekBar.setProgress(octopusService.getMediaPlayer().getCurrentPosition());
			}
		}
	}
}
