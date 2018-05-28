package org.halykcoin.wallet.ui.widget.holder;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.halykcoin.wallet.R;
import org.halykcoin.wallet.entity.HalykTransaction;
import org.halykcoin.wallet.ui.widget.OnTransactionClickListener;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import timber.log.Timber;

public class TransactionHolder extends BinderViewHolder<HalykTransaction> implements View.OnClickListener {

    public static final int VIEW_TYPE = 1003;

    private static final int SIGNIFICANT_FIGURES = 3;

    public static final String DEFAULT_SYMBOL_ADDITIONAL = "network_symbol";

    private final TextView type;
    private final TextView address;
    private final TextView value;
    private final ImageView typeIcon;

    private HalykTransaction transaction;
    private String defaultAddress;
    private OnTransactionClickListener onTransactionClickListener;

    public TransactionHolder(int resId, ViewGroup parent) {
        super(resId, parent);

        typeIcon = findViewById(R.id.type_icon);
        address = findViewById(R.id.address);
        type = findViewById(R.id.type);
        value = findViewById(R.id.value);

        typeIcon.setColorFilter(
                ContextCompat.getColor(getContext(), R.color.item_icon_tint),
                PorterDuff.Mode.SRC_ATOP);

        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(@Nullable HalykTransaction data, @NonNull Bundle addition) {
        transaction = data; // reset
        if (this.transaction == null) {
            return;
        }

        String networkSymbol = addition.getString(DEFAULT_SYMBOL_ADDITIONAL);
        // If operations include token transfer, display token transfer instead

        fill(transaction.getTransactionType(), transaction.getPayment_id(), transaction.getTxid(), networkSymbol, transaction.getAmount().toString(),
                11);
    }

    private void fill(
            HalykTransaction.TransactionType transactionType,
            String from,
            String to,
            String symbol,
            String valueStr,
            long decimals) {

        String valueSign = "";
        switch (transactionType) {
            case OUT:
                type.setText(getString(R.string.sent));
                typeIcon.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                value.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                valueSign = "-";
                break;
            case IN:
                type.setText(getString(R.string.received));
                typeIcon.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                value.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                valueSign = "+";
                break;
            case PENDING:
                type.setText(getString(R.string.pending));
                typeIcon.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                value.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
                break;
        }
        address.setText(to);

        if (valueStr.equals("0")) {
            valueStr = "0 " + symbol;
        } else {
            valueStr = valueSign + getScaledValue(valueStr, decimals) + " " + symbol;
        }

        this.value.setText(valueStr);
    }

    private String getScaledValue(String valueStr, long decimals) {
        // Perform decimal conversion
        BigDecimal value = new BigDecimal(valueStr);
        value = value.divide(new BigDecimal(Math.pow(10, decimals)), MathContext.DECIMAL32);
        int scale = SIGNIFICANT_FIGURES - value.precision() + value.scale();
        return value.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    @Override
    public void onClick(View view) {
//        if (onTransactionClickListener != null) {
//            onTransactionClickListener.onTransactionClick(view, transaction);
//        }
    }

    public void setOnTransactionClickListener(OnTransactionClickListener onTransactionClickListener) {
        this.onTransactionClickListener = onTransactionClickListener;
    }
}
