package com.frankegan.awesomelist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;


public class MainActivity extends AppCompatActivity implements ImgurAdapter.OnDisplayListener,
        ImageDisplayFragment.OnDismissListener, OnAppBarChangeListener {
    ImgurGridFragment imgurGridFragment;
    String awesomeTag = "imgur_list_fragment";
    String displayTag = "display_fragment";
    Toolbar toolbar;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        frame = (FrameLayout)findViewById(R.id.fragment_frame);
        setSupportActionBar(toolbar);
        imgurGridFragment = (ImgurGridFragment) getFragmentManager().findFragmentByTag(awesomeTag);
        if (imgurGridFragment == null) {
            Log.i("frankegan", "Adding awesomeFragment");
            imgurGridFragment = ImgurGridFragment.getInstance();
            imgurGridFragment.setRetainInstance(true);
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame, imgurGridFragment, awesomeTag).commit();
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
    public void onDisplay(ImgurData data) {
        getFragmentManager().beginTransaction().add(R.id.fragment_frame, ImageDisplayFragment.getInstance(data), displayTag).commit();
    }

    @Override
    public void onDismiss() {
        Fragment fragment = getFragmentManager().findFragmentByTag(displayTag);
        if(fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().findFragmentByTag(displayTag) != null)
            onDismiss();
        else finish();
    }

    @Override
    public void onVisibilityChanged(int visibility) {
        switch (visibility){
            case OnAppBarChangeListener.VISIBLE:
                getSupportActionBar().show();
                break;
            case OnAppBarChangeListener.GONE:
                getSupportActionBar().hide();
                break;
        }
    }

    @Override
    public void onAppBarScrollOut() {
        toolbar.animate().translationY(-(toolbar.getHeight() + getStatusBarHeight())).setInterpolator(new AccelerateInterpolator(2));
    }

    @Override
    public void onAppBarScrollIn() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
