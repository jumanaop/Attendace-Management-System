package in.example.attendance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static in.example.attendance.R.color.actionbar_theme;
import static in.example.attendance.R.color.statusbartheme;

public class P_homeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Alert Dialog Manager

    SharedPreferences pref;
    String image_name="";
    ImageView photo;
    ProgressDialog progress;
    Bitmap bitmap;
    // Session Manager Class
    AppSession session;
    phppath path=new phppath();
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_home);

        // Session class instance
        session = new AppSession(getApplicationContext());
        session.checkLogin();
        pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setIndeterminate(false);
        progress.setCancelable(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable( getResources().getColor(actionbar_theme)));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Setting the color for status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(getResources().getColor(statusbartheme));
            getWindow().setStatusBarColor(getResources().getColor(statusbartheme));
        }
        photo=(ImageView)findViewById(R.id.iv_pic);
        //Toast.makeText(getApplicationContext(), pref.getString("photo",""), Toast.LENGTH_LONG).show();
        Picasso.with(getApplicationContext()).invalidate(path.url+"appp/uploads/"+image_name);
        Picasso.with(getApplicationContext()).load(path.url+"appp/uploads/"+pref.getString("photo","")).transform(new P_homeActivity.CircleTransform()).into(photo);
        int strokeWidth = 5;
        GradientDrawable gD = new GradientDrawable();
        gD.setShape(GradientDrawable.OVAL);
        gD.setStroke(5, Color.parseColor("#ffffff"));
        photo.setBackground(gD);
        // Picasso.with(getApplicationContext()).load(path.url+"appp/uploads/images/"+pref.getString("photo","")).into(photo);

        //setting current date
        TextView tv_date=findViewById(R.id.tv_date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        String f=df.format(c.getTime());
        tv_date.setText(f);
        //Setting the name
        TextView tv_name=findViewById(R.id.tv_name);
        TextView grade=findViewById(R.id.grade);
        TextView regno=findViewById(R.id.regno);



        //Reading the pref value
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);

        tv_name.setText(pref.getString("u_name", null));
        grade.setText(pref.getString("class", null));
        regno.setText(pref.getString("registerNO", null));

        //////////////Card View Section///////////////
        CardView view_att=findViewById(R.id.view_att);
        view_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P_homeActivity.this, P_View_Att.class);

                startActivity(intent);
            }
        });




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.p_home, menu);
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver




            String[] filePath = { MediaStore.Images.Media.DATA };

            File f = new File(String.valueOf(filePath));
            //image_name = f.getName();

            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imagePath, options);


            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();

            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

          /*  Paint painta = new Paint();
            painta.setColor(Color.GRAY);
            painta.setStyle(Paint.Style.STROKE);
            painta.setAntiAlias(true);
            painta.setStrokeWidth(5);
            canvas.drawCircle(r, r, r, painta);*/
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.p_logout) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("u_type",null);
            editor.commit();
            Intent i = new Intent(getApplicationContext(), LoginActivity1.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_att) {
            Intent i = new Intent(getApplicationContext(), P_View_Att.class);
            startActivity(i);
        }

        else if (id == R.id.nav_logout) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("u_type",null);
            editor.commit();
            Intent i = new Intent(getApplicationContext(), LoginActivity1.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
