package in.example.attendance;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import static in.example.attendance.R.color.action_bar_color;

public class T_Class_ListActivity extends AppCompatActivity {

    private String TAG = T_Class_ListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get JSON
    private static String url = "http://mesmampad.org/app.php?%20mode=course";

    ArrayList<HashMap<String, String>> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t__class__list);
        //setting Action bar title and background
        /*getSupportActionBar().setTitle("Attendance");
        getSupportActionBar().setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                Color.parseColor("#080039"),
                Color.parseColor("#330066"),
        }));*/
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(action_bar_color));
        }

        courseList = new ArrayList<>();
        lv = findViewById(R.id.list);

        new GetContacts().execute();



    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(T_Class_ListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeCallGet(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray courses = jsonObj.getJSONArray("course");
                    // looping through All Contacts
                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject c = courses.getJSONObject(i);

                        //////////////////////////////////////////////////////
                        String id = c.getString("id");
                        String course=c.getString("course");
                        String seat=c.getString("seat");
                        String status=c.getString("status");
                        Log.e(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@ " );

                        // Phone node is JSON Object
                        /*JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");*/

                        // tmp hash map for single course
                        HashMap<String, String>tmp_course = new HashMap<>();
                        // adding each child node to HashMap key => value
                        tmp_course.put("id",id);
                        tmp_course.put("course",course);
                        tmp_course.put("seat",seat);
                        tmp_course.put("status",status);

                        // adding contact to contact list
                        courseList.add(tmp_course);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Updating parsed JSON data into ListView
            final ListAdapter adapter = new SimpleAdapter(T_Class_ListActivity.this, courseList,
                    R.layout.class_list_item, new String[]{"course","seat"}, new int[]{R.id.class_batch,R.id.strength});
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView textview1 = view.findViewById(R.id.class_batch);
                    TextView textview2 = view.findViewById(R.id.strength);
                    String oo=textview1.getText().toString().trim();
                    String pp=textview2.getText().toString().trim();

                    /*Log.v("textview1",textview1.getText().toString().trim());
                    Log.v("textview2",textview2.getText().toString().trim());*/
                    Intent i = new Intent(getApplicationContext(), MainGridActivity.class);
                    i.putExtra("oo",oo);
                    i.putExtra("pp",pp);
                    startActivity(i);
                    Toast.makeText(T_Class_ListActivity.this, oo+"\n"+pp, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}



