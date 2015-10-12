package us.ktv.android.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import us.ktv.android.R;
import us.ktv.android.fragment.BaseListFragment;
import us.ktv.android.fragment.PlaySongFragment;
import us.ktv.android.fragment.RoomListFragment;
import us.ktv.android.fragment.SongListFragment;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.Song;


public class MainActivity extends AppCompatActivity implements BaseListFragment.OnFragmentInteractionListener {

    private static final int REQUEST_ROOM_ID = 1;

    private boolean forwardToSongListFragment = false;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RoomListFragment fragment = RoomListFragment.newInstance(R.layout.item_room);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (forwardToSongListFragment) {
            SongListFragment fragment = SongListFragment.newInstance(R.layout.item_song, roomId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
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
                Toast.makeText(MainActivity.this, "Connect failed, please check your network", Toast.LENGTH_SHORT).show();
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
            SongListFragment fragment = SongListFragment.newInstance(R.layout.item_song, room.id);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (o instanceof Song) {
            Song song = (Song) o;
            PlaySongFragment fragment = PlaySongFragment.newInstance(song.id);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTransaction = new Slide(Gravity.LEFT);
                slideTransaction.setDuration(100);
                fragment.setEnterTransition(slideTransaction);

                Transition changeBounds = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds);
                fragment.setSharedElementEnterTransition(changeBounds);

                SimpleDraweeView cover = ButterKnife.findById(view, R.id.cover);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addSharedElement(cover, getString(R.string.cover_common_name))
                        .addToBackStack(null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        } else if (view instanceof FloatingActionButton) {
            Intent intent = new Intent(this, AddRoomActivity.class);
            startActivityForResult(intent, REQUEST_ROOM_ID);
        }
    }
}
