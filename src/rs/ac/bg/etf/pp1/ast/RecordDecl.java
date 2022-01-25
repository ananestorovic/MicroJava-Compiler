// generated with ast extension for cup
// version 0.8
// 25/0/2022 7:22:29


package rs.ac.bg.etf.pp1.ast;

public class RecordDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String recordName;
    private VarDeclListModif VarDeclListModif;

    public RecordDecl (String recordName, VarDeclListModif VarDeclListModif) {
        this.recordName=recordName;
        this.VarDeclListModif=VarDeclListModif;
        if(VarDeclListModif!=null) VarDeclListModif.setParent(this);
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName=recordName;
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
        if(VarDeclListModif!=null) VarDeclListModif.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListModif!=null) VarDeclListModif.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListModif!=null) VarDeclListModif.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RecordDecl(\n");

        buffer.append(" "+tab+recordName);
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
