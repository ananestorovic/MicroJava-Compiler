// generated with ast extension for cup
// version 0.8
// 26/0/2022 7:30:43


package rs.ac.bg.etf.pp1.ast;

public class NoStatementListDeclarations extends StatementList {

    public NoStatementListDeclarations () {
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
        buffer.append("NoStatementListDeclarations(\n");

        buffer.append(tab);
        buffer.append(") [NoStatementListDeclarations]");
        return buffer.toString();
    }
}
