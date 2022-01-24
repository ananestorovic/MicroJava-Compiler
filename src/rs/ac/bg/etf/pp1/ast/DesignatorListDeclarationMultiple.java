// generated with ast extension for cup
// version 0.8
// 24/0/2022 5:38:1


package rs.ac.bg.etf.pp1.ast;

public class DesignatorListDeclarationMultiple extends DesignatorList {

    private DesignatorElement DesignatorElement;
    private DesignatorList DesignatorList;

    public DesignatorListDeclarationMultiple (DesignatorElement DesignatorElement, DesignatorList DesignatorList) {
        this.DesignatorElement=DesignatorElement;
        if(DesignatorElement!=null) DesignatorElement.setParent(this);
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
    }

    public DesignatorElement getDesignatorElement() {
        return DesignatorElement;
    }

    public void setDesignatorElement(DesignatorElement DesignatorElement) {
        this.DesignatorElement=DesignatorElement;
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorElement!=null) DesignatorElement.accept(visitor);
        if(DesignatorList!=null) DesignatorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorElement!=null) DesignatorElement.traverseTopDown(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorElement!=null) DesignatorElement.traverseBottomUp(visitor);
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorListDeclarationMultiple(\n");

        if(DesignatorElement!=null)
            buffer.append(DesignatorElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorListDeclarationMultiple]");
        return buffer.toString();
    }
}
