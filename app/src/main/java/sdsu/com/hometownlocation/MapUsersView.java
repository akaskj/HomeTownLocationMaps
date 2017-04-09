package sdsu.com.hometownlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapUsersView extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        final ArrayList<String> myValue = bundle.getStringArrayList("usersList");

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
            googleMap = mMap;
            for(int i = 0 ; i < myValue.size() ; i++ ) {

                myValue.get(i);
                createMarker(myValue.get(i).toString());
            }
            }
        });
        return rootView;
    }

    public Marker createMarker(String mapObj) {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(mapObj);

            Double latitude = Double.parseDouble(jsonObj.getString("latitude"));
            Double longitude = Double.parseDouble(jsonObj.getString("longitude"));

            if (latitude == 0.0 || longitude == 0.0) {
                Geocoder locator = new Geocoder(getActivity());
                try {
                    String country_state = jsonObj.getString("state") + ", " + jsonObj.getString("country");
                    List<Address> state = locator.getFromLocationName(country_state,1);
                    for (Address stateLocation: state) {
                        if (stateLocation.hasLatitude())
                            latitude = stateLocation.getLatitude();
                        if (stateLocation.hasLongitude())
                            longitude = stateLocation.getLongitude();
                    }
                } catch (Exception error) {
                    Log.e("rew", "Address lookup Error", error);
                }
            }

            return googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .title(jsonObj.getString("nickname"))

                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }
        catch (JSONException e) {
            System.out.println("Json exception");
        }
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34, 151))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    }
}
