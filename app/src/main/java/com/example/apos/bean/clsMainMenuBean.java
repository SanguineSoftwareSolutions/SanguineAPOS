package com.example.apos.bean;

/**
 * Created by admin on 2015-07-04.
 */
public class clsMainMenuBean {

    public String FormName_strFormName;
    public String ModuleName_strModuleName;
    public String ModuleType_strModuleType;
    public String ImageName_strImageName;
    public String Sequence_intSequence;
    public String ColorImageName_strColorImageName;
    public String strDayStartYN;

    public String getStrDayStartYN() {
        return strDayStartYN;
    }

    public void setStrDayStartYN(String strDayStartYN) {
        this.strDayStartYN = strDayStartYN;
    }

    public String getFormName_strFormName() {
        return FormName_strFormName;
    }

    public void setFormName_strFormName(String formName_strFormName) {
        FormName_strFormName = formName_strFormName;
    }

    public String getModuleName_strModuleName() {
        return ModuleName_strModuleName;
    }

    public void setModuleName_strModuleName(String moduleName_strModuleName) {
        ModuleName_strModuleName = moduleName_strModuleName;
    }

    public String getModuleType_strModuleType() {
        return ModuleType_strModuleType;
    }

    public void setModuleType_strModuleType(String moduleType_strModuleType) {
        ModuleType_strModuleType = moduleType_strModuleType;
    }

    public String getImageName_strImageName() {
        return ImageName_strImageName;
    }

    public void setImageName_strImageName(String imageName_strImageName) {
        ImageName_strImageName = imageName_strImageName.toLowerCase();
    }

    public String getColorImageName_strColorImageName() {
        return ColorImageName_strColorImageName;
    }

    public void setColorImageName_strColorImageName(String colorImageName_strColorImageName) {
        ColorImageName_strColorImageName = colorImageName_strColorImageName;
    }

    public String getSequence_intSequence() {
        return Sequence_intSequence;
    }

    public void setSequence_intSequence(String sequence_intSequence) {
        Sequence_intSequence = sequence_intSequence;
    }
}
