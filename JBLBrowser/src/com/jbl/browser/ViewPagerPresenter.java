/*package com.jbl.browser;
import java.util.ArrayList;
import java.util.List;

import com.jbl.browser.adapter.MyListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

public class ViewPagerPresenter  
{  
    private static final String TAG = "ViewPagerPresenter";  
    private static final int PAGE_SIZE = 8; // 每页显示的数据个数  
    private static final int TEST_LIST_SIZE = 43; // 数据总长度  
    private static int sTotalPages = 1;  
    private int mCurrentPage;  
    private List<MyListAdapter> mAdapters;  
    private List<List<String>> mPageList;  
    private List<GridView> mGridViews;  
    private List<View> mViewPages;  
    private Context mContext;  
    *//** 菜单文字 **//*
    private String [] str=new String[]{"添加书签","书签","刷新","历史","夜间模式",
 		   "关闭无图","下载管理","退出","旋转屏幕","翻页按钮","无痕浏览","全屏浏览",
 		   "更换壁纸","省流加速","阅读模式","设置","关于","意见反馈","检查更新","页内查找","保存网页"};

    public ViewPagerPresenter(Context context) {  
    	mContext = context;  
        mPageList = new ArrayList<List<String>>();  
        mGridViews = new ArrayList<GridView>();  
        mAdapters = new ArrayList<MyListAdapter>();  
        mViewPages = new ArrayList<View>();  
        initPages(getTestList());  
        initViewAndAdapter();  
    }  
  
    *//** 
     * 将数据分页 
     * @param list 
     *//*  
    public void initPages(List<String> list)  
    {  
        if (list.size() % PAGE_SIZE == 0) {  
            sTotalPages = list.size() / PAGE_SIZE;  
        } else {  
            sTotalPages = list.size() / PAGE_SIZE + 1;  
        }  
        mCurrentPage = 0;  
        List<String> l = new ArrayList<String>();  
        for (int i = 0; i < list.size(); ++i) {  
            l.add(list.get(i));  
            if ((i + 1) % PAGE_SIZE == 0) {  
                mPageList.add(l);  
                l = new ArrayList<String>();  
            }  
        }  
        if (l.size() > 0) {  
            mPageList.add(l);  
        }  
        
    }  
  
    *//** 
     * 模拟数据 
     * @return 
     *//*  
    public List<String> getTestList()  
    {  
        List<String> strs = new ArrayList<String>();  
        for (int i = 0; i < str.length; ++i) {  
            String s = str[i].toString();  
            strs.add(s);  
        }  

        return strs;  
    }  
    private void initViewAndAdapter()  
    {  
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < sTotalPages; ++i) {  
            View v = inflater.inflate(R.layout.viewpager_gridview, null);  
            GridView lv = (GridView) v.findViewById(R.id.viewpage_grid);  
            mGridViews.add(lv);  
            MyListAdapter adapter = new MyListAdapter(mContext, mPageList.get(i));  
            mAdapters.add(adapter);  
            lv.setAdapter(adapter);  
            mViewPages.add(v);  
        }  
    }  
    public List<View> getPageViews()  
    {  
        return mViewPages;  
    }  
  
}  */