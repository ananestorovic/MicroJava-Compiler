// generated with ast extension for cup
// version 0.8
// 24/0/2022 5:38:1


package rs.ac.bg.etf.pp1.ast;

public class OnlyVariableDecl extends Variable {

    private String varName;

    public OnlyVariableDecl (String varName) {
        this.varName=varName;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OnlyVariableDecl(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OnlyVariableDecl]");
        return buffer.toString();
    }
}
