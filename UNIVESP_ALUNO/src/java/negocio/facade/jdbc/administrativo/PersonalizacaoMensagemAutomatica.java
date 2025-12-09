/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.facade.jdbc.administrativo;

/**
 * 
 * @author Mauro
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.ModuloTemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.PersonalizacaoMensagemAutomaticaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PersonalizacaoMensagemAutomatica extends ControleAcesso implements PersonalizacaoMensagemAutomaticaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public PersonalizacaoMensagemAutomatica() {
		super();
		setIdEntidade("PersonalizacaoMensagemAutomatica");
	}

	@Override
	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.TODOS)) {
			sqlStr = "SELECT * FROM personalizacaoMensagemAutomatica WHERE curso is null";
		} else {
			sqlStr = "SELECT * FROM personalizacaoMensagemAutomatica WHERE templateMensagemAutomaticaEnum  = '" + templateMensagemAutomaticaEnum.name() + "' and curso is null limit 1";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return null;
	}

	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, Integer codigoCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.TODOS)) {
			sqlStr = "SELECT * FROM personalizacaoMensagemAutomatica";
		} else {
			sqlStr = "SELECT * FROM personalizacaoMensagemAutomatica WHERE templateMensagemAutomaticaEnum  = '" + templateMensagemAutomaticaEnum.name() + "' and curso = " + codigoCurso + " limit 1";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return this.consultarPorNomeTemplate(templateMensagemAutomaticaEnum, controlarAcesso, usuario);
	}
	
	@Override
	public List<PersonalizacaoMensagemAutomaticaVO> consultarPorParamentrosTemplate(String assunto, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, ModuloTemplateMensagemAutomaticaEnum moduloTemplateMensagemAutomaticaEnum, String nomeCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		if (templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.TODOS)) {
			sqlStr.append("SELECT personalizacaoMensagemAutomatica.* FROM personalizacaoMensagemAutomatica");
			sqlStr.append(" left join curso on curso.codigo = personalizacaomensagemautomatica.curso ");
			sqlStr.append(" WHERE 1=1 ");
			if (moduloTemplateMensagemAutomaticaEnum != null && !moduloTemplateMensagemAutomaticaEnum.equals(ModuloTemplateMensagemAutomaticaEnum.TODOS)) {
				sqlStr.append("  AND templateMensagemAutomaticaEnum  in('TODOS' ");
				for (TemplateMensagemAutomaticaEnum templateMensagemAutomatica : TemplateMensagemAutomaticaEnum.values()) {
					if (templateMensagemAutomatica.getModuloTemplateMensagemAutomatica() != null && templateMensagemAutomatica.getModuloTemplateMensagemAutomatica().equals(moduloTemplateMensagemAutomaticaEnum)) {
						sqlStr.append(", '").append(templateMensagemAutomatica.toString()).append("' ");
					}
				}
				sqlStr.append(" ) ");
			}
		} else {
			sqlStr.append("SELECT personalizacaoMensagemAutomatica.* FROM personalizacaoMensagemAutomatica ");
			sqlStr.append(" left join curso on curso.codigo = personalizacaomensagemautomatica.curso ");
			sqlStr.append(" WHERE templateMensagemAutomaticaEnum  = '" + templateMensagemAutomaticaEnum + "' ");
		}
		List<Object> filtros =  new ArrayList<Object>(0);
		if (nomeCurso != null && !nomeCurso.trim().equals("")) {
			sqlStr.append(" and sem_acentos(curso.nome) ilike sem_acentos(?) ");
			filtros.add(PERCENT+ nomeCurso+PERCENT);
		}
		if (assunto != null && !assunto.trim().equals("")) {
			sqlStr.append(" and sem_acentos(assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+ assunto+PERCENT);
		}
		sqlStr.append(" order by templateMensagemAutomaticaEnum ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public  List<PersonalizacaoMensagemAutomaticaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<PersonalizacaoMensagemAutomaticaVO> vetResultado = new ArrayList<PersonalizacaoMensagemAutomaticaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public  PersonalizacaoMensagemAutomaticaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO obj = new PersonalizacaoMensagemAutomaticaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.valueOf(dadosSQL.getString("templateMensagemAutomaticaEnum")));
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setTags(dadosSQL.getString("tags"));
		obj.setMensagem(dadosSQL.getString("mensagem"));
		obj.setDesabilitarEnvioMensagemAutomatica(dadosSQL.getBoolean("desabilitarEnvioMensagemAutomatica"));
		obj.setMensagemSMS(dadosSQL.getString("mensagemsms"));
		obj.setDesabilitarEnvioMensagemSMSAutomatica(dadosSQL.getBoolean("desabilitarEnvioMensagemSMSAutomatica"));
		obj.setEnviarCopiaPais(dadosSQL.getBoolean("enviarCopiaPais"));
		obj.setCopiaFollowUp(dadosSQL.getBoolean("copiafollowup"));
		obj.setNivelEducacionalInfantil(dadosSQL.getBoolean("nivelEducacionalInfantil"));
		obj.setNivelEducacionalBasico(dadosSQL.getBoolean("nivelEducacionalBasico"));
		obj.setNivelEducacionalMedio(dadosSQL.getBoolean("nivelEducacionalMedio"));
		obj.setNivelEducacionalExtensao(dadosSQL.getBoolean("nivelEducacionalExtensao"));
		obj.setNivelEducacionalSequencial(dadosSQL.getBoolean("nivelEducacionalSequencial"));
		obj.setNivelEducacionalGraduacaoTecnologica(dadosSQL.getBoolean("nivelEducacionalGraduacaoTecnologica"));
		obj.setNivelEducacionalSuperior(dadosSQL.getBoolean("nivelEducacionalSuperior"));
		obj.setNivelEducacionalPosGraduacao(dadosSQL.getBoolean("nivelEducacionalPosGraduacao"));
		obj.setNivelEducacionalMestrado(dadosSQL.getBoolean("nivelEducacionalMestrado"));
		obj.setNivelEducacionalProfissionalizante(dadosSQL.getBoolean("nivelEducacionalProfissionalizante"));
		obj.getUsuarioUltimaAlteracao().setCodigo(dadosSQL.getInt("usuarioUltimaAlteracao"));
		obj.setEnviarEmailInstitucional(dadosSQL.getBoolean("enviaremailinstitucional"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
		if (obj.getCursoVO().getCodigo() != 0) {
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
		}
		if (Uteis.isAtributoPreenchido(obj.getUsuarioUltimaAlteracao())) {
			obj.setUsuarioUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioUltimaAlteracao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		obj.setDataUltimaAlteracao(dadosSQL.getTimestamp("dataUltimaAlteracao"));
		obj.setEnviarEmail(dadosSQL.getBoolean("enviarEmail"));
		obj.setTipoRequerimento(dadosSQL.getInt("tipoRequerimento"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			obj.setNivelMontarDados(NivelMontarDados.BASICO);
            return obj;
        }
        obj.setPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs(getFacadeFactory().getPersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade().consultarPersonalizacaoMensagemAutomaticaUnidadeEnsinoPorPersonalizacaoMensagemAutomatica(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        obj.setNivelMontarDados(NivelMontarDados.TODOS);
        
		return obj;
	}

	public static String getIdEntidade() {
		return PersonalizacaoMensagemAutomatica.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		PersonalizacaoMensagemAutomatica.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluir(final PersonalizacaoMensagemAutomaticaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO personalizacaoMensagemAutomatica(assunto, tags, mensagem, desabilitarEnvioMensagemAutomatica, templateMensagemAutomaticaEnum, mensagemsms, desabilitarEnvioMensagemSMSAutomatica, enviarCopiaPais, copiafollowup, nivelEducacionalInfantil, nivelEducacionalBasico, nivelEducacionalMedio, nivelEducacionalExtensao, nivelEducacionalSequencial, nivelEducacionalGraduacaoTecnologica, nivelEducacionalSuperior, nivelEducacionalPosGraduacao, nivelEducacionalMestrado, nivelEducacionalProfissionalizante, usuarioUltimaAlteracao, dataUltimaAlteracao, enviarEmailInstitucional, curso, enviarEmail, tipoRequerimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getAssunto());
					sqlInserir.setString(2, obj.getTags());
					sqlInserir.setString(3, obj.getMensagem());
					sqlInserir.setBoolean(4, obj.getDesabilitarEnvioMensagemAutomatica());
					sqlInserir.setString(5, obj.getTemplateMensagemAutomaticaEnum().toString());
					sqlInserir.setString(6, obj.getMensagemSMS());
					sqlInserir.setBoolean(7, obj.getDesabilitarEnvioMensagemSMSAutomatica());
					sqlInserir.setBoolean(8, obj.getEnviarCopiaPais());
					sqlInserir.setBoolean(9, obj.getCopiaFollowUp());
					sqlInserir.setBoolean(10, obj.getNivelEducacionalInfantil());
					sqlInserir.setBoolean(11, obj.getNivelEducacionalBasico());
					sqlInserir.setBoolean(12, obj.getNivelEducacionalMedio());
					sqlInserir.setBoolean(13, obj.getNivelEducacionalExtensao());
					sqlInserir.setBoolean(14, obj.getNivelEducacionalSequencial());
					sqlInserir.setBoolean(15, obj.getNivelEducacionalGraduacaoTecnologica());
					sqlInserir.setBoolean(16, obj.getNivelEducacionalSuperior());
					sqlInserir.setBoolean(17, obj.getNivelEducacionalPosGraduacao());
					sqlInserir.setBoolean(18, obj.getNivelEducacionalMestrado());
					sqlInserir.setBoolean(19, obj.getNivelEducacionalProfissionalizante());
					if (obj.getUsuarioUltimaAlteracao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(20, obj.getUsuarioUltimaAlteracao().getCodigo());
					} else {
						sqlInserir.setNull(20, 0);
					}
					sqlInserir.setTimestamp(21, Uteis.getDataJDBCTimestamp(obj.getDataUltimaAlteracao()));
					sqlInserir.setBoolean(22, obj.getEnviarEmailInstitucional());
					if (obj.getCursoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(23, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}
					sqlInserir.setBoolean(24, obj.getEnviarEmail());
					if (Uteis.isAtributoPreenchido(obj.getTipoRequerimento())) {
						sqlInserir.setInt(25, obj.getTipoRequerimento());
					} else {
						sqlInserir.setNull(25, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			
			 getFacadeFactory().getPersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade().incluirPersonalizacaoMensagemAutomaticaUnidadeEnsino(obj.getCodigo(), obj.getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs());
			
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterar(final PersonalizacaoMensagemAutomaticaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE personalizacaoMensagemAutomatica set assunto=?, tags=?, mensagem=? , desabilitarEnvioMensagemAutomatica=?, templateMensagemAutomaticaEnum=?, mensagemsms=?, desabilitarEnvioMensagemSMSAutomatica=?, enviarCopiaPais=?, copiafollowup=?, nivelEducacionalInfantil=?, nivelEducacionalBasico=?, nivelEducacionalMedio=?, nivelEducacionalExtensao=?, nivelEducacionalSequencial=?, nivelEducacionalGraduacaoTecnologica=?, nivelEducacionalSuperior=?, nivelEducacionalPosGraduacao=?, nivelEducacionalMestrado=?, nivelEducacionalProfissionalizante=?, usuarioUltimaAlteracao=?, dataUltimaAlteracao=?, enviaremailinstitucional=?, curso=?, enviarEmail=?, tipoRequerimento=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getAssunto());
					sqlAlterar.setString(2, obj.getTags());
					sqlAlterar.setString(3, obj.getMensagem());
					sqlAlterar.setBoolean(4, obj.getDesabilitarEnvioMensagemAutomatica());
					sqlAlterar.setString(5, obj.getTemplateMensagemAutomaticaEnum().toString());
					sqlAlterar.setString(6, obj.getMensagemSMS());
					sqlAlterar.setBoolean(7, obj.getDesabilitarEnvioMensagemSMSAutomatica());
					sqlAlterar.setBoolean(8, obj.getEnviarCopiaPais());
					sqlAlterar.setBoolean(9, obj.getCopiaFollowUp());
					sqlAlterar.setBoolean(10, obj.getNivelEducacionalInfantil());
					sqlAlterar.setBoolean(11, obj.getNivelEducacionalBasico());
					sqlAlterar.setBoolean(12, obj.getNivelEducacionalMedio());
					sqlAlterar.setBoolean(13, obj.getNivelEducacionalExtensao());
					sqlAlterar.setBoolean(14, obj.getNivelEducacionalSequencial());
					sqlAlterar.setBoolean(15, obj.getNivelEducacionalGraduacaoTecnologica());
					sqlAlterar.setBoolean(16, obj.getNivelEducacionalSuperior());
					sqlAlterar.setBoolean(17, obj.getNivelEducacionalPosGraduacao());
					sqlAlterar.setBoolean(18, obj.getNivelEducacionalMestrado());
					sqlAlterar.setBoolean(19, obj.getNivelEducacionalProfissionalizante());
					if (obj.getUsuarioUltimaAlteracao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(20, obj.getUsuarioUltimaAlteracao().getCodigo());
					} else {
						sqlAlterar.setNull(20, 0);
					}
					sqlAlterar.setTimestamp(21, Uteis.getDataJDBCTimestamp(obj.getDataUltimaAlteracao()));
					sqlAlterar.setBoolean(22, obj.getEnviarEmailInstitucional());
					if (obj.getCursoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(23, obj.getCursoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(23, 0);
					}
					sqlAlterar.setBoolean(24, obj.getEnviarEmail());
					if (Uteis.isAtributoPreenchido(obj.getTipoRequerimento())) {
						sqlAlterar.setInt(25, obj.getTipoRequerimento());
					} else {
						sqlAlterar.setNull(25, 0);
					}
					sqlAlterar.setInt(26, obj.getCodigo());
					return sqlAlterar;
				}
			});
			
			getFacadeFactory().getPersonalizacaoMensagemAutomaticaUnidadeEnsinoFacade().alterarPersonalizacaoMensagemAutomaticaUnidadeEnsino(obj.getCodigo(), obj.getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs());
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCursoTampleteMensagemAutomatica(PersonalizacaoMensagemAutomaticaVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM personalizacaoMensagemAutomatica WHERE curso = ? and templatemensagemautomaticaenum = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCursoVO().getCodigo(), obj.getTemplateMensagemAutomaticaEnum().name() });
		} catch (Exception e) {
			throw e;
		}
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void executarGeracaoMensagemPadrao(Boolean controlarAcesso, UsuarioVO usuario) throws Exception {		
		for (TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum : TemplateMensagemAutomaticaEnum.values()) {
			executarGeracaoMensagemPadraoTemplateEspecifico(templateMensagemAutomaticaEnum, controlarAcesso, usuario);			
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public PersonalizacaoMensagemAutomaticaVO executarGeracaoMensagemPadraoTemplateEspecifico(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			PersonalizacaoMensagemAutomaticaVO mensagemAuto = null;		
			if(templateMensagemAutomaticaEnum != null){
			mensagemAuto = consultarPorNomeTemplate(templateMensagemAutomaticaEnum, controlarAcesso, usuario);
			if (mensagemAuto == null && !templateMensagemAutomaticaEnum.equals(TemplateMensagemAutomaticaEnum.TODOS)) {
				mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
				mensagemAuto.setTemplateMensagemAutomaticaEnum(templateMensagemAutomaticaEnum);
				mensagemAuto.setTags(templateMensagemAutomaticaEnum.getTags_Apresentar());
				mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + templateMensagemAutomaticaEnum.toString()));
				mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + templateMensagemAutomaticaEnum.name()));
				mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
				mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + templateMensagemAutomaticaEnum.name() + "_SMS"));
				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
				mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
				incluir(mensagemAuto, usuario);
			}
			}
		return mensagemAuto;
	}
	
	private StringBuilder getSQLPadraoConsultaTemplateMensagemAutomatica(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, List<Integer> unidadeEnsinos, Integer curso, Integer tipoRequerimento) {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT unidadeensino.codigo unidadeensino, case when coalesce(unidadeensino.caminhobaselogoemailcima) <> '' and coalesce(unidadeensino.nomearquivologoemailcima) <> '' then ")
				.append(" configuracaogeralsistema.localuploadarquivofixo || '/' || replace(unidadeensino.caminhobaselogoemailcima, '\\', '/' ) || '/' || unidadeensino.nomearquivologoemailcima else '' end as cima_sei, ")
				.append(" case when coalesce(unidadeensino.caminhobaselogoemailbaixo) <> '' and coalesce(unidadeensino.nomearquivologoemailbaixo) <> '' then ")
				.append(" configuracaogeralsistema.localuploadarquivofixo || '/' || replace(unidadeensino.caminhobaselogoemailbaixo, '\\', '/') || '/' || unidadeensino.nomearquivologoemailbaixo else '' end as baixo_sei, CASE ")
				.append(" WHEN COALESCE(configuracaogeralsistema.urlexternodownloadarquivo, '') <> '' ")
				.append(" AND COALESCE(unidadeensino.caminhobaselogoemailcima, '') <> '' ")
				.append(" AND COALESCE(unidadeensino.caminhobaselogoemailbaixo, '') <> '' THEN REPLACE(REPLACE(personalizacaoMensagemAutomatica.mensagem, ")
				.append("  '../resources/imagens/email/cima_sei.jpg', configuracaogeralsistema.urlexternodownloadarquivo || '/' || replace(unidadeensino.caminhobaselogoemailcima, '\\', '/' ) ")
				.append(" || '/' || unidadeensino.nomearquivologoemailcima), '../resources/imagens/email/baixo_sei.jpg', ")
				.append(" configuracaogeralsistema.urlexternodownloadarquivo || '/' || replace(unidadeensino.caminhobaselogoemailbaixo, '\\', '/') || '/' || unidadeensino.nomearquivologoemailbaixo) ")
				.append(" ELSE personalizacaoMensagemAutomatica.mensagem END AS mensagem, ")
				.append(" personalizacaoMensagemAutomatica.codigo, personalizacaoMensagemAutomatica.templateMensagemAutomaticaEnum, personalizacaoMensagemAutomatica.assunto, ")
				.append(" personalizacaoMensagemAutomatica.tags, personalizacaoMensagemAutomatica.desabilitarEnvioMensagemAutomatica, personalizacaoMensagemAutomatica.mensagemsms, ")
				.append(" personalizacaoMensagemAutomatica.desabilitarEnvioMensagemSMSAutomatica, personalizacaoMensagemAutomatica.enviarCopiaPais, ")
				.append(" personalizacaoMensagemAutomatica.nivelEducacionalInfantil, personalizacaoMensagemAutomatica.nivelEducacionalBasico, ")
				.append(" personalizacaoMensagemAutomatica.nivelEducacionalMedio, personalizacaoMensagemAutomatica.nivelEducacionalExtensao, ")
				.append(" personalizacaoMensagemAutomatica.nivelEducacionalSequencial, personalizacaoMensagemAutomatica.nivelEducacionalGraduacaoTecnologica, ")
				.append(" personalizacaoMensagemAutomatica.nivelEducacionalSuperior, personalizacaoMensagemAutomatica.nivelEducacionalPosGraduacao, ")
				.append(" personalizacaoMensagemAutomatica.nivelEducacionalMestrado, personalizacaoMensagemAutomatica.nivelEducacionalProfissionalizante, personalizacaoMensagemAutomatica.curso, ")
				.append(" personalizacaoMensagemAutomatica.copiafollowup, personalizacaoMensagemAutomatica.usuarioultimaalteracao, personalizacaoMensagemAutomatica.dataultimaalteracao, personalizacaoMensagemAutomatica.enviaremailinstitucional, personalizacaoMensagemAutomatica.enviaremail, personalizacaoMensagemAutomatica.tipoRequerimento FROM personalizacaoMensagemAutomatica ")
				.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo IN ")
				.append(Uteis.isAtributoPreenchido(unidadeEnsinos) ? unidadeEnsinos.stream().distinct().map(String::valueOf).collect(Collectors.joining(", ", " (", ")")) : "(0)")
				.append(" LEFT JOIN configuracoes ON configuracoes.padrao ")
				.append(" LEFT JOIN configuracaogeralsistema ON configuracoes.codigo = configuracaogeralsistema.configuracoes ")
				.append(" WHERE templateMensagemAutomaticaEnum = '")
				.append(templateMensagemAutomaticaEnum.name())
				.append("' ");
				if (Uteis.isAtributoPreenchido(curso)) {
					sql.append(" AND personalizacaoMensagemAutomatica.curso = ").append(curso);
				} else {
					sql.append(" AND personalizacaoMensagemAutomatica.curso is null ");
				}
				if (Uteis.isAtributoPreenchido(tipoRequerimento)) {
					sql.append(" AND personalizacaoMensagemAutomatica.tiporequerimento = ").append(tipoRequerimento);
				} else {
					sql.append(" AND personalizacaoMensagemAutomatica.tiporequerimento is null ");
				}
				
		return sql;
	}
	
	private PersonalizacaoMensagemAutomaticaVO montarDadosImagensUnidadeEnsino(UsuarioVO usuario, int nivelMontarDados, SqlRowSet tabelaResultado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO montarDados = montarDados(tabelaResultado, nivelMontarDados, usuario);
		montarDados.setCaminhoImagemPadraoCima(tabelaResultado.getString("cima_sei"));
		montarDados.setCaminhoImagemPadraoBaixo(tabelaResultado.getString("baixo_sei"));
		return montarDados;
	}
	
	@Override	
	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso,  Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		return consultarPorNomeTemplate(templateMensagemAutomaticaEnum, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsino, usuarioVO, null);
	}
	
	@Override
	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuarioVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = getSQLPadraoConsultaTemplateMensagemAutomatica(templateMensagemAutomaticaEnum, Uteis.isAtributoPreenchido(unidadeEnsinoVOs) ? unidadeEnsinoVOs.stream().map(UnidadeEnsinoVO::getCodigo).collect(Collectors.toList()) : Arrays.asList(unidadeEnsino), 0, 0).append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDadosImagensUnidadeEnsino(usuarioVO, nivelMontarDados, tabelaResultado);
		}
		return null;
	}
	
	@Override
	public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplateCurso(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = getSQLPadraoConsultaTemplateMensagemAutomatica(templateMensagemAutomaticaEnum, Arrays.asList(unidadeEnsino), curso, 0).append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDadosImagensUnidadeEnsino(usuarioVO, nivelMontarDados, tabelaResultado);
		}
		return null;
	}
	
	@Override
	public Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, List<Integer> unidadeEnsinos, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = getSQLPadraoConsultaTemplateMensagemAutomatica(templateMensagemAutomaticaEnum, unidadeEnsinos, 0, 0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Map<Integer, PersonalizacaoMensagemAutomaticaVO> map = new HashMap<>();
		while (tabelaResultado.next()) {
			map.put(tabelaResultado.getInt("unidadeensino"), montarDadosImagensUnidadeEnsino(usuarioVO, nivelMontarDados, tabelaResultado));
		}
		return map;
	}
	
	   public PersonalizacaoMensagemAutomaticaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	        consultar(getIdEntidade(), false, usuario);
	        String sql = "SELECT * FROM personalizacaoMensagemAutomatica WHERE codigo = ?";
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
	        if (!tabelaResultado.next()) {
	            throw new ConsistirException("Dados Não Encontrados ( CondicaoRenegociacao ).");
	        }
	        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	    }

    @Override
    public void inicializarDadosUnidadeEnsinoSelecionadaEdicao(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO) {
    	HashMap<Integer, PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> mapPesonalizacaoMensagemAutomaticaEnsinoVOs = new HashMap<Integer, PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO>(0);
    	for (PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO personalizacaoMensagemAutomaticaUnidadeEnsinoEdicaoVO : personalizacaoMensagemAutomaticaVO.getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs()) {
    		mapPesonalizacaoMensagemAutomaticaEnsinoVOs.put(personalizacaoMensagemAutomaticaUnidadeEnsinoEdicaoVO.getUnidadeEnsino().getCodigo(), personalizacaoMensagemAutomaticaUnidadeEnsinoEdicaoVO);
    	}
    	personalizacaoMensagemAutomaticaVO.getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs().clear();
    	for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
    		PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO personalizacaoMensagemAutomaticaUnidadeEnsinoVO = new PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO();
			if (mapPesonalizacaoMensagemAutomaticaEnsinoVOs.containsKey(unidadeEnsinoVO.getCodigo())) {
				personalizacaoMensagemAutomaticaUnidadeEnsinoVO = mapPesonalizacaoMensagemAutomaticaEnsinoVOs.get(unidadeEnsinoVO.getCodigo());
			} else {
				personalizacaoMensagemAutomaticaUnidadeEnsinoVO.getUnidadeEnsino().setFiltrarUnidadeEnsino(false);
				personalizacaoMensagemAutomaticaUnidadeEnsinoVO.getUnidadeEnsino().setCodigo(unidadeEnsinoVO.getCodigo());
				personalizacaoMensagemAutomaticaUnidadeEnsinoVO.getUnidadeEnsino().setNome(unidadeEnsinoVO.getNome());
			}
			
			personalizacaoMensagemAutomaticaVO.getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs().add(personalizacaoMensagemAutomaticaUnidadeEnsinoVO);
		}
		
    }
	
    @Override
    public PersonalizacaoMensagemAutomaticaVO consultarMensagemAutomaticaDeferimentoRequerimento(Integer codigoTipoRequerimento, UsuarioVO usuario) throws Exception {
		String sql = "SELECT * FROM personalizacaomensagemautomatica WHERE templatemensagemautomaticaenum = 'MENSAGEM_REQUERIMENTO_DEFERIDO' AND tiporequerimento = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoTipoRequerimento);
    	if (!tabelaResultado.next()) {
    		return new PersonalizacaoMensagemAutomaticaVO();
    	}
    	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
    }
    
    @Override
    public PersonalizacaoMensagemAutomaticaVO consultarMensagemAutomaticaIndeferimentoRequerimento(Integer codigoTipoRequerimento, UsuarioVO usuario) throws Exception {
    	String sql = "SELECT * FROM personalizacaomensagemautomatica WHERE templatemensagemautomaticaenum = 'MENSAGEM_REQUERIMENTO_INDEFERIDO' AND tiporequerimento = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoTipoRequerimento);
    	if (!tabelaResultado.next()) {
    		return new PersonalizacaoMensagemAutomaticaVO();
    	}
    	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomatica, UsuarioVO usuario) throws Exception {
    	if (Uteis.isAtributoPreenchido(personalizacaoMensagemAutomatica)) {
    		alterar(personalizacaoMensagemAutomatica, usuario);
    	} else {
    		incluir(personalizacaoMensagemAutomatica, usuario);
    	}
    }
    
    @Override
    public PersonalizacaoMensagemAutomaticaVO consultarPorNomeTemplatePorTipoRequerimento(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, Integer tipoRequerimento, UsuarioVO usuarioVO) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = getSQLPadraoConsultaTemplateMensagemAutomatica(templateMensagemAutomaticaEnum, new ArrayList<>(Arrays.asList(unidadeEnsino)), 0, tipoRequerimento).append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDadosImagensUnidadeEnsino(usuarioVO, nivelMontarDados, tabelaResultado);
		}
		return null;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorTipoRequerimento(Integer tipoRequerimento, UsuarioVO usuario) {
    	if (Uteis.isAtributoPreenchido(tipoRequerimento)) {
    		String delete = "DELETE FROM personalizacaomensagemautomatica WHERE tiporequerimento = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    		getConexao().getJdbcTemplate().update(delete, tipoRequerimento);
    	}
    }
}
