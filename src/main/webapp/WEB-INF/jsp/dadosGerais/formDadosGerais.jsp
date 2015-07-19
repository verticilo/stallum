<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'DADOS_GERAIS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formDadosGerais" class="formulario" action="<c:url value="/dadosGerais"/>" method="post">

    <input type="hidden" name="dadosGerais.id" value="${dadosGerais.id}" />
    <input type="hidden" name="dadosGerais.versao" value="${dadosGerais.versao}" />
    <input type="hidden" name="dadosGerais.valeRefeicao" value="${dadosGerais.valeRefeicao}" />
    

	<h2><fmt:message key="formDadosGerais.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<div class="subColuna">
				<fmt:message key="dadosGerais.data" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="dadosGerais.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${dadosGerais.data}" />" size="15" />
			</div>		
			<div class="subColuna">
				<fmt:message key="dadosGerais.descricao" /> <span>*</span><br/>
				<input type="text" name="dadosGerais.descricao" value="${dadosGerais.descricao}" size="80" />
			</div>		
		</div>		
	</div>
	<div>
		<div class="linha obrigatorio">
			<div class="subColuna">
				<fmt:message key="dadosGerais.tetoSalFamilia" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="dadosGerais.tetoSalFamilia" value="<fmt:formatNumber type="NUMBER" value="${dadosGerais.tetoSalFamilia}" minFractionDigits="2" />" size="18" />
			</div>			
			<div class="subColuna">
				<fmt:message key="dadosGerais.adicDependente" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.valor" />" name="dadosGerais.adicDependente" value="<fmt:formatNumber type="NUMBER" value="${dadosGerais.adicDependente}" minFractionDigits="2" />" size="18" />
			</div>			
			<div class="subColuna">
				<fmt:message key="dadosGerais.percAnuenio" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.percentual" />" name="dadosGerais.percAnuenio" value="<fmt:formatNumber type="NUMBER" value="${dadosGerais.percAnuenio}" minFractionDigits="2" />" size="12" />
			</div>			
			<div class="subColuna">
				<fmt:message key="dadosGerais.jornadaDiaMin" /> <span>*</span><br/>
				<input type="text" class="mask" title="999" name="dadosGerais.jornadaDiaMin" value="<fmt:formatNumber type="NUMBER" value="${dadosGerais.jornadaDiaMin}" />" size="18" />
			</div>			
		</div>
	</div>	
	<div>
		<div class="linha obrigatorio" style="height: auto;">
			<fmt:message key="dadosGerais.encargos" /> <span>*</span><br/>

			<div class="lista">
			
			<div class="cabecalho">
				<div style="float: left; width: 250px;"><fmt:message key="encargo.empresa" /></div>
				<div style="float: left; width: 180px;"><fmt:message key="encargo.sindicato" /></div>
				<div style="float: left; width: 90px;"><fmt:message key="encargo.aliquota1" /></div>
				<div style="float: left; width: 90px;"><fmt:message key="encargo.aliquota2" /></div>
			</div>
			
			<c:forEach items="${encargos}" var="encargo" varStatus="idx">
				<input type="hidden" name="encargos[${idx.count}].id" value="${encargo.id}" />
				<input type="hidden" name="encargos[${idx.count}].versao" value="${encargo.versao}" />
				<input type="hidden" name="encargos[${idx.count}].empresa.id" value="${encargo.empresa.id}" />
				<input type="hidden" name="encargos[${idx.count}].empresa.nomeCurto" value="${encargo.empresa.nomeCurto}" />
				<input type="hidden" name="encargos[${idx.count}].empresa.versao" value="${encargo.empresa.versao}" />
				<input type="hidden" name="encargos[${idx.count}].sindicato.id" value="${encargo.sindicato.id}" />
				<input type="hidden" name="encargos[${idx.count}].sindicato.versao" value="${encargo.sindicato.versao}" />
				<div class="linha" style="height: auto; padding: 0;">
					<div style="float: left; width: 250px;">${encargo.empresa.nomeCurto}</div>
					<div style="float: left; width: 180px;">${encargo.sindicato.nome}</div>
					<div style="float: left; width: 90px;"><input class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="encargos[${idx.count}].aliquota1" value="<fmt:formatNumber type="NUMBER" value="${encargo.aliquota1}" minFractionDigits="2" />" size="10" /></div>
					<div style="float: left; width: 90px;"><input class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="encargos[${idx.count}].aliquota2" value="<fmt:formatNumber type="NUMBER" value="${encargo.aliquota2}" minFractionDigits="2" />" size="10" /></div>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>	
	
	<div>
		<div class="linha obrigatorio" style="height: auto;">
			<fmt:message key="dadosGerais.horarios" /> <span>*</span><br/>

			<div class="lista">
			
			<div class="cabecalho">
				<div style="float: left; width: 120px;"><fmt:message key="horarioTrabalho.diaSemana" /></div>
				<div style="float: left; width: 100px;"><fmt:message key="horarioTrabalho.horaEntrada" /></div>
				<div style="float: left; width: 100px;"><fmt:message key="horarioTrabalho.horaSaida" /></div>
				<div style="float: left; width: 100px;"><fmt:message key="horarioTrabalho.tempoAlmoco" /></div>
			</div>
			
			<c:forEach items="${horarios}" var="horario">
				<input type="hidden" name="horarios[${idx.count}].id" value="${horario.id}" />
				<input type="hidden" name="horarios[${idx.count}].versao" value="${horario.versao}" />
				<input type="hidden" name="horarios[${idx.count}].diaSemana" value="${horario.diaSemana}" />
				<div class="linha" style="height: auto; padding: 0;">
					<div style="float: left; width: 120px;"><fmt:message key="diaSemana.${horario.diaSemana}"/></div>
					<div style="float: left; width: 100px;"><input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" name="horarios[${idx.count}].horaEntrada" value="<fmt:formatDate type="TIME" timeStyle="SHORT" value="${horario.horaEntrada}" />" size="10" /></div>
					<div style="float: left; width: 100px;"><input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" name="horarios[${idx.count}].horaSaida" value="<fmt:formatDate type="TIME" timeStyle="SHORT" value="${horario.horaSaida}" />" size="10" /></div>
					<div style="float: left; width: 100px;"><input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" name="horarios[${idx.count}].tempoAlmoco" value="<fmt:formatDate type="TIME" timeStyle="SHORT" value="${horario.tempoAlmoco}" />" size="10" /></div>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>	
	
	
	<div>
		<div class="linha">
			<c:if test="${not empty dadosGerais.id}">
				<c:if test="${sessaoUsuario.administrador}">
					<a class="lnkRemover imgBtn" href="<c:url value="/dadosGerais/${dadosGerais.id}/remover"/>"><img src="<c:url value="/images/remover.png"/>" title="<fmt:message key="remover"/>" /></a>
				</c:if>
				<button type="button" class="imgBtn novo redir" value="<c:url value="/dadosGerais/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<c:if test="${empty dadosGerais.id or sessaoUsuario.administrador}">
				<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
			</c:if>
		</div>
	</div>
	
</form>

</div>

<div id="confirmarModal" title="<fmt:message key="dadosGerais.remover" />">
  <p><fmt:message key="dadosGerais.remover.msg" /></p>
</div>