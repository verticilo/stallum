<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li <c:if test="${item eq 'APONTAMENTOS'}">class="ativo"</c:if> ><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formApontamento" class="formulario" action="<c:url value="/apontamentos"/>" method="post">

	<input type="hidden" id="apontId" name="apontamento.id" value="${apontamento.id}" />
 	<input type="hidden" name="apontamento.versao" value="${apontamento.versao}" />
 	<c:if test="${not empty apontamento.id}">
 		<input type="hidden" name="apontamento.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${apontamento.data}" />" />
 		<input type="hidden" name="apontamento.obra.id" value="${apontamento.obra.id}" />
 		<input type="hidden" name="apontamento.centroCusto.id" value="${apontamento.centroCusto.id}" />
 		<input type="hidden" name="apontamento.sindicato.id" value="${apontamento.sindicato.id}" />
 	</c:if>
 	
	<h2><fmt:message key="formApontamento.titulo" /></h2>
	
	<div>
		<div class="linha">
			<div class="subColuna obrigatorio">
				<fmt:message key="apontamento.data" /> <span>*</span><br/>
				<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="apontamento.data" value="<fmt:formatDate type="date" dateStyle="MEDIUM" value="${apontamento.data}" />" size="15" <c:if test="${not empty apontamento.id}">disabled="disabled"</c:if> />
			</div>
			<div class="subColuna obrigatorio">
				<fmt:message key="apontamento.obra" /> <span>*</span><br/>
				<select id="obra" name="apontamento.obra.id" <c:if test="${not empty apontamento.id}">disabled="disabled"</c:if> >
					<option value=""></option>
					<c:forEach items="${obras}" var="obra">
						<option value="${obra.id}" <c:if test="${obra == apontamento.obra}" >selected="selected"</c:if> >${obra.nomeCurto}</option>
					</c:forEach>
				</select>
			</div>
			<div id="copiar" class="subColuna" style="margin-top: -3px; display: none;">
				<br/>
				<button id="linkCopiar" type="button" class="imgBtn copiar redir" value="<c:url value="/apontamentos/copiar/${apontamento.strData}/0"/>" title="<fmt:message key="formApontamento.copiar" />" ></button>
			</div>
			<div class="subColuna obrigatorio">
				<fmt:message key="apontamento.centroCusto" /> <span>*</span><br/>
				<select id="cCusto" name="apontamento.centroCusto.id" <c:if test="${not empty apontamento.id}">disabled="disabled"</c:if> >
					<option value=""></option>
					<c:forEach items="${centrosCusto}" var="centroCusto">
						<option value="${centroCusto.id}" <c:if test="${centroCusto == apontamento.centroCusto}" >selected="selected"</c:if> >${centroCusto.nome}</option>
					</c:forEach>
				</select>
			</div>
			<div class="subColuna">
				<fmt:message key="apontamento.feriado" /><br/>
				<input id="feriado" type="checkbox" name="apontamento.feriado" <c:if test="${apontamento.feriado}">checked="checked"</c:if> />
			</div>
			<div class="subColuna" id="sindicato" <c:if test="${not apontamento.feriado}">style="display: none;"</c:if> >
				<fmt:message key="apontamento.sindicato" /><br/>
				<select name="apontamento.sindicato.id" <c:if test="${not empty apontamento.id}">disabled="disabled"</c:if> >
					<option value=""><fmt:message key="todos" /></option>
					<c:forEach items="${sindicatos}" var="sindicato" >
						<option value="${sindicato.id}" <c:if test="${sindicato == apontamento.obra.sindicato}" > selected="selected"</c:if> >${sindicato.nome} </option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
		
	<c:if test="${not empty pontos}">	
	
	<div>
		<div class="linha obrigatorio" style="height: auto;">

			<div class="lista">
			
			<div class="cabecalho">
				<div style="float: left; width: 250px;"><fmt:message key="ponto.funcionario" /></div>
				<div style="float: left; width: 70px;"><fmt:message key="ponto.horaEntrada" /></div>
				<div style="float: left; width: 70px;"><fmt:message key="ponto.horaSaida" /></div>
