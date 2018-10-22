package in.example.attendance;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
public class att_list_info_array_adapter extends ArrayAdapter<att_day_list>{
    private int resource;
    private LayoutInflater inflater;
    private Context context;
   att_day_list hlist;
    String phone;
    List objects;
  int hid;
  final ViewHolder holder=new ViewHolder();;

    public att_list_info_array_adapter(Context context, int resource, List objects) {
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
         hlist= (att_day_list) getItem(position);

        TextView subject=(TextView)convertView.findViewById(R.id.subject);
        subject.setText(hlist.getSubject());
        TextView hour=(TextView)convertView.findViewById(R.id.hour);
        hour.setText(hlist.getHour());
        TextView status=(TextView)convertView.findViewById(R.id.status);
        status.setText(hlist.getStatus());
        if(hlist.getStatus().equals("Absent"))
        {
            status.setTextColor(Color.RED);
            subject.setTextColor(Color.RED);
            hour.setTextColor(Color.RED);
        }


        //image.setBackground(gD);.


       // convertView.setId(hid);
        //list.get(position).setSelected(true);

        return convertView;
    }


static class ViewHolder
    {
        protected TextView tv;
        protected CheckBox cb;
    }
}
