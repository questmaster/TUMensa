package de.questmaster.tudmensa;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class MensaMealsSettings extends PreferenceActivity {

	private MensaMealsSettings.Settings mSettings = new MensaMealsSettings.Settings();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Read settings
		mSettings.ReadSettings(this);

		// Setup Theme
		if (mSettings.m_sThemes.equals("dark")) {
			setTheme(android.R.style.Theme_Black_NoTitleBar);
		} else if (mSettings.m_sThemes.equals("light")) {
			setTheme(android.R.style.Theme_Light_NoTitleBar);
		}

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// Set last update date
		Preference pLastUpdate = getPreferenceManager().findPreference(getString(R.string.PREF_KEY_LAST_UPDATE));
		Calendar oLastUpdate = Calendar.getInstance();
		oLastUpdate.setTimeInMillis(mSettings.m_lLastUpdate);
		pLastUpdate.setSummary(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.FULL, SimpleDateFormat.MEDIUM).format(oLastUpdate.getTime()));
	}


	public static class Settings
	{
		// default values
		public String m_sMensaLocation = "stadtmitte";
		public long m_lLastUpdate = 0;
		public boolean m_bAutoUpdate = true;
		public boolean m_bDeleteOldData = true;
		public boolean m_bGestures = true;
		public String m_sThemes = "dark";


		public void ReadSettings(Context p_oContext)
		{
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(p_oContext);
			Resources res = p_oContext.getResources();

			if (sharedPref != null) {
				m_bAutoUpdate = sharedPref.getBoolean(res.getString(R.string.PREF_KEY_AUTO_UPDATE), m_bAutoUpdate);
				m_bDeleteOldData = sharedPref.getBoolean(res.getString(R.string.PREF_KEY_DELETE_OLD_DATA), m_bDeleteOldData);
				m_bGestures = sharedPref.getBoolean(res.getString(R.string.PREF_KEY_GESTURES), m_bGestures);

				m_lLastUpdate = sharedPref.getLong(res.getString(R.string.PREF_KEY_LAST_UPDATE), m_lLastUpdate);

				m_sMensaLocation = sharedPref.getString(res.getString(R.string.PREF_KEY_MENSA_LOCATION), m_sMensaLocation);

				m_sThemes = sharedPref.getString(res.getString(R.string.PREF_KEY_THEMES), m_sThemes);
			}
		}
		
		public void setLastUpdate(Context p_oContext) {
			Editor sharedPref = PreferenceManager.getDefaultSharedPreferences(p_oContext).edit();
			Resources res = p_oContext.getResources();
			
			m_lLastUpdate = Calendar.getInstance().getTimeInMillis();
			sharedPref.putLong(res.getString(R.string.PREF_KEY_LAST_UPDATE), m_lLastUpdate);
			
			sharedPref.commit();
		}

		public void setMensaLocation(Context p_oContext, String loc_id) {
			Editor sharedPref = PreferenceManager.getDefaultSharedPreferences(p_oContext).edit();
			Resources res = p_oContext.getResources();
			
			m_sMensaLocation = loc_id;
			sharedPref.putString(res.getString(R.string.PREF_KEY_MENSA_LOCATION), m_sMensaLocation);
			
			sharedPref.commit();
		}
}
}
