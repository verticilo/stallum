package stallum.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ClienteDao;
import stallum.dao.ContaCorrenteDao;
import stallum.dao.ContaGerencialDao;
import stallum.dao.ContaReceberDao;
import stallum.dto.FiltroCliente;
import stallum.dto.FiltroContaCorrente;
import stallum.dto.FiltroContaGerencial;
import stallum.dto.FiltroContaReceber;
import stallum.dto.OrdenacaoContaGerencial;
import stallum.dto.OrdenacaoContaReceber;
import stallum.dto.Paginacao;
import stallum.enums.ClasseContaGerencial;
import stallum.enums.FormaPagto;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusConta;
import stallum.enums.TipoDocumento;
import stallum.enums.TipoMovCaixa;
import stallum.infra.SessaoUsuario;
import stallum.modelo.ContaCorrente;
import stallum.modelo.ContaGerencial;
import stallum.modelo.ContaReceber;
import stallum.modelo.MovimentoCaixa;
import stallum.modelo.Recebimento;
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
public class ContasReceberController extends AbstractController {

	private Logger log = Logger.getLogger(ContasReceberController.class);
	
	private final ContaReceberDao dao;
	private final ClienteDao clienteDao;
	private final ContaCorrenteDao contaCorrenteDao;
	private final ContaGerencialDao contaGerencialDao;

	public ContasReceberController(ContaReceberDao dao, ContaGerencialDao contaGerencialDao,
			ClienteDao forncedorDao, ContaCorrenteDao contaCorrenteDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.clienteDao = forncedorDao;
		this.contaCorrenteDao = contaCorrenteDao;
		this.contaGerencialDao = contaGerencialDao;
		
	}
	
	@Get @Path({"/contasReceber/nova", "/contasReceber/{contaReceber.id}/editar"})
	public ContaReceber formContaReceber(ContaReceber contaReceber) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (contaReceber == null)
				contaReceber = new ContaReceber();
			else if (contaReceber.getId() != null && contaReceber.getVersao() == null)
				contaReceber = consultarPorId(contaReceber.getId());
			
			FiltroContaGerencial filtro = FiltroContaGerencial.newInstance(-1);
			filtro.setTotalizadoras(false);
			filtro.setOrdenacao(OrdenacaoContaGerencial.NOME);
			filtro.setClasse(ClasseContaGerencial.RECEITA);
			Collection<ContaGerencial> contas = contaGerencialDao.filtrar(filtro);
			filtro.setClasse(ClasseContaGerencial.CUSTO);
			Collection<ContaGerencial> origens = contaGerencialDao.filtrar(filtro);
			
