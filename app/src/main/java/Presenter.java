import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by nick on 15-10-6.
 */
public class Presenter {
    private AppCompatActivity activity;
    public Presenter(AppCompatActivity activity) {
        this.activity = activity;
    }

    protected void onAddRoomFabClick(View view) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.show();
    }

}
