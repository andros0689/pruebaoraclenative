package com.quarkus.esb.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;

import com.quarkus.esb.model.Modelo;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransformationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        DataFormat mydataformat = new JacksonDataFormat(Modelo.class);

        onException(NullPointerException.class, IllegalArgumentException.class).handled(true)
                .log(LoggingLevel.ERROR, "TRV-02 La estructura del mensaje a procesar presenta errores en la ruta ${routeId}")
                .log(LoggingLevel.ERROR, "ExceptionClass: ${exchangeProperty.CamelExceptionCaught.class}")
                .log(LoggingLevel.ERROR, "StackTrace: ${exception.stacktrace}")
        .end();

        from("direct:transformationRoute").routeId("transformationRoute")
            .log("Procesando...")
            .process(new Processor(){
                @Override
                public void process(Exchange ex) throws Exception {
                    if (ex.getIn().getBody() instanceof Modelo) {
                        Log.info("Nada que hacer");
                        ex.getIn().setHeader("toca", "no");
                    } else {
                        ex.getIn().setHeader("toca", "si");
                    }
                }
            })
            .choice()
                .when(simple("${headers.toca} == 'si'"))
                    .unmarshal(mydataformat)
                .otherwise()
                    .log("Ya viene como es")
            .end()
            .log("Mueche: '${body}'")
            .process(new Processor(){
                @Override
                public void process(Exchange ex) throws Exception {
                    Modelo modelo = ex.getIn().getBody(Modelo.class);
                    ex.getIn().setHeader("elid", modelo.getId());
                    ex.getIn().setHeader("elnombre", modelo.getNombre());
                    String[] nombresApellidos = modelo.getNombre().split(",");
                    ex.getIn().setHeader("lista", nombresApellidos);
                }
            })
            .to("velocity://plantilla.vm?allowContextMapAll=true")
            .log("${body}")
            .to("direct:datasourceProducerRoute")
        .end();
    }

}