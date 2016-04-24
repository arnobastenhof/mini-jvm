Mini-JVM
========

Introduction
------------
Mini-JVM is a meta-circular interpreter-based implementation for a small subset
of the JVM spec. It was written to support a 3-hour hands-on workshop that I
gave, my intention having been to teach my audience some of the basic concepts
by having them implement a JVM themselves. In particular, all of the machine's
runtime data structures are accessed through a single facade by means of which
participants were tasked to implement the instruction set, conveniently
collected on a single interface. A test suite is included as well to help
measure one's progress.

The master branch contains the sources that were provided to the course
participants, where, as explained above, the instruction set's implementation
is missing and with all automated tests still set to be ignored. The full
implementation can be found on the Answers branch.

Its primary use being that of a teaching tool, Mini-JVM was deliberately written
using high-level abstractions to help manage the program-complexity for course
participants. Antero's "Implementing a Java Virtual Machine in the Java
Programming Langauge" (1998) served as a source of inspiration in this regard,
having proposed a similar high-level design based on the different aim of
writing a reference implementation for the JVM spec.

Requirements
------------
* Java SE7 Development Kit
* Maven 2.x

Installation
------------
If you have Git installed, executing the following command will create a
directory `mini-jvm/`, clone the entire repository therein and checkout the
master branch:
```
git clone https://github.com/arnobastenhof/mini-jvm.git
``` 
Alternatively, Github offers the possibility to download a ZIP file containing
the sources from whichever branch is being viewed. E.g., to download the
master branch, run
```
wget https://github.com/arnobastenhof/mini-jvm/archive/master.zip
unzip master.zip
mv mini-jvm-master mini-jvm
```
Next, navigate to the project root and run a build:
```
cd mini-jvm
mvn clean install
```

Usage
-----
After a 1-hour lecture on some of the basic theory behind the JVM, course
participants were tasked with implementing the `InsnVisitor` interface (for
'Instruction Visitor'), with the necessary boilerplate already provided in
`InsnVisitorImpl`. In so doing, they could make use of the operations exposed
by the `JvmFacade` interface. Progress could be tested by removing the
`@Ignore` annotations above individual tests in `MiniJvmTest`. In addition, they
could test their answers by comparing them with those provided in the Answers
branch.

Besides through automated tests, participants could test their work as well by
running the generated JAR on a class file. Since only a limited portion of the
JVM's instruction set is covered, however, not every class file will do. After
having run `mvn clean install` once, several compatible class files may be found
in `target/generated-classes/com/jvm/mini/`. With the JAR file found in
`target/`, one may thus run, for example,
```
java -jar target/mini-jvm-0.0.1-SNAPSHOT.jar target/generated-classes/com/jvm/mini/Arithmetic
```
As output, one is presented with a modest debugging trace, provided all of the
necessary instructions have been correctly implemented. For example, the above
command should result in the following output:
```
                                        BIPUSH 2
              I                    2    BIPUSH 3
             II                    3    IMUL 
              I                    6    BIPUSH 6
             II                    6    INEG 
             II                   -6    SIPUSH 128
            III                  128    ISUB 
             II                 -134    IADD 
              I                 -128    RETURN
```
On the right, the instructions that were executed are presented. The number of
items on the operand stack is visually represented on the left by a number
of consecutive `I` symbols, with the item on top of the stack listed to the
right thereto.

Coverage
--------
Given that the audience was not expected to have any experience with assembly
or virtual machines, combined with the 3 hour time restriction, only a very
limited portion of the instruction set was covered, corresponding, roughly, to
the basic arithmetic operations on the integer and long datatypes, comparisons
and jumps, as well as the necessary instructions for manipulating local
variables. In particular, method calls and reference types are not supported.
