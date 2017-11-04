# Version 1.0.0

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
* TODO Move inhibition implementation to separate AbstractInihibitableReadableProperty
* TODO DeepDisposable interface
* TODO Functional interface for Set/List/MapValueChangeListener

## Migration from version 0.0.1

TODO

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
