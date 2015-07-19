<div class="formulario">

	<h3><fmt:message key="confirmacaoEmail.titulo" /></h3>

	<div style="margin: 30px 80px;">
		<form action="<c:url value='/confirmacao-email' />" method="post">
			<fmt:message key="confirmacaoEmail.mensagem1" /><br/>
			<c:if test="${empty sessaoUsuario or not sessaoUsuario.logado}">
				<br/>
				<fmt:message key="confirmacaoEmail.mensagem2" /><br/>
				<input type="text" class="textField" name="email" size="40" /><br/>							
			</c:if>
			<br/>
			<button type="submit" name="_method" value="PUT"><fmt:message key="confirmacaoEmail.enviar" /></button>
		</form>
	</div>

</div>