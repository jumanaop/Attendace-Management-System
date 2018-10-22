package in.example.attendance;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import static in.example.attendance.R.color.action_bar_color;
import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;

public class P_View_Att extends AppCompatActivity {

    Button goBtn;
    Spinner spn_years,spn_months;
    Dialog dialog;
    Integer mm,dd,yy;
    public String st_id,sel_month,sel_year;
    private ProgressDialog pDialog;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String TAG = P_View_Att.class.getSimpleName();
    ArrayList<HashMap<String, String>> AttList;
    List attList;
    private static final phppath path=new phppath();
    private static final String url_p_view_att = path.url+"app_p_att.php";
    TextView tv_absent,tv_present,tv_total;
    int absent_count=0,present_count=0,total_count=0;
    GridView gridView11;
    String MONTHYEAR="",STUDENTID="",DAY="";
    List<String> labels;
    att_day_list adl;
    String a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31;
    List ph_cat_list= new ArrayList();
    private List<students_info_list> getStudent_info_list(){
        return ph_cat_list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_view_attendance);
        //setting Action bar title and background
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            window.setStatusBarColor(getResources().getColor(action_bar_color));
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
        //getting the pref values
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        st_id=pref.getString("u_id", null);

        spn_years= findViewById(R.id.spn_years);
        spn_months= findViewById(R.id.spn_months);
        goBtn=findViewById(R.id.at_view_go);
        AttList= new ArrayList<>();
        attList=new ArrayList();
        gridView11 = findViewById(R.id.grids);
        gridView11.setVisibility(View.GONE);
        labels = new ArrayList<>(150);

        //Setting the current month amnd year to the spinners
        Date d=new Date();
        yy=d.getYear();
        dd= d.getDate();
        mm=d.getMonth();
        spn_months.setSelection(mm);







