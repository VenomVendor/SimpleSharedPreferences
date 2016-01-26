/*
 * Copyright (C) 2016 VenomVendor <info@VenomVendor.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.venomvendor.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Modified version of {@link SharedPreferences}.<br>
 * Initiate <b>"SimpleSharedPreferences"</b> by passing {@linkplain Context}.
 * <p/>
 * <pre>
 * SimpleSharedPreferences mPreferences = new SimpleSharedPreferences(getApplicationContext());
 * </pre>
 * <p/>
 * <u>Start <b>Modifying / Accessing</b> data in SimpleSharedPreferences.</u>
 * <p/>
 * <pre>
 * mPreferences.putString(key, value);
 * mPreferences.getString(key, defaultValue);
 * </pre>
 * <p/>
 * <b>DO NOT</b> to use {@link SharedPreferences#edit()} or {@link Editor#commit()} <br>
 * <br>
 * <em> Note:
 * <ul>
 * <li>Usage of <b>deprecated</b> methods throws {@linkplain InstantiationError}.</li>
 * <li>{@link #apply()} is available only after API-9</li>
 * <li>Throws <b>"ClassCastException"</b> if wrong key is passed</li>
 * </ul>
 * </em>
 *
 * @see Context#getSharedPreferences
 * @see SharedPreferences
 */
public class SimpleSharedPreferences implements SharedPreferences, Editor {

    private static final String TAG = SimpleSharedPreferences.class.getSimpleName();
    private static SimpleSharedPreferences mInstance;
    private static SharedPreferences mSharedPreferences;
    private static Editor mEditor;
    private static boolean mEnableLog;

    private SimpleSharedPreferences() {
    }

    /**
     * @param context Application's context.
     *                {@link Context} context
     */
    public static SimpleSharedPreferences initialize(Application context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }
        if (mSharedPreferences != null) {
            throw new ExceptionInInitializerError("SimpleSharedPreferences already initialized.");
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        getInstance().incrementAppOpenedCount();
        return getInstance();
    }

    /**
     * Initiates SharedPreferences.
     */
    public static SimpleSharedPreferences getInstance() {
        initCheck();
        if (mInstance == null) {
            synchronized (SimpleSharedPreferences.class) {
                mInstance = new SimpleSharedPreferences();
            }
        }
        return mInstance;
    }

    /**
     * Check if PreferenceManager is Initialized.
     * Checks SharedPreferences & Creates a new Editor for these preferences if required.
     */
    @SuppressLint("CommitPrefEdits")
    private static void initCheck() {
        if (mSharedPreferences == null) {
            throw new InstantiationError("SimpleSharedPreferences not initialized.\n"
                    + "Initialize by calling 'SimpleSharedPreferences.initialize(this)' "
                    + "from `MyApplication extends Application`\n\n"
                    + "public class MyApplication extends Application {\n" +
                    "    @Override\n" +
                    "    public void onCreate() {\n" +
                    "        super.onCreate();\n" +
                    "        SimpleSharedPreferences.initialize(this);\n" +
                    "        ...\n" +
                    "        ...\n" +
                    "    }\n" +
                    "}");
        }
        if (mEditor == null) {
            mEditor = mSharedPreferences.edit();
        }
    }

    /**
     * @return the status of Log
     */
    public boolean isLogEnabled() {
        return mEnableLog;
    }

    /**
     * Enables/Disables printing LogValues.
     *
     * @param enableLog true is log should be enabled.
     */
    public void enableLog(boolean enableLog) {
        mEnableLog = enableLog;
    }

    /**
     * Increment's App opened count by <b>1</b>
     */
    private void incrementAppOpenedCount() {
        final int appOpenedCount = getAppOpenedCount();
        if (mEnableLog) {
            Log.d(TAG, "Count before updating " + appOpenedCount);
        }
        mEditor.putInt(KEYS.OPENED_TIMES_COUNT, (appOpenedCount + 1));
        apply();
    }

    /**
     * @return The number of times the was opened. This will not autoupdate, everytime the app is opened.<br>
     * You should call {@link #incrementAppOpenedCount()} on {@link Activity#onStart()} to update this.
     */
    public int getAppOpenedCount() {
        return mSharedPreferences.getInt(KEYS.OPENED_TIMES_COUNT, 0);
    }

