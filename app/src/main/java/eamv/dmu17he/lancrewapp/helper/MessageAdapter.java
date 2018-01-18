package eamv.dmu17he.lancrewapp.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.activities.CrewMessageActivity;
import eamv.dmu17he.lancrewapp.model.Message;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public MessageAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Message message = getItem(position);

        Log.i("IDID:",position+"<- ");

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        final TextView senderTextView = (TextView) row.findViewById(R.id.sender);
        final TextView messageTextView = (TextView) row.findViewById(R.id.messageText);
        final TextView timeTextView = (TextView) row.findViewById(R.id.time);


        // Skal ændres til at hente Member name ved brug af memberId
        senderTextView.setText(message.getMemberId());
        messageTextView.setText(message.getText());


        //message.getcrea

        timeTextView.setText(MessageTime.display(message.getCreatedAt()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (mContext instanceof CrewMessageActivity) {
            CrewMessageActivity activity = (CrewMessageActivity) mContext;

            if (message.getMemberId().equals(activity.getMessageController().getMemberId()))
            {
                params.setMargins(50, 0, 0, 0);
            messageTextView.setBackgroundColor(activity.getResources().getColor(R.color.chatcolor2));
            } else {
                params.setMargins(0, 0, 50, 0);
                messageTextView.setBackgroundColor(activity.getResources().getColor(R.color.chatcolor1));
            }
        }



        senderTextView.setLayoutParams(params);
        messageTextView.setLayoutParams(params);
        timeTextView.setLayoutParams(params);
        row.setTag(message);
        return row;
    }

}