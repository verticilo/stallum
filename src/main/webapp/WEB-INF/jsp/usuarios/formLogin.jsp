<div class="formulario">

	<h3><fmt:message key="formLogin.titulo" /></h3>

	<form action="<c:url value='/login' />" method="post">
		
		<div style="margin: 20px 40px;">
			<a href="<c:url value='/usuarios/novo' />" ><h4><fmt:message key="formLogin.mensagem1" /></h4></a>
		</div>
		 
		<table>
			<tr>
				<td class="label"><label for="usuario.email"><fmt:message key="usuario.email" /></label></td>
				<td><input class="email" type="text" class="textField" name="usuario.email" size="40" /></td>
			</tr>
			<tr>
				<td class="label"><label for="usuario.senha"><fmt:message key="usuario.senha" /></label></td>
				<td><input type="password" class="textField" name="usuario.senha" /> <a href="<c:url value="/redefinir-senha"/>"><fmt:message key="formLogin.esqueceu" /></a></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="checkbox" name="usuario.lembrar" checked="checked" /> <fmt:message key="formLogin.lembrar" /></td>
			</tr>
			<tr>
				<td></td>
				<td><button><fmt:message key="formLogin.enviar" /></button>
			</tr>
		</table>
	</form>
</div>