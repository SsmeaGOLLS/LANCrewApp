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
import eamv.dmu17he.lancrewapp.model.Hall;
import eamv.dmu17he.lancrewapp.model.Space;

/**
 * Created by alexj on 1/16/2018.
 */

public class ItemAdapter extends ArrayAdapter<Hall> {
    Context mContext;
    int mLayoutResourceId;

    public ItemAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Hall currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView rowTextView = (TextView) row.findViewById(R.id.rowtextview);
        rowTextView.setText(currentItem.getSpaces().get(1).getRow());
        rowTextView.setTextSize(20f);
        rowTextView.setTextColor(Color.BLACK);

        return row;
    }
}
