grammar NF;

@header {
package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.HashSet;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
}

relationSet : relation (';' relation)* ;                                            // start rule
relation : '(' attributeSet ')' '(' keySet ')' '(' functionalDependencySet ')' ;

keySet returns [Set<Key> keys]
    @init {
        $keys = new HashSet<>();
    } :
        key {$keys.add($key.keyObject);} (';' key {$keys.add($key.keyObject);})* ;                                                           // start rule
key returns [Key keyObject]
    @init {
        $keyObject = new Key();
    } :
        '#' attributeSet {$keyObject.addAllAttributes($attributeSet.attributes);} '#' ;

violatedNormalForm : functionalDependency ':' normalForm ;                          // start rule
normalForm : '1' | '2' | '3' | 'BCNF' ;                                             // start rule

attributeClosure : '(' attributeSet ')+' '=' attributeSet ;                         // start rule

// minimalCover : functionalDependencySet ;                                         // start rule // Do we even need this?

functionalDependencySet returns [Set<FunctionalDependency> functionalDependencies]
    @init {
        $functionalDependencies = new HashSet<>();
    } :
        functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);} (';' functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);}) ;         // start rule
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
AlphaNumericChar : Integer | Letter ;
Integer : '0' .. '9' ;
Letter : 'A' .. 'Z' | 'a' .. 'z' ;

WhiteSpace : [ \r\t\n]+ -> skip ;