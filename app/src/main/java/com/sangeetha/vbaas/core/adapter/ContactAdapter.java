package com.sangeetha.vbaas.core.adapter;

import android.view.View;
import android.widget.TextView;

import com.sangeetha.vbaas.R;
import com.sangeetha.vbaas.core.model.ContactModel;

import carbon.widget.CardView;
import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by Poovarasan on 3/27/2016.
 */

@LayoutId(R.layout.list_item_contact)
public class ContactAdapter extends ItemViewHolder<ContactModel>{


    @ViewId(R.id.listcontactname)
    TextView listcontactname;

    @ViewId(R.id.listcontactrelation)
    TextView listcontactrelation;


    @ViewId(R.id.singlecontact)
    CardView singlecontact;

    ContactModel model;
    public ContactAdapter(View view) {
        super(view);
    }

    public ContactAdapter(View view, ContactModel item) {
        super(view, item);
    }

    @Override
    public void onSetValues(ContactModel item, PositionInfo positionInfo) {

        listcontactname.setText(item.getName());
        listcontactrelation.setText(item.getRelation());
    }


    @Override
    public void onSetListeners() {

        singlecontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        super.onSetListeners();
    }
}
