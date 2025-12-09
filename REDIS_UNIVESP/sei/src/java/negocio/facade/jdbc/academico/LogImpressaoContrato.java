/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.LogImpressaoContratoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogImpressaoContratoInterfaceFacade;

/**
 * 
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class LogImpressaoContrato extends ControleAcesso implements LogImpressaoContratoInterfaceFacade {

	private static String idEntidade;

	public static String getIdEntidade() {
		return idEntidade;
	}

	public void setIdEntidade(String aIdEntidade) {
		idEntidade = aIdEntidade;
	}

	public LogImpressaoContrato() throws Exception {
		super();
		setIdEntidade("LogImpressaoContrato");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogImpressaoContratoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LogImpressaoContrato(matricula, usuarioRespImpressao, tipoContrato, textopadrao, assinado, impressaodeclaracao, datageracao) VALUES (?,?,?,?,?,?,?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMatricula().getMatricula());
					sqlInserir.setInt(2, obj.getUsuarioRespImpressao().getCodigo());
					sqlInserir.setString(3, obj.getTipoContrato());
					sqlInserir.setInt(4, obj.getTextoPadrao().getCodigo());
					sqlInserir.setBoolean(5, obj.getAssinado());
					if (Uteis.isAtributoPreenchido(obj.getImpressaoContrato().getCodigo())) {
						sqlInserir.setInt(6, obj.getImpressaoContrato().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(new Date()));
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LogImpressaoContratoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE LogImpressaoContrato SET assinado=?, usuarioRespAssinatura=? WHERE ((codigo =? ))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getAssinado());
					if (obj.getUsuarioRespAssinatura().getCodigo() != 0) {
						sqlAlterar.setInt(2, obj.getUsuarioRespAssinatura().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPorImpressaoContrato(ImpressaoContratoVO obj, UsuarioVO usuario) throws Exception {
		try {

			for (LogImpressaoContratoVO logImpressaoContratoVO : obj.getListaLogImpressaoContratoVO()) {
				alterar(logImpressaoContratoVO, usuario);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder str = new StringBuilder();
		str.append("SELECT LogImpressaoContrato.codigo AS codigo, LogImpressaoContrato.assinado AS assinado, usuarioRespAssinatura.codigo AS codigoUsuarioRespAssinatura, LogImpressaoContrato.datageracao as \"LogImpressaoContrato.datageracao\", ");
		str.append("usuarioRespAssinatura.nome AS nomeUsuarioRespAssinatura, usuarioRespImpressao.codigo AS codigoUsuarioRespImpressao, usuarioRespImpressao.nome AS nomeUsuarioRespImpressao, ");
		str.append("LogImpressaoContrato.matricula AS matricula, LogImpressaoContrato.tipoContrato AS tipoContrato, ");
		str.append("textopadrao.codigo AS codigoTextoPadrao, textopadrao.descricao AS descricaoTextopadrao, ");
		str.append("textopadrao.tipoDesigneTextoEnum AS tipoDesigneTextoEnum, textopadrao.assinarDigitalmenteTextoPadrao AS assinarDigitalmenteTextoPadrao, ");
		str.append("documentoassinado.codigo as \"documentoassinado.codigo\", ");
		str.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.nome as \"arquivo.nome\", ");
		str.append("arquivo.descricao as \"arquivo.descricao\", arquivo.situacao as \"arquivo.situacao\", ");
		str.append("arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\"");
		str.append(" FROM LogImpressaoContrato ");
		str.append(" INNER JOIN usuario AS usuarioRespImpressao ON usuarioRespImpressao.codigo = LogImpressaoContrato.usuarioRespImpressao ");
		str.append(" LEFT JOIN usuario AS usuarioRespAssinatura ON usuarioRespAssinatura.codigo = LogImpressaoContrato.usuarioRespAssinatura ");
		str.append(" INNER JOIN textopadrao ON textopadrao.codigo = LogImpressaoContrato.textopadrao ");
		str.append(" LEFT JOIN impressaodeclaracao ON impressaodeclaracao.codigo = LogImpressaoContrato.impressaodeclaracao ");
		str.append(" LEFT JOIN documentoassinado ON documentoassinado.codigo = impressaodeclaracao.documentoassinado ");
		str.append(" LEFT JOIN arquivo ON arquivo.codigo =  documentoassinado.arquivo  ");
		return str;
	}

	public LogImpressaoContratoVO consultarUltimoContratoPorMatriculaPorTextoPadrao(String matricula, Integer textoPadrao, String tipoContrato, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaBasica();
		sb.append("WHERE LogImpressaoContrato.codigo = ( ");
		sb.append(" select MAX(codigo) from  LogImpressaoContrato ");
		sb.append("WHERE matricula = '").append(matricula).append("' ");
		sb.append("and textopadrao = ").append(textoPadrao).append(" ");
		sb.append("and tipocontrato = '").append(tipoContrato).append("' ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if(tabelaResultado.next()){
			return montarDados(tabelaResultado, usuarioVO);	
		}
		return new LogImpressaoContratoVO();
	}
	
	public List<LogImpressaoContratoVO> consultarPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaBasica();
		sb.append("WHERE LogImpressaoContrato.matricula = '").append(matricula).append("' order by LogImpressaoContrato.datageracao desc, LogImpressaoContrato.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public static List<LogImpressaoContratoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<LogImpressaoContratoVO> vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuarioVO));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	private static LogImpressaoContratoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		LogImpressaoContratoVO obj = new LogImpressaoContratoVO();
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.setDataGeracao(dadosSQL.getDate("LogImpressaoContrato.datageracao"));
		obj.getUsuarioRespImpressao().setCodigo(dadosSQL.getInt("codigoUsuarioRespImpressao"));
		if (Uteis.isAtributoPreenchido(obj.getUsuarioRespImpressao())) {
			obj.setUsuarioRespImpressao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioRespImpressao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
		}
		obj.getUsuarioRespAssinatura().setCodigo(dadosSQL.getInt("codigoUsuarioRespAssinatura"));
		if (Uteis.isAtributoPreenchido(obj.getUsuarioRespAssinatura())) {
			obj.setUsuarioRespAssinatura(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioRespAssinatura().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
		}
		obj.setAssinado(dadosSQL.getBoolean("assinado"));
		obj.setTipoContrato(dadosSQL.getString("tipoContrato"));
		obj.getTextoPadrao().setCodigo(dadosSQL.getInt("codigoTextoPadrao"));
		obj.getTextoPadrao().setDescricao(dadosSQL.getString("descricaoTextopadrao"));
		obj.getTextoPadrao().setTipoDesigneTextoEnum(TipoDesigneTextoEnum.valueOf(dadosSQL.getString("tipoDesigneTextoEnum")));
		obj.getTextoPadrao().setAssinarDigitalmenteTextoPadrao(dadosSQL.getBoolean("assinarDigitalmenteTextoPadrao"));
		if((Integer)dadosSQL.getInt("documentoassinado.codigo") != null && (Integer)dadosSQL.getInt("documentoassinado.codigo") > 0){
			obj.getImpressaoContrato().getDocumentoAssinado().setCodigo(new Integer(dadosSQL.getInt("documentoassinado.codigo")));
			obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo.codigo")));
			obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().setNome(dadosSQL.getString("arquivo.nome"));
			obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().setDescricao(dadosSQL.getString("arquivo.descricao"));
			obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().setDescricaoAntesAlteracao(dadosSQL.getString("arquivo.descricao"));
			obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));		
			obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().setSituacao(dadosSQL.getString("arquivo.situacao"));	
		}
		
		return obj;
	}
}
