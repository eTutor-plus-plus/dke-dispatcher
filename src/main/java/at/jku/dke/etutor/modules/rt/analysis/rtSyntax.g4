grammar rtSyntax;
    start: STRING'(''|'(STRING | ATT_BLOCK)'|'(STRING|ATT_BLOCK)?')'(INK|INK_BLOCK)? EOF;

    STRING: [A-Za-z]+;
    ATT_BLOCK: STRING (',' STRING)*;
    INK: ' 'STRING'('STRING') -> 'STRING'('STRING')';
    INK_BLOCK: INK (','INK)*;

