package com.sangeetha.vbaas.core.adapter;

import android.view.View;
import android.widget.TextView;

import com.sangeetha.vbaas.R;
import com.sangeetha.vbaas.core.model.RelationModel;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by Poovarasan on 3/26/2016.
 */
@LayoutId(R.layout.list_item_relation)
public class RelationAdapter extends ItemViewHolder<RelationModel> {

    @ViewId(R.id.listrelationname)
    TextView relationname;

    @ViewId(R.id.listrelationcount)
    TextView relationcount;

    public RelationAdapter(View view) {
        super(view);
    }

    public RelationAdapter(View view, RelationModel item) {
        super(view, item);
    }

    @Override
    public void onSetValues(RelationModel item, PositionInfo positionInfo) {

        relationname.setText(item.getName());
        relationcount.setText("Total contacts : "+item.getContacts());
    }
}
