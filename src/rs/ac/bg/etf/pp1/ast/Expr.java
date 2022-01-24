// generated with ast extension for cup
// version 0.8
// 24/0/2022 5:38:1


package rs.ac.bg.etf.pp1.ast;

public class Expr implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private OptionalMinus OptionalMinus;
    private AddopList AddopList;

    public Expr (OptionalMinus OptionalMinus, AddopList AddopList) {
        this.OptionalMinus=OptionalMinus;
        if(OptionalMinus!=null) OptionalMinus.setParent(this);
        this.AddopList=AddopList;
        if(AddopList!=null) AddopList.setParent(this);
    }

    public OptionalMinus getOptionalMinus() {
        return OptionalMinus;
    }

    public void setOptionalMinus(OptionalMinus OptionalMinus) {
        this.OptionalMinus=OptionalMinus;
    }

    public AddopList getAddopList() {
        return AddopList;
    }

    public void setAddopList(AddopList AddopList) {
        this.AddopList=AddopList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalMinus!=null) OptionalMinus.accept(visitor);
        if(AddopList!=null) AddopList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalMinus!=null) OptionalMinus.traverseTopDown(visitor);
        if(AddopList!=null) AddopList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalMinus!=null) OptionalMinus.traverseBottomUp(visitor);
        if(AddopList!=null) AddopList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr(\n");

        if(OptionalMinus!=null)
            buffer.append(OptionalMinus.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddopList!=null)
            buffer.append(AddopList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr]");
        return buffer.toString();
    }
}
