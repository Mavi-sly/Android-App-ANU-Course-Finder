package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
public class CodeExp extends Exp{
    private String  codesub;
    private String codenum;

    public CodeExp(String codesub, String codenum) {
        this.codesub = codesub;
        this.codenum = codenum;
    }

    public String getCodenum() {
        return codenum;
    }
    public String getCodesub(){
        return codesub;
    }

    @Override
    public String show() {
        return codesub + codenum;
    }

    public String getCode(){return show();}
}
