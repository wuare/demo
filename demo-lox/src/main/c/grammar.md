```bnf
statement      → exprStmt
               | forStmt
               | ifStmt
               | printStmt
               | returnStmt
               | whileStmt
               | block ;

declaration    → classDecl
               | funDecl
               | varDecl
               | statement ;
```