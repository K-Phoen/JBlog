<%@page import="metier.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="../../jspf/header.jspf" %>

<h2>Gestion des utilisateurs</h2>

<%
List<User> list = (List<User>) request.getAttribute("elems");
%>

<table>
	<thead>
		<tr>
            <th>Identifiant</th>
            <th>Nom</th>
            <th>Mail</th>
			<th>Action</th>
		</tr>
	</thead>

	<tbody>
        <%
        for(User u : list) {
        %>
		<tr>
            <td><%= HTML.escape(u.getLogin()) %></td>
            <td><%= HTML.escape(u.getDisplayName()) %></td>
            <td><%= HTML.escape(u.getMail()) %></td>
			<td>
                <a href="./admin/users/edit/<%= u.getId() %>/">
                    <img src="images/pencil.png" alt="Editer" title="Editer" />
                </a>
                <a href="./admin/users/delete/<%= u.getId() %>/" onclick="return confirm('Etes-vous certain de vouloir supprimer cet utilisateur ?');">
                    <img src="images/delete.png" alt="Supprimer" title="Supprimer" />
                </a>
            </td>
		</tr>
		<%
        }
        %>
	</tbody>
</table>

<%@include file="../../jspf/footer.jspf" %>