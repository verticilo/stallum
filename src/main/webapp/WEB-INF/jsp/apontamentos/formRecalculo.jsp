<div class="leftBar">

<div class="menuBox">

	<ul>
	<c:forEach items="${sessaoUsuario.menu}" var="item">
		<li><a href="<c:url value="${item.uri}"/>"><fmt:message key="itensMenu.${item}" /></a></li>
	</c:forEach>
	</ul>

</div> <!-- menuBox -->

</div> <!-- leftBar -->

<div class="content">

<form id="formRecalculo" class="formulario" action="<c:url value="/custos/recalcular"/>" method="post">

    <h2><fmt:message key="formRecalculo.titulo" /></h2>
	
	<div>
		<div class="coluna">
			<fmt:message key="despesa.obra" /><br/>
			<select name="obra.id">
				<option value=""></option>
				<c:forEach items="${obras}" var="o">
					<option value="${o.id}">${o.nomeCurto}</option>
				</c:forEach>
			</select>
		</div>
		<div class="coluna">
			<fmt:message key="despesa.centroCusto" /><br/>
			<select name="centroCusto.id">
				<option value=""></option>
				<c:forEach items="${centrosCusto}" var="cc">
					<option value="${cc.id}" >${cc.nomeCurto}</option>
				</c:forEach>
			</select>			
		</div>
	</div>
	<div>
		<div class="coluna">
			<fmt:message key="dataDe" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="dataIni" size="40" />
		</div>
		<div class="coluna">
			<fmt:message key="dataAte" /><br/>
			<input type="text" class="mask" title="<fmt:message key="formato.maskData" />" name="dataFim" size="40" />
		</div>
	</div>	
	
	<div>
		<div class="linha">
			<button type="submit" class="imgBtn confirmar" title="<fmt:message key="confirmar" />" onclick="document.getElementById('msgProc').style.display = 'inline'" ></button>
		</div>
	</div>
		
</form>

<span id="msgProc" style="display: none;"><fmt:message key="formRecalculo.msgProc" /></span>
			
</div>