<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'PLANO_CONTAS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formContaGerencial" class="formulario" action="<c:url value="/planoContas"/>" method="post">

    <input type="hidden" name="contaGerencial.id" value="${contaGerencial.id}" />
    <input type="hidden" name="contaGerencial.saldo" value="<fmt:formatNumber type="NUMBER" value="${contaGerencial.saldo}" minFractionDigits="2" />" />
    <input type="hidden" name="contaGerencial.versao" value="${contaGerencial.versao}" />

	<h2><fmt:message key="formContaGerencial.titulo" /></h2>
	
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaGerencial.numero" /> <span>*</span><br/>
			<input class="mask2" title="<fmt:message key="formato.contaGerencial" />" name="contaGerencial.numero" value="${contaGerencial.numero}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="contaGerencial.nome" /> <span>*</span><br/>
			<input type="text" name="contaGerencial.nome" value="${contaGerencial.nome}" size="45" />
		</div>
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaGerencial.totalizadora" /><br/>
			<input type="checkbox" name="contaGerencial.totalizadora" <c:if test="${contaGerencial.totalizadora}">checked="checked"</c:if> />
		</div>
		<div class="coluna">
			<fmt:message key="contaGerencial.saldo" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="contaGerencial.saldo" value="<fmt:formatNumber type="NUMBER" value="${contaGerencial.saldo}" minFractionDigits="2" />" size="45" disabled="disabled" />
		</div>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty contaGerencial.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/planoContas/${contaGerencial.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/planoContas/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="contaGerencial.remover" />">
  <p><fmt:message key="contaGerencial.remover.msg" /></p>
</div>