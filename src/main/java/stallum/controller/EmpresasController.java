package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.EmpresaDao;
import stallum.dto.FiltroEmpresa;
import stallum.dto.OrdenacaoEmpresa;
import stallum.dto.Paginacao;
import stallum.enums.Estado;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Empresa;
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
public class EmpresasController extends AbstractController {

	private Logger log = Logger.getLogger(EmpresasController.class);
	
	private final EmpresaDao dao;

	public EmpresasController(EmpresaDao dao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
	}

	@Get @Path({"/empresas/nova", "/empresas/{empresa.id}/editar"})
	public Empresa formEmpresa(Empresa empresa) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (empresa == null)
				empresa = new Empresa();
			else if (empresa.getId() != null && empresa.getVersao() == null)
				empresa = consultarPorId(empresa.getId());
			
			result.include("estados", Estado.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formEmpresa.falha.carregar"));
			result.redirectTo("/empresas/listar");
		}
		
		return empresa;
	}
	
	@Put @Post @Path("/empresas")
	public void salvar(final Empresa empresa) {
		
		validator.checking(new Validations() {{
			that(preenchido(empresa.getNomeCurto()), "empresa.nomeCurto", "obrigatorio");
			that(preenchido(empresa.getRazaoSocial()), "empresa.razaoSocial", "obrigatorio");
			that(preenchido(empresa.getCnpj()), "empresa.cnpj", "obrigatorio");
			that(cpfCnpjValido(empresa.getCnpj()), "empresa.cnpj", "invalido");
			that(preenchido(empresa.getTelefone1()), "empresa.telefone1", "obrigatorio");
			that(preenchido(empresa.getEmail()), "empresa.email", "obrigatorio");
			that(emailValido(empresa.getEmail()), "empresa.email", "invalido");
			that(preenchido(empresa.getEndereco().getLogradouro()), "endereco.logradouro", "obrigatorio");
			that(preenchido(empresa.getEndereco().getNumero()), "endereco.numero", "obrigatorio");
			that(preenchido(empresa.getEndereco().getCidade()), "endereco.cidade", "obrigatorio");
			that(preenchido(empresa.getEndereco().getEstado()) && !empresa.getEndereco().getEstado().equalsIgnoreCase("XX"), "endereco.estado", "obrigatorio");
			that(preenchido(empresa.getEndereco().getCep()), "endereco.cep", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formEmpresa(empresa);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			if (empresa.getId() == null)
				dao.incluir(empresa);								
			else
				dao.alterar(empresa);
			
			if (empresa.getEndereco().getId() == null)
				dao.incluir(empresa.getEndereco());
			else
				dao.alterar(empresa.getEndereco());
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formEmpresa.sucesso"));
			result.redirectTo("/empresas/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formEmpresa.falha.salvar"));
			result.redirectTo(this).formEmpresa(empresa);
		}
		
	}
	
	@Get @Path("/empresas/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Empresa empresa = consultarPorId(id);
			
			dao.excluir(empresa);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("empresa.sucesso.remover", id));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("empresa.falha.remover", id));
		}
		
		result.redirectTo("/empresas/listar");
	}

	private Empresa consultarPorId(Long id) {
		
		Empresa empresa = null;
		try {
			
			empresa = dao.consultarPorId(id);

			if (empresa == null)
				validator.add(new ValidationMessage(getMessage("empresa.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("empresa.falha.recuperar", id));
			result.forwardTo(this).listarEmpresas(null);
		}
		
		return empresa;
	}
	
	@Get @Post @Path("/empresas/listar")
	public void listarEmpresas(FiltroEmpresa filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroEmpresa.newInstance(getRegistrosPagina());

			for (OrdenacaoEmpresa ordem: OrdenacaoEmpresa.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaEmpresas", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarEmpresas.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
}
