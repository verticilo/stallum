<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'APONTAMENTOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formApontamento" class="formulario" action="<c:url value="/apontamentos/detalhar"/>" method="post">

	<h2><fmt:message key="formApontamento.titulo" /></h2>
	
	<div>
		<div class="linha">
			<div class="subColuna obrigatorio">
				<fmt:message key="apontamento.data" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="apontamento.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${apontamento.data}" />" size="15" <c:if test="${not empty pontos}">disabled="disabled"</c:if> />
			</div>
			<div class="subColuna">
				<fmt:message key="apontamento.feriado" /><br/>
				<input id="feriado" type="checkbox" name="apontamento.feriado" <c:if test="${apontamento.feriado}">checked="checked"</c:if> />
			</div>
			<div class="subColuna" id="sindicato" <c:if test="${not apontamento.feriado}">style="display: none;"</c:if> >
				<fmt:message key="apontamento.sindicato" /><br/>
				<select name="apontamento.sindicato.id" <c:if test="${not empty apontamento.versao and not empty pontos}">disabled="disabled"</c:if> >
					<option value=""><fmt:message key="todos" /></option>
					<c:forEach items="${sindicatos}" var="sindicato" >
						<option value="${sindicato.id}" <c:if test="${sindicato == apontamento.obra.sindicato}" > selected="selected"</c:if> >${sindicato.nome} </option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
		
	<div>
		<div class="linha">
			<fmt:message key="apontamento.obs" /><br/>
			<textarea name="apontamento.obs" rows="4" cols="45">${apontamento.obs}</textarea>
		</div>
	</div>

	<div>
		<div class="linha">
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<script>
$(function() {
	
	$("#feriado").change(function() {
		feriado();
	});

	function feriado() {
	
		if ($("#feriado").is(":checked")) {
			$("#sindicato").show();			
		} else {
			$("#sindicato").hide();	
		}
	
	}
			
});
</script>