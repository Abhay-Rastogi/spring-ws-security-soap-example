/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wandrell.example.swss.testing.unit.endpoint.signature.xwss;

import java.security.KeyStore;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.wandrell.example.swss.testing.util.SecurityUtils;
import com.wandrell.example.swss.testing.util.config.context.ServletWSS4JContextConfig;
import com.wandrell.example.swss.testing.util.config.context.ServletXWSSContextConfig;
import com.wandrell.example.swss.testing.util.config.context.TestContextConfig;
import com.wandrell.example.swss.testing.util.config.properties.EndpointXWSSPropertiesConfig;
import com.wandrell.example.swss.testing.util.config.properties.InterceptorXWSSPropertiesConfig;
import com.wandrell.example.swss.testing.util.config.properties.SOAPPropertiesConfig;
import com.wandrell.example.swss.testing.util.config.properties.TestPropertiesConfig;
import com.wandrell.example.swss.testing.util.test.unit.endpoint.AbstractTestEntityEndpointRequest;

/**
 * Implementation of {@code AbstractTestEntityEndpointRequest} for a XWSS plain
 * password protected endpoint.
 *
 * @author Bernardo Martínez Garrido
 */
@ContextConfiguration(locations = { ServletWSS4JContextConfig.BASE,
        ServletXWSSContextConfig.SIGNATURE, TestContextConfig.KEYSTORE })
@TestPropertySource({ TestPropertiesConfig.WSDL, SOAPPropertiesConfig.UNSECURE,
        SOAPPropertiesConfig.SIGNATURE,
        InterceptorXWSSPropertiesConfig.SIGNATURE,
        EndpointXWSSPropertiesConfig.SIGNATURE,
        EndpointXWSSPropertiesConfig.BASE, TestPropertiesConfig.USER, })
public final class TestEntityEndpointSignatureXWSS extends
        AbstractTestEntityEndpointRequest {

    /**
     * Alias for the certificate for signing messages.
     */
    @Value("${keystore.alias}")
    private String   alias;
    /**
     * Key store for signing messages.
     */
    @Autowired
    @Qualifier("keyStore")
    private KeyStore keystore;
    /**
     * Password for the passworded message.
     */
    @Value("${security.credentials.password}")
    private String   password;
    /**
     * Password for the certificate for signing messages.
     */
    @Value("${keystore.password}")
    private String   passwordKey;
    /**
     * Path to the file containing the unsecured SOAP request.
     */
    @Value("${soap.request.path}")
    private String   pathUnsecure;
    /**
     * Path to the file containing the valid SOAP request.
     */
    @Value("${soap.request.template.path}")
    private String   pathValid;
    /**
     * Username for the passworded message.
     */
    @Value("${security.credentials.user}")
    private String   username;

    /**
     * Constructs a {@code TestEntityEndpointPasswordPlainXWSS}.
     */
    public TestEntityEndpointSignatureXWSS() {
        super();
    }

    @Override
    protected final Source getRequestEnvelope() {
        try {
            return new StreamSource(SecurityUtils.getSignedMessageStream(
                    pathUnsecure, alias, passwordKey, alias, keystore));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
