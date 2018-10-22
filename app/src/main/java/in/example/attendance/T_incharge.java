package in.example.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;

public class T_incharge extends AppCompatActivity {
    private ProgressDialog pdLoading;
    public static String t_id,oo,pp,qq,ooID,ppID,qqID,st_ID,st_name,strength,st_roll;
    private String TAG = T_incharge.class.getSimpleName();
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public ListView lv_class;
    private static final phppath path=new phppath();
    String url_class_list = path.url+"app_t_classList1.php";
    String url_stud_list = path.url+"app_t_classList2.php";

    String url_class_subject_section_by_teacherID = path.url+"prisma/index.php/Data/get_class_subject_section_by_teacherID";

    ArrayList<HashMap<String, String>> classList,studList;
    ArrayList<String> arr1=new ArrayList<>();
    ArrayList<String> roll_array=new ArrayList<>();
    ArrayList<String> name_array=new ArrayList<>();
    FloatingActionButton fab_select;
    int TYPE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_incharge);
        lv_class=findViewById(R.id.lv_class);
        classList= new ArrayList<>();
        studList=new ArrayList<>();
        fab_select=findViewById(R.id.fab_select);
        //checking network availability
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                finish();
            }else
            {
                //fetching the class list
                new GetClassList().execute();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        if(pref.getString("u_type","").equals("Admin"))
            url_class_list = path.url+"app_t_classList1_admin.php";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setting Action bar title and background
        getSupportActionBar().setTitle("Classes");
        getSupportActionBar().setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                Color.parseColor("#098a83"),
                Color.parseColor("#098a83"),
        }));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable( getResources().getColor(actionbar_theme)));
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
        fab_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),T_Att_data.class);
                startActivity(intent);
            }
        });


    }

    private class GetClassList extends AsyncTask<String,String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading = new ProgressDialog(T_incharge.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            //Reading from sharedpreference
            SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
            t_id=pref.getString("u_id", null);
            URL url;
            try {
                url = new URL(url_class_subject_section_by_teacherID);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("t_id", t_id);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, " claaaaaaaaaassss ");
                Log.e(TAG, "url "+url);
                Log.e(TAG, "paraa "+query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    bufferedReader.close();
                    input.close();
                    conn.disconnect();

                    // Pass data to onPostExecute method
                    return result.toString();

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            if(pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            int success;
            Log.e(TAG, "Response from url: " + result);

            try {

                if (!result.equals("[]")) {
                    JSONArray info_array = new JSONArray(result);
                    for (int i = 0; i < info_array.length(); i++) {
                        JSONObject c = info_array.getJSONObject(i);
                        String classesID = c.getString("classesID");
                        String classes = c.getString("classes");
                        String sectionID = c.getString("sectionID");
                        String section = c.getString("section");
                        String subjectID = c.getString("subjectID");
                        String subject = c.getString("subject");

                        HashMap<String, String> tmp_class = new HashMap<>();
                        tmp_class.put("classesID", classesID);
                        tmp_class.put("classes", classes);
                        tmp_class.put("sectionID", sectionID);
                        tmp_class.put("section", section);
                        tmp_class.put("subjectID", subjectID);
                        tmp_class.put("subject", subject);
                        classList.add(tmp_class);

                    }

                    //Updating parsed JSON data into ListView
                    /*View headerView = getLayoutInflater().inflate(R.layout.header_list_mark, null);
                    lv_class.addHeaderView(headerView);*/
                    final ListAdapter adapter = new SimpleAdapter(T_incharge.this, classList,
                            R.layout.class_sec_sub_list_item, new String[]{"classesID", "sectionID","subjectID",
                            "classes","section","subject"},
                            new int[]{R.id.tv_class_id, R.id.tv_sec_id,R.id.tv_sub_id,
                                    R.id.tv_class_name,R.id.tv_sec_name,R.id.tv_sub_name});
                    lv_class.setAdapter(adapter);
                    lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //checking network availability
                            try{
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                                if (!isConnected) {
                                    Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            TextView textview1 = view.findViewById(R.id.tv_class_id);
                            TextView textview2 = view.findViewById(R.id.tv_sec_id);
                            TextView textview3 = view.findViewById(R.id.tv_sub_id);
                            TextView textview4 = view.findViewById(R.id.tv_class_name);
                            TextView textview5 = view.findViewById(R.id.tv_sec_name);
                            TextView textview6 = view.findViewById(R.id.tv_sub_name);

                            ooID = textview1.getText().toString().trim();
                            ppID = textview2.getText().toString().trim();
                            qqID = textview3.getText().toString().trim();
                            oo = textview4.getText().toString().trim();
                            pp = textview5.getText().toString().trim();
                            qq = textview6.getText().toString().trim();

                            Log.e("tv_class_id=", textview1.getText().toString().trim());
                            Log.e("tv_sec_id=", textview2.getText().toString().trim());
                            Log.e("tv_sub_id=", textview3.getText().toString().trim());
                            Log.e("tv_class=", textview1.getText().toString().trim());
                            Log.e("tv_sec=", textview2.getText().toString().trim());
                            Log.e("tv_sub=", textview3.getText().toString().trim());


                            //Fetching students list
                            new GetStudList().execute();
                            //Toast.makeText(T_incharge.this, "Loading...", Toast.LENGTH_SHORT).show();

                        }
                    });

                }else if(result.equals("[]")) {
                    Toast.makeText(T_incharge.this, "Class Not Fetched", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                Log.d(TAG,"errrrrror");
                e.printStackTrace();
            }
        }
    }

    private class GetStudList extends AsyncTask<String,String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading = new ProgressDialog(T_incharge.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            URL url;
            try {
                url = new URL(url_stud_list);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("classes_id", ooID)
                        .appendQueryParameter("sec_id",ppID)
                        .appendQueryParameter("subjectID",qqID);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, " stuuuuuuuuuuuuuuudent ");
                Log.e(TAG, "url "+url);
                Log.e(TAG, "paraa "+query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                    bufferedReader.close();
                    input.close();
                    conn.disconnect();

                    // Pass data to onPostExecute method
                    return result.toString();

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(pdLoading.isShowing()){
                pdLoading.dismiss();
            }
            int success;
            Log.e(TAG, "Response from url: " + result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                success = jsonObj.getInt("success");
                TYPE=jsonObj.getInt("type");
                if (success == 1) {
                    strength=jsonObj.getString("strength");
                    Log.e("Student fetch Success", jsonObj.toString());
                    JSONArray class_stu_info = jsonObj.getJSONArray("class_stu_info");
                    for (int i = 0; i < class_stu_info.length(); i++) {
                        JSONObject c = class_stu_info.getJSONObject(i);
                        st_ID = c.getString("studentID");
                        st_name = c.getString("name");
                        st_roll=c.getString("roll");

                        HashMap<String, String> stu_pro = new HashMap<>();
                        stu_pro.put("st_ID",st_ID);
                        stu_pro.put("st_name",st_name);
                        studList.add(stu_pro);

                        roll_array.add(st_roll);
                        name_array.add(st_name);
                        arr1.add(st_ID);

                    }

                    //Toast.makeText(T_incharge.this, roll_array.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(T_incharge.this, arr1.toString(), Toast.LENGTH_SHORT).show();

                    //setting sharedpreferences
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("count_pref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("strength",strength);
                    editor.apply();

                    Intent in = new Intent(getApplicationContext(), MainGridActivity.class);
                    in.putExtra("class_name",oo);
                    in.putExtra("class_id",ooID);
                    in.putExtra("sec_name",pp);
                    in.putExtra("sec_id",ppID);
                    in.putExtra("sub_name",qq);
                    in.putExtra("sub_id",qqID);
                    in.putExtra("strength",strength);
                    in.putExtra("roll_array",roll_array);
                    in.putExtra("name_array",name_array);
                    in.putExtra("type",TYPE);
                    in.putStringArrayListExtra("id_array",arr1);
                    startActivity(in);


                }else{
                    Toast.makeText(T_incharge.this, "No Students", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {

                Log.e(TAG,"errrrrrrrrrrrror");
                Toast.makeText(T_incharge.this, "No Students", Toast.LENGTH_SHORT).show();
                //finish();
                e.printStackTrace();
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();  return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        if(pref.getString("u_type","").equals("Admin"))
        {

        }
        else
        {
            Intent intent=new Intent(getApplicationContext(),T_homeActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}
