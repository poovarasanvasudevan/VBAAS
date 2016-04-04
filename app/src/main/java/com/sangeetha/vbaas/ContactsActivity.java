package com.sangeetha.vbaas;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.image.SmartImageView;
import com.sangeetha.vbaas.core.adapter.ContactAdapter;
import com.sangeetha.vbaas.core.model.ContactModel;
import com.sangeetha.vbaas.core.util.ItemClickSupport;
import com.sangeetha.vbaas.model.Contacts;
import com.sangeetha.vbaas.model.Relation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import carbon.beta.AppBarLayout;
import carbon.widget.Button;
import carbon.widget.CheckBox;
import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class ContactsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.contactlist)
    RecyclerView contactlist;

    String relationPosition;
    String phoneNo = null;
    String name = null;
    Long id = null;
    boolean msg1 = false;
    String cmessage = "";
    List<Relation> rm;
    List<String> relation;
    @Bind(R.id.contactswipe)
    SwipeRefreshLayout contactswipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        contactlist.setLayoutManager(llm);

        rm = new ArrayList<>();
        relation = new ArrayList<>();

        new ContactLoader().execute();

        contactswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ContactLoader().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addcontact) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case 1:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {

        Cursor cursor = null;
        try {


            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            id = cursor.getLong(idIndex);


            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title("Add Contact")
                    .customView(R.layout.add_contact_dialog, true)
                    .show();

            View v = dialog.getCustomView();

            SmartImageView contactImageDialog = (SmartImageView) v.findViewById(R.id.dialogContactImage);
            TextView contactNameDialog = (TextView) v.findViewById(R.id.dialogContactName);
            TextView contactNumberDialog = (TextView) v.findViewById(R.id.dialogContactNumber);
            Spinner relationSpinner = (Spinner) v.findViewById(R.id.relationSpinner);
            Button addButton = (Button) v.findViewById(R.id.addcontactdialog);
            Button cancelButton = (Button) v.findViewById(R.id.cancelcontactdialog);
            final CheckBox msg = (CheckBox) v.findViewById(R.id.messageconfirm);
            final EditText contactmsg = (EditText) v.findViewById(R.id.contactMessage);


            msg1 = msg.isEnabled();
            cmessage = contactmsg.getText().toString();

            Realm realm = Realm.getInstance(ContactsActivity.this);

            RealmResults<Relation> relations = realm.where(Relation.class).findAll();
            for (Relation r : relations) {
                // ... do something with the object ...
                // rm.add(new Relation(r.getName(),r.getMessage()));
                relation.add(r.getName());
            }

            realm.close();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, relation);
            relationSpinner.setAdapter(dataAdapter);
            contactNameDialog.setText(name);
            contactNumberDialog.setText(phoneNo);

            relationSpinner.setOnItemSelectedListener(this);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Realm realm = Realm.getInstance(ContactsActivity.this);
                    realm.beginTransaction();
                    Contacts con = realm.createObject(Contacts.class);
                    con.setName(name);
                    con.setNumber(phoneNo.replace("+91", "").replaceAll("\\s+", ""));
                    con.setMessage(msg.isChecked());
                    con.setCmessage(contactmsg.getText().toString().trim());
                    realm.commitTransaction();


                    Relation relations = realm.where(Relation.class).equalTo("name", relationPosition).findFirst();

                    realm.beginTransaction();
                    relations.getContacts().add(con);
                    realm.commitTransaction();
                    realm.close();
                    dialog.dismiss();

                    new ContactLoader().execute();
                }
            });

            Log.i("id = ", id + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        relationPosition = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class ContactLoader extends AsyncTask<Void, Void, List<ContactModel>> {

        @Override
        protected List<ContactModel> doInBackground(Void... voids) {
            Realm realm = Realm.getInstance(ContactsActivity.this);

            List<ContactModel> rm = new ArrayList<>();
            RealmResults<Contacts> relations = realm.where(Contacts.class).findAll();
            for (Contacts r : relations) {
                // ... do something with the object ...
                Log.i("Message and Status",r.getCmessage() + "--"+r.isMessage());
                rm.add(new ContactModel(r.getName(), r.getNumber()));
            }

            realm.close();

            return rm;
        }

        @Override
        protected void onPostExecute(final List<ContactModel> relationModels) {

            contactlist.setAdapter(new EasyRecyclerAdapter<ContactModel>(
                    getApplicationContext(),
                    ContactAdapter.class,
                    relationModels));

            contactswipe.setRefreshing(false);
            ItemClickSupport.addTo(contactlist).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

                @Override
                public void onItemClicked(RecyclerView recyclerView, final int position, View v) {

                    final MaterialDialog mat = new MaterialDialog.Builder(ContactsActivity.this)
                            .title("Please select the action")
                            .items(R.array.contactarray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    Log.i("click", text.toString());

                                    if (text.toString().equalsIgnoreCase("delete")) {
                                        ContactModel m = relationModels.get(position);

                                        Realm realm = Realm.getInstance(ContactsActivity.this);
                                        RealmResults<Relation> r = realm.where(Relation.class).findAll();
                                        for (int i = 0; i < r.size(); i++) {
                                            for (int j = 0; j < r.get(i).getContacts().size(); j++) {
                                                Log.i("click1", r.get(i).getContacts().get(j).getNumber());
                                                Log.i("click2", m.getRelation());

                                                if (r.get(i).getContacts().get(j).getNumber().equalsIgnoreCase(m.getRelation())) {

                                                    Log.i("click1", r.get(i).getContacts().get(j).getNumber());
                                                    Log.i("click2", m.getRelation());
                                                    updateThis(r.get(i), r.get(i).getContacts().get(j));

                                                    dialog.dismiss();

                                                }
                                            }
                                        }

                                        realm.close();
                                        new ContactLoader().execute();
                                    }
                                }
                            }).show();


                }
            });
            super.onPostExecute(relationModels);
        }
    }

    private void updateThis(Relation re, Contacts c) {
        Realm realm = Realm.getInstance(ContactsActivity.this);
        realm.beginTransaction();
        re.getContacts().remove(c);
        c.removeFromRealm();
        realm.commitTransaction();
        realm.close();
    }

}
