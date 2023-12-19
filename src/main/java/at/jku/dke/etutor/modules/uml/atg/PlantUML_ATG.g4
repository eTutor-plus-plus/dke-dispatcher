grammar PlantUML_ATG;

// Parser Rules
classDiagram: '@startuml' (classDefinition | relationship)* '@enduml';

classDefinition: visibility? abstractModifier? 'class' className ('extends' parentClassName)? '{' (attribute*) '}';
moreDefinitions: '{ID(' attribute (',' attribute)? ')}';
attribute:  attributeName attributeModifier?|moreDefinitions;
attributeModifier: '{ID}';
relationship: participant relationTyp participant (':' label)? labelMultiplicity?;
relationTyp: ('--' | '<--' | '---|>' | '<|--');
participant: participantMultiplicity? className participantMultiplicity?;

participantMultiplicity: '"'('*'|cardinality)'"';

// Lexer Rules
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
className: Identifier;
parentClassName: Identifier;
attributeName: Identifier;
label: Identifier+;
//##prob absolete## dataType: Identifier;
labelMultiplicity: ('*' | '?' | '+'|'>'|'<')?;
cardinality: Integer;
// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;
Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
Integer : [0-9]+;
// Parser entry point
start: classDiagram;
