package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
public class AndExp extends Exp{
    private int filter_size=3;
    private Exp term;

    public int getFilter_size() {
        return filter_size;
    }

    private Exp[] exp=new Exp[filter_size];

    public AndExp(Exp term, Exp[] exp) {
        //term
        this.term = term;
        this.exp = condition(term,exp);
    }

    private Exp[] condition(Exp newcon,Exp[] origin){

        if(newcon instanceof CodeExp){
            origin[0]=newcon;
        }else if ((newcon instanceof SubExp)){
            origin[1]=newcon;
        }else if(newcon instanceof NameExp){
            origin[2]=newcon;
        }
        return origin;

    }
    @Override
    public String show() {
        return exp.toString();
    }

}
