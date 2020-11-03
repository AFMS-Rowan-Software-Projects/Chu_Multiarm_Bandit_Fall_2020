Known Problems:
This only checks to see if correct flags are used. It assumes a value after, and will break if no value after.
Program architecture becomes sloppy again. All command line flag checks done in Driver

Known Solutions:
Check entire string beforehand for correct flags and length
Abstract code out from Driver into new class called InputManager
