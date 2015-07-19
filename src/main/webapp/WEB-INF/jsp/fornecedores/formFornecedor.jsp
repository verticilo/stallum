<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'FORNECEDORES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formFornecedor" class="formulario" action="<c:url value="/fornecedores"/>" method="post">

    <input type="hidden" name="fornecedor.id" value="${fornecedor.id}" />
    <input type="hidden" name="fornecedor.ativo" value="${fornecedor.ativo}" />
    <input type="hidden" name="fornecedor.versao" value="${fornecedor.versao}" />
    <input type="hidden" name="fornecedor.endereco.id" value="${fornecedor.endereco.id}" />
    <input type="hidden" name="fornecedor.endereco.versao" value="${fornecedor.endereco.versao}" />

	<h2><fmt:message key="formFornecedor.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="fornecedor.razaoSocial" /> <span>*</span><br/>
			<input type="text" name="fornecedor.razaoSocial" value="${fornecedor.razaoSocial}" size="100" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="fornecedor.nomeCurto" /><br/>
			<input type="text" name="fornecedor.nomeCurto" value="${fornecedor.nomeCurto}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="fornecedor.email" /> <span>*</span><br/>
			<input class="email" type="text" name="fornecedor.email" value="${fornecedor.email}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="fornecedor.contato" /><br/>
			<input type="text" name="fornecedor.contato" value="${fornecedor.contato}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="fornecedor.funcao" /><br/>
			<input type="text" name="fornecedor.funcao" value="${fornecedor.funcao}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="fornecedor.cnpj" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.cnpj" />" name="fornecedor.cnpj" value="${fornecedor.cnpj}" size="45" /> 
		</div>
		<div class="coluna">
			<fmt:message key="fornecedor.inscricao" /> <br/>
			<input type="text" name="fornecedor.inscricao" value="${fornecedor.inscricao}" size="45" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="fornecedor.ddd1" /><br/>
				<input type="text" name="fornecedor.ddd1" value="${fornecedor.ddd1}" size="2" />
			</div>
			<div class="subColuna obrigatorio">
				<fmt:message key="fornecedor.telefone1" /> <span>*</span><br/>
				<input type="text" name="fornecedor.telefone1" value="${fornecedor.telefone1}" size="15" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="fornecedor.ddd2" /><br/>
				<input type="text" name="fornecedor.ddd2" value="${fornecedor.ddd2}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="fornecedor.telefone2" /><br/>
				<input type="text" name="fornecedor.telefone2" value="${fornecedor.telefone2}" size="15" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cep" /> <span>*</span><br/>
			<input id="cep" type="text" style="float:left;" name="fornecedor.endereco.cep" value="${fornecedor.endereco.cep}" size="15" />
			<button id="buscaCep" class="imgBtn pesquisar" style="float:left;" type="button" title="<fmt:message key="endereco.buscarCep" />"></button>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.logradouro" /> <span>*</span><br/>
			<input id="rua" type="text" name="fornecedor.endereco.logradouro" value="${fornecedor.endereco.logradouro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna obrigatorio">
				<fmt:message key="endereco.numero" /> <span>*</span><br/>
				<input type="text" name="fornecedor.endereco.numero" value="${fornecedor.endereco.numero}" size="7" />
			</div>
			<div class="subColuna">
				<fmt:message key="endereco.complemento" /><br/>
				<input type="text" name="fornecedor.endereco.complemento" value="${fornecedor.endereco.complemento}" size="20" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.bairro" /><br/>
			<input id="bairro" type="text" name="fornecedor.endereco.bairro" value="${fornecedor.endereco.bairro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cidade" /> <span>*</span><br/>
			<input id="cidade" type="text" name="fornecedor.endereco.cidade" value="${fornecedor.endereco.cidade}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.estado" /> <span>*</span><br/>
			<select id="uf" name="fornecedor.endereco.estado">
				<c:forEach items="${estados}" var="estado">
					<option value="${estado}" <c:if test="${estado == fornecedor.endereco.estado}" > selected="selected"</c:if> >${estado.nome} </option>
				</c:forEach>
			</select>
		</div>
	</div>	
	
	<div>
		<div class="linha">
			<c:if test="${not empty fornecedor.id}">
				<c:if test="${fornecedor.ativo}">
					<a class="lnkRemover imgBtn" href="<c:url value="/fornecedores/${fornecedor.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				</c:if>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/fornecedores/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
	<c:if test="${not empty fornecedor.id and not fornecedor.ativo}">
		<a style="float:right;" href="<c:url value="/fornecedores/${fornecedor.id}/recuperar"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="recuperar"/>" /></a>
		<span style="float:right; margin: 7px;"><fmt:message key="alteradoEm" /> ${fornecedor.alteradoEm}</span>
	</c:if>
		
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="fornecedor.remover" />">
  <p><fmt:message key="fornecedor.remover.msg" /></p>
</div>