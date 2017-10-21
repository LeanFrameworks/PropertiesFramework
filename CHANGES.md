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
