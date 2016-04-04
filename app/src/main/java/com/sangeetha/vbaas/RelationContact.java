package com.sangeetha.vbaas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sangeetha.vbaas.core.adapter.ContactAdapter;
import com.sangeetha.vbaas.core.model.ContactModel;
import com.sangeetha.vbaas.model.Contacts;
import com.sangeetha.vbaas.model.Relation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import carbon.beta.AppBarLayout;
import io.realm.Realm;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class RelationContact extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.relationContacts)
    RecyclerView relationContacts;
    String relationName;
    @Bind(R.id.notfound)
    TextView notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation_contact);
        ButterKnife.bind(this);

        relationName = getIntent().getStringExtra("relationname");

        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        relationContacts.setLayoutManager(llm);


        new ContactsLoaderAsync().execute(relationName);

    }


    class ContactsLoaderAsync extends AsyncTask<String, Void, List<ContactModel>> {


        @Override
        protected List<ContactModel> doInBackground(String... strings) {

            List<ContactModel> cModel = new ArrayList<>();
            Realm realm = Realm.getInstance(getApplicationContext());
            Relation relations = realm
                    .where(Relation.class)
                    .equalTo("name", strings[0])
                    .findFirst();

            for (Contacts c : relations.getContacts()) {
                cModel.add(new ContactModel(c.getName(), c.getNumber()));
            }

            realm.close();

            return cModel;
        }

        @Override
        protected void onPostExecute(List<ContactModel> contactModels) {

            if (contactModels.size() > 0) {
                relationContacts.setAdapter(new EasyRecyclerAdapter<ContactModel>(
                        getApplicationContext(),
                        ContactAdapter.class,
                        contactModels));
            } else {
                notfound.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(contactModels);
        }
    }

}
