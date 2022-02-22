// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class RecordDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private RecordName RecordName;
    private VarDeclListModif VarDeclListModif;

    public RecordDecl (RecordName RecordName, VarDeclListModif VarDeclListModif) {
        this.RecordName=RecordName;
        if(RecordName!=null) RecordName.setParent(this);
        this.VarDeclListModif=VarDeclListModif;
        if(VarDeclListModif!=null) VarDeclListModif.setParent(this);
    }

    public RecordName getRecordName() {
        return RecordName;
    }

    public void setRecordName(RecordName RecordName) {
        this.RecordName=RecordName;
    }

    public VarDeclListModif getVarDeclListModif() {
        return VarDeclListModif;
    }

    public void setVarDeclListModif(VarDeclListModif VarDeclListModif) {
        this.VarDeclListModif=VarDeclListModif;
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
        if(RecordName!=null) RecordName.accept(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RecordName!=null) RecordName.traverseTopDown(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RecordName!=null) RecordName.traverseBottomUp(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RecordDecl(\n");

        if(RecordName!=null)
            buffer.append(RecordName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListModif!=null)
            buffer.append(VarDeclListModif.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RecordDecl]");
        return buffer.toString();
    }
}
