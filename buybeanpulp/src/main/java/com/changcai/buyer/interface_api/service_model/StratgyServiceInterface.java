package com.changcai.buyer.interface_api.service_model;

import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.bean.StrategyTargetDataModel;
import com.changcai.buyer.bean.StrategyTargetModel;
import com.changcai.buyer.interface_api.service_model.base.ServiceRequestCallback;

/**
 * Created by lufeisong on 2018/3/19.
 */

public interface StratgyServiceInterface {
    void strategyInit(ServiceRequestCallback<StrategyInitModel> callback);

    void getStrategyTarget(String data,String folderId, ServiceRequestCallback<StrategyTargetModel> callbak);

    void getStrategyTargetDatas(String targetId, String folderId, String code, String dataUrlParam, String data, ServiceRequestCallback<StrategyTargetDataModel> callback);

    void getStrategyPaper(String folderId, String data, ServiceRequestCallback<StrategyPaperModel> callback);
}
