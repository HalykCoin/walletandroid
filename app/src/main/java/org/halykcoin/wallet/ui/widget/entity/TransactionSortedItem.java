package org.halykcoin.wallet.ui.widget.entity;

import android.text.format.DateUtils;

import org.halykcoin.wallet.entity.HalykTransaction;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

public class TransactionSortedItem extends TimestampSortedItem<HalykTransaction> {

    public TransactionSortedItem(int viewType, HalykTransaction value, int order) {
        super(viewType, value, 0, order);
    }

    @Override
    public int compare(SortedItem other) {
        return super.compare(other);
//        return other.viewType == TransactionHolder.VIEW_TYPE ||
//                ? super.compare(other)
//                : weight - other.weight;
    }

    @Override
    public boolean areContentsTheSame(SortedItem newItem) {
        if (viewType == newItem.viewType) {
            HalykTransaction transaction = (HalykTransaction)newItem.value;
            boolean same = value.getTxid().equals(transaction.getTxid()) && value.getTimestamp() == transaction.getTimestamp();
            return same;
        }
        return false;
    }

    @Override
    public boolean areItemsTheSame(SortedItem other) {
        return false;
    }

    @Override
    public Date getTimestamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(value.getTimestamp() * DateUtils.SECOND_IN_MILLIS);
        return calendar.getTime();
    }
}
