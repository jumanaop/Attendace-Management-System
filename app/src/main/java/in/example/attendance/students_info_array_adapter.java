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
public class students_info_array_adapter extends ArrayAdapter<students_info_list>{
    private int resource;
    private LayoutInflater inflater;
    private Context context;
   students_info_list hlist;
    String phone;
    List objects;
  int hid;
  final ViewHolder holder=new ViewHolder();;
  final List<students_info_list> list;
    public students_info_array_adapter(Context context, int resource, List objects,List<students_info_list> list) {
        super(context, resource, objects);
       this.resource = resource;
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.objects=objects;
        this.list=list;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* create a new view of my layout and inflate it in the row */
        convertView = (LinearLayout) inflater.inflate(resource, null );


        /* Extract the city's object to show */
         hlist= (students_info_list) getItem(position);
        Button call=(Button)convertView.findViewById(R.id.call);
        Button message=(Button)convertView.findViewById(R.id.message);
        CheckBox select=(CheckBox) convertView.findViewById(R.id.select);
        //select.setChecked(true);

        holder.cb=(CheckBox) convertView.findViewById(R.id.select);
        holder.tv=(TextView) convertView.findViewById(R.id.phno);
        convertView.setTag(holder);
        convertView.setTag(R.id.select,holder.cb);
        holder.cb.setTag(position);
        holder.tv.setTag(position);


        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               int getPosition=(Integer)buttonView.getTag();
                list.get(getPosition).setSelected(buttonView.isChecked());

            }
        });
         phone=hlist.getPhno().toString();
         call.setText(hlist.getPhno());
         message.setText(hlist.getPhno());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Button bb=(Button)v;
                intent.setData(Uri.parse("tel:" +bb.getText().toString()));
                intent. addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                Button bb=(Button)v;
                intent.setData(Uri.parse("sms:" +bb.getText().toString()));
                intent. addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        TextView c_name=(TextView)convertView.findViewById(R.id.name);
        c_name.setText(hlist.getC_name());
        TextView phno=(TextView)convertView.findViewById(R.id.phno);
        phno.setText(phone);


        //image.setBackground(gD);.

        hid=hlist.getId();
        convertView.setId(hid);
        //list.get(position).setSelected(true);
        holder.cb.setChecked(list.get(position).isSelected());
        return convertView;
    }


static class ViewHolder
    {
        protected TextView tv;
        protected CheckBox cb;
    }
}
