/*
 * Copyright (C) 2011 Daniel Jacobi
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import android.os.Bundle;
import android.text.format.DateFormat;

/**
 * @author Daniel
 * 
 */
public class VoteHelper extends Thread {

	private final static int MODE_GET_VOTES = 0;
	private final static int MODE_SET_VOTES = 1;
	private static final String mURLlocation = "http://www.questmaster.de/tumensa/";

	private Bundle mDialogData = null;
	private MealsDbAdapter mDbHelper;
	private int mModeOfOperation = MODE_GET_VOTES;
	private MensaMeals mParent = null;

	/**
	 * @param bundle
	 */
	public VoteHelper(MensaMeals parent, MealsDbAdapter dbHelper) {
		mDbHelper = dbHelper;
		mParent = parent;

		mModeOfOperation = MODE_GET_VOTES;
	}

	/**
	 * @param bundle
	 */
	public VoteHelper(Bundle bundle) {
		mDialogData = bundle;
		mModeOfOperation = MODE_SET_VOTES;
	}

	@Override
	public void run() {
		switch (mModeOfOperation) {
		case MODE_GET_VOTES:
			float res_visual,
			res_taste,
			res_price;
			int cnt_visual,
			cnt_price,
			cnt_taste;
			String db_location,
			db_counter;
			String[] db_dates;
			int db_counter_num;

			// get dates in DB from today on
			db_dates = mDbHelper.fetchDatesFromToday((String) DateFormat.format("yyyyMMdd", Calendar.getInstance()));

			for (String db_date : db_dates) {
				// read votes data
				try {
					URL voteURL = new URL(String.format(mURLlocation + "D%s.txt", db_date));
					InputStream in = voteURL.openStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(in));

					String line;
					while ((line = br.readLine()) != null) {
						String tokens[] = line.split(" "); // <mealid> <v1> <vc1> <v2> <vc2> <v3> <vc3>
						String db_tokens[] = tokens[0].split("\\|"); // <location> <counter> <num>

						if (tokens.length == 7 && db_tokens.length == 3) {
							// populate vars
							db_location = db_tokens[0].replaceAll("_", " ");
							db_counter = db_tokens[1];
							db_counter_num = Integer.parseInt(db_tokens[2]);

							res_visual = Float.parseFloat(tokens[1]);
							cnt_visual = Integer.parseInt(tokens[2]);
							res_price = Float.parseFloat(tokens[3]);
							cnt_price = Integer.parseInt(tokens[4]);
							res_taste = Float.parseFloat(tokens[5]);
							cnt_taste = Integer.parseInt(tokens[6]);

							// store votes in DB
							long id = mDbHelper.fetchMealId(db_location, db_date, db_counter, db_counter_num);
							mDbHelper.updateMealExternalVotes(id, res_visual, res_price, res_taste, cnt_visual, cnt_price, cnt_taste);
						}
					}

					in.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// This occurs if file not found, but thats ok.
					//e.printStackTrace();
				}
			}

			// update list
			mParent.mHandler.sendEmptyMessage(1);
			break;
		case MODE_SET_VOTES:
			try {
				String visual = String.valueOf(mDialogData.getBoolean(MensaMeals.VOTE_DIALOG_VISUAL_CHANGE_ID) ? mDialogData.getFloat(MensaMeals.VOTE_DIALOG_VISUAL_ID) : 0);
				String price = String.valueOf(mDialogData.getBoolean(MensaMeals.VOTE_DIALOG_PRICE_CHANGE_ID) ? mDialogData.getFloat(MensaMeals.VOTE_DIALOG_PRICE_ID) : 0);
				String taste = String.valueOf(mDialogData.getBoolean(MensaMeals.VOTE_DIALOG_TASTE_CHANGE_ID) ? mDialogData.getFloat(MensaMeals.VOTE_DIALOG_TASTE_ID) : 0);

				URL voteURL = new URL(String.format(mURLlocation + "mealcounter.php?mealid=%s&date=%s&vote1=%s&vote2=%s&vote3=%s", mDialogData.getString(MensaMeals.VOTE_DIALOG_MEAL_SCRIPT_ID),
						mDialogData.getString(MensaMeals.VOTE_DIALOG_DATE_ID), visual, price, taste));

				URLConnection uc = voteURL.openConnection();

				uc.connect();
				uc.getContent();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
