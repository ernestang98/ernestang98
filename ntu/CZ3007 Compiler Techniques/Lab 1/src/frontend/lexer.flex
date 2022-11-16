/* You do not need to change anything up here. */
package lexer;

import frontend.Token;
import static frontend.Token.Type.*;

%%

%public
%final
%class Lexer
%function nextToken
%type Token
%unicode
%line
%column

%{
	/* These two methods are for the convenience of rules to create token objects.
	* If you do not want to use them, delete them
	* otherwise add the code in
	*/

	private Token token(Token.Type type) {
		return token(type, yytext());
	}

	/* Use this method for rules where you need to process yytext() to get the lexeme of the token.
	 *
	 * Useful for string literals; e.g., the quotes around the literal are part of yytext(),
	 *       but they should not be part of the lexeme.
	*/
	private Token token(Token.Type type, String text) {
		return new Token(type, yyline, yycolumn, text);
	}
%}

/* This definition may come in handy. If you wish, you can add more definitions here. */
WhiteSpace = [ ] | \t | \f | \n | \r


%%
/* put in your rules here.    */

// Keywords
"if" { return token(IF); }
"else" { return token(ELSE); }
"while" { return token(WHILE); }
"break" { return token(BREAK); }
"int" { return token(INT); }
"boolean" { return token(BOOLEAN); }
"false" { return token(FALSE); }
"true" { return token(TRUE); }
"import" { return token(IMPORT); }
"module" { return token(MODULE); }
"public" { return token(PUBLIC); }
"return" { return token(RETURN); }
"type" { return token(TYPE); }
"void" { return token(VOID); }

// punctuation symbols
"," { return token(COMMA); }
";"	{ return token(SEMICOLON); }
"["	{ return token(LBRACKET); }
"]"	{ return token(RBRACKET); }
"{"	{ return token(LCURLY); }
"}"	{ return token(RCURLY); }
"("	{ return token(LPAREN); }
")"	{ return token(RPAREN); }

// operators
"+"	{ return token(PLUS); }
"-"	{ return token(MINUS); }
"*"	{ return token(TIMES); }
"/"	{ return token(DIV); }
"="	{ return token(EQL); }
">"	{ return token(GT); }
"<"	{ return token(LT); }
"==" { return token(EQEQ); }
"!=" { return token(NEQ); }
">=" { return token(GEQ); }
"<=" { return token(LEQ); }

// identifier
[a-zA-Z][a-zA-Z0-9_]* { return token(ID); }

// literals
[0-9]+ { return token(INT_LITERAL); }
\"[^\"\n]*\" { return token(STRING_LITERAL, yytext().substring(1, yytext().length() - 1)); }

// Whitespace
{WhiteSpace} {}

/* You don't need to change anything below this line. */
.							{ throw new Error("unexpected character '" + yytext() + "'"); }
<<EOF>>						{ return token(EOF); }