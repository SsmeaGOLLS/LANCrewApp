package eamv.dmu17he.lancrewapp.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import eamv.dmu17he.lancrewapp.R;
import eamv.dmu17he.lancrewapp.model.User;
import eamv.dmu17he.lancrewapp.sql.sqLiteDatabase;

public class ContactsActivity extends AppCompatActivity {

    sqLiteDatabase db = sqLiteDatabase.getDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToUDAO();

            }
        });
    }

    public void postDataToUDAO(){

        db.uDAO().deleteTable();

        User user = new User();
        user.setName("Daniel Jensen");
        user.setPhoneNumber(22868576);
        user.setNickName("FerieDreng");


        User user1 = new User();
        user1.setName("Jonas Kjeldgaard");
        user1.setPhoneNumber(98728576);
        user1.setNickName("PutIRøvManden");


        User user2 = new User();
        user2.setName("Mikkel Nielsen");
        user2.setPhoneNumber(85927194);
        user2.setNickName("MegaHateren");

        User user3 = new User();
        user3.setName("Glenn Mortensen");
        user3.setPhoneNumber(73058034);
        user3.setNickName("ANIMA MAN");

        db.uDAO().insertUser(user);
        db.uDAO().insertUser(user1);
        db.uDAO().insertUser(user2);
        db.uDAO().insertUser(user3);

        List<User> listUsers = db.uDAO().getAll();
        LinearLayout list = (LinearLayout) this.findViewById(R.id.listUsers);


        for (User item : listUsers) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setText(item.toString());
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(16f);
            list.addView(textView);

        }

    }

}
