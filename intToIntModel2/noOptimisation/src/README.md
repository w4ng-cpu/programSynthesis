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


LINE 3
    TEST 1, 6 NODES (FOR SPEED)
        NODE 1:
            ________________________________________________
            Time to search a StatementsList permutations: 5618ms
            Number generated: 801
            Number for next: 0
            Number dropped: 801
            Number attemped to compile: 1603
            Number failed to compile: 1
            AVG Compile Time: 3ms
            Memory used up: 211872568
            ________________________________________________


            Time spent on line: 39099s
            Time to gc: 17ms
            Memory increase since line: 1051704
            Line: 3/3
            Node: 18198/18199

            ________________________________________________
            Time to search a StatementsList permutations: 14062ms
            Number generated: 1453
            Number for next: 0
            Number dropped: 1453
            Number attemped to compile: 4360
            Number failed to compile: 2739
            AVG Compile Time: 3ms
            Memory used up: 169313088
            ________________________________________________


            Time spent on line: 39113s
            Time to gc: 18ms
            Memory increase since line: 839208


            ________________________________________________
            Time to search a line: 39113s
            Number generated: 6249955
            Number for next: 0
            Number dropped: 6249955
            Number attemped to compile in this line: 6833362
            Number failed to compile: 258699
            Memory used up: 839208
            ________________________________________________
            
        NODE 2:
________________________________________________
Time to search a StatementsList permutations: 1743ms
Number generated: 325
Number for next: 0
Number dropped: 325
Number attemped to compile: 326
Number failed to compile: 1
AVG Compile Time: 5ms
Memory used up: 33112208
________________________________________________


Time spent on line: 35408s
Time to gc: 19ms
Memory increase since line: 609312
Line: 3/3
Node: 17549/17550

________________________________________________
Time to search a StatementsList permutations: 8028ms
Number generated: 801
Number for next: 0
Number dropped: 801
Number attemped to compile: 1603
Number failed to compile: 630
AVG Compile Time: 5ms
Memory used up: 56412264
________________________________________________


Time spent on line: 35417s
Time to gc: 21ms
Memory increase since line: 777072


________________________________________________
Time to search a line: 35417s
Number generated: 5729454
Number for next: 0
Number dropped: 5729454
Number attemped to compile in this line: 5790258
Number failed to compile: 51516
Memory used up: 777072
________________________________________________

            NODE 3
________________________________________________
Time to search a StatementsList permutations: 1401ms
Number generated: 325
Number for next: 0
Number dropped: 325
Number attemped to compile: 326
Number failed to compile: 1
AVG Compile Time: 4ms
Memory used up: 78154080
________________________________________________


Time spent on line: 35484s
Time to gc: 15ms
Memory increase since line: 862232
Line: 3/3
Node: 17549/17550

________________________________________________
Time to search a StatementsList permutations: 6512ms
Number generated: 801
Number for next: 0
Number dropped: 801
Number attemped to compile: 1603
Number failed to compile: 630
AVG Compile Time: 4ms
Memory used up: 57941024
________________________________________________


Time spent on line: 35490s
Time to gc: 21ms
Memory increase since line: 952416


________________________________________________
Time to search a line: 35490s
Number generated: 5729454
Number for next: 0
Number dropped: 5729454
Number attemped to compile in this line: 5790258
Number failed to compile: 51516
Memory used up: 952416
________________________________________________

            NODE 4
________________________________________________
Time to search a StatementsList permutations: 1964ms
Number generated: 325
Number for next: 0
Number dropped: 325
Number attemped to compile: 326
Number failed to compile: 1
AVG Compile Time: 6ms
Memory used up: 52557936
________________________________________________


Time spent on line: 35330s
Time to gc: 24ms
Memory increase since line: 730912
Line: 3/3
Node: 17549/17550

________________________________________________
Time to search a StatementsList permutations: 8694ms
Number generated: 801
Number for next: 0
Number dropped: 801
Number attemped to compile: 1603
Number failed to compile: 630
AVG Compile Time: 5ms
Memory used up: 130465608
________________________________________________


Time spent on line: 35338s
Time to gc: 23ms
Memory increase since line: 813744


________________________________________________
Time to search a line: 35338s
Number generated: 5729454
Number for next: 0
Number dropped: 5729454
Number attemped to compile in this line: 5790258
Number failed to compile: 51516
Memory used up: 813744
________________________________________________

            NODE 5
________________________________________________
Time to search a StatementsList permutations: 1312ms
Number generated: 325
Number for next: 0
Number dropped: 325
Number attemped to compile: 326
Number failed to compile: 1
AVG Compile Time: 4ms
Memory used up: 113504728
________________________________________________


Time spent on line: 35927s
Time to gc: 15ms
Memory increase since line: 867560
Line: 3/3
Node: 17549/17550

________________________________________________
Time to search a StatementsList permutations: 6009ms
Number generated: 801
Number for next: 0
Number dropped: 801
Number attemped to compile: 1603
Number failed to compile: 630
AVG Compile Time: 3ms
Memory used up: 7147216
________________________________________________


Time spent on line: 35933s
Time to gc: 15ms
Memory increase since line: 868584


________________________________________________
Time to search a line: 35933s
Number generated: 5729454
Number for next: 0
Number dropped: 5729454
Number attemped to compile in this line: 5790258
Number failed to compile: 51516
Memory used up: 868584
________________________________________________


            Node 6:
________________________________________________
Time to search a StatementsList permutations: 1582ms
Number generated: 325
Number for next: 0
Number dropped: 325
Number attemped to compile: 326
Number failed to compile: 1
AVG Compile Time: 4ms
Memory used up: 41220936
________________________________________________


Time spent on line: 35458s
Time to gc: 18ms
Memory increase since line: 832264
Line: 3/3
Node: 17549/17550

________________________________________________
Time to search a StatementsList permutations: 7276ms
Number generated: 801
Number for next: 0
Number dropped: 801
Number attemped to compile: 1603
Number failed to compile: 630
AVG Compile Time: 4ms
Memory used up: 146755080
________________________________________________


Time spent on line: 35466s
Time to gc: 19ms
Memory increase since line: 746792


________________________________________________
Time to search a line: 35466s
Number generated: 5729454
Number for next: 0
Number dropped: 5729454
Number attemped to compile in this line: 5790258
Number failed to compile: 51516
Memory used up: 746792
________________________________________________




