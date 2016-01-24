# Computer-Science-with-Android
Computer Science with Android 2016

## Anagrams

![](http://i.imgur.com/tUiHUuUl.jpg)

The game will provide a word from the dictionary to the user. 
The user must try to create as many words as possible that contain all the letters of the given word plus one additional letter. Note that adding the extra letter at the beginning or the end without reordering the other letters is not valid.

For example, if the game picks the word 'ore' as a starter, the user might guess 'rose' or 'zero' but not 'sore'. 

The user can give up and see the words that they did not guess by clicking on the hint button.

## Pig

![](http://i.imgur.com/fg1GWhCl.png)

Pig is a turn-based dice game where a player is pit against a computer to try and accumulate the highest score possible without rolling a one.

During each turn, the player can repeatedly roll a single die until either a 1 is rolled or until he/she decides to "hold":
* If the player rolls a 1, they score nothing and it becomes the next player's turn.
* If the player rolls any other number, it is added to their turn total and the player's turn continues.
* If a player chooses to "hold", their turn total is added to their score, and it becomes the computer's turn.
* The first one to score 100 or more points wins.

## Ghost

![](http://imgur.com/SpDacZWl.png)

Ghost is a word game in which players take turns adding individual letters to a growing word fragment, trying **not** to be the one to complete a valid word. Each fragment must be the beginning of an actual word, and, for our purposes, we will consider 4 to be the minimum word length. The player who completes a word or creates a fragment that is not the prefix of a word loses the round.

So on player 1's turn, they can:
* challenge player 2's word if they think player 2 has formed a valid word of at least 4 letters. If the fragment is a word, then player 1 wins; if the fragment is not a word, then player 2 wins.
* challenge player 2's word if they think that no word can be formed with the current fragment. Then, player 2 must provide a valid word starting with the current fragment or lose.
* add a letter to move the fragment towards a valid word
* attempt to bluff player 2 by adding a letter that doesn't move the fragment towards a valid word 
