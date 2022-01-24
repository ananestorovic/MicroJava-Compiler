package rs.ac.bg.etf.pp1;
import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

/********** WHITE SPACES **********/

<YYINITIAL> " " 	{ }
<YYINITIAL> "\b" 	{ }
<YYINITIAL> "\t" 	{ }
<YYINITIAL> "\r\n" 	{ }
<YYINITIAL> "\f" 	{ }


<YYINITIAL> "program"   { return new_symbol(sym.PROGRAM, yytext());}
<YYINITIAL> "return" 	{ return new_symbol(sym.RETURN, yytext()); }
<YYINITIAL> "void"      { return new_symbol(sym.VOID, yytext()); }
<YYINITIAL> "print"     { return new_symbol(sym.PRINT, yytext()); }
<YYINITIAL> "read"      { return new_symbol(sym.READ, yytext()); }

<YYINITIAL> "new"       { return new_symbol(sym.NEW, yytext()); }
<YYINITIAL> "const"     { return new_symbol(sym.CONST, yytext()); }

<YYINITIAL> "extends"   { return new_symbol(sym.EXTENDS, yytext()); }
<YYINITIAL> "record"	{ return new_symbol(sym.RECORD, yytext()); }
<YYINITIAL> "record"	{ return new_symbol(sym.RECORD, yytext()); }
<YYINITIAL> "if"	    { return new_symbol(sym.IF, yytext()); }
<YYINITIAL> "else"	    { return new_symbol(sym.ELSE, yytext()); }
<YYINITIAL> "do"	    { return new_symbol(sym.DO, yytext()); }
<YYINITIAL> "while"	    { return new_symbol(sym.WHILE, yytext()); }
<YYINITIAL> "break"	    { return new_symbol(sym.BREAK, yytext()); }
<YYINITIAL> "continue"	{ return new_symbol(sym.CONTINUE, yytext()); }

<YYINITIAL> "||"	{ return new_symbol(sym.LOGICALOR, yytext()); }
<YYINITIAL> "&&"	{ return new_symbol(sym.LOGICALAND, yytext()); }




<YYINITIAL> "("			{ return new_symbol(sym.LPAREN, yytext()); }
<YYINITIAL> ")"			{ return new_symbol(sym.RPAREN, yytext()); }
<YYINITIAL> "{"         { return new_symbol(sym.LBRACE, yytext()); }
<YYINITIAL> "}"         { return new_symbol(sym.RBRACE, yytext()); }
<YYINITIAL> "["         { return new_symbol(sym.LSQUAREB, yytext()); }
<YYINITIAL> "]"         { return new_symbol(sym.RSQUAREB, yytext()); }

<YYINITIAL> ";"         { return new_symbol(sym.SEMI, yytext()); }
<YYINITIAL> ","         { return new_symbol(sym.COMMA, yytext()); }
<YYINITIAL> ":"			{ return new_symbol(sym.COLON, yytext()); }
<YYINITIAL> "."		    { return new_symbol(sym.DOT, yytext()); }

<YYINITIAL> "="         { return new_symbol(sym.EQUAL, yytext()); }
<YYINITIAL> "+"         { return new_symbol(sym.PLUS, yytext()); }
<YYINITIAL> "-"         { return new_symbol(sym.MINUS, yytext()); }
<YYINITIAL> "*"         { return new_symbol(sym.MUL, yytext()); }
<YYINITIAL> "/"         { return new_symbol(sym.DIV, yytext()); }
<YYINITIAL> "%"         { return new_symbol(sym.MOD, yytext()); }
<YYINITIAL> "=="        { return new_symbol(sym.EQUALTO, yytext()); }
<YYINITIAL> "!="        { return new_symbol(sym.NOTEQUALTO, yytext()); }
<YYINITIAL> ">"         { return new_symbol(sym.GREATER, yytext()); }
<YYINITIAL> ">="        { return new_symbol(sym.GREATEROREQUAL, yytext()); }
<YYINITIAL> "<"         { return new_symbol(sym.LESS, yytext()); }
<YYINITIAL> "<="        { return new_symbol(sym.LESSOREQUAL, yytext()); }
<YYINITIAL> "++"        { return new_symbol(sym.INC, yytext()); }
<YYINITIAL> "--"        { return new_symbol(sym.DEC, yytext()); }

/********** BOOL **********/

<YYINITIAL> ("true"|"false")  				{return new_symbol(sym.BOOL, yytext());}


/********** IDENTIFICATORS **********/

<YYINITIAL> ([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }


/********** NUMBER **********/

[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }


/********** CHAR **********/

<YYINITIAL> '[\x20-\x7E]' 							{return new_symbol(sym.CHAR, new Character(yytext().charAt(1)));}


/********** COMMENTS **********/

<YYINITIAL> "//" 	{yybegin(COMMENT);}
<COMMENT> . 		{yybegin(COMMENT);}
<COMMENT> "\r\n" 	{ yybegin(YYINITIAL); }


/********** ERROR **********/

<YYINITIAL> [^] { System.err.println("Leksicka greska ("+ yytext() + ") u liniji "+ (yyline + 1) + " i u koloni " + (yycolumn + 1)); }



