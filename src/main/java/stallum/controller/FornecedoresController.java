package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.FornecedorDao;
import stallum.dto.FiltroFornecedor;
import stallum.dto.OrdenacaoFornecedor;
import stallum.dto.Paginacao;
import stallum.enums.Estado;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Fornecedor;
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
public class FornecedoresController extends AbstractController {

	private Logger log = Logger.getLogger(FornecedoresController.class);
	
	private final FornecedorDao dao;

	public FornecedoresController(FornecedorDao dao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
	}

	@Get @Path({"/fornecedores/novo", "/fornecedores/{fornecedor.id}/editar"})
	public Fornecedor formFornecedor(Fornecedor fornecedor) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (fornecedor == null)
				fornecedor = new Fornecedor();
			else if (fornecedor.getId() != null && fornecedor.getVersao() == null)
				fornecedor = consultarPorId(fornecedor.getId());
			
			result.include("estados", Estado.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formFornecedor.falha.carregar"));
			result.redirectTo("/fornecedores/listar");
		}
		
		return fornecedor;
	}
	
	@Put @Post @Path("/fornecedores")
	public void salvar(final Fornecedor fornecedor, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(fornecedor.getRazaoSocial()), "fornecedor.razaoSocial", "obrigatorio");
			that(preenchido(fornecedor.getCnpj()), "fornecedor.cnpj", "obrigatorio");
			that(cpfCnpjValido(fornecedor.getCnpj()), "fornecedor.cnpj", "invalido");
			that(preenchido(fornecedor.getTelefone1()), "fornecedor.telefone1", "obrigatorio");
			that(preenchido(fornecedor.getEmail()), "fornecedor.email", "obrigatorio");
			that(emailValido(fornecedor.getEmail()), "fornecedor.email", "invalido");
			that(preenchido(fornecedor.getEndereco().getLogradouro()), "endereco.logradouro", "obrigatorio");
			that(preenchido(fornecedor.getEndereco().getNumero()), "endereco.numero", "obrigatorio");
			that(preenchido(fornecedor.getEndereco().getCidade()), "endereco.cidade", "obrigatorio");
			that(preenchido(fornecedor.getEndereco().getEstado()) && !fornecedor.getEndereco().getEstado().equalsIgnoreCase("XX"), "endereco.estado", "obrigatorio");
			that(preenchido(fornecedor.getEndereco().getCep()), "endereco.cep", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formFornecedor(fornecedor);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
					
			if (fornecedor.getId() == null) {
				fornecedor.setAtivo(true);				
				dao.incluir(fornecedor);								
			} else {
				dao.alterar(fornecedor);
			}
			
			if (fornecedor.getEndereco().getId() == null)
				dao.incluir(fornecedor.getEndereco());
			else
				dao.alterar(fornecedor.getEndereco());
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formFornecedor.sucesso"));
			result.redirectTo("/fornecedores/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formFornecedor.falha.salvar"));
			result.redirectTo(this).formFornecedor(fornecedor);
		}
		
	}

	@Get @Path({"/fornecedores/{idRemover}/remover", "/fornecedores/{idRecuperar}/recuperar"})
	public void removerRecuperar(Long idRemover, Long idRecuperar) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Fornecedor fornecedor = null;
			String msg = getMessage("fornecedor.sucesso.remover", idRemover);
			
			if (idRemover != null) {
				fornecedor = consultarPorId(idRemover);
				fornecedor.setAtivo(false);
			} else {
				fornecedor = consultarPorId(idRecuperar);
				fornecedor.setAtivo(true);
				msg = getMessage("fornecedor.sucesso.recuperar", idRecuperar);
			}
			
			dao.alterar(fornecedor);
			
			tx.commit();
			
			sessao.addMensagem(msg);
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			if (idRemover != null)
				sessao.addErro(getMessage("fornecedor.falha.remover", idRemover));
			else
				sessao.addErro(getMessage("fornecedor.falha.recuperar", idRecuperar));
		}
		
		result.redirectTo("/fornecedores/listar");
	}
		
	@Get @Post @Path("/fornecedores/listar")
	public void listarFornecedores(FiltroFornecedor filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroFornecedor.newInstance(getRegistrosPagina());

			for (OrdenacaoFornecedor ordem: OrdenacaoFornecedor.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaFornecedores", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarFornecedores.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	private Fornecedor consultarPorId(Long id) {
		
		Fornecedor fornecedor = null;
		try {
			
			fornecedor = dao.consultarPorId(id);
			
			if (fornecedor == null)
				validator.add(new ValidationMessage(getMessage("fornecedor.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("fornecedor.falha.recuperar", id));
			result.forwardTo(this).listarFornecedores(null);
		}
		
		return fornecedor;
	}
	
}
