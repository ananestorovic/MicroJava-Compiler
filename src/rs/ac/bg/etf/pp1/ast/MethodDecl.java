// generated with ast extension for cup
// version 0.8
// 26/0/2022 7:30:43


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodTypeName MethodTypeName;
    private OptionalFormalParams OptionalFormalParams;
    private MethodVarList MethodVarList;
    private StatementList StatementList;

    public MethodDecl (MethodTypeName MethodTypeName, OptionalFormalParams OptionalFormalParams, MethodVarList MethodVarList, StatementList StatementList) {
        this.MethodTypeName=MethodTypeName;
        if(MethodTypeName!=null) MethodTypeName.setParent(this);
        this.OptionalFormalParams=OptionalFormalParams;
        if(OptionalFormalParams!=null) OptionalFormalParams.setParent(this);
        this.MethodVarList=MethodVarList;
        if(MethodVarList!=null) MethodVarList.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodTypeName getMethodTypeName() {
        return MethodTypeName;
    }

    public void setMethodTypeName(MethodTypeName MethodTypeName) {
        this.MethodTypeName=MethodTypeName;
    }

    public OptionalFormalParams getOptionalFormalParams() {
        return OptionalFormalParams;
    }

    public void setOptionalFormalParams(OptionalFormalParams OptionalFormalParams) {
        this.OptionalFormalParams=OptionalFormalParams;
    }

    public MethodVarList getMethodVarList() {
        return MethodVarList;
    }

    public void setMethodVarList(MethodVarList MethodVarList) {
        this.MethodVarList=MethodVarList;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
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
        if(MethodTypeName!=null) MethodTypeName.accept(visitor);
        if(OptionalFormalParams!=null) OptionalFormalParams.accept(visitor);
        if(MethodVarList!=null) MethodVarList.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseTopDown(visitor);
        if(OptionalFormalParams!=null) OptionalFormalParams.traverseTopDown(visitor);
        if(MethodVarList!=null) MethodVarList.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodTypeName!=null) MethodTypeName.traverseBottomUp(visitor);
        if(OptionalFormalParams!=null) OptionalFormalParams.traverseBottomUp(visitor);
        if(MethodVarList!=null) MethodVarList.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodTypeName!=null)
            buffer.append(MethodTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalFormalParams!=null)
            buffer.append(OptionalFormalParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodVarList!=null)
            buffer.append(MethodVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
