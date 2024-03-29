package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class SemanticPass extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	Obj currentRecord = null;
	boolean errorDetected = false;
	int nVars;
	public static Struct boolType;
	List<Expr> listOfActParams = new ArrayList<>();
	int doStatement = 0;
	Struct methodReturnType = Tab.noType;
	boolean returnFound = false;
	List<Obj> currentMethodParams = new ArrayList<>();
	Struct typeGlobalVar = null;
	Boolean mainFound = false;
	int valueOfConst;
	int constVarCount = 0;
	Boolean inRecord = false;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	private void addBoolTypeInSymTab() {
		boolType = new Struct(Struct.Bool);
		boolType.setElementType(Tab.noType); // Mozda mora da se kastuje u struct
		Tab.insert(Obj.Type, "bool", boolType);
	}

	public void visit(ProgramName programName) {
		programName.obj = Tab.insert(Obj.Prog, programName.getProgramName(), Tab.noType);
		addBoolTypeInSymTab();
		Tab.openScope();
	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();

		Obj methodeMain = Tab.find("main");
		if (!(methodeMain.getType() == Tab.noType && methodeMain != Tab.noObj && methodeMain.getKind() == Obj.Meth
				&& methodeMain.getLevel() == 0))
			report_error("Greska: Ne postoji MAIN!", program);

		Tab.chainLocalSymbols(program.getProgramName().obj);
		Tab.closeScope();
	}

	public void visit(RecordName recordName) {
		Struct recordType = new Struct(Struct.Class);
		recordType.setElementType(Tab.noType);
		recordName.obj = Tab.insert(Obj.Type, recordName.getRecordName(), recordType);
		Tab.openScope();
		inRecord = true;
	}

	public void visit(RecordDecl recordDecl) {
		Tab.chainLocalSymbols(recordDecl.getRecordName().obj.getType());
		Tab.closeScope();
		inRecord = false;
	}

	public void visit(MethodDeclTypeName methodTypeName) {
		String nameMethod = methodTypeName.getMethodName();
		Obj obj = Tab.find(nameMethod);
		methodReturnType = methodTypeName.getReturnType().struct;

		if (obj == Tab.noObj) {
			if (methodTypeName.getMethodName().equals("main")) {
				if (!methodReturnType.equals(Tab.noType)) {
					report_error("Greska: Metoda MAIN mora biti deklarisana kao void metoda \r\n" + "bez argumenata!",
							methodTypeName);
				}
			}
			obj = Tab.insert(Obj.Meth, nameMethod, methodReturnType);
			Tab.openScope();
			methodTypeName.obj = obj;

		} else {
			report_error("Greska: Metoda sa ovim imenom vec postoji!", methodTypeName);
			Tab.openScope();
			methodTypeName.obj = Tab.noObj;
		}
		currentMethod = methodTypeName.obj;
	}

	public void visit(MethodDecl methodDecl) {
		// TODO Ovde ce biti problem sa argumentima
		Obj obj = methodDecl.getMethodTypeName().obj;
		if (obj != Tab.noObj) {
			Tab.chainLocalSymbols(obj);
			obj.setLevel(currentMethodParams.size());
		}
		Tab.closeScope();
		if (obj.getName().equals("main") && obj.getLevel() != 0) {
			report_error("Metoda MAIN ne sme imati formalne parametre", methodDecl);
		} else {
			mainFound = true;
		}

		if (!returnFound && !methodReturnType.equals(Tab.noType)) {
			report_error("Greska: Ako metoda nije tipa void, mora imati iskaz return unutar svog tela!", methodDecl);
		}

		methodReturnType = Tab.noType;
		returnFound = false;
		currentMethodParams.clear();
		currentMethod = null;
	}

	public void visit(NoReturnMethodType noReturnType) {
		noReturnType.struct = Tab.noType;
	}

	public void visit(ReturnMethodType returnType) {
		returnType.struct = returnType.getType().struct;
	}

	// Type

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();

			} else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}
		typeGlobalVar = type.struct;
	}

	public void visit(DesignatorElementWithDot elementWithDot) {

		DesignatorListDeclarationMultiple parent = (DesignatorListDeclarationMultiple) elementWithDot.getParent();
		Obj myLeft;
		SyntaxNode parentOfParent = parent.getParent();
		if (parent.getDesignatorList() instanceof NoDesignatorListDeclarationMultiple) {

			for (parentOfParent = parent.getParent(); parentOfParent instanceof DesignatorListDeclarationMultiple;) {
				parentOfParent = parentOfParent.getParent();
			}
			myLeft = ((Designator) parentOfParent).getDesignatorName().obj;
		} else
			myLeft = parent.getDesignatorList().obj;

		if (myLeft == null)
			elementWithDot.obj = Tab.noObj;
		else {
			if (myLeft.getType().getKind() != Struct.Class) {
				report_error("Greska: designator nije record ", elementWithDot);
				elementWithDot.obj = Tab.noObj;
			} else {
				Obj help = myLeft.getType().getMembersTable().searchKey(elementWithDot.getElementWithDotName());
				if (help == null) {
					report_error("Greska: ne postoji polje/metoda ", elementWithDot);
					elementWithDot.obj = Tab.noObj;

				} else {
					elementWithDot.obj = help;
				}
			}
		}
	}

	public void visit(DesignatorElementSquar elementSquare) {

		DesignatorListDeclarationMultiple parent = (DesignatorListDeclarationMultiple) elementSquare.getParent();
		Obj myLeft;
		SyntaxNode parentOfParent;
		if (parent.getDesignatorList() instanceof NoDesignatorListDeclarationMultiple) {
			for (parentOfParent = parent.getParent(); parentOfParent instanceof DesignatorListDeclarationMultiple;) {
				parentOfParent = parentOfParent.getParent();
			}
			myLeft = ((Designator) parentOfParent).getDesignatorName().obj;
		} else
			myLeft = parent.getDesignatorList().obj;

		if (myLeft == null)
			elementSquare.obj = Tab.noObj;
		else {
			if (elementSquare.getExpr().struct.getKind() != Struct.Int) {
				report_error("Greska: expr mora biti int", elementSquare);
				elementSquare.obj = Tab.noObj;
			} else {
				if (myLeft.getType().getKind() == Struct.Array) {
					elementSquare.obj = new Obj(Obj.Elem, "_elementNiza", myLeft.getType().getElemType());
				} else {
					report_error("Greska: nije niz ", elementSquare);
					elementSquare.obj = Tab.noObj;
				}
			}
		}

	}

	public void visit(DesignatorListDeclarationMultiple listDeclarationMultipl) {
		listDeclarationMultipl.obj = listDeclarationMultipl.getDesignatorElement().obj;
	}

	public void visit(NoDesignatorListDeclarationMultiple noListDeclarationMultiple) {
		noListDeclarationMultiple.obj = Tab.noObj;

	}

	public void visit(DesignatorName designatorName) {
		designatorName.obj = Tab.find(designatorName.getDesignatorName());

	}

	public void visit(Designator designator) {

		Obj obj = Tab.find(designator.getDesignatorName().getDesignatorName());
		if (obj == Tab.noObj) {
			designator.obj = Tab.noObj;
			report_error("Greska na liniji " + designator.getLine() + " : ime " + designator.getDesignatorName()
					+ " nije deklarisano! ", null);
		}
		if (designator.getDesignatorList() instanceof NoDesignatorListDeclarationMultiple)
			designator.obj = designator.getDesignatorName().obj;
		else
			designator.obj = designator.getDesignatorList().obj;
	}

	public void visit(NoMulopListDecls noMulopList) {
		noMulopList.struct = noMulopList.getFactor().struct;
	}

	public void visit(MulopListDecls mulopList) {
		if (!mulopList.getFactor().struct.equals(mulopList.getMulopList().struct)) {

			report_error("Greska na liniji " + mulopList.getLine() + " : ova mulop lista nema parametre istog tipa! ",
					mulopList);
			mulopList.struct = Tab.noType;
			return;
		}

		if (mulopList.getFactor().struct != Tab.intType) {
			report_error("Greska na liniji " + mulopList.getLine() + " : mullop lista mora imati int parametre! ",
					mulopList);
			mulopList.struct = Tab.noType;
			return;
		}

		mulopList.struct = mulopList.getMulopList().struct;
	}

	public void visit(Term term) {
		term.struct = term.getMulopList().struct;
	}

	public void visit(NoAddopList noAddopList) {
		noAddopList.struct = noAddopList.getTerm().struct;
	}

	public void visit(AddopListDecl addopList) {

		if (!addopList.getTerm().struct.equals(addopList.getAddopList().struct)) {
			report_error("Greska: ova addop lista nema parametre istog tipa! ", addopList);
			addopList.struct = Tab.noType;
			return;
		}
		if (addopList.getTerm().struct != Tab.intType) {
			report_error("Greska na liniji " + addopList.getLine() + " : addop lista mora imati int parametre! ",
					addopList);
			addopList.struct = Tab.noType;
			return;
		}

		addopList.struct = addopList.getTerm().struct;
	}

	public void visit(Expr expr) {
		expr.struct = expr.getAddopList().struct;

		if (expr.getOptionalMinus() instanceof OptionalMinusAdd) {
			if (!expr.getAddopList().struct.equals(Tab.intType)) {
				report_error("Greska: ne moze stajati minus ispred izraza koji nije int!", expr);
				methodReturnType = Tab.noType;
			}
		}
	}

	// ActPars

	public void visit(OptionalActParsDecl actuals) {
		if (currentMethod == null) {
			report_error("Greska: ne postoji metoda!", actuals);
			return;
		}

		Collection<Obj> parametersFromSymTab = Tab.find(currentMethod.getName()).getLocalSymbols();
		ArrayList<Obj> listParametersFromSymTab = new ArrayList<>(parametersFromSymTab);

		if (listOfActParams.size() != currentMethod.getLevel()) {
			report_error("Greska: Broj formalnih i stvarnih argumenata metode mora biti isti!", actuals);
			return;
		}

		for (int i = 0; i < listOfActParams.size(); i++) {
			if (listOfActParams.get(i).struct.getKind() != listParametersFromSymTab.get(i).getType().getKind()) {
				report_error(
						"Greska: Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog "
								+ "argumenta na odgovarajucoj poziciji!",
						actuals);
			}
		}
	}

	public void visit(NoOptionalActParsDecl noActuals) {
		Obj methObj = Tab.find(currentMethod.getName());

		if (methObj == Tab.noObj) {
			report_error("Greska: ne postoji metoda!", noActuals);
			return;
		} else if (methObj.getLevel() > 0) {
			report_error(
					"Greska: Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog "
							+ "argumenta na odgovarajucoj poziciji!",
					noActuals);
			return;
		}
	}

	// SingleStatement

	public void visit(DoStatementStart doStatementStart) {
		doStatement++;
	}

	public void visit(DoStatement doStatementDecl) {
		doStatement--;

		if (doStatementDecl.getCondition().struct != boolType) {
			report_error("Greska: Uslovni izraz Condition mora biti tipa bool.", doStatementDecl);
		}
	}

	public void visit(BreakStatment breakStatment) {
		if (doStatement == 0) {
			report_error("Greska: Iskaz break se moze koristiti samo unutar do-while petlje", breakStatment);
		}

	}

	public void visit(ContinueStatament continueStatament) {
		if (doStatement == 0) {
			report_error("Greska: Iskaz continue se moze koristiti samo unutar do-while petlje", continueStatament);
		}

	}

	public void visit(ReturnExpr returnExpr) {
		if (returnExpr.getExpr().struct != methodReturnType) {
			report_error(
					"Greska: Tip neterminala Expr mora biti ekvivalentan povratnom tipu tekuce metode/ globalne funkcije",
					returnExpr);
		}
		returnFound = true;
	}

	public void visit(ReturnNoExpr returnNoExpr) {
		if (!methodReturnType.equals(Tab.noType)) {
			report_error("Greska: Mora se navesti izraz u return naredbi posto metoda nije void.", returnNoExpr);
		}
		returnFound = true;
	}

	public void visit(ReadStatement readStatement) {
		Obj designator = readStatement.getDesignator().obj;
		if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error(
					"Greska:  Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.\r\n" + "",
					readStatement);
		}

		if (!(designator.getType().getKind() == Struct.Int || designator.getType().getKind() == Struct.Char
				|| designator.getType().getKind() == Struct.Bool)) {
			report_error("Greska:   Designator mora biti tipa int, char ili bool", readStatement);
		}
	}

	public void visit(PrintStmt printSatement) {
		printCallCount++;

		
		Struct exprType = printSatement.getExpr().struct;
		if (!exprType.equals(Tab.intType) && !exprType.equals(Tab.charType) && !exprType.equals(boolType)) {
			report_error("Greska: Expr kod printa mora biti tipa int, char ili bool", printSatement);
		}

	}
	
	public void visit(UnmatchedIf unmatchedIf) {
		unmatchedIf.struct = unmatchedIf.getIfWithCondition().struct;
	}
	
	public void visit(UnmatchedIfElse unmatchedIfElse) {
		unmatchedIfElse.struct = unmatchedIfElse.getIfWithCondition().struct;
	}
	
	
	public void visit(IfWithCondition ifWithCondition) {
		ifWithCondition.struct = ifWithCondition.getCondition().struct;
	}

	public void visit(MatchedStatement statementIfElse) {
		if (statementIfElse.getIfWithCondition().struct != boolType && statementIfElse.getIfWithCondition().struct != Tab.intType ) {
			report_error("Greska: Tip uslovnog izraza Condition mora biti bool. (IfElse)", statementIfElse);
		}
	}

	// DesignatorStatement

	public void visit(Inc inc) {

		Obj designator = inc.getDesignator().obj;

		if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error(
					"Greska: Designator mora oznacavati promenljivu, element niza ili polje objekta unutrasnje klase",
					inc);
		}

		if (designator.getType().getKind() != Struct.Int) {
			report_error("Greska:  Designator mora biti tipa int.", inc);
		}

	}

	public void visit(Dec dec) {
		Obj designator = dec.getDesignator().obj;

		if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error(
					"Greska: Designator mora oznacavati promenljivu, element niza ili polje objekta unutrasnje klase",
					dec);
		}

		if (designator.getType().getKind() != Struct.Int) {
			report_error("Greska: Designator mora biti tipa int.", dec);
		}

	}

	public void visit(Assignment assignment) {

		Obj designator = Tab.find(assignment.getDesignator().getDesignatorName().getDesignatorName());

		if (designator.equals(Tab.noObj)) {
			report_error("Greska: Promenljiva nije nadjena!", assignment);
			return;
		}

		if (designator.getKind() == Obj.Meth) {
			report_error("Greska: Ime predstavlja funkciju!", assignment);
			return;
		}

		if (!assignment.getAssignmentStatement().struct.assignableTo(assignment.getDesignator().obj.getType())) {
			report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilni!", assignment);
			return;
		}

//		if (designator.getType().getKind() == Struct.Array
//				&& assignment.getAssignmentStatement().struct.getKind() == Struct.Array) {
//
//			Struct element1 = designator.getType().getElemType();
//			Struct element2 = assignment.getAssignmentStatement().struct.getElemType();
//
//			if (!element1.equals(element2)) {
//
//				while (element1.getKind() == Struct.Array) {
//					element1 = element1.getElemType();
//				}
//				while (element2.getKind() == Struct.Array) {
//					element2 = element2.getElemType();
//				}
//				if (!element1.equals(element2))
//					report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilni!", assignment);
//				return;
//			}
//
//		}
//
//		else if (designator.getType().getKind() == Struct.Array) {
//
//			Struct element1 = designator.getType().getElemType();
//
//			if (!element1.equals(assignment.getAssignmentStatement().struct)) {
//				report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilni!", assignment);
//				return;
//			}
//
//		}
//
//		else if (assignment.getAssignmentStatement().struct.getKind() == Struct.Array) {
//
//			Struct element1 = assignment.getAssignmentStatement().struct.getElemType();
//			//  - STAVILA SAM OVAJ TAG DA VIDIS SAMO JE LI OKEJ
//			// PROVERITI JE LI OKEJ JER BAS I NE IZGLEDA TAKO :)
//			if (designator.getType().getKind() == Struct.Class) {
//				int e1 = assignment.getAssignmentStatement().struct.getElemType().getKind();
//				int e2 = designator.getKind();
//
//				if (e1 != e2) {
//					report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilni!!", assignment);
//				}
//
//				return;
//			}
//
//			if (!element1.equals(designator.getType())) {
//				report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilni", assignment);
//				return;
//			}
//
//			return;
//
//		} else if (assignment.getAssignmentStatement().struct.getKind() == Struct.Class) {
//			// OVO MORA DA SE SREDI!!!!!!!!!!!!!!!!!!!! 
//			if (designator.getType().getElemType().getKind() != assignment.getAssignmentStatement().struct.getElemType()
//					.getKind()) {
//				report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilni!", assignment);
//				return;
//			}
//		}
//		else if (assignment.getAssignmentStatement().struct.getKind() != assignment.getDesignator().obj.getType().getKind()) {
//			report_error("Greska: Ne mozete dodeliti jedan tip drugom, nisu kompatibilniHEHE", assignment);
//			return;
//		}

	}

	public void visit(AssignmentStatement assignmentStatement) {
		assignmentStatement.struct = assignmentStatement.getExpr().struct;
	}

	// FormPars

	public void visit(FormParsDeclArray formParsDecl) {

		Obj obj = Tab.find(formParsDecl.getNameForm());

		if (obj == Tab.noObj || obj.getLevel() == 0) {
			Obj inserted = Tab.insert(Obj.Var, formParsDecl.getNameForm(), new Struct(Struct.Array));
			inserted.getType().setElementType(formParsDecl.getFormParsType().struct);

			inserted.setFpPos(currentMethodParams.size());
			currentMethodParams.add(inserted);
		} else {
			errorDetected = true;
			report_error("Greska: Vec je definisan ovaj form param!", formParsDecl);
		}
	}

	public void visit(FormParsDeclVar formParsDeclVar) {

		Obj obj = Tab.find(formParsDeclVar.getNameForm());

		if (obj == Tab.noObj || obj.getLevel() == 0) {
			Obj inserted = Tab.insert(Obj.Var, formParsDeclVar.getNameForm(),
					formParsDeclVar.getFormParsType().getType().struct);

			inserted.setFpPos(currentMethodParams.size());
			currentMethodParams.add(inserted);
		} else {
			errorDetected = true;
			report_error("Greska: Vec je definisan ovaj form param", formParsDeclVar);
		}
	}

