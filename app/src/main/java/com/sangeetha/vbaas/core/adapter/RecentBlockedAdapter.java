package com.sangeetha.vbaas.core.adapter;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.sangeetha.vbaas.R;
import com.sangeetha.vbaas.core.model.RecentBlockModel;
import com.sangeetha.vbaas.core.util.Utils;

import java.util.Date;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by Poovarasan on 3/28/2016.
 */

@LayoutId(R.layout.list_item_recent_blocked)
public class RecentBlockedAdapter extends ItemViewHolder<RecentBlockModel> {


    @ViewId(R.id.blockedname)
    TextView blockedName;

    @ViewId(R.id.blockednumber)
    TextView blockedNumber;

    @ViewId(R.id.blockeddate)
    TextView blockedDate;

    public RecentBlockedAdapter(View view) {
        super(view);
    }

    public RecentBlockedAdapter(View view, RecentBlockModel item) {
        super(view, item);
    }

    @Override
    public void onSetValues(RecentBlockModel item, PositionInfo positionInfo) {

        blockedName.setText(item.getName());
        blockedNumber.setText(item.getNumber());
        String date = DateUtils.getRelativeTimeSpanString(Utils.getDateInMillis(item.getDate().toString()), Utils.getDateInMillis(new Date().toString()), DateUtils.MINUTE_IN_MILLIS).toString();
       blockedDate.setText(date);
    }
}
