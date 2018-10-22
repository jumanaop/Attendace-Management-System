package in.example.attendance;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

public class GridView11Adapter extends BaseAdapter {

    private Activity activity;
    private String[] strings;
    public List selectedPositions;

    public GridView11Adapter(List<String> strings, Activity activity) {
        this.strings = strings.toArray(new String[0]);
        this.activity = activity;
        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;
        customView.display(strings[position], selectedPositions.contains(position));

        return customView;
    }
}
