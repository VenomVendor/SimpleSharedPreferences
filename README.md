# Android SimpleSharedPreferences
Use SimpleSharedPreferences to avoid unnecessary code while writing and fetching from [SharedPreferences][1]

## Usage
Import [library][7], or add any one [*.jar][8] into `/libs`
<pre>
SimpleSharedPreferences mPreferences = new SimpleSharedPreferences(getApplicationContext());
mPreferences.putString("STRING_KEY", "STRING_VALUE");
mPreferences.putInt("INTEGER_KEY", 50);  // Put String
mPreferences.getString("STRING_KEY", "STRING_DEF_VALUE"); // Get String
</pre>

##Demo
[Sample][9] <br>
[Sample.apk][10]

###Note
 - Can be used beside [SharedPreferences][6] without any conflict.
 - Usage of <b>deprecated</b> methods throws <u>*InstantiationError*</u>.
 - <kbd>SimpleSharedPreferences#apply()</kbd> is available only after **<u>API-9</u>**
 - All Methods in `SharedPreferences` & `SharedPreferences.Editor` are available in `SimpleSharedPreferences`.

## Why?
 - Was bored of using  [SharedPreferences.edit()][2] and [Editor.commit()][3] every time to push data into SharedPreferences.
 - **[putStringSet][4] /  [getStringSet][5]** can be used <kbd>**pre API level 11**</kbd>.
 - Throws Exact **Exception** when Wrong Key is passed.
 - To set **"STRING_VALUE"** & **50**(INT_VALUE) Seperately in SharedPreferences.

<pre>
// Old boring code
SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
SharedPreferences.Editor mEditor = mPreferences.edit();
mEditor.putString("STRING_KEY", "STRING_VALUE"); // mEditor
mEditor.commit(); // mEditor
...
...
...
mEditor.putInt("INTEGER_KEY", 50); // mEditor
mEditor.commit(); // mEditor
...
mPreferences.getString("STRING_KEY", "STRING_DEF_VALUE");  // mPreferences
...
</pre>


### imports
	import vee.android.lib.SimpleSharedPreferences;

### ProGuard
	-keepclassmembers class * implements android.content.SharedPreferences.** { *; }

###### Author : [VenomVendor](https://www.google.com/#newwindow=1&q=VenomVendor "Find me on Google")

#License
	Copyright (C) 2014 VenomVendor <info@VenomVendor.com>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	
 [1]: http://developer.android.com/reference/android/content/SharedPreferences.html "SharedPreferences"
 [2]: http://developer.android.com/reference/android/content/SharedPreferences.html#edit%28%29
 [3]: http://developer.android.com/reference/android/content/SharedPreferences.Editor.html#commit%28%29
 [4]: http://developer.android.com/reference/android/content/SharedPreferences.Editor.html#putStringSet%28java.lang.String,%20java.util.Set%3Cjava.lang.String%3E%29 "Added in API level 11"
 [5]: http://developer.android.com/reference/android/content/SharedPreferences.html#getStringSet%28java.lang.String,%20java.util.Set%3Cjava.lang.String%3E%29 "Added in API level 11"
 [6]: http://developer.android.com/training/basics/data-storage/shared-preferences.html#WriteSharedPreference "Using Shared Preferences"
 [7]: https://github.com/VenomVendor/SimpleSharedPreferences/tree/master/library/
 [8]: https://github.com/VenomVendor/SimpleSharedPreferences/tree/master/library/bin/
 [9]: https://github.com/VenomVendor/SimpleSharedPreferences/tree/master/sample/src/vee/android/sample/SimpleSharedPreferencesDemo.java "Sample for SimpleSharedPreferences"
 [10]: https://github.com/VenomVendor/SimpleSharedPreferences/tree/master/sample/bin