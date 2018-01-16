package eamv.dmu17he.lancrewapp.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;

import eamv.dmu17he.lancrewapp.R;

/**
 * Created by patri on 15-01-2018.
 */

public class DateTimeAdapter extends ArrayAdapter<Calendar> {
    Context context;
    int mlayoutresourceid;
    public DateTimeAdapter(Context context, int layoutresourceid){
        super(context,layoutresourceid);
        this.context = context;
        mlayoutresourceid = layoutresourceid;
    }
    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        View row = convertview;
        final Calendar currentitem = getItem(position);
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(mlayoutresourceid, parent, false);
        }
        row.setTag(currentitem);
        final TextView calendar = (TextView)row.findViewById(R.id.rowtextview);
        calendar.setText(currentitem.toString());
        return row;
    }
}
