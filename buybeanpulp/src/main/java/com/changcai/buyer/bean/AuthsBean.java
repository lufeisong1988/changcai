package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class AuthsBean implements Serializable, IKeepFromProguard {

    /**
     * menu : spot
     * authority : true
     * minGrade :
     * minGradeName :
     * minGradeDescription :
     */

    private String menu;
    private boolean authority;
    private String minGrade;
    private String minGradeName;
    private String minGradeDescription;

    public String getMinGradePic() {
        return minGradePic;
    }

    public void setMinGradePic(String minGradePic) {
        this.minGradePic = minGradePic;
    }

    private String minGradePic;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public boolean isAuthority() {
        return authority;
    }

    public void setAuthority(boolean authority) {
        this.authority = authority;
    }

    public String getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(String minGrade) {
        this.minGrade = minGrade;
    }

    public String getMinGradeName() {
        return minGradeName;
    }

    public void setMinGradeName(String minGradeName) {
        this.minGradeName = minGradeName;
    }

    public String getMinGradeDescription() {
        return minGradeDescription;
    }

    public void setMinGradeDescription(String minGradeDescription) {
        this.minGradeDescription = minGradeDescription;
    }
}
