package com.changcai.buyer.im.rts.doodle;

import java.util.List;

/**
 * Created by huangjun on 2015/6/24.
 */
public interface TransactionObserver {
    void onTransaction(List<Transaction> transactions);
}
