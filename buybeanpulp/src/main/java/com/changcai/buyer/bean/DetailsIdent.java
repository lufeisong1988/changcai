package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2016/12/1.
 */

public class DetailsIdent implements Serializable ,IKeepFromProguard {


    /**
     * file1 : http://files2.maidoupo.com/ccfile//jpg/20161013_1476340648563.JPG
     * file2 : http://files2.maidoupo.com/ccfile//jpg/20161013_1476340648563.JPG
     * file3 : http://files2.maidoupo.com/ccfile//jpg/20161013_1476340648563.JPG
     * file4 : http://files2.maidoupo.com/ccfile//jpg/20160928_1475033490280.JPG
     * file5 : http://files2.maidoupo.com/ccfile//jpg/20160928_1475033492830.JPG
     * file6 : http://files2.maidoupo.com/ccfile//jpg/20160928_1475033495826.JPG
     * zzjgdm : 814187118
     * uniformSocialCreditCode :
     */

    private String file1;
    private String file2;
    private String file3;
    private String file4;
    private String file5;
    private String file6;
    private String zzjgdm;
    private String uniformSocialCreditCode;
    private String userName;
    private String identityCardNo;
    private String  enterName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(String identityCardNo) {
        this.identityCardNo = identityCardNo;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public String getFile3() {
        return file3;
    }

    public void setFile3(String file3) {
        this.file3 = file3;
    }

    public String getFile4() {
        return file4;
    }

    public void setFile4(String file4) {
        this.file4 = file4;
    }

    public String getFile5() {
        return file5;
    }

    public void setFile5(String file5) {
        this.file5 = file5;
    }

    public String getFile6() {
        return file6;
    }

    public void setFile6(String file6) {
        this.file6 = file6;
    }

    public String getZzjgdm() {
        return zzjgdm;
    }

    public void setZzjgdm(String zzjgdm) {
        this.zzjgdm = zzjgdm;
    }

    public String getUniformSocialCreditCode() {
        return uniformSocialCreditCode;
    }

    public void setUniformSocialCreditCode(String uniformSocialCreditCode) {
        this.uniformSocialCreditCode = uniformSocialCreditCode;
    }
}
