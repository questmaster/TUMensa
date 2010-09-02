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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import de.questmaster.tudmensa.R;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
//import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class MensaMeals extends ListActivity {
	public static final int INSERT_ID = Menu.FIRST;
	
//	private int mNoteNumber = 1;
	private MealsDbAdapter mDbHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_list);
        mDbHelper = new MealsDbAdapter(this);
        mDbHelper.open();
        getWebPage();

        fillData();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case INSERT_ID:
            createNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void createNote() {
        String noteName = "Note " + mNoteNumber++;
        mDbHelper.createNote(noteName, "");
        fillData();
    }
*/    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllMeals();
        startManagingCursor(c);

        String[] from = new String[] { MealsDbAdapter.KEY_NAME };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter meals =
            new SimpleCursorAdapter(this, R.layout.meals_entry, c, from, to);
        setListAdapter(meals);
    }
    
    private void getWebPage() {
    	Vector<String> webTable = new Vector<String>();
    	
    	try {
			URL uTest = new URL("http://www.studentenwerkdarmstadt.de/index.php?option=com_spk&task=stadtmitte&view=week");
			BufferedReader br = new BufferedReader(new InputStreamReader(uTest.openStream()));

			boolean store = false;
			String s;
			while ((s = br.readLine()) != null)   {
				// find first line of meal tables
				if (s.indexOf("class=\"spk_table\">") >= 0) {
					// remove before table
					s = s.substring(s.indexOf("<table"));
					store = true;
				}
				if (store) {
					if (s.indexOf("</table>") >= 0) {
						// remove after table
						s = "</table>";
						store = false;
					}
					
					// append line
					webTable.add(s);
					
					if (!store) {
						break; // fertig
					}
				}
		    }
			
    	} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }    
}
