package in.example.attendance;

import android.app.DatePickerDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.makeText;
import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;
import static java.lang.Integer.parseInt;

public class MainGridActivity extends AppCompatActivity {

    private ArrayList<String> absentees;
    public String strength,ids_abs,ids_all,class_id,sec_id,sub_id,sel_hour,name_abs;
    public  Integer mYear,mMonth,mDay;
    public String dd,mm,yy,mth_yr;
    GridViewAdapter adapter=null;
    TextView tv1,tv2,tv3,tv_att_date;
    private Button btn_date;
    GridView gridView;
    ArrayList arr1=new ArrayList();
    ArrayList roll_array=new ArrayList();
    ArrayList name_array=new ArrayList();
    ArrayList<HashMap<String, String>> abs_name_list;
    public ListView lv_abs_name;
    private String TAG = MainGridActivity.class.getSimpleName();
    private ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final phppath path=new phppath();
    private static final String url_class_list = path.url+"prisma/index.php/data/take_att";
    HashMap roll_id=new HashMap<Integer,Integer>();
    HashMap roll_name=new HashMap<Integer,String>();
    Spinner spn_hour;
    String u_type;
    int TYPE=0;
    int kk=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        makeText(MainGridActivity.this, "Please Choose a Hour befor take attendance", Toast.LENGTH_SHORT).show();
        //getSupportActionBar().setTitle("Attendance");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
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
        //checking network availability
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        gridView = findViewById(R.id.grids);
        final FloatingActionButton fab_Go = findViewById(R.id.fab_Go);
        final FloatingActionButton fab_verify = findViewById(R.id.fab_verify);
        final FloatingActionButton fab_cancel = findViewById(R.id.fab_cancel);
        lv_abs_name=findViewById(R.id.lv_abs_name);
        lv_abs_name.setVisibility(View.GONE);
        tv_att_date=findViewById(R.id.tv_att_date);
        spn_hour=findViewById(R.id.spn_hour);

