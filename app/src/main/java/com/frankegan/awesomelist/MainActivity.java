package com.frankegan.awesomelist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements MyAdapter.OnDisplayListener, ImageDisplayFragment.OnDismissListener{
    AwesomeFragment awesomeFragment;
    String awesomeTag = "imgur_list_fragment";
    String displayTag = "display_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        awesomeFragment = (AwesomeFragment) getFragmentManager().findFragmentByTag(awesomeTag);
        if (awesomeFragment == null) {
            Log.i("frankegan", "Adding awesomeFragment");
            awesomeFragment = AwesomeFragment.getInstance();
            awesomeFragment.setRetainInstance(true);
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame, awesomeFragment, awesomeTag).commit();
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
    public void onDisplay(String link) {
        getFragmentManager().beginTransaction().add(R.id.fragment_frame, ImageDisplayFragment.getInstance(link), displayTag).commit();
        Log.i("frankegan", "created fragment with link = " + link);
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
    }
}
