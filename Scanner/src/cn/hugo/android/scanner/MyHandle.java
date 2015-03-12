package cn.hugo.android.scanner;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MyHandle extends Handler {
	private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;
    private WeakReference<Activity> activityReference;
    public MyHandle(Activity activity) {
        activityReference = new WeakReference<Activity>(activity);

    }

    @Override
    public void handleMessage(Message msg) {

        super.handleMessage(msg);
    }

}