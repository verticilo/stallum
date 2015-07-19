<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'DISSIDIOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">


<c:if test="${empty dissidios}">
	<form id="formDissidio" class="formulario" action="<c:url value="/dissidios/detalhar"/>" method="post">
</c:if>
<c:if test="${not empty dissidios}">
	<form id="formDissidio" class="formulario" action="<c:url value="/dissidios"/>" method="post">
</c:if>

	 <c:if test="${not empty dissidios}">
	 	<input type="hidden" name="dissidio.id" value="${dissidio.id}" />
	 	<input type="hidden" name="dissidio.funcao.sindicato.id" value="${dissidio.funcao.sindicato.id}" />
	 </c:if> 
	 <c:if test="${empty dissidio.funcao.sindicato}">
	 	<input type="hidden" name="dissidio.beneficio" value="<fmt:formatNumber type="NUMBER" value="${dissidio.beneficio}" minFractionDigits="2" />" />
	</c:if>

	<h2><fmt:message key="formDissidio.titulo" /></h2>
	
	<div>
		<div class="linha obrigatorio">
			<div class="subColuna">
				<fmt:message key="funcao.sindicato" /> <span>*</span><br/>
				<select name="dissidio.funcao.sindicato.id" <c:if test="${not empty dissidios}">disabled="disabled"</c:if> >
					<option value=""></option>
					<c:forEach items="${sindicatos}" var="sindicato">
						<option value="${sindicato.id}" <c:if test="${sindicato == dissidio.funcao.sindicato}" > selected="selected"</c:if> >${sindicato.nome}</option>
					</c:forEach>
				</select>
			</div>
			<div class="subColuna">
				<fmt:message key="dissidio.data" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="dissidio.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${dissidio.data}" />" size="15" />
			</div>
			<div class="subColuna">
				<fmt:message key="dissidio.percReajuste" /> <span>*</span><br/>
				<input class="mask" type="text" title="<fmt:message key="formato.percentual" />" name="dissidio.percReajuste" value="<fmt:formatNumber type="NUMBER" value="${dissidio.percReajuste}" minFractionDigits="2" />" size="15" />
			</div>
			<c:if test="${not empty dissidio.funcao.sindicato}">
				<div class="subColuna">
					<fmt:message key="sindicato.valorBeneficio" /> <span>*</span><br/>
					<input class="mask" type="text" title="<fmt:message key="formato.valor" />" name="dissidio.beneficio" value="<fmt:formatNumber type="NUMBER" value="${dissidio.beneficio}" minFractionDigits="2" />" size="15" />
				</div>
			</c:if>
		</div>
	</div>	
	
	<c:if test="${not empty encargos}">
	
	<div>
		<div class="linha obrigatorio" style="height: auto;">
			<fmt:message key="dadosGerais.encargos" /> <span>*</span><br/>

			<div class="lista">
			
			<div class="cabecalho">
				<div style="float: left; width: 250px;"><fmt:message key="encargo.empresa" /></div>
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
					<div style="float: left; width: 90px;"><input class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="encargos[${idx.count}].aliquota1" value="<fmt:formatNumber type="NUMBER" value="${encargo.aliquota1}" minFractionDigits="2" />" size="10" /></div>
					<div style="float: left; width: 90px;"><input class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="encargos[${idx.count}].aliquota2" value="<fmt:formatNumber type="NUMBER" value="${encargo.aliquota2}" minFractionDigits="2" />" size="10" /></div>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>
		
	</c:if>	
	
	<c:if test="${not empty dissidios}">
	
	<div>
		<div class="linha obrigatorio" style="height: auto;">
			<fmt:message key="dissidio.funcao" /> <span>*</span><br/>

			<div class="lista">
			
			<div class="cabecalho">
				<div style="float: left; width: 220px;"><fmt:message key="funcao.nome" /></div>
				<div style="float: left; width: 100px;"><fmt:message key="dissidio.percReajuste" /></div>
				<div style="float: left; width: 100px;"><fmt:message key="funcao.salario" /></div>
			</div>
			
			<c:forEach items="${dissidios}" var="dissidio">
				<div class="linha" style="height: auto; padding: 0;">
					<div style="float: left; width: 220px;">${dissidio.funcao.nome}</div>
					<div style="float: left; width: 100px;"><input class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="reajustes[]" value="<fmt:formatNumber type="NUMBER" value="${dissidio.percReajuste}" minFractionDigits="2" maxFractionDigits="2" />" size="10" /></div>
					<div style="float: left; width: 100px;"><input class="mask" title="<fmt:message key="formato.valor" />" type="text" name="salarios[]" value="<fmt:formatNumber type="NUMBER" value="${dissidio.funcao.salario}" minFractionDigits="2" maxFractionDigits="2" />" size="10" /></div>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>
	
	</c:if>

	<div>
		<div class="linha">
			<c:if test="${not empty dissidio.id}">
				<button type="button" class="imgBtn novo redir" value="<c:url value="/dissidios/novo"/>" title="<fmt:message key="novo" />" ></button>
			</c:if>			
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>