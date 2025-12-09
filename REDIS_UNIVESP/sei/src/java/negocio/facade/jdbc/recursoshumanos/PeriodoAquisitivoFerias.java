package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.PeriodoAquisitivoFeriasInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>PeriodoAquisitivoFeriasVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>PeriodoAquisitivoFeriasVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PeriodoAquisitivoFerias extends SuperFacade<PeriodoAquisitivoFeriasVO> implements PeriodoAquisitivoFeriasInterfaceFacade<PeriodoAquisitivoFeriasVO> {

	private static final long serialVersionUID = 5561153106086740266L;
	protected static String idEntidade;

	public PeriodoAquisitivoFerias() throws Exception {
		super();
		setIdEntidade("PeriodoAquisitivoFerias");
	}
	
	public PeriodoAquisitivoFerias(Conexao conexao){
		super();
		setConexao(conexao);
		setIdEntidade("FaltasFuncionario");
	}
	
	public PeriodoAquisitivoFerias(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("FaltasFuncionario");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistirTodos(List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivoFeriasFuncionario, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		excluir(listaPeriodoAquisitivoFeriasFuncionario, funcionarioCargo, usuarioVO);
		
		for (PeriodoAquisitivoFeriasVO periodoAquisitivo : listaPeriodoAquisitivoFeriasFuncionario) {
			periodoAquisitivo.setFuncionarioCargo(funcionarioCargo);
			persistir(periodoAquisitivo, validarAcesso, usuarioVO);
		}
	}

	private void excluir(List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivoFeriasFuncionario, FuncionarioCargoVO funcionarioCargoVO, UsuarioVO usuarioVO) {
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(funcionarioCargoVO.getCodigo());

		Iterator<PeriodoAquisitivoFeriasVO> i = listaPeriodoAquisitivoFeriasFuncionario.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM periodoaquisitivoferias WHERE funcionariocargo = ?");
	    while (i.hasNext()) {
	    	PeriodoAquisitivoFeriasVO objeto = i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
		
	}

	@Override
	public void persistir(PeriodoAquisitivoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(PeriodoAquisitivoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			PeriodoAquisitivoFerias.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.periodoaquisitivoferias(funcionariocargo, inicioPeriodo, finalPeriodo, situacao, informacoesAdicionais, ");
					sql.append(" qtdFalta)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ");
					sql.append(" ?) ");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInicioPeriodo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFinalPeriodo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInformacoesAdicionais(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getQtdFalta(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException {
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

	@Override
	public void alterar(PeriodoAquisitivoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		PeriodoAquisitivoFerias.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.periodoaquisitivoferias SET funcionariocargo=?, inicioPeriodo=?, finalPeriodo=?, situacao=?, informacoesAdicionais=?, ");
				sql.append(" qtdFalta=? ");
				sql.append(" WHERE codigo=?");
				sql.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getInicioPeriodo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFinalPeriodo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getInformacoesAdicionais(), ++i, sqlAlterar);

				Uteis.setValuePreparedStatement(obj.getQtdFalta(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	/**
	 * Alterar situação do periodo aquisitivo. 
	 */
	@Override
	public void alterarSituacao(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		PeriodoAquisitivoFerias.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.periodoaquisitivoferias SET situacao=? WHERE codigo=?");
				sql.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(periodoAquisitivoFeriasVO.getSituacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(periodoAquisitivoFeriasVO.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
		
	}

	@Override
	public void excluir(PeriodoAquisitivoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		PeriodoAquisitivoFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM periodoaquisitivoferias WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM periodoaquisitivoferias WHERE (funcionariocargo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public PeriodoAquisitivoFeriasVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE periodoaquisitivoferias.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	public Integer consultarPorDataInicioEDataFim(PeriodoAquisitivoFeriasVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(periodoaquisitivoferias.codigo) AS qtde FROM periodoaquisitivoferias");
		sql.append(" INNER JOIN funcionariocargo ON funcionariocargo.codigo = periodoaquisitivoferias.funcionariocargo");
		sql.append(" WHERE (inicioPeriodo BETWEEN ? AND ? OR finalPeriodo BETWEEN ? AND ?)");
		sql.append(" AND funcionariocargo.matriculacargo = ? AND periodoaquisitivoferias.codigo != ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getInicioPeriodo(), obj.getFinalPeriodo(), obj.getInicioPeriodo(), obj.getFinalPeriodo(), obj.getFuncionarioCargo().getMatriculaCargo(), obj.getCodigo());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	private StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT periodoaquisitivoferias.* FROM public.periodoaquisitivoferias ");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = periodoaquisitivoferias.funcionariocargo ");
		return sql;
	}

	@Override
	public PeriodoAquisitivoFeriasVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		PeriodoAquisitivoFeriasVO obj = new PeriodoAquisitivoFeriasVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setFuncionarioCargo(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null)));
		obj.setInicioPeriodo(tabelaResultado.getDate("inicioPeriodo"));
		obj.setFinalPeriodo(tabelaResultado.getDate("finalPeriodo"));
		obj.setInformacoesAdicionais(tabelaResultado.getString("informacoesAdicionais"));
		
		obj.setQtdFalta(tabelaResultado.getInt("qtdFalta"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacao(SituacaoPeriodoAquisitivoEnum.valueOf(tabelaResultado.getString("situacao")));
		}

		return obj;
	}
	
	/**
	 * Monta a lista de dados da consulta.
	 * 
	 * @param tabelaResultado
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	private List<PeriodoAquisitivoFeriasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<PeriodoAquisitivoFeriasVO> lista = new ArrayList<>();
		while(tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return lista;
	}

	@Override
	public void validarDados(PeriodoAquisitivoFeriasVO obj) throws ConsistirException {
		
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getInicioPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_inicioPeriodo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getFinalPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_finalPeriodo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getSituacao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_situacao"));
		}
		
		if (obj.getInicioPeriodo().after(obj.getFinalPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dataFimMaiorDataInicio"));
		}
	}
	
	@Override
	public void validarDadosAdicionarPeriodoAquisitivo(PeriodoAquisitivoFeriasVO obj, List<PeriodoAquisitivoFeriasVO> listaPeriodosAquisitivos) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getInicioPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_inicioPeriodo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getFinalPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_finalPeriodo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getSituacao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_situacao"));
		}

		if (obj.getInicioPeriodo().after(new Date())  && obj.getSituacao().equals(SituacaoPeriodoAquisitivoEnum.FECHADO)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_dataInvalidaParaSituacaoFechada"));
		}

		if (obj.getInicioPeriodo().after(obj.getFinalPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dataFimMenorDataInicio"));
		}

		if (obj.getInicioPeriodo().equals(obj.getFinalPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_periodosIguais"));
		}

		if(UteisData.adicionarDiasEmData(UteisData.obterDataFuturaAdicionandoMes(obj.getInicioPeriodo(), 12), -1).before(obj.getFinalPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_periodoAquisitivoMaiorQueUmAno"));
		}

		//Existir somente 1 período aberto
		if(listaPeriodosAquisitivos.stream().anyMatch(p -> p.getSituacao().equals(SituacaoPeriodoAquisitivoEnum.ABERTO) && 
					obj.getSituacao().equals(p.getSituacao())) && !Uteis.isAtributoPreenchido(obj.getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_jaExisteUmPeriodoAbertoCadastrado"));
		}
	}	

	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		dataModelo.setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario));
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario));
	}

	@Override
	public List<PeriodoAquisitivoFeriasVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE periodoaquisitivoferias.funcionariocargo = ?");
		sql.append(" order by periodoaquisitivoferias.inicioperiodo desc ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo());
		
		List<PeriodoAquisitivoFeriasVO> lista = new ArrayList<>();
		while(rs.next()) {
			lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return lista;
	}

	@Override
	public PeriodoAquisitivoFeriasVO consultarPrimeiroPeriodoAquisitivoAbertoPorFuncionarioCargo(String matriculaCargo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE periodoaquisitivoferias.situacao = ? and funcionariocargo.matriculacargo = ? ");
		sql.append(" order by periodoaquisitivoferias.finalperiodo asc limit 1 ");

		List<String> filtros = new ArrayList<>();
		filtros.add(SituacaoPeriodoAquisitivoEnum.ABERTO.getValor());
		filtros.add(matriculaCargo);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		try {
			if(tabelaResultado.next())
				return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new PeriodoAquisitivoFeriasVO();
	}

	@Override
	public PeriodoAquisitivoFeriasVO consultarPrimeiroPeriodoAquisitivoVencidoPorFuncionarioCargo(String matriculaCargo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE periodoaquisitivoferias.situacao = ? and funcionariocargo.matriculacargo = ? ");
		sql.append(" order by periodoaquisitivoferias.finalperiodo asc limit 1 ");
		
		List<String> filtros = new ArrayList<>();
		filtros.add(SituacaoPeriodoAquisitivoEnum.VENCIDO.getValor());
		filtros.add(matriculaCargo);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		try {
			if(tabelaResultado.next())
				return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new PeriodoAquisitivoFeriasVO();
	}
	
	@Override
	public List<PeriodoAquisitivoFeriasVO> consultarPeriodoAquisitivoAbertoEVencido() throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM periodoaquisitivoferias WHERE situacao != 'FECHADO'");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}

		return new ArrayList<>();
	}

	public PeriodoAquisitivoFeriasVO consultarUltimoPeriodoAquisitivoPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE funcionariocargo.codigo = ? ");
		sql.append(" order by periodoaquisitivoferias.finalperiodo desc limit 1 ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo());
		
		try {
			if(tabelaResultado.next())
				return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new PeriodoAquisitivoFeriasVO();
	}

	@Override
	public PeriodoAquisitivoFeriasVO montarDadosAPartirFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		PeriodoAquisitivoFeriasVO obj = new PeriodoAquisitivoFeriasVO();
		obj.setFuncionarioCargo(funcionarioCargo);
		obj.setInicioPeriodo(new Date());
		preencherFinalPeriodoAquisitivo(obj);
		obj.setSituacao(SituacaoPeriodoAquisitivoEnum.ABERTO);
		return obj;
	}

	@Override
	public void preencherFinalPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO) {
		if(Uteis.isAtributoPreenchido(periodoAquisitivoFeriasVO.getInicioPeriodo())) {
			Date dataFinalPeriodoAquisitivo = UteisData.obterDataFuturaAdicionandoMes(periodoAquisitivoFeriasVO.getInicioPeriodo(), 12);
			periodoAquisitivoFeriasVO.setFinalPeriodo(UteisData.adicionarDiasEmData(dataFinalPeriodoAquisitivo, -1));
		}
	}

	/**
	 * Inativa todos os periodos aquisitivo de ferias do funcionario cargo informado.
	 * @throws Exception 
	 */
	@Override
	public void inativarPeriodoAquisitivoFeriasDoFuncionarioCargo(FuncionarioCargoVO obj) throws Exception {
		List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivo = consultarPeriodoAquisitivoAtivoFuncionarioCargo(obj);
		for (PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO : listaPeriodoAquisitivo) {
			inativarPeriodoAquisitivoFeriasDoFuncionarioCargo(periodoAquisitivoFeriasVO);
		}
	}

	private void inativarPeriodoAquisitivoFeriasDoFuncionarioCargo(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.periodoaquisitivoferias SET situacao=? WHERE codigo=?");

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(SituacaoPeriodoAquisitivoEnum.FECHADO, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(periodoAquisitivoFeriasVO.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
		
	}

	private List<PeriodoAquisitivoFeriasVO> consultarPeriodoAquisitivoAtivoFuncionarioCargo(FuncionarioCargoVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico())
		.append(" INNER JOIN funcionario ON funcionario.codigo = funcionariocargo.funcionario")
		.append(" WHERE funcionario.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getFuncionarioVO().getCodigo());
		List<PeriodoAquisitivoFeriasVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}

		return lista;
	}

	/**
	 * Atualiza a situacao do periodo aquisitivo dos funcionarios
	 * 
	 * Caso o PeriodoAquisitivoFerias do FuncionarioCargoVO esteja completo altera a situacao do PeriodoAquisitivoFerias como VENCIDO
	 * e abre um novo PeriodoAquisitivoFerias com situacao igual a ABERTO
	 * 
	 * @param funcionarioCargoVO
	 * 
	 */
	public void realizarAtualizacaoDoPeriodoAquisitivoDoFuncionario(CompetenciaFolhaPagamentoVO competencia, FuncionarioCargoVO funcionarioCargoVO) throws Exception{

		PeriodoAquisitivoFeriasVO periodoAquisitivo = consultarPrimeiroPeriodoAquisitivoAbertoPorFuncionarioCargo(funcionarioCargoVO.getMatriculaCargo());

		if(Uteis.isAtributoPreenchido(periodoAquisitivo)) {
			//Atualiza a falta dos funcionarios
			Integer qtdFaltas = realizarAtualizacaoDasFaltasDoFuncionarioCargoNoPeriodoAquisitivo(periodoAquisitivo);

			if(qtdFaltas >= 32) {
				periodoAquisitivo.setSituacao(SituacaoPeriodoAquisitivoEnum.PERDIDO);
			} else {
				periodoAquisitivo.setSituacao(SituacaoPeriodoAquisitivoEnum.VENCIDO);
			}

			if(validarAberturaNovoPeriodoAquisitivo(periodoAquisitivo, competencia)) {
				alterarSituacaoDoPeriodoAquisitivo(periodoAquisitivo);	
				realizarAbrirNovoPeriodoAquisitivoParaFuncionarioCargo(funcionarioCargoVO);
			}

		} else {
			realizarAbrirNovoPeriodoAquisitivoParaFuncionarioCargo(funcionarioCargoVO);
		}
	}

	public void alterarSituacaoDoPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivo) {
		StringBuilder sql = new StringBuilder("update periodoaquisitivoferias set situacao = ? where codigo = ? ");
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { periodoAquisitivo.getSituacao().getValor(), periodoAquisitivo.getCodigo()});
	}

	/**
	 * Valida a necessiadade de abrir um novo periodo aquisitivo:
	 * - Caso nao exista nenhum periodo aquisitivo aberto
	 * - Data do periodo aquisitivo e menor que a data final da competencia
	 *  
	 * @param periodoAquisitivo
	 * @param competencia
	 * @return true - caso haja necessidade de criar um novo periodo aquisitivo
	 */
	public boolean validarAberturaNovoPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivo, CompetenciaFolhaPagamentoVO competencia) {
		
		if(!Uteis.isAtributoPreenchido(periodoAquisitivo))
			return true;
		
		if(periodoAquisitivo.getFinalPeriodo().before(UteisData.getUltimaDataMes(competencia.getDataCompetencia())) ||
				periodoAquisitivo.getFinalPeriodo().equals(UteisData.getUltimaDataMes(competencia.getDataCompetencia())))
			return true;
		
		return false;
	}

	/**
	 * Atualiza a qtdFalta do periodo aquisitivo para 
	 * 
	 * @param periodoAquisitivo
	 * @param funcionarioCargoVO
	 */
	public Integer realizarAtualizacaoDasFaltasDoFuncionarioCargoNoPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivo) {
		Integer qtdFaltas = getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarQtdFaltasDoPeriodo(periodoAquisitivo.getFuncionarioCargo(), periodoAquisitivo.getInicioPeriodo(), periodoAquisitivo.getFinalPeriodo());
		
		StringBuilder sql = new StringBuilder("update periodoaquisitivoferias set qtdFalta = ? where codigo = ? ");
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { qtdFaltas, periodoAquisitivo.getCodigo() });
		
		return qtdFaltas;
	}

	/**
	 * Abre um novo PeriodoAquisitivo com situacao igual a ABERTO para o FuncionarioCargo
	 * @param funcionarioCargoVO
	 * @throws Exception
	 */
	public void realizarAbrirNovoPeriodoAquisitivoParaFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		PeriodoAquisitivoFeriasVO periodoAquisitivo = consultarUltimoPeriodoAquisitivoPorFuncionarioCargo(funcionarioCargoVO);
		PeriodoAquisitivoFeriasVO periodoAquisitivoNovo = new PeriodoAquisitivoFeriasVO();
		
		if(!Uteis.isAtributoPreenchido(periodoAquisitivo)) {
			periodoAquisitivoNovo.setInicioPeriodo(funcionarioCargoVO.getDataAdmissao());
		} else {
			periodoAquisitivoNovo.setInicioPeriodo(UteisData.obterDataFuturaUsandoCalendar(periodoAquisitivo.getFinalPeriodo(), 1));
			
		}
		periodoAquisitivoNovo.setFuncionarioCargo(funcionarioCargoVO);
		periodoAquisitivoNovo.setSituacao(SituacaoPeriodoAquisitivoEnum.ABERTO);
		preencherFinalPeriodoAquisitivo(periodoAquisitivoNovo);
		
		persistir(periodoAquisitivoNovo, false, null);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		PeriodoAquisitivoFerias.idEntidade = idEntidade;
	}

	
	/**
	 * Encerra o periodo aquisitivo 
	 * Caso encerrarPeriodoAquisitivo == true: abre um novo periodo aquisitivo e fecha o antigo com a dataFechamento
	 * 
	 * @param periodoAquisitivo
	 * @param encerrarPeriodoAquisitivo
	 * @param dataFechamento
	 * @param validarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void encerrarPeriodoAquisitivo(PeriodoAquisitivoFeriasVO periodoAquisitivo, boolean encerrarPeriodoAquisitivo, Date dataFechamento, 
			boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		if (Uteis.isAtributoPreenchido(periodoAquisitivo)) {

			if(encerrarPeriodoAquisitivo && Uteis.isAtributoPreenchido(dataFechamento) 
					&& periodoAquisitivo.getSituacao().equals(SituacaoPeriodoAquisitivoEnum.ABERTO)) {

				periodoAquisitivo.setSituacao(SituacaoPeriodoAquisitivoEnum.FECHADO);				
				periodoAquisitivo.setFinalPeriodo(dataFechamento);
				persistir(periodoAquisitivo, validarAcesso, usuarioVO);
			} else {
				periodoAquisitivo.setSituacao(SituacaoPeriodoAquisitivoEnum.FECHADO);
				alterarSituacao(periodoAquisitivo, validarAcesso, usuarioVO);				
			}
		}
	}

	
	/**
	 * Consulta PeriodoAquisitivo de ferias do funcionario valido para marcacao de ferias (Vencido ou Aberto)
	 * 
	 * @param funcionarioCargo
	 * @return
	 */
	@Override
	public PeriodoAquisitivoFeriasVO consultarPeriodoAquisitivoValidoParaFerias(FuncionarioCargoVO funcionarioCargo) {
		
		PeriodoAquisitivoFeriasVO periodoAquisitivo = consultarPrimeiroPeriodoAquisitivoVencidoPorFuncionarioCargo(funcionarioCargo.getMatriculaCargo());
		
		if(Uteis.isAtributoPreenchido(periodoAquisitivo))
			return periodoAquisitivo;
		else
			return consultarPrimeiroPeriodoAquisitivoAbertoPorFuncionarioCargo(funcionarioCargo.getMatriculaCargo());
	}

	@Override
	public Integer consultarQuantidadeFaltasPorPeriodoAquisitivo(Integer codigo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT qtdfalta FROM periodoaquisitivoferias WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtdfalta");
		}
		return 0;
	}
}