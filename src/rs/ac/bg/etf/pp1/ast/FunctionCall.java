// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class FunctionCall extends DesignatorStatement {

    private DesignatorFunctionCall DesignatorFunctionCall;

    public FunctionCall (DesignatorFunctionCall DesignatorFunctionCall) {
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
        buffer.append("FunctionCall(\n");

        if(DesignatorFunctionCall!=null)
            buffer.append(DesignatorFunctionCall.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FunctionCall]");
        return buffer.toString();
    }
}
