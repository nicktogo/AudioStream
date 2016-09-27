package us.ktv.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import us.ktv.android.Presenter;
import us.ktv.android.R;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;
import us.ktv.database.utils.GsonUtils;
import us.ktv.network.SocketCallbackListener;

/**
 * A login screen that offers login via email/password.
 */
public class AddRoomActivity extends AppCompatActivity {

    public static final String ROOM_ID = "room_id";

    private AddRoomTask addRoomTask;

    // UI references.
    @InjectView(R.id.room_id)
    protected TextInputEditText mIpPortView;

    @InjectView(R.id.add_room_progress)
    protected View mProgressView;

    @InjectView(R.id.add_room_form)
    protected View mAddRoomFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        ButterKnife.findById(this, R.id.add_room_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAddRoom();
            }
        });

        ButterKnife.inject(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptAddRoom() {

        if (addRoomTask != null) {
            return;
        }

        // Reset errors.
        mIpPortView.setError(null);

        // Store values at the time of the login attempt.
        String ipPort = mIpPortView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid ipPort address.
        if (TextUtils.isEmpty(ipPort)) {
            mIpPortView.setError(getString(R.string.error_field_required));
            focusView = mIpPortView;
            cancel = true;
        } else if (!isInputValid(ipPort)) {
            mIpPortView.setError(getString(R.string.error_invalid_input));
            focusView = mIpPortView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            addRoomTask = new AddRoomTask(ipPort, new SocketCallbackListener() {
                @Override
                public void onConnect(String roomId, String songList) {
                    List<Song> list = GsonUtils.JsonToObject(songList, new TypeToken<List<Song>>() {
                    }.getType());
                    SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
                    helper.insertList(roomId, list);

                    Room room = new Room();
                    room.id = roomId;
                    room.name = roomId;
                    RoomHelper roomHelper = RoomHelper.getInstance(MicApplication.getInstance());
                    roomHelper.insert(room);
                }

                @Override
                public void onError(Exception e) {
//                    Toast.makeText(AddRoomActivity.this, "Connect failed, please check your network", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            addRoomTask.execute();
        }
    }

    private boolean isInputValid(String ipPort) {
        return ipPort.contains(":");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAddRoomFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddRoomFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddRoomFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAddRoomFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class AddRoomTask extends AsyncTask<String, Void, String> {

        private String ip;

        private int port;

        private SocketCallbackListener listener;

        private boolean isConnected;

        public AddRoomTask(String ipPort, SocketCallbackListener listener) {
            this.ip = ipPort.split(":")[0];
            this.port = Integer.parseInt(ipPort.split(":") [1]);
            this.listener = listener;
            this.isConnected = false;
        }

        @Override
        protected String doInBackground(String... params) {
            Presenter presenter = Presenter.getPresenter(ip, port);
            isConnected = presenter.connect(ip, port, listener);
            return ip + ":" + port;
        }

        @Override
        protected void onPostExecute(String roomId) {
            addRoomTask = null;
            Intent returnIntent = new Intent();
            returnIntent.putExtra(ROOM_ID, roomId);
            setResult(isConnected ? RESULT_OK : RESULT_FIRST_USER, returnIntent);
            showProgress(false);
            finish();
        }
    }
}

