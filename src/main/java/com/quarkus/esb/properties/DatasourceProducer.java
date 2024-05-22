package com.quarkus.esb.properties;

import io.smallrye.config.ConfigMapping;



@ConfigMapping(prefix = "sql.producer")

public interface DatasourceProducer {
	
	public  String getQueryConsulta();	
	
}