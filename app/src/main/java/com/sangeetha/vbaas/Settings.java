package com.sangeetha.vbaas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.sangeetha.vbaas.model.RecentBlocked;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import carbon.beta.AppBarLayout;
import carbon.widget.Button;
import io.realm.Realm;
import io.realm.RealmResults;

public class Settings extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.blockunknownswitch)
    SwitchCompat blockunknownswitch;

    SharedPreferences sharedPreferences;
    @Bind(R.id.clearblockedlist)
    Button clearblockedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("VBAAS", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        blockunknownswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putBoolean("unknown", true);
                    e.commit();
                } else {

                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putBoolean("unknown", false);
                    e.commit();
                }
            }
        });
    }

    @OnClick(R.id.clearblockedlist)
    public void clearBlockedList(View v) {
        Realm realm = Realm.getInstance(Settings.this);
        realm.beginTransaction();
        RealmResults<RecentBlocked> relations = realm.where(RecentBlocked.class).findAll();

        for(int i=0;i<relations.size();i++) {
            relations.remove(i);
        }

        realm.commitTransaction();
        realm.close();

        Toast.makeText(getApplicationContext(),"Blocked list cleared",Toast.LENGTH_LONG).show();
    }
}
