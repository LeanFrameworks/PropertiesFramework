# Known issues

* Inconsistent behavior of dispose() methods: test setting value before/after dispose() (from property, from component),
test listener before/after dispose(), test multiple dispose()

# Ideas for the short term

* Better use of "? super" and "? extends" wildcards
* Convenient value change listeners to check the correct behavior of the properties (incl. set, list and map
properties): always on the same thread, always on EDT, never on EDT, only called when values are different, etc.
* Better support for Java 8 regarding the use of lambdas and method references (e.g. for set/list/map properties)
* Set-, List- and Map-related transformers
* More JavaFX 2 support
* Property injection framework to help implementing user interfaces with a composable MVP pattern

* Clean up demo module

* Implement Disposable where appropriate
* Consistent comments (use of dispose(), cast transformers, default behaviors, what classes can be used for, etc.)
* Add more examples, javadoc and UML diagrams to wiki

# Ideas for the long term

* Predicates/conditions
* Thread-safety
* Property binding flow logging
* Use of IDs
* All features described in the wiki
