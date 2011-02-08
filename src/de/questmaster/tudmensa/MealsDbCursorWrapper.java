/*
 * Copyright (C) 2010 Daniel Jacobi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.questmaster.tudmensa;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

/**
 * This Wrapper replaces the picture type with its picture resource id.
 * 
 * @author Daniel
 */
public class MealsDbCursorWrapper implements Cursor {

	private Cursor mCursor;
	private MensaMealsSettings.Settings mSettings = new MensaMealsSettings.Settings();

	/**
	 * 
	 */
	public MealsDbCursorWrapper(Cursor c, Context ct) {
		mCursor = c;

		// Read settings
		mSettings.ReadSettings(ct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#close()
	 */
	public void close() {
		mCursor.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#copyStringToBuffer(int,
	 * android.database.CharArrayBuffer)
	 */
	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		mCursor.copyStringToBuffer(columnIndex, buffer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#deactivate()
	 */
	public void deactivate() {
		mCursor.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getBlob(int)
	 */
	public byte[] getBlob(int columnIndex) {
		return mCursor.getBlob(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getColumnCount()
	 */
	public int getColumnCount() {
		return mCursor.getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getColumnIndex(java.lang.String)
	 */
	public int getColumnIndex(String columnName) {
		return mCursor.getColumnIndex(columnName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getColumnIndexOrThrow(java.lang.String)
	 */
	public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
		return mCursor.getColumnIndexOrThrow(columnName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		return mCursor.getColumnName(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getColumnNames()
	 */
	public String[] getColumnNames() {
		return mCursor.getColumnNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getCount()
	 */
	public int getCount() {
		return mCursor.getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getDouble(int)
	 */
	public double getDouble(int columnIndex) {
		return mCursor.getDouble(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getExtras()
	 */
	public Bundle getExtras() {
		return mCursor.getExtras();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getFloat(int)
	 */
	public float getFloat(int columnIndex) {
		return mCursor.getFloat(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getInt(int)
	 */
	public int getInt(int columnIndex) {
		return mCursor.getInt(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getLong(int)
	 */
	public long getLong(int columnIndex) {
		return mCursor.getLong(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getPosition()
	 */
	public int getPosition() {
		return mCursor.getPosition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getShort(int)
	 */
	public short getShort(int columnIndex) {
		return mCursor.getShort(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getString(int)
	 */
	public String getString(int columnIndex) {
		String result = mCursor.getString(columnIndex);

		if (columnIndex == this.getColumnIndex(MealsDbAdapter.KEY_TYPE)) {
			// Theme support
			if (mSettings.m_sThemes.equals("dark")) {
				// create type drawable
				if (result.equals("F")) {
					result = String.valueOf(R.drawable.meal_f_d);
				} else if (result.equals("G")) {
					result = String.valueOf(R.drawable.meal_g_d);
				} else if (result.equals("K")) {
					result = String.valueOf(R.drawable.meal_k_d);
				} else if (result.equals("R")) {
					result = String.valueOf(R.drawable.meal_r_d);
				} else if (result.equals("RS")) {
					result = String.valueOf(R.drawable.meal_rs_d);
				} else if (result.equals("S")) {
					result = String.valueOf(R.drawable.meal_s_d);
				} else if (result.equals("V")) {
					result = String.valueOf(R.drawable.meal_v_d);
				} else {
					result = String.valueOf(R.drawable.essen_d);
				}
			} else if (mSettings.m_sThemes.equals("light")) {
				// create type drawable
				if (result.equals("F")) {
					result = String.valueOf(R.drawable.meal_f);
				} else if (result.equals("G")) {
					result = String.valueOf(R.drawable.meal_g);
				} else if (result.equals("K")) {
					result = String.valueOf(R.drawable.meal_k);
				} else if (result.equals("R")) {
					result = String.valueOf(R.drawable.meal_r);
				} else if (result.equals("RS")) {
					result = String.valueOf(R.drawable.meal_rs);
				} else if (result.equals("S")) {
					result = String.valueOf(R.drawable.meal_s);
				} else if (result.equals("V")) {
					result = String.valueOf(R.drawable.meal_v);
				} else {
					result = String.valueOf(R.drawable.essen);
				}
			}

		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#getWantsAllOnMoveCalls()
	 */
	public boolean getWantsAllOnMoveCalls() {
		return mCursor.getWantsAllOnMoveCalls();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#isAfterLast()
	 */
	public boolean isAfterLast() {
		return mCursor.isAfterLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#isBeforeFirst()
	 */
	public boolean isBeforeFirst() {
		return mCursor.isBeforeFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#isClosed()
	 */
	public boolean isClosed() {
		return mCursor.isClosed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#isFirst()
	 */
	public boolean isFirst() {
		return mCursor.isFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#isLast()
	 */
	public boolean isLast() {
		return mCursor.isLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#isNull(int)
	 */
	public boolean isNull(int columnIndex) {
		return mCursor.isNull(columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#move(int)
	 */
	public boolean move(int offset) {
		return mCursor.move(offset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#moveToFirst()
	 */
	public boolean moveToFirst() {
		return mCursor.moveToFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#moveToLast()
	 */
	public boolean moveToLast() {
		return mCursor.moveToLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#moveToNext()
	 */
	public boolean moveToNext() {
		return mCursor.moveToNext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#moveToPosition(int)
	 */
	public boolean moveToPosition(int position) {
		return mCursor.moveToPosition(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#moveToPrevious()
	 */
	public boolean moveToPrevious() {
		return mCursor.moveToPrevious();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#registerContentObserver(android.database.
	 * ContentObserver)
	 */
	public void registerContentObserver(ContentObserver observer) {
		mCursor.registerContentObserver(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#registerDataSetObserver(android.database.
	 * DataSetObserver)
	 */
	public void registerDataSetObserver(DataSetObserver observer) {
		mCursor.registerDataSetObserver(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#requery()
	 */
	public boolean requery() {
		return mCursor.requery();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#respond(android.os.Bundle)
	 */
	public Bundle respond(Bundle extras) {
		return mCursor.respond(extras);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.Cursor#setNotificationUri(android.content.ContentResolver
	 * , android.net.Uri)
	 */
	public void setNotificationUri(ContentResolver cr, Uri uri) {
		mCursor.setNotificationUri(cr, uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#unregisterContentObserver(android.database.
	 * ContentObserver)
	 */
	public void unregisterContentObserver(ContentObserver observer) {
		mCursor.unregisterContentObserver(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.Cursor#unregisterDataSetObserver(android.database.
	 * DataSetObserver)
	 */
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mCursor.unregisterDataSetObserver(observer);
	}

}
