<img src="https://github.com/T1GIT/T1GIT/raw/main/covers/Sea_Battle.png">

<h3 align="center"> |
    <a href="#Description"> Description </a> |
    <a href="#Getting-Started"> Getting Started </a> |
    <a href="#Built-With"> Built With </a> |
    <a href="#Author"> Author </a> |
    <a href="#License"> License </a> |
</h3> 

------------------------------------------------

<h1 align="center"> Console Battleship with multiplayer </h1>

## Description
It is console realisation of the game Battle Ship in Java

#### Rooms system
Game going through players in the **room**. The room may
contains 2 or more players. If the room includes more
then 2 players than game going through cyclic. Example:
We have room with 3 players: A, B, C.
Games looks so:
1. A attacks B
2. B attacks C
3. C attacks A
4. ... and etc in the same way

#### Playing

When one of them won't have alive boats he'll lose
and skipped. Player before them will attack next
player. Game continues while all players almost one
will lose.

#### Player types

* PC (Automatically generate data. Has some logic)
* UI (Will request data from the user)

You can choose every player whom it can be at 
the moment of initialisation. 

#### Commands

In any moment of the constellations or game process
you can type plying as UI [_r_](#commands) or [_random_](#commands)
```
r - for random step
random - for repeated random steps
```
**Other**
```
chat <some text> - sending message to all players in the room
```

#### Multiplayer

It has possibility of playing via the internet. 
And you can secure your room with **password**. In 
web mode you can chat with all competitors. Just 
type [chat \<message>](#commands) when coordinates for 
attack or was requested, then all participants 
will see it. In any mode you can make your 
computer play for you, for that you should 
call yourself "PC".


## Getting Started

1. Download project
2. Install java version 14 or newer
4. Run [SeaBattle.java](src/seaBattle/SeaBattle.java) from the IntelliJ IDE


## Built With

* [Java 14](https://www.oracle.com/ru/java/) - Language


## Author

### [**Derbin Dmitriy**](https://github.com/T1GIT)

#### Student of the Financial University
##### Group: PI19-5

## License

This project is licensed under the GPL License - see the [LICENSE](LICENSE) file for details

### Version 1.0
#### 31.10.2020
