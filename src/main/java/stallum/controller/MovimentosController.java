package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.FuncaoDao;
import stallum.dao.FuncionarioDao;
import stallum.dao.MovimentoDao;
import stallum.dto.FiltroFuncao;
import stallum.dto.FiltroMovimento;
import stallum.dto.OrdenacaoMovimento;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusFuncionario;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Funcao;
import stallum.modelo.Funcionario;
import stallum.modelo.Movimento;
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
public class MovimentosController extends AbstractController {

	private Logger log = Logger.getLogger(MovimentosController.class);
	
	private final MovimentoDao dao;
	private final FuncionarioDao funcionarioDao;
	private final FuncaoDao funcaoDao;

	public MovimentosController(MovimentoDao dao, FuncionarioDao funcionarioDao, FuncaoDao funcaoDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.funcionarioDao = funcionarioDao;
		this.funcaoDao = funcaoDao;
	}
	
	
	@Get @Path({"/funcionarios/{movimento.funcionario.id}/movimentos/novo", "/movimentos/{movimento.id}/editar"})
	public Movimento formMovimento(Movimento movimento) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (movimento.getId() == null && movimento.getData() == null) {
				Funcionario funcionario = buscarFuncionario(movimento.getFuncionario().getId());
				movimento = new Movimento();
				movimento.setFuncionario(funcionario);
			} else if (movimento.getId() != null && movimento.getVersao() == null) {
				movimento = consultarPorId(movimento.getId());
			}
			
			result.include("funcoes", funcaoDao.filtrar(FiltroFuncao.newInstance(-1)));
			result.include("listaStatus", StatusFuncionario.values(sessao.getUsuario().getPerfil()));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formMovimento.falha.carregar"));
			result.redirectTo("/funcionarios/listar");
		}
		
		return movimento;
		
	}
		
	@Get @Post @Path("/funcionarios/{filtro.funcionario.id}/movimentos/listar")
	public void listarMovimentos(FiltroMovimento filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			Funcionario funcionario = buscarFuncionario(filtro.getFuncionario().getId());
			
			if (filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroMovimento.newInstance(getRegistrosPagina());

			for (OrdenacaoMovimento ordem: OrdenacaoMovimento.values())
				filtro.addOrdencacao(ordem);
			
			filtro.setFuncionario(funcionario);
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaMovimentos", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarMovimentos.falha.listar"));
			result.redirectTo("/funcionarios/listar");
		}	
	}
		
	@Put @Post @Path("/movimentos")
	public void salvar(final Movimento movimento, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(movimento.getFuncionario()), "movimento.funcionario", "obrigatorio");
			that(preenchido(movimento.getData()), "movimento.data", "obrigatoria");
			that(preenchido(movimento.getMotivo()), "movimento.motivo", "obrigatorio");
			that(preenchido(movimento.getNovaFuncao().getId()) || preenchido(movimento.getNovoStatus()) || preenchido(movimento.getBonus()), "movimento.funcaoOuStatus", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formMovimento(movimento);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Funcionario funcionario = buscarFuncionario(movimento.getFuncionario().getId());
			movimento.setFuncionario(funcionario);
			
			if (movimento.getNovaFuncao() != null && movimento.getNovaFuncao().getId() != null) {
				Funcao funcao = funcaoDao.consultarPorId(movimento.getNovaFuncao().getId());
				movimento.setNovaFuncao(funcao);
				funcionario.setFuncao(funcao);
				funcionario.setSalarioBase(funcao.getSalario());
				funcionario.setSindicato(funcao.getSindicato());
			} else {
				movimento.setNovaFuncao(null);
			}
			
			if (movimento.getNovoStatus() != null) {
				
				if (!StatusFuncionario.ATIVO.equals(movimento.getNovoStatus())) {

					if (movimento.getDataFim() != null) {
						dao.removePonto(funcionario.getId(), movimento.getData(), movimento.getDataFim());
					} else if (dao.existePonto(movimento.getData(), funcionario.getId())) { // Verifica se existe apontamento >= data do movimento					
						validator.add(new ValidationMessage(getMessage("movimento.validacao.existePonto", funcionario.getNomeCurto(), Util.dataToString(movimento.getData())), "semCategoria"));
						movimento.setDataFim(movimento.getData());
					}
					validator.onErrorRedirectTo(this).formMovimento(movimento);
				}

				// So altera o status do funcionario se o movimento >= a data do status atual
				if (funcionario.getDataDemissao() == null || movimento.getData().compareTo(funcionario.getDataDemissao()) >= 0) {
					funcionario.setDataDemissao(movimento.getData());
					funcionario.setStatus(movimento.getNovoStatus());
				}
			}
			
			if (movimento.getBonus() != null) 
				funcionario.setBonusSalario(movimento.getBonus());
			
			if (movimento.getId() == null)
				dao.incluir(movimento);
			else 
				dao.alterar(movimento);
				
			dao.alterar(funcionario);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formMovimento.sucesso"));
			result.redirectTo("/funcionarios/" + movimento.getFuncionario().getId() + "/movimentos/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formMovimento.falha.salvar"));
			result.redirectTo(this).formMovimento(movimento);
		}
		
	}
		
	private Movimento consultarPorId(Long id) {
		
		Movimento movimento = null;
		try {
			
			movimento = dao.consultarPorId(id);

			if (movimento == null)
				validator.add(new ValidationMessage(getMessage("movimento.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("movimento.falha.recuperar", id));
			result.forwardTo("/funcionarios/listar");
		}
		
		return movimento;
	}
	
	private Funcionario buscarFuncionario(Long id) {

		if (id == null)
			return null;
		
		Funcionario funcionario = null;
		
		try {
			
			funcionario = funcionarioDao.consultarPorId(id);

			if (funcionario == null)
				validator.add(new ValidationMessage(getMessage("funcionario.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("movimento.falha.recuperar", id));
			result.forwardTo("/funcionarios/listar");
		}
		
		return funcionario;
	}
		
}
