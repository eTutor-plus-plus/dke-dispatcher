grammar NF;

/*
 * Note: I'm including the ANTLR license text, as per the BSD license's requirements (Gerald Wimmer, 2024-01-06):
 *
 * ANTLR 4 License
 * [The BSD License]
 * Copyright (c) 2012 Terence Parr and Sam Harwell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *  - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  - Neither the name of the author nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

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

relationSetSubmission returns [Set<IdentifiedRelation> relations]                              // start rule
    @init {
        $relations = new HashSet<>();
    } :
        relation {$relations.add($relation.parsedRelation);} (';' relation {$relations.add($relation.parsedRelation);})* EOF ;
relation returns [IdentifiedRelation parsedRelation]
    @init {
        $parsedRelation = new IdentifiedRelation();
    } :
        relationId {try {$parsedRelation.setID($relationId.idString);} catch(Exception e) { e.printStackTrace();}} ':' '(' attributeSet {$parsedRelation.setAttributes($attributeSet.attributes);} ')' '->' '(' ( functionalDependencySet {$parsedRelation.setFunctionalDependencies($functionalDependencySet.functionalDependencies);} )? ')' '#' '(' keySet {$parsedRelation.setMinimalKeys($keySet.keys);} ')' ;
relationId returns [String idString]:
        AlphaNumericChain {$idString = $AlphaNumericChain.text;} ;

keySetSubmission returns [Set<Key> keys]
    @init {
            $keys = new HashSet<>();
        } :
            keySet {$keys.addAll($keySet.keys);} EOF ;  // start rule

keySet returns [Set<Key> keys]
    @init {
        $keys = new HashSet<>();
    } :
        key {$keys.add($key.keyObject);} (';' key {$keys.add($key.keyObject);})* ;
key returns [Key keyObject]
    @init {
        $keyObject = new Key();
    } :
        attributeSet {$keyObject.addAllAttributes($attributeSet.attributes);} ;

normalFormSubmission returns [NormalformDeterminationSubmission submission]
    @init {
        $submission = new NormalformDeterminationSubmission();
    } :
        normalForm {$submission.setOverallLevel($normalForm.level);} '.' (normalFormViolationSet {$submission.setNormalformViolations($normalFormViolationSet.violations); } )? EOF ;                    // start rule
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
normalFormSpecification returns [NormalformLevel level] :
    normalForm {$level = $normalForm.level;} EOF; // only used for creating exercise specifications in the platform frontend

normalForm returns [NormalformLevel level] :
        '1NF' {$level = NormalformLevel.FIRST;}
    |   '2NF' {$level = NormalformLevel.SECOND;}
    |   '3NF' {$level = NormalformLevel.THIRD;}
    |   'BCNF' {$level = NormalformLevel.BOYCE_CODD;} ;

functionalDependencySetSubmission returns [Set<FunctionalDependency> functionalDependencies]  // start rule
    @init {
        $functionalDependencies = new HashSet<>();
    } :
        functionalDependencySet {$functionalDependencies.addAll($functionalDependencySet.functionalDependencies);} EOF ;

functionalDependencySet returns [Set<FunctionalDependency> functionalDependencies]
    @init {
        $functionalDependencies = new HashSet<>();
    } :
        functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);} (';' functionalDependency {$functionalDependencies.add($functionalDependency.fdObject);})* ;
functionalDependency returns [FunctionalDependency fdObject]
    @init {
        $fdObject = new FunctionalDependency();
    } :
        attributeSet {$fdObject.setLhsAttributes($attributeSet.attributes);} '->' attributeSet {$fdObject.setRhsAttributes($attributeSet.attributes);} ;

attributeSetSubmission returns [Set<String> attributes]
    @init {
        $attributes = new HashSet<>();
    } :
        attributeSet {$attributes.addAll($attributeSet.attributes);} EOF ;  // start rule

attributeSet returns [Set<String> attributes]
    @init {
        $attributes = new HashSet<>();
    } :
        attribute {$attributes.add($attribute.text);} (',' attribute {$attributes.add($attribute.text);})* ;
attribute : AlphaNumericChain ;

// Lexer rules

Integer : Digit+ ;
AlphaNumericChain : Letter (Letter | Digit | '_')* ; // Note: Has to start with letter, to distinguish it from Integer
Digit : '0' .. '9' ;
Letter : 'A' .. 'Z' | 'a' .. 'z';

WhiteSpace : [ \r\t\n]+ -> skip ;