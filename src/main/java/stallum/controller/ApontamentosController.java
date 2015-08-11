package stallum.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ApontamentoDao;
import stallum.dao.CentroCustoDao;
import stallum.dao.DadosGeraisDao;
import stallum.dao.FuncionarioDao;
import stallum.dao.ObraDao;
import stallum.dao.SindicatoDao;
import stallum.dto.DiaApontamento;
import stallum.dto.FiltroApontamento;
import stallum.dto.FiltroCentroCusto;
import stallum.dto.FiltroObra;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoApontamento;
import stallum.dto.Paginacao;
import stallum.dto.PontoNovo;
import stallum.enums.MotivoFalta;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusFuncionario;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Apontamento;
import stallum.modelo.CentroCusto;
import stallum.modelo.DadosGerais;
import stallum.modelo.Funcionario;
import stallum.modelo.Obra;
import stallum.modelo.Ponto;
import stallum.relatorio.FuncionarioRelatorio;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;

@Resource
public class ApontamentosController extends AbstractController {

	private Logger log = Logger.getLogger(ApontamentosController.class);
	
	private final ApontamentoDao dao;
	private final ObraDao obraDao;
	private final CentroCustoDao centroCustoDao;
	private final FuncionarioDao funcionarioDao;
	private final DadosGeraisDao dadosGeraisDao;
	private final SindicatoDao sindicatoDao;

	public ApontamentosController(ApontamentoDao dao, ObraDao obraDao, FuncionarioDao funcionarioDao, DadosGeraisDao dadosGeraisDao, 
			CentroCustoDao centroCustoDao, SindicatoDao sindicatoDao,
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.obraDao = obraDao;
		this.funcionarioDao = funcionarioDao;
		this.dadosGeraisDao = dadosGeraisDao;
		this.centroCustoDao = centroCustoDao;
		this.sindicatoDao = sindicatoDao;
	}

	@Get @Path({"/apontamentos/novo/", "/apontamentos/novo/{apontamento.strData}"})
	public Apontamento formNovoApontamento(Apontamento apontamento) {

		validaSessao(PerfilUsuario.OPERADOR);

		result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
		
		if (apontamento == null)
			apontamento = new Apontamento();
		
		return apontamento;
	}
	