			result.include("contas", contas);
			result.include("clientes", clienteDao.filtrar(FiltroCliente.newInstance(-1)));
			result.include("tipos", TipoDocumento.values());
			result.include("origens", origens);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaReceber.falha.carregar"));
			result.redirectTo("/contasReceber/listar");
		}
		
		return contaReceber;
	}
	
	@Get @Post @Path("/contasReceber/listar")
	public void listarContasReceber(FiltroContaReceber filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroContaReceber.newInstance(getRegistrosPagina());

			for (OrdenacaoContaReceber ordem: OrdenacaoContaReceber.values())
				filtro.addOrdencacao(ordem);

			filtro.getPaginacao().atualizar();
		
			filtro.setSemVencto(true);
			Collection<ContaReceber> lista = dao.filtrar(filtro);
			filtro.setSemVencto(false);
			lista.addAll(dao.filtrar(filtro));
			
			result.include("filtro", filtro);
			result.include("contasReceber", lista);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarContasReceber.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/contasReceber/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		ContaReceber contaReceber = consultarPorId(id);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {		
			
			dao.excluir(contaReceber);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("contaReceber.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaReceber.falha.remover", id));
		}
		
		result.redirectTo("/contasReceber/listar");
	}

	@Get @Post @Path({"/contasReceber/recebimento", "/contasReceber/{idConta}/recebimento"})
	public Recebimento formRecebimento(Recebimento recebimento, List<ContaReceber> contasReceber, Long idConta) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		List<Long> ids = new ArrayList<Long>();
		
		if (idConta != null)
			ids.add(idConta);
		
		if (contasReceber != null) {
			for (ContaReceber cp: contasReceber) {
				if (cp.isMarcada() && !cp.getId().equals(idConta))
					ids.add(cp.getId());
			}
		}
		
		if (ids.size() == 0) {
			sessao.addErro(getMessage("formContaReceber.borderoVazio"));
			result.redirectTo("/contasReceber/listar");
			return null;
		}
		
		FiltroContaReceber filtro = FiltroContaReceber.newInstance(-1);
		filtro.setIds(ids);
		filtro.setStatus(StatusConta.ABERTA);
		contasReceber = new ArrayList<ContaReceber>(dao.filtrar(filtro));
		
		if (recebimento == null)
			recebimento = new Recebimento();
		recebimento.setContasRecebidas(contasReceber);
		
		result.include("contasCorrente", contaCorrenteDao.filtrar(FiltroContaCorrente.newInstance(-1)));
		result.include("formasPagto", FormaPagto.values());
		
		recebimento.setData(new Date());
		
		return recebimento;
		
	}
	
	@Put @Post @Path("/contasReceber")
	public void salvar(final ContaReceber contaReceber) {
		
		validator.checking(new Validations() {{
			that(preenchido(contaReceber.getConta().getId()), "contaReceber.conta", "obrigatoria");
			that(preenchido(contaReceber.getData()), "contaReceber.data", "obrigatoria");
			that(preenchido(contaReceber.getCliente().getId()), "contaReceber.cliente", "obrigatorio");
			that(preenchido(contaReceber.getTipo()), "contaReceber.tipo", "obrigatorio");
			that(preenchido(contaReceber.getOrigem().getId()), "contaReceber.origem", "obrigatorio");
			that(preenchido(contaReceber.getValor()), "contaReceber.valor", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formContaReceber(contaReceber);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			boolean novo = contaReceber.getId() == null;
			
			if (contaReceber.getStatus() == null && (contaReceber.getDataRecto() == null || contaReceber.getDataRecto().compareTo(contaReceber.getData()) > 0))
				contaReceber.setStatus(StatusConta.ABERTA);
			
			contaReceber.setCliente(clienteDao.buscarPorId(contaReceber.getCliente().getId()));
			contaReceber.setConta(contaGerencialDao.buscarPorId(contaReceber.getConta().getId()));
			contaReceber.setOrigem(contaGerencialDao.buscarPorId(contaReceber.getOrigem().getId()));

			if (novo) {
				dao.incluir(contaReceber);								
				result.redirectTo("/contasReceber/nova");
			} else {
				dao.alterar(contaReceber);
				result.redirectTo("/contasReceber/listar");
			}		
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formContaReceber.sucesso"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaReceber.falha.salvar"));
			result.redirectTo(this).formContaReceber(contaReceber);
		}
		
	}

	@Put @Post @Path("/contasReceber/receber")
	public void receber(final Recebimento recebimento, List<ContaReceber> contasReceber) {
		
		validator.checking(new Validations() {{
			that(preenchido(recebimento.getData()), "recebimento.data", "obrigatoria");
			that(preenchido(recebimento.getContaCorrente().getId()), "recebimento.contaCorrente", "obrigatoria");
			that(preenchido(recebimento.getFormaPagto()), "recebimento.formaPagto", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formRecebimento(recebimento, contasReceber, null);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			ContaCorrente cc = contaCorrenteDao.buscarPorId(recebimento.getContaCorrente().getId());
			cc.credita(recebimento.getTotalRecto());
			dao.alterar(cc);
			recebimento.setContaCorrente(cc);

			if (recebimento.getId() == null)
				dao.incluir(recebimento);								
			else
				dao.alterar(recebimento);
			
			for (ContaReceber contaReceber: contasReceber) {
				ContaReceber cp = dao.buscarPorId(contaReceber.getId());
				cp.setRecebimento(recebimento);
				cp.setDataRecto(recebimento.getData());
				cp.setAcrescimos(contaReceber.getAcrescimos());
				cp.setValorRecebido(contaReceber.getValorRecebido());
				cp.setStatus(StatusConta.LIQUIDADA);
				dao.alterar(cp);
			}
			
			MovimentoCaixa movCaixa = new MovimentoCaixa();
			movCaixa.setData(recebimento.getData());
			movCaixa.setConta(cc);
			movCaixa.setDocumento(recebimento.getNumDocto());
			movCaixa.setTipoMovimento(TipoMovCaixa.PAGTO);
			movCaixa.setValor(recebimento.getTotalRecto());
			movCaixa.setIdRegistro(recebimento.getId());
			dao.incluir(movCaixa);
			recebimento.setMovCaixa(movCaixa);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formRecebimento.sucesso"));
			result.redirectTo("/contasReceber/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			for (ContaReceber cp: contasReceber)
				cp.setMarcada(true); // Necessario para o formRecebimento
			sessao.addErro(getMessage("formContaReceber.falha.salvar"));
			result.redirectTo(this).formRecebimento(recebimento, contasReceber, null);
		}
		
	}
		
	private ContaReceber consultarPorId(Long id) {
		
		ContaReceber contaReceber = null;
		try {
			
			contaReceber = dao.consultarPorId(id);

			if (contaReceber == null)
				validator.add(new ValidationMessage(getMessage("contaReceber.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaReceber.falha.recuperar", id));
			result.forwardTo("/contasReceber/listar");
		}
		
		return contaReceber;
	}
	
}
