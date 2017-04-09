package sdsu.com.hometownlocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView nickName = (TextView) convertView.findViewById(R.id.nickName);
        TextView country = (TextView) convertView.findViewById(R.id.country);
        TextView state = (TextView) convertView.findViewById(R.id.state);
        TextView city = (TextView) convertView.findViewById(R.id.city);

        nickName.setText("NickName: " + user.nickName);
        country.setText("Country: " + user.country);
        state.setText("State: " + user.state);
        city.setText("City: " + user.city);

        return convertView;
    }
}
