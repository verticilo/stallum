package stallum.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ContaCorrenteDao;
import stallum.dao.ContaGerencialDao;
import stallum.dao.ContaPagarDao;
import stallum.dao.FornecedorDao;
import stallum.dto.FiltroContaCorrente;
import stallum.dto.FiltroContaGerencial;
import stallum.dto.FiltroContaPagar;
import stallum.dto.FiltroFornecedor;
import stallum.dto.OrdenacaoContaGerencial;
import stallum.dto.OrdenacaoContaPagar;
import stallum.dto.Paginacao;
import stallum.enums.ClasseContaGerencial;
import stallum.enums.FormaPagto;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusConta;
import stallum.enums.TipoDocumento;
import stallum.enums.TipoMovCaixa;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.ContaCorrente;
import stallum.modelo.ContaGerencial;
import stallum.modelo.ContaPagar;
import stallum.modelo.MovimentoCaixa;
import stallum.modelo.Pagamento;
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
public class ContasPagarController extends AbstractController {

	private Logger log = Logger.getLogger(ContasPagarController.class);
	
	private final ContaPagarDao dao;
	private final FornecedorDao fornecedorDao;
	private final ContaCorrenteDao contaCorrenteDao;
	private final ContaGerencialDao contaGerencialDao;

	public ContasPagarController(ContaPagarDao dao, ContaGerencialDao contaGerencialDao,
			FornecedorDao forncedorDao, ContaCorrenteDao contaCorrenteDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.fornecedorDao = forncedorDao;
		this.contaCorrenteDao = contaCorrenteDao;
		this.contaGerencialDao = contaGerencialDao;
		
	}
	
	@Get @Path({"/contasPagar/nova", "/contasPagar/{contaPagar.id}/editar"})
	public ContaPagar formContaPagar(ContaPagar contaPagar) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (contaPagar == null)
				contaPagar = new ContaPagar();
			else if (contaPagar.getId() != null && contaPagar.getVersao() == null)
				contaPagar = consultarPorId(contaPagar.getId());
			
			FiltroContaGerencial filtro = FiltroContaGerencial.newInstance(-1);
			filtro.setTotalizadoras(false);
			filtro.setOrdenacao(OrdenacaoContaGerencial.NOME);
			filtro.setClasse(ClasseContaGerencial.DESPESA);
			Collection<ContaGerencial> contas = contaGerencialDao.filtrar(filtro);
			filtro.setClasse(ClasseContaGerencial.CUSTO);
			Collection<ContaGerencial> origens = contaGerencialDao.filtrar(filtro);
			
