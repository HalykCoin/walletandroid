package org.halykcoin.wallet.repository;

/**
 * Created by Maestro on 07.01.2018.
 */

public class HalykGeneralRequestBody {
    private String id;

    public HalykGeneralRequestBody(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
