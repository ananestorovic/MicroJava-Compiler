// generated with ast extension for cup
// version 0.8
// 26/0/2022 7:30:43


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListModification extends VarDeclListModif {

    private VarDecl VarDecl;
    private VarDeclListModif VarDeclListModif;

    public VarDeclListModification (VarDecl VarDecl, VarDeclListModif VarDeclListModif) {
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.VarDeclListModif=VarDeclListModif;
        if(VarDeclListModif!=null) VarDeclListModif.setParent(this);
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public VarDeclListModif getVarDeclListModif() {
        return VarDeclListModif;
    }

    public void setVarDeclListModif(VarDeclListModif VarDeclListModif) {
        this.VarDeclListModif=VarDeclListModif;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListModification(\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListModif!=null)
            buffer.append(VarDeclListModif.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListModification]");
        return buffer.toString();
    }
}
