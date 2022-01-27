// generated with ast extension for cup
// version 0.8
// 27/0/2022 19:47:41


package rs.ac.bg.etf.pp1.ast;

public class Assignment extends DesignatorStatement {

    private Designator Designator;
    private AssignmentStatement AssignmentStatement;

    public Assignment (Designator Designator, AssignmentStatement AssignmentStatement) {
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
        buffer.append("Assignment(\n");

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
        buffer.append(") [Assignment]");
        return buffer.toString();
    }
}
