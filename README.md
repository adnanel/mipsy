# mipsy - A MIPS simulator

Work in progress

# Supported instructions

Instruction |      Example     | Working 
------------|------------------|--------
Add         |  add $s1,$s2,$s3 |   YES   
AddI        |  addi $s1,$s2,50 |   YES   
And         |  and $s1,$s2,$s3 |   YES   
AndI        |                  |   NO     
Beq         |                  |   NO   
Bne         |                  |   NO   
Halt        |  halt            |   YES   
J           |                  |   NO   
Jal         |                  |   NO   
Jr          |                  |   NO   
Lb          |                  |   NO   
Lbu         |                  |   NO   
Lh          |                  |   NO   
Lhu         |                  |   NO   
Ll          |                  |   NO   
Lui         |                  |   NO   
Lw          |  lw $s1, 0($s2)  |   YES  
Nor         |                  |   NO   
Or          |                  |   NO   
OrI         |                  |   NO   
Sb          |                  |   NO   
Sc          |                  |   NO   
Sh          |                  |   NO   
Sll         |                  |   NO   
Slt         |                  |   NO   
SltI        |                  |   NO   
SltIU       |                  |   NO   
Srl         |                  |   NO   
Sub         |                  |   NO   
Sw          |                  |   NO   

Total:   31
Working: 5

# Input files 

MIPSy supports importing source, data and register values from an input file. 
An example input file is given below:

```
# Comments are supported and start with the # character.

# The .data block defines the memory state. Values are defined as ADDRESS = VALUE 
# where both address and value can be given in hex or dec.

.data
0x0 = 0
0x1 = 0xFF
4 = 4
5 = 0x5
0x6 = 0x6

# The .regs block defines the register values. Values are defined as REG_NAME = VALUE 
# where the value can be given in both hex and dec.

.regs
$s0 = 0
$s1 = 1
$s2 = 0x2
$s3 = 50

# The .code segment defines the source code to load.

.code
# s1 = s2 + s3
add $s1, $s2, $s3

# Optional. The simulator will read halt when PC doesn't point to a instruction.
halt 

# Note: These 3 segments don't have to be in a particular order, also they can be fragmented (e.g. you can define
#       multiple code segments, MIPSy will combine them into one while loading)
```
