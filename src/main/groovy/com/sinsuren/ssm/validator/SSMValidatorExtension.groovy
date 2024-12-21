package com.sinsuren.ssm.validator

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

import javax.inject.Inject
import javax.naming.spi.ObjectFactory

class SSMValidatorExtension {
    final Property<String> destinationFilePath;
    final Property<String> destinationKeyRegex;
    final Property<String> destinationKeyCleanupRegex;
    final Property<String> sourceKeyFilePath;
    final Property<String> sourceKeyRegex;
    final Property<String> sourceKeyCleanupRegex;
    final SetProperty<String> skippedKeys;
    final SetProperty<String> mandatoryKeys;
    final Property<String> sourceCommentPattern;

    @Inject
    SSMValidatorExtension(ObjectFactory objectFactory) {
        destinationFilePath = objectFactory.property(String)
        destinationKeyRegex = objectFactory.property(String)
        destinationKeyCleanupRegex = objectFactory.property(String)
        sourceKeyFilePath = objectFactory.property(String)
        sourceKeyRegex = objectFactory.property(String)
        sourceKeyCleanupRegex = objectFactory.property(String)
        skippedKeys = objectFactory.setProperty(String)
        mandatoryKeys = objectFactory.setProperty(String)
        sourceCommentPattern = objectFactory.property(String)
    }
}
