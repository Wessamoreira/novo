package negocio.facade.jdbc.academico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ControleLivroRegistroDiplomaUnidadeEnsinoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ControleLivroRegistroDiplomaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ControleLivroRegistroDiplomaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ControleLivroRegistroDiplomaVO
 * @see SuperEntidade
*/
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ControleLivroRegistroDiplomaUnidadeEnsino extends ControleAcesso implements ControleLivroRegistroDiplomaUnidadeEnsinoInterfaceFacade {
	
	private static final long serialVersionUID = -8902357054344793625L;
	private static final String TABLE_NAME = "controleLivroRegistroDiplomaUnidadeEnsino";
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ControleLivroRegistroDiplomaVO obj, UsuarioVO usuario) throws Exception {
		List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> lista = obj.getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(clu -> clu.getSelecionado()).collect(Collectors.toList());
		for (ControleLivroRegistroDiplomaUnidadeEnsinoVO controleUnidade : lista) {
			if (controleUnidade.getSelecionado()) {
				if (!Uteis.isAtributoPreenchido(controleUnidade.getCodigo())) {
					controleUnidade.setControleLivroRegistroDiploma(obj);
					incluir(controleUnidade, usuario);
				}
			} 
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleLivroRegistroDiplomaUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		incluir(obj, TABLE_NAME, new AtributoPersistencia().add("controlelivroregistrodiploma", obj.getControleLivroRegistroDiploma()).add("unidadeensino", obj.getUnidadeEnsino()), usuario);
	}
	    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final Integer codigoControleLivroRegistro, UsuarioVO usuario) throws Exception {
    	getConexao().getJdbcTemplate().update("delete from controlelivroregistrodiplomaunidadeensino where controlelivroregistrodiploma = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), codigoControleLivroRegistro);
    }
    
    @Override
	public void carregarUnidadeEnsinoNaoSelecionado(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select 0 as codigo, 0 as controlelivroregistrodiploma, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\"  ");		
		sb.append(" from unidadeEnsino ");
		sb.append(" where not exists (select codigo from controlelivroregistrodiplomaunidadeensino where unidadeEnsino.codigo = controlelivroregistrodiplomaunidadeensino.unidadeEnsino ");		
		sb.append(" and controlelivroregistrodiplomaunidadeensino.controlelivroregistrodiploma = ? ) order by unidadeEnsino.nome  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), controleLivroRegistroDiplomaVO.getCodigo());
		controleLivroRegistroDiplomaVO.getControleLivroRegistroDiplomaUnidadeEnsinoVOs().addAll(montarDadosConsulta(rs));
	}
    
    private ControleLivroRegistroDiplomaUnidadeEnsinoVO montarDados(SqlRowSet rs) {
    	ControleLivroRegistroDiplomaUnidadeEnsinoVO controleUnidade = new ControleLivroRegistroDiplomaUnidadeEnsinoVO();
    	controleUnidade.setNovoObj(rs.getInt("codigo") > 0);
		controleUnidade.setSelecionado(rs.getInt("codigo") > 0 ? true : false);
		controleUnidade.setCodigo(rs.getInt("codigo"));
		controleUnidade.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
		controleUnidade.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		controleUnidade.getControleLivroRegistroDiploma().setCodigo(rs.getInt("controlelivroregistrodiploma"));
		return controleUnidade;
	}
    
    public List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs) {
    	List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs = new ArrayList<ControleLivroRegistroDiplomaUnidadeEnsinoVO>(0);
    	while (rs.next()) {
			controleLivroRegistroDiplomaUnidadeEnsinoVOs.add(montarDados(rs));
		}
    	return controleLivroRegistroDiplomaUnidadeEnsinoVOs;
    }
    
    private StringBuilder getSqlPadrao() {
		StringBuilder sb = new StringBuilder("");
		sb.append("select controlelivroregistrodiplomaunidadeensino.codigo, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sb.append(" controlelivroregistrodiplomaunidadeensino.controlelivroregistrodiploma ");
		sb.append(" from controlelivroregistrodiplomaunidadeensino ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = controlelivroregistrodiplomaunidadeensino.unidadeEnsino ");
		return sb;
	}

	@Override
	public List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> consultarPorControleLivroRegistroDiploma(Integer codigo) throws Exception {
		StringBuilder sb = new StringBuilder(getSqlPadrao());
		sb.append(" where controlelivroregistrodiplomaunidadeensino.controlelivroregistrodiploma = ? order by unidadeEnsino.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), codigo);		
		return (montarDadosConsulta(rs));
	}

}