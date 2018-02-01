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

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.interceptor.SimpleTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.Assert;



@EnableConfigurationProperties(MonitoringProperties.class)
public class MonitoringAutoConfiguration {
    @Autowired
    private MonitoringProperties monitoringProperties;


    @Configuration
    @EnableAspectJAutoProxy
    @Aspect
    @ConditionalOnProperty(prefix = "ineat.monitoring.trace", name = "enabled", havingValue = "true")
    public class TraceMonitoringConfiguration {

        @Autowired
        private MonitoringProperties monitoringProperties;

        @Bean
        public SimpleTraceInterceptor simpleTraceMonitorInterceptor() {
            return new SimpleTraceInterceptor(true);
        }

        @Bean
        public Advisor simpleTraceMonitorAdvisor() {
            Assert.notNull(monitoringProperties.getTrace().getPointcutExpression(), MonitoringProperties.REQUIRED_EXPRESSION_MSG);

            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(monitoringProperties.getTrace().getPointcutExpression());
            return new DefaultPointcutAdvisor(pointcut, simpleTraceMonitorInterceptor());
        }


    }

    @Configuration
    @EnableAspectJAutoProxy
    @Aspect
    @ConditionalOnProperty(prefix = "ineat.monitoring.performance", name = "enabled", havingValue = "true")
    public class PerformanceMonitoringConfiguration {


        @Bean
        public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
            return new PerformanceMonitorInterceptor(true);
        }

        @Bean
        public Advisor performaceMonitorAdvisor() {
            Assert.notNull(monitoringProperties.getPerformance().getPointcutExpression(), MonitoringProperties.REQUIRED_EXPRESSION_MSG);

            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(monitoringProperties.getPerformance().getPointcutExpression());
            return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
        }


    }
}