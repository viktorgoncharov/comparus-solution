# LongMap Solution

The solution based on standard java library approaches.

Because requirements regarding performance and memory conflict 
each other, I identificated the following primary requirements:
* it should not use any known Map implementations; 
* it should use as less memory as possible

And secondary:
* it should have adequate performance;

The solution implements a map which contains number (1000) of buckets. 
Every bucket can contain a single value or list up to 10 items or AVL tree
to keep more than 10 values. The picture below shows the solution in 
principle.

![map](http://i.piccy.info/i9/a670290edf9ee979a216f986e2065b49/1587668657/71930/1374641/longmap.png)

The map contains 1000 buckets. To insert a new item it computes a 
key from a range [1..1000] as a remainder of the division to 1000.    

There is a special test which checks the doesn't the tree have a height 
more than theoretical limit.

The code covered by unit-tests for more than 80%.

The code organized by the following structure:
* **Bucket** - source code for a bucket
* **BucketMode** - enum for modes of the bucket
* **LongMap** - an original interface
* **LongMapImpl** - an implementation of the map
* **Node** - AVL tree
* **Pair** - a container for pairs 

An original task described below:

# long-map

Finish development of class LongMapImpl, which implements a map with keys of type long. It has to be a hash table (like HashMap). Requirements:
* it should not use any known Map implementations; 
* it should use as less memory as possible and have adequate performance;
* the main aim is to see your codestyle and teststyle 
