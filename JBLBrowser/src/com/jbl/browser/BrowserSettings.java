
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jbl.browser;

import java.util.HashMap;
import java.util.Observable;

import android.webkit.WebSettings;

/*
 * Package level class for storing various WebView and Browser settings. To use
 * this class:
 * BrowserSettings s = BrowserSettings.getInstance();
 * s.addObserver(webView.getSettings());
 * s.loadFromDb(context); // Only needed on app startup
 * s.javaScriptEnabled = true;
 * ... // set any other settings
 * s.update(); // this will update all the observers
 *
 * To remove an observer:
 * s.deleteObserver(webView.getSettings());
 */
public class BrowserSettings extends Observable {

    // Private variables for settings
    // NOTE: these defaults need to be kept in sync with the XML
    // until the performance of PreferenceManager.setDefaultValues()
    // is improved.
    // Note: boolean variables are set inside reset function.
    private boolean loadsImagesAutomatically;
    private boolean javaScriptEnabled;
    private WebSettings.PluginState pluginState;
    private boolean javaScriptCanOpenWindowsAutomatically;
 
    private boolean rememberPasswords;
    private boolean saveFormData;

    private String defaultTextEncodingName;

    private boolean loadsPageInOverviewMode;

    // Development settings
    public WebSettings.LayoutAlgorithm layoutAlgorithm =
        WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
    private boolean useWideViewPort = true;
    private int userAgent = 0;

    private boolean lightTouch = false;
    private boolean navDump = false;

    // By default the error console is shown once the user navigates to about:debug.
    // The setting can be then toggled from the settings menu.


    // Private preconfigured values
    private static int minimumFontSize = 8;
    private static int minimumLogicalFontSize = 8;
    private static int defaultFontSize = 16;
    private static int defaultFixedFontSize = 13;
    public static WebSettings.TextSize textSize =
        WebSettings.TextSize.NORMAL;
    private static WebSettings.ZoomDensity zoomDensity =
        WebSettings.ZoomDensity.MEDIUM;


    // Preference keys that are used outside this class
    public final static String PREF_CLEAR_CACHE = "privacy_clear_cache";
    public final static String PREF_CLEAR_COOKIES = "privacy_clear_cookies";
    public final static String PREF_CLEAR_HISTORY = "privacy_clear_history";
    public final static String PREF_HOMEPAGE = "homepage";
    public final static String PREF_SEARCH_ENGINE = "search_engine";
    public final static String PREF_CLEAR_FORM_DATA =
            "privacy_clear_form_data";
    public final static String PREF_CLEAR_PASSWORDS =
            "privacy_clear_passwords";
    public final static String PREF_EXTRAS_RESET_DEFAULTS =
            "reset_default_preferences";
    public final static String PREF_DEBUG_SETTINGS = "debug_menu";
    public final static String PREF_WEBSITE_SETTINGS = "website_settings";
    public final static String PREF_TEXT_SIZE = "text_size";
    public final static String PREF_DEFAULT_ZOOM = "default_zoom";
    public final static String PREF_DEFAULT_TEXT_ENCODING =
            "default_text_encoding";
    public final static String PREF_CLEAR_GEOLOCATION_ACCESS =
            "privacy_clear_geolocation_access";

    private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +
            "U; Intel Mac OS X 10_5_7; en-us) AppleWebKit/530.17 (KHTML, " +
            "like Gecko) Version/4.0 Safari/530.17";

    private static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; " +
            "CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 " +
            "(KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";

    // Value to truncate strings when adding them to a TextView within
    // a ListView
    public final static int MAX_TEXTVIEW_LEN = 80;

   // private TabControl mTabControl;

    // Single instance of the BrowserSettings for use in the Browser app.
    private static BrowserSettings sSingleton;

    // Private map of WebSettings to Observer objects used when deleting an
    // observer.
    private HashMap<WebSettings,Observer> mWebSettingsToObservers =
        new HashMap<WebSettings,Observer>();

