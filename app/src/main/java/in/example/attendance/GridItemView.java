package in.example.attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GridItemView extends FrameLayout {

    private TextView textView;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_grid, this);
        textView = getRootView().findViewById(R.id.text);
    }

    public void display(String text, boolean isSelected) {

        String str[]=text.split(",");
        if(str.length==2)
        {
            if(str[1].equals("A"))
                textView.setTextColor(Color.RED);
            else if(str[1].equals("P"))
                textView.setTextColor(Color.GREEN);
            textView.setText(str[0]);
        }
        else
            textView.setText(text);



        display(isSelected);
    }

    public void display(boolean isSelected) {
        // is selected for grid itenms..
        textView.setBackgroundResource(isSelected ? R.drawable.sq_act : R.drawable.sq_deact);

    }
}