<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'CONTAS_CORRENTES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formContaCorrente" class="formulario" action="<c:url value="/contasCorrentes"/>" method="post">

    <input type="hidden" name="contaCorrente.id" value="${contaCorrente.id}" />
    <input type="hidden" name="contaCorrente.saldo" value="<fmt:formatNumber type="NUMBER" value="${contaCorrente.saldo}" minFractionDigits="2" />" />
    <input type="hidden" name="contaCorrente.versao" value="${contaCorrente.versao}" />

	<h2><fmt:message key="formContaCorrente.titulo" /></h2>
	
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaCorrente.nomeBanco" /> <span>*</span><br/>
			<input type="text" name="contaCorrente.nomeBanco" value="${contaCorrente.nomeBanco}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="contaCorrente.numBanco" /> <span>*</span><br/>
			<input class="email" name="contaCorrente.numBanco" value="${contaCorrente.numBanco}" size="45" />
		</div>
	</div>
	<div class="linha obrigatorio">
		<div class="coluna">
			<fmt:message key="contaCorrente.numAgencia" /> <span>*</span><br/>
			<input type="text" name="contaCorrente.numAgencia" value="${contaCorrente.numAgencia}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="contaCorrente.numConta" /> <span>*</span><br/>
			<input class="email" name="contaCorrente.numConta" value="${contaCorrente.numConta}" size="45" />
		</div>
	</div>
	<div class="linha">
		<div class="coluna">
			<fmt:message key="contaCorrente.saldo" /><br/>
			<input type="text" name="contaCorrente.saldo" value="<fmt:formatNumber type="NUMBER" value="${contaCorrente.saldo}" minFractionDigits="2" />" size="45" disabled="disabled" />
		</div>
		<div class="coluna">
		</div>
	</div>
	
	<div>
		<div class="linha">
			<c:if test="${not empty contaCorrente.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/contasCorrentes/${contaCorrente.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/contasCorrentes/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="contaCorrente.remover" />">
  <p><fmt:message key="contaCorrente.remover.msg" /></p>
</div>