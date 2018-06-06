# Version 1.0.1 (to be released)

# Version 1.0.0

* Moved to Java 8
* Made better use of generics (with `extends` and `super`)
* Simplified `ReadableWritable*Property` interfaces, now using the same type for read and write
* Removed deprecated `Transformer`, `Aggregator` and `CollectionElementTransformer` from `base` module
* Renamed `read()` methods of the `Binder` to `from()`
* Renamed `write()` methods of the `Binder` to `to()`
* Renamed `SimpleBond` to `SimpleBinding`
* Changed `ReadableWritableProperty<R, W>` to `ReadableWritableProperty<V>`
* Changed `ReadableWritableSetProperty<R, W>` to `ReadableWritableSetProperty<V>`
* Changed `ReadableWritableListProperty<R, W>` to `ReadableWritableListProperty<V>`
* Changed `ReadableWritableMapProperty<K, R, W>` to `ReadableWritableMapProperty<K, V>`
* Annotated one-method interfaces as `@FunctionalInterface`
* Replaced the `SetValueChangeListener` interface by the `SetPropertyChangeListener` functional interface
* Replaced the `ListValueChangeListener` interface by the `ListPropertyChangeListener` functional interface
* Replaced the `MapValueChangeListener` interface by the `MapPropertyChangeListener` functional interface
* Replaced the `ValueChangeListener` interface by the `PropertyChangeListener` functional interface
* Renamed `PrintStreamValueChangeAdapter` to `PrintStreamPropertyChangeAdapter`
* Added JSliderValueProperty, JSliderMinimumValueProperty and JSliderMaximumValueProperty

## Migration from version 0.0.1

Compilation issues are only related to the changes mentioned above; fixing them is quite straightforward. There is no
functionality modified or removed. Fixing compilation issues is a one-to-one mapping from the old APIs to the new APIs.

So there is no incompatible change in terms of behavior (beyond the compilation). 

# Version 0.0.1

* Extraction from the ValidationFramework 3.4.1
* Transferred ownership to LeanFrameworks organization
* Adapted package names accordingly
* Adapted Maven group ID accordingly
* Adapted license accordingly

## Migration from ValidationFramework

If you were only using the properties and binding mechanism from the ValidationFramework (e.g. version 3.4.1), you may
adapt your Maven dependencies as follows:
* Change groupId from `com.google.code.validationframework` to `com.github.leanframeworks`;
* Change artifactId's from `validationframework-*` to `propertiesframework-*`;
* Change version to `0.0.1`.

In the code, simply:
* Change package names from `com.google.code.validationframework` to `com.github.leanframeworks.propertiesframework`.

If you were using other pieces of the ValidationFramework, then the migration is not recommended until the
ValidationFramework itself has been migrated to use the separate PropertiesFramework.
