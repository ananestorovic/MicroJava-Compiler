// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class ConditionListDecl extends ConditionList {

    private ConditionListLeft ConditionListLeft;
    private ConditionList ConditionList;

    public ConditionListDecl (ConditionListLeft ConditionListLeft, ConditionList ConditionList) {
        this.ConditionListLeft=ConditionListLeft;
        if(ConditionListLeft!=null) ConditionListLeft.setParent(this);
        this.ConditionList=ConditionList;
        if(ConditionList!=null) ConditionList.setParent(this);
    }

    public ConditionListLeft getConditionListLeft() {
        return ConditionListLeft;
    }

    public void setConditionListLeft(ConditionListLeft ConditionListLeft) {
        this.ConditionListLeft=ConditionListLeft;
    }

    public ConditionList getConditionList() {
        return ConditionList;
    }

    public void setConditionList(ConditionList ConditionList) {
        this.ConditionList=ConditionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionListLeft!=null) ConditionListLeft.accept(visitor);
        if(ConditionList!=null) ConditionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionListLeft!=null) ConditionListLeft.traverseTopDown(visitor);
        if(ConditionList!=null) ConditionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionListLeft!=null) ConditionListLeft.traverseBottomUp(visitor);
        if(ConditionList!=null) ConditionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionListDecl(\n");

        if(ConditionListLeft!=null)
            buffer.append(ConditionListLeft.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionList!=null)
            buffer.append(ConditionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionListDecl]");
        return buffer.toString();
    }
}
