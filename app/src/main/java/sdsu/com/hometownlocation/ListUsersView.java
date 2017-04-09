package sdsu.com.hometownlocation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListUsersView extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        final ArrayList<String> myValue = bundle.getStringArrayList("usersList");

//        System.out.println(myValue);
        View view = inflater.inflate(R.layout.fragment_list_users_view, container, false);
        ArrayList<User> arrayOfUsers = new ArrayList<User>();
        UserAdapter adapter = new UserAdapter(getActivity(), arrayOfUsers);
        User newUser;
        UserDetails userDetails;
        DbHelper dbHelper = new DbHelper(this.getContext());
        System.out.println("will start insert now");

        for(int i = 0 ; i < myValue.size() ; i++ ) {
            myValue.get(i);
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(myValue.get(i));
                newUser = new User(jsonObj);
                userDetails = new UserDetails(jsonObj);
                dbHelper.addUser(userDetails);

                adapter.add(newUser);
                ListView listView = (ListView) view.findViewById(android.R.id.list);
                listView.setAdapter(adapter);
            }catch (JSONException e) {
                System.out.println("Json exception");
            }
        }

        System.out.println("values_added");
        return view;
    }


}
