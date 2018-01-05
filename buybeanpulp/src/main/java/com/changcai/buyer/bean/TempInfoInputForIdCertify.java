package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxingwei on 2016/12/1.
 */

public class TempInfoInputForIdCertify implements Serializable, IKeepFromProguard {

    private String inputTempName;
    private String inputTempIdNo;
    private String companyName;
    private String code;
    private String iv1Select;

    public String getIv1Select() {
        return iv1Select;
    }

    public void setIv1Select(String iv1Select) {
        this.iv1Select = iv1Select;
    }

    private String iv2Select;




    public String getIv2Select() {
        return iv2Select;
    }

    public void setIv2Select(String iv2Select) {
        this.iv2Select = iv2Select;
    }

    private Map<String, LocalPhotoFilePathAndUploadId> idMap;


    public String getInputTempName() {
        return inputTempName;
    }

    public void setInputTempName(String inputTempName) {
        this.inputTempName = inputTempName;
    }

    public String getInputTempIdNo() {
        return inputTempIdNo;
    }

    public void setInputTempIdNo(String inputTempIdNo) {
        this.inputTempIdNo = inputTempIdNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, LocalPhotoFilePathAndUploadId> getIdMap() {
        return idMap;
    }

    public void setIdMap(Map<String, LocalPhotoFilePathAndUploadId> idMap) {
        this.idMap = idMap;
    }


}