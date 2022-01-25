package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	public static Struct boolType;
	List<Expr> listOfActParams = new ArrayList<>();
	int doStatement = 0;
	Struct methodReturnType = Tab.noType;
	Boolean haveReturn = false;
	List<Obj> currentMethodParams = new ArrayList<>();
	Struct typeGlobalVar = null;
	//List<Obj> currentMethodParams = new ArrayList<>();


	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	private void addBoolTypeInSymTab() {
		boolType = new Struct(Struct.Bool);
		boolType.setElementType(Tab.noType); //Mozda mora da se kastuje u struct
		Tab.insert(Obj.Type, "bool", boolType);
	}

	
	
	
    
    public void visit(ProgramName programName){
    	programName.obj = Tab.insert(Obj.Prog, programName.getProgramName(), Tab.noType);
    	Tab.openScope();
    }
  
    public void visit(Program program){
nVars = Tab.currentScope.getnVars();
		
		
		Obj mainMeth = Tab.find("main");
		if(mainMeth != Tab.noObj  
			&&	mainMeth.getKind() == Obj.Meth 
			&&	mainMeth.getType() == Tab.noType
			&&	mainMeth.getLevel() == 0) 
			report_info("postoji ispravan main", program);
		else
			report_error("ne postoji void main() globalna funkcija", program);
		
		Tab.chainLocalSymbols(program.getProgramName().obj);
		Tab.closeScope();
	}
    
    
    
    //Type
    
    public void visit(Type type){
    	Obj typeNode = Tab.find(type.getTypeName());
    	if(typeNode == Tab.noObj){
    		report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    		}else{
    			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
    }
   
    
    
    

    public void visit(MethodDeclTypeName methodTypeName){
    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(), methodTypeName.getType().struct);
    	methodTypeName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeName.getMethodName(), methodTypeName);
    }
    
    public void visit(MethodDeclVoidName methodTypeName){
    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethodName(), Tab.noType);
    	methodTypeName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeName.getMethodName(), methodTypeName);
    }
    

    
    public void visit(MethodDecl methodDecl){
    	if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	returnFound = false;
    	currentMethod = null;
    }
    
    public void visit (DesignatorElementWithDot elementWithDot) {
    	
    	elementWithDot.obj = new Obj (Obj.NO_VALUE, elementWithDot.getI1(),Tab.noType);
    	
    }
    
    public void visit (DesignatorElementSquar elementSquar) { 
    	
    	if(elementSquar.getExpr().struct != Tab.intType)
			report_error("Greska: expr mora biti tipa int", elementSquar);
    	
    	elementSquar.obj = new Obj (Obj.Elem, "elemNiza" ,Tab.noType);
    }
    
    public void visit (DesignatorListDeclarationMultiple listDeclarationMultiple) { // - ime: elemNiza niz[], niz-kind Array
    	Obj desni = listDeclarationMultiple.getDesignatorList().obj ;
    	Obj levi = listDeclarationMultiple.getDesignatorElement().obj;
    	
    	if(desni.getName().equals("elemNiza") && levi.getName().equals("elemNiza")) { 
    		report_error("Greska: ne moze niz niza", listDeclarationMultiple);
    		listDeclarationMultiple.obj=Tab.noObj;
    		return;
    	}
    	if(desni.getName().equals("elemNiza")) {
    		Obj postoji = Tab.find(levi.getName());
    		if(postoji!=null) {
    		if(postoji.getType().getKind()==Struct.Array) {
    			listDeclarationMultiple.obj= new Obj(Obj.Elem,"elemNiza",postoji.getType().getElemType());
    			return;
    		}
    		else {
    			report_error("Greska na liniji:" + listDeclarationMultiple.getLine() + " trebao je niz za ovo indeksiranje", listDeclarationMultiple);
        		listDeclarationMultiple.obj=Tab.noObj;
        		return;
    		}
    		}
    		else {
    			report_error("Greska na liniji:" + listDeclarationMultiple.getLine() + " ne postoji u tabeli simbola", listDeclarationMultiple);
        		listDeclarationMultiple.obj=Tab.noObj;
        		return;
    		}
    		
    	}
    	
    	////////////////////////////////
    	
    	if(desni == Tab.noObj) {
    		
    		listDeclarationMultiple.obj=levi;
    		return;
    	}
    	else {
    		Obj postojiLevi = Tab.find(levi.getName());
    		if(postojiLevi != null && postojiLevi.getType().getKind()==Struct.Class) {
    			Obj postojiDesniUScope = postojiLevi.getType().getMembersTable().searchKey(desni.getName());
    			if(postojiDesniUScope!= null) {
    				listDeclarationMultiple.obj=levi;
    			}
    			else {
    				report_error("Greska na liniji: " + listDeclarationMultiple.getLine() + " nije u scope-u date strukture", listDeclarationMultiple);
    				listDeclarationMultiple.obj=Tab.noObj;
    			}
    		}
    		else {
    			report_error("Greska na liniji: " + listDeclarationMultiple.getLine() + " nije tipa strukture ili ne postoji u tabeli simbola ", listDeclarationMultiple);
				listDeclarationMultiple.obj=Tab.noObj;

    		}
    	}
    }
    
    public void visit (NoDesignatorListDeclarationMultiple noListDeclarationMultiple) {
    	noListDeclarationMultiple.obj = Tab.noObj;
        
    }
    /*
    //ovo nema veze sa vezom
    public void visit(DesignatorDecl designator){
    	Obj obj = Tab.find(designator.getDesignatorName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getDesignatorName()+" nije deklarisano! ", null);
    	}
    	designator.obj = obj;
    }
    

    public void visit(DesignatorWithActPars funcCall){
    	Obj func = funcCall.getOptionalActPars().obj;
    	if(Obj.Meth == func.getKind()){
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
			funcCall.struct = func.getType();
    	}else{
			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			funcCall.struct = Tab.noType;
    	}
    }
    */
    
    public void visit (NoMulopListDecls noMulopList) {
    	noMulopList.struct = noMulopList.getFactor().struct;
    }
   
    public void visit (MulopListDecls mulopList) {  
    	if(mulopList.getFactor().struct != mulopList.getMulopList().struct) {
    		
    		report_error("Greska na liniji " + mulopList.getLine()+ " : ova mulop lista nema parametre istog tipa! ", null);
    	}
    	//dodati Term i Factor moraju biti tipa int
    	
			mulopList.struct = mulopList.getFactor().struct;	
    
    }
    

    public void visit(Term term){
    	term.struct = term.getMulopList().struct;
    }
    
    public void visit(NoAddopList noAddopList) {
    	noAddopList.struct = noAddopList.getTerm().struct;	
    }
    
    public void visit(AddopListDecl addopList) {
    	if(addopList.getTerm().struct != addopList.getAddopList().struct) {
    		report_error("Greska na liniji " + addopList.getLine()+ " : ova addop lista nema parametre istog tipa! ", null);
    	}
    	
    	//dodati Expr i Term moraju biti tipa int.
    	
    	addopList.struct = addopList.getTerm().struct;	
    }
    
    public void visit(OptionalMinusAdd minusAdd) {
    	if(((Expr)minusAdd.getParent()).getAddopList().struct != Tab.intType) {
    		report_error("Greska na liniji " + minusAdd.getLine()+" : ne moze stajati minus ispred izraza koji nije int!", null);
    	}	
    }
    
    public void visit(Expr expr) {
    	expr.struct=expr.getAddopList().struct;
    }
    
    
    
    
    
    
    //ActPars

    public void visit(OptionalActParsDecl actuals) {
    	if(currentMethod == null) {
			report_error("Greska: ne postoji metoda!", actuals);
    		return;
    	}
    	
    	Collection<Obj> parametersFromSymTab = Tab.find(currentMethod.getName()).getLocalSymbols();
    	ArrayList<Obj> listParametersFromSymTab  = new ArrayList<>(parametersFromSymTab);
    	
    	if(listOfActParams.size()!=currentMethod.getLevel()) {
    		report_error("Greska: Broj formalnih i stvarnih argumenata metode mora biti isti!", actuals);
    		return;
    	}
    	
    	for(int i=0; i<listOfActParams.size(); i++) {
    		if(listOfActParams.get(i).struct.getKind() != listParametersFromSymTab.get(i).getType().getKind()) {
    			report_error("Greska: Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog "
    					+ "argumenta na odgovarajucoj poziciji!", actuals);
    		}
    	}
    	
    	
    	//OVDE JE TREBALO NESTO OKO STEKA, NEMAM POJMA STA
    }
    
    public void visit(NoOptionalActParsDecl noActuals) {
    	Obj methObj = Tab.find(currentMethod.getName());
    	
    	if(methObj == Tab.noObj) {
    		report_error("Greska: ne postoji metoda!", noActuals);
    		return;
    	}
    	else if(methObj.getLevel() > 0) {
    		report_error("Greska: Tip svakog stvarnog argumenta mora biti kompatibilan pri dodeli sa tipom svakog formalnog "
					+ "argumenta na odgovarajucoj poziciji!", noActuals);
    		return;
    	}
    }
  
    
    
    
    
    
    //SingleStatement
    
    public void visit(DoStatementStart doStatementStart) {
		doStatement++;
	}
    
    public void visit(DoStatement doStatementDecl) {
    	doStatement--;
    	
    	if(doStatementDecl.getCondition().struct != boolType) {
    		report_error("Greska: Uslovni izraz Condition mora biti tipa bool.", doStatementDecl);
    	}
	}
    

	public void visit(BreakStatment breakStatment) {
		if (doStatement == 0) {
			report_error("Greska: Iskaz break se moze koristiti samo unutar do-while petlje", breakStatment);
		}
		
		//treba li da naznacim neki prekid ili da dekrementiram do?
	}
	

	public void visit(ContinueStatament continueStatament) {
		if (doStatement == 0) {
			report_error("Greska: Iskaz continue se moze koristiti samo unutar do-while petlje", continueStatament);
		}
		
		//treba li da naznacim neki prekid ili da dekrementiram do?
	}

	public void visit(ReturnExpr returnExpr) {
		if (!returnExpr.getExpr().struct.equals(methodReturnType)) {
			report_error("Greska: Tip neterminala Expr mora biti ekvivalentan povratnom tipu tekuce metode/ globalne funkcije", returnExpr);
		}
		haveReturn = true;
	}

	public void visit(ReturnNoExpr returnNoExpr) {
		if (!methodReturnType.equals(Tab.noType)) {
			report_error("Greska: Tip neterminala Expr mora biti ekvivalentan povratnom tipu tekuce metode/ globalne funkcije (ReturnNoExpr)", returnNoExpr);
		}
		//TREBA LI NESTO ZA VOID??
		haveReturn = true;
	}
	
	public void visit(ReadStatement readStatement) {
		Obj designator = Tab.find(((DesignatorListDecl)readStatement.getDesignator()).getDesignatorName()); //NZM STO JE TRAZIO KAST
		if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error("Greska:  Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.\r\n"
					+ "", readStatement);
		}
		
		if(!(designator.getType().getKind() ==Struct.Int || designator.getType().getKind() ==Struct.Char 
				|| designator.getType().getKind() ==Struct.Bool)) {
			report_error("Greska:   Designator mora biti tipa int, char ili bool", readStatement);
		}
	}

	public void visit(PrintStmt printSatement) {
		printCallCount++;
		
		if(!(printSatement.getExpr().struct.getKind()==Struct.Int || printSatement.getExpr().struct.getKind()==Struct.Char
				|| printSatement.getExpr().struct.getKind()==Struct.Bool))
			report_error("Greska: Expr kod printa mora biti tipa int, char ili bool", printSatement);
	}
	
	public void visit(MatchedStatement statementIfElse) {
	if(statementIfElse.getCondition().struct!= boolType) {
		report_error("Greska: Tip uslovnog izraza Condition mora biti bool. (IfElse)", statementIfElse);
	}
	}
    
	
	
	
	
	
	//DesignatorStatement
	
	public void visit(Inc inc) {
		
		Obj designator = Tab.find(((DesignatorListDecl)inc.getDesignator()).getDesignatorName());
		
		if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error("Greska: Designator mora oznacavati promenljivu, element niza ili polje objekta unutrasnje klase", inc);
		}
		
		if(designator.getType().getKind() != Struct.Int) {
			report_error("Greska:  Designator mora biti tipa int.", inc);
		}
		
		
	}

	public void visit(Dec dec) {
		Obj designator = Tab.find(((DesignatorListDecl)dec.getDesignator()).getDesignatorName());
		
		if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error("Greska: Designator mora oznacavati promenljivu, element niza ili polje objekta unutrasnje klase", dec);
		}
		
		if(designator.getType().getKind() != Struct.Int) {
			report_error("Greska: Designator mora biti tipa int.", dec);
		}
		
	}
	
	public void visit(Assignment assignment) {
		
		Obj designator = Tab.find(((DesignatorListDecl)assignment.getDesignator()).getDesignatorName());
				
    	Struct expresion = assignment.getExpr().struct;
    	
    	if(designator.getKind() != expresion.getKind()) {
    		report_error("Greska: Tip neterminala Expr mora biti kompatibilan pri dodeli sa tipom neterminala Designator", assignment);
    	}
    	
    	if (!(designator.getKind() == Obj.Fld || designator.getKind() == Obj.Elem || designator.getKind() == Obj.Var)) {
			report_error("Greska: Designator mora oznacavati promenljivu, element niza ili polje unutar objekta", assignment);
		}
	}


	
	
	
	

    
    
	
	//FormPars
	
	public void visit(FormParsDecl formParsDecl) {
	
		Obj obj = Tab.find(formParsDecl.getNameForm());
		
		if (obj == Tab.noObj || obj.getLevel() == 0) {
			Obj inserted = Tab.insert(Obj.Var, formParsDecl.getNameForm(),
					formParsDecl.getOptionalSquareMethodFormPars().struct);
			
			inserted.setFpPos(Tab.currentScope.getnVars());
			currentMethodParams.add(inserted);
		} else {
			errorDetected = true;
			report_error("Greska: Vec je definisan ovaj form param", formParsDecl);
		}
	}
	
	