        //capturing the month
        spn_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //sel_month= String.valueOf(spn_months.getSelectedItemPosition()+1);
                //String temp_month=String.valueOf(spn_months.getSelectedItemPosition()+1);
                Integer m=spn_months.getSelectedItemPosition()+1;
                if(m<=9)
                {
                    sel_month="0"+m.toString();
                }
                else{
                    sel_month=m.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(P_View_Att.this, "Please Select Month", Toast.LENGTH_SHORT).show();
            }
        });

        //capturing the Year
        spn_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_year= String.valueOf(spn_years.getSelectedItem());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(P_View_Att.this, "Please Select Year", Toast.LENGTH_SHORT).show();
                }
        });


        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month_year=sel_month+"-"+sel_year;
                MONTHYEAR=month_year;
                labels.clear();
                STUDENTID=st_id+"";
                new Async_view_attendance().execute(month_year,st_id);
            }
        });

    }

    private class Async_view_attendance extends AsyncTask<String, String, String> {
        HttpURLConnection conn;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(P_View_Att.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            try {
                url = new URL(url_p_view_att);
                conn  = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("month_year", params[0])
                        .appendQueryParameter("st_id", params[1]);

                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.e(TAG, "url "+url);
                Log.e(TAG, "paraa "+query);
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }

            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                Log.e(TAG, "response_code==="+response_code);
                if (response_code == HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "URLConnection OK ");
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
            super.onPostExecute(result);
            Integer success;
            String msg;
            // Dismiss the progress dialog
            if (pDialog.isShowing()) pDialog.dismiss();

            Log.e(TAG, "Response from url: " + result);

            try {
                JSONObject jsonObj = new JSONObject(result);
                success = jsonObj.getInt("success");
                msg= jsonObj.getString("message");
                if (success == 1) {

                        JSONArray p_att_details = jsonObj.getJSONArray("p_att_details");
                        for (int i = 0; i < p_att_details.length(); i++) {
                            JSONObject c = p_att_details.getJSONObject(i);
                             a1 = c.getString("a1");
                             a2 = c.getString("a2");
                             a3 = c.getString("a3");
                             a4 = c.getString("a4");
                             a5 = c.getString("a5");
                             a6 = c.getString("a6");
                             a7 = c.getString("a7");
                             a8 = c.getString("a8");
                             a9 = c.getString("a9");
                            a10 = c.getString("a10");
                            a11 = c.getString("a11");
                            a12 = c.getString("a12");
                            a13 = c.getString("a13");
                            a14 = c.getString("a14");
                            a15 = c.getString("a15");
                            a16 = c.getString("a16");
                            a17 = c.getString("a17");
                            a18 = c.getString("a18");
                            a19 = c.getString("a19");
                            a20 = c.getString("a20");
                            a21 = c.getString("a21");
                            a22 = c.getString("a22");
                            a23 = c.getString("a23");
                            a24 = c.getString("a24");
                            a25 = c.getString("a25");
                            a26 = c.getString("a26");
                            a27 = c.getString("a27");
                            a28 = c.getString("a28");
                            a29 = c.getString("a29");
                            a30 = c.getString("a30");
                            a31 = c.getString("a31");
                        }

                    //Setting fetched data into grid view
                        for (int i = 1; i <=31 ; i++) {
                            switch (i){
                                case 1:
                                    String m1= String.valueOf(a1);
                                    if(m1.equals("null") || m1.equals("NULL")) {
                                        labels.add("1");
                                    }
                                    else {
                                        labels.add("1,"+m1);
                                        if(m1.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 2:
                                    String m2= String.valueOf(a2);
                                    if(m2.equals("null") || m2.equals("NULL")) {
                                        labels.add("2");
                                    }
                                    else {
                                        labels.add("2,"+m2);
                                        if(m2.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 3:
                                    String m3= String.valueOf(a23);
                                    if(m3.equals("null") || m3.equals("NULL")) {
                                        labels.add("3");
                                    }
                                    else {
                                        labels.add("3,"+m3);
                                        if(m3.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 4:
                                    String m4= String.valueOf(a4);
                                    if(m4.equals("null") || m4.equals("NULL")) {
                                        labels.add("4");
                                    }
                                    else {
                                        labels.add("4,"+m4);
                                        if(m4.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 5:
                                    String m5= String.valueOf(a5);
                                    if(m5.equals("null") || m5.equals("NULL")) {
                                        labels.add("5");
                                    }
                                    else {
                                        labels.add("5,"+m5);
                                        if(m5.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 6:
                                    String m6= String.valueOf(a6);
                                    if(m6.equals("null") || m6.equals("NULL")) {
                                        labels.add("6");
                                    }
                                    else {
                                        labels.add("6,"+m6);
                                        if(m6.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 7:
                                    String m7= String.valueOf(a7);
                                    if(m7.equals("null") || m7.equals("NULL")) {
                                        labels.add("7");
                                    }
                                    else {
                                        labels.add("7,"+m7);
                                        if(m7.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 8:
                                    String m8= String.valueOf(a8);
                                    if(m8.equals("null") || m8.equals("NULL")) {
                                        labels.add("8");
                                    }
                                    else {
                                        labels.add("8,"+m8);
                                        if(m8.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 9:
                                    String m9= String.valueOf(a9);
                                    if(m9.equals("null") || m9.equals("NULL")) {
                                        labels.add("9");
                                    }
                                    else {
                                        labels.add("9,"+m9);
                                        if(m9.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 10:
                                    String m10= String.valueOf(a10);
                                    if(m10.equals("null") || m10.equals("NULL")) {
                                        labels.add("10");
                                    }
                                    else {
                                        labels.add("10,"+m10);
                                        if(m10.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 11:
                                    String m11= String.valueOf(a11);
                                    if(m11.equals("null") || m11.equals("NULL")) {
                                        labels.add("11");
                                    }

                                    else {
                                        labels.add("11,"+m11);
                                        if(m11.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 12:
                                    String m12= String.valueOf(a12);
                                    if(m12.equals("null") || m12.equals("NULL")) {
                                        labels.add("12");
                                    }
                                    else {
                                        labels.add("12,"+m12);
                                        if(m12.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 13:
                                    String m13= String.valueOf(a13);
                                    if(m13.equals("null") || m13.equals("NULL")) {
                                        labels.add("13");
                                    }
                                    else {
                                        labels.add("13,"+m13);
                                        if(m13.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 14:
                                    String m14= String.valueOf(a14);
                                    if(m14.equals("null") || m14.equals("NULL")) {
                                        labels.add("14");
                                    }
                                    else {
                                        labels.add("14,"+m14);
                                        if(m14.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 15:
                                    String m15= String.valueOf(a15);
                                    if(m15.equals("null") || m15.equals("NULL")) {
                                        labels.add("15");
                                    }
                                    else {
                                        labels.add("15,"+m15);
                                        if(m15.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 16:
                                    String m16= String.valueOf(a16);
                                    if(m16.equals("null") || m16.equals("NULL")) {
                                        labels.add("16");
                                    }
                                    else {
                                        labels.add("16,"+m16);
                                        if(m16.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 17:
                                    String m17= String.valueOf(a17);
                                    if(m17.equals("null") || m17.equals("NULL")) {
                                        labels.add("17");
                                    }
                                    else {
                                        labels.add("17,"+m17);
                                        if(m17.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 18:
                                    String m18= String.valueOf(a18);
                                    if(m18.equals("null") || m18.equals("NULL")) {
                                        labels.add("18");
                                    }
                                    else {
                                        labels.add("18,"+m18);
                                        if(m18.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 19:
                                    String m19= String.valueOf(a19);
                                    if(m19.equals("null") || m19.equals("NULL")) {
                                        labels.add("19");
                                    }
                                    else {
                                        labels.add("19,"+m19);
                                        if(m19.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 20:
                                    String m20= String.valueOf(a20);
                                    if(m20.equals("null") || m20.equals("NULL")) {
                                        labels.add("20");
                                    }
                                    else {
                                        labels.add("20,"+m20);
                                        if(m20.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 21:
                                    String m21= String.valueOf(a21);
                                    if(m21.equals("null") || m21.equals("NULL")) {
                                        labels.add("21");
                                    }
                                    else {
                                        labels.add("21,"+m21);
                                        if(m21.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 22:
                                    String m22= String.valueOf(a22);
                                    if(m22.equals("null") || m22.equals("NULL")) {
                                        labels.add("22");
                                    }
                                    else {
                                        labels.add("22,"+m22);
                                        if(m22.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 23:
                                    String m23= String.valueOf(a23);
                                    if(m23.equals("null") || m23.equals("NULL")) {
                                        labels.add("23");
                                    }
                                    else {
                                        labels.add("23,"+m23);
                                        if(m23.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 24:
                                    String m24= String.valueOf(a24);
                                    if(m24.equals("null") || m24.equals("NULL")) {
                                        labels.add("24");
                                    }
                                    else {
                                        labels.add("24,"+m24);
                                        if(m24.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 25:
                                    String m25= String.valueOf(a25);
                                    if(m25.equals("null") || m25.equals("NULL")) {
                                        labels.add("25");
                                    }
                                    else {
                                        labels.add("25,"+m25);
                                        if(m25.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 26:
                                    String m26= String.valueOf(a26);
                                    if(m26.equals("null") || m26.equals("NULL")) {
                                        labels.add("26");
                                    }
                                    else {
                                        labels.add("26,"+m26);
                                        if(m26.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 27:
                                    String m27= String.valueOf(a27);
                                    if(m27.equals("null") || m27.equals("NULL")) {
                                        labels.add("27");
                                    }
                                    else {
                                        labels.add("27,"+m27);
                                        if(m27.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 28:
                                    String m28= String.valueOf(a28);
                                    if(m28.equals("null") || m28.equals("NULL")) {
                                        labels.add("28");
                                    }
                                    else {
                                        labels.add("28,"+m28);
                                        if(m28.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 29:
                                    String m29= String.valueOf(a29);
                                    if(m29.equals("null") || m29.equals("NULL")) {
                                        labels.add("29");
                                    }
                                    else {
                                        labels.add("29,"+m29);
                                        if(m29.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 30:
                                    String m30= String.valueOf(a30);
                                    if(m30.equals("null") || m30.equals("NULL")) {
                                        labels.add("30");
                                    }
                                    else {
                                        labels.add("30,"+m30);
                                        if(m30.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;
                                case 31:
                                    String m31= String.valueOf(a31);
                                    if(m31.equals("null") || m31.equals("NULL")) {
                                        labels.add("31");
                                    }
                                    else {
                                        labels.add("31,"+m31);
                                        if(m31.equals("P"))
                                            present_count+=1;
                                        else
                                            absent_count+=1;
                                    }
                                    break;

                                default:
                                    break;
                            }

                        }
                        final GridView11Adapter adapter = new GridView11Adapter(labels, P_View_Att.this);
                        gridView11.setVisibility(View.VISIBLE);
                        gridView11.setAdapter(adapter);
                    gridView11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            DAY=(position+1)+"";
                            ph_cat_list.clear();
                            try {
                               // progress.show();
                               get_att sendPostReqAsyncTask = new get_att(getApplicationContext());
                                sendPostReqAsyncTask.execute("");

                            } catch (Exception e) {
                                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }else if(success == 2){
                    gridView11.setVisibility(View.GONE);
                    Toast.makeText(P_View_Att.this, msg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, msg);

                }else if(success == 3){
                    gridView11.setVisibility(View.GONE);
                    Toast.makeText(P_View_Att.this, msg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, msg);
                }

                tv_absent=(TextView)findViewById(R.id.absent);
                tv_present=(TextView)findViewById(R.id.present);
                tv_total=(TextView)findViewById(R.id.total);
                tv_absent.setText(absent_count+"");
                tv_present.setText(present_count+"");
                tv_total.setText((absent_count+present_count)+"");
            } catch (JSONException e) {
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
    public class ViewDialog {
        EditText sms;
        TextView limit_tv;
        Context context=P_View_Att.this;
        int limit=0;
        public void showDialog(Activity activity, String msg){
            dialog = new Dialog(context,R.style.AppTheme);// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Attendance");
            dialog.setCancelable(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // dialog.getWindow().getAttributes().windowAnimations=R.style.animationDialog;
            dialog.setContentView(R.layout.atte_list_inflate);
            TextView tv = (TextView) dialog.findViewById(R.id.date);
            tv.setText(DAY+"-"+MONTHYEAR);
            ListView lv = (ListView) dialog.findViewById(R.id.att_list);
            att_list_info_array_adapter  aaa=new att_list_info_array_adapter(getApplicationContext(), R.layout.att_day_list_inflate, ph_cat_list);
            lv.setAdapter(aaa);
            if(ph_cat_list.size()<=0)
            {
                ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.no_result_found, new String[]{"Result not found"});
                lv.setAdapter(aa);
            }
            dialog.show();

        }
    }


    @SuppressWarnings("ALL")
    class get_att extends AsyncTask<String, String, String> {
        Context ccc;
        TextView name,phno,place1,place2;
        ImageView image;
        ImageButton call,message;
        JSONObject c;
        ArrayList<String> ch=new ArrayList<String>();
        get_att(Context c) {
            ccc = c;
        }

        String g;

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();



                nameValuePairs.add(new BasicNameValuePair("monthyear", MONTHYEAR+""));
                nameValuePairs.add(new BasicNameValuePair("studentID", STUDENTID+""));
                nameValuePairs.add(new BasicNameValuePair("day", DAY+""));

                HttpClient httpClient = new DefaultHttpClient();
                phppath path=new phppath();
                HttpPost httpPost = new HttpPost( path.url+"prisma/index.php/data/get_sub_attendance_by_monthyear_userid");
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

            //  hlist.removeAllViewsInLayout();
            ArrayList<View> lv;
            try {
                JSONArray jsonArray = new JSONArray(result);


                for (int i = 0; i < jsonArray.length(); i++) {
                    c = jsonArray.getJSONObject(i);
                    List<String> det=new ArrayList<>();
                    if(c.getString("status").equals("null"))
                    adl=new att_day_list(c.getString("subject"), c.getString("hour"),"No class");
                    else if(c.getString("status").equals("P"))
                        adl=new att_day_list(c.getString("subject"), c.getString("hour"),"Present");
                    else if(c.getString("status").equals("A"))
                        adl=new att_day_list(c.getString("subject"), c.getString("hour"),"Absent");
                    else
                    adl=new att_day_list(c.getString("subject"), c.getString("hour"),c.getString("status"));
                    ph_cat_list.add(adl);

                }
                ViewDialog alert = new ViewDialog();

                alert.showDialog(getParent(), "Attendance");

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Attendance not taken", Toast.LENGTH_SHORT).show();
            }
            if(result.equals("[]"))
            {
               // view = infalInflater.inflate(R.layout.no_result_found_inflate, null);
            }


           // progress.dismiss();


        }

    }
}


