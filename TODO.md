# Next release

* TODO Check all usages of "? super" and "? extends" wildcards
* TODO Check where they can be used
* TODO Check all usages of Dispose and DeepDispose interfaces
* TODO Move inhibition implementation to separate AbstractInihibitableReadableProperty
* TODO DeepDisposable interface
* TODO Functional interface for Set/List/MapValueChangeListener
* TODO Use \<p\> or \<p/\> in javadoc
* TODO Replace "Default constructor" by "Constructor"
* TODO Check each new entry in CHANGES.md

# Known issues

* Inconsistent behavior of dispose() methods: test setting value before/after dispose() (from property, from component),
test listener before/after dispose(), test multiple dispose()

# Ideas for the short term

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
