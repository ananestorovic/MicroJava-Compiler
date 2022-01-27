// generated with ast extension for cup
// version 0.8
// 27/0/2022 4:45:39


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignator extends Factor {

    private DesignatorFunctionCall DesignatorFunctionCall;

    public FactorDesignator (DesignatorFunctionCall DesignatorFunctionCall) {
        this.DesignatorFunctionCall=DesignatorFunctionCall;
        if(DesignatorFunctionCall!=null) DesignatorFunctionCall.setParent(this);
    }

    public DesignatorFunctionCall getDesignatorFunctionCall() {
        return DesignatorFunctionCall;
    }

    public void setDesignatorFunctionCall(DesignatorFunctionCall DesignatorFunctionCall) {
        this.DesignatorFunctionCall=DesignatorFunctionCall;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorFunctionCall!=null) DesignatorFunctionCall.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorFunctionCall!=null) DesignatorFunctionCall.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorFunctionCall!=null) DesignatorFunctionCall.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignator(\n");

        if(DesignatorFunctionCall!=null)
            buffer.append(DesignatorFunctionCall.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignator]");
        return buffer.toString();
    }
}
