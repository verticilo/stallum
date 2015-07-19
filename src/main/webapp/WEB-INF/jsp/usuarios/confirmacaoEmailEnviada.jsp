<div class="formulario">

	<h3><fmt:message key="confirmacaoEmailEnviada.titulo" /></h3>

	<div style="margin: 30px 80px;">
		<fmt:message key="confirmacaoEmailEnviada.mensagem1" /> <b>${sessaoUsuario.usuario.email }</b>
		<br/><br/>
		<fmt:message key="confirmacaoEmailEnviada.mensagem2" /><br/><br/>
		<fmt:message key="bemVindo.mensagem3" /><br/><br/>
		<fmt:message key="bemVindo.mensagem4" /><br/><br/><br/><br/>
		<a href="<c:url value="/"/>" style="display: block; text-align: center;"><fmt:message key="home.nome" /></a>
	</div>
	
</div>