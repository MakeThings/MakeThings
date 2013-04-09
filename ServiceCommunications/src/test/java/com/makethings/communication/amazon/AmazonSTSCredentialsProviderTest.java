package com.makethings.communication.amazon;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/AmazonSTSCredentialsProviderTest.xml")
public class AmazonSTSCredentialsProviderTest {

    @Autowired
    private STSSessionCredentialsProvider stsCredentialsProvider;
    
    @Autowired
    private AmazonSTSCredentialsProvider amazonSTSCredentialsProvider;

    private AWSCredentials tempAwsCredentials;
    
    @Test
    public void givenAwsCredentialsThenTemporaryCredentialsCanBeCreatedViaSts() {
        givenTempAwsCredentials();
        givenStsCredentialsProvider();
        
        AmazonTempCredentials tempCredentials = whenCreateTemporaryCredentials();
        
        thenCredentialsContainsTempAwsCredentials(tempCredentials);

    }

    private void thenCredentialsContainsTempAwsCredentials(AmazonTempCredentials tempCredentials) {
        AWSCredentials credentials =  tempCredentials.getAwsCredentials();
        Assert.assertThat(credentials, CoreMatchers.sameInstance(tempAwsCredentials));
    }

    private void givenTempAwsCredentials() {
        tempAwsCredentials = Mockito.mock(AWSCredentials.class);
    }

    private void givenStsCredentialsProvider() {
        Mockito.when(stsCredentialsProvider.getCredentials()).thenReturn(tempAwsCredentials);
    }

    private AmazonTempCredentials whenCreateTemporaryCredentials() {
        return amazonSTSCredentialsProvider.getCredentials();
    }
}
