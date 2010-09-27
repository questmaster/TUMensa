package de.questmaster.tudmensa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class MensaMealsSettings extends PreferenceActivity {
	  public static final String PREF_KEY_MENSA_LOCATION    = "mensa_location";
	  public static final String PREF_KEY_AUTO_UPDATE    	= "auto_update";
	  public static final String PREF_KEY_DELETE_OLD_DATA	= "del_old_data";
	  public static final String PREF_KEY_THEMES			= "themes";
    
  // ----------------------------------------------------------------------------
  @Override protected void onCreate (Bundle savedInstanceState)
  // ----------------------------------------------------------------------------
  {
    super.onCreate (savedInstanceState);

    setPreferenceScreen (CreatePreferences ());
  }

  // ----------------------------------------------------------------------------
  private PreferenceScreen CreatePreferences ()
  // ----------------------------------------------------------------------------
  {
    PreferenceScreen root = getPreferenceManager ().createPreferenceScreen (this);

    // Gruppe Mensa
    PreferenceCategory pcMensa = new PreferenceCategory (this);
    pcMensa.setTitle (R.string.pref_cat_mensa);
    root.addPreference (pcMensa);

    // Manual Update 
    CheckBoxPreference cb = new CheckBoxPreference (this);
    cb.setDefaultValue  (true);
    cb.setKey     (PREF_KEY_AUTO_UPDATE);
    cb.setTitle   (R.string.pref_AutoUpdateLabel);
    cb.setSummary (R.string.pref_AutoUpdateDescr);
    
    pcMensa.addPreference (cb);
   
    // Mensa Location
    ListPreference lst = new ListPreference (this);
    lst.setEntries (R.array.MensaLocations);
    lst.setEntryValues (R.array.MensaLocationsValues);
    lst.setDefaultValue  ("stadtmitte");
    lst.setDialogTitle (R.string.pref_MensaLocationLabel);
    lst.setTitle (R.string.pref_MensaLocationLabel);
    lst.setKey (PREF_KEY_MENSA_LOCATION);
    lst.setSummary (R.string.pref_MensaLocationSummary);

    pcMensa.addPreference (lst);

    // Gruppe Database
    PreferenceCategory pcDB = new PreferenceCategory (this);
    pcDB.setTitle (R.string.pref_cat_db);
    root.addPreference (pcDB);

    // Manual Update 
    cb = new CheckBoxPreference (this);
    cb.setDefaultValue  (true);
    cb.setKey     (PREF_KEY_DELETE_OLD_DATA);
    cb.setTitle   (R.string.pref_DeleteOldDataLabel);
    cb.setSummary (R.string.pref_DeleteOldDataDescr);
    
    pcDB.addPreference (cb);

 /*   // Gruppe Themes
    PreferenceCategory pcTheme = new PreferenceCategory (this);
    pcTheme.setTitle (R.string.pref_cat_themes);
    root.addPreference (pcTheme);

    // Theme
    lst = new ListPreference (this);
    lst.setEntries (R.array.Themes);
    lst.setEntryValues (R.array.ThemesValues);
    lst.setDefaultValue  ("dark");
    lst.setDialogTitle (R.string.pref_ThemesLabel);
    lst.setTitle (R.string.pref_ThemesLabel);
    lst.setKey (PREF_KEY_THEMES);
    lst.setSummary (R.string.pref_ThemesSummary);

    pcTheme.addPreference (lst);
*/
//    // Date format
//    lst = new ListPreference (this);
//    lst.setEntries (R.array.asDateFormatOptions);
//    lst.setEntryValues (R.array.asDateFormatOptionValues);
//    lst.setDefaultValue  (__DATE_FORMAT_DEFAULT);
//    lst.setDialogTitle (R.string.prfDateFormatLabel);
//    lst.setTitle (R.string.prfDateFormatCaption);
//    lst.setKey (__PREF_KEY_DATE_FORMAT);
//    lst.setSummary (R.string.prfDateFormatSummary);
//
//    pcMensa.addPreference (lst);
//
//    // Time format
//    lst = new ListPreference (this);
//    lst.setEntries (R.array.asTimeFormatOptions);
//    lst.setEntryValues (R.array.asTimeFormatOptionValues);
//    lst.setDefaultValue  (__TIME_FORMAT_DEFAULT);
//    lst.setDialogTitle (R.string.prfTimeFormatLabel);
//    lst.setTitle (R.string.prfTimeFormatCaption);
//    lst.setKey (__PREF_KEY_TIME_FORMAT);
//    lst.setSummary (R.string.prfTimeFormatSummary);
//
//    ppcAppearance.addPreference (lst);
//    
//    // Dialog based preferences
//    PreferenceCategory dialogBasedPrefCat = new PreferenceCategory (this);
//    dialogBasedPrefCat.setTitle (R.string.dialog_based_preferences);
//    root.addPreference (dialogBasedPrefCat);
//
//    // Edit text preference
//    EditTextPreference editTextPref = new EditTextPreference (this);
//    editTextPref.setDialogTitle (R.string.dialog_title_edittext_preference);
//    editTextPref.setKey ("edittext_preference");
//    editTextPref.setTitle (R.string.title_edittext_preference);
//    editTextPref.setSummary (R.string.summary_edittext_preference);
//    dialogBasedPrefCat.addPreference (editTextPref);
//
//    // List preference
//    ListPreference listPref = new ListPreference (this);
//    listPref.setEntries (R.array.entries_list_preference);
//    listPref.setEntryValues (R.array.entryvalues_list_preference);
//    listPref.setDialogTitle (R.string.dialog_title_list_preference);
//    listPref.setKey ("list_preference");
//    listPref.setTitle (R.string.title_list_preference);
//    listPref.setSummary (R.string.summary_list_preference);
//    dialogBasedPrefCat.addPreference (listPref);
//
//    // Launch preferences
//    PreferenceCategory launchPrefCat = new PreferenceCategory (this);
//    launchPrefCat.setTitle (R.string.launch_preferences);
//    root.addPreference (launchPrefCat);
//
//    // The Preferences screenPref serves as a screen break (similar to page
//    // break in word processing). Like for other preference types, we assign a
//    // key here so that it is able to save and restore its instance state.
//    // Screen preference
//    PreferenceScreen screenPref = getPreferenceManager ().createPreferenceScreen (this);
//    screenPref.setKey ("screen_preference");
//    screenPref.setTitle (R.string.title_screen_preference);
//    screenPref.setSummary (R.string.summary_screen_preference);
//    launchPrefCat.addPreference (screenPref);
//
//    // You can add more preferences to screenPref that will be shown on the next
//    // screen.
//    // Example of next screen toggle preference
//    CheckBoxPreference nextScreenCheckBoxPref = new CheckBoxPreference (this);
//    nextScreenCheckBoxPref.setKey ("next_screen_toggle_preference");
//    nextScreenCheckBoxPref.setTitle (R.string.title_next_screen_toggle_preference);
//    nextScreenCheckBoxPref.setSummary (R.string.summary_next_screen_toggle_preference);
//    screenPref.addPreference (nextScreenCheckBoxPref);
//
//    // Intent preference
//    PreferenceScreen intentPref = getPreferenceManager ().createPreferenceScreen (this);
//    intentPref.setIntent (new Intent ().setAction (Intent.ACTION_VIEW).setData (Uri.parse ("http://www.android.com")));
//    intentPref.setTitle (R.string.title_intent_preference);
//    intentPref.setSummary (R.string.summary_intent_preference);
//    launchPrefCat.addPreference (intentPref);
//
//    // Preference attributes
//    PreferenceCategory prefAttrsCat = new PreferenceCategory (this);
//    prefAttrsCat.setTitle (R.string.preference_attributes);
//    root.addPreference (prefAttrsCat);
//
//    // Visual parent toggle preference
//    CheckBoxPreference parentCheckBoxPref = new CheckBoxPreference (this);
//
//    parentCheckBoxPref.setTitle (R.string.title_parent_preference);
//    parentCheckBoxPref.setSummary (R.string.summary_parent_preference);
//    prefAttrsCat.addPreference (parentCheckBoxPref);
//
//    // Visual child toggle preference
//    // See res/values/attrs.xml for the <declare-styleable> that defines
//    // TogglePrefAttrs.
//    TypedArray a = obtainStyledAttributes (R.styleable.TogglePrefAttrs);
//    
//    CheckBoxPreference childCheckBoxPref = new CheckBoxPreference (this);
//    
//    childCheckBoxPref.setTitle (R.string.title_child_preference);
//    childCheckBoxPref.setSummary (R.string.summary_child_preference);
//    childCheckBoxPref.setLayoutResource (a.getResourceId (R.styleable.TogglePrefAttrs_android_preferenceLayoutChild, 0));
//    prefAttrsCat.addPreference (childCheckBoxPref);
//    a.recycle ();

    return root;
  }
  
  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  public static class Settings
  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  {
    public String  m_sMensaLocation 	 = "stadtmitte";
    public boolean  m_bAutoUpdate        = true;
    public boolean  m_bDeleteOldData     = true;
//    public boolean  m_bExpandAll       = false;
//    public boolean  m_bShowCreatedTime = true;
//    public int      m_iSortOrder;
    public String   m_sThemes			 = "dark";
//    public String   m_sTimeFormat;

    // ----------------------------------------------------------------------------
    public void ReadSettings (Context p_oContext)
    // ----------------------------------------------------------------------------
    {
      SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences (p_oContext);
      
      if (sharedPref != null)
      {
    	  m_bAutoUpdate      = sharedPref.getBoolean (MensaMealsSettings.PREF_KEY_AUTO_UPDATE, m_bAutoUpdate);
          m_bDeleteOldData   = sharedPref.getBoolean (MensaMealsSettings.PREF_KEY_DELETE_OLD_DATA, m_bDeleteOldData);
//        m_bExpandAll       = sharedPref.getBoolean (MensaMealsSettings.__PREF_KEY_EXPAND_ALL, m_bExpandAll);
//        m_bShowCreatedTime = sharedPref.getBoolean (MensaMealsSettings.__PREF_KEY_SHOW_CREATED, m_bShowCreatedTime);
        
    	  m_sMensaLocation = sharedPref.getString (MensaMealsSettings.PREF_KEY_MENSA_LOCATION, m_sMensaLocation); 
        
//        m_iSortOrder = Integer.parseInt (sSortOrder);
//        
          m_sThemes = sharedPref.getString (MensaMealsSettings.PREF_KEY_THEMES, m_sThemes);
//        m_sTimeFormat = sharedPref.getString (MensaMealsSettings.__PREF_KEY_TIME_FORMAT, __TIME_FORMAT_DEFAULT);
      }
    }
  }
}
