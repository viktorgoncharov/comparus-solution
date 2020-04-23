# LongMap Solution

In result of considering the task, I assumed the following primary requirements:
* it should not use any known Map implementations; 
* it should use as less memory as possible

And secondary:
* it should have adequate performance;

The solution implements a map which contains number of buckets. 
Every bucket can contain a single value, list up to 10 items or AVL tree
to keep more than 10 values. The picture below shows the solution in 
principle.

![map]([URL=http://piccy.info/view3/13770637/5d6cc1a9e76b36b26fba5a0538ab43eb/][IMG]http://i.piccy.info/i9/a80274230a320c5f5aa7179f5dae8ff1/1587668657/6703/1374641/longmap_240.jpg[/IMG][/URL][URL=http://i.piccy.info/a3c/2020-04-23-19-04/i9-13770637/240x154-r][IMG]http://i.piccy.info/a3/2020-04-23-19-04/i9-13770637/240x154-r/i.gif[/IMG][/URL])

The map contains 1000 buckets, and to insert a new item it computes a 
key from a range [1..1000] as a remainder of the division to 1000.    

There is a special test which checks the tree doesn't have a height more
than theoretical limit.

The code covered by unit-tests for more than 80%. 

An original task described below:

# long-map

Finish development of class LongMapImpl, which implements a map with keys of type long. It has to be a hash table (like HashMap). Requirements:
* it should not use any known Map implementations; 
* it should use as less memory as possible and have adequate performance;
* the main aim is to see your codestyle and teststyle 
