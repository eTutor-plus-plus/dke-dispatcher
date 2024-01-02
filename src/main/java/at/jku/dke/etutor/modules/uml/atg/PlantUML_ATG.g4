grammar PlantUML_ATG;

// Parser Rules
classDiagram: '@startuml' (classDefinition | relationship | multiRelationship | constraints | note | noteConnection |association)* '@enduml';

association: '(' className ',' className ')' '..' className;

noteConnection: noteName '..' multiRelationshipName;

constraints: '(' className ',' className ')' '..' '('className ',' className ')' ':' '{'constrainttype'}';

constrainttype: ('disjoint'|'overlapping'|('Teilmenge' labelMultiplicity)|('Ungleich' labelMultiplicity?));

note:'note' '"'noteText '"' 'as' noteName;

noteName: Identifier;

noteText: Identifier;
classDefinition: visibility? abstractModifier? 'class' className ('extends' parentClassName)? '{' (attribute*) '}';
multiRelationship: 'diamond' multiRelationshipName;

moreDefinitions: '{ID(' attribute (',' attribute)? ')}';
attribute:  attributeName attributeModifier?|moreDefinitions;
attributeModifier: '{ID}';
relationship: participant relationTyp participant (':' label)? labelMultiplicity?;
relationTyp: ('*--'|'--' | '<--' | '---|>' | '<|--');
participant: participantMultiplicity? className participantMultiplicity?;

participantMultiplicity: '"'('*'|cardinality|(cardinality'..'cardinality))'"';

// Lexer Rules
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
className: Identifier;
parentClassName: Identifier;
attributeName: Identifier;
label: Identifier+;
multiRelationshipName: Identifier;
//##prob absolete## dataType: Identifier;
labelMultiplicity: ('*' | '?' | '+'|'>'|'<')?;
cardinality: Integer;
// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;
Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
Integer : [0-9]+;
// Parser entry point
start: classDiagram;
