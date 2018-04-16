# twaddle

Three way decision with new ppy.

[Reference.](http://www2.cs.uregina.ca/~twd/)

Some infomation can see at [data/readme](data/readme.txt)

+ __Part 1__ :: example [tab1](data/tab1)  
+ __Part 2__ :: example [tab2](data/tab2) [tab3](data/tab3)  
+ __Part 3__ :: mushroom [mushroom](http://archive.ics.uci.edu/ml/datasets/Mushroom)  
+ __Part 4__ :: connect-4 [Connnect-4](http://archive.ics.uci.edu/ml/datasets/connect-4) ___!multi-classification___

---
Note:

```scala
implicit class Crossable[X](xs: Traversable[X]) {
  def cross[Y](ys: Traversable[Y]) = for { x <- xs; y <- ys } yield (x, y)
}
```

---
EOF
