<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'FUNCOES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formFuncao" class="formulario" action="<c:url value="/funcoes"/>" method="post">

    <input type="hidden" name="funcao.id" value="${funcao.id}" />
    <input type="hidden" name="funcao.versao" value="${funcao.versao}" />

	<h2><fmt:message key="formFuncao.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="funcao.nome" /> <span>*</span><br/>
			<input type="text" name="funcao.nome" value="${funcao.nome}" size="100" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcao.sindicato" /> <span>*</span><br/>
			<select id="uf" name="funcao.sindicato.id">
				<option value=""></option>
				<c:forEach items="${sindicatos}" var="sindicato">
					<option value="${sindicato.id}" <c:if test="${sindicato == funcao.sindicato}" > selected="selected"</c:if> >${sindicato.nome} </option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="funcao.salario" /> <span>*</span><br/>
			<input class="mask" type="text" name="funcao.salario" title="<fmt:message key="formato.valor" />" value="<fmt:formatNumber type="NUMBER" value="${funcao.salario}" minFractionDigits="2" />" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="dissidio.data" /><br/>
			<input type="text" class="mask mask" title="<fmt:message key="formato.maskData" />" name="funcao.dataUltimoDissidio" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${funcao.dataUltimoDissidio}" />" size="45" <c:if test="${not empty funcao.id}">disabled="disabled"</c:if> />
		</div>
		<div class="coluna">
			<fmt:message key="dissidio.percReajuste" /><br/>
			<input class="mask" type="text" title="<fmt:message key="formato.percentual" />" name="funcao.percUltimoDissidio" value="<fmt:formatNumber type="NUMBER" value="${funcao.percUltimoDissidio}" minFractionDigits="2" />" size="45" <c:if test="${not empty funcao.id}">disabled="disabled"</c:if> />
		</div>
	</div>	

	<div>
		<div class="linha">
			<c:if test="${not empty funcao.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/funcoes/${funcao.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/funcoes/nova"/>" title="<fmt:message key="nova" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="funcao.remover" />">
  <p><fmt:message key="funcao.remover.msg" /></p>
</div>