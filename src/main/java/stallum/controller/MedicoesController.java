package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ContaGerencialDao;
import stallum.dao.EmpresaDao;
import stallum.dao.MedicaoDao;
import stallum.dao.ObraDao;
import stallum.dto.FiltroEmpresa;
import stallum.dto.FiltroMedicao;
import stallum.dto.FiltroObra;
import stallum.dto.OrdenacaoMedicao;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusConta;
import stallum.enums.TipoDocumento;
import stallum.infra.SessaoUsuario;
import stallum.modelo.ContaReceber;
import stallum.modelo.Medicao;
import stallum.modelo.Obra;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;

@Resource
public class MedicoesController extends AbstractController {

	private Logger log = Logger.getLogger(MedicoesController.class);
	
	private final MedicaoDao dao;
	private final ObraDao obraDao;
	private final EmpresaDao empresaDao;
	private final ContaGerencialDao ctaGerencialDao;

	public MedicoesController(MedicaoDao dao, ObraDao obraDao, EmpresaDao empresaDao, ContaGerencialDao ctaGerencialDao,  
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.obraDao = obraDao;
		this.empresaDao = empresaDao;
		this.ctaGerencialDao = ctaGerencialDao;
	}
	
	@Get @Path({"/medicoes/nova", "/medicoes/{medicao.id}/editar"})
	public Medicao formMedicao(Medicao medicao, String msgModal) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (medicao == null)
				medicao = new Medicao();
			else if (medicao.getId() != null && medicao.getVersao() == null)
				medicao = consultarPorId(medicao.getId());
				
			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			result.include("empresas", empresaDao.filtrar(FiltroEmpresa.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formMedicao.falha.carregar"));
			result.redirectTo("/medicoes/listar");
		}
		
		return medicao;
	}
	
	@Get @Post @Path({"/medicoes/listar", "/medicoes/obra/{idObra}/listar"})
	public void listarMedicoes(FiltroMedicao filtro, Long idObra) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroMedicao.newInstance(getRegistrosPagina());

			for (OrdenacaoMedicao ordem: OrdenacaoMedicao.values())
				filtro.addOrdencacao(ordem);

			if (filtro.getObra() != null)
				filtro.setObra(buscarObra(filtro.getObra().getId()));
			
			filtro.getPaginacao().atualizar();
		
			if (idObra != null) {
				Obra obra = new Obra();
				obra.setId(idObra);
				filtro.setObra(obra);
			}
			
			result.include("filtro", filtro);
			result.include("listaMedicoes", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarMedicoes.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	@Put @Post @Path("/medicoes")
	public void salvar(final Medicao medicao, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(medicao.getObra()) && preenchido(medicao.getObra().getId()), "medicao.obra", "obrigatoria");
			that(preenchido(medicao.getNotaFiscal()), "medicao.notaFiscal", "obrigatoria");
			that(preenchido(medicao.getData()), "medicao.data", "obrigatoria");
			if (!Boolean.TRUE.equals(medicao.getCancelada()))
				that(preenchido(medicao.getValor()), "medicao.valor", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formMedicao(medicao, null);
		
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
						
			medicao.setObra(obraDao.consultarPorId(medicao.getObra().getId()));
			medicao.setEmpresa(empresaDao.buscarPorId(medicao.getEmpresa().getId()));
			
			if (!medicao.isRepetir() && dao.existe(medicao)) {
				// Se for recibo (RC), solicita confirmacao
				if (medicao.getNotaFiscal().trim().toLowerCase().startsWith("rc")) {
					medicao.setRepetir(true);
					validator.add(new ValidationMessage(getMessage("formMedicao.msgJaExiste", medicao.getNotaFiscal(), medicao.getData()), "medicao.notaFiscal"));
				} else {
					medicao.setRepetir(false);
					validator.add(new ValidationMessage(getMessage("jaCadastrada"), "medicao.notaFiscal"));
				}
				validator.onErrorRedirectTo(this).formMedicao(medicao, null);
			}
			
			boolean novo = false;
			
			
			if (medicao.getId() == null) {
				novo = true;
				
				ContaReceber cr = new ContaReceber();
				cr.setData(medicao.getData());
				cr.setCliente(medicao.getObra().getCliente());
				cr.setConta(ctaGerencialDao.consultarPorNumero("1.02"));
				cr.setNumero("MD " + medicao.getNotaFiscal());
				cr.setValor(medicao.getValor());
				cr.setTipo(TipoDocumento.BOLETO);
				cr.setObs(medicao.getObra().getNomeCurto());
				cr.setOrigem(medicao.getObra().getContaGerencial());
				cr.setStatus(StatusConta.ABERTA);
				ctaGerencialDao.incluir(cr);
				
				dao.incluir(medicao);								
			} else {
				dao.alterar(medicao);
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formMedicao.sucesso"));
			if (novo)
				result.redirectTo("/medicoes/nova");
			else
				result.redirectTo("/medicoes/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formMedicao.falha.salvar"));
			result.redirectTo(this).formMedicao(medicao, null);
		}
		
	}
		
	@Get @Path("/medicoes/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Medicao medicao = consultarPorId(id);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {		
			
			dao.excluir(medicao);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("medicao.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("medicao.falha.remover", id));
		}
		
		result.redirectTo("/medicoes/listar");
	}
	
	private Medicao consultarPorId(Long id) {
		
		Medicao medicao = null;
		try {
			
			medicao = dao.consultarPorId(id);

			if (medicao == null)
				validator.add(new ValidationMessage(getMessage("medicao.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("medicao.falha.recuperar", id));
			result.forwardTo("/medicoes/listar");
		}
		
		return medicao;
	}
	
	private Obra buscarObra(Long id) {

		if (id == null)
			return null;
		
		Obra obra = null;
		
		try {
			
			obra = obraDao.buscarPorId(id);

			if (obra == null)
				validator.add(new ValidationMessage(getMessage("obra.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("medicao.falha.recuperar", id));
			result.forwardTo("/medicoes/listar");
		}
		
		return obra;
	}
	
}
