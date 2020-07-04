package com.realm.database;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Button writeButton, updateButton, deleteButton;
    private EditText firstNameEditText,lastNameEditText,emailEditText,mobileEditText;
    private TextView showDataHereTextView;
    private Realm realm;
    private RealmAsyncTask transaction;
    private RealmResults<UserTable> userTableResults;
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeButton = (Button)findViewById(R.id.write_button);
        updateButton = (Button)findViewById(R.id.update_button);
        deleteButton = (Button)findViewById(R.id.delete_button);
        firstNameEditText = (EditText)findViewById(R.id.first_name_edit_text);
        lastNameEditText = (EditText)findViewById(R.id.last_name_edit_text);
        emailEditText = (EditText)findViewById(R.id.email_edit_text);
        mobileEditText = (EditText)findViewById(R.id.mobile_edit_text);
        showDataHereTextView = (TextView)findViewById(R.id.show_data_text_view);

        /*
         * open “realm_demo.realm”
         * Get a Realm instance for this thread
         */
        realm = Realm.getDefaultInstance();

        userTableResults = realm.where(UserTable.class).findAll();
        size = userTableResults.size();
        showDataHereTextView.setText("");

        for(UserTable data : userTableResults)
        {
            showDataHereTextView.append("** "+"Name : "+data.getFirstName()+" "+data.getLastName()+" Email : "+data.getEmail()+" Mobile : "+data.getMobile()+" **"+"\n");
        }

        userTableResults.addChangeListener(new RealmChangeListener<RealmResults<UserTable>>() {
            @Override
            public void onChange(RealmResults<UserTable> element) {
                showDataHereTextView.setText("");
                for(UserTable data : element)
                {
                    showDataHereTextView.append("** "+"Name : "+data.getFirstName()+" "+data.getLastName()+" Email : "+data.getEmail()+" Mobile : "+data.getMobile()+" **"+"\n");
                }
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                transaction =  realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realmCreator)
                    {
                        UserTable userTable = realmCreator.createObject(UserTable.class,mobileEditText.getText().toString());
                        userTable.setFirstName(firstNameEditText.getText().toString());
                        userTable.setLastName(lastNameEditText.getText().toString());
                        userTable.setEmail(emailEditText.getText().toString());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess()
                    {
                        LogUtility.informationMessage("onSuccess","Insert Success");
                        clearText();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error)
                    {
                        LogUtility.errorMessage("onError","Insert error : "+error.toString());
                    }
                });
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                transaction =  realm.executeTransactionAsync(new Realm.Transaction()
                {
                    @Override
                    public void execute(Realm realmCreator)
                    {
                        RealmResults<UserTable> results = realmCreator.where(UserTable.class).equalTo("mobile", mobileEditText.getText().toString()).findAll();
                        if (results != null) {
                            for(UserTable userTable : results)
                            {
                                userTable.setFirstName(firstNameEditText.getText().toString());
                                userTable.setLastName(lastNameEditText.getText().toString());
                                userTable.setEmail(emailEditText.getText().toString());
                            }
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess()
                    {
                        LogUtility.informationMessage("onSuccess","Update Success");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error)
                    {
                        LogUtility.errorMessage("onError","Update error : "+error.toString());
                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                transaction =  realm.executeTransactionAsync(new Realm.Transaction()
                {
                    @Override
                    public void execute(Realm realmCreator) {
                        RealmResults<UserTable> results = realmCreator.where(UserTable.class).equalTo("mobile", mobileEditText.getText().toString()).findAll();
                        if (results != null) {
                            results.deleteAllFromRealm();
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess()
                    {
                        LogUtility.informationMessage("onSuccess","Delete Success");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error)
                    {
                        LogUtility.errorMessage("onError","Delete error : "+error.toString());
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * do not forget to close realm object when your work is done
         */
        if (realm != null) {
            realm.close();
        }

        if (transaction != null && !transaction.isCancelled()) {
            LogUtility.errorMessage("Transaction","Transaction Not Null");
            transaction.cancel();
        }
    }

    public void clearText()
    {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        emailEditText.setText("");
        mobileEditText.setText("");
    }
}

