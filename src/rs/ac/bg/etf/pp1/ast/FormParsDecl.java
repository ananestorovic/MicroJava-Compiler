// generated with ast extension for cup
// version 0.8
// 25/0/2022 7:22:29


package rs.ac.bg.etf.pp1.ast;

public class FormParsDecl extends FormPars {

    private Type Type;
    private String nameForm;
    private OptionalSquareMethodFormPars OptionalSquareMethodFormPars;

    public FormParsDecl (Type Type, String nameForm, OptionalSquareMethodFormPars OptionalSquareMethodFormPars) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.nameForm=nameForm;
        this.OptionalSquareMethodFormPars=OptionalSquareMethodFormPars;
        if(OptionalSquareMethodFormPars!=null) OptionalSquareMethodFormPars.setParent(this);
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

    public OptionalSquareMethodFormPars getOptionalSquareMethodFormPars() {
        return OptionalSquareMethodFormPars;
    }

    public void setOptionalSquareMethodFormPars(OptionalSquareMethodFormPars OptionalSquareMethodFormPars) {
        this.OptionalSquareMethodFormPars=OptionalSquareMethodFormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OptionalSquareMethodFormPars!=null) OptionalSquareMethodFormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalSquareMethodFormPars!=null) OptionalSquareMethodFormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalSquareMethodFormPars!=null) OptionalSquareMethodFormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+nameForm);
        buffer.append("\n");

        if(OptionalSquareMethodFormPars!=null)
            buffer.append(OptionalSquareMethodFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDecl]");
        return buffer.toString();
    }
}
