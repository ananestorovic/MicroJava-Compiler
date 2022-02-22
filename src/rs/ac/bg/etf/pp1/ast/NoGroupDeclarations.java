// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class NoGroupDeclarations extends GroupDeclList {

    public NoGroupDeclarations () {
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
        buffer.append("NoGroupDeclarations(\n");

        buffer.append(tab);
        buffer.append(") [NoGroupDeclarations]");
        return buffer.toString();
    }
}
