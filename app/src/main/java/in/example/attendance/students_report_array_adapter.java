package in.example.attendance;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 30-03-17.
 */
public class students_report_array_adapter extends ArrayAdapter<students_report_list>{
    private int resource;
    private LayoutInflater inflater;
    private Context context;
   students_report_list hlist;
    String phone;
    List objects;
  int hid;


    public students_report_array_adapter(Context context, int resource, List objects) {
        super(context, resource, objects);
       this.resource = resource;
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.objects=objects;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* create a new view of my layout and inflate it in the row */
        convertView = (LinearLayout) inflater.inflate(resource, null );


        /* Extract the city's object to show */
         hlist= (students_report_list) getItem(position);




        TextView name=(TextView)convertView.findViewById(R.id.name);
        name.setText(hlist.getName());
        TextView pname=(TextView)convertView.findViewById(R.id.pname);
        pname.setText(hlist.getPname());
        TextView regno=(TextView)convertView.findViewById(R.id.registerno);
        regno.setText(hlist.getRegno());
        TextView admno=(TextView)convertView.findViewById(R.id.admno);
        admno.setText(hlist.getAdmno());
        TextView rollno=(TextView)convertView.findViewById(R.id.roll);
        rollno.setText(hlist.getRollno());

        TextView phno=(TextView)convertView.findViewById(R.id.phno);
        phno.setText(hlist.getPhno());


        //image.setBackground(gD);.

        hid=hlist.getId();
        convertView.setId(hid);
        //list.get(position).setSelected(true);

        return convertView;
    }


static class ViewHolder
    {
        protected TextView tv;
        protected CheckBox cb;
    }
}
