package sdsu.com.hometownlocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jayacak on 4/9/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "HomeTown";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public static final String ID = "id",
//            NICKNAME = "nickname",
//            COUNTRY= "country"
//    public static final String NICKNAME;
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
//                EMP_NAME + " TEXT, " + EMP_AGE + " INTEGER, " + EMP_NUMBER + " INTEGER" + ")";

        String createTableQuery = "CREATE TABLE users (id INTEGER PRIMARY KEY, nickname TEXT, country TEXT, state TEXT, city TEXT, year INTEGER, latitude REAL, longitude REAL, timestamp TEXT)";
        db.execSQL(createTableQuery);

//        "nickname": "rew",
//                "city": "Devils Lake",
//                "longitude": 0,
//                "state": "North Dakota",
//                "year": 1985,
//                "id": 1,
//                "latitude": 0,
//                "time-stamp": "2017-03-01T02:44:03.247Z",
//                "country": "USA"
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS users");
    }

    public void addUser(UserDetails userDetails) {

        SQLiteDatabase db = this.getWritableDatabase();
        double lat= 0, lng = 0;


        ContentValues values = new ContentValues();
        values.put("id", userDetails.getId());
        values.put("nickname", userDetails.getNickname());
        values.put("country", userDetails.getCountry());
        values.put("state", userDetails.getState());
        values.put("city", userDetails.getCity());
        values.put("timestamp", userDetails.getTimeStamp());
        values.put("year", userDetails.getYear());
        values.put("latitude", userDetails.getLatitude());
        values.put("longitude", userDetails.getLongitude());

        db.insert("users", null, values);
        db.close();

    }
}
