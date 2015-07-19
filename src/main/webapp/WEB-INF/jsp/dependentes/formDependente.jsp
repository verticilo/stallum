<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'FUNCIONARIOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formDependente" class="formulario" action="<c:url value="/dependentes"/>" method="post">

    <input type="hidden" name="dependente.id" value="${dependente.id}" />
    <input type="hidden" name="dependente.versao" value="${dependente.versao}" />
    <input type="hidden" name="dependente.funcionario.id" value="${dependente.funcionario.id}" />
    <input type="hidden" name="dependente.funcionario.versao" value="${dependente.funcionario.versao}" />
    
	<h2><fmt:message key="formDependente.titulo" /></h2>
	
	<div>
		<div class="linha">
			<fmt:message key="dependente.funcionario" /><br/>
			<input type="text" name="dependente.funcionario.nome" value="${dependente.funcionario.nome}" size="95" disabled="disabled" />
		</div>		
	</div>
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="dependente.nome" /> <span>*</span><br/>
			<input type="text" name="dependente.nome" value="${dependente.nome}" size="95" />
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="dependente.dataNascimento" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="dependente.dataNascimento" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${dependente.dataNascimento}" />" size="40" />
		</div>
		<div class="coluna">
		
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty dependente.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/dependentes/${dependente.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/funcionarios/${dependente.funcionario.id}/dependentes/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="dependente.remover" />">
  <p><fmt:message key="dependente.remover.msg" /></p>
</div>