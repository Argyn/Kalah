#COMP34120 Project 1.

The project contains an implementation of AI algorithm that plays Mancala 7-7.

##Heuristics

The heuristics is quite simple:

```
score = (myMancala - yourMancala)*2 + (seedsInMyPits - seedsInYourPits)
```

This heuristics proved to be quite effective as it is aiming to capture as many seeds as possible and also maximizes the number of seeds on player's side. Seeds on player's side are likely to land in his Mancala when the game finishes.

###Pie Rule

The pile rule is implemented as follows:

1. If we are moving second, then we check how would we move if we were the opposing player
2. If we would have made the same move, then we decide to SWAP
3. Otherwise, we stick up to the previously assigned role

It also should be noted, that AplhaBetaMiniMax has been modified in such a way that it takes into consideration pie rule, so that it does not get extra move after very first move on the board.

##Building
1. Navigate to **src** directory
2. Run **./buildScript**
3. Find jar file in **../build** directory

##Testing
1. To test against **MKRefAgent**, run
```
./test/testAgainstMKRefAgent
```
You will be prompted to chose the agent that moves first, choose either **MKAgent(us)** or **MKRefAgent**

2. To Test against **JimmyPlayer**, run
```
./test/testAgainstJimmyPlayer
```

Again choose, what agent moves first.


