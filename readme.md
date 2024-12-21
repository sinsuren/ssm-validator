# SSM Validator Gradle Plugin

The **SSM Validator Gradle Plugin** helps validate and ensure that SSM (Systems Manager) keys referenced in your source application configuration are properly defined in your destination configuration files, such as `common.yaml`.

---

## Features
- Validates the presence of mandatory SSM keys in the destination configuration file.
- Identifies missed SSM keys from the source configuration and checks if they are already present in the destination file.
- Skips specific keys defined as "skipped keys".
- Allows for flexible regex configuration for matching and cleaning up SSM keys.

---
## Plugin ID

```groovy
id 'com.sinsuren.ssm-validator' version '1.0.0'
```
--- 
## Installation

To use this plugin in your project, apply the plugin in your build.gradle file:

```groovy
plugins {
    id 'com.sinsuren.ssm-validator' version '1.0.0'
}
```
---

## Configuration

Example Configuration

```groovy

ssmValidator {
    // File where application properties are being set from SSM
    sourceKeyFilePath = "${rootProject.rootDir.absolutePath}/server/core/src/main/resources/application.properties"
    sourceKeyRegex = "\\{[A-Z_]{2,}+"
    sourceKeyCleanupRegex = "\\{"

    // Skip keys automatically set by the build pipeline and don't need to be validated
    skippedKeys = ["STATSD_HOST", "RUN_ENV", "STATSD_PORT", "SERVICE_NAME", "APM_KEY"]
    
    // Mandatory keys that must be found in the destination file
    mandatoryKeys = ["JAVA_OPTS"]

    // File to validate the keys against (e.g., common.yaml)
    destinationFilePath = "${rootProject.rootDir.absolutePath}/code/configs/common.yaml"
    destinationKeyRegex = "[v1\\//][A-Z_]+"
    destinationKeyCleanupRegex = "/"

    // Pattern to skip commented lines in source files
    sourceCommentPattern = "^#"
}
```

---
## Properties

- `sourceKeyFilePath`: Path to the source configuration file (e.g., application.properties) containing the SSM keys.
- `sourceKeyRegex`: Regular expression to match SSM keys in the source configuration file.
- `sourceKeyCleanupRegex`: Regular expression to clean up matched SSM keys from the source file.
- `skippedKeys`: A set of keys that should be ignored during validation (e.g., keys automatically set by the build 
  pipeline).
- `mandatoryKeys`: A set of keys that must be present in the destination configuration file.
- `destinationFilePath`: Path to the destination configuration file (e.g., common.yaml).
- `destinationKeyRegex`: Regular expression to match SSM keys in the destination configuration file.
- `destinationKeyCleanupRegex`: Regular expression to clean up matched SSM keys from the destination file.
- `sourceCommentPattern`: Regular expression to skip commented lines in the source file.

---- 

## Example Output

When running the task, you will see logs indicating the matching and missing keys:
```yaml
Destination file path: /path/to/common.yaml
Destination file regex: [v1\\//][A-Z_]+
Destination file cleanup regex: /
Source file path: /path/to/application.properties
Source file regex: \{[A-Z_]{2,}+
Source file cleanup regex: \{
Commented line pattern: ^#
Keys found in destination path: [JAVA_OPTS, STATSD_HOST]
Missed Key Set: [RUN_ENV, SERVICE_NAME]

```
---

## Contributing
Contributions are welcome! Feel free to fork the project, make improvements, and create a pull request.