//VarDecl

	public void visit(OnlyVariableDecl localVar) {
		Obj obj = Tab.noObj;
		if (inRecord) {
			SymbolDataStructure locals = Tab.currentScope().getLocals();
			if (locals != null) {
				obj = locals.searchKey(localVar.getVarName());
				if (obj == null)
					obj = Tab.noObj;
			}
		} else {
			obj = Tab.find(localVar.getVarName());
		}
		if (obj != Tab.noObj)
			report_error("Greska: Vec deklarisana!", localVar);
		else {
			varDeclCount++;
			if (inRecord) {
				localVar.obj = Tab.insert(Obj.Fld, localVar.getVarName(), typeGlobalVar);
				localVar.obj.getType().setElementType(Tab.noType);
			} else {
				localVar.obj = Tab.insert(Obj.Var, localVar.getVarName(), typeGlobalVar);
				localVar.obj.getType().setElementType(Tab.noType);
			}

		}
	}

	public void visit(VariableDecl localArr) {
		Obj obj = Tab.noObj;
		if (inRecord) {
			SymbolDataStructure locals = Tab.currentScope().getLocals();
			if (locals != null) {
				obj = locals.searchKey(localArr.getVarName());
				if (obj == null)
					obj = Tab.noObj;
			}
		} else {
			obj = Tab.find(localArr.getVarName());
		}
		if (obj != Tab.noObj)
			report_error("Greska: Vec deklarisano!", localArr);
		else {
			varDeclCount++;
			if (inRecord) {
				localArr.obj = Tab.insert(Obj.Fld, localArr.getVarName(), new Struct(Struct.Array));
				localArr.obj.getType().setElementType(typeGlobalVar);
			} else {
				localArr.obj = Tab.insert(Obj.Var, localArr.getVarName(), new Struct(Struct.Array));
				localArr.obj.getType().setElementType(typeGlobalVar);
			}
		}
	}

	// Const

	public void visit(ConstDeclOne constDeclOne) {
		constVarCount++;
		Obj help = Tab.find(constDeclOne.getConstName().getName());
		if (help != Tab.noObj) {
			report_error("Greska: Ova const promenljiva je vec definisana! ", null);
		}
		if (!constDeclOne.getConstDeclVal().struct.assignableTo(typeGlobalVar)) {
			report_error("Greska: Tipovi nisu kompatibilni (const value)! ", null);
		}
		Obj varNode = Tab.insert(Obj.Con, constDeclOne.getConstName().getName(), typeGlobalVar);
		varNode.setAdr(valueOfConst);
	}

	public void visit(NumConstant numConstant) {
		valueOfConst = numConstant.getValue();
		numConstant.struct = Tab.intType;
	}

	public void visit(CharConstant charConstant) {
		charConstant.struct = Tab.charType;

		valueOfConst = charConstant.getValue();
	}

	public void visit(BoolConstant boolConstant) {
		boolConstant.struct = boolType;
		String isBoolOk = boolConstant.getValue();

		if (isBoolOk.equals("false")) {
			valueOfConst = 0;
			return;
		} else if (isBoolOk.equals("true")) {
			valueOfConst = 1;
			return;
		} else {
			report_error("Greska: Nije odgovarajuci tip za ovu bool promenljivu! ", null);

		}

	}

	//
	public void visit(NoConditionListDecl noConditionList) {
		noConditionList.struct = noConditionList.getCondTerm().struct;
	}
	
	public void visit(ConditionListLeft conditionListLeft) {
		conditionListLeft.struct = conditionListLeft.getCondTerm().struct;
	}

	public void visit(ConditionListDecl conditionListDec) {
		if (conditionListDec.getConditionListLeft().struct == boolType
				&& conditionListDec.getConditionList().struct == boolType) {
			conditionListDec.struct = boolType;
		} 
		else if(conditionListDec.getConditionListLeft().struct == Tab.intType
				&& conditionListDec.getConditionList().struct == Tab.intType) {
			conditionListDec.struct = Tab.intType;

		}
		else {
			report_error("Greska na liniji " + conditionListDec.getLine() + " : trebalo je da bude tipa bool!",
					conditionListDec);
			conditionListDec.struct = Tab.noType;
		}
	}

	public void visit(Condition condition) {
		condition.struct = condition.getConditionList().struct;
	}

	public void visit(NoCondTermListDecl noCondTermList) {
		noCondTermList.struct = noCondTermList.getCondFact().struct;

	}
	
	public void visit(CondTermListLeft condTermListLeft) {
		condTermListLeft.struct = condTermListLeft.getCondFact().struct;
	}

	public void visit(CondTermListDecl condTermList) {
		if (condTermList.getCondTermListLeft().struct == boolType && condTermList.getCondTermList().struct == boolType)
			condTermList.struct = boolType;
		else if(condTermList.getCondTermListLeft().struct == Tab.intType && condTermList.getCondTermList().struct == Tab.intType)
			condTermList.struct = Tab.intType;
		else {
			report_error("Greska na liniji " + condTermList.getLine() + " : trebalo je da bude tipa bool!",
					condTermList);
			condTermList.struct = Tab.noType;
		}
	}

	public void visit(CondTerm condTerm) {
		condTerm.struct = condTerm.getCondTermList().struct;
	}

	public void visit(ConditionFact conditionFact) {

		if (conditionFact.getExpr().struct == boolType) {
			conditionFact.struct = boolType;
		}
		else if(conditionFact.getExpr().struct==Tab.intType) {
			conditionFact.struct = Tab.intType;
		} else {
			report_error("Greska na liniji " + conditionFact.getLine() + " : trebalo je da bude tipa bool!",
					conditionFact);
			conditionFact.struct = Tab.noType;
		}

	}

	public void visit(ConditionFactWithRelopExpr conditionFactWithRelopExpr) {

		if (conditionFactWithRelopExpr.getExpr().struct.getKind() != conditionFactWithRelopExpr.getExpr1().struct
				.getKind()) {
			report_error("Greska na liniji " + conditionFactWithRelopExpr.getLine() + " : tipovi nisu kompatibilni!",
					conditionFactWithRelopExpr);
			conditionFactWithRelopExpr.struct = Tab.noType;
			return;
		}
		if ((conditionFactWithRelopExpr.getExpr().struct.getKind() == Struct.Array)
				|| (conditionFactWithRelopExpr.getExpr1().struct.getKind() == Struct.Array)
				|| (conditionFactWithRelopExpr.getExpr().struct.getKind() == Struct.Class)
				|| (conditionFactWithRelopExpr.getExpr1().struct.getKind() == Struct.Class)) {
			if ((conditionFactWithRelopExpr.getRelop().equals("!="))
					|| (conditionFactWithRelopExpr.getRelop().equals("=="))) {
				conditionFactWithRelopExpr.struct = boolType;
				return;
			} else {
				report_error(
						"Greska na liniji " + conditionFactWithRelopExpr.getLine()
								+ " : ovi relacioni operatori nisu kompatibilni za ovaj tip promenljivih!",
						conditionFactWithRelopExpr);
				conditionFactWithRelopExpr.struct = Tab.noType;
				return;
			}
		}
		conditionFactWithRelopExpr.struct = boolType;
	}

	public void visit(DesignatorWithActPars designatorWithActPars) {

		if (designatorWithActPars.getDesignator().obj.getKind() != Obj.Meth) {
			report_error("Greska: Metoda ne postoji!", designatorWithActPars);
			designatorWithActPars.struct = Tab.noType;
			return;
		}

		// postaviti tip kad se pozove metoda
		designatorWithActPars.struct = designatorWithActPars.getDesignator().obj.getType();
	}

	public void visit(Var factorDesignator) {
		if (factorDesignator.getDesignator().obj == null)
			factorDesignator.struct = Tab.noType;
		else
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
	}

	// proslediti tip kad se pozove metoda do factora
	public void visit(FactorDesignator factorDesignator) {
		factorDesignator.struct = factorDesignator.getDesignatorFunctionCall().struct;
	}

	//

	public void visit(NewFactorExprOpt newOpt) {
		if (newOpt.getExpr().struct.getKind() != Struct.Int)
			report_error("Greska na liniji: " + newOpt.getLine() + " expr mora biti tipa int ", newOpt);
	}

	public void visit(NewFactor newFactor) {

		if (newFactor.getNewFactorExprOptional() instanceof NoNewFactorExprOpt) {
			if (newFactor.getType().struct.getKind() != Struct.Class) {
				report_error("Greska na liniji: " + newFactor.getLine() + " ovo nije tipa strukture ", newFactor);
				newFactor.struct = Tab.noType;
				return;
			}
			newFactor.struct = newFactor.getType().struct;
		} else {
			newFactor.struct = new Struct(Struct.Array);
			newFactor.struct.setElementType(newFactor.getType().struct);
		}
	}

	//

	public void visit(NumFactor numFactor) {
		numFactor.struct = Tab.intType;
	}

	public void visit(CharFactor charFactor) {
		charFactor.struct = Tab.charType;
	}

	public void visit(BoolFactor boolFactor) {
		boolFactor.struct = boolType;
	}

	public void visit(ExprFactor exprFactor) {
		exprFactor.struct = exprFactor.getExpr().struct;
	}

	public boolean passed() {
		return !errorDetected;
	}

}
