package com.sangeetha.vbaas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sangeetha.vbaas.core.adapter.RecentBlockedAdapter;
import com.sangeetha.vbaas.core.model.RecentBlockModel;
import com.sangeetha.vbaas.model.RecentBlocked;
import com.sangeetha.vbaas.receiver.MyCallReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import carbon.beta.AppBarLayout;
import carbon.widget.Button;
import carbon.widget.CardView;
import carbon.widget.LinearLayout;
import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class Home extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.contactactivity)
    Button contactactivity;
    @Bind(R.id.btncard)
    CardView btncard;
    @Bind(R.id.servicearea)
    LinearLayout servicearea;
    @Bind(R.id.relationactivity)
    Button relationactivity;

    int status = 0;
    @Bind(R.id.server)
    FloatingActionButton server;

    NotificationManager mNotificationManager;
    @Bind(R.id.recentlyblocked)
    RecyclerView recentlyblocked;
    @Bind(R.id.statustxt)
    TextView statustxt;
    @Bind(R.id.settingactivity)
    Button settingactivity;
    @Bind(R.id.blockedswipe)
    SwipeRefreshLayout blockedswipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ome);
        ButterKnife.bind(this);


        mNotificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        setSupportActionBar(toolbar);

        stopService();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recentlyblocked.setLayoutManager(llm);


//
//        Realm realm = Realm.getInstance(getApplicationContext());
//        realm.beginTransaction();
//        RecentBlocked blocked = realm.createObject(RecentBlocked.class);
//        blocked.setNumber("123456789");
//        blocked.setName("Hello");
//        blocked.setDate(new Date());
//        realm.commitTransaction();
//        realm.close();

        new RecentBlockLoader().execute();

        blockedswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RecentBlockLoader().execute();
            }
        });

    }

    @OnClick(R.id.contactactivity)
    public void contactActivityClicked(View v) {
        Intent i = new Intent(Home.this, ContactsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.settingactivity)
    public void settingClicked(View v) {
        Intent i = new Intent(Home.this, Settings.class);
        startActivity(i);
    }

    @OnClick(R.id.relationactivity)
    public void relationActivityClicked(View v) {
        Intent i = new Intent(Home.this, RelationActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.server)
    public void serverStatus(View v) {
        if (status == 0) {
            startService();
            makeNotification(getApplicationContext());
            statustxt.setText("Server Status : Started");
            server.setImageResource(R.drawable.ic_stop_white_48dp);
            status = 1;
        } else {
            stopService();
            cancelNotification();
            statustxt.setText("Server Status : Stopped");
            server.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            status = 0;
        }
    }


    public void startService() {

        ComponentName receiver = new ComponentName(this, MyCallReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
    }

    public void stopService() {

        ComponentName receiver = new ComponentName(this, MyCallReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
    }

    private void makeNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Notification Title")
                .setContentText("Sample Notification Content")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_done_all_white_48dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_all_white_48dp));
        Notification n;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(1, n);
    }

    private void cancelNotification() {
        mNotificationManager.cancel(1);
    }


    class RecentBlockLoader extends AsyncTask<Void, Void, List<RecentBlockModel>> {

        @Override
        protected List<RecentBlockModel> doInBackground(Void... voids) {

            List<RecentBlockModel> recentBlockedModel = new ArrayList<>();
            Realm realm = Realm.getInstance(Home.this);
            RealmResults<RecentBlocked> recentBlocked = realm.where(RecentBlocked.class).findAll();
            for (RecentBlocked r : recentBlocked) {
                recentBlockedModel.add(new RecentBlockModel(r.getName(), r.getNumber(), r.getDate()));
            }


            return recentBlockedModel;
        }

        @Override
        protected void onPostExecute(List<RecentBlockModel> recentBlockModels) {

            recentlyblocked.setAdapter(new EasyRecyclerAdapter<RecentBlockModel>(
                    getApplicationContext(),
                    RecentBlockedAdapter.class,
                    recentBlockModels));
            blockedswipe.setRefreshing(false);
            super.onPostExecute(recentBlockModels);
        }
    }

}
