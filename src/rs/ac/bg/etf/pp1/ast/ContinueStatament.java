// generated with ast extension for cup
// version 0.8
// 25/0/2022 7:22:29


package rs.ac.bg.etf.pp1.ast;

public class ContinueStatament extends Matched {

    public ContinueStatament () {
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
        buffer.append("ContinueStatament(\n");

        buffer.append(tab);
        buffer.append(") [ContinueStatament]");
        return buffer.toString();
    }
}
