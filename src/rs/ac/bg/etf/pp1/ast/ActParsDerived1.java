// generated with ast extension for cup
// version 0.8
// 25/0/2022 7:22:29


package rs.ac.bg.etf.pp1.ast;

public class ActParsDerived1 extends ActPars {

    public ActParsDerived1 () {
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
        buffer.append("ActParsDerived1(\n");

        buffer.append(tab);
        buffer.append(") [ActParsDerived1]");
        return buffer.toString();
    }
}
