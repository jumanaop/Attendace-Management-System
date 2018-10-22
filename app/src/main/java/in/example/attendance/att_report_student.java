package in.example.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

public class att_report_student extends AppCompatActivity {
    ProgressDialog progress;
    String dd, mm, yy, mth_yr,mth_yrto,fromDD,toDD;
    int studentID;
    ListView ph_listview;
    List ph_cat_list = new ArrayList();
    students_att_report_list sa;
    students_att_report_array_adapter aaa;
    LayoutInflater infalInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_report_student);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setIndeterminate(false);
        progress.setCancelable(true);
        infalInflater = LayoutInflater.from(this);
        Intent intent=getIntent();
        ph_listview = (ListView) findViewById(R.id.att_report);
        studentID=intent.getIntExtra("studentID",0);
        mth_yr= intent.getStringExtra("from");
        mth_yrto=intent.getStringExtra("to");
        fromDD=intent.getStringExtra("fromDD");
        toDD=intent.getStringExtra("toDD");
        try {
            progress.show();
            getd sendPostReqAsyncTask = new getd(getApplicationContext());
            sendPostReqAsyncTask.execute("");

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

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

                SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
                nameValuePairs.add(new BasicNameValuePair("studentID", studentID+ ""));
                nameValuePairs.add(new BasicNameValuePair("from", mth_yr + ""));
                nameValuePairs.add(new BasicNameValuePair("to", mth_yrto + ""));
                nameValuePairs.add(new BasicNameValuePair("fromDD", fromDD + ""));
                nameValuePairs.add(new BasicNameValuePair("toDD", toDD + ""));


                HttpClient httpClient = new DefaultHttpClient();
                phppath path = new phppath();
                HttpPost httpPost = new HttpPost(path.url + "prisma/index.php/data/get_att_report_by_studentID_from_to");
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
                    sa = new students_att_report_list(c.getString("date"), c.getString("h1"), c.getString("h2"), c.getString("h3"), c.getString("h4"), c.getString("h5"),1);
                    ph_cat_list.add(sa);



                    //det.add(0, c.getString("phone"));
                    //det.add(1, c.getString("studentID"));
                    //phdet.add(i, det);
                    //h.add(i, c.getString("phone"));
                    View hr = new View(getApplicationContext());
                }
                aaa = new students_att_report_array_adapter(getApplicationContext(), R.layout.att_report_student_inflate, ph_cat_list);
                ph_listview.setAdapter(aaa);
                ph_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("studentID",view.getId()+"");
                        Intent intent=new Intent(getApplicationContext(),att_report_student.class);
                        intent.putExtra("studentID",view.getId());
                        intent.putExtra("from",mth_yr);
                        intent.putExtra("to",mth_yrto);
                        intent.putExtra("fromDD",fromDD);
                        intent.putExtra("toDD",toDD);

                        startActivity(intent);
                    }
                });
                if (jsonArray.length() <= 0) {
                    ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.no_result_found, new String[]{"Result not found"});
                    ph_listview.setAdapter(aa);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            if (result.equals("[]")) {
                View view = infalInflater.inflate(R.layout.no_result_found_inflate, null);
            }


            progress.dismiss();


        }

    }
}
