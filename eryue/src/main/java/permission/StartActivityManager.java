package permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class StartActivityManager {

    private StartActivityManager() {
    }

    private static StartActivityManager mInstance = null;

    private static int REQUEST_CODE = 33000;

    public static StartActivityManager getInstance() {
        if (mInstance == null) {
            mInstance = new StartActivityManager();
        }
        return mInstance;
    }

    private Map<Integer, StartActivityForResultAction> actions = new HashMap<>();

    @TargetApi(11)
    public void startActivityForResult(Object context, Intent intent, StartActivityForResultAction activityForResultAction) {
        try {
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
            } else if (context instanceof Fragment) {
                ((Fragment) context).startActivityForResult(intent, REQUEST_CODE);
            } else if (context instanceof android.app.Fragment) {
                ((android.app.Fragment) context).startActivityForResult(intent, REQUEST_CODE);
            }
            actions.put(REQUEST_CODE, activityForResultAction);
            REQUEST_CODE++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean handActivityResult(int requestCode, int resultCode, Intent data) {
        if (actions.containsKey(requestCode)) {
            StartActivityForResultAction activityForResultAction = actions.get(requestCode);
            if (activityForResultAction != null) {
                activityForResultAction.handleResult(resultCode, data);
            }
            actions.remove(requestCode);
            return true;
        }
        return false;
    }
}
