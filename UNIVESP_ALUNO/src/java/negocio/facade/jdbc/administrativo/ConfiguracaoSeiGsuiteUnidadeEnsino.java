package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.administrativo.enumeradores.FormaGeracaoEventoAulaOnLineGoogleMeetEnum;
import negocio.comuns.administrativo.enumeradores.TipoGeracaoEmailIntegracaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoSeiGsuiteUnidadeEnsino extends ControleAcesso implements ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2897150227856503096L;
	private static String idEntidade = "ConfiguracaoSeiGsuite";

	public static String getIdEntidade() {
		return ConfiguracaoSeiGsuiteUnidadeEnsino.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoSeiGsuiteUnidadeEnsino.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_unidadeEnsino"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDominioEmail()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_dominioEmail"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDominioEmailFuncionario()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_dominioEmailFuncionario"));
		Uteis.checkState(!obj.getDominioEmail().contains("@") || !obj.getDominioEmailFuncionario().contains("@"), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_dominioInvalido"));		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeOrganizacionalAluno()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_unidadeOrganizacionalAluno"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeOrganizacionalFuncionario()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_unidadeOrganizacionalFuncionario"));
	}	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
//			ConfiguracaoSeiGsuite.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "configuracaoSeiGsuiteUnidadeEnsino", new AtributoPersistencia()
					.add("configuracaoSeiGsuite", obj.getConfiguracaoSeiGsuiteVO())
					.add("unidadeensino", obj.getUnidadeEnsinoVO())
					.add("unidadeOrganizacionalAluno", obj.getUnidadeOrganizacionalAluno())
					.add("unidadeOrganizacionalFuncionario", obj.getUnidadeOrganizacionalFuncionario())
					.add("dominioEmail", obj.getDominioEmail())
					.add("alterarSenhaProximoLogin", obj.isAlterarSenhaProximoLogin())
					.add("formaGeracaoEventoAulaOnLineGoogleMeet", obj.getFormaGeracaoEventoAulaOnLineGoogleMeet())
					.add("tipoGeracaoEmailGsuiteEnum", obj.getTipoGeracaoEmailGsuiteEnum())
					.add("alterarSenhaProximoLogin", obj.isAlterarSenhaProximoLogin())
					.add("dominioEmailFuncionario", obj.getDominioEmailFuncionario()) 
					.add("eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada", obj.getEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada())
					.add("notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada", obj.getNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
//			ConfiguracaoSeiGsuite.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "configuracaoSeiGsuiteUnidadeEnsino", new AtributoPersistencia()
					.add("configuracaoSeiGsuite", obj.getConfiguracaoSeiGsuiteVO())
					.add("unidadeensino", obj.getUnidadeEnsinoVO())
					.add("unidadeOrganizacionalAluno", obj.getUnidadeOrganizacionalAluno())
					.add("unidadeOrganizacionalFuncionario", obj.getUnidadeOrganizacionalFuncionario())
					.add("dominioEmail", obj.getDominioEmail()) 
					.add("dominioEmailFuncionario", obj.getDominioEmailFuncionario())
					.add("alterarSenhaProximoLogin", obj.isAlterarSenhaProximoLogin())
					.add("formaGeracaoEventoAulaOnLineGoogleMeet", obj.getFormaGeracaoEventoAulaOnLineGoogleMeet())
					.add("tipoGeracaoEmailGsuiteEnum", obj.getTipoGeracaoEmailGsuiteEnum())
					.add("alterarSenhaProximoLogin", obj.isAlterarSenhaProximoLogin())
					.add("eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada", obj.getEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada()) 
					.add("notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada", obj.getNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada()), 
					new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj : lista) {
			validarDados(obj);
			if (!Uteis.isAtributoPreenchido(obj)) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> consultarPorMatriculaMaiorNivelEducacionalMaiorDataPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception  {
		StringBuilder str = new StringBuilder();
		
		str.append(" SELECT ");
		str.append(" t.dominioemail, t.dominioemailfuncionario, ");
		str.append(" t.unidadeorganizacionalaluno , t.unidadeorganizacionalfuncionario ,");
		str.append(" t.alterarSenhaProximoLogin, t.tipoGeracaoEmailGsuiteEnum,");
		str.append(" array_to_string(array_agg(t.matricula),',') as listaMatriculaString ");
		str.append(" from (");
		str.append(" SELECT   ");
		str.append(" dominioemail, dominioemailfuncionario,  unidadeorganizacionalaluno , ");
		str.append(" alterarSenhaProximoLogin, tipoGeracaoEmailGsuiteEnum, ");
		str.append(" unidadeorganizacionalfuncionario , matricula.matricula ");
		str.append(" FROM matricula  ");
		str.append(" inner join curso on curso.codigo = matricula.curso  ");
		str.append(" inner join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino = matricula.unidadeensino  ");
		str.append(" where matricula.aluno = ?  ");
		str.append(" and matricula.situacao in ('AT', 'PR') ");
		str.append(" order by ");
		str.append(" matricula.data desc,");
		str.append(" (case when curso.niveleducacional = '").append(TipoNivelEducacional.MESTRADO.getValor()).append("' then  9 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.POS_GRADUACAO.getValor()).append("' then  8 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.SUPERIOR.getValor()).append("' then 7 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor()).append("' then  6 ");
		str.append("       when curso.niveleducacional = '").append(TipoNivelEducacional.MEDIO.getValor()).append("'then 5 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.BASICO.getValor()).append("'then 4 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.INFANTIL.getValor()).append("' then 3 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.EXTENSAO.getValor()).append("' then 2 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.SEQUENCIAL.getValor()).append("' then 1 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.PROFISSIONALIZANTE.getValor()).append("' then  0 else -1 end ) desc ");
		str.append(" ) as t ");
		str.append(" group by ");
		str.append(" t.dominioemail, t.dominioemailfuncionario, ");
		str.append(" t.alterarSenhaProximoLogin, t.tipoGeracaoEmailGsuiteEnum, ");
		str.append(" t.unidadeorganizacionalaluno , t.unidadeorganizacionalfuncionario ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), pessoa);
		List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> lista = new ArrayList<ConfiguracaoSeiGsuiteUnidadeEnsinoVO>();
		while (dadosSQL.next()) {
			ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj = new ConfiguracaoSeiGsuiteUnidadeEnsinoVO();
			obj.setNovoObj(false);
			obj.setUnidadeOrganizacionalAluno(dadosSQL.getString("unidadeOrganizacionalAluno"));
			obj.setUnidadeOrganizacionalFuncionario(dadosSQL.getString("unidadeOrganizacionalFuncionario"));
			obj.setDominioEmail(dadosSQL.getString("dominioEmail"));
			obj.setDominioEmailFuncionario(dadosSQL.getString("dominioEmailFuncionario"));
			obj.setAlterarSenhaProximoLogin(dadosSQL.getBoolean("alterarSenhaProximoLogin"));
			obj.setTipoGeracaoEmailGsuiteEnum(TipoGeracaoEmailIntegracaoEnum.valueOf(dadosSQL.getString("tipoGeracaoEmailGsuiteEnum")));
			obj.setListaMatriculaString(dadosSQL.getString("listaMatriculaString"));
			lista.add(obj);
		}
		return lista;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" csgue.codigo as \"csgue.codigo\", ");
		sql.append(" csgue.configuracaoSeiGsuite as \"csgue.configuracaoSeiGsuite\", ");
		sql.append(" csgue.unidadeOrganizacionalAluno as \"csgue.unidadeOrganizacionalAluno\", ");
		sql.append(" csgue.unidadeOrganizacionalFuncionario as \"csgue.unidadeOrganizacionalFuncionario\", ");
		sql.append(" csgue.dominioEmail as \"csgue.dominioEmail\", ");
		sql.append(" csgue.dominioEmailFuncionario as \"csgue.dominioEmailFuncionario\", ");
		sql.append(" csgue.alterarSenhaProximoLogin as \"csgue.alterarSenhaProximoLogin\", ");
		sql.append(" csgue.formaGeracaoEventoAulaOnLineGoogleMeet, csgue.eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada, csgue.notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada, ");
		sql.append(" csgue.tipoGeracaoEmailGsuiteEnum, ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", ");
		sql.append(" unidadeensino.nome as \"unidadeensino.nome\" ");
		sql.append(" FROM configuracaoSeiGsuiteUnidadeEnsino as csgue ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo =  csgue.unidadeensino ");

		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> consultarPorConfiguracaoSeiGsuiteVO(ConfiguracaoSeiGsuiteVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE csgue.configuracaoSeiGsuite = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			return (montarDadosConsulta(tabelaResultado));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoSeiGsuiteUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE csgue.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( ConfiguracaoSeiGsuiteUnidadeEnsinoVO ).");
			}
			return (montarDados(tabelaResultado));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet tabelaResultado) {
		List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}

	private ConfiguracaoSeiGsuiteUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL) {
		ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj = new ConfiguracaoSeiGsuiteUnidadeEnsinoVO();
		obj.setCodigo((dadosSQL.getInt("csgue.codigo")));
		obj.getConfiguracaoSeiGsuiteVO().setCodigo(dadosSQL.getInt("csgue.configuracaoSeiGsuite"));
		obj.setUnidadeOrganizacionalAluno(dadosSQL.getString("csgue.unidadeOrganizacionalAluno"));
		obj.setUnidadeOrganizacionalFuncionario(dadosSQL.getString("csgue.unidadeOrganizacionalFuncionario"));
		obj.setDominioEmail(dadosSQL.getString("csgue.dominioEmail"));
		obj.setDominioEmailFuncionario(dadosSQL.getString("csgue.dominioEmailFuncionario"));
		obj.setAlterarSenhaProximoLogin(dadosSQL.getBoolean("csgue.alterarSenhaProximoLogin"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.setTipoGeracaoEmailGsuiteEnum(TipoGeracaoEmailIntegracaoEnum.valueOf(dadosSQL.getString("tipoGeracaoEmailGsuiteEnum")));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("formaGeracaoEventoAulaOnLineGoogleMeet"))) {
			obj.setFormaGeracaoEventoAulaOnLineGoogleMeet(FormaGeracaoEventoAulaOnLineGoogleMeetEnum.getEnumPorValor(dadosSQL.getString("formaGeracaoEventoAulaOnLineGoogleMeet")));			
		}
		obj.setEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada(dadosSQL.getInt("eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada"));
		obj.setNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada(dadosSQL.getInt("notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada"));
		return obj;
	}

}
