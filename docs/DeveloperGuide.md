# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}
### Round Class (@K-J-Q)
The Round class represents a game round in the Javatro card game system. It manages the core gameplay mechanics for a single round of play.

#### Core Functionality
The class provides methods to:
- Play cards from the player's hand
- Discard unwanted cards
- Determine when a round is won or ended

#### Tracks
- Player State: Tracks the player's hand and jokers
- Round Configuration: Stores settings like round name, description, and target score
- Game State: Manages the current score, remaining plays, and discard actions

#### Sub-classes
It attempts to incoperate Single Responsibility Principle and delegates tasks to the following sub classes:
- `RoundState`: Tracks mutable state values
- `RoundConfig`: Stores configuration parameters
- `RoundActions`: Encapsulates gameplay mechanics
- `RoundObservable`: Implements observer pattern for UI updates


## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
