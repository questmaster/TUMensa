package de.questmaster.tudmensa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
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
	}


	public static class Settings
	{
		// default values
		public String m_sMensaLocation = "stadtmitte";
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

				m_sMensaLocation = sharedPref.getString(res.getString(R.string.PREF_KEY_MENSA_LOCATION), m_sMensaLocation);

				m_sThemes = sharedPref.getString(res.getString(R.string.PREF_KEY_THEMES), m_sThemes);
			}
		}
	}
}
