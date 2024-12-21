package com.sinsuren.ssm.validator

import org.gradle.api.Plugin
import org.gradle.api.Project

class SSMValidatorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('ssmValidator', SSMValidatorExtension)
        // Register the custom task
        project.tasks.register('ssmValidator', SSMValidator) {
            group = 'SSM Validator'
            description = 'Validates SSM keys between source and destination files.'
            destinationFilePath.set(project.ssmValidator.destinationFilePath)
            destinationKeyRegex.set(project.ssmValidator.destinationKeyRegex)
            destinationKeyCleanupRegex.set(project.ssmValidator.destinationKeyCleanupRegex)
            sourceKeyFilePath.set(project.ssmValidator.sourceKeyFilePath)
            sourceKeyRegex.set(project.ssmValidator.sourceKeyRegex)
            sourceKeyCleanupRegex.set(project.ssmValidator.sourceKeyCleanupRegex)
            skippedKeys.set(project.ssmValidator.skippedKeys)
            mandatoryKeys.set(project.ssmValidator.mandatoryKeys)
            sourceCommentPattern.set(project.ssmValidator.sourceCommentPattern)
        }
    }
}
