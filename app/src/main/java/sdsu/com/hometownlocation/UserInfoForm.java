package sdsu.com.hometownlocation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.widget.ThemedSpinnerAdapter;

import static android.R.attr.button;
import static android.R.attr.password;
import static android.R.attr.targetActivity;
import static android.R.attr.value;
import static sdsu.com.hometownlocation.R.id.countrySpinner;
import static sdsu.com.hometownlocation.R.id.nicknameEditText;
import static sdsu.com.hometownlocation.R.id.stateSpinner;


public class UserInfoForm extends AppCompatActivity  {


    EditText nickname;
    EditText password;
    EditText city;
    EditText year;
    EditText latlong;
    static final int INTENT_MAP_REQUEST=1;
    String nickUrl="http://bismarck.sdsu.edu/hometown/nicknameexists?name=";
    public static String result;
    public static boolean test=true;
    Spinner country;
    Spinner state;
    String countryUrl;
    String stateUrl;
    String postUrl=" ";
    String selectedState=null;
    ArrayList<String> cList = null;
    String setLat=null;
    String setLong=null;

    Boolean nicknameValid = false, passwordValid = false, cityValid = false, yearValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_form);

        nickname = (EditText) findViewById(R.id.nicknameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        country = (Spinner) findViewById(countrySpinner);
        state = (Spinner) findViewById(stateSpinner);
        city = (EditText) findViewById(R.id.cityEditText);
        year = (EditText) findViewById(R.id.yearEditText);
        latlong = (EditText) findViewById(R.id.latlongEditText);
        countryUrl="http://bismarck.sdsu.edu/hometown/countries";
        new AsyncServerConnection().execute(countryUrl);

      nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                nicknameValid = true;
                if(!hasFocus) {
                    if(nickname.getText().toString().equals(""))
                    {
                        nickname.setError("Enter the nickname");
                        nicknameValid = false;
                    }
                    else {
                        final String nick = nickname.getText().toString();
                        String nickUrl = "http://bismarck.sdsu.edu/hometown/nicknameexists?name="+nick;
                        new AsyncServerConnection().execute(nickUrl, "nickname");
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                passwordValid = true;
                if(!hasFocus) {
                    if(password.getText().toString().equals("")) {
                        password.setError("Please enter the password");
                        passwordValid = false;
                    }
                    else if(password.getText().toString().length()<3){
                        password.setError("Password must be atleast 3 charecters long!");
                        passwordValid = false;
                    }
                }
            }
        });


        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                cityValid = true;

                if(!hasFocus) {
                    if(city.getText().toString().equals("")) {
                        cityValid = false;
                        city.setError("Please enter the City");
                    }
                }
            }
        });

        year.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                String svalue= year.getText().toString();
                yearValid = true;
                if(!hasFocus) {
                    if(svalue.equals("")) {
                        yearValid = false;
                        year.setError("Enter the year");
                    }
                    else {
                        long ivalue=Integer.parseInt(svalue);
                        if(ivalue < 1970 || ivalue > 2017)
                        {
                            yearValid = false;
                            year.setError("Please enter valid year");
                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.viewUsers:
                Intent go= new Intent(this,ViewActivity.class);
                startActivity(go);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickSetlatlong(View latlong){
        Intent go= new Intent(this,SetMapContent.class);
        startActivityForResult(go,INTENT_MAP_REQUEST);
    }


   public void onClickPostData(View submit){

       String paramNickname=nickname.getText().toString();
       String paramPassword=password.getText().toString();
       String paramCountry=country.getSelectedItem().toString();
       String paramState=state.getSelectedItem().toString();
       String paramCity=city.getText().toString();
       String paramYear=year.getText().toString();



       if (nicknameValid && passwordValid && cityValid && yearValid){
           postUrl="http://bismarck.sdsu.edu/hometown/adduser";
           new AsyncForPost().execute(postUrl,paramNickname,paramPassword,paramCountry,paramState,paramCity,paramYear,setLat,setLong);
       } else {
           Toast.makeText(getBaseContext(), "Please enter valid data.", Toast.LENGTH_LONG).show();
       }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_MAP_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    setLat = data.getStringExtra("lat");
                    setLong = data.getStringExtra("lng");
                    latlong.setText(setLat+","+setLong);
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }


    public class AsyncForPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                URL url=new URL(params[0]);
                HttpURLConnection con= (HttpURLConnection) url .openConnection();
                con.setRequestMethod("POST");
                DataOutputStream printout;
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                try {
                    if(params[7]==null || params[8]==null)
                    {
                        jsonParam.put("nickname", params[1])
                                .put("password", params[2])
                                .put("country", params[3])
                                .put("state", params[4])
                                .put("city",params[5])
                                .put("year", Integer.parseInt(params[6]));
                    }
                    else {
                        jsonParam.put("nickname", params[1])
                                .put("password", params[2])
                                .put("country", params[3])
                                .put("state", params[4])
                                .put("city", params[5])
                                .put("year", Integer.parseInt(params[6]))
                                .put("latitude", Double.parseDouble(params[7]))
                                .put("longitude", Double.parseDouble(params[8]));

                    }
                } catch (JSONException e) {
                    System.out.println("Json exception "+e);
                }
                printout = new DataOutputStream(con.getOutputStream ());
                printout.writeBytes(jsonParam.toString());
                printout.flush ();
                printout.close ();


                int HttpResult = con.getResponseCode();
                if (HttpResult == 400) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String val=bf.readLine();
                    return "failed";
                }
                else {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String val=bf.readLine();
                    return "success";
                }

            }
            catch (IOException e) {
                Log.i("IO exception", String.valueOf(e));
            }
            return "result";
        }
        @Override
        protected void onPostExecute(String s) {
            if (s.equals("success")){
                Toast.makeText(getBaseContext(), "User created", Toast.LENGTH_LONG).show();
            } else if (s.equals(("failed"))){
                Toast.makeText(getBaseContext(), "Request failed, please enter valid data", Toast.LENGTH_LONG).show();
            }
        }
    }



    public class AsyncServerConnection extends AsyncTask<String, Void, String> {

        boolean countryFlag=false;
        boolean stateFlag=false;
        boolean nicknameFlag=false;
        boolean test=false;

        @Override
        protected String doInBackground(String... params) {
            try {
                 if(params[0] == countryUrl)
                     countryFlag = true;
                 else if(params[0] == stateUrl)
                    stateFlag=true;
                 else if(params[1] == "nickname")
                    nicknameFlag=true;


                URL url=new URL(params[0]);
                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String value=bf.readLine();
                result = value;

            }
            catch (IOException e) {
                Log.i("response illa", String.valueOf(e));
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if(nicknameFlag) {
                if(s.equals("true"))
                {
                    nickname.setError("Duplicate nickname");
                    nicknameValid = false;
                }
            }

            if(countryFlag) {
                try {
                    JSONArray jsonArray = null;
                    cList = new ArrayList<String>();
                    jsonArray = new JSONArray(s);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cList.add(jsonArray.getString(i));
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                country = (Spinner) findViewById(countrySpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UserInfoForm.this ,android.R.layout.simple_spinner_dropdown_item, cList); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                country.setAdapter(spinnerArrayAdapter);

                country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {
                        selectedState=cList.get(pos);
                        stateUrl="http://bismarck.sdsu.edu/hometown/states?country="+selectedState;
                        new AsyncServerConnection().execute(stateUrl);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

            }
           if(stateFlag) {
                ArrayList<String> cList = null;
                try {
                    JSONArray jsonArray = null;
                    cList = new ArrayList<String>();
                    jsonArray = new JSONArray(s);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cList.add(jsonArray.getString(i));
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                state = (Spinner) findViewById(stateSpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UserInfoForm.this ,android.R.layout.simple_spinner_dropdown_item, cList); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                state.setAdapter(spinnerArrayAdapter);
            }
        }
    }
}


