package negocio.facade.jdbc.recursoshumanos;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO.EnumCampoConsultaAfastamentoFuncionario;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoAfastamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoAfastamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.AfastamentoFuncionarioInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>AfastamentoFuncionarioVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>AfastamentoFuncionarioVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class AfastamentoFuncionario  extends SuperFacade<AfastamentoFuncionarioVO> implements AfastamentoFuncionarioInterfaceFacade<AfastamentoFuncionarioVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public AfastamentoFuncionario() throws Exception {
		super();
		setIdEntidade("AfastamentoFuncionario");
	}

	public AfastamentoFuncionario(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("AfastamentoFuncionario");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistirTodos(List<AfastamentoFuncionarioVO> listaAfastamentosFuncionario, FuncionarioCargoVO funcionarioCargo, boolean b, UsuarioVO usuarioVO) throws Exception {
		excluirPorFuncionarioCargo(funcionarioCargo, false, usuarioVO);

		for (AfastamentoFuncionarioVO afastamentoFuncionarioVO : listaAfastamentosFuncionario) {
			afastamentoFuncionarioVO.setFuncionarioCargo(funcionarioCargo);
			this.persistir(afastamentoFuncionarioVO, false, usuarioVO);

			incluirHistoricoAfastamento(funcionarioCargo, usuarioVO, afastamentoFuncionarioVO);
			incluirHistoricoSituacao(funcionarioCargo, usuarioVO, afastamentoFuncionarioVO);
		}

		if (Uteis.isAtributoPreenchido(listaAfastamentosFuncionario)) {
			//Pega o ultimo registro do afastamento.
			AfastamentoFuncionarioVO afastamentoFuncionarioVO = listaAfastamentosFuncionario.get(listaAfastamentosFuncionario.size() - 1);

			alterarSituacaoDoFuncionarioCargoPorTipoAfastamento(funcionarioCargo, usuarioVO, afastamentoFuncionarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(AfastamentoFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(AfastamentoFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AfastamentoFuncionario.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "afastamentofuncionario",
				new AtributoPersistencia()
						.add("funcionarioCargo", obj.getFuncionarioCargo().getCodigo())
						.add("tipoAfastamento", obj.getTipoAfastamento())
						.add("motivoAfastamento", obj.getMotivoAfastamento())
						.add("dataInicio", Uteis.getDataJDBCTimestamp(obj.getDataInicio()))
						.add("dataFinal", Uteis.getDataJDBCTimestamp(obj.getDataFinal()))
						.add("arquivo", obj.getArquivo().getCodigo() == 0 ? null :obj.getArquivo().getCodigo())
						.add("quantidadeDiasAfastado",  obj.getQuantidadeDiasAfastado()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	public void alterar(AfastamentoFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AfastamentoFuncionario.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "afastamentofuncionario", 
				new AtributoPersistencia()
				.add("funcionarioCargo", obj.getFuncionarioCargo().getCodigo())
				.add("tipoAfastamento", obj.getTipoAfastamento())
				.add("motivoAfastamento", obj.getMotivoAfastamento())
				.add("dataInicio", Uteis.getDataJDBCTimestamp(obj.getDataInicio()))
				.add("dataFinal", Uteis.getDataJDBCTimestamp(obj.getDataFinal()))
				.add("arquivo", obj.getArquivo().getCodigo() == 0 ? null :obj.getArquivo().getCodigo())
				.add("quantidadeDiasAfastado", obj.getQuantidadeDiasAfastado()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Exclui o {@link AfastamentoFuncionarioVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(AfastamentoFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AfastamentoFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM AfastamentoFuncionario WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}
	
	@Override
	public void excluirPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AfastamentoFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM AfastamentoFuncionario WHERE funcionariocargo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), funcionarioCargo.getCodigo());
	}
	
	@Override
	public void excluirPorFuncionarioCargo(List<AfastamentoFuncionarioVO> listaAfastamentosFuncionario, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		this.excluirPorFuncionarioCargo(funcionarioCargo, validarAcesso, usuarioVO);

		for (AfastamentoFuncionarioVO afastamentoFuncionarioVO : listaAfastamentosFuncionario) {
			if (Uteis.isAtributoPreenchido(afastamentoFuncionarioVO.getArquivo().getCodigo())) {
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
				getFacadeFactory().getArquivoFacade().excluir(afastamentoFuncionarioVO.getArquivo(), usuarioVO, configuracaoGeralSistemaVO);
			}
		}
	}

	/**
	 * Consulta o {@link AfastamentoFuncionarioVO} pelo  codigo informado.
	 */
	@Override
	public AfastamentoFuncionarioVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link AfastamentoFuncionarioVO}
	 */
	@Override
	public void validarDados(AfastamentoFuncionarioVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getTipoAfastamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_tipoAfastamento"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getMotivoAfastamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_motivoAfastamento"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataInicio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_dataInicio"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataFinal())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_dataFinal"));
		}
		
		validarDadosDataInicioMaiorDataFinal(obj);
	}

	/**
	 * Valida data final menor que a data inicial.
	 * 
	 * @param obj
	 * @throws ConsistirException
	 * @throws ParseException
	 */
	private void validarDadosDataInicioMaiorDataFinal(AfastamentoFuncionarioVO obj) throws ConsistirException {
		try {
			if (UteisData.validarDataInicialMaiorFinal(obj.getDataInicio(), obj.getDataFinal())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_ValorReferenciaFolhaPagamento_dataFinalMenorDataIncial"));
			}			
		} catch (ParseException e) {
			throw new ConsistirException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarAfastamentoFuncionario(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalAfastamentoFuncionario(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<AfastamentoFuncionarioVO> consultarAfastamentoFuncionario(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (EnumCampoConsultaAfastamentoFuncionario.valueOf(dataModelo.getCampoConsulta())) {
		case CARGO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND cargo.nome like UPPER(sem_acentos(?))");
			break;
		case FUNCIONARIO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_CARGO:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta());
			sql.append(" AND fc.matriculacargo = ?");
			break;
		case MATRICULA_FUNCIONARIO:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta());
			sql.append(" AND funcionario.matricula = ?");
			break;
		default:
			break;
		}

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Consulta todos os afastamento relativo ao funcionario cargo informado.
	 * 
	 * @param codigoFuncionarioCargo
	 */
	@Override
	public List<AfastamentoFuncionarioVO> consultarAfastamentoPorCodigoFuncionarioCargo(Integer codigoFuncionarioCargo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM afastamentofuncionario WHERE funcionariocargo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionarioCargo);

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Consulta o total de {@link AfastamentoFuncionarioVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalAfastamentoFuncionario(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaAfastamentoFuncionario.valueOf(dataModelo.getCampoConsulta())) {
		case CARGO:
			sql.append(" AND cargo.nome like UPPER(sem_acentos(?))");
			break;
		case FUNCIONARIO:
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_CARGO:
			sql.append(" AND fc.matriculacargo = ?");
			break;
		case MATRICULA_FUNCIONARIO:
			sql.append(" AND funcionario.matricula = ?");
			break;
		default:
			break;
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link AfastamentoFuncionarioVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<AfastamentoFuncionarioVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<AfastamentoFuncionarioVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public AfastamentoFuncionarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		AfastamentoFuncionarioVO obj = new AfastamentoFuncionarioVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoAfastamento"))) {
			obj.setTipoAfastamento(TipoAfastamentoEnum.valueOf(tabelaResultado.getString("tipoAfastamento")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivoAfastamento"))) {
			obj.setMotivoAfastamento(MotivoAfastamentoEnum.valueOf(tabelaResultado.getString("motivoAfastamento")));
		}

		obj.setDataInicio(tabelaResultado.getDate("dataInicio"));
		obj.setDataFinal(tabelaResultado.getDate("dataFinal"));
		obj.setQuantidadeDiasAfastado(tabelaResultado.getInt("quantidadeDiasAfastado"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("arquivo"))) {
			obj.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("arquivo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT afastamento.codigo, afastamento.funcionariocargo, pessoa.nome, fc.matriculacargo , afastamento.tipoafastamento,");
		sql.append(" afastamento.motivoafastamento, afastamento.dataInicio, afastamento.dataFinal, afastamento.arquivo");
		sql.append(" FROM afastamentofuncionario afastamento");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = afastamento.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(afastamento.codigo) as qtde FROM AfastamentoFuncionario afastamento");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = afastamento.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}
	
	private void incluirHistoricoAfastamento(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO,
			AfastamentoFuncionarioVO afastamentoFuncionarioVO) throws Exception {
		HistoricoAfastamentoVO historicoAfastamento = getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().montarDadosPorAfastamentoFuncionario(afastamentoFuncionarioVO, funcionarioCargo);
		getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().persistir(historicoAfastamento, false, usuarioVO);
	}
	
	private void incluirHistoricoSituacao(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO, AfastamentoFuncionarioVO afastamentoFuncionarioVO) throws Exception {
		HistoricoSituacaoVO historicoSituacaoVO = getFacadeFactory().getHistoricoSituacaoInterfaceFacade().montarDadosPorAfastamentoFuncionario(afastamentoFuncionarioVO, funcionarioCargo);
		getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistir(historicoSituacaoVO, false, usuarioVO);
	}

	private void alterarSituacaoDoFuncionarioCargoPorTipoAfastamento(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO, AfastamentoFuncionarioVO afastamentoFuncionarioVO) throws Exception {
		String situacaoFuncionario = validarSituacaoFuncionarioPorTipoAfastamento(afastamentoFuncionarioVO.getTipoAfastamento().name());
		getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionario(funcionarioCargo.getCodigo(), situacaoFuncionario, usuarioVO);
	}

	/**
	 * Valida a Situação do funcionario pelo tipo de afastamento.
	 * 
	 * @param afastamentoFuncionarioVO
	 * @return
	 */
	@Override
	public String validarSituacaoFuncionarioPorTipoAfastamento(String afastamentoFuncionarioVO) {
		switch (afastamentoFuncionarioVO) {
		case "FERIAS":
			return "FERIAS";
		case "AFASTAMENTO_ACIDENTE_TRABALHO":
			return "AFASTAMENTO_PREVIDENCIA";
		case "AFASTAMENTO_PREVIDENCIA":
			return "AFASTAMENTO_PREVIDENCIA";
		case "LICENCA_MATERNIDADE":
			return "LICENCA_MATERNIDADE";
		case "LICENCA_MATERNIDADE_COMPL_180_DIAS":
			return "LICENCA_MATERNIDADE";
		case "LICENCA_PATERNIDADE":
			return "LICENCA_PATERNIDADE";
		case "LICENCA_REMUNERADA":
			return "LICENCA_REMUNERADA";
		case "LICENCA_SEM_VENCIMENTO":
			return "LICENCA_SEM_VENCIMENTO";
		case "OUTROS":
			return "OUTROS";
		default:
			break;
		}
		return "ATIVO";
	}

	@Override
	public SqlRowSet consultarQuantidadeDeDiasAfastados(FuncionarioCargoVO funcionarioCargoVO, Integer anoCompetencia, Integer mesCompetencia) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT datainicio as dataInicialAfastamento, datafinal as dataFinalAfastamento FROM afastamentofuncionario WHERE funcionariocargo = ? "); 
		sql.append(" and (datainicio between ? and ? or datafinal between ? and ? )");
		sql.append(" order by datainicio ");

		List<Object> filtros = new ArrayList<>();
		filtros.add(funcionarioCargoVO.getCodigo());
		filtros.add(UteisData.getPrimeiroDataMes(mesCompetencia, anoCompetencia));
		filtros.add(UteisData.getUltimaDataMes(mesCompetencia, anoCompetencia));
		filtros.add(UteisData.getPrimeiroDataMes(mesCompetencia, anoCompetencia));
		filtros.add(UteisData.getUltimaDataMes(mesCompetencia, anoCompetencia));
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		if(tabelaResultado.next()) {
			return tabelaResultado;
		}

		return null;
	}

	/**
 	 * Consulta o ultimo registro do {@link FuncionarioCargoVO} e pela data de inicio 
	 */
	@Override
	public List<AfastamentoFuncionarioVO> consultarUltimpoAfastamentoPorFuncionarioEDataComparacao(FuncionarioCargoVO funcionarioCargoVO, Date dataComparacao) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from afastamentofuncionario where funcionariocargo = ?");
		sql.append(" and ( (EXTRACT('Month' FROM datainicio) = ? and EXTRACT('Year' FROM datainicio) = ? )");
		sql.append(" or (extract('month' from datafinal) = ? and extract('year' from datafinal) = ?))");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo(), UteisData.getMesData(dataComparacao), UteisData.getAnoData(dataComparacao), UteisData.getMesData(dataComparacao), UteisData.getAnoData(dataComparacao));
		
		List<AfastamentoFuncionarioVO> afastamentoFuncionarioVOs = new ArrayList<>();
		while (tabelaResultado.next()) {
			afastamentoFuncionarioVOs.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return afastamentoFuncionarioVOs;
	}

	/**
	 * Consulta o ultimo registro do {@link FuncionarioCargoVO} e pela data de inicio 
	 */
	@Override
	public List<AfastamentoFuncionarioVO> consultarAfastamentoPorFuncionarioEDataComparacaoEMotivoAfastamento(FuncionarioCargoVO funcionarioCargoVO, Date dataComparacao, String motivo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from afastamentofuncionario where funcionariocargo = ?");
		sql.append(" and ( (EXTRACT('Month' FROM datainicio) = ? and EXTRACT('Year' FROM datainicio) = ? )");
		sql.append(" or (extract('month' from datafinal) = ? and extract('year' from datafinal) = ?))");
		sql.append(" and motivoAfastamento = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo(), UteisData.getMesData(dataComparacao), UteisData.getAnoData(dataComparacao), UteisData.getMesData(dataComparacao), UteisData.getAnoData(dataComparacao), motivo);
		List<AfastamentoFuncionarioVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return lista;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AfastamentoFuncionario.idEntidade = idEntidade;
	}
}