////NE VIDIII MI OVE KLASE!!!!!!!!!!!!!!!!!!
	
//	public void visit(SquareMethodFormPars squareMethodFormPars) {
//		squareMethodFormPars.struct = new Struct(Struct.Array, typeGlobalVar);
//	}
//
//	public void visit(NoSquareMethodFormPars noSquareMethodFormPars) {
//		noSquareMethodFormPars.struct = typeGlobalVar;
//	}


	
	
	
	
	
//VarDecl
	
	public void visit(OnlyVariableDecl localVar) {
		Obj obj = Tab.currentScope().findSymbol(localVar.getVarName());
		if (obj == Tab.noObj) //ILI !=
			report_error("Greska: Vec deklarisana!", localVar);

		else {
			if(typeGlobalVar!=localVar.obj.getType()) //NZM MOZE LI OVAKO
				report_error("Greska: Los tip promenljive!", localVar);
			else {

				varDeclCount++;

				localVar.obj = Tab.insert(Obj.Var, localVar.getVarName(), typeGlobalVar);
				localVar.obj.getType().setElementType(Tab.noType);

			}
		}

	}

	public void visit(VariableDecl localArr) {
		Obj obj = Tab.currentScope().findSymbol(localArr.getVarName());
		if (obj == Tab.noObj)
			report_error("Greska: Vec deklarisano!", localArr);
		else {
			if (typeGlobalVar!=localArr.obj.getType())
				report_error("Greska: Los tip promenljive!", localArr);
			else {

				
				localArr.obj = Tab.insert(Obj.Var, localArr.getVarName(), new Struct(Struct.Array));
				localArr.obj.getType().setElementType(typeGlobalVar);

			}
		}
	}   
    
    
    
    
    
    
    
    
    //
    public void visit(NoConditionListDecl noConditionList) {
    	noConditionList.struct=noConditionList.getCondTerm().struct;
    }
    public void visit(ConditionListDecl conditionListDec) {
    	if(conditionListDec.getCondTerm().struct == boolType && conditionListDec.getConditionList().struct==boolType){
    		conditionListDec.struct=boolType;
    	}
    	else {
    		report_error("Greska na liniji " + conditionListDec.getLine()+" : trebalo je da bude tipa bool!", conditionListDec);
    		conditionListDec.struct=Tab.noType;
    	}
    }
    public void visit(Condition condition) {
    	condition.struct = condition.getConditionList().struct;
    }
    public void visit(NoCondTermListDecl noCondTermList) {
    	noCondTermList.struct=noCondTermList.getCondFact().struct;
    
    }
    
    public void visit(CondTermListDecl condTermList) {
    	if(condTermList.getCondFact().struct == boolType && condTermList.getCondTermList().struct == boolType)
    		condTermList.struct = boolType;  
    	else {
    		report_error("Greska na liniji " + condTermList.getLine()+" : trebalo je da bude tipa bool!", condTermList);
    		condTermList.struct=Tab.noType;
    	}
    }
    
   public void visit(CondTerm condTerm) {
	   condTerm.struct=condTerm.getCondTermList().struct;       
    }
    
    public void visit(ConditionFact conditionFact) {
    	
    	if(conditionFact.getExpr().struct == boolType) {
    		conditionFact.struct = conditionFact.getExpr().struct;
    	}
    	report_error("Greska na liniji " + conditionFact.getLine()+" : trebalo je da bude tipa bool!", conditionFact);
    	conditionFact.struct=Tab.noType;
    }
    
    
    public void visit(ConditionFactWithRelopExpr conditionFactWithRelopExpr) {
    	if(conditionFactWithRelopExpr.getExpr().struct.getKind() != conditionFactWithRelopExpr.getExpr1().struct.getKind()) {
    		report_error("Greska na liniji " + conditionFactWithRelopExpr.getLine()+" : tipovi nisu kompatibilni!", conditionFactWithRelopExpr);
    	}
    	if((conditionFactWithRelopExpr.getExpr().struct.getKind()==Struct.Array)||(conditionFactWithRelopExpr.getExpr1().struct.getKind()==Struct.Array)||(conditionFactWithRelopExpr.getExpr().struct.getKind()==Struct.Class)||(conditionFactWithRelopExpr.getExpr1().struct.getKind()==Struct.Class)) {
    		if((conditionFactWithRelopExpr.getRelop().equals("!="))||(conditionFactWithRelopExpr.getRelop().equals("=="))) {
    			conditionFactWithRelopExpr.struct= boolType;
    		}
    		else {
        		report_error("Greska na liniji " + conditionFactWithRelopExpr.getLine()+" : ovi relacioni operatori nisu kompatibilni za ovaj tip promenljivih!", conditionFactWithRelopExpr);
        		conditionFactWithRelopExpr.struct=Tab.noType;
    		}
    	}
    }
    
    //
    
    public void visit(DesignatorWithActPars designatorWithActPars) {
    	if(designatorWithActPars.getOptionalActPars() instanceof NoOptionalActParsDecl)
			return;
    	if(designatorWithActPars.getDesignator().obj.getKind() != Obj.Meth) {
    		report_error("Greska na liniji " + designatorWithActPars.getLine()+" : ovaj parametar je trebalo da bude metoda", designatorWithActPars);
			return;
		}
    	
    }
    
    //
    public void visit(Var factorDesignator) {
    	if (factorDesignator.getDesignator().obj == Tab.noObj)
    		factorDesignator.struct = Tab.noType;
		else
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();

	}
   
    
    //
    
    public void visit(NewFactorExprOpt newOpt) {
    	if(newOpt.getExpr().struct.getKind() != Struct.Int)
    		report_error("Greska na liniji: " + newOpt.getLine() + " expr mora biti tipa int ", newOpt);		
    }
    
    public void visit(NewFactor newFactor) {
    	
    	if(newFactor.getNewFactorExprOptional() instanceof NoNewFactorExprOpt) {
    		if(newFactor.getType().struct.getKind() != Struct.Class){
    			report_error("Greska na liniji: " + newFactor.getLine() + " ovo nije tipa strukture ", newFactor);	
    			return;
    		}
    	}
		newFactor.struct=newFactor.getType().struct; //NISAM SIGURNA STA POSTIZEM OVIM
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

    
    /*
    
    public void visit(TermExpr termExpr){
    	termExpr.struct = termExpr.getTerm().struct;
    }
    
    public void visit(AddExpr addExpr){
    	Struct te = addExpr.getExpr().struct;
    	Struct t = addExpr.getTerm().struct;
    	if(te.equals(t) && te == Tab.intType){
    		addExpr.struct = te;
    	}else{
			report_error("Greska na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje.", null);
			addExpr.struct = Tab.noType;
    	}
    }
    
    public void visit(Const cnst){
    	cnst.struct = Tab.intType;
    }
    
    public void visit(Var var){
    	var.struct = var.getDesignator().obj.getType();
    }
    
    public void visit(ReturnExpr returnExpr){
    	returnFound = true;
    	Struct currMethType = currentMethod.getType();
    	if(!currMethType.compatibleWith(returnExpr.getExpr().struct)){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(Assignment assignment){
    	if(!assignment.getExpr().struct.assignableTo(assignment.getDesignator().obj.getType()))
    		report_error("Greska na liniji " + assignment.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ", null);
    }
    
    */
    public boolean passed(){
    	return !errorDetected;
    }
   
}
