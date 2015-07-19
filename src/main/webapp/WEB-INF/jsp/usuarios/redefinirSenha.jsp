<div class="formulario">

	<h3><fmt:message key="redefinirSenha.titulo" /></h3>

	<div style="margin: 20px 80px;">
		<form action="<c:url value='/redefinir-senha' />" method="post">
			<fmt:message key="redefinirSenha.mensagem1" /><br/><br/>
			<fmt:message key="redefinirSenha.mensagem2" /><br/>
			<input type="text" class="textField" name="email" size="40" />							
			<br/><br/>
			<button type="submit" name="_method" value="PUT"><fmt:message key="redefinirSenha.enviar" /></button>
		</form>
	</div>

</div>
