# mipsy - A simple pipelined MIPS simulator

MIPSy is a (very) simple pipelined MIPS simulator based on the MIPS architecture described in the book "Computer Organization and Design MIPS Edition - 5th Edition" by David Patterson and John Hennessy.

It's basically a Java implementation of all units described mostly in chapters 4.3 and 4.4 of the book.

The GUI is written in JavaFX 2.  
![gui](https://github.com/adnanel/mipsy/raw/master/gui.png "Logo Title Text 1")


# Supported instructions

Instruction |      Example     | Working |    Pseudo   
------------|------------------|---------|--------------
Add         |  add $s1,$s2,$s3 |   YES   |  $s1 = $s2 + $s3
AddI        |  addi $s1,$s2,50 |   YES   |  $s1 = $s2 + 50           
And         |  and $s1,$s2,$s3 |   YES   |  $s1 = band( $s2, $s3 )           
AndI        |  andi $s1,$s2,50 |   YES   |  $s1 = band( $s2, 50 )
Or          |  or $s1,$s2,$s3  |   YES   |  $s1 = $s2 \| $s3            
OrI         |  ori $s1,$s2,10  |   YES   |  $s1 = $s2 \| 10
Lw          |  lw $s1, 0($s2)  |   YES   |  $s1 = MEMORY[ 0 + $s2 ]
Sw          |  sw $s1, 0($s2)  |   YES   |  MEMORY[ 0 + $s2 ] = $s1
Sub         |  sub $s1,$s1,$s2 |   YES   |  $s1 = $s1 - $s2
Nor         |  nor $s1,$s2,$s3 |   YES   |  $s1 = !( $s2 \| $s3 )
Beq         |  beq $s1,$s2,15  |   YES   |  if $s1 == $s2 then pc += 15 * 4           
Slt         |  slt $s1,$s2,$s3 |   YES   |  if $s2 < $s3 then $s1 = 1 else $s1 = 0           
SltI        |  slt $s1,$s2,10  |   YES   |  if $s2 < 10 then $s1 = 1 else $s1 = 0           
Bne         |  bne $s1,$s2,15  |   NO    |  if $s1 != $s2 then pc += 15 * 4          
J           |  j 5             |   NO    |  pc = 4 * 5
Jal         |  jal 15          |   NO    |              
Jr          |  jr $s1          |   NO    |  pc = $s1    
Lb          |  lb $s1, 15($s2) |   NO    |  $s1 = MEMORY[ $s2 + 15 ] & 0xFFFFFF
Lbu         |                  |   NO    |             
Lh          |                  |   NO    |             
Lhu         |                  |   NO    |             
Ll          |                  |   NO    |             
Lui         |                  |   NO    |                        
Sb          |                  |   NO    |             
Sc          |                  |   NO    |             
Sh          |                  |   NO    |             
Sll         |                  |   NO    |             
SltIU       |                  |   NO    |             
Srl         |                  |   NO    |             
Halt        |  halt            |   YES   |             

Total:   30  
Working: 14

# The simulated data path
Below is a picture of the simulated pipeline. It's a slightly modified book version with added labels and a clear distinction of the phases and named temp registers which are used to transfer data between stages.   
![datapath](https://github.com/adnanel/mipsy/blob/master/src/mipsy/ui/ra_mipsy.png?raw=true "Logo Title Text 1")
  
## Not working instructions
All instructions which are currently supported, but don't work as intended *cannot* work with the current architecture design. 
In order to implement them, certain changes to the design need to be applied (as described in the end of chapter 4.4 of the earlier mentioned book) which is planned as soon as I find some free time.


# Hazard detection

Currently, hazards are detected and avoided by stalling the pipeline. 


# Input files 

MIPSy supports importing source, data and register values from an input file. 
An example input file is given below ( for simple examples see /test_programs ):

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

