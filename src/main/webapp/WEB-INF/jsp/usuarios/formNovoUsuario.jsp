<div class="formulario" style="width: 530px;">

	<h3><fmt:message key="formNovoUsuario.titulo" /></h3>

	<form action="<c:url value='/usuarios' />" method="post">
		
		<table>
		
		<tr>
			<td class="label requerido"><label for="usuario.nome">*<fmt:message key="usuario.nome" /></label></td>
			<td><input type="text" name="usuario.nome" value="${usuario.nome}" size="35" /></td>
		</tr>
		<tr>
			<td class="label requerido"><label for="usuario.email">*<fmt:message key="usuario.email" /></label></td>
			<td><input class="email" type="text" name="usuario.email" value="${usuario.email}" size="35" /></td>
		</tr>
		<tr>
			<td class="label"><label for="usuario.dataNascimento"><fmt:message key="usuario.dataNascimento" /></label></td>
			<td>
				<input type="text" name="usuario.dataNascimento" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${usuario.dataNascimento }" />" /> 
				<i>(<fmt:message key="formato.maskData" />)</i>
			</td>
		</tr>
		<tr>
			<td class="label requerido"><label for="usuario.senha">*<fmt:message key="usuario.senha" /></label></td>
			<td><input type="password" name="usuario.senha" /> <i><fmt:message key="usuario.senha.regra" /></i></td>
		</tr>
		<tr>
			<td class="label requerido"><label for="usuario.confirmaSenha">*<fmt:message key="usuario.confirmaSenha" /></label></td>
			<td><input type="password" name="usuario.confirmaSenha" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="checkbox" name="usuario.newsletter" <c:if test="${usuario.newsletter }">checked="checked"</c:if> /><fmt:message key="usuario.newsletter" /></td>
		</tr>
		<tr>
			<td></td>
			<td><button><fmt:message key="formNovoUsuario.enviar" /></button></td>
		</tr>
		
		</table>
		
	</form>
</div>

<div class="instrucoes" style="width: 360px;">
	
	<h3><fmt:message key="formNovoUsuario.observacoes" /></h3>

	<ol>
		<li><fmt:message key="forms.obs.campoRequerido" /></li>
		<li><fmt:message key="formNovoUsuario.observacao1" /></li>
		<li><fmt:message key="formNovoUsuario.observacao2" /></li>
		<li><fmt:message key="formNovoUsuario.observacao3" /></li>
		<li><fmt:message key="formNovoUsuario.observacao4" /></li>
	</ol>

</div>