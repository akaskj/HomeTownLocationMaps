package sdsu.com.hometownlocation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jayacak on 4/9/17.
 */

public class UserDetails {

    private String nickname, country, state, city, timeStamp;
    private int id,year;
    private double latitude, longitude;

    public UserDetails(JSONObject object){
        try {
            this.nickname = object.getString("nickname");
            this.country = object.getString("country");
            this.state = object.getString("state");
            this.city = object.getString("city");
            this.timeStamp = object.getString("time-stamp");
            this.id = object.getInt("id");
            this.year = object.getInt("year");
            this.latitude = object.getDouble("latitude");
            this.longitude = object.getDouble("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getNickname ()
    {
        return nickname;
    }

    public void setNickname (String nickname)
    {
        this.nickname = nickname;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public int getYear ()
    {
        return year;
    }

    public void setYear (int year)
    {
        this.year = year;
    }

    public double getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (double latitude)
    {
        this.latitude = latitude;
    }

    public String getTimeStamp ()
    {
        return timeStamp;
    }

    public void setTimeStamp (String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    @Override
    public String toString()
    {
        return "UserDetails [id = "+id+", nickname = "+nickname+", state = "+state+", year = "+year+", longitude = "+longitude+", latitude = "+latitude+", time-stamp = "+timeStamp+", country = "+country+", city = "+city+"]";
    }





}