    @Override
    public SimpleSharedPreferences putString(String key, String value) {
        mEditor.putString(key, value);
        commit();
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences putStringSet(String key, Set<String> values) throws Error {
        final JSONArray jArray = new JSONArray();
        for (final String value : values) {
            jArray.put(value);
        }

        final JSONObject json = new JSONObject();
        try {
            json.put(key, jArray);
            putString(key, json.toString());
            if (mEnableLog) {
                Log.d(TAG, key + ":: " + json.toString());
            }
        } catch (final JSONException ex) {
            throw new Error(ex);
        }
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences putInt(String key, int value) {
        mEditor.putInt(key, value);
        commit();
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences putLong(String key, long value) {
        mEditor.putLong(key, value);
        commit();
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        commit();
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        commit();
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences remove(String key) {
        mEditor.remove(key);
        commit();
        return mInstance;
    }

    @Override
    public SimpleSharedPreferences clear() {
        mEditor.clear();
        commit();
        return mInstance;
    }

    /**
     * <h1><u>Do not use this method</u></h1> <br>
     */
    @Deprecated
    @Override
    public boolean commit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            apply();
            return true;
        }
        return mEditor.commit();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void apply() throws NoSuchMethodError {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mEditor.apply();
        } else {
            mEditor.commit();
        }
    }

    @Override
    public Map<String, ?> getAll() throws ClassCastException {
        return mSharedPreferences.getAll();
    }

    @Override
    public String getString(String key, String defValue) throws ClassCastException {
        try {
            return mSharedPreferences.getString(key, defValue);
        } catch (final ClassCastException e) {
            final String returnType = new Object() {
            }.getClass().getEnclosingMethod().getReturnType().toString();
            throw new ClassCastException(getDetailMessage(key, returnType));
        }
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) throws ClassCastException {
        final String jsonPref = getString(key, null);

        if (jsonPref == null) {
            return defValues;
        }

        final List<String> prefValues = new ArrayList<String>();

        try {
            final JSONArray mJsonArray = new JSONObject(jsonPref).getJSONArray(key);
            for (int i = 0; i < mJsonArray.length(); i++) {
                prefValues.add(mJsonArray.getString(i));
            }
        } catch (final JSONException ex) {
            if (mEnableLog) {
                Log.d(TAG, ex.getLocalizedMessage(), ex);
            }
        }

        try {
            return new HashSet<String>(prefValues);
        } catch (final ClassCastException e) {
            final String returnType = new Object() {
            }.getClass().getEnclosingMethod().getReturnType().toString();
            throw new ClassCastException(getDetailMessage(key, returnType));
        } catch (final Exception ex) {
            if (mEnableLog) {
                Log.d(TAG, ex.getLocalizedMessage(), ex);
            }
            return defValues;
        }
    }

    @Override
    public int getInt(String key, int defValue) throws ClassCastException {
        try {
            return mSharedPreferences.getInt(key, defValue);
        } catch (final ClassCastException e) {
            final String returnType = new Object() {
            }.getClass().getEnclosingMethod().getReturnType().toString();
            throw new ClassCastException(getDetailMessage(key, returnType));
        }
    }

    @Override
    public long getLong(String key, long defValue) throws ClassCastException {
        try {
            return mSharedPreferences.getLong(key, defValue);
        } catch (final ClassCastException e) {
            final String returnType = new Object() {
            }.getClass().getEnclosingMethod().getReturnType().toString();
            throw new ClassCastException(getDetailMessage(key, returnType));
        }
    }

    @Override
    public float getFloat(String key, float defValue) throws ClassCastException {
        try {
            return mSharedPreferences.getFloat(key, defValue);
        } catch (final ClassCastException e) {
            final String returnType = new Object() {
            }.getClass().getEnclosingMethod().getReturnType().toString();
            throw new ClassCastException(getDetailMessage(key, returnType));
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) throws ClassCastException {
        try {
            return mSharedPreferences.getBoolean(key, defValue);
        } catch (final ClassCastException e) {
            final String returnType = new Object() {
            }.getClass().getEnclosingMethod().getReturnType().toString();
            throw new ClassCastException(getDetailMessage(key, returnType));
        }
    }

    private String getDetailMessage(String key, String returnType) {
        return "\n ======================================== \n"
                + "ClassCastException : " + key + "'s value is not a "
                + returnType + " \n ======================================== \n";
    }

    @Override
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * <h1><u>Do not use this method</u></h1> <br>
     */
    @Deprecated
    @Override
    public Editor edit() {
        return mEditor;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Holds Constants for Keys
     */
    class KEYS {
        /**
         * The Constant Key for App Opened Count.
         */
        final static String OPENED_TIMES_COUNT = "VEE_APP_OPENED_TIMES_COUNT";
    }
}
