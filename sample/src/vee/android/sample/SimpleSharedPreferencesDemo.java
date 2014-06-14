
package vee.android.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import vee.android.lib.SimpleSharedPreferences;
import vee.android.sample.Constants.KEYS;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleSharedPreferencesDemo extends Activity {

    private static SimpleSharedPreferences mPreferences;
    @SuppressLint("DefaultLocale")
    private final String tag = getClass().getSimpleName().toUpperCase(Locale.ENGLISH);
    private final static String mValue = "Value in Pref : ";
    private static TextView mPrefResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefResult = (TextView) findViewById(R.id.pref_result);

        /*
         * Initialization
         */
        mPreferences = new SimpleSharedPreferences(getApplicationContext());

        /*
         * Put Boolean
         */
        mPreferences.putBoolean(KEYS.VEE_BOOL, true);

        /*
         * Put Float
         */
        mPreferences.putFloat(KEYS.VEE_FLOAT, 2.3f);

        /*
         * Put Integer
         */
        mPreferences.putInt(KEYS.VEE_INT, 50);

        /*
         * Put Long
         */
        mPreferences.putLong(KEYS.VEE_LONG, 12345678910L);

        /*
         * Put String
         */
        mPreferences.putString(KEYS.VEE_STRING, tag);

        /*
         * Create String set
         */
        final Set<String> mStringSet = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            mStringSet.add("String" + i);
        }

        /*
         * Put String Set
         */
        mPreferences.putStringSet(KEYS.VEE_STRING_SET, mStringSet);

        /*
         * Called when a shared preference is changed, added, or removed. This may be called even if a preference is set
         * to its existing value.
         */
        mPreferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);

        /*
         * Update `String` to see if "OnSharedPreferenceChangeListener" is triggered
         */
        mPreferences.putString(KEYS.VEE_STRING, "UPDATED String");

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPreferences.incrementAppOpenedCount();
    }

    /**
     * Increment App Opened COunt
     */
    public void incrementAppOpenedCount(View v) {
        mPreferences.incrementAppOpenedCount();
    }

    /**
     * Get Boolean
     */
    public void getBoolean(View v) {
        setText(mValue + mPreferences.getBoolean(KEYS.VEE_BOOL, false));
    }

    /**
     * Get Float
     */
    public void getFloat(View v) {
        setText(mValue + mPreferences.getFloat(KEYS.VEE_FLOAT, 0f));
    }

    /**
     * Get Integer
     */
    public void getInt(View v) {
        setText(mValue + mPreferences.getInt(KEYS.VEE_INT, -1));
    }

    /**
     * Get Long
     */
    public void getLong(View v) {
        setText(mValue + mPreferences.getLong(KEYS.VEE_LONG, -1L));
    }

    /**
     * Get String
     */
    public void getString(View v) {
        setText(mValue + mPreferences.getString(KEYS.VEE_STRING, ""));
    }

    /**
     * Get StringSet
     */
    public void getStringSet(View v) {

        final Set<String> mSet = mPreferences.getStringSet(KEYS.VEE_STRING_SET, null);
        if (mSet != null) {
            final StringBuilder mBuilder = new StringBuilder();
            for (final String string : mSet) {
                Log.w(tag, string);
                mBuilder.append("\n\n " + string);
            }
            setText(mValue + mBuilder.toString());
        }
        else {
            setText(mValue + "null");
        }
    }

    /**
     * Get All
     */
    public void getAll(View v) {

        final Map<String, ?> map = mPreferences.getAll();

        final StringBuilder mBuilder = new StringBuilder();
        for (final Entry<String, ?> entry : map.entrySet()) {
            final String keyx = entry.getKey();
            final Object value = entry.getValue();
            mBuilder.append("\n\nPrefrences key : " + keyx + " , " + "value : " + value.toString());
        }
        setText(mBuilder.toString());
    }

    /**
     * Get boolean Value in SharedPreferences<br>
     * using wrong Key.
     */
    public void getWrongData(View v) {
        try {
            mPreferences.getBoolean(KEYS.VEE_STRING, false);
        }
        catch (final ClassCastException e) {
            setText("Error:" + e.getMessage());
        }
    }

    /**
     * Get App Opened Count
     */
    public void getAppOpenedCount(View v) {
        setText(mValue + mPreferences.getAppOpenedCount());
    }

    /**
     * Clears all Value in {@link SharedPreferences}
     */
    public void Clear(View v) {
        mPreferences.clear();
        setText("Cleared All");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences.unregisterOnSharedPreferenceChangeListener(mPreferenceChangeListener);
    }

    private final OnSharedPreferenceChangeListener mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences , String key) {

            final Map<String, ?> map = sharedPreferences.getAll();

            for (final Entry<String, ?> entry : map.entrySet()) {
                final String keyx = entry.getKey();
                final Object value = entry.getValue();
                if (keyx.equalsIgnoreCase(key)) {
                    Log.w(tag, "New/Updated Prefrences key : " + keyx + " , " + "value : " + value.toString());
                    break;
                }
            }

        }
    };

    /**
     * Set's Preferences Value in UI
     *
     * @param text
     */
    private void setText(final String text) {
        mPrefResult.setText(text);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        Log.v(tag, mValue + text);
    }
}
