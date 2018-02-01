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

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration Properties describing all availables keys for monitoring configuration
 */
@Getter
@Setter
@ConfigurationProperties("ineat.monitoring")
public class MonitoringProperties {
    public static final String REQUIRED_EXPRESSION_MSG = "Pointcut expression must be configured through pointcut.expression key";

    /**
     * Properties for Trace monitoring
     */
    private TraceMonitoringProperties trace = new TraceMonitoringProperties();
    /**
     * Properties for Performance Monitoring
     */
    private PerformanceMonitoringProperties performance = new PerformanceMonitoringProperties();




    @Data
    public static class TraceMonitoringProperties extends AbstractMonitoringProperties {

    }

    @Data
    public static class PerformanceMonitoringProperties extends AbstractMonitoringProperties {

    }

    @Getter
    @Setter
    public static  abstract class AbstractMonitoringProperties {
        /**
         * Enable this interceptor aspect
         */
        private boolean enabled;

        /**
         * <p>AspectJ Pointcut expression (see <a href="https://docs.spring.io/spring/docs/4.3.13.RELEASE/spring-framework-reference/html/aop.html#aop-pointcuts">examples of corrects expressions</a>)
         *
         * <p>Example : execution(* com.ineat.lab.sample..service.*ServiceImpl.*(..))</p>
         *
         * <p><b>Be carefull TO NOT SET a too general pointcut expression (like execution(* *(..)) which will result on enabling all
         * aspects presents in your classpath (Spring included !)</b>
         * </p>
         */
        protected String pointcutExpression;


    }



}
