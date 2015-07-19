package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ClienteDao;
import stallum.dto.FiltroCliente;
import stallum.dto.OrdenacaoCliente;
import stallum.dto.Paginacao;
import stallum.enums.Estado;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Cliente;
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
public class ClientesController extends AbstractController {

	private Logger log = Logger.getLogger(ClientesController.class);
	
	private final ClienteDao dao;

	public ClientesController(ClienteDao dao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
	}

	@Get @Path({"/clientes/novo", "/clientes/{cliente.id}/editar"})
	public Cliente formCliente(Cliente cliente) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (cliente == null)
				cliente = new Cliente();
			else if (cliente.getId() != null && cliente.getVersao() == null)
				cliente = consultarPorId(cliente.getId());
			
			result.include("estados", Estado.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formCliente.falha.carregar"));
			result.redirectTo("/clientes/listar");
		}
		
		return cliente;
	}
	
	@Put @Post @Path("/clientes")
	public void salvar(final Cliente cliente, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(cliente.getRazaoSocial()), "cliente.razaoSocial", "obrigatorio");
			that(preenchido(cliente.getCnpj()), "cliente.cnpj", "obrigatorio");
			that(cpfCnpjValido(cliente.getCnpj()), "cliente.cnpj", "invalido");
			that(preenchido(cliente.getTelefone1()), "cliente.telefone1", "obrigatorio");
			that(preenchido(cliente.getEmail()), "cliente.email", "obrigatorio");
			that(emailValido(cliente.getEmail()), "cliente.email", "invalido");
			that(preenchido(cliente.getEndereco().getLogradouro()), "endereco.logradouro", "obrigatorio");
			that(preenchido(cliente.getEndereco().getNumero()), "endereco.numero", "obrigatorio");
			that(preenchido(cliente.getEndereco().getCidade()), "endereco.cidade", "obrigatorio");
			that(preenchido(cliente.getEndereco().getEstado()) && !cliente.getEndereco().getEstado().equalsIgnoreCase("XX"), "endereco.estado", "obrigatorio");
			that(preenchido(cliente.getEndereco().getCep()), "endereco.cep", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formCliente(cliente);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
					
			if (cliente.getId() == null) {
				cliente.setAtivo(true);				
				dao.incluir(cliente);								
			} else {
				dao.alterar(cliente);
			}
			
			if (cliente.getEndereco().getId() == null)
				dao.incluir(cliente.getEndereco());
			else
				dao.alterar(cliente.getEndereco());
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formCliente.sucesso"));
			result.redirectTo("/clientes/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formCliente.falha.salvar"));
			result.redirectTo(this).formCliente(cliente);
		}
		
	}

	@Get @Path({"/clientes/{idRemover}/remover", "/clientes/{idRecuperar}/recuperar"})
	public void removerRecuperar(Long idRemover, Long idRecuperar) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Cliente cliente = null;
			String msg = getMessage("cliente.sucesso.remover", idRemover);
			
			if (idRemover != null) {
				cliente = consultarPorId(idRemover);
				cliente.setAtivo(false);
			} else {
				cliente = consultarPorId(idRecuperar);
				cliente.setAtivo(true);
				msg = getMessage("cliente.sucesso.recuperar", idRecuperar);
			}
			
			dao.alterar(cliente);
			
			tx.commit();
			
			sessao.addMensagem(msg);
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			if (idRemover != null)
				sessao.addErro(getMessage("cliente.falha.remover", idRemover));
			else
				sessao.addErro(getMessage("cliente.falha.recuperar", idRecuperar));
		}
		
		result.redirectTo("/clientes/listar");
	}
		
	@Get @Post @Path("/clientes/listar")
	public void listarClientes(FiltroCliente filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroCliente.newInstance(getRegistrosPagina());

			for (OrdenacaoCliente ordem: OrdenacaoCliente.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaClientes", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarClientes.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	private Cliente consultarPorId(Long id) {
		
		Cliente cliente = null;
		try {
			
			cliente = dao.consultarPorId(id);
			
			if (cliente == null)
				validator.add(new ValidationMessage(getMessage("cliente.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("cliente.falha.recuperar", id));
			result.forwardTo(this).listarClientes(null);
		}
		
		return cliente;
	}
	
}
