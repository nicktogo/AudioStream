package us.ktv.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import us.ktv.android.R;
import us.ktv.android.fragment.BaseListFragment;
import us.ktv.android.fragment.PlaySongFragment;
import us.ktv.android.fragment.RoomFragment;
import us.ktv.android.fragment.RoomListFragment;
import us.ktv.android.transition.SongTransition;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.Song;


public class MainActivity
        extends AppCompatActivity
        implements
        BaseListFragment.OnFragmentInteractionListener,
        RoomFragment.OnFragmentInteractionListener {

    private static final int REQUEST_ROOM_ID = 1;

    private boolean forwardToSongListFragment = false;
    private String roomId;

    private static final int REQUEST_RECORD_AUDIO = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission
                (MicApplication.getInstance(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "RECORD_AUDIO permission has NOT been granted. Requesting permission.");
            // BEGIN_INCLUDE(camera_permission_request)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.
                Log.i("MainActivity",
                        "Displaying record_audio permission rationale to provide additional context.");
                View coordinatorLayout = this.findViewById(R.id.coordinator_layout);
                final Snackbar snackbar = Snackbar.make(
                        coordinatorLayout,
                        R.string.permission_record_audio_rationale,
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                        REQUEST_RECORD_AUDIO);
                            }
                        })
                        .setAction(R.string.dismiss, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                                MainActivity.this.finish();
                            }
                        });
                snackbar.show();
            } else {
                // Record audio permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO);
            }
        }

        RoomListFragment fragment = RoomListFragment.newInstance(R.layout.item_room);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        fragmentManager.executePendingTransactions();
//        getSupportActionBar().setTitle(R.string.room_ui);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for record_audio permission.
            Log.i("MainActivity", "Received response for Record audio permission request.");
            View coordinatorLayout = this.findViewById(R.id.coordinator_layout);
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Record audio permission has been granted, preview can be displayed
                Log.i("MainActivity", "Record audio permission has now been granted.");
                Snackbar.make(coordinatorLayout, R.string.permission_available_record_audio,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i("MainActivity", "Record audio permission was NOT granted.");
                Snackbar.make(coordinatorLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
//        if (forwardToSongListFragment) {
//            SongListFragment fragment = SongListFragment.newInstance(R.layout.item_song, roomId);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .addToBackStack(null)
//                    .commit();
//            setTitle(R.string.song_ui);
//            forwardToSongListFragment = false;
//        }
//        RoomFragment fragment = RoomFragment.newInstance(roomId);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fragment.setSharedElementEnterTransition(new SongTransition());
//            fragment.setEnterTransition(new Fade());
//            fragment.setSharedElementReturnTransition(new SongTransition());
//            TextView roomName = (TextView) view.findViewById(R.id.name);
//            getSupportFragmentManager().beginTransaction()
//                    .addSharedElement(roomName, getString(R.string.toolbar_title_transition_name))
//                    .replace(R.id.container, fragment)
//                    .addToBackStack(null)
//                    .commit();
//        } else {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .addToBackStack(null)
//                    .commit();
//        }
        if (forwardToSongListFragment) {
            RoomFragment fragment = RoomFragment.newInstance(roomId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
//            setTitle(R.);
            forwardToSongListFragment = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ROOM_ID) {
            if (resultCode == RESULT_OK) {
                roomId = data.getStringExtra(AddRoomActivity.ROOM_ID);
                forwardToSongListFragment = true;
            } else if (resultCode == RESULT_FIRST_USER){
                showSnackbar("无法连接到房间，请检查网络。");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Object o, View view) {
        if (o instanceof Room) {
            Room room = (Room) o;
//            SongListFragment fragment = SongListFragment.newInstance(R.layout.item_song, room.id);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                fragment.setSharedElementEnterTransition(new SongTransition());
//                fragment.setEnterTransition(new Fade());
//                fragment.setSharedElementReturnTransition(new SongTransition());
//                TextView roomName = (TextView) view.findViewById(R.id.name);
//                getSupportFragmentManager().beginTransaction()
//                        .addSharedElement(roomName, getString(R.string.toolbar_title_transition_name))
//                        .replace(R.id.container, fragment)
//                        .addToBackStack(null)
//                        .commit();
//            } else {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
            RoomFragment fragment = RoomFragment.newInstance(room.id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementEnterTransition(new SongTransition());
                fragment.setEnterTransition(new Fade());
                fragment.setSharedElementReturnTransition(new SongTransition());
                TextView roomName = (TextView) view.findViewById(R.id.name);
                getSupportFragmentManager().beginTransaction()
                        .addSharedElement(roomName, getString(R.string.toolbar_title_transition_name))
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }

        } else if (o instanceof Song) {
            Song song = (Song) o;
            PlaySongFragment fragment = PlaySongFragment.newInstance(song.id);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementEnterTransition(new SongTransition());
                fragment.setEnterTransition(new Fade());
                fragment.setSharedElementReturnTransition(new SongTransition());

                SimpleDraweeView cover = ButterKnife.findById(view, R.id.cover);
                TextView songName = ButterKnife.findById(view, R.id.name);

                getSupportFragmentManager().beginTransaction()
                        .addSharedElement(songName, getString(R.string.toolbar_title_transition_name))
                        .addSharedElement(cover, getString(R.string.cover_common_name))
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
            setTitle(song.name);
        } else if (view instanceof FloatingActionButton) {
            Intent intent = new Intent(this, AddRoomActivity.class);
            startActivityForResult(intent, REQUEST_ROOM_ID);
        }
    }

    public void showSnackbar(String msg) {
        View coordinatorLayout = this.findViewById(R.id.coordinator_layout);
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