			result.include("contas", contas);
			result.include("fornecedores", fornecedorDao.filtrar(FiltroFornecedor.newInstance(-1)));
			result.include("tipos", TipoDocumento.values());
			result.include("origens", origens);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaPagar.falha.carregar"));
			result.redirectTo("/contasPagar/listar");
		}
		
		return contaPagar;
	}
	
	@Get @Post @Path("/contasPagar/listar")
	public void listarContasPagar(FiltroContaPagar filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroContaPagar.newInstance(getRegistrosPagina());

			for (OrdenacaoContaPagar ordem: OrdenacaoContaPagar.values())
				filtro.addOrdencacao(ordem);

			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("contasPagar", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarContasPagar.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/contasPagar/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		ContaPagar contaPagar = consultarPorId(id);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {		
			
			dao.excluir(contaPagar);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("contaPagar.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaPagar.falha.remover", id));
		}
		
		result.redirectTo("/contasPagar/listar");
	}

	@Get @Post @Path({"/contasPagar/pagamento", "/contasPagar/{idConta}/pagamento"})
	public Pagamento formPagamento(Pagamento pagamento, List<ContaPagar> contasPagar, Long idConta) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		List<Long> ids = new ArrayList<Long>();
		
		if (idConta != null)
			ids.add(idConta);
		
		if (contasPagar != null) {
			for (ContaPagar cp: contasPagar) {
				if (cp.isMarcada() && !cp.getId().equals(idConta))
					ids.add(cp.getId());
			}
		}
		
		if (ids.size() == 0) {
			sessao.addErro(getMessage("formContaPagar.borderoVazio"));
			result.redirectTo("/contasPagar/listar");
			return null;
		}
		
		FiltroContaPagar filtro = FiltroContaPagar.newInstance(-1);
		filtro.setIds(ids);
		filtro.setStatus(StatusConta.ABERTA);
		contasPagar = new ArrayList<ContaPagar>(dao.filtrar(filtro));
		
		if (pagamento == null)
			pagamento = new Pagamento();
		pagamento.setContasPagas(contasPagar);
		
		result.include("contasCorrente", contaCorrenteDao.filtrar(FiltroContaCorrente.newInstance(-1)));
		result.include("formasPagto", FormaPagto.values());
		
		pagamento.setData(new Date());
		
		return pagamento;
		
	}
	
	@Put @Post @Path("/contasPagar")
	public void salvar(final ContaPagar contaPagar) {
		
		validator.checking(new Validations() {{
			that(preenchido(contaPagar.getConta().getId()), "contaPagar.conta", "obrigatoria");
			that(preenchido(contaPagar.getData()), "contaPagar.data", "obrigatoria");
			that(preenchido(contaPagar.getFornecedor().getId()), "contaPagar.fornecedor", "obrigatorio");
			that(preenchido(contaPagar.getTipo()), "contaPagar.tipo", "obrigatorio");
			that(preenchido(contaPagar.getOrigem().getId()), "contaPagar.origem", "obrigatorio");
			that(preenchido(contaPagar.getValor()), "contaPagar.valor", "obrigatorio");
			if (TipoDocumento.CARNE.equals(contaPagar.getTipo())) 
				that(preenchido(contaPagar.getParcs()) && preenchido(contaPagar.getInterv()), "contaPagar.parcs", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formContaPagar(contaPagar);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			boolean novo = contaPagar.getId() == null;
			
			if (contaPagar.getStatus() == null && (contaPagar.getDataPagto() == null || contaPagar.getDataPagto().compareTo(contaPagar.getData()) > 0))
				contaPagar.setStatus(StatusConta.ABERTA);
			
			contaPagar.setFornecedor(fornecedorDao.buscarPorId(contaPagar.getFornecedor().getId()));
			contaPagar.setConta(contaGerencialDao.buscarPorId(contaPagar.getConta().getId()));
			contaPagar.setOrigem(contaGerencialDao.buscarPorId(contaPagar.getOrigem().getId()));
			
			if (novo) {
				if (TipoDocumento.CARNE.equals(contaPagar.getTipo()))
					inlcuirCarne(contaPagar);
				else
					dao.incluir(contaPagar);
			} else {
				dao.alterar(contaPagar);
			}		
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formContaPagar.sucesso"));
			if (novo)
				result.redirectTo("/contasPagar/nova");
			else
				result.redirectTo("/contasPagar/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaPagar.falha.salvar"));
			result.redirectTo(this).formContaPagar(contaPagar);
		}
		
	}

	private void inlcuirCarne(ContaPagar contaPagar) throws HibernateException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(Util.diaSemHora(contaPagar.getVencto()));
		
		String numero = contaPagar.getNumero();
		
		for (int i = 1; i <= contaPagar.getParcs(); i++) {
			ContaPagar cp = new ContaPagar();		
			cp.setConta(contaPagar.getConta());
			cp.setData(contaPagar.getData());
			cp.setFornecedor(contaPagar.getFornecedor());
			cp.setTipo(contaPagar.getTipo());
			cp.setNumero(numero + " #" + i + "/" + contaPagar.getParcs());
			cp.setOrigem(contaPagar.getOrigem());
			cp.setValor(contaPagar.getValor());
			if (i > 1)
				cal.add(Calendar.DAY_OF_MONTH, contaPagar.getInterv());
			cp.setVencto(cal.getTime());
			cp.setStatus(contaPagar.getStatus());
			cp.setObs(contaPagar.getObs());
			dao.incluir(cp);
		}
		
	}

	@Put @Post @Path("/contasPagar/pagar")
	public void pagar(final Pagamento pagamento, List<ContaPagar> contasPagar) {
		
		validator.checking(new Validations() {{
			that(preenchido(pagamento.getData()), "pagamento.data", "obrigatoria");
			that(preenchido(pagamento.getContaCorrente().getId()), "pagamento.contaCorrente", "obrigatoria");
			that(preenchido(pagamento.getFormaPagto()), "pagamento.formaPagto", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formPagamento(pagamento, contasPagar, null);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			ContaCorrente cc = contaCorrenteDao.buscarPorId(pagamento.getContaCorrente().getId());
			cc.debita(pagamento.getTotalPagto());
			dao.alterar(cc);
			pagamento.setContaCorrente(cc);

			if (pagamento.getId() == null)
				dao.incluir(pagamento);								
			else
				dao.alterar(pagamento);
			
			for (ContaPagar contaPagar: contasPagar) {
				ContaPagar cp = dao.buscarPorId(contaPagar.getId());
				cp.setPagamento(pagamento);
				cp.setDataPagto(pagamento.getData());
				cp.setAcrescimos(contaPagar.getAcrescimos());
				cp.setValorPago(contaPagar.getValorPago());
				cp.setStatus(StatusConta.LIQUIDADA);
				dao.alterar(cp);
			}
			
			MovimentoCaixa movCaixa = new MovimentoCaixa();
			movCaixa.setData(pagamento.getData());
			movCaixa.setConta(cc);
			movCaixa.setDocumento(pagamento.getNumDocto());
			movCaixa.setTipoMovimento(TipoMovCaixa.PAGTO);
			movCaixa.setValor(pagamento.getTotalPagto());
			movCaixa.setIdRegistro(pagamento.getId());
			dao.incluir(movCaixa);
			pagamento.setMovCaixa(movCaixa);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formPagamento.sucesso"));
			result.redirectTo("/contasPagar/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			for (ContaPagar cp: contasPagar)
				cp.setMarcada(true); // Necessario para o formPagamento
			sessao.addErro(getMessage("formContaPagar.falha.salvar"));
			result.redirectTo(this).formPagamento(pagamento, contasPagar, null);
		}
		
	}
		
	private ContaPagar consultarPorId(Long id) {
		
		ContaPagar contaPagar = null;
		try {
			
			contaPagar = dao.consultarPorId(id);

			if (contaPagar == null)
				validator.add(new ValidationMessage(getMessage("contaPagar.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaPagar.falha.recuperar", id));
			result.forwardTo("/contasPagar/listar");
		}
		
		return contaPagar;
	}
	
}
