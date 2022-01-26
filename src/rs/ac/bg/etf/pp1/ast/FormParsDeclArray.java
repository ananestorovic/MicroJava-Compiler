// generated with ast extension for cup
// version 0.8
// 26/0/2022 7:30:43


package rs.ac.bg.etf.pp1.ast;

public class FormParsDeclArray extends FormPars {

    private Type Type;
    private String nameForm;

    public FormParsDeclArray (Type Type, String nameForm) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.nameForm=nameForm;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getNameForm() {
        return nameForm;
    }

    public void setNameForm(String nameForm) {
        this.nameForm=nameForm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDeclArray(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+nameForm);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDeclArray]");
        return buffer.toString();
    }
}
