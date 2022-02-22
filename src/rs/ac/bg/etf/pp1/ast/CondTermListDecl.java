// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class CondTermListDecl extends CondTermList {

    private CondTermListLeft CondTermListLeft;
    private CondTermList CondTermList;

    public CondTermListDecl (CondTermListLeft CondTermListLeft, CondTermList CondTermList) {
        this.CondTermListLeft=CondTermListLeft;
        if(CondTermListLeft!=null) CondTermListLeft.setParent(this);
        this.CondTermList=CondTermList;
        if(CondTermList!=null) CondTermList.setParent(this);
    }

    public CondTermListLeft getCondTermListLeft() {
        return CondTermListLeft;
    }

    public void setCondTermListLeft(CondTermListLeft CondTermListLeft) {
        this.CondTermListLeft=CondTermListLeft;
    }

    public CondTermList getCondTermList() {
        return CondTermList;
    }

    public void setCondTermList(CondTermList CondTermList) {
        this.CondTermList=CondTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTermListLeft!=null) CondTermListLeft.accept(visitor);
        if(CondTermList!=null) CondTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTermListLeft!=null) CondTermListLeft.traverseTopDown(visitor);
        if(CondTermList!=null) CondTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTermListLeft!=null) CondTermListLeft.traverseBottomUp(visitor);
        if(CondTermList!=null) CondTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTermListDecl(\n");

        if(CondTermListLeft!=null)
            buffer.append(CondTermListLeft.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTermList!=null)
            buffer.append(CondTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTermListDecl]");
        return buffer.toString();
    }
}
