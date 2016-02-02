package com.venomvendor.sample.simplesharedpreferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.venomvendor.library.SimpleSharedPreferences;
import com.venomvendor.sample.simplesharedpreferences.utils.Constants.KEYS;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleSharedPreferencesDemo extends Activity {

    private final static String VALUE_IN_PREF = "Value in Pref : ";
    private static final String TAG = SimpleSharedPreferencesDemo.class.getSimpleName();
    private SimpleSharedPreferences mPreferences;
    private TextView mPrefResult;
    private OnSharedPreferenceChangeListener mPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initListeners();

        addData();
    }

    private void initViews() {
        mPrefResult = (TextView) findViewById(R.id.pref_result);
    }

    private void initListeners() {
        /*
         * Initialization
         */
        mPreferences = SimpleSharedPreferences.getInstance();

        mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                final Map<String, ?> map = sharedPreferences.getAll();

                for (final Entry<String, ?> entry : map.entrySet()) {
                    final String entryKey = entry.getKey();
                    final Object value = entry.getValue();
                    if (entryKey.equalsIgnoreCase(key)) {
                        Log.d(TAG, "New/Updated Preferences key : " + entryKey + " , " + "value : " + value.toString());
                        break;
                    }
                }

            }
        };
    }

    private void addData() {

        /*
         * Put Boolean
         */
        mPreferences.putBoolean(KEYS.VEE_BOOL, true);

        /*
         * Put Float
         */
        mPreferences.putFloat(KEYS.VEE_FLOAT, 2.3f);

        /* put multiple key value pairs */
        mPreferences.putInt(KEYS.VEE_INT, 50) /* Put Integer */
                .putLong(KEYS.VEE_LONG, 12345678910L) /* Put Long */
                .putString(KEYS.VEE_STRING, TAG); /* Put String */

        /*
         * Create String set
         */
        final Set<String> mStringSet = new LinkedHashSet<String>();
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

    /**
     * Get Boolean
     */
    public void getBoolean(View v) {
        setText(VALUE_IN_PREF + mPreferences.getBoolean(KEYS.VEE_BOOL, false));
    }

    /**
     * Get Float
     */
    public void getFloat(View v) {
        setText(VALUE_IN_PREF + mPreferences.getFloat(KEYS.VEE_FLOAT, 0f));
    }

    /**
     * Get Integer
     */
    public void getInt(View v) {
        setText(VALUE_IN_PREF + mPreferences.getInt(KEYS.VEE_INT, -1));
    }

    /**
     * Get Long
     */
    public void getLong(View v) {
        setText(VALUE_IN_PREF + mPreferences.getLong(KEYS.VEE_LONG, -1L));
    }

    /**
     * Get String
     */
    public void getString(View v) {
        setText(VALUE_IN_PREF + mPreferences.getString(KEYS.VEE_STRING, ""));
    }

    /**
     * Get StringSet
     */
    public void getStringSet(View v) {

        final Set<String> mSet = mPreferences.getStringSet(KEYS.VEE_STRING_SET, null);
        if (mSet != null) {
            final StringBuilder mBuilder = new StringBuilder();
            for (final String string : mSet) {
                Log.w(TAG, string);
                mBuilder.append("\n\n ")
                        .append(string);
            }
            setText(VALUE_IN_PREF + mBuilder.toString());
        } else {
            setText(VALUE_IN_PREF + "null");
        }
    }

    /**
     * Get All
     */
    public void getAll(View v) {

        final Map<String, ?> map = mPreferences.getAll();

        final StringBuilder mBuilder = new StringBuilder();
        for (final Entry<String, ?> entry : map.entrySet()) {
            final String entryKey = entry.getKey();
            final Object value = entry.getValue();
            mBuilder.append("\n\nPreferences key : ")
                    .append(entryKey).append(" , ")
                    .append("value : ")
                    .append(value.toString());
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
        } catch (final ClassCastException e) {
            setText("Error:" + e.getMessage());
        }
    }

    /**
     * Get App Opened Count
     */
    public void getAppOpenedCount(View v) {
        setText(VALUE_IN_PREF + mPreferences.getAppOpenedCount());
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

    /**
     * Set's Preferences Value in UI
     *
     * @param text data
     */
    private void setText(final String text) {
        mPrefResult.setText(text);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        Log.d(TAG, VALUE_IN_PREF + text);
    }
}
