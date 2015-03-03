package com.jbl.browser.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jbl.browser.R;
import com.jbl.browser.utils.AnimationManager;
import com.jbl.browser.utils.Constants;
import com.jbl.browser.utils.Controller;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.CustomWebView;
import com.jbl.browser.view.CustomWebViewClient;

public class MultipageActivity extends Activity{
	public static MultipageActivity INSTANCE = null;
	private static final int CONTEXT_MENU_OPEN_IN_NEW_TAB = Menu.FIRST + 11;
	protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
	        new FrameLayout.LayoutParams(
	        ViewGroup.LayoutParams.MATCH_PARENT,
	       ViewGroup.LayoutParams.MATCH_PARENT);
	protected LayoutInflater mInflater = null;
	private ImageView mPreviousTabView;
	private ImageView mNextTabView;
	private CustomWebView mCurrentWebView;
	private List<CustomWebView> mWebViews;
	private TextView mNewTabButton;
	private ImageButton mRemoveTabButton;
	private boolean mUrlBarVisible;
	private ViewFlipper mViewFlipper;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);              
        INSTANCE = this;
        Constants.initializeConstantsFromResources(this);
        Controller.getInstance().setPreferences(PreferenceManager.getDefaultSharedPreferences(this));       
        if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)) {        	
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS, true)) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setProgressBarVisibility(true);
        setContentView(R.layout.multipage);   
        mUrlBarVisible = true;
    	mWebViews = new ArrayList<CustomWebView>();
    	Controller.getInstance().setWebViewList(mWebViews);
    	mViewFlipper = (ViewFlipper) findViewById(R.id.View_filper);
    	mPreviousTabView = (ImageView) findViewById(R.id.PreviousTabView);
    	mPreviousTabView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showPreviousTab(true);
			}
		});
    	mPreviousTabView.setVisibility(View.GONE);
    	mNextTabView = (ImageView) findViewById(R.id.NextTabView);
    	mNextTabView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showNextTab(true);
			}
		});
    	mNextTabView.setVisibility(View.GONE);
		mNewTabButton = (TextView) findViewById(R.id.NewTabBtn);
		mNewTabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	addTab(true);
            }          
        });
		mRemoveTabButton = (ImageButton) findViewById(R.id.RemoveTabBtn);
		mRemoveTabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (mViewFlipper.getChildCount() == 1 && !mCurrentWebView.getUrl().equals(Constants.URL_ABOUT_START)) {
            		navigateToHome();
                	updateUI();
                	updatePreviousNextTabViewsVisibility();
            	}
            	else
            		removeCurrentTab();
            }          
        });
        getResources().getDrawable(R.drawable.spinner);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  buildComponents();                
        mViewFlipper.removeAllViews();   
        Intent i = getIntent();
        if (i.getData() != null) {
        	addTab(false);
        	navigateToUrl(i.getDataString());
        } else {
        	boolean lastPageRestored = false;
        	if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.PREFERENCES_BROWSER_RESTORE_LAST_PAGE, false)) {
        		if (savedInstanceState != null) {        		
        			String savedUrl = savedInstanceState.getString(Constants.EXTRA_SAVED_URL);
        			if (savedUrl != null) {
        				addTab(false);
        				navigateToUrl(savedUrl);
        				lastPageRestored = true;
        			}
        		}
        	}
        	if (!lastPageRestored) {
        		addTab(true);
        	}
        }
    }  
	/*public void buildComponents() {
    	mUrlBarVisible = true;
    	mWebViews = new ArrayList<CustomWebView>();
    	Controller.getInstance().setWebViewList(mWebViews);
    	mViewFlipper = (ViewFlipper) findViewById(R.id.View_filper);
    	mFindBar = (LinearLayout) findViewById(R.id.findControls);
    	mFindBar.setVisibility(View.GONE);
    	mPreviousTabView = (ImageView) findViewById(R.id.PreviousTabView);
    	mPreviousTabView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showPreviousTab(true);
			}
		});
    	mPreviousTabView.setVisibility(View.GONE);
    	mNextTabView = (ImageView) findViewById(R.id.NextTabView);
    	mNextTabView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showNextTab(true);
			}
		});
    	mNextTabView.setVisibility(View.GONE);
    	mUrlEditText = (AutoCompleteTextView) findViewById(R.id.UrlText);
    	mUrlEditText.setThreshold(1);
    	mUrlEditText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {												
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					navigateToUrl();
					return true;
				}
				return false;
			}
    	});
    	mUrlEditText.addTextChangedListener(mUrlTextWatcher);  	
    	mUrlEditText.setCompoundDrawablePadding(5);	
    	mGoButton = (ImageButton) findViewById(R.id.GoBtn);    	
    	mGoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (mCurrentWebView.isLoading()) {
            		mCurrentWebView.stopLoading();
            	} else if (!mCurrentWebView.isSameUrl(mUrlEditText.getText().toString())) {
            		navigateToUrl();
            	} else {
            		mCurrentWebView.reload();
            	}
            }          
        });
    	mProgressBar = (ProgressBar) findViewById(R.id.WebViewProgress);
    	mProgressBar.setMax(100);
    	mPreviousButton = (ImageButton) findViewById(R.id.PreviousBtn);
    	mNextButton = (ImageButton) findViewById(R.id.NextBtn);
		mNewTabButton = (ImageButton) findViewById(R.id.NewTabBtn);
		mNewTabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	addTab(true);
            }          
        });
		mRemoveTabButton = (ImageButton) findViewById(R.id.RemoveTabBtn);
		mRemoveTabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (mViewFlipper.getChildCount() == 1 && !mCurrentWebView.getUrl().equals(Constants.URL_ABOUT_START)) {
            		navigateToHome();
                	updateUI();
                	updatePreviousNextTabViewsVisibility();
            	}
            	else
            		removeCurrentTab();
            }          
        });
    }*/
    private void initializeCurrentWebView() {
    	mCurrentWebView.setWebViewClient(new CustomWebViewClient(this));
		mCurrentWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onCreateWindow(WebView view, final boolean dialog, final boolean userGesture, final Message resultMsg) {
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				addTab(false, mViewFlipper.getDisplayedChild());
				transport.setWebView(mCurrentWebView);
				resultMsg.sendToTarget();
				return true;
			}
			@Override
			public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
				final LayoutInflater factory = LayoutInflater.from(MultipageActivity.this);
                final View v = factory.inflate(R.layout.javascript_prompt_dialog, null);
                ((TextView) v.findViewById(R.id.JavaScriptPromptMessage)).setText(message);
                ((EditText) v.findViewById(R.id.JavaScriptPromptInput)).setText(defaultValue);
                return true;
			}		
		});
    }
    private void addTab(boolean navigateToHome) {
    	addTab(navigateToHome, -1);
    }
    private void addTab(boolean navigateToHome, int parentIndex) {
    	RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.webview, mViewFlipper, false);
    	mCurrentWebView = (CustomWebView) view.findViewById(R.id.webview);
    	initializeCurrentWebView();    		
    	synchronized (mViewFlipper) {
    		if (parentIndex != -1) {
    			mWebViews.add(parentIndex + 1, mCurrentWebView);    		
    			mViewFlipper.addView(view, parentIndex + 1);
    		} else {
    			mWebViews.add(mCurrentWebView);
    			mViewFlipper.addView(view);
    		}
    		mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(view));    		
    	}
    	updateUI();
    	updatePreviousNextTabViewsVisibility();
    	if (navigateToHome) {
    		navigateToHome();
    	}
    }
    private void removeCurrentTab() {
    	int removeIndex = mViewFlipper.getDisplayedChild();
    	mCurrentWebView.doOnPause();
    	synchronized (mViewFlipper) {
    		mViewFlipper.removeViewAt(removeIndex);
    		mViewFlipper.setDisplayedChild(removeIndex - 1);    		
    		mWebViews.remove(removeIndex);    		
    	}
    	mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());
    	updateUI();
    	updatePreviousNextTabViewsVisibility();
    	
    }
    private void navigateToUrl(String url) {
    	
    	if ((url != null) &&
    			(url.length() > 0)) {		    	
    		if (url.equals(Constants.URL_ABOUT_START)) {
    			mCurrentWebView.loadUrl(UrlUtils.URL_GET_HOST);	
    		} else {
    			// If the url is not from GWT mobile view, and is in the mobile view url list, then load it with GWT.
    			if ((!url.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT)) 
    					) {
    				
    				url = String.format(Constants.URL_GOOGLE_MOBILE_VIEW, url);    				
    			}
    			mCurrentWebView.loadUrl(url);    			
    		}
    	}
    }        
    
    private void navigateToHome() {
    	navigateToUrl(Controller.getInstance().getPreferences().getString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
    			Constants.URL_ABOUT_START));
    }
	private void clearTitle() {
		this.setTitle(getResources().getString(R.string.ApplicationName));
    }
	private void updateTitle() {
		String value = mCurrentWebView.getTitle();
    	if ((value != null) &&
    			(value.length() > 0)) {    	
    		this.setTitle(String.format(getResources().getString(R.string.ApplicationNameUrl), value));    		
    	} else {
    		clearTitle();
    	}
	}
	
	private void updateUI() {
		if (mCurrentWebView.getUrl() != null)
			mRemoveTabButton.setEnabled((mViewFlipper.getChildCount() > 1 || !mCurrentWebView.getUrl().equals(Constants.URL_ABOUT_START)));
		else
			mRemoveTabButton.setEnabled(mViewFlipper.getChildCount() > 1);
		updateTitle();
	}
	@Override
	protected void onPause() {
		mCurrentWebView.doOnPause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		mCurrentWebView.doOnResume();
		super.onResume();
	}
	private void showToastOnTabSwitch() {
		if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_SHOW_TOAST_ON_TAB_SWITCH, true)) {
			String text;
			if (mCurrentWebView.getTitle() != null) {
				text = String.format(getString(R.string.Main_ToastTabSwitchFullMessage), mViewFlipper.getDisplayedChild() + 1, mCurrentWebView.getTitle());
			} else {
				text = String.format(getString(R.string.Main_ToastTabSwitchMessage), mViewFlipper.getDisplayedChild() + 1);
			}
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		}		
	}
	private void updatePreviousNextTabViewsVisibility() {
    	if ((mUrlBarVisible) 
    			) {
    		if (mViewFlipper.getDisplayedChild() > 0) {
    			mPreviousTabView.setVisibility(View.VISIBLE);
    		} else {
    			mPreviousTabView.setVisibility(View.GONE);
    		}

    		if (mViewFlipper.getDisplayedChild() < mViewFlipper.getChildCount() - 1) {
    			mNextTabView.setVisibility(View.VISIBLE);
    		} else {
    			mNextTabView.setVisibility(View.GONE);
    		}
    	} else {
    		mPreviousTabView.setVisibility(View.GONE);
    		mNextTabView.setVisibility(View.GONE);
    	}
    }
	private void showPreviousTab(boolean resetToolbarsRunnable) {
		if (mViewFlipper.getChildCount() > 1) {
			mCurrentWebView.doOnPause();
			mViewFlipper.setInAnimation(AnimationManager.getInstance().getInFromLeftAnimation());
			mViewFlipper.setOutAnimation(AnimationManager.getInstance().getOutToRightAnimation());
			mViewFlipper.showPrevious();
			mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());
			mCurrentWebView.doOnResume();
			showToastOnTabSwitch();
			updatePreviousNextTabViewsVisibility();
			updateUI();
		}
	}
	private void showNextTab(boolean resetToolbarsRunnable) {
		if (mViewFlipper.getChildCount() > 1) {
			mCurrentWebView.doOnPause();
			mViewFlipper.setInAnimation(AnimationManager.getInstance().getInFromRightAnimation());
			mViewFlipper.setOutAnimation(AnimationManager.getInstance().getOutToLeftAnimation());
			mViewFlipper.showNext();
			mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());
			mCurrentWebView.doOnResume();
			showToastOnTabSwitch();
			updatePreviousNextTabViewsVisibility();
			updateUI();
		}
	}
	public void onPageFinished(String url) {
		updateUI();			
		if ((Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_ADBLOCKER_ENABLE, true))
		) {
			mCurrentWebView.loadAdSweep();
		}
		WebIconDatabase.getInstance().retainIconForPageUrl(mCurrentWebView.getUrl());
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if ((item != null) &&
				(item.getIntent() != null)) {
			Bundle b = item.getIntent().getExtras();
			switch(item.getItemId()) {
			case CONTEXT_MENU_OPEN_IN_NEW_TAB:
				if (b != null) {
					addTab(false, mViewFlipper.getDisplayedChild());
					navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
				}			
				return true;
			default: return super.onContextItemSelected(item);
			}	
		}
		return super.onContextItemSelected(item);
	}
}
