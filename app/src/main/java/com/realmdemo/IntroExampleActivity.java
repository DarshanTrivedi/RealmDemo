package com.realmdemo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.realmdemo.adapter.AdapterPersonList;
import com.realmdemo.realm_bean.Person;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.Sort;

import static com.realmdemo.R.id.edtAge;


/**
 * Created by Darshan Trivedi on 22/9/16 in com.realmdemo.
 */


public class IntroExampleActivity extends Activity {

    public static final String TAG = IntroExampleActivity.class.getName();
    private LinearLayout llAddRecord;
    private ListView lvViewRecord;
    private EditText edtNo, edtName, edtGender;
    private TextView tvAddRecord, tvDeleteRecord, tvViewRecord;

    private Realm realm;
    private RealmConfiguration realmConfig;
    private RealmList<Person> mPersonList = new RealmList<>();

    private AdapterPersonList adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_basic_example);

        // These operations are small enough that
        // we can generally safely run them on the UI thread.

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(this).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        initViews();


        setEvent();

    }

    private void initViews() {

        llAddRecord = (LinearLayout) findViewById(R.id.llAddRecord);
        lvViewRecord = (ListView) findViewById(R.id.lvViewRecord);



        edtNo = (EditText) findViewById(edtAge);
        edtName = (EditText) findViewById(R.id.edtName);
        edtGender = (EditText) findViewById(R.id.edtGender);

        tvAddRecord = (TextView) findViewById(R.id.tvAddRecord);
        tvDeleteRecord = (TextView) findViewById(R.id.tvDeleteRecord);
        tvViewRecord = (TextView) findViewById(R.id.tvViewRecord);

    }

    public void setEvent(){
        tvAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llAddRecord.getVisibility()==View.GONE){
                    llAddRecord.setVisibility(View.VISIBLE);
                    lvViewRecord.setVisibility(View.GONE);
                } else{
                    if(edtNo.getText().toString().isEmpty()){
                        edtNo.setError("Enter Age");
                    } else if(edtName.getText().toString().isEmpty()){
                        edtName.setError("Enter Name");
                    } else if(edtGender.getText().toString().isEmpty()){
                        edtGender.setError("Enter Gender");
                    } else {
                        if(!realm.isInTransaction())
                            realm.beginTransaction();
                        Person person = realm.createObject(Person.class);
                        person.setAge(Integer.parseInt(edtNo.getText().toString()));
                        person.setName(edtName.getText().toString());
                        person.setGender(edtGender.getText().toString());
                        realm.commitTransaction();

                        edtNo.setText("");
                        edtName.setText("");
                        edtGender.setText("");

                        edtNo.setFocusable(true);
                    }
                }
            }
        });

        tvViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAddRecord.setVisibility(View.GONE);
                lvViewRecord.setVisibility(View.VISIBLE);
                mPersonList.clear();
                mPersonList.addAll(realm.where(Person.class).findAllSorted("name", Sort.ASCENDING));
                if(mPersonList.size()>0) {
                    adapter = new AdapterPersonList(IntroExampleActivity.this, mPersonList);
                    lvViewRecord.setAdapter(adapter);
                }else{
                    Toast.makeText(IntroExampleActivity.this,"No record added. Please add record first.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvViewRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(mPersonList.get(position).getName());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    private void showDeleteDialog(final String name) {
        try {
            final Dialog internetDialog = new Dialog(IntroExampleActivity.this);
            internetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            internetDialog.setContentView(R.layout.dialog_delete_person);
            internetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            internetDialog.findViewById(R.id.btYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    internetDialog.dismiss();

                    if (!realm.isInTransaction())
                        realm.beginTransaction();
                    {
                        final Person getPerson = realm.where(Person.class).equalTo("name",name).findFirst();

                        realm.commitTransaction();
                        if (getPerson != null) {

                            if (!realm.isInTransaction()) {
                                realm.beginTransaction();
                            }

                            getPerson.removeFromRealm();

                            realm.commitTransaction();
                        }
                    }
                    mPersonList.clear();
                    mPersonList.addAll(realm.where(Person.class).findAllSorted("name", Sort.ASCENDING));
                    if(mPersonList.size()>0) {
                        adapter = new AdapterPersonList(IntroExampleActivity.this, mPersonList);
                        lvViewRecord.setAdapter(adapter);
                    }else{
                        Toast.makeText(IntroExampleActivity.this,"No record added. Please add record first.",Toast.LENGTH_SHORT).show();
                    }


                }
            });
            internetDialog.findViewById(R.id.btNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    internetDialog.dismiss();
                }
            });
            internetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

