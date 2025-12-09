package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.segmentacao.ProspectSegmentacaoOpcaoVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.ProspectSegmentacaoOpcaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CursoInteresseVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CursoInteresseVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see CursoInteresseVO
 * @see SuperEntidade
 * @see Prospects
 */
@Repository
@Scope("singleton")
@Lazy
public class ProspectSegmentacaoOpcao extends ControleAcesso implements ProspectSegmentacaoOpcaoInterfaceFacade {

	protected static String idEntidade;

	public ProspectSegmentacaoOpcao() throws Exception {
		super();
		setIdEntidade("ProspectSegmentacaoOpcao");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CursoInteresseVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CursoInteresseVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProspectSegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "INSERT INTO prospectsegmentacaoopcao(segmentacaoopcao, prospect) VALUES (?, ?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Long) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement sqlInserir = conn.prepareStatement(sql);

				sqlInserir.setInt(1, obj.getSegmentacaoOpcao().getCodigo().intValue());
				sqlInserir.setInt(2, obj.getProspect().getCodigo().intValue());

				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet res) throws SQLException, DataAccessException {
				if (res.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return res.getLong("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProspectSegmentacaoOpcoes(ProspectsVO prospect, UsuarioVO usuario) throws Exception {
		Iterator<ProspectSegmentacaoOpcaoVO> e = prospect.getProspectSegmentacaoOpcaoVOs().iterator();
		while (e.hasNext()) {
			ProspectSegmentacaoOpcaoVO obj = (ProspectSegmentacaoOpcaoVO) e.next();
			incluir(obj, usuario);

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProspectSegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE prospectsegmentacaoopcao SET segmentacaoopcao=?, prospect=? WHERE ((codigo = ?));" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getSegmentacaoOpcao().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getSegmentacaoOpcao().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (obj.getProspect().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getProspect().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setLong(3, obj.getCodigo().longValue());

				return sqlAlterar;
			}
		}) == 0){
			incluir(obj, usuario);
			return;
		};
	}

	@Override
	public void alterarProspectsSegmentacaoOpcao(Integer prospects, List<ProspectSegmentacaoOpcaoVO> objetos, UsuarioVO usuario) throws Exception {
		String str = "DELETE FROM prospectsegmentacaoopcao WHERE prospect = ?";
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			ProspectSegmentacaoOpcaoVO objeto = (ProspectSegmentacaoOpcaoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(str, new Object[] { prospects });
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ProspectSegmentacaoOpcaoVO objeto = (ProspectSegmentacaoOpcaoVO) e.next();
			objeto.getProspect().setCodigo(prospects);
			if (objeto.getCodigo().equals(0l)) {
				incluir(objeto, usuario);
			} else {
				alterar(objeto, usuario);
			}
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProspectSegmentacaoOpcao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProspectSegmentacaoOpcao.idEntidade = idEntidade;
	}

}
