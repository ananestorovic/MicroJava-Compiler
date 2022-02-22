package rs.ac.bg.etf.pp1;


import java.util.Stack;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddopDecl;
import rs.ac.bg.etf.pp1.ast.AddopListDecl;
import rs.ac.bg.etf.pp1.ast.Assignment;
import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.BreakStatment;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.CondTermListDecl;
import rs.ac.bg.etf.pp1.ast.CondTermListLeft;
import rs.ac.bg.etf.pp1.ast.ConditionFact;
import rs.ac.bg.etf.pp1.ast.ConditionFactWithRelopExpr;
import rs.ac.bg.etf.pp1.ast.ConditionListDecl;
import rs.ac.bg.etf.pp1.ast.ConditionListLeft;
import rs.ac.bg.etf.pp1.ast.ContinueStatament;
import rs.ac.bg.etf.pp1.ast.Dec;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DesignatorElementSquar;
import rs.ac.bg.etf.pp1.ast.DesignatorElementWithDot;
import rs.ac.bg.etf.pp1.ast.DesignatorListDeclarationMultiple;
import rs.ac.bg.etf.pp1.ast.DesignatorName;
import rs.ac.bg.etf.pp1.ast.DesignatorWithActPars;
import rs.ac.bg.etf.pp1.ast.DivDecl;
import rs.ac.bg.etf.pp1.ast.DoStatement;
import rs.ac.bg.etf.pp1.ast.DoStatementStart;
import rs.ac.bg.etf.pp1.ast.ElseStart;
import rs.ac.bg.etf.pp1.ast.EqualToOp;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.FactorDesignator;
import rs.ac.bg.etf.pp1.ast.FunctionCall;
import rs.ac.bg.etf.pp1.ast.GreaterOREqualOp;
import rs.ac.bg.etf.pp1.ast.GreaterOp;
import rs.ac.bg.etf.pp1.ast.IfWithCondition;
import rs.ac.bg.etf.pp1.ast.Inc;
import rs.ac.bg.etf.pp1.ast.LessOREqualOp;
import rs.ac.bg.etf.pp1.ast.LessOp;
import rs.ac.bg.etf.pp1.ast.MatchedStatement;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodDeclTypeName;
import rs.ac.bg.etf.pp1.ast.MulDecl;
import rs.ac.bg.etf.pp1.ast.MulopListDecls;
import rs.ac.bg.etf.pp1.ast.NewFactor;
import rs.ac.bg.etf.pp1.ast.NewFactorExprOpt;
import rs.ac.bg.etf.pp1.ast.NoAddopList;
import rs.ac.bg.etf.pp1.ast.NoDesignatorListDeclarationMultiple;
import rs.ac.bg.etf.pp1.ast.NotEqualToOp;
import rs.ac.bg.etf.pp1.ast.NumFactor;
import rs.ac.bg.etf.pp1.ast.OptionalActParsDecl;
import rs.ac.bg.etf.pp1.ast.OptionalMinusAdd;
import rs.ac.bg.etf.pp1.ast.OptionalPrintParamm;
import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.Proba;
import rs.ac.bg.etf.pp1.ast.ProbaDerived1;
import rs.ac.bg.etf.pp1.ast.ProgramName;
import rs.ac.bg.etf.pp1.ast.ReadStatement;
import rs.ac.bg.etf.pp1.ast.Relop;
import rs.ac.bg.etf.pp1.ast.ReturnExpr;
import rs.ac.bg.etf.pp1.ast.ReturnNoExpr;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.UnmatchedIf;
import rs.ac.bg.etf.pp1.ast.UnmatchedIfElse;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.WhileStart;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}
	
	private void firstHelperBultInMeth(Obj obj) {
		obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(obj.getLevel());
		Code.put(obj.getLocalSymbols().size());
		Code.put(Code.load_n);
	}
	
	private void secondHelperBultInMeth() {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	private void addLenMethod() {
		Obj obj = Tab.find("len");
		firstHelperBultInMeth(obj);
		Code.put(Code.arraylength);
		secondHelperBultInMeth();
		
	}

	private void addChrMethod() {
		Obj obj = Tab.find("chr");
		firstHelperBultInMeth(obj);
		secondHelperBultInMeth();
	}

	private void addOrdMethod() {
		Obj obj = Tab.find("ord");
		firstHelperBultInMeth(obj);
		secondHelperBultInMeth();
	}
	
	public void visit (ProgramName programName) {
		addChrMethod();
		addLenMethod();
		addOrdMethod();
	}

	public void visit(PrintStmt printSatement) {
		int putCode;
		int loadConst = 0;

		if (printSatement.getOptionalPrintParam() instanceof OptionalPrintParamm) {
			loadConst = ((OptionalPrintParamm) printSatement.getOptionalPrintParam()).getN1();
		}
		if (printSatement.getExpr().struct == Tab.intType) {
			putCode = Code.print;
		} else {
			putCode = Code.bprint;
		}

		Code.loadConst(loadConst);
		Code.put(putCode);

	}

	public void visit(ReadStatement readStatement) {
		int putCode;
		if (readStatement.getDesignator().obj.getType() == Tab.intType) {
			putCode = Code.read;
		} else {
			putCode = Code.bread;
		}

		Code.put(putCode);
		Code.store(readStatement.getDesignator().obj);

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

	public void visit(MethodDeclTypeName methodTypeName) {

		if ("main".equalsIgnoreCase(methodTypeName.getMethodName())) {
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();

		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);

		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());

	}

	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(Assignment assignment) {
		Code.store(assignment.getDesignator().obj);
	}

	public void visit(FactorDesignator procCall) {
		Obj functionObj = ((DesignatorWithActPars) procCall.getDesignatorFunctionCall()).getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);

		Code.put2(offset);
	}

	public void visit(FunctionCall funcCall) {
		Obj functionObj = ((DesignatorWithActPars) funcCall.getDesignatorFunctionCall()).getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if (functionObj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}

	public void visit(ReturnExpr returnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(ReturnNoExpr returnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(AddopListDecl addopListDecl) {
		if (addopListDecl.getAddop() instanceof AddopDecl) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}

	}

	public void visit(MulopListDecls mulopListDecls) {
		if (mulopListDecls.getMulop() instanceof MulDecl) {
			Code.put(Code.mul);
		} else if (mulopListDecls.getMulop() instanceof DivDecl) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}

	}

	public void visit(NoAddopList term) {
		SyntaxNode myParent = term.getParent();

		while (!(myParent instanceof Expr)) {
			myParent = myParent.getParent();
		}

		if (((Expr) myParent).getOptionalMinus() instanceof OptionalMinusAdd) {
			Code.put(Code.neg);
		}
	}

	public void visit(Inc inc) {
		if (inc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(inc.getDesignator().obj);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(inc.getDesignator().obj);
	}

	public void visit(Dec dec) {
		if (dec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(dec.getDesignator().obj);
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.store(dec.getDesignator().obj);
	}

	public void visit(NewFactor newFactor) {
		if (newFactor.getNewFactorExprOptional() instanceof NewFactorExprOpt) {
			int loadConst = 1;
			Code.put(Code.newarray);
			if (newFactor.getType().struct == Tab.charType) {
				loadConst = 0;
			}
			Code.put(loadConst);
		} else if (newFactor.getType().struct.getKind() == Struct.Class) {
			Code.put(Code.new_);
			Code.put2(newFactor.getType().struct.getNumberOfFields() * 4);
		}
	}

	public void visit(Designator designator) {
		SyntaxNode parent = designator.getParent();

		if (Assignment.class != parent.getClass() && DesignatorWithActPars.class != parent.getClass()
				&& Inc.class != parent.getClass() && Dec.class != parent.getClass()
				&& ReadStatement.class != parent.getClass()) {
			Code.load(designator.obj);
		}
	}

	public void visit(DesignatorName designatorName) {
		Designator designator = (Designator) designatorName.getParent();
		if (designator.getDesignatorList() instanceof DesignatorListDeclarationMultiple) {
			Code.load(designatorName.obj);
		}
	}

	public void visit(ProbaDerived1 proba) {

		DesignatorListDeclarationMultiple parent = (DesignatorListDeclarationMultiple) proba.getParent().getParent();
		SyntaxNode myLeft;
		SyntaxNode parentOfParent;
		if (parent.getDesignatorList() instanceof NoDesignatorListDeclarationMultiple) {
			for (parentOfParent = parent.getParent(); parentOfParent instanceof DesignatorListDeclarationMultiple;) {
				parentOfParent = parentOfParent.getParent();
			}
			myLeft = ((Designator) parentOfParent).getDesignatorName();
		} else
			myLeft = parent.getDesignatorList();

		if (myLeft instanceof DesignatorListDeclarationMultiple) {
			DesignatorListDeclarationMultiple temp = (DesignatorListDeclarationMultiple) myLeft;
			if (temp.getDesignatorElement() instanceof DesignatorElementWithDot) {
				Code.load(temp.getDesignatorElement().obj);
			}
		}
	}

	public void visit(DesignatorElementSquar array) {
	}

	public void visit(DesignatorElementWithDot elementWithDot) {

		SyntaxNode parent = elementWithDot.getParent();
		if (parent.getClass() == DesignatorListDeclarationMultiple.class) {
			DesignatorListDeclarationMultiple temp = (DesignatorListDeclarationMultiple) parent;
			if (temp.getDesignatorList() instanceof DesignatorListDeclarationMultiple) {
				temp = (DesignatorListDeclarationMultiple) temp.getDesignatorList();
				Code.load(temp.getDesignatorElement().obj);
			}
		}
	}
	
	public void visit(ConditionFact conditionFact) {
		Code.loadConst(0);
		
		int addressToFix1 = Code.pc + 1;
		Code.putFalseJump(Code.eq, 0);
		Code.loadConst(0);
		int addressToFix2 = Code.pc + 1;
		Code.put(Code.jmp);
		Code.put2(0);
		Code.fixup(addressToFix1);
		Code.loadConst(1);
		Code.fixup(addressToFix2);
		
	}
	
	public void helpRelop(int myCodeOp ) {
		int addressToFix1 = Code.pc + 1;
		Code.putFalseJump(myCodeOp, 0);
		Code.loadConst(1);
		int addressToFix2 = Code.pc + 1;
		Code.put(Code.jmp);
		Code.put2(0);
		Code.fixup(addressToFix1);
		Code.loadConst(0);
		Code.fixup(addressToFix2);
		
	}
	
	
	public void visit(ConditionFactWithRelopExpr conditionFactWithRelopExpr) {
		Relop relop = conditionFactWithRelopExpr.getRelop();
	
		if(relop instanceof EqualToOp) {
			helpRelop(Code.eq);	
		}
		else if(relop instanceof NotEqualToOp) {
			helpRelop(Code.ne);	
		}
		else if(relop instanceof GreaterOp) {
			helpRelop(Code.gt);	
		}
		else if(relop instanceof GreaterOREqualOp) {
			helpRelop(Code.ge);	
		}
		else if(relop instanceof LessOp) {
			helpRelop(Code.lt);	
		}
		else if(relop instanceof LessOREqualOp) {
			helpRelop(Code.le);	
		}
	}
	
	private Stack<Integer> stackForAnd = new Stack <Integer>(); 
	
	public void visit(CondTermListLeft condTermListLeft ){
		Code.put(Code.dup);
		Code.loadConst(1);
		int addressToFix1 = Code.pc + 1; 
		Code.putFalseJump(Code.eq, 0); 
		Code.put(Code.pop);
		stackForAnd.push(addressToFix1); 
			
	}
	
	public void visit(CondTermListDecl condTermListDecl) {		
		Code.fixup(stackForAnd.pop());	
	}
	
	private Stack<Integer> stackForOr = new Stack <Integer>(); 
	
	public void visit(ConditionListLeft conditionListLeft) {
		Code.put(Code.dup);
		Code.loadConst(0);
		int addressToFix1 = Code.pc + 1; 
		Code.putFalseJump(Code.eq, 0); 
		Code.put(Code.pop);
		stackForOr.push(addressToFix1);
	}
	
	public void visit(ConditionListDecl conditionListDecl) {		
		Code.fixup(stackForOr.pop());	
	}
	
	
	private Stack<Integer> stackForIf = new Stack <Integer>(); 
	
	public void visit(IfWithCondition ifWithCondition) {
		Code.loadConst(1);
		int addressToFix1 = Code.pc + 1; 
		Code.putFalseJump(Code.eq, 0); 
		stackForIf.push(addressToFix1); 
	}
	
	private Stack<Integer> stackForElse = new Stack <Integer>(); 
	
	public void visit(ElseStart elseStart) {
		int addressToFix2 = Code.pc + 1; 
		Code.putJump(0);
		stackForElse.push(addressToFix2);
		Code.fixup(stackForIf.pop());
	}
	
	public void visit(MatchedStatement matchedStatement) {
		Code.fixup(stackForElse.pop());
	}
	
	public void visit(UnmatchedIfElse unmatchedIfElse) {
		Code.fixup(stackForElse.pop());
	}
	
	public void visit(UnmatchedIf unmatchedIf) {
		Code.fixup(stackForIf.pop());
	}
	
	private Stack<Integer> stackForDo = new Stack <Integer>(); 
	
	public void visit(DoStatementStart doStatementStart) {
		stackForDo.push(Code.pc);
	}
	
	public void visit(DoStatement doStatement) {
		Code.loadConst(1);
		Code.putFalseJump(Code.ne, stackForDo.pop());
		
		while(!stackForBreak.empty()) {
			Code.fixup(stackForBreak.pop());
		}
	}
	
	private Stack<Integer> stackForContinue = new Stack <Integer>(); 
	
	public void visit(WhileStart whileStart) {
		while(!stackForContinue.empty()) {
			Code.fixup(stackForContinue.pop());
		}
	}
	
	public void visit(ContinueStatament continueStatement) {
		int addressToFix = Code.pc + 1;
		Code.putJump(addressToFix);
		stackForContinue.push(addressToFix);
	}
	
	private Stack<Integer> stackForBreak = new Stack <Integer>(); 

	
	public void visit(BreakStatment breakStatment) {
		
		int addressToFix = Code.pc + 1;
		Code.putJump(addressToFix);
		stackForBreak.push(addressToFix);
	}
	
	
	

}
