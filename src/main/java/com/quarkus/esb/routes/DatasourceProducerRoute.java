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

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import com.quarkus.esb.properties.DatasourceProducer;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("sqlProducerRoute")
public class DatasourceProducerRoute extends RouteBuilder {

	@Inject
	private DatasourceProducer consumerBase;
	
	@Inject
	@DataSource("datasourceProducer")
	AgroalDataSource inventoryDataSource;


	public void configure() throws Exception {


		onException(ConnectException.class).handled(true)
		    .maximumRedeliveries(3)
		    .redeliveryDelay(2000)
			.logRetryAttempted(true)
			.retryAttemptedLogLevel(LoggingLevel.WARN)
		    .log(LoggingLevel.ERROR, "TRV-01 El host de destino no ha sido alcanzado presenta errores de conexion en la ruta ${routeId}")
		    .log(LoggingLevel.ERROR, "ExceptionClass: ${exchangeProperty.CamelExceptionCaught.class}")
		    .log(LoggingLevel.ERROR, "StackTrace: ${exception.stacktrace}")
		.end();
		
		onException(SQLRecoverableException.class).handled(true)
		    .maximumRedeliveries(3)
		    .redeliveryDelay(3000)
			.logRetryAttempted(true)
			.retryAttemptedLogLevel(LoggingLevel.WARN)
		    .log(LoggingLevel.ERROR, "SQL-01 La transaccion que se que se esta ejecutando presenta errores en la ruta ${routeId}")
		    .log(LoggingLevel.ERROR, "ExceptionClass: ${exchangeProperty.CamelExceptionCaught.class}")
		    .log(LoggingLevel.ERROR, "StackTrace: ${exception.stacktrace}")
		.end();
		
		onException(SQLSyntaxErrorException.class).handled(true)
		    .log(LoggingLevel.ERROR, "SQL-03 La sentencia que se esta ejecutando presenta errores en la ruta ${routeId}")
		    .log(LoggingLevel.ERROR, "ExceptionClass: ${exchangeProperty.CamelExceptionCaught.class}")
		    .log(LoggingLevel.ERROR, "StackTrace: ${exception.stacktrace}")
		.end();
		
		onException(SQLException.class).handled(true)
			.log(LoggingLevel.ERROR, "SQL-02 La transaccion que se esta ejecutando presenta errores en la ruta ${routeId}")
			.log(LoggingLevel.ERROR, "ExceptionClass: ${exchangeProperty.CamelExceptionCaught.class}")
			.log(LoggingLevel.ERROR, "StackTrace: ${exception.stacktrace}")
		.end();
		
		from("direct:datasourceProducerRoute").routeId("testing_database_producer")
			.to("sql:" + consumerBase.getQueryConsulta() + "?dataSource=#datasourceProducer")
			.log("Insercion realizada")
		.end();
	}
}