<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'CENTROS_CUSTO'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formCentroCusto" class="formulario" action="<c:url value="/centrosCusto"/>" method="post">

    <input type="hidden" name="centroCusto.id" value="${centroCusto.id}" />
    <input type="hidden" name="centroCusto.versao" value="${centroCusto.versao}" />
    
	<h2><fmt:message key="formCentroCusto.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="centroCusto.nome" /> <span>*</span><br/>
			<input type="text" name="centroCusto.nome" value="${centroCusto.nome}" size="40" />
		</div>
		<div class="coluna">
			<fmt:message key="centroCusto.sindicato" /><br/>
			<select id="uf" name="centroCusto.sindicato.id">
				<option value=""><fmt:message key="centroCusto.todasObras" /></option>
				<c:forEach items="${sindicatos}" var="sindicato">
					<option value="${sindicato.id}" <c:if test="${sindicato == centroCusto.sindicato}" > selected="selected"</c:if> >${sindicato.nome} </option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div>		
		<div class="linha">
			<fmt:message key="centroCusto.obs" /><br/>
			<textarea name="centroCusto.obs" rows="5" cols="40">${centroCusto.obs}</textarea>			
		</div>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty centroCusto.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/centrosCusto/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
			
</form>

</div>