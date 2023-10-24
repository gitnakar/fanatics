# Goal

Showcase your skills in algorithm design, multithreaded programming, performance tuning and
production quality coding.
# Your Task
Write a Java program that generates a random collection of tasks, finds the solution for each task, ranks the solutions and then prints the top 50 solutions.You should be able to reuse as much of your code as possible for a variety of tasks. Specifically, we ask you to solve 2 types of tasks:

## LongestOnes: find the longest sequence of 1's in a binary vector.
Each task is a vector of randomly generated 1's and 0's. The vector's length is 8K.
The solution for a given vector is the length of the longest subsequence of all 1's. For example, if the vector is "01011001110", the solution is 3 (i.e., the length of "111").
The output of the program is the top 50 solutions, ranked by descending solution length (e.g., 32, 31, 31, 29, .....).
## LongestPrefix: find the longest substring that is a prefix of a given input string.
Each task is a sequence of randomly generated characters. Only 4 characters are used: A, G, C or T. The sequence length is 8K.
To keep things simple, the input string is the constant string "AAGGCCTTAGCTAGCTAAGGAGCTTTTAAAGGG"  
The solution for a given sequence is the length of the longest substring that is a prefix of the input string. For example, if the task sequence is "TAATAAGGCCTCCC", the solution is "AAGGCCT".
The output of the program is the top 50 solutions, ranked by descending solution
length (e.g., "AAGGCCT", "AAGGC", "AAGGC", "AAG", ...)


Please make sure your program execution is deterministic. In other words, given the same input
parameters it should always produce the same output. Your program should take the following command parameters:

<Task type: LonestOnes or LongestPrefix> <# of tasks to generate> <random generator seed>

```bash
E.g., java MySolver LongestOnes 100000 1234567
```


# Estimated Effort
You will probably need to spend a couple hours to come up with a good solution for both  tasks.
We appreciate your time, and you can do as much as you want. However, for the parts you decided not to do, tell us what you would have done given more time.


# What we would like to see:
Production quality code: use proper abstraction with a reasonable amount of comments and error checking.
Performance: utilize as much of your hardware as possible to boost performance.
Analysis: explain your design decisions and tradeoffs, use your tool of choice to show where the time is spent in your code.

# Solution

There are two main parts of the solution 

## Platform 

### Producer Consumer framework
* package `com.rahul.fanatics.orchestration` implements Consumer Producer pattern  to run sequence generation and consumption in parallel fashion.
** Producer - Encapsulates a producer and starts `ProducerRunnable`s in a thread pool
** Consumer - Encapsulates a consumer and runs `ConsumerRunnable`s in a thread pool that it manages
** SharedQueue - shared between producer and consumer thread to producer and consume tasks
** Poison pill - special message on queue to indicate that queue is bounded and there is no more messages to process
** logic to indicate to the main thread that all producers and consumers have run their course and can be shut down  

### Tasks

As required there are two tasks 

* com.rahul.fanatics.tasks.LongestOnesTask
* com.rahul.fanatics.tasks.LongestPrefixTask

Tasks encapsulate logic for
* producing the problem sequence
* solving for the problem sequence 
* managing how results are to be aggregated - in this case , store top n results only 

## Running the solution 

### On command line
* Download the repo 
* On a terminal go to the base folder and run `mvn clean install`
** This will build the jar file in folder `target/`, named as `fanatics-1.0.jar`
* Execute the jar file using 
```
 java -jar target/fanatics-1.0.jar LongestOnes 100000 1234567
```

### In IDE 
* Navigate to main class `com.rahul.fanatics.EntryPoint`
* Run main class with input parameters `LongestOnes 100000 1234567`

### EntryPoint
* Main class that orchestrates consumer-producer. Its    
