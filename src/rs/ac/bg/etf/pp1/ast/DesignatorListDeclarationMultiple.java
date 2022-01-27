// generated with ast extension for cup
// version 0.8
// 27/0/2022 19:47:41


package rs.ac.bg.etf.pp1.ast;

public class DesignatorListDeclarationMultiple extends DesignatorList {

    private DesignatorList DesignatorList;
    private DesignatorElement DesignatorElement;

    public DesignatorListDeclarationMultiple (DesignatorList DesignatorList, DesignatorElement DesignatorElement) {
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
        this.DesignatorElement=DesignatorElement;
        if(DesignatorElement!=null) DesignatorElement.setParent(this);
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
    }

    public DesignatorElement getDesignatorElement() {
        return DesignatorElement;
    }

    public void setDesignatorElement(DesignatorElement DesignatorElement) {
        this.DesignatorElement=DesignatorElement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.accept(visitor);
        if(DesignatorElement!=null) DesignatorElement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
        if(DesignatorElement!=null) DesignatorElement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        if(DesignatorElement!=null) DesignatorElement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorListDeclarationMultiple(\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorElement!=null)
            buffer.append(DesignatorElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorListDeclarationMultiple]");
        return buffer.toString();
    }
}
