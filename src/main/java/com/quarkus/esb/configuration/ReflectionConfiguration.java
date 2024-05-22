package com.quarkus.esb.configuration;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;

import javax.xml.xpath.XPathExpressionException;

import org.apache.camel.ValidationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.quarkus.esb.model.Modelo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.UnmarshalException;

@RegisterForReflection(targets = {
        ConnectException.class,
        SQLRecoverableException.class,
        SQLSyntaxErrorException.class,
        SQLException.class,
        NullPointerException.class,
        InvalidFormatException.class,
        UnmarshalException.class,
        JsonParseException.class,
        MarshalException.class,
        ValidationException.class,
        XPathExpressionException.class,
        String.class,
        SocketTimeoutException.class,
        java.lang.ClassCastException.class,
        Modelo.class
})
public class ReflectionConfiguration {
}
