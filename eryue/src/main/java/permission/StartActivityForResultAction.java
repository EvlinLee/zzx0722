package permission;
import android.content.Intent;


public abstract class StartActivityForResultAction {
    public abstract void handleResult(int resultCode, Intent data);
}
