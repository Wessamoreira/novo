package negocio.facade.jdbc.recursoshumanos;

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
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO.EnumCampoConsultaHistoricoAfastamento;
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
import negocio.interfaces.recursoshumanos.HistoricoAfastamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoAfastamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoAfastamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoAfastamento  extends SuperFacade<HistoricoAfastamentoVO> implements HistoricoAfastamentoInterfaceFacade<HistoricoAfastamentoVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public HistoricoAfastamento() throws Exception {
		super();
		setIdEntidade("HistoricoAfastamento");
	}

	public HistoricoAfastamento(Conexao conexao, FacadeFactory facade) throws Exception {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("HistoricoAfastamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoAfastamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(HistoricoAfastamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			HistoricoAfastamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			incluir(obj, "historicoafastamento", new AtributoPersistencia()
					.add("funcionariocargo", obj.getFuncionarioCargo())
					.add("datainicio", obj.getDataInicio())
					.add("datafinal", obj.getDataFinal())
					.add("datarequerimento", obj.getDataRequerimento())
					.add("quantidadedias", obj.getQuantidade())
					.add("tipoafastamento", obj.getTipoAfastamento())
					.add("motivoafastamento", obj.getMotivoAfastamento())					
					.add("processado", obj.getProcessado())					
					, usuarioVO);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(HistoricoAfastamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoAfastamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "historicoafastamento", new AtributoPersistencia()
				.add("funcionariocargo", obj.getFuncionarioCargo())
				.add("datainicio", obj.getDataInicio())
				.add("datafinal", obj.getDataFinal())
				.add("datarequerimento", obj.getDataRequerimento())
				.add("quantidadedias", obj.getQuantidade())
				.add("tipoafastamento", obj.getTipoAfastamento())
				.add("motivoafastamento", obj.getMotivoAfastamento())			
				.add("processado", obj.getProcessado())			
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	/**
	 * Exclui o {@link HistoricoAfastamentoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoAfastamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoAfastamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoAfastamento WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	/**
	 * Consulta o {@link HistoricoAfastamentoVO} pelo  codigo informado.
	 */
	@Override
	public HistoricoAfastamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link HistoricoAfastamentoVO}
	 */
	@Override
	public void validarDados(HistoricoAfastamentoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoAfastamento_funcionarioCargo"));
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarHistoricoAfastamento(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoAfastamento(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoAfastamentoVO> consultarHistoricoAfastamento(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (EnumCampoConsultaHistoricoAfastamento.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Consulta o total de {@link HistoricoAfastamentoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHistoricoAfastamento(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaHistoricoAfastamento.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Monta a lista de {@link HistoricoAfastamentoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoAfastamentoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoAfastamentoVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public HistoricoAfastamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoAfastamentoVO obj = new HistoricoAfastamentoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
		obj.setDataInicio(tabelaResultado.getDate("datainicio"));
		obj.setDataFinal(tabelaResultado.getDate("datafinal"));
		obj.setDataRequerimento(tabelaResultado.getDate("datarequerimento"));
		obj.setQuantidade(tabelaResultado.getInt("quantidadedias"));
		obj.setProcessado(tabelaResultado.getBoolean("processado"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivoafastamento"))) {
			obj.setMotivoAfastamento(MotivoAfastamentoEnum.valueOf(tabelaResultado.getString("motivoafastamento")));
		}

		if ( Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoafastamento"))) {
			obj.setTipoAfastamento(TipoAfastamentoEnum.valueOf(tabelaResultado.getString("tipoafastamento")));
		}

		return obj;
	}

	@Override
	public HistoricoAfastamentoVO montarDadosPorAfastamentoFuncionario(AfastamentoFuncionarioVO afastamentoFuncionarioVO, FuncionarioCargoVO funcionarioCargo) {
		HistoricoAfastamentoVO historicoFuncionario= new HistoricoAfastamentoVO();

		historicoFuncionario.setDataInicio(afastamentoFuncionarioVO.getDataInicio());
		historicoFuncionario.setDataFinal(afastamentoFuncionarioVO.getDataFinal());
		historicoFuncionario.setMotivoAfastamento(afastamentoFuncionarioVO.getMotivoAfastamento());
		historicoFuncionario.setTipoAfastamento(afastamentoFuncionarioVO.getTipoAfastamento());
		historicoFuncionario.setDataRequerimento(new Date());
		historicoFuncionario.setFuncionarioCargo(funcionarioCargo);
		historicoFuncionario.setProcessado(Boolean.FALSE);

		historicoFuncionario.setQuantidade(afastamentoFuncionarioVO.getQuantidadeDiasAfastado());

		return historicoFuncionario;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT hist.codigo, hist.funcionariocargo, pessoa.nome, fc.matriculacargo , hist.datainicio, hist.datafinal,");
		sql.append(" hist.datarequerimento, hist.quantidadedias, hist.tipoafastamento, hist.motivoafastamento, hist.processado FROM HistoricoAfastamento hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(hist.codigo) as qtde FROM HistoricoAfastamento hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processaSituacaoFuncionarioAfastado(UsuarioVO usuarioVO) throws Exception {
		List<HistoricoAfastamentoVO> historicoAfastamentoVOs = consultarHistoriosAfastamentosParaProcessar();

		for (HistoricoAfastamentoVO historicoAfastamentoVO : historicoAfastamentoVOs) {
			getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionario(historicoAfastamentoVO.getFuncionarioCargo().getCodigo(), "ATIVO", usuarioVO);

			historicoAfastamentoVO.setProcessado(Boolean.TRUE);
			this.alterar(historicoAfastamentoVO, false, usuarioVO);

			HistoricoSituacaoVO historicoSituacaoVO = getFacadeFactory().getHistoricoSituacaoInterfaceFacade().montarDadosPorSituacaoFuncionario(historicoAfastamentoVO.getFuncionarioCargo(), "ATIVO");
			getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistir(historicoSituacaoVO, false, usuarioVO);
		}
	}

	private List<HistoricoAfastamentoVO> consultarHistoriosAfastamentosParaProcessar() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM historicoafastamento ")
		.append("WHERE datafinal < now() AND processado = false");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return new ArrayList<>();
		}

		return montarDadosLista(tabelaResultado);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		HistoricoAfastamento.idEntidade = idEntidade;
	}
	
	@Override
	public SqlRowSet consultarAfastamentoDoFuncionarioCargo(FuncionarioCargoVO funcionarioCargo, Date dataInicio, Date dataFinal, Boolean consideraAfastamentoPrevidencia) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT datainicio as dataInicialAfastamento, to_char(datainicio, 'dd'):: integer as diaInicio, to_char(datainicio, 'MM'):: integer as mesInicio, "); 
		sql.append(" datafinal as dataFinalAfastamento, to_char(datafinal, 'dd'):: integer as diaFinal, to_char(datafinal, 'MM'):: integer as mesFinal ");
		sql.append(" from historicoafastamento "); 
		sql.append(" where funcionariocargo = ? and (datainicio between ? and ? or datafinal between ? and ?)");
		
		if(consideraAfastamentoPrevidencia) {
			sql.append(" and (tipoafastamento like 'LICENCA_SEM_VENCIMENTO' or tipoafastamento like 'OUTROS' or tipoafastamento like 'AFASTAMENTO_PREVIDENCIA') ");
		} else {
			sql.append(" and (tipoafastamento like 'LICENCA_SEM_VENCIMENTO' or tipoafastamento like 'OUTROS') ");
		}
		
		sql.append(" order by datainicio asc ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargo.getCodigo(), dataInicio, dataFinal, dataInicio, dataFinal);
		
		if(tabelaResultado.next()) {
			return tabelaResultado;
		}
		
		return null;
	}

	@Override
	public int consultarQuantidadeDeDiasDeAfastamentoDoFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, Date inicioPeriodo, Date finalPeriodo, Boolean consideraAfastamentoPrevidencia) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT datainicio, datafinal from historicoafastamento "); 
		sql.append(" where funcionariocargo = ? and (datainicio between ? and ? or datafinal between ? and ?)");
		
		if(consideraAfastamentoPrevidencia) {
			sql.append(" and (tipoafastamento like 'LICENCA_SEM_VENCIMENTO' or tipoafastamento like 'OUTROS' or tipoafastamento like 'AFASTAMENTO_PREVIDENCIA') ");
		} else {
			sql.append(" and (tipoafastamento like 'LICENCA_SEM_VENCIMENTO' or tipoafastamento like 'OUTROS') ");
		}
		
		sql.append(" order by datainicio asc ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo(), inicioPeriodo, finalPeriodo, inicioPeriodo, finalPeriodo);
		
		Long qtdDiasAfastados = 0l;
		
		if(tabelaResultado.next()) {
			
			do {
				Date dataInicio = tabelaResultado.getDate("datainicio");
				Date dataFinal = tabelaResultado.getDate("datafinal");
			
				if(dataInicio.before(inicioPeriodo))
					dataInicio = inicioPeriodo;
				
				if(dataFinal == null || dataFinal.before(finalPeriodo))
					dataFinal = finalPeriodo;
				
				qtdDiasAfastados = UteisData.getCalculaDiferencaEmDias(dataInicio, dataFinal) + 1;	
			} while (tabelaResultado.next());
			
		}
		
		return qtdDiasAfastados.intValue();
	}

}
