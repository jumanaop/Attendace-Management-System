package in.example.attendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import static in.example.attendance.R.color.statusbartheme;

public class first_page extends Activity {
AutoCompleteTextView a;

    String login_type;
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.open_image);
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_image);
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
        new CountDownTimer(1000,500){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish() {
                        //set the new Content of your activity
                        Intent i = new Intent(getApplicationContext(), LoginActivity1.class);
                        startActivity(i);
                        finish();
            }
        }.start();
    }
}
