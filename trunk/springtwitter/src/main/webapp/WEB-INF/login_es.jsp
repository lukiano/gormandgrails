<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.springframework.security.web.WebAttributes" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Spring Twitter</title>
</head>
<body>
<div>
    <c:if test="${not empty param.login_error}">
        <div class="error">
            No pudiste iniciar sesión. Intentá de nuevo.<br/><br/>
            Razón: <%= ((Throwable) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage() %>
        </div>
    </c:if>
    <form name="f" action="spring_security_login" method="post">
        <fieldset>
            <legend>Información de inicio de sesión</legend>
            <p>
                <label for="j_username">Usuario:</label>
                <br/>
                <input type="text" name="j_username" id="j_username"
                       <c:if test="${not empty param.login_error}">value="<%= request.getParameter("login_name") %>"
                </c:if> />
            </p>
            <p>
                <label for="j_password">Clave:</label>
                <br/>
                <input type="password" name="j_password" id="j_password"/>
            </p>
            <p>
                <input type="checkbox" name="_spring_security_remember_me" id="remember_me"/>
                <label for="remember_me">No preguntarme la clave por dos semanas:</label>
            </p>
            <p>
                <button id="submit" type="submit">Login</button>
            </p>
        </fieldset>
    </form>
    <p>
        <a href="signup">Registrarse con un usuario nuevo</a>
    </p>
</div>
</body>
</html>