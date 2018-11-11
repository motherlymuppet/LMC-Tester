Magnus' batch tester is awful. This one is better.
Nobody listened when I told them they should write an emulator so I did.
It's pretty easy to edit the test generator to fit your needs. (Future years?)

Tests all values 0..999, to/from bases 0..15
Expects a file called lmc.lmc in the same folder.
The lmc.lmc file should be created by opening the lmc, compiling your code, then doing file > save in the main window (not the assembly window)

It only tests values which are possible in the from base (i.e. won't input 666 base 5)

It expects errors in the following situations:

Base < 2
Base > 10
Output value > 3 digits long

It generates the test cases on the fly and is multithreaded. It took 0.03 seconds to test all cases on my pc.

Errors appear in the terminal. If you have lots of errors, it will be hard to read. That's your job to fix.

Source code is on gituhb: https://github.com/motherlymuppet/LMC-Tester

Launch with java -jar tester.jar.