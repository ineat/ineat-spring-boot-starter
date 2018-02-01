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

import org.springframework.aop.interceptor.SimpleTraceInterceptor;

public class TraceMonitoringAutoConfigurationTest extends AbstractMonitoringAutoConfigurationTest<SimpleTraceInterceptor>{

    @Override
    protected String getPropertiesPrefix() {
        return "ineat.monitoring.trace.";
    }

    @Override
    protected MonitoringProperties.AbstractMonitoringProperties getMonitoringProperties() {
        MonitoringProperties monitoringProperties = context.getBean(MonitoringProperties.class);

        return monitoringProperties.getTrace();
    }

    @Override
    protected Class<SimpleTraceInterceptor> getInterceptorBeanClass() {
        return SimpleTraceInterceptor.class;
    }

}
