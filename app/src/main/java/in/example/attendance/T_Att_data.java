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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Objects;

import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;

public class T_Att_data extends AppCompatActivity {
    private ProgressDialog pdLoading;
    public static String t_id,oo,pp,qq,st_ID,st_name,strength,st_roll;
    private String TAG = T_Att_data.class.getSimpleName();
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final phppath path=new phppath();
    String url_dept_list = path.url+"prisma/index.php/Data/get_class";
    String url_sem_list = path.url+"prisma/index.php/Data/get_section_by_classesID";
    String url_subject_list = path.url+"prisma/index.php/Data/get_subject_by_sectionID";


    String url_stud_list = path.url+"app_t_classList2.php";

    ArrayList<HashMap<String, String>> classList,studList;
    ArrayList<String> arr1=new ArrayList<>();
    ArrayList<String> roll_array=new ArrayList<>();
    ArrayList<String> name_array=new ArrayList<>();

    private ArrayList<Spn_Adapter_ID_NAME> deptList,semList,subList;
    Spinner spn_dept,spn_sem,spn_subject;
    TextView label_dept,label_sem,label_sub;
    String deptID="",deptName="",semID="",semName="",subjectID="",subjectName="";
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t__att_data);
        //setting Action bar title and background
        getSupportActionBar().setTitle("Attendance");
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
        spn_dept=findViewById(R.id.spn_dept);
        spn_sem=findViewById(R.id.spn_sem);
        spn_subject=findViewById(R.id.spn_subject);
        label_dept=findViewById(R.id.label_dept);
        label_sem=findViewById(R.id.label_sem);
        label_sub=findViewById(R.id.label_sub);

        studList=new ArrayList<>();

        spn_dept.setVisibility(View.VISIBLE);
        spn_sem.setVisibility(View.VISIBLE);
        spn_subject.setVisibility(View.VISIBLE);
        label_dept.setVisibility(View.VISIBLE);
        label_sem.setVisibility(View.VISIBLE);
        label_sub.setVisibility(View.VISIBLE);

        //Reading from sharedpreference
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        t_id=pref.getString("u_id", null);

        //checking network availability
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Fetching dept
                new GetDept().execute();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        //spinner actions
        spn_dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //checking network availability
                try{
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (!isConnected) {
                        Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                    }else{
                        deptID=String.valueOf(deptList.get(position).getId());
                        deptName=String.valueOf(spn_dept.getItemAtPosition(position));
                        Log.e(TAG,"deptID=="+deptID);
                        Log.e(TAG,"deptName=="+deptName);
                        //Toast.makeText(getApplicationContext(), deptName, Toast.LENGTH_SHORT).show();

                        //getting semester
                        new GetSem().execute(deptID);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please Choose a Department", Toast.LENGTH_SHORT).show();
            }
        });

        spn_sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //checking network availability
                try{
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (!isConnected) {
                        Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                    }else{
                        semID=String.valueOf(semList.get(position).getId());
                        semName=String.valueOf(spn_sem.getItemAtPosition(position));
                        Log.e(TAG,"semID=="+semID);
                        Log.e(TAG,"semName=="+semName);

                        //Toast.makeText(getApplicationContext(), semName, Toast.LENGTH_SHORT).show();

                        //getting subjects
                        new GetSubject().execute(semID);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(getApplicationContext(), "Please Choose a Semester", Toast.LENGTH_SHORT).show();
            }
        });

        spn_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectID=String.valueOf(subList.get(position).getId());
                subjectName=String.valueOf(spn_subject.getItemAtPosition(position));
                Log.e(TAG,"subjectID=="+subjectID);
                Log.e(TAG,"subjectName=="+subjectName);

                //Toast.makeText(getApplicationContext(), subjectName, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(getApplicationContext(), "Please Choose a Subject", Toast.LENGTH_SHORT).show();
            }
        });

        btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking network availability
                try{
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (!isConnected) {
                        Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                    }else{
                        if(!subjectID.equals("") && !subjectName.equals("")) {
                            //Fetching students list
                            Log.e(TAG,"------------------------------");
                            Log.e(TAG,"deptID=="+deptID);
                            Log.e(TAG,"deptName=="+deptName);
                            Log.e(TAG,"semID=="+semID);
                            Log.e(TAG,"semName=="+semName);
                            Log.e(TAG,"subjectID=="+subjectID);
                            Log.e(TAG,"subjectName=="+subjectName);
                            Log.e(TAG,"------------------------------");

                            //fetching student details
                            new GetStudList().execute();
                        }else{
                            Toast.makeText(getApplicationContext(), "No subject chosen", Toast.LENGTH_SHORT).show();

                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private class GetDept extends AsyncTask<String,String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading = new ProgressDialog(T_Att_data.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            URL url;
            try {
                url = new URL(url_dept_list);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("t_id", "");
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, " deptttttttttttttttttttttt ");
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
                if(!result.equals("[]")){
//                JSONObject jsonObj = new JSONObject(result);
//                success = jsonObj.getInt("success");
//                if (success == 1) {
//                    Log.d("Dept fetch Success", jsonObj.toString());
                    //converting the string to json array object
                    JSONArray JArray = new JSONArray(result);
                    deptList=new ArrayList<>();
                    for (int i = 0; i < JArray.length(); i++) {
                        JSONObject c = JArray.getJSONObject(i);
                        int classesID = c.getInt("classesID");
                        String classes = c.getString("classes");

                        Log.e(TAG, "dept:::::::::::: " + classes);
                        Spn_Adapter_ID_NAME cat = new Spn_Adapter_ID_NAME(classesID, classes);
                        deptList.add(cat);
                        //Toast.makeText(PViewMark2.this, subjectID+subject+mark , Toast.LENGTH_SHORT).show();
                        List<String> lables1 = new ArrayList<>();
                        for (int j = 0;j < deptList.size(); j++) {
                            lables1.add(deptList.get(j).getName());
                        }
                        // Creating adapter for spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(T_Att_data.this, R.layout.spn_one_item11, lables1);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_dept.setAdapter(spinnerAdapter);


                    }
                }else if(result.equals("[]")){
                    Toast.makeText(getApplicationContext(), "Department Not Fetched", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                Log.d(TAG,"errrrrror");
                e.printStackTrace();
            }
        }
    }

    private class GetSem extends AsyncTask<String,String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            /*pdLoading = new ProgressDialog(T_Att_data.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();*/
            spn_dept.setVisibility(View.VISIBLE);
            spn_sem.setVisibility(View.VISIBLE);
            spn_subject.setVisibility(View.VISIBLE);
            label_dept.setVisibility(View.VISIBLE);
            label_sem.setVisibility(View.VISIBLE);
            label_sub.setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            try {
                url = new URL(url_sem_list);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("classesID", deptID);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, " semmmmmmmmmmmmmmmmmmmmmmmmmmmm ");
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
            /*if(pdLoading.isShowing()) {
                pdLoading.dismiss();
            }*/
            int success;
            Log.e(TAG, "Response from url: " + result);

            try {
                if(!result.equals("[]")){
//                JSONObject jsonObj = new JSONObject(result);
//                success = jsonObj.getInt("success");
//                if (success == 1) {
//                    Log.d("Dept fetch Success", jsonObj.toString());
                    //converting the string to json array object
                    JSONArray JArray = new JSONArray(result);
                    semList=new ArrayList<>();
                    for (int i = 0; i < JArray.length(); i++) {
                        JSONObject c = JArray.getJSONObject(i);
                        int classesID = c.getInt("sectionID");
                        String classes = c.getString("section");

                            /*//setting sharedpreferences
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("exam_pref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("exam_id",exam_id);
                            editor.putString("exam",exam);
                            editor.putString("exam_date",exam_date);
                            editor.apply();*/
                        Log.e(TAG, "sem:::::::::::: " + classes);
                        Spn_Adapter_ID_NAME cat = new Spn_Adapter_ID_NAME(classesID, classes);
                        semList.add(cat);
                        //Toast.makeText(PViewMark2.this, subjectID+subject+mark , Toast.LENGTH_SHORT).show();
                        List<String> lables1 = new ArrayList<>();
                        for (int j = 0;j < semList.size(); j++) {
                            lables1.add(semList.get(j).getName());
                        }
                        // Creating adapter for spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(T_Att_data.this, R.layout.spn_one_item11, lables1);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_sem.setAdapter(spinnerAdapter);

                    }



                }else if(result.equals("[]")){
                    Toast.makeText(getApplicationContext(), "Semester Not Fetched", Toast.LENGTH_SHORT).show();
                    spn_dept.setVisibility(View.VISIBLE);
                    spn_sem.setVisibility(View.GONE);
                    spn_subject.setVisibility(View.GONE);
                    label_dept.setVisibility(View.VISIBLE);
                    label_sem.setVisibility(View.GONE);
                    label_sub.setVisibility(View.GONE);
                    //finish();
                }
            } catch (JSONException e) {
                Log.d(TAG,"errrrrror");
                e.printStackTrace();
            }
        }
    }

    private class GetSubject extends AsyncTask<String,String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            /*pdLoading = new ProgressDialog(T_Att_data.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();*/
            spn_dept.setVisibility(View.VISIBLE);
            spn_sem.setVisibility(View.VISIBLE);
            spn_subject.setVisibility(View.VISIBLE);
            label_dept.setVisibility(View.VISIBLE);
            label_sem.setVisibility(View.VISIBLE);
            label_sub.setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            try {
                url = new URL(url_subject_list);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("sectionID", semID);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, "subbbbbbbbbbbbbbbbb::::::::: ");
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
                if(!result.equals("[]")){
//                JSONObject jsonObj = new JSONObject(result);
//                success = jsonObj.getInt("success");
//                if (success == 1) {
//                    Log.d("Dept fetch Success", jsonObj.toString());
                    //converting the string to json array object
                    JSONArray JArray = new JSONArray(result);
                    subList=new ArrayList<>();
                    for (int i = 0; i < JArray.length(); i++) {
                        JSONObject c = JArray.getJSONObject(i);
                        int classesID = c.getInt("subjectID");
                        String classes = c.getString("subject");

                        Log.e(TAG, "sub:::::::::::: " + classes);
                        Spn_Adapter_ID_NAME cat = new Spn_Adapter_ID_NAME(classesID, classes);
                        subList.add(cat);
                        //Toast.makeText(PViewMark2.this, subjectID+subject+mark , Toast.LENGTH_SHORT).show();
                        List<String> lables1 = new ArrayList<>();
                        for (int j = 0;j < subList.size(); j++) {
                            lables1.add(subList.get(j).getName());
                        }
                        // Creating adapter for spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(T_Att_data.this, R.layout.spn_one_item11, lables1);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_subject.setAdapter(spinnerAdapter);

                    }


                }else if(result.equals("[]")){
                    Toast.makeText(getApplicationContext(), "Subject Not Fetched", Toast.LENGTH_SHORT).show();
                    spn_subject.setVisibility(View.GONE);
                    label_sub.setVisibility(View.GONE);
                    //finish();
                }
            } catch (JSONException e) {
                Log.d(TAG,"errrrrror");
                e.printStackTrace();
                spn_subject.setVisibility(View.GONE);
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
            pdLoading = new ProgressDialog(T_Att_data.this);
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
                        .appendQueryParameter("classes_id", deptID)
                        .appendQueryParameter("sec_id",semID);
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
                    in.putExtra("class_name",deptName);
                    in.putExtra("class_id",deptID);
                    in.putExtra("sec_name",semName);
                    in.putExtra("sec_id",semID);
                    in.putExtra("sub_name",subjectName);
                    in.putExtra("sub_id",subjectID);
                    in.putExtra("strength",strength);
                    in.putExtra("roll_array",roll_array);
                    in.putExtra("name_array",name_array);
                    in.putStringArrayListExtra("id_array",arr1);
                    startActivity(in);

                }else{
                    Toast.makeText(getApplicationContext(), "No Students", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {

                Log.e(TAG,"errrrrrrrrrrrror");
                Toast.makeText(getApplicationContext(), "No Students", Toast.LENGTH_SHORT).show();
                //finish();
                e.printStackTrace();
            }

        }


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
