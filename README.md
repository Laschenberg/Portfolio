To Whom it May Concern:

This is a collection of my favorite projects. Below I have included a short description of the project, and a note of how much code from each file is mine.

Travling Salesman Project:
This is a Java project that I worked on for my Evolutionary Computation class. It's a classic problem, but I enjoyed going the extra mile here and paralellize the solution. There was provided code, but I took very little of it, opting to build a solution basically from scratch in order to structure it for parallelization. 
This code runs the traveling salesmam problem many times and collects statistics on the efficiency of the program. It currently displays graphs at the end for the amount of generations and the amount of time in milliseconds each run took. Five test cases run 64 times each. The test cases are for 10 cities, 15 cities, 20 cities, 25 cities, and 30 cities. The number of cities in each test case can easily be controlled by the switch statement at the top of the Main class.
The CityDistances.txt file and the MapBuilder.java file are the only files that are not mine. 

Mixed Signals Simulator:
This is a C project that I worked on my own time. The code has some structures in it designed with the possibility of paralelization in mind, but the problem never grew to a size where parallelization was necessary. This project was a basic simulation of random players placing stones in a game I am developing called Mixed Signals, and the 28 statistics I collect are various goals for player-stone patterns that I was testing for how difficult they would be to attain. 