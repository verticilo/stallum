<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'CLIENTES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formCliente" class="formulario" action="<c:url value="/clientes"/>" method="post">

    <input type="hidden" name="cliente.id" value="${cliente.id}" />
    <input type="hidden" name="cliente.ativo" value="${cliente.ativo}" />
    <input type="hidden" name="cliente.versao" value="${cliente.versao}" />
    <input type="hidden" name="cliente.endereco.id" value="${cliente.endereco.id}" />
    <input type="hidden" name="cliente.endereco.versao" value="${cliente.endereco.versao}" />

	<h2><fmt:message key="formCliente.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="cliente.razaoSocial" /> <span>*</span><br/>
			<input type="text" name="cliente.razaoSocial" value="${cliente.razaoSocial}" size="100" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="cliente.nomeCurto" /><br/>
			<input type="text" name="cliente.nomeCurto" value="${cliente.nomeCurto}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="cliente.email" /> <span>*</span><br/>
			<input class="email" type="text" name="cliente.email" value="${cliente.email}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="cliente.contato" /><br/>
			<input type="text" name="cliente.contato" value="${cliente.contato}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="cliente.funcao" /><br/>
			<input type="text" name="cliente.funcao" value="${cliente.funcao}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="cliente.cnpj" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.cnpj" />" name="cliente.cnpj" value="${cliente.cnpj}" size="45" /> 
		</div>
		<div class="coluna">
			<fmt:message key="cliente.inscricao" /> <br/>
			<input type="text" name="cliente.inscricao" value="${cliente.inscricao}" size="45" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="cliente.ddd1" /><br/>
				<input type="text" name="cliente.ddd1" value="${cliente.ddd1}" size="2" />
			</div>
			<div class="subColuna obrigatorio">
				<fmt:message key="cliente.telefone1" /> <span>*</span><br/>
				<input type="text" name="cliente.telefone1" value="${cliente.telefone1}" size="15" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="cliente.ddd2" /><br/>
				<input type="text" name="cliente.ddd2" value="${cliente.ddd2}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="cliente.telefone2" /><br/>
				<input type="text" name="cliente.telefone2" value="${cliente.telefone2}" size="15" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cep" /> <span>*</span><br/>
			<input id="cep" type="text" style="float:left;" name="cliente.endereco.cep" value="${cliente.endereco.cep}" size="15" />
			<button id="buscaCep" class="imgBtn pesquisar" style="float:left;" type="button" title="<fmt:message key="endereco.buscarCep" />"></button>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.logradouro" /> <span>*</span><br/>
			<input id="rua" type="text" name="cliente.endereco.logradouro" value="${cliente.endereco.logradouro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna obrigatorio">
				<fmt:message key="endereco.numero" /> <span>*</span><br/>
				<input type="text" name="cliente.endereco.numero" value="${cliente.endereco.numero}" size="7" />
			</div>
			<div class="subColuna">
				<fmt:message key="endereco.complemento" /><br/>
				<input type="text" name="cliente.endereco.complemento" value="${cliente.endereco.complemento}" size="20" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.bairro" /><br/>
			<input id="bairro" type="text" name="cliente.endereco.bairro" value="${cliente.endereco.bairro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cidade" /> <span>*</span><br/>
			<input id="cidade" type="text" name="cliente.endereco.cidade" value="${cliente.endereco.cidade}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.estado" /> <span>*</span><br/>
			<select id="uf" name="cliente.endereco.estado">
				<c:forEach items="${estados}" var="estado">
					<option value="${estado}" <c:if test="${estado == cliente.endereco.estado}" > selected="selected"</c:if> >${estado.nome} </option>
				</c:forEach>
			</select>
		</div>
	</div>	
	
	<div>
		<div class="linha">
			<c:if test="${not empty cliente.id}">
				<c:if test="${cliente.ativo}">
					<a class="lnkRemover imgBtn" href="<c:url value="/clientes/${cliente.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				</c:if>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/clientes/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
	<c:if test="${not empty cliente.id and not cliente.ativo}">
		<a style="float:right;" href="<c:url value="/clientes/${cliente.id}/recuperar"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="recuperar"/>" /></a>
		<span style="float:right; margin: 7px;"><fmt:message key="alteradoEm" /> ${cliente.alteradoEm}</span>
	</c:if>
		
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="cliente.remover" />">
  <p><fmt:message key="cliente.remover.msg" /></p>
</div>