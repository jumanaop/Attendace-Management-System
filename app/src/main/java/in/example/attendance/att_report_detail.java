package in.example.attendance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.makeText;
import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;

public class att_report_detail extends AppCompatActivity {
    LinearLayout hlist, advance_search, phdetl;
    LayoutInflater infalInflater;
    View view, temp_v;
    SearchView search, search_a, suggest_search;
    InputMethodManager imm;
    ArrayList<List<String>> phdet = new ArrayList<List<String>>();
    int type;
    ProgressDialog progress;
    String district_str = "";
    List<String> hid;
    String district, search_query;
    int status=0;
    public Integer mYear, mMonth, mDay;
    int stype;
    PopupWindow pw;
    String sel_hour;
    AutoCompleteTextView autoCompleteTextView;
    String suggession[];
    Context context;
    SwipeRefreshLayout swipeLayout;
    int cid;
    List ph_cat_list = new ArrayList();
    ListView ph_listview;
    students_report_array_adapter aaa;
    Intent intent;
    String search_i = "";
    Button send;
    Dialog dialog;
    String message_str = "", phlist = "[";
    int selected_stud_count = 0;
    Spinner spn_hour;
    students_report_list sa;
    CheckBox select_all;
    ArrayList<String> checked_phone_list = new ArrayList<String>();
    private Button btn_date;
    public String dd, mm, yy, mth_yr;
    Spinner status_sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_report_detail);
        spn_hour=findViewById(R.id.spn_hour);
        status_sp=findViewById(R.id.status);

        spn_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sel_hour=String.valueOf(spn_hour.getItemAtPosition(position));
                if(!sel_hour.equals("Choose Hour")){
                    sel_hour = String.valueOf(position);
                    //sel_hour=spn_hour.getSelectedItem();
                    //makeText(getApplicationContext(), sel_hour, Toast.LENGTH_SHORT).show();
                }
                try {
                    progress.show();
                    getd sendPostReqAsyncTask = new getd(getApplicationContext());
                    sendPostReqAsyncTask.execute("");

                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        status_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(status_sp.getSelectedItemPosition()==0)
                     status=0;
                else
                    status=1;
                try {
                    progress.show();
                    getd sendPostReqAsyncTask = new getd(getApplicationContext());
                    sendPostReqAsyncTask.execute("");

                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        intent = getIntent();
        context = this;
        btn_date = findViewById(R.id.btn_date);
        Date d = new Date();
        dd = String.valueOf(d.getDate());
        Integer m = d.getMonth() + 1;
        if (m <= 9) {
            mm = "0" + m.toString();
        } else {
            mm = m.toString();
        }
        Calendar instance = Calendar.getInstance();
        yy = String.valueOf(instance.get(Calendar.YEAR));
        mth_yr = String.valueOf(mm + "-" + yy);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setIndeterminate(false);
        progress.setCancelable(true);

        btn_date.setText(dd + "-" + mm + "-" + yy);


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Choose Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(att_report_detail.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {

                                dd = String.valueOf(dayOfMonth);
                                Integer m = monthOfYear + 1;
                                if (m <= 9) {
                                    mm = "0" + m.toString();
                                } else {
                                    mm = m.toString();
                                }
                                yy = String.valueOf(year);

                                mth_yr = mm + "-" + yy;

                                btn_date.setText(dd + "-" + mm + "-" + yy);
                                try {
                                    progress.show();
                                    getd sendPostReqAsyncTask = new getd(getApplicationContext());
                                    sendPostReqAsyncTask.execute("");

                                } catch (Exception e) {
                                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        try {

            getd sendPostReqAsyncTask = new getd(getApplicationContext());
            sendPostReqAsyncTask.execute("");

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    ph_listview = (ListView) findViewById(R.id.att_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = getIntent().getIntExtra("type", 1);
        cid = getIntent().getIntExtra("cid", 2);
        getSupportActionBar().setTitle("Report");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(actionbar_theme)));
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }


        hid = new ArrayList<String>();

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setIndeterminate(false);
        progress.setCancelable(true);



        infalInflater = LayoutInflater.from(this);
        try {
            // progress.show();
            //swipeLayout.setRefreshing(true);

            stype = 0;
            getd sendPostReqAsyncTask = new getd(getApplicationContext());
            sendPostReqAsyncTask.execute("");

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }




   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_sms_menu, menu);

        return true;
    }*/
    }
    @SuppressWarnings("ALL")
    class getd extends AsyncTask<String, String, String> {
        Context ccc;
        TextView name, phno, place1, place2;
        ImageView image;
        ImageButton call, message;
        JSONObject c;
        ArrayList<String> ch = new ArrayList<String>();

        getd(Context c) {
            ccc = c;
        }

        String g;

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("classesID", intent.getStringExtra("class_id") + ""));
                nameValuePairs.add(new BasicNameValuePair("sectionID", intent.getStringExtra("sec_id") + ""));
                nameValuePairs.add(new BasicNameValuePair("hour", (sel_hour) + ""));
                nameValuePairs.add(new BasicNameValuePair("monthyear", mth_yr + ""));

                HttpClient httpClient = new DefaultHttpClient();
                phppath path = new phppath();
                HttpPost httpPost = new HttpPost(path.url + "prisma/index.php/data/get_att_report_by_courseID_sectionID_hour");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                g = httpClient.execute(httpPost, responseHandler);

                // HttpEntity entity = response.getEntity();
            } catch (NullPointerException e) {
                //Toast.makeText(ccc, e.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//		Toast.makeText(ccc,e.toString(), Toast.LENGTH_LONG).show();
                return e.toString();
            }
            return g;

        }

        @Override


        protected void onPostExecute(String result) {
            ph_cat_list.clear();
            Log.e("error",result);
            //  hlist.removeAllViewsInLayout();
            ArrayList<View> lv;
            try {
                JSONArray jsonArray = new JSONArray(result);


                for (int i = 0; i < jsonArray.length(); i++) {
                    c = jsonArray.getJSONObject(i);
                    List<String> det = new ArrayList<>();
                    sa = new students_report_list(c.getString("name"), c.getString("parentID"), c.getString("registerNO"), c.getString("roll"), c.getString("a"+dd), c.getString("state"), c.getInt("studentID"));
                    if(status==0)
                    {
                        if( c.getString("a"+dd).equals("A"))
                        {
                            ph_cat_list.add(sa);
                        }
                    }
                    else if(status==1)
                    {
                        if( c.getString("a"+dd).equals("P"))
                        {
                            ph_cat_list.add(sa);
                        }
                    }

                    det.add(0, c.getString("phone"));
                    det.add(1, c.getString("studentID"));
                    phdet.add(i, det);
                    ch.add(i, c.getString("phone"));
                    View hr = new View(getApplicationContext());
                }
                aaa = new students_report_array_adapter(getApplicationContext(), R.layout.student_att_report_inflate, ph_cat_list);
                ph_listview.setAdapter(aaa);

                if (jsonArray.length() <= 0) {
                    ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.no_result_found, new String[]{"Result not found"});
                    ph_listview.setAdapter(aa);
                }
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
            if (result.equals("[]")) {
                view = infalInflater.inflate(R.layout.no_result_found_inflate, null);
            }


            progress.dismiss();


        }

    }
}