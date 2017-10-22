# Known issues

* Inconsistent behavior of dispose() methods: test setting value before/after dispose() (from property, from component),
test listener before/after dispose(), test multiple dispose()

# Ideas for the short term

* Add predicates/conditions to Binder: for example, from(p1).filter(f1).transform(t).filter(f2).to(p2)
* Move inhibition implementations to separate AbstractInhibitableReadable*Property classes
* Add DeepDisposable interface and make dispose() methods consistent
* Improve InvokeLaterPropertyWrapper
* Convenient value change listeners to check the correct behavior of the properties (incl. set, list and map
properties): always on the same thread, always on EDT, never on EDT, only called when values are different, etc.
* Set-, List- and Map-related transformers
* More JavaFX 2 support
* Property injection framework to help implementing user interfaces with a composable MVP pattern

* Add demo module

* Add ReadOnlyComponentEnabledProperty with multiple components
* Add selected row/column property for table with model/view indices
* Add ListElementTransformer
* Implement Disposable where appropriate
* Consistent comments (use of dispose(), cast transformers, default behaviors, what classes can be used for, etc.)
* Add more examples, javadoc and UML diagrams to wiki

* Inhibit set/list/map properties
* Add setAll() method to set/list/map properties
* Add property change listener from a property change listener (while being notified)

# Ideas for the long term

* Inject Swing components in Presenter (just like @FXML for JavaFX)
* Thread-safety
* Multi-threading
* Property binding flow logging
* Use of IDs
* All features described in ValidationFramework wiki
