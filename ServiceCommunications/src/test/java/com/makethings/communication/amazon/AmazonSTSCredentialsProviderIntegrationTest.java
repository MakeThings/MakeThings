package com.makethings.communication.amazon;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;

public class AmazonSTSCredentialsProviderIntegrationTest {
    private AmazonSTSCredentialsProvider provider;

    @Test
    @Ignore
    public void givenAWSCredentialsThenTemporaryOneShouldBeGenerated() throws IOException {
        givenWeHaveCredentationsProvider();

        AmazonServiceCredentials credentials = whenGettingCredentials();

        thenCredentialsAreGenerated(credentials);
    }

    private void thenCredentialsAreGenerated(AmazonServiceCredentials credentials) {
        assertThat(credentials.getAwsCredentials(), notNullValue());
        assertThat(credentials.getAwsCredentials().getAWSAccessKeyId(), notNullValue());
        assertThat(credentials.getAwsCredentials().getAWSSecretKey(), notNullValue());
    }

    private AmazonServiceCredentials whenGettingCredentials() {
        return provider.getCredentials();
    }

    private void givenWeHaveCredentationsProvider() throws IOException {
        InputStream properties = this.getClass().getResourceAsStream("/aws_my.properties");
        PropertiesCredentials awsCred = new PropertiesCredentials(properties);

        provider = new AmazonSTSCredentialsProvider();
        provider.setAwsCredentials(awsCred);
    }
}
