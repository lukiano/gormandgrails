<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter" %>
<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.web.WebAttributes" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Spring Twitter</title>
</head>
<body>
<div>
    <c:if test="${not empty param.login_error}">
        <div class="error">
            Your login attempt was not successful, try again.<br/><br/>
            Reason: <%= ((AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage() %>
        </div>
    </c:if>
    <form name="f" action="spring_security_login" method="post">
        <fieldset>
            <legend>Login Information</legend>
            <p>
                <label for="j_username">User:</label>
                <br/>
                <input type="text" name="j_username" id="j_username"
                       <c:if test="${not empty param.login_error}">value="<%= session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY) %>"
                </c:if> />
            </p>
            <p>
                <label for="j_password">Password:</label>
                <br/>
                <input type="password" name="j_password" id="j_password"/>
            </p>
            <p>
                <input type="checkbox" name="_spring_security_remember_me" id="remember_me"/>
                <label for="remember_me">Don't ask for my password for two weeks:</label>
            </p>
            <p>
                <button id="submit" type="submit">Login</button>
            </p>
        </fieldset>
    </form>
    <p>
        <a href="signup">Sign up with a new user</a>
    </p>
</div>
</body>
</html>