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

	// private int mNoteNumber = 1;
	private MealsDbAdapter mDbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meals_list);
		mDbHelper = new MealsDbAdapter(this);
		mDbHelper.open();

		parseTable(getWebPage("stadtmitte", "week"));

		fillData();
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { boolean result
	 * = super.onCreateOptionsMenu(menu); menu.add(0, INSERT_ID, 0,
	 * R.string.menu_insert); return result; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case INSERT_ID: createNote(); return true; } return
	 * super.onOptionsItemSelected(item); }
	 * 
	 * private void createNote() { String noteName = "Note " + mNoteNumber++;
	 * mDbHelper.createNote(noteName, ""); fillData(); }
	 */
	private void fillData() {
		// Get all of the notes from the database and create the item list
		Cursor c = mDbHelper.fetchAllMeals();
		startManagingCursor(c);

		String[] from = new String[] { MealsDbAdapter.KEY_NAME };
		int[] to = new int[] { R.id.text1 };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter meals = new SimpleCursorAdapter(this,
				R.layout.meals_entry, c, from, to);
		setListAdapter(meals);
	}

	private Vector<String> getWebPage(String task, String view) {
		Vector<String> webTable = new Vector<String>();

		try {
			URL uTest = new URL(
					"http://www.studentenwerkdarmstadt.de/index.php?option=com_spk&task="
							+ task + "&view=" + view);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					uTest.openStream()));

			boolean store = false;
			String s;
			while ((s = br.readLine()) != null) {
				// find first line of meal tables
				if (s.indexOf("class=\"spk_table\">") >= 0) {
					// remove before table
					s = s.substring(s.indexOf("<tr><td"));
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
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		return webTable;
	}

	private void parseTable(Vector<String> tbl) {
		Vector<String> days = new Vector<String>();
		String curCounter = "";

		for (String s : tbl) {
			// new Row -> maybe new counter
			if (s.startsWith("<tr>")) {
				if (s.indexOf("&nbsp;") < 0) {
					// get new Counter name
					curCounter = extractData(s);
				}
			}

			if (s.startsWith("<td")) {
				String tmp = extractData(s);

				// date line
				if (curCounter.compareTo("") == 0 && tmp.length() == 10) {
						tmp = tmp.substring(6, 10) + tmp.substring(3, 5) + tmp.substring(0, 2);
						days.add(tmp);
				} else if (tmp.lastIndexOf(",") > 0 && // €-sign unfortunately not encoded, so checking price-tag
						Character.isDigit(tmp.charAt(tmp.lastIndexOf(",")-1)) &&
						Character.isDigit(tmp.charAt(tmp.lastIndexOf(",")+1)) &&
						Character.isDigit(tmp.charAt(tmp.lastIndexOf(",")+2))) { 
					// TODO meal line
					System.out.println("meal line");
				}
			}

			// TODO </table>: end -> store to db
			if (s.equals("</table>")) {
				System.out.println("store to db");
			}
		}
	}

	private String extractData(String s) {
		// cut end "</td>"
		s = s.substring(0, s.length() - 5);

		// cut begining
		return s.substring(s.lastIndexOf(">") + 1, s.length()).trim();
	}
}
