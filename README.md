# Algorithms

All homework assignments are form Princeton Algorithms course (coursera).
Pass 100%.

Princeton Algorithms part I: https://www.coursera.org/learn/algorithms-part1

Princeton Algorithms part II: https://www.coursera.org/learn/algorithms-part2
<br/>
<br/>
<br/>

**1. Percolation** <br/><br/>
Given a composite systems comprised of randomly distributed insulating and metallic materials: what fraction of the materials need to be metallic so that the composite system is an electrical conductor? Given a porous landscape with water on the surface (or oil below), under what conditions will the water be able to drain through to the bottom (or the oil to gush through to the surface)? Scientists have defined an abstract process known as percolation to model such situations.
<br/>
<br/>
<br/>

**2. Queue** <br/><br/>
Write a generic data type for a deque and a randomized queue. The goal of this assignment is to implement elementary data structures using arrays and linked lists, and to introduce you to generics and iterators.
<br/>
<br/>
<br/>

**3. Collinear** <br/><br/>
Write a program to recognize line patterns in a given set of points.
Computer vision involves analyzing patterns in visual images and reconstructing the real-world objects that produced them. The process is often broken up into two phases: feature detection and pattern recognition. Feature detection involves selecting important features of the image; pattern recognition involves discovering patterns in the features. We will investigate a particularly clean pattern recognition problem involving points and line segments. This kind of pattern recognition arises in many other applications such as statistical data analysis.
<br/>
<br/>
<br/>


**4. 8-Puzzle** <br/><br/>
Write a program to solve the 8-puzzle problem (and its natural generalizations) using the A* search algorithm.
<br/>
<br/>
<br/>


**5. Kd-Tree** <br/><br/>
Write a data type to represent a set of points in the unit square (all points have x- and y-coordinates between 0 and 1) using a 2d-tree to support efficient range search (find all of the points contained in a query rectangle) and nearest-neighbor search (find a closest point to a query point). 2d-trees have numerous applications, ranging from classifying astronomical objects to computer animation to speeding up neural networks to mining data to image retrieval.
<br/>
<br/>
<br/>



**6. Word Net** <br/><br/>
WordNet is a semantic lexicon for the English language that computational linguists and cognitive scientists use extensively. For example, WordNet was a key component in IBM’s Jeopardy-playing Watson computer system. WordNet groups words into sets of synonyms called synsets. For example, { AND circuit, AND gate } is a synset that represent a logical gate that fires only when all of its inputs fire. WordNet also describes semantic relationships between synsets. One such relationship is the is-a relationship, which connects a hyponym (more specific synset) to a hypernym (more general synset). For example, the synset { gate, logic gate } is a hypernym of { AND circuit, AND gate } because an AND gate is a kind of logic gate.
<br/>
<br/>
<br/>


**7. Seam Carving** <br/><br/>
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row; a horizontal seam is a path of pixels connected from the left to the right with one pixel in each column. Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (such as cropping and scaling), seam carving preserves the most interest features (aspect ratio, set of objects present, etc.) of the image.
<br/>
<br/>
<br/>


**8. Baseball Elimination** <br/><br/>
In the baseball elimination problem, there is a division consisting of n teams. At some point during the season, team i has w[i] wins, l[i] losses, r[i] remaining games, and g[i][j] games left to play against team j. A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place. The goal is to determine exactly which teams are mathematically eliminated. For simplicity, we assume that no games end in a tie (as is the case in Major League Baseball) and that there are no rainouts (i.e., every scheduled game is played).
<br/>
<br/>
<br/>


**9. Boggle** <br/><br/>
Write a program to play the word game Boggle®.

The Boggle game. Boggle is a word game designed by Allan Turoff and distributed by Hasbro. It involves a board made up of 16 cubic dice, where each die has a letter printed on each of its 6 sides. At the beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with only the top sides of the dice visible. The players compete to accumulate points by building valid words from the dice, according to these rules:

A valid word must be composed by following a sequence of adjacent dice—two dice are adjacent if they are horizontal, vertical, or diagonal neighbors.<br/>
A valid word can use each die at most once.<br/>
A valid word must contain at least 3 letters.<br/>
A valid word must be in the dictionary (which typically does not contain proper nouns).
<br/>
<br/>
<br/>


**10. BurrowsWheeler** <br/><br/>
Implement the Burrows–Wheeler data compression algorithm. This revolutionary algorithm outcompresses gzip and PKZIP, is relatively easy to implement, and is not protected by any patents. It forms the basis of the Unix compression utility bzip2.
The Burrows–Wheeler data compression algorithm consists of three algorithmic components, which are applied in succession:

Burrows–Wheeler transform. Given a typical English text file, transform it into a text file in which sequences of the same character occur near each other many times.
Move-to-front encoding. Given a text file in which sequences of the same character occur near each other many times, convert it into a text file in which certain characters appear much more frequently than others.
Huffman compression. Given a text file in which certain characters appear much more frequently than others, compress it by encoding frequently occurring characters with short codewords and infrequently occurring characters with long codewords.
<br/>
<br/>
<br/>

