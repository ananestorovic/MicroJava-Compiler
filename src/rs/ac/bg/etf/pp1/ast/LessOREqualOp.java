// generated with ast extension for cup
// version 0.8
// 24/0/2022 5:38:1


package rs.ac.bg.etf.pp1.ast;

public class LessOREqualOp extends Relop {

    public LessOREqualOp () {
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
        buffer.append("LessOREqualOp(\n");

        buffer.append(tab);
        buffer.append(") [LessOREqualOp]");
        return buffer.toString();
    }
}
