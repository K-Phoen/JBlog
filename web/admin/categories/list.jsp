<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@include file="../../jspf/header.jspf" %>

<h2>Gestion des catégories</h2>

<%
List<Category> list = (List<Category>) request.getAttribute("elems");

if(list.isEmpty()) {
%>
<p>
    Aucune catégorie à afficher.
</p>
<%
} else {
%>
<table>
	<thead>
		<tr>
            <th>Nom</th>
			<th>Action</th>
		</tr>
	</thead>

	<tbody>
        <%
        for(Category c : list) {
        %>
		<tr>
            <td><a href="./article/categorie/<%= HTML.escape(c.getSlug()) %>"><%= HTML.escape(c.getTitle()) %></a></td>
			<td>
                <a href="./admin/categories/edit/<%= c.getId() %>/">
                    <img src="images/pencil.png" alt="Editer" title="Editer" />
                </a>
                <a href="./admin/categories/delete/<%= c.getId() %>/" onclick="return confirm('Etes-vous certain de vouloir supprimer cette catégorie ?');">
                    <img src="images/delete.png" alt="Supprimer" title="Supprimer" />
                </a>
            </td>
		</tr>
		<%
        }
        %>
	</tbody>
</table>
<%
}
%>

<h2>Accès rapide</h2>

<ul>
    <li><a href="./admin/categories/new/">Créer une nouvelle catégorie</a></li>
    <li><a href="./admin/articles/">Gérer les articles</a></li>
</ul>

<%@include file="../../jspf/footer.jspf" %>