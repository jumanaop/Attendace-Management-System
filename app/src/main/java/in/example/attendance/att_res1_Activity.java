package in.example.attendance;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static in.example.attendance.R.color.action_bar_color;

public class att_res1_Activity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_res1_);
        //setting Action bar title and background
        getSupportActionBar().setTitle("Attendance");
        getSupportActionBar().setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                Color.parseColor("#098a83"),
                Color.parseColor("#098a83"),
        }));
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(action_bar_color));
        }
        //Setting the current date without time
        TextView tv_date=findViewById(R.id.tv_date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        String f=df.format(c.getTime());
        tv_date.setText(f);

        tv=findViewById(R.id.tv_absent);
        tv.setText("Absentees are:");

        getIntentData();
    }
    public void getIntentData() {
        ArrayList<String> stringArrayList = getIntent().getStringArrayListExtra("absentees");
        //Appending absentees number into JSONArray
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < stringArrayList.size(); i++) {
            jsonArray.put(stringArrayList.get(i));
        }

        Toast.makeText(this, ""+jsonArray+"", Toast.LENGTH_SHORT).show();

        assert stringArrayList != null;

        if (stringArrayList.size() > 0) {
            for (int i = 0; i < stringArrayList.size(); i++) {
                if (i <= stringArrayList.size() - 1) {
                    String temp=stringArrayList.get(i);
                    //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
                    tv.setText(tv.getText() + "  "+ stringArrayList.get(i) + "  ");
                } else {
                    tv.setText(tv.getText() + "  " +stringArrayList.get(i) +".");
                }
            }
        }
        else{
            tv.setText("All are present..");

        }

    }
}
