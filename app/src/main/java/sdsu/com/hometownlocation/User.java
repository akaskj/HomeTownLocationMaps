package sdsu.com.hometownlocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jayacak on 3/19/17.
 */

public class User {

    public String nickName;
    public String country;
    public  String state;
    public String city;
    public User(JSONObject object){
        try {
            this.nickName = object.getString("nickname");
            this.country = object.getString("country");
            this.state = object.getString("state");
            this.city = object.getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> fromJson(JSONArray jsonObjects) {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new User(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
}