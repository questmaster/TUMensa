/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.questmaster.tudmensa;

import de.questmaster.tudmensa.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class MensaMeals extends ListActivity {
	public static final int UPDATE_ID = Menu.FIRST;
	public static final int SETTINGS_ID = Menu.FIRST + 1;
	
	public static final int ON_SETTINGS_CHANGE = 0;

	private MensaMealsSettings.Settings mSettings = new MensaMealsSettings.Settings (); 

	private MealsDbAdapter mDbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meals_list);
		
		// Settings
		mSettings.ReadSettings (this);
		
		// Database
		mDbHelper = new MealsDbAdapter(this);
		mDbHelper.open();

		// get data
		DataExtractor de = new DataExtractor(this, mDbHelper);
		de.retrieveData(mSettings.m_sMensaLocation);
		
		fillData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuItem mItem = null;

		mItem = menu.add(0, UPDATE_ID, 0, R.string.menu_update);
		mItem.setIcon(android.R.drawable.ic_menu_rotate);

		mItem = menu.add(0, SETTINGS_ID, 1, R.string.menu_settings);
//		mItem.setShortcut('3', 's');
		mItem.setIcon(android.R.drawable.ic_menu_preferences);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case UPDATE_ID:
			DataExtractor de = new DataExtractor(this, mDbHelper);
			de.retrieveData(mSettings.m_sMensaLocation);
			fillData();
			break;
			
		case SETTINGS_ID:
	        Intent iSettings = new Intent ();
	        iSettings.setClass(this, MensaMealsSettings.class);
	        startActivityForResult(iSettings, ON_SETTINGS_CHANGE); 
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override 
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ON_SETTINGS_CHANGE:
	        mSettings.ReadSettings (this);
	        
	        // TODO clear database
	        
			break;
		}
	}
	
	protected void onDestroy () {
		super.onDestroy();
		
		// close database
		mDbHelper.close();
	}
	
	private void fillData() {
		// Get all of the notes from the database and create the item list
		Cursor c = mDbHelper.fetchMealsOfDay(mSettings.m_sMensaLocation, "20100903");
		startManagingCursor(c);

		String[] from = new String[] { MealsDbAdapter.KEY_NAME };
		int[] to = new int[] { R.id.text1 };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter meals = new SimpleCursorAdapter(this,
				R.layout.meals_entry, c, from, to);
		setListAdapter(meals);
	}
}
