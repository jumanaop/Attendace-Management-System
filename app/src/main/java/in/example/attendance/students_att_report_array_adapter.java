package in.example.attendance;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 30-03-17.
 */
public class students_att_report_array_adapter extends ArrayAdapter<students_att_report_list>{
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    students_att_report_list hlist;
    String phone;
    List objects;
  int hid;


    public students_att_report_array_adapter(Context context, int resource, List objects) {
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
         hlist= (students_att_report_list) getItem(position);




        TextView name=(TextView)convertView.findViewById(R.id.h1);
        name.setText(hlist.getH1());
        if(hlist.getH1()=="null")
            name.setText("-");
        TextView pname=(TextView)convertView.findViewById(R.id.h2);
        pname.setText(hlist.getH2());
        if(hlist.getH2()=="null")
            pname.setText("-");
        TextView regno=(TextView)convertView.findViewById(R.id.h3);
        regno.setText(hlist.getH3());
        if(hlist.getH3()=="null")
            regno.setText("-");
        TextView admno=(TextView)convertView.findViewById(R.id.h4);
        admno.setText(hlist.getH4());
        if(hlist.getH4()=="null")
            admno.setText("-");
        TextView rollno=(TextView)convertView.findViewById(R.id.h5);
        rollno.setText(hlist.getH5());
        if(hlist.getH5()=="null")
            rollno.setText("-");
        TextView phno=(TextView)convertView.findViewById(R.id.date);
        phno.setText(hlist.getDate());


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
