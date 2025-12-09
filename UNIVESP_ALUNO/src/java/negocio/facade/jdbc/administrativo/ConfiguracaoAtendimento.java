package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.administrativo.ConfiguracaoAtendimentoFuncionarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.interfaces.administrativo.ConfiguracaoAtendimentoInterfaceFacade;

/**
 * 
 * @author Pedro
 */

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoAtendimento extends ControleAcesso implements ConfiguracaoAtendimentoInterfaceFacade {

	protected static String idEntidade;

	public ConfiguracaoAtendimento() throws Exception {
		super();
		setIdEntidade("ConfiguracaoAtendimento");

	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ConfiguracaoAtendimentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAtendimentoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoAtendimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			executarValidacaoSeExisteUnidadeEnsinoParaOutrasConfiguracaoAtendimento(obj, true);
			incluir(getIdEntidade(), true, usuario);
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoAtendimento( nome, responsavelCadastro, dataCadastro, numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa,  ");
			sql.append(" tempoMaximoParaResponderCadaInteracaoEntreDepartamentos, tempoMaximoParaRespostaOuvidoriaPeloOuvidor,  grupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo, ");
			sql.append(" grupoDestinatarioQuandoOuvidoriaForMalAvaliada, textoParaOrientacaoOuvidoria ) ");			
			sql.append(" VALUES ( ?, ? , ?, ?, ?, ? , ?, ?, ? ) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getNome());					
					sqlInserir.setInt(2, obj.getResponsavelCadastro().getCodigo());
					sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlInserir.setInt(4, obj.getNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa());
					sqlInserir.setInt(5, obj.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos());
					sqlInserir.setInt(6, obj.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor());
					if( obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo().getCodigo().intValue() != 0){
						sqlInserir.setInt(7, obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo().getCodigo());	
					}else{
						sqlInserir.setNull(7, 0);
					}
					if( obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada().getCodigo().intValue() != 0){
						sqlInserir.setInt(8, obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada().getCodigo());	
					}else{
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setString(9, obj.getTextoParaOrientacaoOuvidoria());					
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
			getFacadeFactory().getConfiguracaoAtendimentoFuncionarioFacade().incluirConfiguracaoAtendimentoFuncionarioVO(obj.getCodigo(), obj.getListaConfiguracaoAtendimentoFuncionarioVOs(), usuario);
			getFacadeFactory().getConfiguracaoAtendimentoUnidadeEnsinoFacade().incluirConfiguracaoAtendimentoUnidadeEnsinoVO(obj.getCodigo(), obj.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ConfiguracaoAtendimentoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAtendimentoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoAtendimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			executarValidacaoSeExisteUnidadeEnsinoParaOutrasConfiguracaoAtendimento(obj, false);
			alterar(getIdEntidade(), true, usuario);
			realizarUpperCaseDados(obj);			
			final StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoAtendimento set ");
			sql.append(" nome=?, responsavelCadastro=?, dataCadastro=?, numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa=?,  ");
			sql.append(" tempoMaximoParaResponderCadaInteracaoEntreDepartamentos=?, tempoMaximoParaRespostaOuvidoriaPeloOuvidor=?,  grupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo=?, ");
			sql.append(" grupoDestinatarioQuandoOuvidoriaForMalAvaliada=?, textoParaOrientacaoOuvidoria=? ");			
			sql.append("  WHERE ((codigo = ?))");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getNome());					
					sqlAlterar.setInt(2, obj.getResponsavelCadastro().getCodigo());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlAlterar.setInt(4, obj.getNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa());
					sqlAlterar.setInt(5, obj.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos());
					sqlAlterar.setInt(6, obj.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor());					
					if( obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo().getCodigo().intValue() != 0){
						sqlAlterar.setInt(7, obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo().getCodigo());	
					}else{
						sqlAlterar.setNull(7, 0);
					}
					if( obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada().getCodigo().intValue() != 0){
						sqlAlterar.setInt(8, obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada().getCodigo());	
					}else{
						sqlAlterar.setNull(8, 0);
					}
					sqlAlterar.setString(9, obj.getTextoParaOrientacaoOuvidoria());			
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getConfiguracaoAtendimentoFuncionarioFacade().alterarConfiguracaoAtendimentoFuncionarioVO(obj.getCodigo(), obj.getListaConfiguracaoAtendimentoFuncionarioVOs(), usuario);
			getFacadeFactory().getConfiguracaoAtendimentoUnidadeEnsinoFacade().alterarConfiguracaoAtendimentoUnidadeEnsinoVO(obj.getCodigo(), obj.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#excluir(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoAtendimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM ConfiguracaoAtendimento WHERE ((codigo = ?))";
			getFacadeFactory().getConfiguracaoAtendimentoFuncionarioFacade().excluirConfiguracaoAtendimentoFuncionarioVO(obj.getCodigo(), usuario);
			getFacadeFactory().getConfiguracaoAtendimentoUnidadeEnsinoFacade().excluirConfiguracaoAtendimentoUnidadeEnsinoVO(obj.getCodigo(), usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#persistir(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoAtendimentoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuario);
		} else {
			alterar(obj, usuario);
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#validarDados(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO)
	 */
	@Override
	public void validarDados(ConfiguracaoAtendimentoVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getNome() == null) || (obj.getNome().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAtendimento_nome"));
		}
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>ConfiguracaoAtendimentoVO</code>.
	 */
	public void validarUnicidade(List<ConfiguracaoAtendimentoVO> lista, ConfiguracaoAtendimentoVO obj) throws ConsistirException {
		for (ConfiguracaoAtendimentoVO repetido : lista) {
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(ConfiguracaoAtendimentoVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {			
			return;
		}
		obj.setNome(obj.getNome().toUpperCase());
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#consultar(java.lang.String, java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List<ConfiguracaoAtendimentoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (campoConsulta.equals("CODIGO")) {
//			if (valorConsulta.trim().equals("")) {
//				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
//			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return consultarPorCodigo(valorInt, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("NOME")) {
//			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
//				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
//			}
			return consultarPorNome(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("UNIDADE_ENSINO")) {
//			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
//				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
//			}
			return consultarPorUnidadeEnsino(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
		}
//		if (campoConsulta.equals("DATA_CADASTRO")) {
//			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
//				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
//			}
//			return consultarPorNome(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
//		}
		return new ArrayList(0);
	}
	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#consultarPorNome(java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT distinct ConfiguracaoAtendimento.* FROM ConfiguracaoAtendimento ");
		sqlSb.append(" inner join configuracaoatendimentounidadeensino on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo ");
		sqlSb.append(" inner join unidadeensino on configuracaoatendimentounidadeensino.unidadeensino = unidadeensino.codigo ");
		sqlSb.append(" WHERE sem_acentos(unidadeensino.nome) ilike (sem_acentos(?))");
		sqlSb.append(" ORDER BY ConfiguracaoAtendimento.nome");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), valorConsulta), nivelMontarDados, usuario);
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#consultarPorNome(java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT ConfiguracaoAtendimento.* FROM ConfiguracaoAtendimento ");
		sqlSb.append(" WHERE sem_acentos(ConfiguracaoAtendimento.nome) ilike(sem_acentos(?)) ");
		sqlSb.append(" ORDER BY ConfiguracaoAtendimento.nome");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), valorConsulta), nivelMontarDados, usuario);
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoAtendimento WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 */
	public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TitulacaoCursoVO</code>.
	 * 
	 * @return O objeto da classe <code>TitulacaoCursoVO</code> com os dados
	 *         devidamente montados.
	 */
	public  ConfiguracaoAtendimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoAtendimentoVO obj = new ConfiguracaoAtendimentoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));		
		obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.setNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa(dadosSQL.getInt("numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa"));
		obj.setTempoMaximoParaResponderCadaInteracaoEntreDepartamentos(dadosSQL.getInt("tempoMaximoParaResponderCadaInteracaoEntreDepartamentos"));
		obj.setTempoMaximoParaRespostaOuvidoriaPeloOuvidor(dadosSQL.getInt("tempoMaximoParaRespostaOuvidoriaPeloOuvidor"));
		obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo().setCodigo(dadosSQL.getInt("grupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo"));
		obj.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada().setCodigo(dadosSQL.getInt("grupoDestinatarioQuandoOuvidoriaForMalAvaliada"));
		obj.setTextoParaOrientacaoOuvidoria(dadosSQL.getString("textoParaOrientacaoOuvidoria"));
		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.setListaConfiguracaoAtendimentoFuncionarioVOs(getFacadeFactory().getConfiguracaoAtendimentoFuncionarioFacade().consultarPorCodigoConfiguracaoAtendimento(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setListaConfiguracaoAtendimentoUnidadeEnsinoVOs(getFacadeFactory().getConfiguracaoAtendimentoUnidadeEnsinoFacade().consultarPorCodigoConfiguracaoAtendimento(obj.getCodigo(), false, nivelMontarDados, usuario));
		return obj;
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public ConfiguracaoAtendimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ConfiguracaoAtendimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ConfiguracaoAtendimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public ConfiguracaoAtendimentoVO consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (usuario.getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor()) && valorConsulta == 0) {
			return null;
		}
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT ConfiguracaoAtendimento.* FROM ConfiguracaoAtendimento ");
		sqlSb.append(" inner join configuracaoatendimentounidadeensino on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo ");
		sqlSb.append(" inner join unidadeensino on configuracaoatendimentounidadeensino.unidadeensino = unidadeensino.codigo ");
		sqlSb.append(" WHERE unidadeensino.codigo =").append(valorConsulta).append("");
		sqlSb.append(" ORDER BY unidadeensino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString());
		if (!tabelaResultado.next()) {
			if (usuario.getCodigo() == 0) {
				return null;
			} else {
				throw new ConsistirException("Dados Não Encontrados ( ConfiguracaoAtendimento ) para essa unidade de ensino.");
			}
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public void realizarValidacaoSeExisteConfiguracaoAtendimentoValidaPorUnidadeEnsino(Integer codigoUnidadeEnisno, UsuarioVO usuario) throws Exception{
//		if (codigoUnidadeEnisno == 0) {
//			throw new ConsistirException("Não foi encontrado nenhuma unidade de ensino para registar a ouvidoria.");
//		}
		StringBuilder sql = new StringBuilder(" ");		
		sql.append(" select configuracaoatendimento.codigo , count (configuracaoatendimentofuncionario.funcionario) qtd ");
		sql.append(" from configuracaoatendimento ");
		sql.append(" inner join configuracaoatendimentounidadeensino on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo ");		
		sql.append(" left join configuracaoatendimentofuncionario on configuracaoatendimentofuncionario.configuracaoatendimento = configuracaoatendimento.codigo and configuracaoatendimentofuncionario.inativotemporario = false ");		
		sql.append(" where configuracaoatendimentounidadeensino.unidadeEnsino = ?  ");
		sql.append(" group by  configuracaoatendimento.codigo  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoUnidadeEnisno);
		if (!tabelaResultado.next()) {
			if (usuario.getCodigo() == 0) {
				return ;
			} else {
				throw new ConsistirException("Não foi encontrado nenhuma (CONFIGURAÇÃO DE ATENDIMENTO) para essa unidade de ensino. Por favor configure o atendimento e tente novamente.");
			}
		}	
		if(tabelaResultado.getInt("qtd") == 0) {
			throw new ConsistirException("Não foi encontrado nenhum funcionário disponivél para realizar os atendimentos. Por favor configure o atendimento e tente novamente.");
		}		
	}
	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#adicionarConfiguracaoAtendimentoFuncionarioVOs(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO, negocio.comuns.administrativo.ConfiguracaoAtendimentoFuncionarioVO)
	 */
    @Override
	public void adicionarConfiguracaoAtendimentoFuncionarioVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoFuncionarioVO obj) throws Exception {
        getFacadeFactory().getConfiguracaoAtendimentoFuncionarioFacade().validarDados(obj);
        obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionarioVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
        obj.setConfiguracaoAtendimentoVO(configuracaoAtendimentoVO);
        int index = 0;
        for (ConfiguracaoAtendimentoFuncionarioVO objExistente : configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs()) {
            if (objExistente.getFuncionarioVO().getCodigo().equals(obj.getFuncionarioVO().getCodigo())) {                
            	configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs().set(index, obj);
                return;
            }
            index++;
        }        
        configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs().add(obj);
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#excluirConfiguracaoAtendimentoFuncionarioVOs(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO, negocio.comuns.administrativo.ConfiguracaoAtendimentoFuncionarioVO)
	 */
    @Override
	public void excluirConfiguracaoAtendimentoFuncionarioVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoFuncionarioVO obj) throws Exception {
        int index = 0;
        for (ConfiguracaoAtendimentoFuncionarioVO objExistente : configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs()) {
            if (objExistente.getFuncionarioVO().getCodigo().equals(obj.getFuncionarioVO().getCodigo())) {
            	configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs().remove(index);
                return;
            }
            index++;
        }
    }
    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#adicionarConfiguracaoAtendimentoUnidadeEnsinoVOs(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO, negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO)
	 */
    @Override
	public void adicionarConfiguracaoAtendimentoUnidadeEnsinoVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoUnidadeEnsinoVO obj) throws Exception {
    	getFacadeFactory().getConfiguracaoAtendimentoUnidadeEnsinoFacade().validarDados(obj);
    	obj.setConfiguracaoAtendimentoVO(configuracaoAtendimentoVO);
    	int index = 0;
    	for (ConfiguracaoAtendimentoUnidadeEnsinoVO objExistente : configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs()) {
    		if (objExistente.getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())) {                
    			configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs().set(index, obj);
    			return;
    		}
    		index++;
    	}        
    	configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs().add(obj);
    }
    
    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoInterfaceFacade#excluirConfiguracaoAtendimentoUnidadeEnsinoVOs(negocio.comuns.administrativo.ConfiguracaoAtendimentoVO, negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO)
	 */
    @Override
	public void excluirConfiguracaoAtendimentoUnidadeEnsinoVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoUnidadeEnsinoVO obj, UsuarioVO usuarioLogado) throws Exception {
    	excluirConfiguracaoAtendimentoFuncionarioVOPorUnidadeEnsino(configuracaoAtendimentoVO, obj, usuarioLogado);
    	int index = 0;
    	for (ConfiguracaoAtendimentoUnidadeEnsinoVO objExistente : configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs()) {
    		if (objExistente.getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())) {
    			configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs().remove(index);
    			return;
    		}
    		index++;
    	}
    }
    
    public void excluirConfiguracaoAtendimentoFuncionarioVOPorUnidadeEnsino(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoUnidadeEnsinoVO obj, UsuarioVO usuarioLogado) throws Exception {
    	if(!configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs().isEmpty()){
    		List<FuncionarioVO> lista =  getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeAutoComplete("", obj.getUnidadeEnsinoVO().getCodigo().toString(), "", null, null, 0, false, usuarioLogado);
        	Iterator<ConfiguracaoAtendimentoFuncionarioVO> i = configuracaoAtendimentoVO.getListaConfiguracaoAtendimentoFuncionarioVOs().iterator();
        	whileFuncionario: while (i.hasNext()) {
    			ConfiguracaoAtendimentoFuncionarioVO objFuncionario = (ConfiguracaoAtendimentoFuncionarioVO) i.next();
    			for (FuncionarioVO funcionarioVO : lista) {
    				if(funcionarioVO.getCodigo().equals(objFuncionario.getFuncionarioVO().getCodigo())){
    					i.remove();
    					continue whileFuncionario;
    				}
    			}
    		}	
    	}
    }
    
    public void executarValidacaoSeExisteUnidadeEnsinoParaOutrasConfiguracaoAtendimento(ConfiguracaoAtendimentoVO obj, boolean inculir) throws Exception{
		StringBuilder sqlSb = new StringBuilder();
		StringBuilder msg = new StringBuilder();
		Boolean existeUnidade = false;
		Boolean adicionou = false;
		try {
			if (!obj.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs().isEmpty()) {
				sqlSb.append(" SELECT unidadeensino.nome as unidadeEnsino ");
				sqlSb.append(" FROM configuracaoatendimentounidadeensino ");
//				sqlSb.append(" inner join configuracaoatendimento on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo ");
				sqlSb.append(" inner join unidadeensino on configuracaoatendimentounidadeensino.unidadeensino = unidadeensino.codigo ");
				sqlSb.append(" where unidadeensino in ( ").append(obj.getListaCodigoUnidadeEnsino()).append(" ) ");
				if (!inculir) {
					sqlSb.append(" and configuracaoAtendimento <>  ").append(obj.getCodigo());
				}
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString());
				msg.append("Já existe uma configuração atendimento que utiliza as seguintes unidades de ensinos : ");
				while (tabelaResultado.next()) {
					existeUnidade = true;
					if (adicionou) {
						msg.append(", ").append(tabelaResultado.getString("unidadeEnsino"));
					} else {
						msg.append(tabelaResultado.getString("unidadeEnsino"));
						adicionou = true;
					}
				}
				if (existeUnidade) {
					throw new Exception(msg.toString());
				}
			}
		} finally {
			sqlSb = null;
			msg = null;
			existeUnidade = null;
			adicionou = null;
		}
    	
    }

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoAtendimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoAtendimento.idEntidade = idEntidade;
	}
	
	
}
