/**
 * Copyright (C) 2018 The Ineat Lab team (ilab-team@ineat-conseil.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ineat.lab.boot.autoconfigure.monitoring;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.mockito.Mockito.*;

/**
 * Provides a suit of tests, validating AutoConfiguration Classes for Monitoring
 * @param <T>
 */
public abstract class AbstractMonitoringAutoConfigurationTest<T extends MethodInterceptor> {

    protected AnnotationConfigApplicationContext context;

    @Before
    public void setup(){
        context = new AnnotationConfigApplicationContext();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void beanInterceptorShouldNotExistBecauseDisabled(){

        try {
            EnvironmentTestUtils.addEnvironment(context,
                    getPropertiesPrefix() + "enabled:false");
            context.register(MonitoringAutoConfiguration.class);

            context.refresh();

            context.getBean(getInterceptorBeanClass());
            Assert.fail("NoSuchBeanDefinitionException must be raised");
        } catch(NoSuchBeanDefinitionException nsde){
            Assert.assertEquals(0, nsde.getNumberOfBeansFound());
        }
    }

    @Test(expected = BeanCreationException.class)
    public void exceptionMustBeRaisedIfNoExpressionProvide(){
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            EnvironmentTestUtils.addEnvironment(context,
                    getPropertiesPrefix() + "enabled:true");


            context.register(MonitoringAutoConfiguration.class);
            context.refresh();


    }

    @Test
    public void testConfigurationOk() throws Throwable {

        EnvironmentTestUtils.addEnvironment(context,

                getPropertiesPrefix() + "enabled:true",
                getPropertiesPrefix() + "pointcut-expression:within(com.ineat.lab.boot.autoconfigure.monitoring.ClassForTest)");
        context.register(MonitoringAutoConfiguration.class);

        context.refresh();

        Assert.assertNotNull(getMonitoringProperties());
        Assert.assertEquals(true, getMonitoringProperties().isEnabled());
        Assert.assertEquals("within(com.ineat.lab.boot.autoconfigure.monitoring.ClassForTest)", getMonitoringProperties().getPointcutExpression());

    }


    @Test
    @Ignore
    // spy not working with aspect
    public void testLogHello() throws Throwable {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(context,

                getPropertiesPrefix() + "enabled:true",
                getPropertiesPrefix() + "pointcut-expression:within(com.ineat.lab.boot.autoconfigure.monitoring.ClassForTest)",
                "logging.level.com.ineat.lab:TRACE");
        context.register(MonitoringAutoConfiguration.class);

        context.refresh();

        T sti = context.getBean(getInterceptorBeanClass());
        Assert.assertNotNull(sti);

        T spy = Mockito.spy(sti);

        ClassForTest.logHello();

        //Now verify our logging interactions
        verify(spy, times(3)).invoke(anyObject());
    }

    /**
     * <p>Must return the base properties key prefix, to allow setting configuration keys dynamically,
     * according to {@link MonitoringProperties} classes </p>
     * <p>Must end with a DOT</p>
     * <p>example : ineat.monitoring.performance.</p>
     *
     * @return a key prefix for Autoconfiguration properties
     */
    protected abstract String getPropertiesPrefix();

    /**
     * Must return an instance of {@link MonitoringProperties} like {@link com.ineat.lab.boot.autoconfigure.monitoring.MonitoringProperties.TraceMonitoringProperties}
     * to allow testing values defined in configuration file
     * @return an {@link com.ineat.lab.boot.autoconfigure.monitoring.MonitoringProperties.AbstractMonitoringProperties}
     */
    protected abstract MonitoringProperties.AbstractMonitoringProperties getMonitoringProperties();

    /**
     * Must provide the {@link MethodInterceptor} implementation which allow unit test to call spring context.getBean()
     *
     * @return
     */
    protected abstract Class<T> getInterceptorBeanClass();

}
