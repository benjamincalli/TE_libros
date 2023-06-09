<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page import ="java.util.List"%>
<%@page import ="com.emergentes.modelo.Libro"%>

<%
    List<Libro> lista=(List<Libro>)request.getAttribute("lista");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Listado de libros</h1>
        <p><a href="MainController?op=nuevo">Nuevo</a></p>
        <table border="1">
            <tr>
                <th>Id</th>
                <th>ISBN</th>
                <th>Titulo</th>
                <th>Categoria</th>
                <th></th>
                <th></th>
                
            </tr>

            <c:forEach var="libro" items="${lista}">
                <tr>
                    <td>${libro.id}</td>
                    <td>${libro.isbn}</td>
                    <td>${libro.titulo}</td>
                    <td>${libro.categoria}</td>
                    <td><a href="MainController?op=editar&id=${libro.id}">Editar</a></td>
                    <td><a href="MainController?op=eliminar&id=${libro.id}"
                           onclick="return(confirm('Esta seguro ???'))">Eliminar</a></td>
                           
                                  
                </tr>
            
            </c:forEach>
                        </table>
    </body>                   

</html>
