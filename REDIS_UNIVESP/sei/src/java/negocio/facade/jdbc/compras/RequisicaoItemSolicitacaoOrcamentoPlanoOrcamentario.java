package negocio.facade.jdbc.compras;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.recursoshumanos.AfastamentoFuncionario;
import negocio.interfaces.compras.RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>AfastamentoFuncionarioVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>AfastamentoFuncionarioVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario extends SuperFacade<RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO> 
	implements RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade<RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO> {

	private static final long serialVersionUID = 4963965047388186169L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persitir(RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else { 
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(obj, "RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario", new AtributoPersistencia()
				.add("requisicaoItem", obj.getRequisicaoItemVO())
				.add("itemSolicitacaoOrcamentoPlanoOrcamentario", obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO()) , usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
		
	}

	@Override
	public void alterar(RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, boolean validarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		alterar(obj, "RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario", new AtributoPersistencia()
				.add("requisicaoItem", obj.getRequisicaoItemVO())
				.add("itemSolicitacaoOrcamentoPlanoOrcamentario", obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO()) ,
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
		
	}

	@Override
	public void excluir(RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, boolean validarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		AfastamentoFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario WHERE codigo = ?").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO montarDados(SqlRowSet tabelaResultado,
			int nivelMontarDados) throws Exception {
		RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = new RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("requisicaoItem"))) {
			obj.setRequisicaoItemVO(getFacadeFactory().getRequisicaoItemFacade().consultarPorChavePrimaria(tabelaResultado.getInt("requisicaoItem"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("itemSolicitacaoOrcamentoPlanoOrcamentario"))) {
			obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().
					consultarPorChavePrimaria( tabelaResultado.getInt("itemSolicitacaoOrcamentoPlanoOrcamentario"), null));
		}
		return obj;
	}

	@Override
	public void validarDados(RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getRequisicaoItemVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_funcionarioCargo"));
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_funcionarioCargo"));
		}
	}
}