	@Post @Path("/apontamentos/detalhar")
	public Apontamento formApontamento(final Apontamento apontamento) {
		
		if (!validator.hasErrors()) {
			validator.checking(new Validations() {{
				that(preenchido(apontamento.getData()), "apontamento.data", "obrigatoria");
			}});
			validator.onErrorForwardTo(this).formNovoApontamento(apontamento);
		}
		
		Collection<Obra> obras = obraDao.listarAtivas(apontamento.getData());
		Collection<CentroCusto> centrosCusto = centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1));
		Collection<Ponto> pontos = listarPontos(apontamento);
		
		if (Boolean.TRUE.equals(apontamento.getFeriado())) {
		
			// Se foi marcado feriado, grava apontamentos para todas as obras ativas do sindicato selecionado

			Transaction tx = dao.getSession().beginTransaction();		
			
			try {
				
				for (Obra obra: obras) {
					if (apontamento.getSindicato().getId() != null && !obra.getSindicato().equals(apontamento.getSindicato()))
						continue;
					Apontamento apont = new Apontamento();
					apont.setData(apontamento.getData());
					apont.setObra(obra);
					apont.setFeriado(true);
					apont.setSindicato(apontamento.getSindicato());
					dao.incluir(apont);						
				}
				
				tx.commit();
				
				sessao.addMensagem(getMessage("formApontamento.sucesso"));
				result.redirectTo("/apontamentos/listar/" + apontamento.getStrData());
				
				return new Apontamento();
				
			} catch (HibernateException e) {
				tx.rollback();
				log.error(e.getMessage());
				sessao.addErro(getMessage("formApontamento.falha.salvar"));
				result.redirectTo("/apontamentos/listar");
			}
			
		} else if (pontos.size() == 0) {
			
			// Se nao houverem pontos a serem lancados, retorna.
			sessao.addMensagem(getMessage("formApontamento.pontosJaLancados", Util.dataToString(apontamento.getData())));
			result.redirectTo("/apontamentos/listar/" + apontamento.getStrData());
			return null;
			
		} else {
				
			try {
				
				result.include("obras", obras);
				result.include("centrosCusto", centrosCusto);
				result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
				result.include("motivosFalta", MotivoFalta.values());
				result.include("pontos", pontos);
				
			} catch (HibernateException e) {
				log.error(e.getMessage());
				sessao.addErro(getMessage("formApontamento.falha.carregar"));
				result.redirectTo("/apontamentos/listar");
			}
			
		}
		
		return apontamento;
		
	}
			
	@Get @Path("/apontamentos/{idApont}/editar")
	public Apontamento formApontamento(Long idApont) {
		
		validaSessao(PerfilUsuario.OPERADOR);
			
		Apontamento apontamento = null;
		
		try {

			apontamento = consultarPorId(idApont);
			
			List<Ponto> pontos = (List<Ponto>)listarPontos(apontamento);
			Collection<Obra> obras = obraDao.listarAtivas(apontamento.getData());
			Collection<CentroCusto> centrosCusto = centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1));
			
			result.include("obras", obras);					
			result.include("centrosCusto", centrosCusto);					
			result.include("pontos", pontos);
			result.include("motivosFalta", MotivoFalta.values());
			result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formApontamento.falha.carregar"));
			result.redirectTo("/apontamentos/listar");
		}
		
		return apontamento;
		
	}
	
	@Get @Path("/apontamentos/nao-lancados/{strData}")
	public Apontamento formApontamento(String strData) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		Apontamento apontamento = new Apontamento();			
		
		try {
						
			FiltroApontamento filtro = FiltroApontamento.newInstance(-1);
			filtro.setData(strData);			
			apontamento.setData(filtro.getData());
			
			Collection<Ponto> pontos = listarPontos(apontamento);
			Collection<Obra> obras = obraDao.listarAtivas(filtro.getData());
			Collection<CentroCusto> centrosCusto = centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1));
			
			result.include("obras", obras);					
			result.include("centrosCusto", centrosCusto);					
			result.include("pontos", pontos);
			result.include("motivosFalta", MotivoFalta.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formApontamento.falha.carregar"));
			result.redirectTo("/apontamentos/listar");
		}
		
		return apontamento;
		
	}
	
	@Get @Path("/apontamentos/copiar/{strData}/{idObra}")
	public Apontamento formApontamento(Long idObra, String strData) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		Apontamento apontamento = null;			
		
		try {
			
			Date data = Util.stringToData(strData);
			apontamento = dao.getUltimoApontamentoObra(idObra, data);
			if (apontamento == null) {
				Obra obra = consultarObraPorId(idObra);
				sessao.addMensagem(getMessage("formApontamento.copiar.naoExiste", obra.getNomeCurto()));
				result.forwardTo(this).formApontamento(strData);
				return null;
			}
			apontamento.setData(data);
			
			Collection<Ponto> pontos = listarPontos(apontamento, true);
			Collection<Obra> obras = obraDao.listarAtivas(data);
			Collection<CentroCusto> centrosCusto = centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1));
			
			apontamento.setId(null);
			apontamento.setAlteradoEm(null);
			apontamento.setData(data);
			
			result.include("obras", obras);					
			result.include("centrosCusto", centrosCusto);					
			result.include("pontos", pontos);
			result.include("motivosFalta", MotivoFalta.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formApontamento.falha.carregar"));
			result.redirectTo("/apontamentos/listar");
		}
		
		return apontamento;
		
	}
	
	@Post @Path("/apontamentos")
	public void salvar(Apontamento apontamento, List<Ponto> pontos) {
		
		validaSessao(PerfilUsuario.OPERADOR);
				
		final Apontamento apontTela = apontamento;
		
		validator.checking(new Validations() {{
			that(preenchido(apontTela.getData()), "apontamento.data", "obrigatoria");
			that(apontTela.getObra().getId() != null || apontTela.getCentroCusto().getId() != null, "apontamento.obraOuCCusto", "obrigatorio");
		}});
		validator.onErrorForwardTo(this).formApontamento(apontamento);
		
		Transaction tx = dao.getSession().beginTransaction();		
		
		try {			
			
			Apontamento apontBanco = dao.conultarPorData(apontamento);
			if (apontBanco != null)
				apontamento = apontBanco;

			boolean novo = apontamento.getId() == null;
			
			if (apontamento.getObra() != null && apontamento.getObra().getId() != null) {
				
				apontamento.setObra(consultarObraPorId(apontamento.getObra().getId()));
				apontamento.setCentroCusto(null);
				
			} else {
				
				apontamento.setCentroCusto(consultarCentroCusto(apontamento));
				apontamento.setObra(null);
				
			}
			
			DadosGerais dadosGerais = dadosGeraisDao.consultarPorData(apontamento.getData());			
			if (dadosGerais == null)
				validator.add(new ValidationMessage(getMessage("dadosGerais.falha.naoCadastrado"), "semCategoria"));
			validator.onErrorRedirectTo(this).formApontamento(apontamento);
				
			if (novo)
				dao.incluir(apontamento);					
			else
				dao.alterar(apontamento);
		
			calculaCustoIndiretoHH(apontamento, dadosGerais.getJornadaDiaMin() * 30);
			
			boolean manterApont = false;
			boolean limparApont = false;
			Collection<Ponto> aRemover = new ArrayList<Ponto>(); 
			for (Ponto ponto: pontos) {				
				if (Boolean.TRUE.equals(ponto.getPresente()) && !Boolean.TRUE.equals(apontamento.getFeriado())) {
					limparApont = !ponto.getApontamento().equals(apontamento);
					manterApont = true;
					ponto.setApontamento(apontamento);
					ponto.setFuncionario(funcionarioDao.consultarComCusto(ponto.getFuncionario().getId(), apontamento.getData()));
					
					calculaPonto(ponto, dadosGerais);
					
					aRemover.add(ponto);
					if (ponto.getId() == null)
						dao.incluir(ponto);
					else
						dao.alterar(ponto);
				} else if (ponto.getId() != null && ponto.getApontamento().equals(apontamento)) {
					// Se o ponto ja havia sido marcado no apontamento corrente e foi desmarcado, exclui
					dao.excluir(ponto);
				}
			}				
			pontos.removeAll(aRemover);
			
			if (!manterApont && apontamento.getId() != null)
				dao.excluir(apontamento);
			
			tx.commit();
			
			if (limparApont) {
				tx = dao.getSession().beginTransaction();	
				dao.removeApontamentoSemPonto();
				tx.commit();
			}
			
			sessao.addMensagem(getMessage("formApontamento.sucesso"));
			
			Apontamento novoApont = new Apontamento();
			novoApont.setData(apontamento.getData());
			novoApont.setPontos(apontamento.getPontos());
			result.forwardTo(this).formApontamento(novoApont);
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formApontamento.falha.salvar"));
			result.forwardTo(this).formApontamento(apontamento);
		}
		
	}
		
	private Collection<DiaApontamento> mapaApontamento(Calendar cal) {
		
		if (cal == null)
			cal = Calendar.getInstance();
		
		int ultimoDia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);		
		Date dataAtual = new Date();
		
		Collection<DiaApontamento> dias = new ArrayList<DiaApontamento>();
		List<DiaApontamento> diasApont = new ArrayList<DiaApontamento>(dao.buscarCalendarioObra(cal));
		for (int i = 1; i <= ultimoDia; i++) {
			DiaApontamento dia = new DiaApontamento();
			cal.set(Calendar.DAY_OF_MONTH, i);
			dia.setData(Util.diaSemHora(cal.getTime()));
			if (diasApont.contains(dia)) {
				DiaApontamento diaApont = diasApont.get(diasApont.indexOf(dia));
				dia.setNumFuncionarios(diaApont.getNumFuncionarios());
				dia.setNumPontos(diaApont.getNumPontos());
			} else if (dia.getData().compareTo(dataAtual) < 0) { 
				// Se nao for data futura, forca cor vermelha.
				dia.setNumFuncionarios(-1); 
			}
			
			if (i == 1) {
				for (int j = 0; j < dia.getDiaSemana().ordinal(); j++)
					dias.add(new DiaApontamento());
			}
			
			dias.add(dia);
			
			if (i == ultimoDia) {
				for (int j = dia.getDiaSemana().ordinal() + 1; j <= 6 ;j++)
					dias.add(new DiaApontamento());				
			}
		}
				
		return dias;				
	}

	@Get @Post @Path({"/apontamentos/listar", "/apontamentos/listar/{strData}"})
	public void listarApontamentos(FiltroApontamento filtro, String strData) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroApontamento.newInstance(getRegistrosPagina());

			for (OrdenacaoApontamento ordem: OrdenacaoApontamento.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
			
			Collection<Apontamento> listaApont = null;
			if (preenchido(strData)) {
				filtro.setData(strData);
				if (strData.split("-").length > 2) {
					filtro.setStrData(strData);
					listaApont = dao.filtrar(filtro);
				}
			}
			
			List<DiaApontamento> mapa = (List<DiaApontamento>)mapaApontamento(filtro.getCal());
			
			if (listaApont != null) {
				DiaApontamento diaSel = new DiaApontamento();
				diaSel.setData(filtro.getData());
				diaSel = mapa.get(mapa.indexOf(diaSel));
				if (diaSel != null & diaSel.getNumPontos() > 0 && diaSel.getNumPontos() < diaSel.getNumFuncionarios())
					filtro.setDia(diaSel);
			}
			
			result.include("listaApontamentos", listaApont);				
			result.include("filtro", filtro);
			result.include("calendario", mapa);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarApontamentos.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	@Get @Post @Path("/apontamentos/obra/{idObra}/listar")
	public void listarApontamentosObra(Long idObra) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {
			
			Obra obra = consultarObraPorId(idObra);
			
			result.include("obra", obra);
			result.include("listaPontos", dao.listarPontosObra(idObra));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarApontamentos.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	@Get @Path("/custos/recalcular")
	public void formRecalculo() {
		
		validaSessao(PerfilUsuario.ADMIN);
		
		try {
			
			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			result.include("centrosCusto", centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formRecalculo.falha.carregar"));
			result.redirectTo("/");
		}
		
	}
		
	@Post @Path("/custos/recalcular")
	public void recalcular(final Obra obra, final CentroCusto centroCusto, Date dataIni, Date dataFim) {

		validator.checking(new Validations() {{
			that(obra.getId() != null || centroCusto.getId() != null, "apontamento.obraOuCCusto", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formRecalculo();
		
		Transaction tx = null;		
		
		try {
			
			FiltroApontamento filtroApont = FiltroApontamento.newInstance(-1);
			filtroApont.setData(dataIni);
			filtroApont.setDataFim(dataFim);
			if (obra.getId() != null)
				filtroApont.setObra(consultarObraPorId(obra.getId()));
			else if (centroCusto.getId() != null)
				filtroApont.setCentroCusto(centroCusto);
			Collection<Apontamento> aponts = dao.filtrarCompleto(filtroApont);
			
			for (Apontamento apont: aponts) {

				tx = dao.getSession().beginTransaction();		
				
				apont.setObra(filtroApont.getObra());
				
				if (apont.getPontos() == null)
					continue;
				
				DadosGerais dadosGerais = dadosGeraisDao.consultarPorData(apont.getData());			
				if (dadosGerais == null)
					validator.add(new ValidationMessage(getMessage("dadosGerais.falha.naoCadastrado"), "semCategoria"));
				validator.onErrorRedirectTo(this).formRecalculo();
							
				calculaCustoIndiretoHH(apont, dadosGerais.getJornadaDiaMin() * 30);
				
				for (Ponto ponto: apont.getPontos()) {
					
					ponto.setApontamento(apont);
					ponto.setFuncionario(funcionarioDao.consultarComCusto(ponto.getFuncionario().getId(), apont.getData()));
					
					calculaPonto(ponto, dadosGerais);
					
					dao.alterar(ponto);
					
				}				
				
				tx.commit();
			}
						
			sessao.addMensagem(getMessage("formRecalculo.sucesso"));
			result.redirectTo("/");
			
		} catch (HibernateException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formRecalculo.falha.recalcular"));
			result.redirectTo("/");
		}
		
	}	
	
	private Apontamento consultarPorId(Long id) {
		
		Apontamento apontamento = null;
		try {
			
			apontamento = dao.consultarPorId(id);
			if (apontamento == null)
				validator.add(new ValidationMessage(getMessage("apontamento.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("apontamento.falha.recuperar", id));
			result.forwardTo("/apontamentos/listar");
		}
		
		return apontamento;
	}
	
	private Obra consultarObraPorId(Long id) {
		
		Obra obra = null;
		try {
			
			obra = obraDao.consultarPorId(id);

			if (obra == null)
				validator.add(new ValidationMessage(getMessage("obra.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("obra.falha.recuperar", id));
			result.forwardTo("/obras/listar");
		}
		
		return obra;	
	}
	
	private CentroCusto consultarCentroCusto(Apontamento apontamento) {
		
		CentroCusto centroCusto = null;
		try {
			
			centroCusto = centroCustoDao.consultarPorId(apontamento.getCentroCusto().getId(), apontamento.getData());
			
			if (centroCusto == null)
				validator.add(new ValidationMessage(getMessage("centroCusto.validacao.naoExiste", apontamento.getCentroCusto().getId()), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("obra.falha.recuperar", apontamento.getCentroCusto().getId()));
			result.forwardTo("/obras/listar");
		}
		
		return centroCusto;
	}
	
	
	private Collection<Ponto> listarPontos(Apontamento apont) {
		return listarPontos(apont, false);
	}
	private Collection<Ponto> listarPontos(Apontamento apont, boolean copia) {
		Collection<Ponto> pontos = new ArrayList<Ponto>();
		if (Boolean.TRUE.equals(apont.getFeriado()))
			return pontos;
		
		// Se for copia de lancamento, limpa as informacoes anteriores
		for (Ponto ponto: apont.getPontos()) {
			if (copia) {
				ponto.setAlteradoEm(null);
				ponto.setHoraEntrada(null);
				ponto.setHoraSaida(null);
				ponto.setHoraExtra(null);
				ponto.setPercHoraExtra(null);
				ponto.setMotivoFalta(null);
				ponto.setMotivoAbono(null);
			}
			ponto.setApontamento(apont);
		}
		
		Collection<Funcionario> funcionariosAtivos = funcionarioDao.listarAtivos(Util.diaCompleto(apont.getData()));
		List<Ponto> pontosLancados = (List<Ponto>)dao.listarPontos(apont.getData());
		List<Ponto> pontosApontamento = (List<Ponto>)apont.getPontos();
		List<Ponto> pontosCopiados = new ArrayList<Ponto>();
		List<Ponto> pontosNaoLancados = new ArrayList<Ponto>();
		for (Funcionario funcionario: funcionariosAtivos) {
			Ponto ponto = new Ponto();
			ponto.setFuncionario(funcionario);
			ponto.setPresente(apont.getPontos().contains(ponto));
			if (ponto.getPresente()) {
				Ponto pontoCopiado = pontosApontamento.get(pontosApontamento.indexOf(ponto));
				if (copia)
					pontoCopiado.setId(null);
				pontoCopiado.setFuncionario(funcionario);
				pontoCopiado.setPresente(true);
				marcarOcorrencia(pontoCopiado);
				pontosCopiados.add(pontoCopiado);
			} else if (!pontosLancados.contains(ponto)) {
				marcarOcorrencia(ponto);
				pontosNaoLancados.add(ponto);
			}
		}
		pontos.addAll(pontosCopiados);
		pontos.addAll(pontosNaoLancados);
		
		// Ao final, lista os ja lancados em outras obras
//		for (Ponto pontoLancado: pontosLancados) {
//			if (!pontoLancado.getApontamento().equals(apont)) {
//				// Prevalece o lancamento do dia sobre o copiado
//				if (pontos.contains(pontoLancado))
//					pontos.remove(pontoLancado);
//				pontoLancado.setPresente(false);
//				pontos.add(pontoLancado);
//			}
//		}
		
		return pontos;
	}
	
	private void marcarOcorrencia(Ponto ponto) {
		if (StatusFuncionario.FERIAS.equals(ponto.getFuncionario().getStatus())) {
			ponto.setMotivoFalta(MotivoFalta.FERIAS);
		} else if (StatusFuncionario.LICENCA.equals(ponto.getFuncionario().getStatus())) {
			ponto.setMotivoFalta(MotivoFalta.ABONO);
			ponto.setMotivoAbono(ponto.getFuncionario().getStatus().name());
		} 
	}
	
	private void calculaPonto(Ponto ponto, DadosGerais dadosGerais) {
		
		if (MotivoFalta.FALTA.equals(ponto.getMotivoFalta()))
			return;
		
		Date dataApont = ponto.getApontamento().getData();
		
		Funcionario funcionario = ponto.getFuncionario();
		
		ponto.setHorasNormais(dadosGerais.getCargaHoraria(dataApont));
		
		BigDecimal custoHoraExtra = BigDecimal.ZERO;
		BigDecimal horaExtra = BigDecimal.ZERO;
		if (ponto.getHoraEntrada() != null)
			horaExtra = horaExtra.add(Util.diferencaHoras(ponto.getHoraEntrada(), dadosGerais.getHoraEntrada(dataApont)));
		if (ponto.getHoraSaida() != null)
			horaExtra = horaExtra.add(Util.diferencaHoras(dadosGerais.getHoraSaida(dataApont), ponto.getHoraSaida()));
		if (!horaExtra.equals(BigDecimal.ZERO))
			ponto.setHoraExtra(horaExtra);
		if (ponto.getHoraExtra() != null) {	
			if (ponto.getHoraExtra().compareTo(BigDecimal.ZERO) < 0) {
				ponto.setHorasNormais(ponto.getHorasNormais().add(ponto.getHoraExtra()));
			} else {
				custoHoraExtra = funcionario.getSalarioHora().multiply(ponto.getHoraExtra()); 
				BigDecimal adicHoraExtra = BigDecimal.ZERO; 
				if (ponto.getPercHoraExtra() != null)
					adicHoraExtra = custoHoraExtra.multiply(ponto.getPercHoraExtra().divide(BigDecimal.valueOf(100)));
				custoHoraExtra = custoHoraExtra.add(adicHoraExtra).setScale(2, RoundingMode.HALF_EVEN);
			}
		}

		ponto.setCustoHoraNormal(funcionario.getSalarioHora().multiply(ponto.getHorasNormais()));		
		ponto.setCustoHoraExtra(custoHoraExtra);

		BigDecimal horasMes = BigDecimal.valueOf(dadosGerais.getJornadaDiaMin() * 30);

		Apontamento apontamento = ponto.getApontamento();
		
		if (apontamento.getCentroCusto() != null) {
			BigDecimal custosInd = ponto.getApontamento().getCentroCusto().getDespesasMes();
			custosInd = custosInd.multiply(ponto.getHorasNormais()).divide(horasMes, 2, RoundingMode.HALF_EVEN);
			ponto.setCustosIndiretos(custosInd);
		} else {
			// Calcula custo indireto todal proporcional as horas trabalhadas
			ponto.setCustosIndiretos(apontamento.getCustoIndiretoHH().multiply(ponto.getHorasNormais()));
		}
		
		BigDecimal aliqEnc1 = dadosGerais.getEncargo(funcionario, DadosGerais.ENCARGO1);
		BigDecimal valEnc1 = funcionario.getTotalSalario().multiply(aliqEnc1.divide(BigDecimal.valueOf(100)));
		valEnc1 = valEnc1.multiply(ponto.getHorasNormais());
		
		BigDecimal aliqEnc2 = dadosGerais.getEncargo(funcionario, DadosGerais.ENCARGO2);
		BigDecimal valEnc2 = funcionario.getTotalBonus().multiply(aliqEnc2.divide(BigDecimal.valueOf(100)));
		valEnc2 = valEnc2.multiply(ponto.getHorasNormais());

		ponto.setEncargos(valEnc1.add(valEnc2));		
		
		BigDecimal benHora = BigDecimal.ZERO;
		if (funcionario.getSalarioMes().compareTo(dadosGerais.getTetoSalFamilia()) <= 0) {
			BigDecimal qtDep = BigDecimal.valueOf(funcionario.getQuantDependentes());
			benHora = dadosGerais.getAdicDependente().multiply(qtDep).divide(horasMes, 2, RoundingMode.HALF_EVEN);
		}
		ponto.setSalarioFamilia(benHora.multiply(ponto.getHorasNormais()));
		
		BigDecimal valeRef = BigDecimal.ZERO;
		if (funcionario.isBonificado())
			 valeRef = dadosGerais.getValeRefeicao().divide(horasMes, 2, RoundingMode.HALF_EVEN);
		ponto.setValeBonificacao(valeRef.multiply(ponto.getCustoHoraNormal()));
					
	}
	
	private void calculaCustoIndiretoHH(Apontamento apontamento, Integer horasMes) {
		
		if (apontamento.getObra() == null) {
			apontamento.setCustoIndiretoHH(BigDecimal.ZERO);
			return;			
		}
			
		
		// Calculo do rateio das despesas para a obra
		BigDecimal ativos = BigDecimal.valueOf(funcionarioDao.listarAtivos(Util.diaCompleto(apontamento.getData())).size());
		BigDecimal mediaFunc = apontamento.getObra().getMediaFuncionarios();
		
		if (ativos == null || ativos.compareTo(BigDecimal.ZERO) == 0 ||
			mediaFunc == null || mediaFunc.compareTo(BigDecimal.ZERO) == 0) {
			
			apontamento.setCustoIndiretoHH(BigDecimal.ZERO);
			return;			
		}
		
		BigDecimal rateio = mediaFunc.divide(ativos, 2, RoundingMode.HALF_EVEN);
		
		// Total de custos indiretos no mes anterior
		BigDecimal custoIndiretoHH = centroCustoDao.custosIndiretosMesAnterior(apontamento.getObra().getSindicato(), apontamento.getData());
		
		// Custo indireto rateado para a obra
		custoIndiretoHH = custoIndiretoHH.multiply(rateio);
		
		// Custo indireto por funcionario da obra
		custoIndiretoHH = custoIndiretoHH.divide(mediaFunc, 2, RoundingMode.HALF_EVEN);
		
		// Custo indireto/funcionario/hora
		custoIndiretoHH = custoIndiretoHH.divide(BigDecimal.valueOf(horasMes), 2, RoundingMode.HALF_EVEN);
		
		apontamento.setCustoIndiretoHH(custoIndiretoHH);
	}

	@Get @Path("/apontamentos/listar-novo")
	public FuncionarioRelatorio formApontamentoNovo(FuncionarioRelatorio relatorio) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {

			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorioFuncionario.falha.carregar"));
			result.redirectTo("/");
		}
		
		return relatorio;		
		
	}
	
	@Post @Path("/apontamentos/lancamento")
	public void lancamentoPonto(final FuncionarioRelatorio relatorio) {
	
		try {
		
			if (relatorio.getPaginacao() == null || relatorio.getPaginacao().getAcao() == 0) {
				
				validator.checking(new Validations() {{
					that(preenchido(relatorio.getObra().getId()), "relatorioFuncionario.obra", "obrigatoria");
					that(preenchido(relatorio.getDataDe()), "relatorioFuncionario.dataDe", "obrigatoria");
				}});
				validator.onErrorRedirectTo(this).formApontamentoNovo(relatorio);
		
				Calendar cal = Calendar.getInstance();
				cal.setTime(relatorio.getDataDe());
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				relatorio.setDataDe(cal.getTime());
				cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				relatorio.setDataAte(cal.getTime());

				Obra obra = obraDao.buscarPorId(relatorio.getObra().getId());
				relatorio.setObra(obra);
				relatorio.setSindicato(obra.getSindicato());
				relatorio.setPaginacao(Paginacao.newInstance(33));
			}
			
			relatorio.getPaginacao().atualizar();
			List<Funcionario> funcionarios = new ArrayList<Funcionario>(funcionarioDao.listarAtivos(relatorio.getDataDe()));
			
			result.include("relatorio", relatorio);
			result.include("funcionarios", funcionarios);
			result.include("motivosFalta", MotivoFalta.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("lamcamentoPonto.falha.listar"));
			result.redirectTo("/apontamentos/listar-novo");
		}	
		
	}
	
	@Post 
	@Path("/apontamentos/lancar/{idObra}/{semana}") 
	public void lancar(PontoNovo[] lista, Long idObra, String semana) {
		
		System.out.println(semana);
		
	}

}
