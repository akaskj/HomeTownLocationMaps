package sdsu.com.hometownlocation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static sdsu.com.hometownlocation.R.id.countrySpinner;
import static sdsu.com.hometownlocation.R.id.countryViewSpinner;
import static sdsu.com.hometownlocation.R.id.filterView;
import static sdsu.com.hometownlocation.R.id.radio_group;
import static sdsu.com.hometownlocation.R.id.stateSpinner;
import static sdsu.com.hometownlocation.R.id.stateViewSpinner;
import static sdsu.com.hometownlocation.R.id.yearTextView;
import static sdsu.com.hometownlocation.R.id.yearViewEditText;


public class ViewActivity extends AppCompatActivity  {

    String countryUrl = "http://bismarck.sdsu.edu/hometown/countries";
    String usersUrl = "http://bismarck.sdsu.edu/hometown/users";
    String stateUrl;
    String result;
    ArrayList<String> cList = null;
    Spinner country;
    Spinner state;
    String selectedState=null;
    Spinner countryList;
    Spinner stateList;
    EditText yearFilter;
    String fragmentValueRadio = "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activity);

        countryList = (Spinner) findViewById(countryViewSpinner);
        stateList = (Spinner) findViewById(stateViewSpinner);
        yearFilter = (EditText) findViewById(yearViewEditText);

        RadioGroup radioGroup;
        radioGroup = (RadioGroup) findViewById(radio_group);
        radioGroup.check(R.id.radio_list);

        new AsyncServerView().execute(countryUrl);
        new AsyncServerView().execute(usersUrl, "users");
    }

    public void listAction(View v){
        ListUsersView listViewFragment = new ListUsersView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.UsersViewLayout,listViewFragment);
        fragmentTransaction.commit();

    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_list:
                if (checked)
                    fragmentValueRadio = "list";
                    break;
            case R.id.radio_map:
                if (checked)
                    fragmentValueRadio = "map";
                    break;
        }
    }



    public void filter(View view){

        Boolean first = true;
        usersUrl = "http://bismarck.sdsu.edu/hometown/users";;
        String countrySelected = countryList.getSelectedItem().toString();
        String stateSelected = stateList.getSelectedItem().toString();
        String yearSelected = yearFilter.getText().toString();
        if (!countrySelected.equals("Select")) {
            if (first){
                usersUrl = usersUrl + "?";
                first = false;
            }
            usersUrl = usersUrl + "country=" + countrySelected;
        }

        if (!stateSelected.equals("Select")){
            if (first){
                usersUrl = usersUrl + "?";
                usersUrl = usersUrl + "state=" + stateSelected;
                first = false;
            }
            else {
                usersUrl = usersUrl + "&state=" + stateSelected;
            }

        }

        if (!yearSelected.equals("")) {
            if (first){
                usersUrl = usersUrl + "?";
                usersUrl = usersUrl + "year=" + yearSelected;
                first = false;
            }
            else {
                usersUrl = usersUrl + "&year=" + yearSelected;
            }

        }
        new AsyncServerView().execute(usersUrl, "users");
    }

    public void back(View done) {
        finish();
    }

    public class AsyncServerView extends AsyncTask<String, Void, String> {

        boolean countryFlag = false;
        boolean stateFlag = false;
        boolean userListFlag = false;

        @Override
        protected String doInBackground(String... params) {
            try {
                if(params[0]==countryUrl)
                    countryFlag = true;
                else if(params[0]==stateUrl)
                    stateFlag=true;
                else if(params[1]=="users")
                    userListFlag = true;


                URL url=new URL(params[0]);
                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String value=bf.readLine();
                result = value;

            }
            catch (IOException e) {
                Log.i("no response", String.valueOf(e));
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if(countryFlag) {
                try {

                    JSONArray jsonArray = null;
                    cList = new ArrayList<String>();
                    //System.out.println(s);
                    jsonArray = new JSONArray(s);
                    if (jsonArray != null) {
                        cList.add("Select");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cList.add(jsonArray.getString(i));
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                country = (Spinner) findViewById(countryViewSpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ViewActivity.this ,android.R.layout.simple_spinner_dropdown_item, cList); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                country.setAdapter(spinnerArrayAdapter);


                country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {
                        selectedState = cList.get(pos);

                        if (!selectedState.equals("Select")){
                            stateUrl="http://bismarck.sdsu.edu/hometown/states?country=" + selectedState;
                            new ViewActivity.AsyncServerView().execute(stateUrl);
                        }
                        else {
                            ArrayList<String> cListState = null;
                            try {
                                JSONArray jsonArray = null;
                                cListState = new ArrayList<String>();
                                cListState.add("Select");
                                if (jsonArray != null) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        cListState.add(jsonArray.getString(i));
                                    }
                                }
                            } catch (JSONException je) {
                                je.printStackTrace();
                            }
                            state = (Spinner) findViewById(stateViewSpinner);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ViewActivity.this ,android.R.layout.simple_spinner_dropdown_item, cListState); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            state.setAdapter(spinnerArrayAdapter);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

            }
            if(stateFlag) {
                ArrayList<String> cListState = null;
                try {
                    JSONArray jsonArray = null;
                    cListState = new ArrayList<String>();
                    //System.out.println(s);
                    jsonArray = new JSONArray(s);
                    cListState.add("Select");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cListState.add(jsonArray.getString(i));
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                state = (Spinner) findViewById(stateViewSpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ViewActivity.this ,android.R.layout.simple_spinner_dropdown_item, cListState); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                state.setAdapter(spinnerArrayAdapter);
            }

            if (userListFlag) {

                ArrayList<String> usersList = null;
                try {
                    JSONArray jsonArray = null;
                    usersList = new ArrayList<String>();
                    //System.out.println(s);
                    jsonArray = new JSONArray(s);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            usersList.add(jsonArray.getString(i));
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }

                if (fragmentValueRadio.equals("list") ) {

                    System.out.println("here");

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("usersList", usersList);
                    MapUsersView fragInfo = new MapUsersView();
                    fragInfo.setArguments(bundle);
                    ListUsersView listViewFragment = new ListUsersView();
                    listViewFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.UsersViewLayout,listViewFragment);
                    fragmentTransaction.commit();

                }
                else if (fragmentValueRadio.equals("map")){
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("usersList", usersList);
                    MapUsersView fragInfo = new MapUsersView();
                    fragInfo.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.UsersViewLayout, fragInfo);
                    fragmentTransaction.commit();
                }
            }
        }
    }

}


