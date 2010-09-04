package de.questmaster.tudmensa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import de.questmaster.tudmensa.R;
import android.app.ProgressDialog;
import android.content.Context;

public class DataExtractor {

	private Context cActivity = null;
	private MealsDbAdapter mDbHelper = null;
	private String firstDate = null;
	private String location = null;

	public DataExtractor(Context c, MealsDbAdapter db) {
		this.mDbHelper = db;
		this.cActivity = c;
	}

	public void retrieveData(String location) {
		ProgressDialog pd = new ProgressDialog(cActivity);
			//ProgressDialog.show(cActivity	, R.string.dialog_updating, R.string.dialog_updating_text, true);
		pd.setTitle(R.string.dialog_updating);
		pd.setMessage(cActivity.getResources().getString(R.string.dialog_updating_text));
		pd.setIndeterminate(true);
		pd.show();
		
		this.location = location;
		parseTable(getWebPage(location, "week"));
		parseTable(getWebPage(location, "nextweek"));

		pd.dismiss();
	}
	
	/* parse Website and store in database */
	private Vector<String> getWebPage(String task, String view) {
		Vector<String> webTable = new Vector<String>();

		try {
			URL uTest = new URL(
					"http://www.studentenwerkdarmstadt.de/index.php?option=com_spk&task="
							+ task + "&view=" + view);
			BufferedReader br = new BufferedReader(new InputStreamReader(uTest.openStream()), 1024);

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
			
			br.close();
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
		int day_index = 0;
		int meal_num = 0;
		String curCounter = "";

		for (String s : tbl) {
			// new Row -> maybe new counter
			if (s.startsWith("<tr>")) {
				if (s.indexOf("&nbsp;") < 0) {
					// get new Counter name
					curCounter = extractData(s);
					meal_num = 0;
				} else if(!curCounter.equals("")) {
					// more meals at one counter
					meal_num++;
				}
			}

			if (s.startsWith("<td")) {
				String tmp = extractData(s);

				// date line
				if (curCounter.compareTo("") == 0 && tmp.length() == 10) { 
					tmp = tmp.substring(6, 10) + tmp.substring(3, 5) + tmp.substring(0, 2);
					days.add(tmp);
					
					if (firstDate == null) {
						firstDate = tmp;
					}
						
				// €-sign unfortunately not encoded, so checking price-tag
				} else if (tmp.lastIndexOf(",") > 0 && 
						Character.isDigit(tmp.charAt(tmp.lastIndexOf(",")-1)) &&
						Character.isDigit(tmp.charAt(tmp.lastIndexOf(",")+1)) &&
						Character.isDigit(tmp.charAt(tmp.lastIndexOf(",")+2))) { 
					
					// price
					tmp = tmp.substring(0, tmp.lastIndexOf(",") + 3);
					String sPrice = tmp.substring(tmp.lastIndexOf(" ") + 1);
					Float price = Float.parseFloat(sPrice.replace(",", "."));
					
					// cut price from string
					tmp = tmp.substring(0, tmp.length() - sPrice.length()).trim();
					
					// type
					String type = tmp.substring(tmp.lastIndexOf(" ") + 1);
					
					// cut type from string
					String meal = tmp.substring(0, tmp.length() - type.length()).trim();
					meal = htmlDecode(meal);
					
					// Add table entry
					String date = days.get(day_index);
					long rowId = 0;
					if (( rowId = mDbHelper.fetchMealId(location, date, curCounter, meal_num)) >= 0) {
						mDbHelper.updateMeal(rowId, location, date, meal_num, curCounter, meal, type, price);
					} else
						mDbHelper.createMeal(location, date, meal_num, curCounter, meal, type, price);
				}
				
				day_index++;
			}

			if (s.startsWith("</tr>")) {
				day_index = 0;
			}
				
			// </table>: end -> clean old entries
			if (s.equals("</table>")) {
				mDbHelper.deleteOldMeal(firstDate);
			}
		}
	}
	
	private String htmlDecode(String in) {
		String out = in;
		
		out = out.replaceAll("&auml;", "ä");
		out = out.replaceAll("&Auml;", "Ä");
		out = out.replaceAll("&ouml;", "ö");
		out = out.replaceAll("&Ouml;", "Ö");
		out = out.replaceAll("&uuml;", "ü");
		out = out.replaceAll("&Uuml;", "Ü");
		out = out.replaceAll("&szlig;", "ß");
		out = out.replaceAll("&amp;", "&");
		out = out.replaceAll("&quot;", "\"");
		
		return out;
	}
	
	private String extractData(String s) {
		// cut end "</td>"
		s = s.substring(0, s.length() - 5);

		// cut begining
		return s.substring(s.lastIndexOf(">") + 1, s.length()).trim();
	}

}
