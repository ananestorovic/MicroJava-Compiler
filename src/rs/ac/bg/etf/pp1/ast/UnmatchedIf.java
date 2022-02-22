// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class UnmatchedIf extends Unmatched {

    private IfWithCondition IfWithCondition;
    private Statement Statement;

    public UnmatchedIf (IfWithCondition IfWithCondition, Statement Statement) {
        this.IfWithCondition=IfWithCondition;
        if(IfWithCondition!=null) IfWithCondition.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public IfWithCondition getIfWithCondition() {
        return IfWithCondition;
    }

    public void setIfWithCondition(IfWithCondition IfWithCondition) {
        this.IfWithCondition=IfWithCondition;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfWithCondition!=null) IfWithCondition.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfWithCondition!=null) IfWithCondition.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfWithCondition!=null) IfWithCondition.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("UnmatchedIf(\n");

        if(IfWithCondition!=null)
            buffer.append(IfWithCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [UnmatchedIf]");
        return buffer.toString();
    }
}
