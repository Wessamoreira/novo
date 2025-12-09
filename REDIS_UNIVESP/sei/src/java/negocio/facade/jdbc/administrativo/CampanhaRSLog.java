package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.CampanhaRSLogVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaRSLogInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CampanhaRSLog extends ControleAcesso implements CampanhaRSLogInterfaceFacade{

	private static final long serialVersionUID = 1L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CampanhaRSLogVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO CampanhaRSLog( data, campanha, codigoUnidadeEnsino, codigoCurso, codigoTurno, nome, email, telefoneResidencial, celular, prospects, tipoCompromisso, compromissoAgendaPessoaHorario, colaborador, duvida, mensagem, ocorreuErro, usuarioOperacoesExternas, configuracaoGeralSistema) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setInt(2, obj.getCampanha());
					sqlInserir.setInt(3, obj.getCodigoUnidadeEnsino());
					sqlInserir.setInt(4, obj.getCodigoCurso());
					sqlInserir.setInt(5, obj.getCodigoTurno());
					sqlInserir.setString(6, obj.getNome());
					sqlInserir.setString(7, obj.getEmail());
					sqlInserir.setString(8, obj.getTelefoneResidencial());
					sqlInserir.setString(9, obj.getCelular());
					sqlInserir.setInt(10, obj.getProspectsVO().getCodigo());
					if (obj.getTipoCompromisso() != null) {
						sqlInserir.setString(11, obj.getTipoCompromisso().toString());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setInt(12, obj.getCompromissoAgendaPessoaHorarioVO().getCodigo());
					sqlInserir.setInt(13, obj.getColaboradorVO().getCodigo());
					sqlInserir.setString(14, obj.getDuvida());
					sqlInserir.setString(15, obj.getMensagem());
					sqlInserir.setBoolean(16, obj.getOcorreuErro());
					sqlInserir.setInt(17, obj.getUsuarioOperacoesExternasVO().getCodigo());
					sqlInserir.setInt(18, obj.getConfiguracaoGeralSistemaVO().getCodigo());

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CampanhaRSLogVO obj, UsuarioVO usuario) throws Exception {
		try {

			final String sql = "UPDATE CampanhaRSLog set data=?, campanha=?, codigoUnidadeEnsino=?, codigoCurso=?, codigoTurno=?, nome=?, email=?, telefoneResidencial=?, celular=?, prospects=?, tipoCompromisso=?, compromissoAgendaPessoaHorario=?, colaborador=?, duvida=?, mensagem=?, ocorreuErro=?, usuarioOperacoesExternas=?, configuracaoGeralSistema=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setInt(2, obj.getCampanha());
					sqlAlterar.setInt(3, obj.getCodigoUnidadeEnsino());
					sqlAlterar.setInt(4, obj.getCodigoCurso());
					sqlAlterar.setInt(5, obj.getCodigoTurno());
					sqlAlterar.setString(6, obj.getNome());
					sqlAlterar.setString(7, obj.getEmail());
					sqlAlterar.setString(8, obj.getTelefoneResidencial());
					sqlAlterar.setString(9, obj.getCelular());
					sqlAlterar.setInt(10, obj.getProspectsVO().getCodigo());
					if (obj.getTipoCompromisso() != null) {
						sqlAlterar.setString(11, obj.getTipoCompromisso().toString());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setInt(12, obj.getCompromissoAgendaPessoaHorarioVO().getCodigo());
					sqlAlterar.setInt(13, obj.getColaboradorVO().getCodigo());
					sqlAlterar.setString(14, obj.getDuvida());
					sqlAlterar.setString(15, obj.getMensagem());
					sqlAlterar.setBoolean(16, obj.getOcorreuErro());
					sqlAlterar.setInt(17, obj.getUsuarioOperacoesExternasVO().getCodigo());
					sqlAlterar.setInt(18, obj.getConfiguracaoGeralSistemaVO().getCodigo());
					sqlAlterar.setInt(19, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

}
