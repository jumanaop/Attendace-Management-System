package in.example.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import static in.example.attendance.R.color.statusbartheme;

public class LoginActivity1 extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private ProgressDialog pdLoading;
    private String TAG = LoginActivity1.class.getSimpleName();
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final phppath path=new phppath();
    private static final String app_login_url = path.url+"app_login1.php";
    Context context;
    // Session Manager Class
    AppSession session;//
    // Alert Dialog Manager

    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
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
        //Reading the pref value
        SharedPreferences log_pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        String user= log_pref.getString("u_type", null);
        if(user!=null) {
            if (user.equals("Teacher")) {
                Intent i = new Intent(getApplicationContext(), T_homeActivity.class);
                startActivity(i);
                finish();
            } else if (user.equals("Parent")) {
                Intent i = new Intent(getApplicationContext(), P_homeActivity.class);
                startActivity(i);
                finish();
            }

        }
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
        // Get Reference to variables
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        // Alert Dialog Manager

        // Session Manager
        session = new AppSession(getApplicationContext());
        btnLogin=findViewById(R.id.btn_sign_in);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking network availability
                if(etUsername.getText().length()!=0)
                {
                    if(etPassword.getText().length()!=0)
                    {
                        try{
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                            if (!isConnected) {
                                Toast.makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                            }else{
                                // Get text from username and passWord field
                                final String username = etUsername.getText().toString();
                                final String password = etPassword.getText().toString();

                                if(username.trim().length() > 0 && password.trim().length() > 0) {
                                    new AsyncLogin().execute(username, password);
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        etPassword.setError("Enter Password");
                        etPassword.requestFocus();
                    }
                }
                else
                {
                    etUsername.setError("Enter your username");
                    etUsername.requestFocus();
                }
            }
        });
    }
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading = new ProgressDialog(LoginActivity1.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            try {
                url = new URL(app_login_url);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, "++++++++++++++++++ ");
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
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            int success;
            String user_type = null;
            Log.e(TAG, "Response from url: " + result);
            if(result.equals("unsuccessful") )
            {
                Toast.makeText(LoginActivity1.this, "Connection error..", Toast.LENGTH_SHORT).show();
                etPassword.setText("");
            }
            try {
                JSONObject jsonObj = new JSONObject(result);
                success = jsonObj.getInt("success");
                Log.e(TAG, "success_v: " + success);
                if( success == 0)
                {
                    Toast.makeText(LoginActivity1.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                }
                else
                    user_type=jsonObj.getString("user_type");
                if (success == 1) {
                    Log.d("Successfully Login!!!!", jsonObj.toString());
                    Log.d("user_type", user_type+"");

                    ////////////////////// if parent
                    if(user_type.equals("3")){
                        Intent i1 = new Intent(LoginActivity1.this,P_homeActivity.class);
                        startActivity(i1);
                        Toast.makeText(LoginActivity1.this, "Student Login Success", Toast.LENGTH_SHORT).show();

                        JSONArray s_details = jsonObj.getJSONArray("s_details");
                        for (int i = 0; i < s_details.length(); i++) {
                            JSONObject c = s_details.getJSONObject(i);
                            String st_id = c.getString("studentID");
                            String st_name=c.getString("name");
                            String st_dob=c.getString("dob");
                            String classesID=c.getString("classesID");
                            String photo=c.getString("photo");
                            String classa=c.getString("classes");
                            String registeno=c.getString("registerNO");
                            //setting sharedpreferences
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("u_id",st_id);
                            editor.putString("u_type","Parent");
                            editor.putString("u_name",st_name);
                            editor.putString("u_dob",st_dob);
                            editor.putString("classesID",classesID);
                            editor.putString("photo",photo);
                            editor.putString("registerNO",registeno);
                            editor.putString("class",classa);
                            editor.apply();
                            session.createLoginSession("parent", st_name);
                        }
                        finish();
                    }

                    /////////// if teacher
                    else if (user_type.equals("2")){
                        Intent i2 = new Intent(LoginActivity1.this, T_homeActivity.class);
                        startActivity(i2);
                        Toast.makeText(LoginActivity1.this, "Teacher Login Success", Toast.LENGTH_SHORT).show();
                        JSONArray t_details = jsonObj.getJSONArray("t_details");
                        for (int i = 0; i < t_details.length(); i++) {
                            JSONObject c = t_details.getJSONObject(i);
                            String t_id = c.getString("teacherID");
                            String t_name = c.getString("name");
                            String t_dob = c.getString("dob");
                            String photo=c.getString("photo");
                            //setting sharedpreferences
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("u_id", t_id);
                            editor.putString("u_type","Teacher");
                            editor.putString("u_name", t_name);
                            editor.putString("u_dob", t_dob);
                            editor.putString("photo",photo);
                            editor.apply();
                            session.createLoginSession("teacher",t_name);
                        }
                        finish();
                    }
					////////////////////// if admin
                    else if (user_type.equals("1")){

                        Toast.makeText(LoginActivity1.this, "Admin Login Success", Toast.LENGTH_SHORT).show();
                        JSONArray t_details = jsonObj.getJSONArray("a_details");
                        for (int i = 0; i < t_details.length(); i++) {
                            JSONObject c = t_details.getJSONObject(i);
                            String t_id = c.getString("systemadminID");
                            String t_name = c.getString("name");
                            String t_dob = c.getString("dob");
                            String photo=c.getString("photo");
                            //setting sharedpreferences
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("u_id", t_id);
                            editor.putString("u_type","Admin");
                            editor.putString("u_name", t_name);
                            editor.putString("u_dob", t_dob);
                            editor.putString("photo",photo);
                            editor.apply();
                            session.createLoginSession("admin",t_name);
                        }
                        finish();
                    }
                    }else{
                        Log.e(TAG,"Error in Login");
                        //Toast.makeText(LoginActivity1.this, "Error in Login", Toast.LENGTH_SHORT).show();
                    }
            } catch (JSONException e) {
                Log.e(TAG, "error_lo: " + e.toString());
            }
            //this method will be running on UI thread
            if(pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
        }
    }
}

