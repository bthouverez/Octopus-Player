package com.octopus.view;

import com.octopus.OctopusActivity;
import com.octopus.OctopusService;
import com.octopus.R;
import com.octopus.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ModulesActivity extends Activity {
	
	private static final String TAG = "MODULES";
	private OctopusService octopusService;
	
	Button buttonQuit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Lancement onglet modules");
		((OctopusActivity)this.getParent()).registerModulesActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabmodules);
		
		CheckBox lectureAleatoire = (CheckBox)findViewById(R.id.checkBox1);
		lectureAleatoire.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				octopusService.setRandomPlaylist(isChecked);
			}});
		CheckBox lectureLoop = (CheckBox)findViewById(R.id.checkBox2);
		lectureLoop.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				octopusService.setLoopPlaylist(isChecked);
			}});
		
		buttonQuit = (Button)findViewById(R.id.quitButton);
		final OctopusActivity parentActivity = (OctopusActivity)this.getParent();
		final ModulesActivity thisActivity = this;
		buttonQuit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.i(TAG, "Trying to shut down the app.");
				if(octopusService != null){
					octopusService.stopService(getIntent());
				}
				parentActivity.finishFromChild(thisActivity);
			}
		});
	}
	public void linkService(OctopusService service) {
		Log.i(TAG,"Liaison avec le service");
		octopusService = service;
	}
}
