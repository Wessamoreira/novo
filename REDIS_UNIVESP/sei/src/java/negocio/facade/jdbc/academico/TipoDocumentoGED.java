package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.TipoDocumentoGEDVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoLocalizadoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TipoDocumentoGEDInterfaceFacade;

@Service
@Scope
@Lazy 
public class TipoDocumentoGED extends ControleAcesso implements TipoDocumentoGEDInterfaceFacade {

	private static final long serialVersionUID = 2334813562862086724L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(TipoDocumentoGEDVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
		if(Uteis.isAtributoPreenchido(obj.getDocumetacaoMatricula())) {
			getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentacaoGed(obj.getDocumetacaoMatricula(), usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(TipoDocumentoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO TipoDocumentoGED( documentacaoGED, tipoDocumento, documetacaoMatricula, situacaoDocumentoLocalizadoEnum, identificadorGed) VALUES ( ?, ?, ?, ?, ?) returning codigo");

			if (usuario != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			}

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					if (!obj.getDocumentacaoGED().getCodigo().equals(0)) {
						sqlInserir.setInt(1, obj.getDocumentacaoGED().getCodigo());						
					} else {
						sqlInserir.setNull(1, 0);
					}

					if (!obj.getTipoDocumento().getCodigo().equals(0)) {
						sqlInserir.setInt(2, obj.getTipoDocumento().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}

					if (!obj.getDocumetacaoMatricula().getCodigo().equals(0)) {						
						sqlInserir.setInt(3, obj.getDocumetacaoMatricula().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}

					if (obj.getSituacaoDocumentoLocalizadoEnum() != null) {
						sqlInserir.setString(4, obj.getSituacaoDocumentoLocalizadoEnum().getValor());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getIdentificadorGed());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(TipoDocumentoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE TipoDocumentoGED set documentacaoGED=?, tipoDocumento=?, documetacaoMatricula=?, situacaoDocumentoLocalizadoEnum =?, identificadorGed =? WHERE codigo = ?");

			if (usuario != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			}
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					if (!obj.getDocumentacaoGED().getCodigo().equals(0)) {
						sqlAlterar.setInt(1, obj.getDocumentacaoGED().getCodigo());						
					} else {
						sqlAlterar.setNull(1, 0);
					}

					if (!obj.getTipoDocumento().getCodigo().equals(0)) {
						sqlAlterar.setInt(2, obj.getTipoDocumento().getCodigo());						
					} else {
						sqlAlterar.setNull(2, 0);
					}

					if (!obj.getDocumetacaoMatricula().getCodigo().equals(0)) {
						sqlAlterar.setInt(3, obj.getDocumetacaoMatricula().getCodigo());						
					} else {
						sqlAlterar.setNull(3, 0);
					}

					if (obj.getSituacaoDocumentoLocalizadoEnum() != null) {
						sqlAlterar.setString(4, obj.getSituacaoDocumentoLocalizadoEnum().getValor());
					} else {
						sqlAlterar.setNull(4, 0);
					}

					sqlAlterar.setString(5, obj.getIdentificadorGed());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				obj.setNovoObj(true);
				obj.setCodigo(0);
				persistir(obj, validarAcesso, usuario);
				return;
			};
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DocumentacaoGEDVO documentacaoGEDVO, TipoDocumentoGEDVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM TipoDocumentoGED WHERE ((codigo = ?))");
			if (usuarioVO != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			}
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
			if(Uteis.isAtributoPreenchido(obj.getDocumetacaoMatricula())) {
				
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorDocumentacaoGED(DocumentacaoGEDVO documentacaoGed, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM TipoDocumentoGED WHERE ((documentacaoged = ?))");

			if (usuarioVO != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			}
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { documentacaoGed.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List consultarPorDocumentacaoGED(boolean controlarAcesso, UsuarioVO usuario, int CodigoDocumentacaoGED) throws Exception {
		List<TipoDocumentoGEDVO> listaDocumentacaoGEDTipoDocumento = new ArrayList<>(0);
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE documentacaoged = ").append(String.valueOf(CodigoDocumentacaoGED));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			listaDocumentacaoGEDTipoDocumento.add(montarDadosBasico(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return listaDocumentacaoGEDTipoDocumento;
	}

	private TipoDocumentoGEDVO montarDadosBasico(SqlRowSet tabelaResultado, int nivelMontarDados) {
		TipoDocumentoGEDVO obj = new TipoDocumentoGEDVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getTipoDocumento().setCodigo(tabelaResultado.getInt("tipodocumento.codigo"));
		obj.getTipoDocumento().setNome(tabelaResultado.getString("tipodocumento.nome"));
		obj.getTipoDocumento().setIdentificadorGED(tabelaResultado.getString("tipodocumento.identificadorGED"));
		obj.getDocumetacaoMatricula().setCodigo(tabelaResultado.getInt("documetacaomatricula"));
		obj.getDocumetacaoMatricula().setEntregue(tabelaResultado.getBoolean("entregue"));
		obj.getDocumentacaoGED().setCodigo(tabelaResultado.getInt("documentacaoged"));
		if(tabelaResultado.getObject("SituacaoDocumentoLocalizadoEnum") != null) {
			obj.setSituacaoDocumentoLocalizadoEnum(SituacaoDocumentoLocalizadoEnum.valueOf(tabelaResultado.getString("SituacaoDocumentoLocalizadoEnum")));
		}
		obj.setIdentificadorGed(tabelaResultado.getString("tipodocumentoged.identificadorGed"));
		return obj;
	}

	public String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tipodocumentoged.codigo, tipodocumento.codigo as \"tipodocumento.codigo\" , tipodocumento.nome as \"tipodocumento.nome\" , documetacaomatricula.codigo as documetacaomatricula, SituacaoDocumentoLocalizadoEnum, ");
		sql.append("tipodocumento.identificadorGED as \"tipodocumento.identificadorGED\", tipodocumentoged.identificadorGed as \"tipodocumentoged.identificadorGed\", documentacaoged, documetacaomatricula.entregue ");
		sql.append("FROM tipodocumentoged LEFT JOIN tipodocumento ON tipodocumento.codigo = tipodocumentoged.tipodocumento ");
		sql.append("LEFT JOIN documetacaomatricula ON documetacaomatricula.codigo = tipodocumentoged.documetacaomatricula ");

		return sql.toString();
	}

	@Override
	public void validarDados(TipoDocumentoGEDVO obj) throws ConsistirException {
		if (obj.getDocumentacaoGED() == null || obj.getDocumentacaoGED().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoDocumentoGED_documentacaoGED"));
		}
	}
}