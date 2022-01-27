// generated with ast extension for cup
// version 0.8
// 27/0/2022 4:45:39


package rs.ac.bg.etf.pp1.ast;

public class NewFactor extends Factor {

    private Type Type;
    private NewFactorExprOptional NewFactorExprOptional;

    public NewFactor (Type Type, NewFactorExprOptional NewFactorExprOptional) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.NewFactorExprOptional=NewFactorExprOptional;
        if(NewFactorExprOptional!=null) NewFactorExprOptional.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public NewFactorExprOptional getNewFactorExprOptional() {
        return NewFactorExprOptional;
    }

    public void setNewFactorExprOptional(NewFactorExprOptional NewFactorExprOptional) {
        this.NewFactorExprOptional=NewFactorExprOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(NewFactorExprOptional!=null) NewFactorExprOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(NewFactorExprOptional!=null) NewFactorExprOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(NewFactorExprOptional!=null) NewFactorExprOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewFactor(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(NewFactorExprOptional!=null)
            buffer.append(NewFactorExprOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewFactor]");
        return buffer.toString();
    }
}
