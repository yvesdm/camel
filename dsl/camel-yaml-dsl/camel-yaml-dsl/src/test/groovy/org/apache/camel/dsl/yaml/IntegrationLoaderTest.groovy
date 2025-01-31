/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dsl.yaml

import org.apache.camel.component.seda.SedaComponent
import org.apache.camel.dsl.yaml.support.YamlTestSupport
import org.apache.camel.spi.PropertiesComponent

class IntegrationLoaderTest extends YamlTestSupport {

    def "integration configuration"() {
        when:
            loadIntegrations('''
                apiVersion: camel.apache.org/v1
                kind: Integration
                metadata:
                  name: foobar
                spec:
                  configuration:
                    - type: property
                      value: camel.component.seda.queueSize = 123
                    - type: property
                      value: camel.component.seda.default-block-when-full = true
                  flows:
                    - from:
                        uri: "seda:foo"
                        steps:    
                          - log:
                             logging-level: "INFO"
                             message: "test"
                             log-name: "yaml"
                          - to: "mock:result"   
                          ''')
        then:
            context.routeDefinitions.size() == 1

            PropertiesComponent pc = context.getPropertiesComponent()
            pc.resolveProperty("camel.component.seda.queueSize").get() == "123"
            pc.resolveProperty("camel.component.seda.default-block-when-full").get() == "true"
    }

}
