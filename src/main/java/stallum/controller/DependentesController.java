package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.DependenteDao;
import stallum.dao.FuncionarioDao;
import stallum.dto.FiltroDependente;
import stallum.dto.OrdenacaoDependente;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Dependente;
import stallum.modelo.Funcionario;
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
public class DependentesController extends AbstractController {

	private Logger log = Logger.getLogger(DependentesController.class);
	
	private final DependenteDao dao;
	private final FuncionarioDao funcionarioDao;

	public DependentesController(DependenteDao dao, FuncionarioDao funcionarioDao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.funcionarioDao = funcionarioDao;
	}
	
	
	@Get @Path({"/funcionarios/{dependente.funcionario.id}/dependentes/novo", "/dependentes/{dependente.id}/editar"})
	public Dependente formDependente(Dependente dependente) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (dependente.getId() == null) {
				Funcionario funcionario = buscarFuncionario(dependente.getFuncionario().getId());
				dependente = new Dependente();
				dependente.setFuncionario(funcionario);
			} else if (dependente.getId() != null && dependente.getVersao() == null) {
				dependente = consultarPorId(dependente.getId());
			}
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDependente.falha.carregar"));
			result.redirectTo("/funcionarios/listar");
		}
		
		return dependente;
		
	}
	
	@Get @Post @Path("/funcionarios/{filtro.funcionario.id}/dependentes/listar")
	public void listarDependentes(FiltroDependente filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			Funcionario funcionario = buscarFuncionario(filtro.getFuncionario().getId());
			
			if (filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroDependente.newInstance(getRegistrosPagina());

			for (OrdenacaoDependente ordem: OrdenacaoDependente.values())
				filtro.addOrdencacao(ordem);
			
			filtro.setFuncionario(funcionario);
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaDependentes", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarDependentes.falha.listar"));
			result.redirectTo("/funcionarios/listar");
		}	
	}
	
	@Get @Path("/dependentes/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.OPERADOR);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		Long idFuncionario = null;
		
		try {
			
			Dependente dependente = consultarPorId(id);
			
			idFuncionario = dependente.getFuncionario().getId();
			
			dao.excluir(dependente);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("dependente.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("dependente.falha.remover", id));
		}
		
		result.redirectTo("/funcionarios/" + idFuncionario + "/dependentes/listar");
	}

	
	@Put @Post @Path("/dependentes")
	public void salvar(final Dependente dependente, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(dependente.getFuncionario()), "dependente.funcionario", "obrigatorio");
			that(preenchido(dependente.getNome()), "dependente.nome", "obrigatoria");
			that(preenchido(dependente.getDataNascimento()), "dependente.dataNascimento", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formDependente(dependente);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			if (dependente.getId() == null)
				dao.incluir(dependente);								
			else
				dao.alterar(dependente);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formDependente.sucesso"));
			result.redirectTo("/funcionarios/" + dependente.getFuncionario().getId() + "/dependentes/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDependente.falha.salvar"));
			result.redirectTo(this).formDependente(dependente);
		}
		
	}
		
	private Dependente consultarPorId(Long id) {
		
		Dependente dependente = null;
		try {
			
			dependente = dao.consultarPorId(id);

			if (dependente == null)
				validator.add(new ValidationMessage(getMessage("dependente.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("dependente.falha.recuperar", id));
			result.forwardTo("/funcionarios/listar");
		}
		
		return dependente;
	}
	
	private Funcionario buscarFuncionario(Long id) {

		if (id == null)
			return null;
		
		Funcionario funcionario = null;
		
		try {
			
			funcionario = funcionarioDao.buscarPorId(id);

			if (funcionario == null)
				validator.add(new ValidationMessage(getMessage("funcionario.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("dependente.falha.recuperar", id));
			result.forwardTo("/funcionarios/listar");
		}
		
		return funcionario;
	}
		
}
