# cardgame

A fun, easy-to-learn [collectible card game](https://en.wikipedia.org/wiki/Collectible_card_game) (CCG) that's free to collect for!

---

## The Problem

Many CCGs have high points. *Magic: the Gathering* (MTG), for example, provides a very  
interactive experience for the players, where one can make many different plays  
leading to different outcomes. *Yu-Gi-Oh* (YGO) features a great deal of deck searching,  
where the player may fetch specific cards to use, which helps to eliminate the sense  
of randomness that comes with drawing from a deck. However, not all is well. In MTG, one  
can fail to draw resources with which to play cards, or draw too many resources and not  
enough cards to play. In YGO, games are often won and lost in but a few turns, where a  
player has no time to prepare his or her defenses. Why is there not a card game that  
combines the best aspects of these wildly popular games, while avoiding their pitfalls?  

---

## Goals

We aim to create a fast, engaging play style that is heavy on decision making, and which  
eliminates some of the randomness of traditional CCGs by employing deck searching and by  
using a substantial amount of effects that allow players to draw cards.  

- Limit games to 10-15 minutes average play time
- Have decision making at every step of play
- Create a non-restrictive mana system
  - All cards will be usable for their effect, or as a resource.
- Involve a lot of deck searching and card drawing mechanics

---

## License

This program is free software: you can redistribute it and/or modify  
it under the terms of the GNU General Public License as published by  
the Free Software Foundation, either version 3 of the License, or  
(at your option) any later version.  

This program is distributed in the hope that it will be useful,  
but WITHOUT ANY WARRANTY; without even the implied warranty of  
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
GNU General Public License for more details.  

You should have received a copy of the GNU General Public License  
along with this program.  If not, see <http://www.gnu.org/licenses/>.  

---

## Building/Installation

To build the program you must use gradle.

Provided in the root directory is a script to execute a gradle wrapper that will download gradle and the projects dependencies.

#### Getting gradle

- Run the ``` gradlew.bat ``` provided in the root directory to install gradle to your project.

#### Building and Running cardgame

- Run the command ``` gradlew.bat run ``` to execute the program.

- **Note: this will download all dependencies required, build, and run the project.**

#### Running cardgame tests

- Run the command ``` gradlew.bat test ``` to execute the unit tests.

- A HTML report will be generated and can be viewed at [/build/reports/tests/test/index.html](./build/reports/tests/test/index.html)

---

## Authors

Forrest Dale  
Josh Hendrix  
Nick Phillips  
William Swihart  
