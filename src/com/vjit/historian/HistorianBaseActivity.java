package com.vjit.historian;


import com.vjit.historian.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;

public class HistorianBaseActivity extends Activity {

	Tab tab;
	// instantiating tabs
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar mActionBar = getActionBar();// instantiating ActionBar and getting ActionBar
	
		//Remove AppIcon Title and Action Bar
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		//Set NAV to NAVIGATION_MODE_TABS
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//add each tab separately to the ActionBar and set TabListeners
		tab = mActionBar.newTab().setTabListener(new EventsFragment());
		tab.setIcon(R.drawable.ic_event);
		tab.setText("Events");
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setTabListener(new BirthsFragment());
		tab.setIcon(R.drawable.ic_birth);
		tab.setText("Births");
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setTabListener(new HolidaysFragment());
		tab.setIcon(R.drawable.ic_holiday);
		tab.setText("Holidays");
		mActionBar.addTab(tab);

		
		tab = mActionBar.newTab().setTabListener(new DeathsFragment());
		tab.setIcon(R.drawable.ic_death);
		tab.setText("Deaths");
		mActionBar.addTab(tab);

	}
}

