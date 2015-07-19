<div class="leftBar">

	<c:if test="${sessaoUsuario.operador}">

	    <div class="menuBox">
	        <ul>
	        <c:forEach items="${sessaoUsuario.menu}" var="item">
	        	<li><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	        </c:forEach>
	        </ul>
	    </div>
    
    </c:if>
    
</div> <!-- leftBar -->

<div class="home">



</div> <!-- home -->