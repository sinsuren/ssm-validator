package com.sinsuren.ssm.validator


import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.util.regex.Matcher
import java.util.regex.Pattern

class SSMValidator extends DefaultTask {

    @Input
    Property<String> destinationFilePath

    @Input
    Property<String> destinationKeyRegex

    @Input
    Property<String> destinationKeyCleanupRegex

    @Input
    Property<String> sourceKeyFilePath

    @Input
    Property<String> sourceKeyRegex

    @Input
    Property<String> sourceKeyCleanupRegex

    @Input
    SetProperty<String> skippedKeys

    @Input
    SetProperty<String> mandatoryKeys

    @Input
    Property<String> sourceCommentPattern

    @TaskAction
    def validateSSM() {
        logger.info("Destination file path: {}", getDestinationFilePath().get())
        logger.info("Destination file regex: {}", getDestinationKeyRegex().get())
        logger.info("Destination file cleanup regex: {}", getDestinationKeyCleanupRegex().get())

        logger.info("Source file path: {}", getSourceKeyFilePath().get())
        logger.info("Source file regex: {}", getSourceKeyRegex().get())
        logger.info("Source file cleanup regex: {}", getSourceKeyCleanupRegex().get())
        logger.info("Commented line pattern: {}", getSourceCommentPattern().get())

        Set<String> missedSSMKeys = new HashSet<>()
        Set<String> existingSSMKeyInDestination = getDestinationKeys()

        logger.info("Keys found in destination path: {}", existingSSMKeyInDestination)

        missedSSMKeys.addAll(validateMandatoryKeys(existingSSMKeyInDestination))
        missedSSMKeys.addAll(getMissedSSMFromSource(existingSSMKeyInDestination))

        logger.info("Missed Key Set: {}", missedSSMKeys)

        if (missedSSMKeys.size() > 0) {
            throw new GradleException(String.format("Missed Keys in %s: %s", getDestinationFilePath().get(), missedSSMKeys.toString()))
        }
    }

    private Set<String> getMissedSSMFromSource(Set<String> existingSSMKeyInDestination) {
        Set<String> missedSSMKeys = new HashSet<>()
        Set<String> skippedKeys = getSkippedKeys().get()

        Pattern applicationConfigurationFilePattern = Pattern.compile(getSourceKeyRegex().get())
        def sourceKeyConfigurationLines = new File(getSourceKeyFilePath().get()).readLines()

        Pattern commentedLinesPattern = Pattern.compile(getSourceCommentPattern().get())

        for (line in sourceKeyConfigurationLines) {
            Matcher matcher = applicationConfigurationFilePattern.matcher(line)

            if (matcher.find()) {
                if (commentedLinesPattern.matcher(line).find()) {
                    logger.info("Skipping the comment: {}", line)
                    continue
                }

                String matchedString = matcher.group().replaceAll(getSourceKeyCleanupRegex().get(), "").trim()

                logger.info("Pattern from source: {}", matcher.group())
                if (!skippedKeys.contains(matchedString) && !existingSSMKeyInDestination.contains(matchedString)) {
                    missedSSMKeys.add(matchedString)
                }
            }
        }
        return missedSSMKeys
    }

    private Set<String> validateMandatoryKeys(Set<String> existingSSMKeyInDestination) {
        Set<String> missedSSMKeys = new HashSet<>()
        for (mandatoryKey in getMandatoryKeys().get()) {
            if (!existingSSMKeyInDestination.contains(mandatoryKey)) {
                missedSSMKeys.add(mandatoryKey)
            }
        }
        return missedSSMKeys
    }

    private Set<String> getDestinationKeys() {
        Set<String> destinationKeySet = new HashSet<>()
        def yamlFileLines = new File(getDestinationFilePath().get()).readLines()

        Pattern codePipelineFilePattern = Pattern.compile(getDestinationKeyRegex().get())

        for (line in yamlFileLines) {
            Matcher matcher = codePipelineFilePattern.matcher(line)
            if (matcher.find()) {
                logger.info("Matched Pattern from destination: {}", matcher.group())
                destinationKeySet.add(matcher.group().replaceAll(getDestinationKeyCleanupRegex().get(), "").trim())
            }
        }
        return destinationKeySet
    }
}
