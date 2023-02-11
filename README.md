# Genetic Algorithm TSP Problem

## Algorithm
A genetic algorithm is used to determine the shortest route that uses all available nodes.

In each generation the fitness of each route is rated and the generation is sorted accordingly.
The Elite, the routes with the highest fitness, are used as parents to make children with similar routes.
Depending on the probability of mutation, a copy of a child is added that differs from its origin by 2 nodes. (mutation)

## Visualisation
<img width="1202" alt="Bildschirmfoto 2022-10-06 um 15 19 22" src="https://user-images.githubusercontent.com/62705365/194323417-4f3f798b-f473-499b-b39b-207377430d90.png">
Best route: &nbsp; &nbsp; White </br>
Best route Generation zero:   Gray
Yellow graph: fitness history
