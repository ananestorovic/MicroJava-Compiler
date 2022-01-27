// generated with ast extension for cup
// version 0.8
// 27/0/2022 4:45:39


package rs.ac.bg.etf.pp1.ast;

public class DesignatorElementWithDot extends DesignatorElement {

    private String elementWithDotName;

    public DesignatorElementWithDot (String elementWithDotName) {
        this.elementWithDotName=elementWithDotName;
    }

    public String getElementWithDotName() {
        return elementWithDotName;
    }

    public void setElementWithDotName(String elementWithDotName) {
        this.elementWithDotName=elementWithDotName;
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
        buffer.append("DesignatorElementWithDot(\n");

        buffer.append(" "+tab+elementWithDotName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorElementWithDot]");
        return buffer.toString();
    }
}
