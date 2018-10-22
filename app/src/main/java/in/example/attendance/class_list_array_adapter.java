package in.example.attendance;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.List;

/**
 * Created by admin on 30-03-17.
 */
public class class_list_array_adapter extends ArrayAdapter{
    private int resource;
    private LayoutInflater inflater;
    private Context context;
  int hid;
    public class_list_array_adapter(Context context, int resource, List objects) {
        super(context, resource, objects);
       this.resource = resource;
        inflater = LayoutInflater.from(context);
        context=context;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* create a new view of my layout and inflate it in the row */
        convertView = (LinearLayout) inflater.inflate(resource, null );

        /* Extract the city's object to show */
        class_list hlist= (class_list) getItem(position);

        TextView c_name=(TextView)convertView.findViewById(R.id.news_heading);
        c_name.setText(hlist.getClasses()+"  "+hlist.getSection());



        hid=hlist.getId();
        convertView.setId(hid);
        return convertView;
    }



}
