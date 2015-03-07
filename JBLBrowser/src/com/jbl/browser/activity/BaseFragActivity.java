package com.jbl.browser.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.R;
import com.jbl.browser.fragment.SettingPagerFragment;

/**
 * fragment activity 基类
 * 抽象fragment activity公共方法
 * @author Desmond
 *
 */
public abstract class BaseFragActivity extends SherlockFragmentActivity {

	/**
     * Navigate to a specific fragment
     * 
     * @param fragmentClass
     * @param bundle
     *            May be null
     * @param addToBackstack
     * @param fragmentTag
     *            May be null
     * @author MAB
     */
    //当前fragment与之前是否是同一个名字，如果不是,把bundle，加入当前的
    public void navigateTo(Class<?> fragmentClass, Bundle bundle, boolean addToBackstack, String fragmentTag) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        String curr_fragment_TAG = "";
        if (currentFragment != null) {
        	                             
            curr_fragment_TAG = currentFragment.getClass().getSimpleName();
        }

        if (!curr_fragment_TAG.contentEquals(fragmentClass.getSimpleName())) {
            try {
                currentFragment = (Fragment) fragmentClass.newInstance();
                if (bundle != null) {
                    currentFragment.setArguments(bundle);
                }

                FragmentManager fragManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragManager.beginTransaction();
                transaction.replace(R.id.fragment_container, currentFragment);
                if (addToBackstack) {
                    transaction.addToBackStack(fragmentTag);//加入退回栈
                }
                
                /* this is not the proper resolve IllegalStateException: Can not perform this action after onSaveInstanceState */
                transaction.commitAllowingStateLoss();

                // Force the transaction to happen immediately and not be scheduled for later
                getSupportFragmentManager().executePendingTransactions();
                

            } catch (Exception e) {
                e.printStackTrace();
                // Do nothin'
            }
        } else {
            // Do nothin'. Same fragment
        }
    }

    /**
     * Navigate to a specific fragment
     * 
     * @param fragmentClass
     * @param bundle
     *            May be null
     * @param addToBackstack
     * @author MAB
     */
    public void navigateTo(Class<?> fragmentClass, Bundle bundle, boolean addToBackstack) {
        navigateTo(fragmentClass, bundle, addToBackstack, null);
    }

    /**
     * Navigate to a specific fragment
     * 
     * @param fragmentClass
     * @param bundle
     *            May be null
     * @author MAB
     */
    public void navigateTo(Class<?> fragmentClass, Bundle bundle) {
        navigateTo(fragmentClass, bundle, false);

    }

    /**
     * Navigate to a specific fragment
     * 
     * @param fragmentClass
     * @author MAB
     */
    public void navigateTo(Class<?> fragmentClass) {
        navigateTo(fragmentClass, null, false);

    }

    /**
     * Clears all the fragments in the backstack
     * 
     * @author MAB
     */
    //清空fragments的back stack
    public void clearBackstack() {
        FragmentManager fm = getSupportFragmentManager();

        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fm.executePendingTransactions();
    }

    /**
     * Clears all the fragments in the backstack
     * 
     * @author MAB
     */
   // 清空fragments的back stack
    public void clearFragFromBackstack(String tag) {

        if (tag != null) {

            FragmentManager fm = getSupportFragmentManager();

            fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            fm.executePendingTransactions();
        }

    }
    
    @Override
    public void onBackPressed() {
//    	FragmentManager fragManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragManager.beginTransaction();
//        if(transaction.isEmpty())
//        	this.finish();
    	super.onBackPressed();
    }
    
    /**
     * 移除fragment
     * @param frag
     */
    public void removeFragment(Fragment frag){
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(frag);
        transaction.commit();
    }
}
