package org.halykcoin.wallet.entity;

import java.util.List;

/**
 * Created by Maestro on 14.01.2018.
 */

public class HalykTransactionsInOut {
    private List<HalykTransaction> in;
    private List<HalykTransaction> out;
    private List<HalykTransaction> pending;

    public HalykTransactionsInOut(List<HalykTransaction> in, List<HalykTransaction> out, List<HalykTransaction> pending) {
        this.in = in;
        this.out = out;
        this.pending = pending;
    }

    public List<HalykTransaction> getIn() {
        return in;
    }

    public void setIn(List<HalykTransaction> in) {
        this.in = in;
    }

    public List<HalykTransaction> getOut() {
        return out;
    }

    public void setOut(List<HalykTransaction> out) {
        this.out = out;
    }

    public List<HalykTransaction> getPending() {
        return pending;
    }

    public void setPending(List<HalykTransaction> pending) {
        this.pending = pending;
    }
}
