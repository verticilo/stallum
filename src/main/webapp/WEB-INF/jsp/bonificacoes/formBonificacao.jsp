<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'BONIFICACOES'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">


<form id="formBonificacao" class="formulario" action="<c:url value="/bonificacoes"/>" method="post">

	<input type="hidden" name="bonificacao.id" value="${bonificacao.id}" />
 	<input type="hidden" name="bonificacao.versao" value="${bonificacao.versao}" />
 	
	<h2><fmt:message key="formBonificacao.titulo" /></h2>
	
	<div>
		<div class="coluna obrigatorio">
			<fmt:message key="bonificacao.data" /> <span>*</span><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="bonificacao.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${bonificacao.data}" />" size="15" />
		</div>			
		<div class="coluna">
		</div>			
	</div>
		
	<div>
		<div class="linha obrigatorio" style="height: auto;">

			<div class="lista">
			
			<div class="cabecalho">
				<div style="float: left; width: 250px;"><fmt:message key="valeRefeicao.funcionario" /></div>
				<div style="float: left; width: 90px;"><fmt:message key="valeRefeicao.valor" /></div>
				<div style="float: left; width: 200px;"><fmt:message key="valeRefeicao.obs" /></div>
			</div>
			
			<c:forEach items="${vales}" var="vale" varStatus="idx">
				<input type="hidden" name="vales[${idx.count}].id" value="${vale.id}" />
				<input type="hidden" name="vales[${idx.count}].versao" value="${vale.versao}" />
				<input type="hidden" name="vales[${idx.count}].funcionario.id" value="${vale.funcionario.id}" />
				<input type="hidden" name="vales[${idx.count}].funcionario.nome" value="${vale.funcionario.nomeCurto}" />
				<input type="hidden" name="vales[${idx.count}].funcionario.versao" value="${vale.funcionario.versao}" />
				<div class="linha" style="height: auto; padding: 0;">
					<div style="float: left; width: 250px;"><input type="checkbox" name="vales[${idx.count}].recebe" <c:if test="${vale.recebe}">checked="checked"</c:if> /> ${vale.funcionario.nomeCurto}</div>
					<div style="float: left; width: 90px;"><input class="mask" title="<fmt:message key="formato.valor" />" type="text" name="vales[${idx.count}].valor" value="<fmt:formatNumber type="NUMBER" value="${vale.valor}" minFractionDigits="2" maxFractionDigits="2" />" size="10" /></div>
					<div style="float: left; width: 200px;"><input type="text" name="vales[${idx.count}].obs" value="${vale.obs}" size="40" /></div>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>
	
	<div>
		<div class="linha">
			<fmt:message key="bonificacao.obs" /><br/>
			<textarea name="bonificacao.obs" rows="4" cols="45">${bonificacao.obs}</textarea>
		</div>
	</div>

	<div>
		<div class="linha">
			<c:if test="${not empty bonificacao.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/bonificacoes/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>