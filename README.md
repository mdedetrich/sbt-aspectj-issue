# sbt-aspectj-issue

This repository is here to demonstrate an issue I have with using
[sbt-aspectj](https://github.com/sbt/sbt-aspectj) to weave an
external library in its own project. 

This example is mainly converted from this [sample](https://github.com/sbt/sbt-aspectj/blob/master/src/sbt-test/weave/jar)
but altered so that the AspectJ classes are in their own project like [here](https://github.com/sbt/sbt-aspectj/blob/master/src/sbt-test/weave/external).

This means that this project is actually comprised of 2 projects

* The `root` (i.e. `sbt-aspectj-issue`) project which has a `@Before` AspectJ annotation
for an external library `akka-actor`. The `root` project is intended to be used
as a library. The sample code for the AspectJ is copied from 
[here](https://github.com/sbt/sbt-aspectj/blob/master/src/sbt-test/weave/jar/src/main/aspectj/WeaveActor.aj)
* The `test` project which is designed to test that the `root` project is
working. It relies on the `root` project via a `libraryDependency`

To test this, first use `root/publishLocal` to publish the main
project locally and then do `test/run`. Note that you may need to do
`root/clean` to clean in between of doing changes.

## The issue
The problem behind the project is that I am unable to get the `sbt-aspectj`
setup working. The idea is that `sbt-aspectj-issue` is meant to be some library
which instruments another external library (in this case `akka-actors`).