    /*
     * An observer wrapper for updating a WebSettings object with the new
     * settings after a call to BrowserSettings.update().
     */
    static class Observer implements java.util.Observer {
        // Private WebSettings object that will be updated.
        private WebSettings mSettings;

        Observer(WebSettings w) {
            mSettings = w;
        }

        public void update(Observable o, Object arg) {
            BrowserSettings b = (BrowserSettings)o;
            WebSettings s = mSettings;

            //s.setLayoutAlgorithm(b.layoutAlgorithm);
            /*if (b.userAgent == 0) {
                // use the default ua string
                s.setUserAgentString(null);
            } else if (b.userAgent == 1) {
                s.setUserAgentString(DESKTOP_USERAGENT);
            } else if (b.userAgent == 2) {
                s.setUserAgentString(IPHONE_USERAGENT);
            }*/
            //s.setUseWideViewPort(b.useWideViewPort);
            //s.setLoadsImagesAutomatically(b.loadsImagesAutomatically);
            //s.setJavaScriptEnabled(b.javaScriptEnabled);
            //s.setPluginState(b.pluginState);
            /*s.setJavaScriptCanOpenWindowsAutomatically(
                    b.javaScriptCanOpenWindowsAutomatically);
            s.setDefaultTextEncodingName(b.defaultTextEncodingName);*/
            //s.setMinimumFontSize(b.minimumFontSize);
            //s.setMinimumLogicalFontSize(b.minimumLogicalFontSize);
            //s.setDefaultFontSize(b.defaultFontSize);
            //s.setDefaultFixedFontSize(b.defaultFixedFontSize);
            //s.setNavDump(b.navDump);
            s.setTextSize(b.textSize);
            //s.setDefaultZoom(b.zoomDensity);
            //s.setLightTouchEnabled(b.lightTouch);
            //s.setSaveFormData(b.saveFormData);
            //s.setSavePassword(b.rememberPasswords);
            //s.setLoadWithOverviewMode(b.loadsPageInOverviewMode);
          //  s.setPageCacheCapacity(pageCacheCapacity);

            // WebView inside Browser doesn't want initial focus to be set.
            //s.setNeedInitialFocus(false);
            // Browser supports multiple windows
            //s.setSupportMultipleWindows(true);
           
        }
    }



    /**
     * Add a WebSettings object to the list of observers that will be updated
     * when update() is called.
     *
     * @param s A WebSettings object that is strictly tied to the life of a
     *            WebView.
     */
    public Observer addObserver(WebSettings s) {
        Observer old = mWebSettingsToObservers.get(s);
        if (old != null) {
            super.deleteObserver(old);
        }
        Observer o = new Observer(s);
        mWebSettingsToObservers.put(s, o);
        super.addObserver(o);
        return o;
    }

    /**
     * Delete the given WebSettings observer from the list of observers.
     * @param s The WebSettings object to be deleted.
     */
    public void deleteObserver(WebSettings s) {
        Observer o = mWebSettingsToObservers.get(s);
        if (o != null) {
            mWebSettingsToObservers.remove(s);
            super.deleteObserver(o);
        }
    }

    /*
     * Package level method for obtaining a single app instance of the
     * BrowserSettings.
     */
    /*package*/ public static BrowserSettings getInstance() {
        if (sSingleton == null ) {
            sSingleton = new BrowserSettings();
        }
        return sSingleton;
    }

    /*package*/ public void update() {
        setChanged();
        notifyObservers();
    }
    
    // Private constructor that does nothing.
    private BrowserSettings() {
        reset();
    }

    private void reset() {
        // Private variables for settings
        // NOTE: these defaults need to be kept in sync with the XML
        // until the performance of PreferenceManager.setDefaultValues()
        // is improved.
        loadsImagesAutomatically = true;
        javaScriptEnabled = true;
        pluginState = WebSettings.PluginState.ON;
        javaScriptCanOpenWindowsAutomatically = false;
        rememberPasswords = true;
        saveFormData = true;
        loadsPageInOverviewMode = true;
    }
}
