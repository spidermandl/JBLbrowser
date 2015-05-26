package com.jbl.browser.view;

import com.jbl.browser.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * toggle 属性的imageview
 * @author Desmond
 *
 */
public class ToggleImageView extends ImageView {

	boolean checked=false;
	int valid_id;
	int invalid_id;
	
	public ToggleImageView(Context context) {
		super(context);
		checked=false;
		valid_id=R.drawable.checkbox_iphone_style_on;
		invalid_id=R.drawable.checkbox_iphone_style_off;
		init();
	}
	
	public ToggleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.toggle_item);
		checked=a.getBoolean(R.styleable.toggle_item_toggle_check, false);
		valid_id=a.getResourceId(R.styleable.toggle_item_toggle_valid, R.drawable.checkbox_iphone_style_on);
		invalid_id=a.getResourceId(R.styleable.toggle_item_toggle_invalid, R.drawable.checkbox_iphone_style_off);
		a.recycle();
		init();
	}
	
	public ToggleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.toggle_item);
		checked=a.getBoolean(R.styleable.toggle_item_toggle_check, false);
		valid_id=a.getResourceId(R.styleable.toggle_item_toggle_valid, R.drawable.checkbox_iphone_style_on);
		invalid_id=a.getResourceId(R.styleable.toggle_item_toggle_invalid, R.drawable.checkbox_iphone_style_off);
		a.recycle();
		init();
	}
	
	void init(){
		if(checked)
			this.setImageResource(valid_id);
		else
			this.setImageResource(invalid_id);
	
	}
	
	public void setToggle(){
		checked=!checked;
		
		if(checked)
			this.setImageResource(valid_id);
		else
			this.setImageResource(invalid_id);
		
	}
}