<%-- 				<div style="float: left; width: 70px;"><fmt:message key="ponto.horaExtra" /></div> --%>
<%-- 				<div style="float: left; width: 70px;"><fmt:message key="ponto.percHoraExtra" /></div> --%>
				<div style="float: left; width: 80px;"><fmt:message key="ponto.motivoFalta" /></div>
				<div style="float: left; width: 145px;"><fmt:message key="ponto.motivoAbono" /></div>
			</div>
			
			<c:forEach items="${pontos}" var="ponto" varStatus="idx">
				<input type="hidden" name="pontos[${idx.count}].id" value="${ponto.id}" />
				<input type="hidden" name="pontos[${idx.count}].versao" value="${ponto.versao}" />
				<input type="hidden" name="pontos[${idx.count}].apontamento.id" value="${ponto.apontamento.id}" />
				<input type="hidden" name="pontos[${idx.count}].funcionario.id" value="${ponto.funcionario.id}" />
				<input type="hidden" name="pontos[${idx.count}].funcionario.nome" value="${ponto.funcionario.nome}" />
				<input type="hidden" name="pontos[${idx.count}].funcionario.versao" value="${ponto.funcionario.versao}" />
				<c:if test="${not empty ponto.apontamento and ponto.apontamento != apontamento}">
					<div class="linha" style="height: auto; padding: 0; color: red">
				</c:if>
				<c:if test="${empty ponto.apontamento or ponto.apontamento == apontamento}">
					<div class="linha" style="height: auto; padding: 0;">
				</c:if>
					<div style="float: left; width: 250px;"><input id="pto-${idx.count}" type="checkbox" class="pto" name="pontos[${idx.count}].presente" <c:if test="${ponto.presente}">checked="checked"</c:if> /> ${ponto.funcionario.nomeCurto}</div>
					<div style="float: left; width: 70px;"><input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" name="pontos[${idx.count}].horaEntrada" value="<fmt:formatDate type="TIME" timeStyle="SHORT" value="${ponto.horaEntrada}" />" size="6" /></div>
					<div style="float: left; width: 70px;"><input class="mask" title="<fmt:message key="formato.maskHora" />" type="text" name="pontos[${idx.count}].horaSaida" value="<fmt:formatDate type="TIME" timeStyle="SHORT" value="${ponto.horaSaida}" />" size="6" /></div>
<%-- 					<div style="float: left; width: 70px;"><input class="sHora" title="[-]99:99" type="text" name="pontos[${idx.count}].strHoraExtra" value="${ponto.strHoraExtra}" size="6" /></div> --%>
<%-- 					<div style="float: left; width: 70px;"><input class="mask" title="<fmt:message key="formato.percentual" />" type="text" name="pontos[${idx.count}].percHoraExtra" value="<fmt:formatNumber type="NUMBER" value="${ponto.percHoraExtra}" minFractionDigits="2" maxFractionDigits="2" />" size="6" /></div> --%>
					<div style="float: left; width: 80px;">
						<select name="pontos[${idx.count}].motivoFalta">
							<option value=""></option>
							<c:forEach items="${motivosFalta}" var="motivo">
								<option value="${motivo}" <c:if test="${motivo == ponto.motivoFalta}" >selected="selected"</c:if> ><fmt:message key="motivoFalta.${motivo}" /></option>
							</c:forEach>
						</select>
					</div>
					<c:if test="${not empty ponto.apontamento and ponto.apontamento != apontamento}">
						<input id="hid-${idx.count}" type="hidden" name="pontos[${idx.count}].motivoAbono" value="${ponto.motivoAbono}" />
						<div style="float: left; width: 145px;"><input id="mot-${idx.count}" type="text" name="pontos[${idx.count}].motivoAbono" value="${ponto.obs}" size="25" disabled="disabled" /></div>
					</c:if>
					<c:if test="${empty ponto.apontamento or ponto.apontamento == apontamento}">
						<div style="float: left; width: 145px;"><input type="text" name="pontos[${idx.count}].motivoAbono" value="${ponto.motivoAbono}" size="25" /></div>
					</c:if>
				</div>		
			</c:forEach>
			
			</div>
			
		</div>
	</div>
	
	</c:if>
	
	<div>
		<div class="linha">
			<fmt:message key="apontamento.obs" /><br/>
			<textarea name="apontamento.obs" rows="4" cols="45">${apontamento.obs}</textarea>
		</div>
	</div>

	<div>
		<div class="linha">
			<button type="button" class="imgBtn novo redir" value="<c:url value="/apontamentos/novo/"/>" title="<fmt:message key="novo" />" ></button>
			<button type="button" class="imgBtn cancelar btnVoltar" title="<fmt:message key="cancelar" />" ></button>			
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" ></button>
		</div>
	</div>
	
</form>

</div>

<script>
$(function() {
	
	$("#feriado").change(function() {
		feriado();
	});

	function feriado() {
	
		if ($("#feriado").is(":checked")) {
			$("#sindicato").show();			
			$("#obra").attr("disabled", "disabled");			
			$("#cCusto").attr("disabled", "disabled");		
		} else {
			$("#sindicato").hide();	
			if ($("#apontId").val() == "") {
				$("#obra").removeAttr("disabled");			
				$("#cCusto").removeAttr("disabled");
			}
		}
	
	}
	
	$("#obra").change(function() {
		var lnk = $("#linkCopiar").val();
		var idObra = $(this).val();
		$("#linkCopiar").val(lnk.substring(0, lnk.lastIndexOf('/')) + "/" + idObra);
		if (idObra != '') {
			$("#copiar").show();
		} else {
			$("#copiar").hide();			
		}
	});
	
	$(".pto").change(function() {
		var idx = $(this).attr("id").split('-')[1];
		var item = "#mot-" + idx;
		if ($(this).is(":checked")) {
			var itemVal = $(item).val();
			$(item).val($("#hid-" + idx).val());
			$("#hid-" + idx).val(itemVal);
			$(item).removeAttr("disabled");
		} else {
			var hidVal = $("#hid-" + idx).val();
			$("#hid-" + idx).val($(item).val());
			$(item).val(hidVal);
			$(item).attr("disabled", "disabled");		
		}
	});
			
});
</script>
