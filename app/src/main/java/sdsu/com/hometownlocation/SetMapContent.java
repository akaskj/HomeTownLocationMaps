package sdsu.com.hometownlocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class SetMapContent extends AppCompatActivity implements MapViewFragment.LatLongListner{

    String lat;
    String lng;
    private LatLng selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_map_content);
        if (getSupportFragmentManager().findFragmentById(R.id.mapViewLayout) == null) {
            MapViewFragment mapViewFragment = new MapViewFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.mapViewLayout,mapViewFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onPositionSelected(LatLng position) {
        selectedPosition = position;
        lat= String.valueOf(position.latitude);
        lng= String.valueOf(position.longitude);
    }

    public void back(View done) {

        Intent toPassBack = getIntent();
        toPassBack.putExtra("lat",lat);
        toPassBack.putExtra("lng",lng);
        setResult(RESULT_OK, toPassBack);
        finish();
    }
}
