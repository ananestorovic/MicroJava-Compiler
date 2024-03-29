// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class DoStatement extends Matched {

    private DoStatementStart DoStatementStart;
    private Statement Statement;
    private WhileStart WhileStart;
    private Condition Condition;

    public DoStatement (DoStatementStart DoStatementStart, Statement Statement, WhileStart WhileStart, Condition Condition) {
        this.DoStatementStart=DoStatementStart;
        if(DoStatementStart!=null) DoStatementStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileStart=WhileStart;
        if(WhileStart!=null) WhileStart.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public DoStatementStart getDoStatementStart() {
        return DoStatementStart;
    }

    public void setDoStatementStart(DoStatementStart DoStatementStart) {
        this.DoStatementStart=DoStatementStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileStart getWhileStart() {
        return WhileStart;
    }

    public void setWhileStart(WhileStart WhileStart) {
        this.WhileStart=WhileStart;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoStatementStart!=null) DoStatementStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileStart!=null) WhileStart.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStatementStart!=null) DoStatementStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileStart!=null) WhileStart.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStatementStart!=null) DoStatementStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileStart!=null) WhileStart.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoStatement(\n");

        if(DoStatementStart!=null)
            buffer.append(DoStatementStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileStart!=null)
            buffer.append(WhileStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoStatement]");
        return buffer.toString();
    }
}
