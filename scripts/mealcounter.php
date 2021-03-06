<?php 
/*
 * Copyright (C) 2011 Daniel Jacobi
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


/*
 * This scripts counts votes from TUMensa Android App
 * mealid with length 1 is just readfile, with more letters its a new entry.
 * example call: .../mealcounter.php?mealid=test123&date=20110101&vote1=2&vote2=4&vote3=5
 */
if (array_key_exists('mealid', $_REQUEST) 
	&& array_key_exists('vote1', $_REQUEST) // visual
	&& array_key_exists('vote2', $_REQUEST) // price
	&& array_key_exists('vote3', $_REQUEST) // taste
	&& array_key_exists('date', $_REQUEST)) {
 
	$new_mealid = $_REQUEST['mealid'];
	$new_vote1 = $_REQUEST['vote1'];
	$new_vote2 = $_REQUEST['vote2'];
	$new_vote3 = $_REQUEST['vote3'];
	$new_date = $_REQUEST['date'];
	
	// plausi-test
	if (!(is_numeric($new_vote1) && is_numeric($new_vote2) 
		&& is_numeric($new_vote3) && is_string($new_mealid)
		&& is_string($new_date)
		&& $new_vote1 >= 0 && $new_vote1 <= 5 
		&& $new_vote2 >= 0 && $new_vote2 <= 5 
		&& $new_vote3 >= 0 && $new_vote3 <= 5
		&& strlen($new_date) == 9
		&& strlen($new_mealid) < 50)) {
		die ("ERR: don't trick me!");	
	}
	
	$filename = $new_date.".txt";
	if (!file_exists($filename)) {
			touch($filename);
	}
	$meallist = file ($filename);
	$newmeallist = array(); 
	
	if (strlen($new_mealid) > 1) {
		$entry_changed = false;
	
		while (file_exists("lock.set")) {  }
	
		touch("lock.set");
		
		// change data
		$i = 0;
		for (; $i < count($meallist); $i++) {
		
			list($mealid, $vote1, $count1, $vote2, $count2, $vote3, $count3) = explode(" ", $meallist[$i]);
	
			if (strcmp($new_mealid, $mealid) == 0) {
				// new votes
				if ($new_vote1 != 0) {
					$vote1 = (($vote1 * $count1) + $new_vote1) / ($count1 + 1);
					$count1 = $count1 + 1;
				}
				
				if ($new_vote2 != 0) {
					$vote2 = (($vote2 * $count2) + $new_vote2) / ($count2 + 1);
					$count2 = $count2 + 1;
				}
	
				if ($new_vote3 != 0) {
					$vote3 = (($vote3 * $count3) + $new_vote3) / ($count3 + 1);
					$count3 = $count3 + 1;
				}
	
				$newmeallist[$i] = $mealid." ".$vote1." ".$count1." ".$vote2." ".$count2." ".$vote3." ".$count3;//."\n";
				$entry_changed = true;
			} else {
				$newmeallist[$i] = $meallist[$i];
			}
//			echo $newmeallist[$i];
		}
		
		// add new entry
		if (!$entry_changed) {
				// new votes
				$mealid = $new_mealid;
				
				$vote1 = $new_vote1;
				if ($new_vote1 != 0) {
					$count1 = 1;
				} else {
					$count1 = 0;
				}
				
				$vote2 = $new_vote2;
				if ($new_vote2 != 0) {
					$count2 = 1;
				} else {
					$count2 = 0;
				}
				
				$vote3 = $new_vote3;
				if ($new_vote3 != 0) {
					$count3 = 1;
				} else {
					$count3 = 0;
				}
	
				$newmeallist[$i] = "\n".$mealid." ".$vote1." ".$count1." ".$vote2." ".$count2." ".$vote3." ".$count3;//."\n" ;
//				echo $newmeallist[$i];
		}
	
		$tmpname = tempnam("/tmp", "FOO");
		chmod ($tmpname, 0644);
		
		$fp = fopen ($tmpname, "w");
	
		// save new file
		for ($i = 0; $i < count($newmeallist); $i++) {
			fwrite($fp, $newmeallist[$i]);
		}
	
		fclose ($fp); 
		
		rename($tmpname, $filename);
	
		unlink("lock.set");
	
		echo "OK.";
	} else {
		if ($meallist) {
			for ($i = 0; $i < count($meallist); $i++) 
				echo $meallist[$i];
		}
	}

} else {
	die ("ERR: parameters missing.");
}
?>
