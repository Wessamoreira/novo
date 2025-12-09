/**
 * 
 */
package negocio.facade.jdbc.secretaria;

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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.processosel.enumeradores.TipoProcessamentoProvaPresencial;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.ResultadoProcessamentoArquivoRespostaProvaPresencialInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ResultadoProcessamentoArquivoRespostaProvaPresencial extends ControleAcesso implements ResultadoProcessamentoArquivoRespostaProvaPresencialInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ResultadoProcessamentoArquivoRespostaProvaPresencial() {
		super();
		setIdEntidade("ResultadoProcessamentoArquivoRespostaProvaPresencial");
	}
	
	public void validarDados(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj) throws Exception {
		if (obj.getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_RESPOSTA_GABARITO.name())) {
			if (obj.getGabaritoVO().getCodigo().equals(0)) {
				throw new Exception("O campo GABARITO deve ser informado.");
			}
		} else {
			if (obj.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				throw new Exception("O campo CONFIGURAÇÃO ACADÊMICO deve ser informado.");
			}
			if (obj.getPeriodicidadeCurso().equals("SE")) {
				if (obj.getAno().equals("")) {
					throw new Exception("O campo ANO deve ser informado.");
				}
				if (obj.getSemestre().equals("")) {
					throw new Exception("O campo SEMESTRE deve ser informado.");
				}
			}
			if (obj.getPeriodicidadeCurso().equals("AN")) {
				if (obj.getAno().equals("")) {
					throw new Exception("O campo ANO deve ser informado.");
				}
			}
			if (obj.getMatriculaProvaPresencialVOs() == null || obj.getMatriculaProvaPresencialVOs().isEmpty()) {
				throw new Exception("Não foi encontrado Alunos na lista para processamento do resultado, favor selecionar o arquivo para o procedimento.");
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
		try {

			validarDados(obj);
//			ResultadoProcessamentoArquivoRespostaProvaPresencial.incluir(getIdEntidade(), usuario);
			progressBarVO.setStatus("Incluindo Registros Principal.");
			final String sql = "INSERT INTO ResultadoProcessamentoArquivoRespostaProvaPresencial( gabarito, tipoProcessamentoProvaPresencial, configuracaoAcademico, variavelNota, realizarCalculoMediaLancamentoNota, periodicidadeCurso, ano, semestre, nomeArquivo, usuario, dataProcessamento, tipoAlteracaoSituacaoHistorico ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getGabaritoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getGabaritoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setString(2, obj.getTipoProcessamentoProvaPresencialEnum().name());
					if (obj.getConfiguracaoAcademicoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getConfiguracaoAcademicoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getVariavelNota());
					sqlInserir.setBoolean(5, obj.getRealizarCalculoMediaLancamentoNota());
					sqlInserir.setString(6, obj.getPeriodicidadeCurso());
					sqlInserir.setString(7, obj.getAno());
					sqlInserir.setString(8, obj.getSemestre());
					sqlInserir.setString(9, obj.getNomeArquivo());
					if (obj.getUsuarioVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getUsuarioVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					sqlInserir.setString(12, obj.getTipoAlteracaoSituacaoHistorico().name().toString());
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
			getFacadeFactory().getMatriculaProvaPresencialFacade().incluirMatriculaProvaPresencial(obj, progressBarVO, obj.getMatriculaProvaPresencialVOs(), usuario);			
			progressBarVO.setStatus("Incluindo log de erros");
			getFacadeFactory().getResultadoProcessamentoProvaPresencialMotivoErroFacade().incluirResultadoProcessamentoErro(obj.getCodigo(), obj.getListaResultadoProcessamentoProvaPresencialMotivoErroVOs(), usuario);
			if (obj.getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_RESPOSTA_GABARITO.name())) {
				progressBarVO.setStatus("Incluindo Registros Matrícula não localizadas.");
				getFacadeFactory().getMatriculaProvaPresencialNaoLocalizadaFacade().incluirMatriculaProvaPresencialNaoLocalizada(obj.getCodigo(), obj.getMatriculaProvaPresencialNaoLocalizadaVOs(), usuario);
			}
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
//			ResultadoProcessamentoArquivoRespostaProvaPresencial.alterar(getIdEntidade(), usuario);
			final String sql = "UPDATE ResultadoProcessamentoArquivoRespostaProvaPresencial set gabarito=?, tipoProcessamentoProvaPresencial=?, configuracaoAcademico=?, variavelNota=?, realizarCalculoMediaLancamentoNota=?, periodicidadeCurso=?, ano=?, semestre=?, nomeArquivo=?, usuario=?, dataProcessamento=?, tipoAlteracaoSituacaoHistorico=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getGabaritoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getGabaritoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setString(2, obj.getTipoProcessamentoProvaPresencialEnum().name());
					if (obj.getConfiguracaoAcademicoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getConfiguracaoAcademicoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getVariavelNota());
					sqlAlterar.setBoolean(5, obj.getRealizarCalculoMediaLancamentoNota());
					sqlAlterar.setString(6, obj.getPeriodicidadeCurso());
					sqlAlterar.setString(7, obj.getAno());
					sqlAlterar.setString(8, obj.getSemestre());
					sqlAlterar.setString(9, obj.getNomeArquivo());
					if (obj.getUsuarioVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getUsuarioVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					sqlAlterar.setString(12, obj.getTipoAlteracaoSituacaoHistorico().name().toString());
					sqlAlterar.setInt(13, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getMatriculaProvaPresencialFacade().alterarMatriculaProvaPresencial(obj, obj.getMatriculaProvaPresencialVOs(), usuario);
			getFacadeFactory().getMatriculaProvaPresencialNaoLocalizadaFacade().alterarMatriculaProvaPresencialNaoLocalizada(obj.getCodigo(), obj.getMatriculaProvaPresencialNaoLocalizadaVOs(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception {
    	ResultadoProcessamentoArquivoRespostaProvaPresencial.excluir(getIdEntidade());
    	getFacadeFactory().getMatriculaProvaPresencialHistoricoFacade().executarAtualizacaoHistoricoNotaAtualizadaParaHistoricoOriginal(obj, usuario);
		getFacadeFactory().getMatriculaProvaPresencialHistoricoFacade().excluirPorListaMatriculaProvaPresencialDisciplina(obj.getMatriculaProvaPresencialVOs(), usuario);
    	getFacadeFactory().getMatriculaProvaPresencialDisciplinaFacade().excluirPorMatriculaProvaPresencial(obj.getMatriculaProvaPresencialVOs(), usuario);
    	getFacadeFactory().getMatriculaProvaPresencialRespostaFacade().excluirPorMatriculaProvaPresencial(obj.getMatriculaProvaPresencialVOs(), usuario);
    	getFacadeFactory().getMatriculaProvaPresencialFacade().excluirMatriculaProvaPresencialPorResultado(obj.getCodigo(), usuario);
    	getFacadeFactory().getMatriculaProvaPresencialNaoLocalizadaFacade().excluirMatriculaProvaPresencialNaoLocalizadaPorResultadoArquivo(obj.getCodigo(), usuario);
    	
        String sql = "DELETE FROM ResultadoProcessamentoArquivoRespostaProvaPresencial WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DisciplinaEquivalenteVO</code>.
     *
     * @return O objeto da classe <code>DisciplinaEquivalenteVO</code> com os dados devidamente montados.
     */
    public static ResultadoProcessamentoArquivoRespostaProvaPresencialVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
    	ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj = new ResultadoProcessamentoArquivoRespostaProvaPresencialVO();
    	obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getGabaritoVO().setCodigo(dadosSQL.getInt("gabarito"));
		obj.getGabaritoVO().setDescricao(dadosSQL.getString("descricao"));
		if (dadosSQL.getString("tipoProcessamentoProvaPresencial") != null) {
			obj.setTipoProcessamentoProvaPresencialEnum(TipoProcessamentoProvaPresencial.valueOf(dadosSQL.getString("tipoProcessamentoProvaPresencial")));
		}
		obj.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("configuracaoAcademico.codigo"));
		obj.getConfiguracaoAcademicoVO().setNome(dadosSQL.getString("configuracaoAcademico.nome"));
		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		obj.setRealizarCalculoMediaLancamentoNota(dadosSQL.getBoolean("realizarCalculoMediaLancamentoNota"));
		obj.setPeriodicidadeCurso(dadosSQL.getString("periodicidadeCurso"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setNomeArquivo(dadosSQL.getString("nomeArquivo"));
		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario"));
		obj.getUsuarioVO().setNome(dadosSQL.getString("usuario.nome"));
		obj.setDataProcessamento(dadosSQL.getDate("dataProcessamento"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

	
	public static String getIdEntidade() {
		return ResultadoProcessamentoArquivoRespostaProvaPresencial.idEntidade;
	}

	
	public void setIdEntidade(String idEntidade) {
		ResultadoProcessamentoArquivoRespostaProvaPresencial.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception {
		if (obj.getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_RESPOSTA_GABARITO.name())) {
			obj.getGabaritoVO().setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getGabaritoVO().getConfiguracaoAcademicoVO().getCodigo(), usuarioVO));
		} else {
			obj.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademicoVO().getCodigo(), usuarioVO));
		}
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, progressBarVO, usuarioVO);			
		} else {
			alterar(obj, usuarioVO);
		}
	}
	
	public void carregarDados(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((ResultadoProcessamentoArquivoRespostaProvaPresencialVO) obj, NivelMontarDados.TODOS, usuario);
	}

	/**
	 * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
	 * Completo e faz a consulta de acordo com o nível especificado.
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @author Carlos
	 */
	public void carregarDados(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((ResultadoProcessamentoArquivoRespostaProvaPresencialVO) obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((ResultadoProcessamentoArquivoRespostaProvaPresencialVO) obj, resultado, usuario);
		}
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder str = new StringBuilder();
		str.append(" select resultado.codigo, resultado.gabarito, resultado.tipoProcessamentoProvaPresencial, configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\", configuracaoAcademico.nome AS \"configuracaoAcademico.nome\",  ");
		str.append(" resultado.variavelNota, resultado.realizarCalculoMediaLancamentoNota, resultado.periodicidadeCurso, resultado.ano, resultado.semestre, ");
		str.append(" resultado.nomeArquivo, resultado.usuario, resultado.dataProcessamento, resultado.tipoAlteracaoSituacaoHistorico  ");
		str.append(" from resultadoprocessamentoarquivorespostaprovapresencial resultado ");
		str.append(" left join gabarito on gabarito.codigo = resultado.gabarito ");
		str.append(" left join configuracaoAcademico on configuracaoAcademico.codigo = resultado.configuracaoAcademico ");
		return str;
	}
	
	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder str = new StringBuilder();
		str.append(" select resultado.codigo, resultado.gabarito, resultado.tipoProcessamentoProvaPresencial, configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\", configuracaoAcademico.nome AS \"configuracaoAcademico.nome\",  ");
		str.append(" resultado.variavelNota, resultado.realizarCalculoMediaLancamentoNota, resultado.periodicidadeCurso, resultado.ano, resultado.semestre, ");
		str.append(" resultado.nomeArquivo, resultado.usuario, resultado.dataProcessamento, resultado.tipoAlteracaoSituacaoHistorico  ");
		str.append(" from resultadoprocessamentoarquivorespostaprovapresencial resultado ");
		str.append(" left join gabarito on gabarito.codigo = resultado.gabarito ");
		str.append(" left join configuracaoAcademico on configuracaoAcademico.codigo = resultado.configuracaoAcademico ");
		
		return str;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (resultado.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (resultado.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		// Dados do Resultado
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getGabaritoVO().setCodigo(dadosSQL.getInt("gabarito"));
		obj.getGabaritoVO().setDescricao(dadosSQL.getString("descricao"));
		if (dadosSQL.getString("tipoProcessamentoProvaPresencial") != null) {
			obj.setTipoProcessamentoProvaPresencialEnum(TipoProcessamentoProvaPresencial.valueOf(dadosSQL.getString("tipoProcessamentoProvaPresencial")));
		}
		obj.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("configuracaoAcademico.codigo"));
		obj.getConfiguracaoAcademicoVO().setNome(dadosSQL.getString("configuracaoAcademico.nome"));
		obj.setRealizarCalculoMediaLancamentoNota(dadosSQL.getBoolean("realizarCalculoMediaLancamentoNota"));
		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		obj.setNomeArquivo(dadosSQL.getString("nomeArquivo"));
		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario"));
		obj.setDataProcessamento(dadosSQL.getDate("dataProcessamento"));
		if (dadosSQL.getString("tipoAlteracaoSituacaoHistorico") != null) {
			obj.setTipoAlteracaoSituacaoHistorico(TipoAlteracaoSituacaoHistoricoEnum.valueOf(dadosSQL.getString("tipoAlteracaoSituacaoHistorico")));
		}
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		// Dados do Resultado
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getGabaritoVO().setCodigo(dadosSQL.getInt("gabarito"));
		if (dadosSQL.getString("tipoProcessamentoProvaPresencial") != null) {
			obj.setTipoProcessamentoProvaPresencialEnum(TipoProcessamentoProvaPresencial.valueOf(dadosSQL.getString("tipoProcessamentoProvaPresencial")));
		}
		obj.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("configuracaoAcademico.codigo"));
		obj.getConfiguracaoAcademicoVO().setNome(dadosSQL.getString("configuracaoAcademico.nome"));
		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		obj.setRealizarCalculoMediaLancamentoNota(dadosSQL.getBoolean("realizarCalculoMediaLancamentoNota"));
		obj.setPeriodicidadeCurso(dadosSQL.getString("periodicidadeCurso"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setNomeArquivo(dadosSQL.getString("nomeArquivo"));
		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario"));
		obj.setDataProcessamento(dadosSQL.getDate("dataProcessamento"));
		if (!obj.getGabaritoVO().getCodigo().equals(0)) {
			obj.setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(obj.getGabaritoVO().getCodigo(), usuarioVO));
		}
		obj.setMatriculaProvaPresencialVOs(getFacadeFactory().getMatriculaProvaPresencialFacade().consultarPorResultadoProcessamentoArquivo(obj.getCodigo(), usuarioVO));
		obj.setMatriculaProvaPresencialNaoLocalizadaVOs(getFacadeFactory().getMatriculaProvaPresencialNaoLocalizadaFacade().consultarPorResultadoProcessamentoArquivo(obj.getCodigo(), usuarioVO));
		obj.setListaResultadoProcessamentoProvaPresencialMotivoErroVOs(getFacadeFactory().getResultadoProcessamentoProvaPresencialMotivoErroFacade().consultarPorResultadoProcessamentoProvaPresencial(obj.getCodigo(), usuarioVO));
	}
	
	public List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> consultarPorGabarito(String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct resultado.codigo, resultado.gabarito, resultado.tipoProcessamentoProvaPresencial, configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\", configuracaoAcademico.nome AS \"configuracaoAcademico.nome\",  ");
		sb.append(" resultado.variavelNota, resultado.realizarCalculoMediaLancamentoNota, resultado.periodicidadeCurso, resultado.ano, resultado.semestre, ");
		sb.append(" resultado.nomeArquivo, resultado.usuario, usuario.nome AS \"usuario.nome\", resultado.dataProcessamento, ");
		sb.append(" resultado.gabarito, gabarito.descricao  ");
		sb.append(" from resultadoprocessamentoarquivorespostaprovapresencial resultado ");
		sb.append(" left join gabarito on gabarito.codigo = resultado.gabarito ");
		sb.append(" left join configuracaoAcademico on configuracaoAcademico.codigo = resultado.configuracaoAcademico ");
		sb.append(" left join usuario on usuario.codigo = resultado.usuario");
		sb.append(" where descricao ilike('").append(valorConsulta).append("%') ");
		sb.append(" order by resultado.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}
	
	public List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> consultarPorMatricula(String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct resultado.codigo, resultado.gabarito, resultado.tipoProcessamentoProvaPresencial, configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\", configuracaoAcademico.nome AS \"configuracaoAcademico.nome\",  ");
		sb.append(" resultado.variavelNota, resultado.realizarCalculoMediaLancamentoNota, resultado.periodicidadeCurso, resultado.ano, resultado.semestre, ");
		sb.append(" resultado.nomeArquivo, resultado.usuario, usuario.nome AS \"usuario.nome\", resultado.dataProcessamento, ");
		sb.append(" resultado.gabarito, gabarito.descricao  ");
		sb.append(" from resultadoprocessamentoarquivorespostaprovapresencial resultado ");
		sb.append(" left join gabarito on gabarito.codigo = resultado.gabarito ");
		sb.append(" inner join matriculaProvaPresencial on matriculaProvaPresencial.resultadoprocessamentoarquivorespostaprovapresencial = resultado.codigo ");
		sb.append(" left join configuracaoAcademico on configuracaoAcademico.codigo = resultado.configuracaoAcademico ");
		sb.append(" left join usuario on usuario.codigo = resultado.usuario");
		sb.append(" where matriculaProvaPresencial.matricula ilike('").append(valorConsulta).append("%') ");
		sb.append(" order by resultado.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}
	
	public List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> consultarPorAluno(String valorConsulta, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct resultado.codigo, resultado.gabarito, resultado.tipoProcessamentoProvaPresencial, configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\", configuracaoAcademico.nome AS \"configuracaoAcademico.nome\",  ");
		sb.append(" resultado.variavelNota, resultado.realizarCalculoMediaLancamentoNota, resultado.periodicidadeCurso, resultado.ano, resultado.semestre, ");
		sb.append(" resultado.nomeArquivo, resultado.usuario, usuario.nome AS \"usuario.nome\", resultado.dataProcessamento, ");
		sb.append(" resultado.gabarito, gabarito.descricao  ");
		sb.append(" from resultadoprocessamentoarquivorespostaprovapresencial resultado ");
		sb.append(" left join gabarito on gabarito.codigo = resultado.gabarito ");
		sb.append(" inner join matriculaProvaPresencial on matriculaProvaPresencial.resultadoprocessamentoarquivorespostaprovapresencial = resultado.codigo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaProvaPresencial.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" left join configuracaoAcademico on configuracaoAcademico.codigo = resultado.configuracaoAcademico ");
		sb.append(" left join usuario on usuario.codigo = resultado.usuario");
		sb.append(" where pessoa.nome ilike('").append(valorConsulta).append("%') ");
		sb.append(" order by resultado.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}


}
