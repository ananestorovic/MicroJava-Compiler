// generated with ast extension for cup
// version 0.8
// 26/0/2022 7:30:43


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatement1 extends Matched {

    private Designator Designator;
    private AssignmentStatement AssignmentStatement;

    public DesignatorStatement1 (Designator Designator, AssignmentStatement AssignmentStatement) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.AssignmentStatement=AssignmentStatement;
        if(AssignmentStatement!=null) AssignmentStatement.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public AssignmentStatement getAssignmentStatement() {
        return AssignmentStatement;
    }

    public void setAssignmentStatement(AssignmentStatement AssignmentStatement) {
        this.AssignmentStatement=AssignmentStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(AssignmentStatement!=null) AssignmentStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(AssignmentStatement!=null) AssignmentStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(AssignmentStatement!=null) AssignmentStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatement1(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignmentStatement!=null)
            buffer.append(AssignmentStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatement1]");
        return buffer.toString();
    }
}
