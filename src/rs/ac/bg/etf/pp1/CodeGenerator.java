package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.NumFactor;
import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	
	public int getMainPc(){
		return mainPc;
	}
	
	public void visit(PrintStmt printSatement) {
		int putCode;
		int loadConst = 0;
		if (printSatement.getExpr().struct == Tab.intType) {
			putCode = Code.print;
		} else {
			putCode = Code.bprint;
		}
//		if (printSatement.getPrintConstantants() instanceof HasPrintConstantants) {
//			loadConst = ((HasPrintConstantants) printSatement.getPrintConstantants()).getN1();
//		}
		Code.put(putCode);
		Code.loadConst(loadConst);
		
	}
	
	 public void visit(NumFactor numFactor) {
			Obj num = Tab.insert(Obj.Con, "$", numFactor.struct);
			num.setLevel(0);
			num.setAdr(numFactor.getN1());
			
			Code.load(num);
		}

		public void visit(CharFactor charFactor) {
			Obj num = Tab.insert(Obj.Con, "$", charFactor.struct);
			num.setLevel(0);
			num.setAdr(charFactor.getC1());
			
			Code.load(num);
		}

		public void visit(BoolFactor boolFactor) {
			Obj num = Tab.insert(Obj.Con, "$", boolFactor.struct);
			num.setLevel(0);
			num.setAdr(boolFactor.getB1().equals("true") ? 1 : 0);
			
			Code.load(num);
		}

	

}
