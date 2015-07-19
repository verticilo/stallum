<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'EMPRESAS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formEmpresa" class="formulario" action="<c:url value="/empresas"/>" method="post">

    <input type="hidden" name="empresa.id" value="${empresa.id}" />
    <input type="hidden" name="empresa.ativa" value="${empresa.ativa}" />
    <input type="hidden" name="empresa.versao" value="${empresa.versao}" />
    <input type="hidden" name="empresa.endereco.id" value="${empresa.endereco.id}" />
    <input type="hidden" name="empresa.endereco.versao" value="${empresa.endereco.versao}" />

	<h2><fmt:message key="formEmpresa.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<fmt:message key="empresa.razaoSocial" /> <span>*</span><br/>
			<input type="text" name="empresa.razaoSocial" value="${empresa.razaoSocial}" size="100" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="empresa.nomeCurto" /> <span>*</span><br/>
			<input type="text" name="empresa.nomeCurto" value="${empresa.nomeCurto}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="empresa.email" /> <span>*</span><br/>
			<input class="email" type="text" name="empresa.email" value="${empresa.email}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="empresa.cnpj" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.cnpj" />" name="empresa.cnpj" value="${empresa.cnpj}" size="45" /> 
		</div>
		<div class="coluna">
			<fmt:message key="empresa.inscricao" /> <br/>
			<input type="text" name="empresa.inscricao" value="${empresa.inscricao}" size="45" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="empresa.ddd1" /><br/>
				<input type="text" name="empresa.ddd1" value="${empresa.ddd1}" size="2" />
			</div>
			<div class="subColuna obrigatorio">
				<fmt:message key="empresa.telefone1" /> <span>*</span><br/>
				<input type="text" name="empresa.telefone1" value="${empresa.telefone1}" size="15" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="empresa.ddd2" /><br/>
				<input type="text" name="empresa.ddd2" value="${empresa.ddd2}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="empresa.telefone2" /><br/>
				<input type="text" name="empresa.telefone2" value="${empresa.telefone2}" size="15" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cep" /> <span>*</span><br/>
			<input id="cep" type="text" style="float:left;" name="empresa.endereco.cep" value="${empresa.endereco.cep}" size="15" />
			<button id="buscaCep" class="imgBtn pesquisar" style="float:left;" type="button" title="<fmt:message key="endereco.buscarCep" />"></button>
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.logradouro" /> <span>*</span><br/>
			<input id="rua" type="text" name="empresa.endereco.logradouro" value="${empresa.endereco.logradouro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna obrigatorio">
				<fmt:message key="endereco.numero" /> <span>*</span><br/>
				<input type="text" name="empresa.endereco.numero" value="${empresa.endereco.numero}" size="7" />
			</div>
			<div class="subColuna">
				<fmt:message key="endereco.complemento" /><br/>
				<input type="text" name="empresa.endereco.complemento" value="${empresa.endereco.complemento}" size="20" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.bairro" /><br/>
			<input id="bairro" type="text" name="empresa.endereco.bairro" value="${empresa.endereco.bairro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.cidade" /> <span>*</span><br/>
			<input id="cidade" type="text" name="empresa.endereco.cidade" value="${empresa.endereco.cidade}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="endereco.estado" /> <span>*</span><br/>
			<select id="uf" name="empresa.endereco.estado">
				<c:forEach items="${estados}" var="estado">
					<option value="${estado}" <c:if test="${estado == empresa.endereco.estado}" > selected="selected"</c:if> >${estado.nome} </option>
				</c:forEach>
			</select>
		</div>
	</div>	
	
	<div>
		<div class="linha">
			<c:if test="${not empty empresa.id}">
				<a class="lnkRemover imgBtn" href="<c:url value="/empresas/${empresa.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/empresas/nova"/>" title="<fmt:message key="nova" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="empresa.remover" />">
  <p><fmt:message key="empresa.remover.msg" /></p>
</div>