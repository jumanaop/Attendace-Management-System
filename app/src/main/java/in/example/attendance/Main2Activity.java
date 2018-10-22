package in.example.attendance;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Main2Activity extends AppCompatActivity {

    private EditText courses, seats, statuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        courses = findViewById(R.id.course);
        seats = findViewById(R.id.seat);
        statuses = findViewById(R.id.status);
        Button viewbtn= findViewById(R.id.view1);
        Button enter= findViewById(R.id.enter);

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent i1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i1);*/
            }
        });

    }

    //TRIGGERS WHEN ENTER BUTTON IS CLICKED , VIA XML
    public void signup(View v) {
        String course = courses.getText().toString();
        String seat = seats.getText().toString();
        String status = statuses.getText().toString();

        //Toast.makeText(this, course+"/"+seat+"/"+status, Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}