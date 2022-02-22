// generated with ast extension for cup
// version 0.8
// 22/1/2022 4:49:20


package rs.ac.bg.etf.pp1.ast;

public class GroupDeclarations extends GroupDeclList {

    private GroupDeclList GroupDeclList;
    private GroupDecl GroupDecl;

    public GroupDeclarations (GroupDeclList GroupDeclList, GroupDecl GroupDecl) {
        this.GroupDeclList=GroupDeclList;
        if(GroupDeclList!=null) GroupDeclList.setParent(this);
        this.GroupDecl=GroupDecl;
        if(GroupDecl!=null) GroupDecl.setParent(this);
    }

    public GroupDeclList getGroupDeclList() {
        return GroupDeclList;
    }

    public void setGroupDeclList(GroupDeclList GroupDeclList) {
        this.GroupDeclList=GroupDeclList;
    }

    public GroupDecl getGroupDecl() {
        return GroupDecl;
    }

    public void setGroupDecl(GroupDecl GroupDecl) {
        this.GroupDecl=GroupDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GroupDeclList!=null) GroupDeclList.accept(visitor);
        if(GroupDecl!=null) GroupDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GroupDeclList!=null) GroupDeclList.traverseTopDown(visitor);
        if(GroupDecl!=null) GroupDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GroupDeclList!=null) GroupDeclList.traverseBottomUp(visitor);
        if(GroupDecl!=null) GroupDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GroupDeclarations(\n");

        if(GroupDeclList!=null)
            buffer.append(GroupDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GroupDecl!=null)
            buffer.append(GroupDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GroupDeclarations]");
        return buffer.toString();
    }
}
