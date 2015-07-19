<div class="formulario">

	<h3><fmt:message key="formTrocaSenha.titulo" /></h3>

	<form action="<c:url value="/troca-senha"/>" method="post">

		<table>
			<tr>
				<td class="label"><label for="usuario.senha"><fmt:message key="formTrocaSenha.senha" /></label></td>
				<td><input type="password" name="usuario.senha" /> <i><fmt:message key="usuario.senha.regra" /></i></td>
			</tr>
			<tr>
				<td class="label"><label for="usuario.confirmaSenha"><fmt:message key="usuario.confirmaSenha" /></label></td>
				<td><input type="password" name="usuario.confirmaSenha" /></td>
			</tr>
			<tr>
				<td></td>
				<td><button name="_method" value="PUT"><fmt:message key="formTrocaSenha.enviar" /></button></td>
			</tr>
		</table>
		
	</form>
</div>