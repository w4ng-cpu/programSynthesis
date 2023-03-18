Brute forcing program synthesis, programming by example

IntToIntModel2

1. Read-only Integer a + Integer b is staple
2. Logical one liners
3. Useful return
    Return last assigned integer
    Return if last assigned integer interacted with a
4. No compile unproductive line
5. Avoiding convergence language
    Add, Sub, Times, Divide
6. No reassign until used
7. No use until assigned


//attempt compilation and test, we do this before checks because optimisations might cause us to skip compile

                //QUESTION SHOULD WE DO THE CHECKS BEFORE COMPILING AND TESTING?
                //FUTURE SPAWNS

                //if compiles and has no runtime errors
                //check new assign/return to have interacted with a if true then ignore
                //else check 
                //something else that interacted with a
                // if position 2 or 4 contains a
                //StatementsList variablesInterA hashmap boolean true for statement that interacted with expression a