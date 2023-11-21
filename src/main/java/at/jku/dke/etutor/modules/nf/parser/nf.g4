grammar nf;

@header {
    package at.jku.dke.etutor.modules.nf.parser ;
}

relationSet : relation (';' relation)* ;                                            // start rule
relation : '(' attributeSet ')' '(' keySet ')' '(' functionalDependencySet ')' ;

keySet : key (';' key)* ;                                                           // start rule
key : '#' attributeSet '#' ;

violatedNormalForm : functionalDependency ':' normalForm ;                          // start rule
normalForm : '1' | '2' | '3' | 'BCNF' ;                                             // start rule

attributeClosure : '(' attributeSet ')+' '=' attributeSet ;                         // start rule

// minimalCover : functionalDependencySet ;                                         // start rule // Do we even need this?

functionalDependencySet : functionalDependency (';' functionalDependency) ;         // start rule
functionalDependency : attributeSet '-' ('.')? '>' attributeSet ;

attributeSet : attribute (',' attribute)* ;
attribute : AlphaNumericChain ;

AlphaNumericChain : AlphaNumericChar+ ;
AlphaNumericChar : Integer | Letter ;
Integer : '0' .. '9' ;
Letter : 'A' .. 'Z' | 'a' .. 'z' ;

WhiteSpace : [ \r\t\n]+ -> skip ;