INT-INT Model 1 Equivalent
0 Optimisations

TRYCOMPILE is the number of times we try to compile, should be more than the numbers of GENERATED, because of multiple returns per generated

1LINE:

    ________________________________________________
    Time to search a StatementsList permutations: 2565ms
    Number generated: 325
    Number for next: 325
    Number dropped: 0
    Number attemped to compile: 326
    Number failed to compile: 1
    AVG Compile Time: 7ms
    ________________________________________________




    ________________________________________________
    Time to search a first line: 2s
    Number generated: 325
    Number for next: 325
    Number dropped: 0
    Number attemped to compile: 326
    Number failed to compile: 1
    Memory used up: 106948096
    ________________________________________________


2LINES:

    TEST1

        Node: 323/325

        ________________________________________________
        Time to search a StatementsList permutations: ####################
        Number generated: 325
        Number for next: 325
        Number dropped: 0
        Number attemped to compile: 326
        Number failed to compile: 1
        AVG Compile Time: 3ms
        Memory used up: -100509920
        ________________________________________________


        Node: 324/325

        ________________________________________________
        Time to search a StatementsList permutations: #################
        Number generated: 801
        Number for next: 649
        Number dropped: 152
        Number attemped to compile: 1603
        Number failed to compile: 630
        AVG Compile Time: 3ms
        Memory used up: -34147040
        ________________________________________________




        ________________________________________________
        Time to search a line: 427s
        Number generated: 106101
        Number for next: 105949
        Number dropped: 152
        Number attemped to compile in this line: 107227
        Number failed to compile: 954
        Memory used up: 53762992
        ________________________________________________
    
    TEST2

        Node: 323/325

        ________________________________________________
        Time to search a StatementsList permutations: 1163ms
        Number generated: 325
        Number for next: 0
        Number dropped: 325
        Number attemped to compile: 326
        Number failed to compile: 1
        AVG Compile Time: 3ms
        Memory used up: -171869456
        ________________________________________________


        Node: 324/325

        ________________________________________________
        Time to search a StatementsList permutations: 5399ms
        Number generated: 801
        Number for next: 0
        Number dropped: 801
        Number attemped to compile: 1603
        Number failed to compile: 630
        AVG Compile Time: 3ms
        Memory used up: -47772432
        ________________________________________________




        ________________________________________________
        Time to search a line: 391s
        Number generated: 106101
        Number for next: 0
        Number dropped: 106101
        Number attemped to compile in this line: 107227
        Number failed to compile: 954
        Memory used up: -17265008
        ________________________________________________


        Node Ready!

    TEST 3:

        GROWING
        Time spent on line: 403s
        Time to gc: 22ms
        Memory increase since line: 10976560
        Line: 2/3
        Node: 324/325

        ________________________________________________
        Time to search a StatementsList permutations: 5554ms
        Number generated: 801
        Number for next: 649
        Number dropped: 152
        Number attemped to compile: 1603
        Number failed to compile: 630
        AVG Compile Time: 3ms
        Memory used up: 178068832
        ________________________________________________


        GROWING
        Time spent on line: 409s
        Time to gc: 24ms
        Memory increase since line: 11251360


        ________________________________________________
        Time to search a line: 409s
        Number generated: 106101
        Number for next: 105949
        Number dropped: 152
        Number attemped to compile in this line: 107227
        Number failed to compile: 954
        Memory used up: 11251360
        ________________________________________________


        Line: 3/3
        Node: 0/105949

        ________________________________________________
        Time to search a StatementsList permutations: 1191ms
        Number generated: 325
        Number for next: 0
        Number dropped: 325
        Number attemped to compile: 326
        Number failed to compile: 1
        AVG Compile Time: 3ms
        Memory used up: 211314960

