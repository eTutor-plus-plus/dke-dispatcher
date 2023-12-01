grammar NF;

@header {
package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.HashSet;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
}

relationSet returns [Set<IdentifiedRelation> relations]                              // start rule
    @init {
        $relations = new HashSet<>();
    } :
        relation {$relations.add($relation.parsedRelation);} (';' relation {$relations.add($relation.parsedRelation);})* ;
relation returns [IdentifiedRelation parsedRelation]
    @init {
        $parsedRelation = new IdentifiedRelation();
    } :
        relationId {try {$parsedRelation.setID($relationId.text);} catch(Exception e) { e.printStackTrace();}} ':' '(' attributeSet {$parsedRelation.setAttributes($attributeSet.attributes);} ')' '(' keySet {$parsedRelation.setMinimalKeys($keySet.keys);} ')' '(' functionalDependencySet {$parsedRelation.setFunctionalDependencies($functionalDependencySet.functionalDependencies);} ')' ;
relationId: 'R' Integer ('.' Integer)? ; // Todo: If the fact that the start is indistinguishable from AlphaNumericChain causes problems, add some unique prefix symbol (.e.g, '*'). (Gerald Wimmer, 2023-11-30)

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
/*
 * The original eTutor would pass in the IDs of violated functional dependencies, which had to be linked to their ids
 * on the UI side. As this is probably not possible on Moodle (or desired at all), the student instead enters the
 * actual dependency.
*/
normalFormViolation : functionalDependency ':' normalForm ;
normalForm returns [NormalformLevel level] :
        '1' {$level = NormalformLevel.FIRST;}
    |   '2' {$level = NormalformLevel.SECOND;}
    |   '3' {$level = NormalformLevel.THIRD;}
    |   'BCNF' {$level = NormalformLevel.BOYCE_CODD;} ;

// attributeClosure : '(' attributeSet ')+' '=' attributeSet ;                      // start rule // do we need this?

// minimalCover : functionalDependencySet ;                                         // start rule // Do we need this?

functionalDependencySet returns [Set<FunctionalDependency> functionalDependencies]  // start rule
    @init {
        $functionalDependencies = new HashSet<>();
    } :
        functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);} (';' functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);})* ;
functionalDependency returns [FunctionalDependency fdObject]
    @init {
        $fdObject = new FunctionalDependency();
    } :
        attributeSet {$fdObject.setLhsAttributes($attributeSet.attributes);} '-' ('.')? '>' attributeSet {$fdObject.setRhsAttributes($attributeSet.attributes);} ;

attributeSet returns [Set<String> attributes]
    @init {
        $attributes = new HashSet<>();
    } :
        attribute {$attributes.add($attribute.text);} (',' attribute {$attributes.add($attribute.text);})* ;
attribute : AlphaNumericChain ;

AlphaNumericChain : AlphaNumericChar+ ;
AlphaNumericChar : Digit | Letter ;
Integer : Digit+ ;
Digit : '0' .. '9' ;
Letter : 'A' .. 'Z' | 'a' .. 'z' ;

WhiteSpace : [ \r\t\n]+ -> skip ;