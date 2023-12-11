grammar NF;

@header {
package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import at.jku.dke.etutor.modules.nf.NormalformDeterminationSubmission;
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
        relationId {try {$parsedRelation.setID($relationId.idString);} catch(Exception e) { e.printStackTrace();}} ':' '(' attributeSet {$parsedRelation.setAttributes($attributeSet.attributes);} ')' '(' keySet {$parsedRelation.setMinimalKeys($keySet.keys);} ')' '(' functionalDependencySet {$parsedRelation.setFunctionalDependencies($functionalDependencySet.functionalDependencies);} ')' ;
relationId returns [String idString]:
        '*' Integer {$idString = $Integer.text;}; // Note: As we needn't specify how we got to the solution, neither is there a need for subindices. ; // ('.' Integer)? ;

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

normalFormSubmission returns [NormalformDeterminationSubmission submission]
    @init {
        $submission = new NormalformDeterminationSubmission();
    } :
        normalForm {$submission.setOverallLevel($normalForm.level);} '.' (normalFormViolationSet {$submission.setNormalformViolations($normalFormViolationSet.violations); } )? ;                    // start rule
normalFormViolationSet returns [Map<FunctionalDependency, NormalformLevel> violations]
    @init {
        $violations = new HashMap<>();
    } :
        normalFormViolation {$violations.put($normalFormViolation.funcDependency, $normalFormViolation.level);} (';' normalFormViolation {$violations.put($normalFormViolation.funcDependency, $normalFormViolation.level);} )* ;
/*
 * The original eTutor would pass in the IDs of violated functional dependencies, which had to be linked to their ids
 * on the UI side. As this is probably not possible on Moodle (or desired at all), the student instead enters the
 * actual dependency in String form.
*/
normalFormViolation returns [FunctionalDependency funcDependency, NormalformLevel level] :
    functionalDependency {$funcDependency = $functionalDependency.fdObject;} ':' normalForm {$level = $normalForm.level;} ;
normalForm returns [NormalformLevel level] :
        '1NF' {$level = NormalformLevel.FIRST;}
    |   '2NF' {$level = NormalformLevel.SECOND;}
    |   '3NF' {$level = NormalformLevel.THIRD;}
    |   'BCNF' {$level = NormalformLevel.BOYCE_CODD;} ;

// attributeClosure : '(' attributeSet ')+' '=' attributeSet ;                      // start rule // Do we need this?

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

// Lexer rules

Integer : Digit+ ;
AlphaNumericChain : Letter (Letter | Digit | '_'{})* ; // Note: Has to start with letter, to distinguish it from Integer
Digit : '0' .. '9' ;
Letter : 'A' .. 'Z' | 'a' .. 'z';

WhiteSpace : [ \r\t\n]+ -> skip ;