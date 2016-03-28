package com.sangeetha.vbaas.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.sangeetha.vbaas.core.model.Contact;
import com.sangeetha.vbaas.core.util.Utils;
import com.sangeetha.vbaas.model.RecentBlocked;
import com.sangeetha.vbaas.model.Relation;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyCallReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // This code will execute when the phone has an incoming call

            // get the phone number 
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            incomingNumber = incomingNumber.replace("+91", "");
            String strGname = "";
            String strMessage = "";
//            DatabaseHandler db = new DatabaseHandler(context);
//            Cursor cursor = db.getGroupname(incomingNumber);
//            if (cursor.moveToFirst()) {
//                do {
//                    strGname = cursor.getString(2);
//
//                } while (cursor.moveToNext());
//            }
//            if (strGname.length() > 0) {
//                Cursor cursor1 = db.getMessage(strGname);
//                if (cursor1.moveToFirst()) {
//                    do {
//                        strMessage = cursor1.getString(1);
//
//                    } while (cursor1.moveToNext());
//                }
//
//                if (strMessage.length() > 0) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage("+91" + incomingNumber, null, strMessage, null, null);
//                }
//
//
//                killCall(context);
//            }

            Realm realm = Realm.getInstance(context);
            RealmResults<Relation> r = realm.where(Relation.class).findAll();

            for (int i = 0; i < r.size(); i++) {
                for (int j = 0; j < r.get(i).getContacts().size(); j++) {

                    if (r.get(i).getContacts().get(j).getNumber().equalsIgnoreCase(incomingNumber)) {

                        if (r.get(i).getContacts().get(j).isMessage()) {
                            String message = "";

                            if (r.get(i).getContacts().get(j).getCmessage() != "") {
                                message += r.get(i).getMessage().replace("#name", r.get(i).getContacts().get(j).getName());
                            } else {
                                message += r.get(i).getContacts().get(j).getCmessage();
                            }


                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("+91" + incomingNumber, null, message, null, null);

                        }
                        realm.beginTransaction();
                        RecentBlocked blocked = realm.createObject(RecentBlocked.class);
                        blocked.setNumber(incomingNumber);
                        blocked.setName(r.get(i).getContacts().get(j).getName() == "" ? "Unknown" : r.get(i).getContacts().get(j).getName());
                        blocked.setDate(new Date());
                        realm.commitTransaction();

                        killCall(context);
                    }
                }
            }

            if(context.getApplicationContext().getSharedPreferences("VBAAS",Context.MODE_PRIVATE).getBoolean("unknown",false)) {
                List<Contact> con = Utils.loadAllContacts(context);
                for (int i = 0; i < con.size(); i++) {
                    if (con.get(i).getPhno().contains(incomingNumber)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+91" + incomingNumber, null, "Please Call me later", null, null);


                        realm.beginTransaction();
                        RecentBlocked blocked = realm.createObject(RecentBlocked.class);
                        blocked.setNumber(incomingNumber);
                        blocked.setName(con.get(i).getName());
                        blocked.setDate(new Date());
                        realm.commitTransaction();

                        killCall(context);

                        break;
                    }
                }
            }


            realm.close();

            Toast.makeText(context, "Call from:" + incomingNumber, Toast.LENGTH_LONG).show();
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // This code will execute when the call is disconnected
            Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();

        }
    }

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls

            return false;
        }
        return true;
    }


}