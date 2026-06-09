[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wPuP5asc)
# ICS3U CPT – Interactive Processing Project

This repository contains your ICS3U Culminating Performance Task (CPT).

# ICS3U CPT – Interactive Processing Project
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wPuP5asc)

## Project Title: Collins Blackjack
**Author:** Collin Jin  
**Course:** ICS3U Culminating Performance Task 
**Development Library:** Processing for Java (`PApplet`)

## Program Screenshot

![Collins Blackjack Game Screen](Screenshot.jpeg)



## Project Description
Collins Blackjack is a interactive, digital card game built from Java and the Processing core library. The game aims to recreates the classic casino staple, Blackjack (21). 

The application uses a state engine to manage a `waitingPhase` (Welcome Screen), `playPhase` (Active Hands), and an `endPhase` (Game Over/Replay evaluation). Features include math rendering to calculate hand sums, automated logic for handling soft/hard Ace values, dealer AI that hit on values under 17, and visual animations that slide drawn cards.



## Controls & Interaction
Interaction with the program relies entirely on keyboard inputs during specific game phases:

**Begin ("b")** – Press on the Welcome Screen to start a brand new game match.
**Hit ("h")** – Request another card from the dealer during your turn if your current sum is under 21.
**Stay ("s")** – Hold your current hand total and pass the turn to the AI dealer.
**Yes ("y")** – Re-trigger a fresh match cycle to play another round when prompted at the end of a game.
**No ("n")** – Exit the active match loop, finish playing, and stop program execution.


## Known Limitations/Incomplete Features
While the primary ruleset of Blackjack is fully integrated, the following edge cases and omissions remain within the build:

1. Static Suit Assignment: Suit symbols are mapped out using a modulo operation on array indices (`deckIndex % 4`). Because the deck array stores static values, the tracking of a unique 52-card physical deck simulation is simplified into a generalized probability draw. With this project, card values are always going to be the same suit which negates certain traditional strategies like card counting. 

2. Splitting & Doubling Down: Advanced wagering structures like Splitting pairs or Doubling Down are not featured.

3. Betting Economy: The screen states the text phrase *"Bets have been returned"* during ties, but a true numerical chip balance is not actively saved or tracked.


## Attribution Section
All programming logic, structural architecture, text setups, and math behaviors were coded entirely from scratch using Java.

 **PApplet Processing:** Processing `PApplet` Core API.
 **Card Suits:** Unicode characters (`♠`, `♥`, `♦`, `♣`).
