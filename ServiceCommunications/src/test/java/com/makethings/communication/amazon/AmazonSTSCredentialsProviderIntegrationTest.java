package com.makethings.communication.amazon;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/AmazonSTSCredentialsProviderIntegrationTest.xml")
public class AmazonSTSCredentialsProviderIntegrationTest {
    
    @Autowired
    private AmazonSTSCredentialsProvider provider;

    @Test
    public void givenAWSCredentialsThenTemporaryOneShouldBeGenerated() throws IOException {
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

}
