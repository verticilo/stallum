package stallum.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.BonificacaoDao;
import stallum.dao.FuncionarioDao;
import stallum.dto.FiltroBonificacao;
import stallum.dto.FiltroFuncionario;
import stallum.dto.OrdenacaoBonificacao;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Bonificacao;
import stallum.modelo.Funcionario;
import stallum.modelo.ValeRefeicao;
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
public class BonificacoesController extends AbstractController {

	private Logger log = Logger.getLogger(BonificacoesController.class);
	
	private final BonificacaoDao dao;
	private final FuncionarioDao funcionarioDao;

	public BonificacoesController(BonificacaoDao dao, FuncionarioDao funcionarioDao,
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.funcionarioDao = funcionarioDao;
	}

	@Get @Post @Path({"/bonificacoes/nova", "/bonificacoes/{bonificacao.id}/editar"})
	public Bonificacao formBonificacao(Bonificacao bonificacao, List<ValeRefeicao> vales) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {
			
			if (bonificacao == null)
				bonificacao = new Bonificacao();
			
			// Editar
			if (bonificacao.getId() != null && bonificacao.getVersao() == null) {		
				bonificacao = consultarPorId(bonificacao.getId());
				vales = (List<ValeRefeicao>)bonificacao.getVales();
			}
			
			// Se for novo lancamento (vales == null) , verifica se ja houve lancamento da obra no dia e, 
			// caso tenha, carrega o bonificacao com seus vales para edicao.
			if (vales == null) {
				Bonificacao apontBanco = dao.conultarPorData(bonificacao);
				if (apontBanco != null) {
					bonificacao.setId(apontBanco.getId());
					bonificacao.setData(apontBanco.getData());
					bonificacao.setObs(apontBanco.getObs());
					bonificacao.setAlteradoEm(apontBanco.getAlteradoEm());
					vales = (List<ValeRefeicao>)apontBanco.getVales();
				}
			}
							
			if (vales == null)
				vales = new ArrayList<ValeRefeicao>();
			
			// Completa a lista com os funcionario nao lancados
			Collection<Funcionario> funcionarios = funcionarioDao.filtrar(FiltroFuncionario.newInstance(-1));
			for (Funcionario funcionario: funcionarios) {
				ValeRefeicao vale = new ValeRefeicao();
				vale.setFuncionario(funcionario);
				if (!vales.contains(vale))
					vales.add(vale);
			}
			
			result.include("vales", vales);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formBonificacao.falha.carregar"));
			result.redirectTo("/bonificacoes/listar");
		}
		
		return bonificacao;
		
	}
	
	@Post @Path("/bonificacoes")
	public void salvar(final Bonificacao bonificacao, List<ValeRefeicao> vales) {
		
		validaSessao(PerfilUsuario.OPERADOR);
				
		validator.checking(new Validations() {{
			that(preenchido(bonificacao.getData()), "bonificacao.data", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formBonificacao(bonificacao, vales);
		
		Transaction tx = dao.getSession().beginTransaction();		
		
		try {
			
			for (ValeRefeicao vale: vales) {				
				if (Boolean.TRUE.equals(vale.getRecebe())) {
					vale.setBonificacao(bonificacao);
					if (vale.getId() == null)
						dao.incluir(vale);
					else
						dao.alterar(vale);
				} else if (vale.getId() != null) {
					dao.excluir(vale);
				}
			}
				
			if (bonificacao.getId() == null)
				dao.incluir(bonificacao);
			else
				dao.alterar(bonificacao);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formBonificacao.sucesso"));
			
			result.redirectTo("/bonificacoes/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formBonificacao.falha.salvar"));
			result.forwardTo(this).formBonificacao(bonificacao, vales);
		}
		
	}
	
	@Get @Post @Path("/bonificacoes/listar")
	public void listarBonificacoes(FiltroBonificacao filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroBonificacao.newInstance(getRegistrosPagina());

			for (OrdenacaoBonificacao ordem: OrdenacaoBonificacao.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaBonificacoes", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarBonificacoes.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	private Bonificacao consultarPorId(Long id) {
		
		Bonificacao bonificacao = null;
		try {
			
			bonificacao = dao.consultarPorId(id);
			if (bonificacao == null)
				validator.add(new ValidationMessage(getMessage("bonificacao.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("bonificacao.falha.recuperar", id));
			result.forwardTo("/bonificacoes/listar");
		}
		
		return bonificacao;
	}
	
}