        //populating spinner
        ArrayList<String> hourss=new ArrayList<>();
        hourss.add("Choose Hour");
        for(int i=1;i<=5;i++)
        {
            hourss.add(String.valueOf(i));
        }
        ArrayAdapter<String> spn_adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,hourss);
        spn_hour.setAdapter(spn_adapter);
        spn_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sel_hour=String.valueOf(spn_hour.getItemAtPosition(position));
                if(!sel_hour.equals("Choose Hour")){
                    sel_hour = String.valueOf(position);
                    //sel_hour=spn_hour.getSelectedItem();
                    makeText(MainGridActivity.this, sel_hour, Toast.LENGTH_SHORT).show();
                }
                if(TYPE==0)
                {
                    try {
                        pdLoading.show();
                        getatt sendPostReqAsyncTask = new getatt(getApplicationContext());
                        sendPostReqAsyncTask.execute("");

                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Reading from intent
        strength=getIntent().getCharSequenceExtra("strength").toString();
        class_id=getIntent().getCharSequenceExtra("class_id").toString();
        sec_id=getIntent().getCharSequenceExtra("sec_id").toString();
        sub_id=getIntent().getCharSequenceExtra("sub_id").toString();
        arr1=getIntent().getStringArrayListExtra("id_array");
        roll_array=getIntent().getStringArrayListExtra("roll_array");
        name_array=getIntent().getStringArrayListExtra("name_array");
        TYPE=getIntent().getIntExtra("type",0);

        //////Setting the class , strength...
        tv1=findViewById(R.id.tv_cls_batch);
        tv2=findViewById(R.id.tv_strength);
        tv3=findViewById(R.id.tv_sub);

        btn_date=findViewById(R.id.btn_date);
        absentees = new ArrayList<>();
        tv1.setText(getIntent().getCharSequenceExtra("class_name"));
        tv2.setText(getIntent().getCharSequenceExtra("strength"));
        tv3.setText(getIntent().getCharSequenceExtra("sub_name"));

        abs_name_list=new ArrayList<>();

        //getting the date and monthyear
        Date d=new Date();
        dd=String.valueOf(d.getDate());
        Integer m=d.getMonth()+1;
        if(m<=9)
        {
            mm="0"+m.toString();
        }
        else{
            mm=m.toString();
        }
        Calendar instance = Calendar.getInstance();
        yy= String.valueOf(instance.get(Calendar.YEAR));
        mth_yr=String.valueOf(mm+"-"+yy);

        tv_att_date.setText(dd+"-"+mm+"-"+yy);
        btn_date.setText(dd+"-"+mm+"-"+yy);
        //Toast.makeText(this, yy, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, dd+"-"+mth_yr, Toast.LENGTH_SHORT).show();

        int count=parseInt(String.valueOf(tv2.getText()));
        List<String> labels = new ArrayList<>(150);
        for(int i=0;i<count;i++)
        {
            labels.add(String.valueOf(roll_array.get(i)));
            roll_id.put(roll_array.get(i),arr1.get(i));
            roll_name.put(roll_array.get(i),name_array.get(i));
        }

        adapter = new GridViewAdapter(labels, this);
        gridView.setAdapter(adapter);
        Log.e("smk",adapter.getCount()+"");
        pdLoading = new ProgressDialog(MainGridActivity.this);
        pdLoading.setMessage("\tPlease Wait..");
        pdLoading.setCancelable(false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(!spn_hour.getSelectedItem().toString().equals("Choose Hour"))
                {
                    int selectedIndex = adapter.selectedPositions.indexOf(position);
                    if (selectedIndex > -1) {
                        adapter.selectedPositions.remove(selectedIndex);
                        ((GridItemView) v).display(false);
                        absentees.remove(parent.getItemAtPosition(position));
                    } else {
                        adapter.selectedPositions.add(position);
                        ((GridItemView) v).display(true);
                        absentees.add((String) parent.getItemAtPosition(position));
                    }
                }
                else
                {
                    Toast.makeText(MainGridActivity.this, "Choose Hour", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fab_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spn_hour.getSelectedItem().toString().equals("Choose Hour"))
                {
                    lv_abs_name.setVisibility(View.VISIBLE);
                    fab_Go.setVisibility(View.VISIBLE);
                    fab_cancel.setVisibility(View.VISIBLE);
                    fab_verify.setVisibility(View.INVISIBLE);
                    gridView.setVisibility(View.GONE);
                    //spn_hour.setVisibility(View.GONE);
                    spn_hour.setEnabled(false);
                    btn_date.setEnabled(false);
                    //getting array of absenties name
                    for (int i=0;i<absentees.size();i++) {
                        String m = absentees.get(i);
                        String uu= roll_name.get(m).toString();
                        HashMap<String, String> abs_name_map = new HashMap<>();
                        abs_name_map.put("name",uu);
                        abs_name_list.add(abs_name_map);
                    }

                    Log.e("ABS Name arr=",abs_name_list.toString());
                    if(abs_name_list.size()>0) {
                        if(kk==0) {
                            View headerView = getLayoutInflater().inflate(R.layout.hdr_abs_name_list, null);
                            lv_abs_name.addHeaderView(headerView);
                            kk=1;
                        }
                        final ListAdapter adapter = new SimpleAdapter(MainGridActivity.this, abs_name_list,
                                R.layout.abs_name_list_item, new String[]{"name"}, new int[]{R.id.tv_abs_name_list_item});
                        lv_abs_name.setAdapter(adapter);
                    }
                    else if(abs_name_list.size()<=0){
                        if(kk==0) {
                            View headerView = getLayoutInflater().inflate(R.layout.hdr_abs_name_list, null);
                            lv_abs_name.addHeaderView(headerView);

                            kk=1;
                        }
                        Toast.makeText(MainGridActivity.this, "Full Present...", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainGridActivity.this, "Choose Hour", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setVisibility(View.VISIBLE);
                fab_verify.setVisibility(View.VISIBLE);
                fab_cancel.setVisibility(View.INVISIBLE);
                fab_Go.setVisibility(View.INVISIBLE);
                lv_abs_name.setVisibility(View.INVISIBLE);
               // spn_hour.setVisibility(View.VISIBLE);
                spn_hour.setEnabled(true);
                btn_date.setEnabled(true);
                abs_name_list.clear();
            }
        });

        //set listener for FAB click
        fab_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*gridView.setVisibility(View.VISIBLE);
                lv_abs_name.setVisibility(View.GONE);
                fab_Go.setVisibility(View.INVISIBLE);
                fab_verify.setVisibility(View.VISIBLE);*/

                //creating array of absenties
                ids_abs="[";
                for (int i=0;i<absentees.size();i++) {

                    String m = absentees.get(i);
                    // String uu= String.valueOf(arr1.get(Integer.parseInt(m)- Integer.parseInt(roll_array.get(0).toString())));
                    String uu= roll_id.get(m).toString();
                    if(i==absentees.size()-1) {
                        ids_abs += uu;
                    }
                    else
                    {
                        ids_abs += uu + ",";
                    }

                }
                ids_abs+="]";

                //creating array of all
                ids_all="[";
                for(int j=0;j<arr1.size();j++)
                {
                    if(j==arr1.size()-1) {
                        ids_all += arr1.get(j).toString();
                    }
                    else{
                        ids_all += arr1.get(j).toString() + ",";
                    }
                }
                ids_all+="]";
                /// Toast.makeText(MainGridActivity.this, ids_abs, Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainGridActivity.this, ids_all, Toast.LENGTH_SHORT).show();


                Log.e("roll arr=",roll_array.toString());
                Log.e("ID arr=",arr1.toString());
                Log.e("Name arr=",name_array.toString());

                //checking network availability
                try{
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (!isConnected) {
                        makeText(getApplicationContext(), "Please connect to Network", Toast.LENGTH_SHORT).show();
                    }else{
                        if(sel_hour.equals("Choose Hour")){
                            makeText(MainGridActivity.this, "Please Choose a Hour", Toast.LENGTH_SHORT).show();
                        }else {
                            gridView.setVisibility(View.VISIBLE);
                            lv_abs_name.setVisibility(View.GONE);
                            fab_Go.setVisibility(View.INVISIBLE);
                            fab_verify.setVisibility(View.VISIBLE);
                            new SubmitAtt().execute();
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }



            }

        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Choose Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainGridActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {

                                dd=String.valueOf(dayOfMonth);
                                Integer m=monthOfYear+1;
                                if(m<=9)
                                {
                                    mm="0"+m.toString();
                                }
                                else{
                                    mm=m.toString();
                                }
                                yy=String.valueOf(year);

                                mth_yr=mm+"-"+yy;
                                tv_att_date.setText(dd+"-"+mm+"-"+yy);
                                btn_date.setText(dd+"-"+mm+"-"+yy);
                                if(TYPE==0)
                                {
                                    try {
                                        pdLoading.show();
                                        getatt sendPostReqAsyncTask = new getatt(getApplicationContext());
                                        sendPostReqAsyncTask.execute("");

                                    } catch (Exception e) {
                                        //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        if(TYPE==0)
        {
            try {
                pdLoading.show();
                getatt sendPostReqAsyncTask = new getatt(getApplicationContext());
                sendPostReqAsyncTask.execute("");

            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        if(pref.getString("u_type","").equals("Admin"))
        {
            Intent in = new Intent(getApplicationContext(), T_Att_data.class);
            startActivity(in);
            this.finish();
        }else if(pref.getString("u_type","").equals("Teacher")){
            Intent in = new Intent(getApplicationContext(), T_incharge.class);
            startActivity(in);
            this.finish();
        }



    }

    private class SubmitAtt extends AsyncTask<String,String, String>
    {
        HttpURLConnection conn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading = new ProgressDialog(MainGridActivity.this);
            pdLoading.setMessage("\tPlease Wait..");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            try {
                url = new URL(url_class_list);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("hour",sel_hour)
                        .appendQueryParameter("subjectID",sub_id)
                        .appendQueryParameter("date",dd)
                        .appendQueryParameter("mth_yr",mth_yr)
                        .appendQueryParameter("id_abs", ids_abs)
                        .appendQueryParameter("id_all",ids_all)
                        .appendQueryParameter("strength",strength)
                        .appendQueryParameter("sectionID",sec_id)
                        .appendQueryParameter("class_id",class_id);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                Log.e(TAG, " attttttttttttttttttttttttttt ");
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
                //Log.e(TAG,"response OK");
                // Check if successful connection made
                Log.e(TAG, String.valueOf(response_code));

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



            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            // Toast.makeText(MainGridActivity.this, result, Toast.LENGTH_SHORT).show();
            //this method will be running on UI thread
            if(pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            Log.e(TAG, result);
            if(!result.equals("")){
                makeText(MainGridActivity.this, "Attendance Added..", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
                if(pref.getString("u_type","").equals("Admin"))
                {
                    Intent in = new Intent(getApplicationContext(), T_Att_data.class);
                    startActivity(in);
                }else if(pref.getString("u_type","").equals("Teacher")){
                    Intent in = new Intent(getApplicationContext(), T_incharge.class);
                    startActivity(in);
                }

            }
            else {
                makeText(MainGridActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, result);
            }

        }

    }


    @SuppressWarnings("ALL")
    class getatt extends AsyncTask<String, String, String> {
        Context ccc;
        TextView name,phno,place1,place2;
        ImageView image;
        ImageButton call,message;
        JSONObject c;
        ArrayList<String> ch=new ArrayList<String>();
        getatt(Context c) {
            ccc = c;
        }

        String g;

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("sectionID", sec_id+""));
                nameValuePairs.add(new BasicNameValuePair("monthyear", mth_yr+""));
                nameValuePairs.add(new BasicNameValuePair("day", dd+""));
                nameValuePairs.add(new BasicNameValuePair("hour", sel_hour+""));

                HttpClient httpClient = new DefaultHttpClient();
                phppath path=new phppath();
                HttpPost httpPost = new HttpPost( path.url+"prisma/index.php/data/get_att_by_sectionID_monthyear_day_hour");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                g = httpClient.execute(httpPost, responseHandler);

                // HttpEntity entity = response.getEntity();


            } catch (NullPointerException e) {
                //Toast.makeText(ccc, e.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//        Toast.makeText(ccc,e.toString(), Toast.LENGTH_LONG).show();
                return e.toString();
            }
            return g;

        }

        @Override


        protected void onPostExecute(String result) {
            absentees.clear();
            gridView.invalidate();
            adapter.selectedPositions.clear();
            Log.e("smk",result);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        if(jsonArray.get(i).equals("A"))
                        {
                            Log.e("smk",i+"");
                            adapter.selectedPositions.add(i);

                            absentees.add((String) adapter.getItem(i));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            gridView.setAdapter(adapter);
            pdLoading.dismiss();



        }
    }
}