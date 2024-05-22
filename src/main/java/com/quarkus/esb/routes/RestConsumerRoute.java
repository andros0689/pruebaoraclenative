/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.quarkus.esb.routes;

import org.apache.camel.builder.RouteBuilder;

import com.quarkus.esb.model.Modelo;
import com.quarkus.esb.properties.RestConsumer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RestConsumerRoute extends RouteBuilder {

	@Inject
	protected RestConsumer restConfig;

	@Override
	public void configure() throws Exception {

		restConfiguration()
			.component("servlet")
			.apiContextPath(restConfig.getApiPath())
			.apiProperty("api.title", restConfig.getApiTitle())
			.apiProperty("api.version", restConfig.getApiVersion())
			.apiProperty("base.path", restConfig.getApiBasePath())
			.apiProperty("cors", "true");

		rest()
			.post(restConfig.getServiceName())
			.consumes("application/json")
			.produces("application/json")
			.type(Modelo.class)
			.to("direct:transformationRoute");
	}
}
