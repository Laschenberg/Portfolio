To Whom it May Concern:

This is a collection of my favorite projects. Below I have included a short description of the project, and a note of how much code from each file is mine.


### Travling Salesman Project (April 2023):

This is a Java project that I worked on for my Evolutionary Computation class. It's a classic problem, but I enjoyed going the extra mile here and paralellize the solution. There was provided code, but I took very little of it, opting to build a solution basically from scratch in order to structure it for parallelization. 

This code runs the traveling salesmam problem many times and collects statistics on the efficiency of the program. It currently displays graphs at the end for the amount of generations and the amount of time in milliseconds each run took. Five test cases run 64 times each. The test cases are for 10 cities, 15 cities, 20 cities, 25 cities, and 30 cities. The number of cities in each test case can easily be controlled by the switch statement at the top of the Main class.

The CityDistances.txt file and the MapBuilder.java file are the only files that are not mine. 


### Slime Climb (March 2023):

This is a Unity game, developed as a group project I completed with Lizzy Shackman, Michael Fisher, and Andree Vuong for a Software Development class. The project was a simulation of a software development workflow, and we utilized Scrum, Agile, and KanBan theories and documentations to coordinate our project.

The overall concept of the game was a group effort, and many aspects of the game ended up having multiple hands in them, so attributing work is complicated. I initially worked on player movement, but Michael provided an alternitave approach to jumping mechanics that we ended up using. I worked on player death, but Andree was responsible for the game-over screen, and modified the code to work for his purposes. I aquired the assets for the soundtrack initially, but Lizzy ended up choosing a couple different ones to better match the aesthetic of the art assets she added in later. 

The components of the game that are most fully my own are the level-up and power-up mechanics. Michael drew up the initial concept for blocks snapping to a grid when they landed, but the implementation is fully mine. When a full layer of blocks is completed, a random level-up effect is applied. Michael also was originally responsible for the slime spawning, but I modified that section to incorporate power-ups that would ocassionally fall in place of slimes. The ideas behind the effects of the leveling and powering up are mine with the exception of the invulnerability power up, which was a group idea when I initially pitched power-ups.
This game took effort from everyone, and the above summary does not include the full extent of anyone's responsibilities, including my own. 


### Game Element Design with Evolutionary Computation (April - June 2023)

This is a literature review I completed as part of my Evolutionary Computation course. It surveys 26 projects pertaining to creating board or card games using Evolutionary Computation. I originally intended on completing a project of my own in this domain, but realized during my research that such a project was far beyond the scope of a single term. I intend to complete an Honors Project next year to actualize my project.

All the writing in this document is mine. I recieved feedback from professor Vera Kazacova for this draft.


### The Market Cellar Bean-Off(2024):

While this is not a coding project, I thought that it was important to include here. This is a board game that I designed in 2024 specifically to be published in one of Knox College's literary magazines, the Cellar Door. This game can be played with relatively common components and a sheet of scratch paper. It is a dice-drafting roll-and-write game, and, depending on the number and familiarity of the players of the game, can be played relatively quickly. 

This game went through multiple playtests with varied player-counts before I submitted it, and, after submission, it went through an additional workshop with the editors of the magazine. However, at the end of the day, all of the ideas, text, and images in this game are fully my own. 


### Mixed Signals Simulator(February 2022):

This is a C project that I worked on in my own time. The code has some structures in it designed with the possibility of paralelization in mind, but the problem never grew to a size where parallelization was necessary. This project was a basic simulation of random players placing stones in a game I am developing called Mixed Signals, and the 28 statistics I collect are various goals for player-stone patterns that I was testing for how difficult they would be to attain. 

In the game, players do not know the goals they score points with, and they are scored by their opponents. The first several moves are exploratory and basically random, so random players were a mostly effective heuristic. While I have done research since then on more complicated game-evaluation metrics, I have not had the opportunity to implement them yet.

All of the work in this project is fully my own.