<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->


<div class="content">

<form id="formUsuario" class="formulario" action="<c:url value="/usuarios/salvar"/>" method="post">

    <input type="hidden" name="usuario.id" value="${usuario.id}" />
    <input type="hidden" name="usuario.versao" value="${usuario.versao}" />
    <input type="hidden" name="usuario.endereco.id" value="${usuario.endereco.id}" />
    <input type="hidden" name="usuario.endereco.versao" value="${usuario.endereco.versao}" />
	<c:if test="${not sessaoUsuario.administrador or sessaoUsuario.idUsuario == usuario.id}">
		<input type="hidden" name="usuario.perfil" value="${usuario.perfil}" />
	</c:if>

	<h2><fmt:message key="formUsuario.titulo" /></h2>
	
	<c:if test="${sessaoUsuario.administrador}">
		<div>
			<div class="linha obrigatorio">
				<fmt:message key="usuario.perfil" /> <span>*</span><br/>
				<c:if test="${sessaoUsuario.idUsuario != usuario.id}">
					<select name="usuario.perfil">
						<c:forEach items="${perfis}" var="perfil">
							<option value="${perfil}" <c:if test="${perfil == usuario.perfil}" > selected="selected"</c:if> >${perfil.nome}</option>
						</c:forEach>
					</select>
				</c:if>
				<c:if test="${sessaoUsuario.idUsuario == usuario.id}">
					<input type="text" value="${usuario.perfil.nome}" disabled="disabled" size="45" />
				</c:if>
			</div>
		</div>
	</c:if>	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="usuario.nome" /> <span>*</span><br/>
			<input type="text" name="usuario.nome" value="${usuario.nome}" size="45" />
		</div>
		<div class="coluna obrigatorio">
			<fmt:message key="usuario.email" /> <span>*</span><br/>
			<input class="email" type="text" name="usuario.email" value="${usuario.email}" size="45" />
		</div>
	</div>	
	<div>
		<c:if test="${empty usuario.id}">
			<div class="coluna obrigatorio">
				<fmt:message key="usuario.senha" /> <span>*</span><br/>
				<input type="password" name="usuario.senha" value="${usuario.senha}" size="45" />
			</div>
			<div class="coluna obrigatorio">
				<fmt:message key="usuario.confirmaSenha" /> <span>*</span><br/>
				<input type="password" name="usuario.confirmaSenha" value="${usuario.confirmaSenha}" size="45" />
			</div>
		</c:if>
		<c:if test="${not empty usuario.id}">
			<div class="coluna">
				<fmt:message key="usuario.senha" /><br/>
				<input type="password" name="usuario.senha" value="${usuario.senha}" size="45" />
			</div>
			<div class="coluna">
				<fmt:message key="usuario.confirmaSenha" /><br/>
				<input type="password" name="usuario.confirmaSenha" value="${usuario.confirmaSenha}" size="45" />
			</div>
		</c:if>
	</div>	
	<div>
		<div class="coluna">
			<fmt:message key="usuario.apelido" /><br/>
			<input type="text" name="usuario.apelido" value="${usuario.apelido}" size="45" />
		</div>
		<div class="coluna">
			<fmt:message key="usuario.dataNascimento" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="usuario.dataNascimento" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${usuario.dataNascimento}" />" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="usuario.cpf" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.cpf" />" name="usuario.cpf" value="${usuario.cpf}" size="45" /> 
		</div>
		<div class="coluna">
			<fmt:message key="usuario.rg" /><br/>
			<input type="text" name="usuario.rg" value="${usuario.rg}" size="45" />
		</div>
	</div>	
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="usuario.dddTelefone" /><br/>
				<input type="text" name="usuario.dddTelefone" value="${usuario.dddTelefone}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="usuario.telefone" /><br/>
				<input type="text" name="usuario.telefone" value="${usuario.telefone}" size="15" />
			</div>
		</div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="usuario.dddCelular" /><br/>
				<input type="text" name="usuario.dddCelular" value="${usuario.dddCelular}" size="2" />
			</div>
			<div class="subColuna">
				<fmt:message key="usuario.celular" /><br/>
				<input type="text" name="usuario.celular" value="${usuario.celular}" size="15" />
			</div>
		</div>
	</div>	
	<div>
		<div class="coluna">
				<fmt:message key="endereco.cep" /><br/>
				<input id="cep" type="text" style="float:left;" name="usuario.endereco.cep" value="${usuario.endereco.cep}" size="15" />
				<button id="buscaCep" class="imgBtn pesquisar" style="float:left;" type="button" title="<fmt:message key="endereco.buscarCep" />"></button>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.logradouro" /><br/>
			<input id="rua" type="text" name="usuario.endereco.logradouro" value="${usuario.endereco.logradouro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<div class="subColuna">
				<fmt:message key="endereco.numero" /><br/>
				<input type="text" name="usuario.endereco.numero" value="${usuario.endereco.numero}" size="7" />
			</div>
			<div class="subColuna">
				<fmt:message key="endereco.complemento" /><br/>
				<input type="text" name="usuario.endereco.complemento" value="${usuario.endereco.complemento}" size="20" />
			</div>
		</div>
		<div class="coluna">
			<fmt:message key="endereco.bairro" /><br/>
			<input id="bairro" type="text" name="usuario.endereco.bairro" value="${usuario.endereco.bairro}" size="45" />
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="endereco.cidade" /><br/>
			<input id="cidade" type="text" name="usuario.endereco.cidade" value="${usuario.endereco.cidade}" size="45" />
		</div>
		<div class="coluna ">
			<fmt:message key="endereco.estado" /><br/>
			<select id="uf" name="usuario.endereco.estado">
				<c:forEach items="${estados}" var="estado">
					<option value="${estado}" <c:if test="${estado == usuario.endereco.estado}" >selected="selected"</c:if> >${estado.nome}</option>
				</c:forEach>
			</select>
		</div>
	</div>	
		
	<div>
		<div class="linha">
			<c:if test="${not empty usuario.id}">
				<c:if test="${sessaoUsuario.administrador}">
					<button type="button" class="imgBtn remover redir" value="<c:url value="/usuarios/${usuario.id}/excluir"/>" title="<fmt:message key="novo" />" ></button>
					<button type="button" class="imgBtn novo redir" value="<c:url value="/usuarios/incluir"/>" title="<fmt:message key="novo" />" ></button>
				</c:if>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>