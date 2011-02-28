<%-- 
    Document   : error
    Created on : 21 févr. 2011, 23:45:16
    Author     : kevin
--%>

<%@page isErrorPage="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Erreur</title>
    </head>
    <body>
        <h1>Erreur</h1>
        <p>
        <%
            String msg = "Pas de message d'erreur fourni !";

            if(request.getAttribute("error_msg") != null)
                msg = "Msg : "+(String) request.getAttribute("error_msg");

            out.print(msg);
        %>
        </p>
    </body>
</html>