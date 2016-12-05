#COMP34120 Project 1.

The project contains an implementation of AI algorithm that plays Mancala 7-7.

##Heuristics

The heuristics is quite simple:

```
score = (myMancala - yourMancala)*2 + (seedsInMyPits - seedsInYourPits)
```

This heuristics proved to be quite effective as it is aiming to capture as many seeds as possible and also maximizes the number of seeds on player's side. Seeds on player's side are likely to land in his Mancala when the game finishes.


##Building
1. Navigate to **src** directory
2. Run **./buildScript**
3. Find jar file in **../build** directory
