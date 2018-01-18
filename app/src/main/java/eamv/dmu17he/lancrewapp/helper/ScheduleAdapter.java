package eamv.dmu17he.lancrewapp.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.Schedule;

/**
 * Created by smeag on 14-01-2018.
 */

public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    Context mContext;
    int mLayoutResourceId;

    public ScheduleAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Schedule currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView title = (TextView) row.findViewById(R.id.titleTV);
        title.setText(currentItem.getTitle());
        title.setTextSize(20f);
        title.setTextColor(Color.BLACK);
        final TextView info = (TextView) row.findViewById(R.id.infoTextView);
        info.setText(currentItem.getInfo());
        info.setTextColor(Color.BLACK);

        return row;
    }
}
