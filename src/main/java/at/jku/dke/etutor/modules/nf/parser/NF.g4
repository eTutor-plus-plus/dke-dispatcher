grammar NF;

@header {
package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.HashSet;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
}

relationSet : relation (';' relation)* ;                                            // start rule
relation : '(' attributeSet ')' '(' keySet ')' '(' functionalDependencySet ')' ;

keySet returns [Set<Key> keys]                                                      // start rule
    @init {
        $keys = new HashSet<>();
    } :
        key {$keys.add($key.keyObject);} (';' key {$keys.add($key.keyObject);})* ;
key returns [Key keyObject]
    @init {
        $keyObject = new Key();
    } :
        '#' attributeSet {$keyObject.addAllAttributes($attributeSet.attributes);} '#' ;

normalFormSubmission: normalForm '.' (normalFormViolationSet)? ;                    // start rule
normalFormViolationSet : normalFormViolation (';' normalFormViolation)* ;
normalFormViolation : functionalDependency ':' normalForm ;
normalForm returns [NormalformLevel level] :
        '1' {$level = NormalformLevel.FIRST;}
    |   '2' {$level = NormalformLevel.SECOND;}
    |   '3' {$level = NormalformLevel.THIRD;}
    |   'BCNF' {$level = NormalformLevel.BOYCE_CODD;} ;

attributeClosure : '(' attributeSet ')+' '=' attributeSet ;                         // start rule

// minimalCover : functionalDependencySet ;                                         // start rule // Do we even need this?

functionalDependencySet returns [Set<FunctionalDependency> functionalDependencies]  // start rule
    @init {
        $functionalDependencies = new HashSet<>();
    } :
        functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);} (';' functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);}) ;
functionalDependency returns [FunctionalDependency fdObject]
    @init {
        $fdObject = new FunctionalDependency();
    } :
        attributeSet {$fdObject.setLHSAttributes($attributeSet.attributes);} '-' ('.')? '>' attributeSet {$fdObject.setRHSAttributes($attributeSet.attributes);} ;

attributeSet returns [Set<String> attributes]
    @init {
        $attributes = new HashSet<>();
    } :
        attribute {$attributes.add($attribute.text);} (',' attribute {$attributes.add($attribute.text);})* ;
attribute : AlphaNumericChain ;

AlphaNumericChain : AlphaNumericChar+ ;
AlphaNumericChar : Digit | Letter ;
Digit : '0' .. '9' ;
Letter : 'A' .. 'Z' | 'a' .. 'z' ;

WhiteSpace : [ \r\t\n]+ -> skip ;