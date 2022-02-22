package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.DesignatorElementWithDot;
import rs.ac.bg.etf.pp1.ast.FormParsDeclArray;
import rs.ac.bg.etf.pp1.ast.FormParsDeclVar;
import rs.ac.bg.etf.pp1.ast.OnlyVariableDecl;
import rs.ac.bg.etf.pp1.ast.VariableDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {

protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
	
		public void visit(FormParsDeclArray formalParms){ //treba li ova klasa
			count++;
		}
		
		public void visit(FormParsDeclVar formalParms){ //treba li ova klasa
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(VariableDecl varDecl){
			count++;
		}
		
		public void visit(OnlyVariableDecl varDecl){
			count++;
		}
	}
	
//	public static class ElementWithDotCounter extends CounterVisitor{
//		
//		public void visit(DesignatorElementWithDot elementWithDot){
//			count++;
//		}
//	}
}
