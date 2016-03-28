package com.sangeetha.vbaas.core.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.sangeetha.vbaas.core.model.Contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Poovarasan on 3/26/2016.
 */
public class Utils {

    public static List<Contact> loadAllContacts(Context ctx) {
        Cursor phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        List<Contact> contacts = new ArrayList<>();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Long id = Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID)));

            contacts.add(new Contact(id,name,phoneNumber));
        }
        phones.close();

        return contacts;
    }
    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "d MMMM yyyy, hh:mm aa");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            //Log.i("Exception while parsing date. " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

}
