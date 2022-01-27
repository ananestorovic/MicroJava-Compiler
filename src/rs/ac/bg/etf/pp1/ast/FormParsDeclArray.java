// generated with ast extension for cup
// version 0.8
// 27/0/2022 19:47:40


package rs.ac.bg.etf.pp1.ast;

public class FormParsDeclArray extends FormPars {

    private FormParsType FormParsType;
    private String nameForm;

    public FormParsDeclArray (FormParsType FormParsType, String nameForm) {
        this.FormParsType=FormParsType;
        if(FormParsType!=null) FormParsType.setParent(this);
        this.nameForm=nameForm;
    }

    public FormParsType getFormParsType() {
        return FormParsType;
    }

    public void setFormParsType(FormParsType FormParsType) {
        this.FormParsType=FormParsType;
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
        if(FormParsType!=null) FormParsType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsType!=null) FormParsType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsType!=null) FormParsType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDeclArray(\n");

        if(FormParsType!=null)
            buffer.append(FormParsType.toString("  "+tab));
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
