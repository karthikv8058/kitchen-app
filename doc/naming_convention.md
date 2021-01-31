# SmartTONi Naming Convention

Reasons for using a naming convention (as opposed to allowing programmers to choose any character sequence) include the following:

- To reduce the effort needed to read and understand source code
 - To enable code reviews to focus on more important issues than arguing over syntax and naming standards.
 - To enable code quality review tools to focus their reporting mainly on significant issues other than syntax and style preferences. (from wiki)


## camelCase  
    For methods , varables
## PascalCase     
    For class,file names
## Underscores
    For String constants "is_closed"
## Uppercase_Underscores
    For constant REFRESH_RATE,MAX_USERS
## General Points
    
   
- Don't use 'Data', 'Item'  etc (`getInventoryData`,`fetchOrderData` etc.) For class, functions, variables etc.

- use methods name like : 
 1. getInventories() for list / getInventory() for single. 
 2. createInventory()
 3. updateInventory()
 4. deleteInventory()

- // use line comment if necessary
-  Use Proper spacing
- Avoid  Spelling mistakes
- use Proper Types:refer https://www.typescriptlang.org/docs/handbook/basic-types.html , also create model classes if necessary  