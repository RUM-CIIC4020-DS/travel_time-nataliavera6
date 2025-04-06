
Your job is to take the current information we have about the train and its destinations and
determine the shortest route towards those destinations. Once that system is done, we want you
to also take our passenger data and analyze how long it takes each one to arrive at their
destination.

Due to budget cuts your project will consist of a 1-person team (congrats on the promotion to
project manager!). Next, we will brief you on the details of your project.

Project Design
For your project you will be tasked with finding the routes that will result in the shortest distance
from the starting position. Once this is achieved you will calculate how long it will take for every
passenger on the train to reach their destination. Additionally, you will include other functionalities
such as how long it will take to reach a destination.

Finding the Shortest Route
The first step for making this a more efficient train station is to identify the shortest route from
point A to point B. To make this happen we will take advantage of data structures such as: Maps,
Stacks, and Sets. Our starting position will be our train station at Westside, and we will be
identifying the shortest route from Westside to every other station.


Part 1: Mapping our solution 
The first step will consist of organizing the data in a way that reflects how the stations are
interconnected. To do this we will take advantage of a Map, where the key will be the name of the
station, and the value will be the neighboring stations which consists of a List of all the stations
they can visit and their respective distance (in kilometers).


Part 2: Shortcuts to Victory
The next step is to find the shortest path from a starting position to every other station. To
accomplish this, we will follow Dijkstra’s algorithm. The main idea is that you do not know
beforehand where everything is, you will begin at the starting position, in our case it will always
be Westside, and from there start discovering every other station that’s available.



