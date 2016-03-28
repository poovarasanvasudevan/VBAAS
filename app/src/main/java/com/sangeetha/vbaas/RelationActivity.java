package com.sangeetha.vbaas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sangeetha.vbaas.core.adapter.RelationAdapter;
import com.sangeetha.vbaas.core.model.RelationModel;
import com.sangeetha.vbaas.model.Relation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import carbon.beta.AppBarLayout;
import carbon.widget.Button;
import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class RelationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.relationList)
    RecyclerView relationList;
    @Bind(R.id.relationswipe)
    SwipeRefreshLayout relationswipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        relationList.setLayoutManager(llm);

        new RelationLoader().execute();

        relationswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RelationLoader().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.relation_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addrelation) {
            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title("Add Relation")
                    .customView(R.layout.add_relation_dialog, true)
                    .show();

            View v = dialog.getCustomView();

            final EditText relationName = (EditText) v.findViewById(R.id.relationName);
            final EditText relationMessage = (EditText) v.findViewById(R.id.relationMessage);
            final TextView error = (TextView) v.findViewById(R.id.errorMsg);
            Button cancel = (Button) v.findViewById(R.id.relationcancelbutton);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            Button add = (Button) v.findViewById(R.id.relationaddbutton);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String relationNameTxt = relationName.getText().toString();
                    String relationMessageTxt = relationMessage.getText().toString();

                    if (!relationNameTxt.trim().equalsIgnoreCase("") && !relationMessageTxt.trim().equalsIgnoreCase("")) {
                        Realm realm = Realm.getInstance(RelationActivity.this);
                        realm.beginTransaction();
                        Relation relation = realm.createObject(Relation.class);
                        relation.setName(relationNameTxt);
                        relation.setMessage(relationMessageTxt);
                        realm.commitTransaction();
                        dialog.dismiss();

                        new RelationLoader().execute();


                    } else {
                        error.setText("Field does not empty...");
                    }
                }
            });


        }
        return super.onOptionsItemSelected(item);
    }

    class RelationLoader extends AsyncTask<Void, Void, List<RelationModel>> {

        @Override
        protected List<RelationModel> doInBackground(Void... voids) {
            Realm realm = Realm.getInstance(RelationActivity.this);

            List<RelationModel> rm = new ArrayList<>();
            RealmResults<Relation> relations = realm.where(Relation.class).findAll();
            for (Relation r : relations) {
                // ... do something with the object ...
                rm.add(new RelationModel(r.getName(), r.getContacts().size() + ""));
            }

            realm.close();

            return rm;
        }

        @Override
        protected void onPostExecute(List<RelationModel> relationModels) {


            relationList.setAdapter(new EasyRecyclerAdapter<RelationModel>(
                    getApplicationContext(),
                    RelationAdapter.class,
                    relationModels));

            relationswipe.setRefreshing(false);

            super.onPostExecute(relationModels);
        }
    }

}
