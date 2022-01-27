// generated with ast extension for cup
// version 0.8
// 27/0/2022 19:47:40


package rs.ac.bg.etf.pp1.ast;

public class NoStatement extends MethodDeclList {

    public NoStatement () {
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
        buffer.append("NoStatement(\n");

        buffer.append(tab);
        buffer.append(") [NoStatement]");
        return buffer.toString();
    }
}
