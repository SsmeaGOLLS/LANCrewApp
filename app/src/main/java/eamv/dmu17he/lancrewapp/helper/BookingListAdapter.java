package eamv.dmu17he.lancrewapp.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.activities.BookingsListOverviewActivity;
import eamv.dmu17he.lancrewapp.model.BookingListViewItem;

/**
 * Created by smeag on 16-01-2018.
 */

public class BookingListAdapter extends ArrayAdapter<BookingListViewItem> {
    Context mContext;
    int mLayoutResourceId;
    View row;

    public BookingListAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;

        final BookingListViewItem currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView time = (TextView) row.findViewById(R.id.timetextview);
        time.setText(currentItem.getTime().toString());
        time.setTextSize(20f);
        time.setTextColor(Color.BLACK);
        final TextView row1 = (TextView) row.findViewById(R.id.rowtextview);
        row1.setText("" + currentItem.getRow());
        row1.setTextColor(Color.BLACK);
        final TextView hallName = (TextView) row.findViewById(R.id.hallnametextview);
        hallName.setText(currentItem.getHallName());
        hallName.setTextColor(Color.BLACK);
        final TextView column = (TextView) row.findViewById(R.id.columntextview);
        column.setText("" + currentItem.getColumn());
        column.setTextColor(Color.BLACK);
        final TextView userName = (TextView) row.findViewById(R.id.usernametextview);
        userName.setText(currentItem.getNickName());
        userName.setTextColor(Color.BLACK);
        final TextView comment = (TextView) row.findViewById(R.id.commenttextview);
        comment.setText(currentItem.getComment());
        comment.setTextColor(Color.BLACK);
        final Button poke = (Button) row.findViewById(R.id.pokebutton);
        poke.setText(String.valueOf(currentItem.getPoke()));
        poke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.getActivity().updatePoke(row, currentItem.getWakeUpID(), mContext, poke, currentItem);


            }
        });
        final Button delete = (Button) row.findViewById(R.id.deletebutton);

        return row;
    }
}
