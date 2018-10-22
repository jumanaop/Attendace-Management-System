package in.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.view.View.GONE;
import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;

public class WebViewActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //setting Action bar title and background
        getSupportActionBar().setTitle("Location");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable( getResources().getColor(actionbar_theme)));
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
        // Get the application context
        mContext = getApplicationContext();
        // Get the activity
        mActivity = WebViewActivity.this;
        // Get the widgets reference from XML layout
        mRelativeLayout = findViewById(R.id.rl);
        mWebView = findViewById(R.id.web_view);
        mProgressBar = findViewById(R.id.pb);
        renderWebPage("https://www.google.com/maps/place/Regional+College+Harithagiri/@11.1944539,76.0106735,14.56z/data=!4m5!3m4!1s0x3ba646205d68c559:0xa12fed93f95819dd!8m2!3d11.1935368!4d76.0182666?hl=en-US");

    }

    // Custom method to render a web page
    protected void renderWebPage(String urlToRender){
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                // Do something on page loading started
                // Visible the progressbar
                mProgressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url){
                // Do something when page loading finished
                //Toast.makeText(mContext,"Page Loaded..",Toast.LENGTH_SHORT).show();
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int newProgress){
                // Update the progress bar with page loading progress
                mProgressBar.setProgress(newProgress);
                if(newProgress == 100){
                    // Hide the progressbar
                    mProgressBar.setVisibility(GONE);
                }
            }
        });
        // Enable the javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // Render the web page
        mWebView.loadUrl(urlToRender);
    }
}
