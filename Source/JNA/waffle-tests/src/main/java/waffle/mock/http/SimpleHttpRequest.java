/*
 * Waffle (https://github.com/Waffle/waffle)
 *
 * Copyright (c) 2010-2020 Application Security, Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors: Application Security, Inc.
 */
package waffle.mock.http;

import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.mockito.Mockito;

/**
 * The Class SimpleHttpRequest.
 *
 * @author dblock[at]dblock[dot]org
 */
public class SimpleHttpRequest extends HttpServletRequestWrapper {

    /** The remote port s. */
    private static int remotePortS = 0;

    /** The request uri. */
    private String requestURI;

    /** The query string. */
    private String queryString;

    /** The remote user. */
    private String remoteUser;

    /** The method. */
    private String method = "GET";

    /** The remote host. */
    private String remoteHost;

    /** The remote addr. */
    private String remoteAddr;

    /** The remote port. */
    private int remotePort = -1;

    /** The headers. */
    private final Map<String, String> headers = new HashMap<>();

    /** The parameters. */
    private final Map<String, String> parameters = new HashMap<>();

    /** The content. */
    private byte[] content;

    /** The session. */
    private HttpSession session = new SimpleHttpSession();

    /** The principal. */
    private Principal principal;

    /**
     * Instantiates a new simple http request.
     */
    public SimpleHttpRequest() {
        super(Mockito.mock(HttpServletRequest.class));
        this.remotePort = SimpleHttpRequest.nextRemotePort();
    }

    /**
     * Next remote port.
     *
     * @return the int
     */
    public static synchronized int nextRemotePort() {
        return ++SimpleHttpRequest.remotePortS;
    }

    /**
     * Reset remote port.
     */
    public static synchronized void resetRemotePort() {
        SimpleHttpRequest.remotePortS = 0;
    }

    /**
     * Adds the header.
     *
     * @param headerName
     *            the header name
     * @param headerValue
     *            the header value
     */
    public void addHeader(final String headerName, final String headerValue) {
        this.headers.put(headerName, headerValue);
    }

    @Override
    public String getHeader(final String headerName) {
        return this.headers.get(headerName);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(this.headers.keySet());
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public int getContentLength() {
        return this.content == null ? -1 : this.content.length;
    }

    @Override
    public int getRemotePort() {
        return this.remotePort;
    }

    /**
     * Sets the method.
     *
     * @param methodName
     *            the new method
     */
    public void setMethod(final String methodName) {
        this.method = methodName;
    }

    /**
     * Sets the content length.
     *
     * @param length
     *            the new content length
     */
    public void setContentLength(final int length) {
        this.content = new byte[length];
    }

    /**
     * Sets the remote user.
     *
     * @param username
     *            the new remote user
     */
    public void setRemoteUser(final String username) {
        this.remoteUser = username;
    }

    @Override
    public String getRemoteUser() {
        return this.remoteUser;
    }

    @Override
    public HttpSession getSession() {
        return this.session;
    }

    @Override
    public HttpSession getSession(final boolean create) {
        if (this.session == null && create) {
            this.session = new SimpleHttpSession();
        }
        return this.session;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    /**
     * Sets the query string.
     *
     * @param query
     *            the new query string
     */
    public void setQueryString(final String query) {
        this.queryString = query;
        if (this.queryString != null) {
            for (final String eachParameter : this.queryString.split("[&]", -1)) {
                final String[] pair = eachParameter.split("=", -1);
                final String value = pair.length == 2 ? pair[1] : "";
                this.addParameter(pair[0], value);
            }
        }
    }

    /**
     * Sets the request uri.
     *
     * @param uri
     *            the new request uri
     */
    public void setRequestURI(final String uri) {
        this.requestURI = uri;
    }

    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    @Override
    public String getParameter(final String parameterName) {
        return this.parameters.get(parameterName);
    }

    /**
     * Adds the parameter.
     *
     * @param parameterName
     *            the parameter name
     * @param parameterValue
     *            the parameter value
     */
    public void addParameter(final String parameterName, final String parameterValue) {
        this.parameters.put(parameterName, parameterValue);
    }

    @Override
    public String getRemoteHost() {
        return this.remoteHost;
    }

    /**
     * Sets the remote host.
     *
     * @param value
     *            the new remote host
     */
    public void setRemoteHost(final String value) {
        this.remoteHost = value;
    }

    @Override
    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    /**
     * Sets the remote addr.
     *
     * @param value
     *            the new remote addr
     */
    public void setRemoteAddr(final String value) {
        this.remoteAddr = value;
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    /**
     * Sets the user principal.
     *
     * @param value
     *            the new user principal
     */
    public void setUserPrincipal(final Principal value) {
        this.principal = value;
    }
}
