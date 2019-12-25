# Mixed Integer Linear Programming (MILP) Solver

This repository contains a MILP that can solve optimization problems of the form:

min c<sup>T</sup>x

s.t. Ax <= b, x<sub>i</sub> are integers
     
where `c` and `x` are vectors in R<sup>n</sup>, `b` is a vector in R<sup>m</sup>, and `A` is a R<sup>mxn</sup> matrix.
