package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

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

	
	public void visit(VariableDecl varDecl){
		varDeclCount++;
		//report_info("Deklarisana promenljiva "+ varDecl.getVarName(), varDecl);
		//Obj varNode = Tab.insert(Obj.Var, varDecl.getVarName(), varDecl.getType().struct);
	}
	
    public void visit(PrintStmt print) {
		printCallCount++;
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
    //
 
    public void visit(OptionalActParsDecl optionalActParsDecl) {
    	if(currentMethod == null) {
			report_error("Greska: nema metode koja se trenutno koristi!", null);
    		return;
    	}
    	
    	/*
    	 * Collection<Obj> real_parameters_from_symbol_table = Symbol_Table.find(current_method_we_are_using.getName()).getLocalSymbols();
    	ArrayList<Obj> list_helper = new ArrayList<>(real_parameters_from_symbol_table);
    	
    	if(list_of_actual_parameters.size()!=current_method_we_are_using.getLevel()) {
    		report_error("NOT THE SAME NUMBER OF ARGUMENTS (from Actuals) !", actuals);
    		return;a
    	}
    	
    	for(int i=0; i<list_of_actual_parameters.size(); i++) {
    		if(list_of_actual_parameters.get(i).struct.getKind() != list_helper.get(i).getType().getKind()) {
    			report_error("NOT THE SAME TYPE OF ARGUMENTS AT ARGUMENT (from Actuals) !" + (i+1), actuals);
    		}
    		else {
    			report_info("TYPE -> " + getTypeAsString(list_of_actual_parameters.get(i).struct.getKind()) + " <- AND -> " + getTypeAsString(list_helper.get(i).getType().getKind()) + " <-", actuals);
    		}
    	}
    	
    	list_of_function_calls.remove(list_of_function_calls.size()-1); // remove the current method used from the stack of methods
    	if(list_of_function_calls.size()>0) // if there is more methods left 
    		current_method_we_are_using = list_of_function_calls.get(list_of_function_calls.size()-1); // get the previous one
    	
    	stack.remove(stack.size()-1); // remove the current list of actual parameters from the stack of actual parameters
    	if(stack.size()>0) // if there is more lists of actual parameters
    		list_of_actual_parameters = stack.get(stack.size()-1); // get the previous one
    }
    	 */
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
