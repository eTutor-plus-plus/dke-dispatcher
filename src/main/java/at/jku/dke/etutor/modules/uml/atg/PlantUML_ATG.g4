grammar PlantUML_ATG;

// Parser Rules
classDiagram: '@startuml' (classDefinition | relationship)* '@enduml';

classDefinition: visibility? abstractModifier? 'class' className ('extends' parentClassName)? '{' attribute* '}';
attribute:  attributeName attributeModifier?;

attributeModifier: '{ID}';
relationship: participant ('--' | '<--' | '---|>' | '<|--') participant (':' label)? multiplicity?;

participant: participantMultiplicity? className participantMultiplicity?;

participantMultiplicity: '"'('*'|cardinality)'"';

// Lexer Rules
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
className: Identifier;
parentClassName: Identifier;
attributeName: Identifier;
label: Identifier;
//##prob absolete## dataType: Identifier;
multiplicity: ('*' | '?' | '+'|'>'|'<')?;

// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;
Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
cardinality: Integer;

// Parser entry point
start: classDiagram;