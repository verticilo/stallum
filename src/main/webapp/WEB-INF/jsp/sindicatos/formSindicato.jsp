<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'SINDICATOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formSindicato" class="formulario" action="<c:url value="/sindicatos"/>" method="post">

    <input type="hidden" name="sindicato.id" value="${sindicato.id}" />
    <input type="hidden" name="sindicato.versao" value="${sindicato.versao}" />

	<h2><fmt:message key="formSindicato.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="sindicato.nome" /> <span>*</span><br/>
			<input type="text" name="sindicato.nome" value="${sindicato.nome}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="sindicato.salarioMinimo" /><br/>
			<input id="feriado" type="checkbox" name="sindicato.salarioMinimo" <c:if test="${sindicato.salarioMinimo}">checked="checked"</c:if> />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="sindicato.valorBeneficio" /><br/>
			<input class="mask" type="text" title="<fmt:message key="formato.valor" />" name="sindicato.valorBeneficio" value="<fmt:formatNumber type="NUMBER" value="${sindicato.valorBeneficio}" minFractionDigits="2" />" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="sindicato.dataDissidio" /> <span>*</span><br/>
			<input type="text" class="mask" title="99/99" name="sindicato.dataDissidio" value="${sindicato.dataDissidio}" size="9" />
		</div>
	</div>	

	<div>
		<div class="linha">
			<c:if test="${not empty sindicato.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/sindicatos/${sindicato.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/sindicatos/nova"/>" title="<fmt:message key="nova" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="sindicato.remover" />">
  <p><fmt:message key="sindicato.remover.msg" /></p>
</div>