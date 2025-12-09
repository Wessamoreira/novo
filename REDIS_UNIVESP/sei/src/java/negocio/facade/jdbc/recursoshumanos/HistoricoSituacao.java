package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO.EnumCampoConsultaHistoricoSituacao;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCargoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.HistoricoSituacaoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoDependentesVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoDependentesVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoSituacao extends SuperFacade<HistoricoSituacaoVO> implements HistoricoSituacaoInterfaceFacade<HistoricoSituacaoVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public HistoricoSituacao() throws Exception {
		super();
		setIdEntidade("HistoricoSituacao");
	}

	public HistoricoSituacao(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("HistoricoSituacao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoSituacaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(HistoricoSituacaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			HistoricoSituacao.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.historicosituacao(funcionariocargo, datamudanca, motivomudanca, situacao)");
					sql.append(" VALUES (?, ?, ?, ?) returning codigo");
					sql.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMotivoMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlInserir);

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
	public void alterar(HistoricoSituacaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoSituacao.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.historicosituacao funcionariocargo=?, datamudanca=?, motivomudanca=?, situacao=?");
				sql.append(" WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMotivoMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void persistirPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso) throws Exception {
		if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getHistoricoSituacaoVO().getMotivoMudanca()) 
				&& funcionarioCargoVO.getHistoricoSituacaoVO().getGerarHistorico()) {
			HistoricoSituacaoVO obj = this.montarDadosPorFuncionarioCargo(funcionarioCargoVO);
			persistir(obj, validarAcesso, null);
		}
	}

	private HistoricoSituacaoVO montarDadosPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) {
		HistoricoSituacaoVO obj = new HistoricoSituacaoVO(); 
		obj.setFuncionarioCargo(funcionarioCargoVO);
        obj.setDataMudanca(new Date());
        obj.setSituacao(SituacaoFuncionarioEnum.valueOf(funcionarioCargoVO.getSituacaoFuncionario()));

        if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getHistoricoSituacaoVO().getMotivoMudanca())) {
        	obj.setMotivoMudanca(funcionarioCargoVO.getHistoricoSituacaoVO().getMotivoMudanca());
        } else {
        	obj.setMotivoMudanca(MotivoMudancaCargoEnum.ADMISSAO);
		}

		return obj;
	}

	/**
	 * Exclui o {@link HistoricoSituacaoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoSituacaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoSituacao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoSituacao WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());

	}

	/**
	 * Consulta o {@link HistoricoSituacaoVO} pelo  codigo informado.
	 */
	@Override
	public HistoricoSituacaoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE hist.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link HistoricoSituacaoVO}
	 */
	@Override
	public void validarDados(HistoricoSituacaoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoSituacao_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataMudanca())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoSituacao_dataMudanca"));
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarHistoricoSituacao(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoSituacao(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoSituacaoVO> consultarHistoricoSituacao(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (EnumCampoConsultaHistoricoSituacao.valueOf(dataModelo.getCampoConsulta())) {
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

		sql.append("ORDER BY datamudanca::date ASC");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Consutal os historicos das situações pela data inicio e data final da competencia, 
	 * funcionario cargo é pela situacao do funcionario.
	 * 
	 * @param dataInicial
	 * @param datafinal
	 * @param funcionarioCargoVO
	 * @param situacao - SituacaoFuncionarioEnum
	 */
	@Override
	public List<HistoricoSituacaoVO> consultaHistoricoSituacao(Date dataInicial, Date datafinal, FuncionarioCargoVO funcionarioCargoVO, String situacao) throws Exception {
		StringBuilder sql = new StringBuilder();
		List<Object> filtros = new ArrayList<>();

		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1 AND ");

		sql.append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataInicial, datafinal, "hist.datamudanca"));

		if (Uteis.isAtributoPreenchido(situacao)) {
			sql.append(" AND hist.situacao = ? ");
			filtros.add(situacao);
		}

		if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
			sql.append(" AND hist.funcionariocargo = ? ");
			filtros.add(funcionarioCargoVO.getCodigo());
		}

		sql.append(" ORDER BY pessoa.nome, hist.dataMudanca ASC ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Consulta o total de {@link HistoricoSituacaoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHistoricoSituacao(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaHistoricoSituacao.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Monta a lista de {@link HistoricoSituacaoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoSituacaoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoSituacaoVO> listaHistoricoSituacoes = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaHistoricoSituacoes.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaHistoricoSituacoes;
	}

	@Override
	public HistoricoSituacaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoSituacaoVO obj = new HistoricoSituacaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataMudanca(tabelaResultado.getDate("datamudanca"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}

		if ( Uteis.isAtributoPreenchido(tabelaResultado.getString("Situacao"))) {
			String tipoAfastamento = getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().
					validarSituacaoFuncionarioPorTipoAfastamento(tabelaResultado.getString("Situacao"));
			
			obj.setSituacao(SituacaoFuncionarioEnum.valueOf(tipoAfastamento));
			
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivoMudanca"))) {
			obj.setMotivoMudanca(MotivoMudancaCargoEnum.valueOf(tabelaResultado.getString("motivoMudanca")));
		}

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT hist.codigo, hist.funcionariocargo, pessoa.nome, fc.matriculacargo , hist.dataMudanca, hist.Situacao,");
		sql.append(" hist.motivoMudanca FROM HistoricoSituacao hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(hist.codigo) as qtde FROM HistoricoSituacao hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	@Override
	public HistoricoSituacaoVO montarDadosPorAfastamentoFuncionario(AfastamentoFuncionarioVO afastamentoFuncionarioVO, FuncionarioCargoVO funcionarioCargo) {
		HistoricoSituacaoVO historicoSituacaoVO = new HistoricoSituacaoVO();
		historicoSituacaoVO.setFuncionarioCargo(funcionarioCargo);
		historicoSituacaoVO.setDataMudanca(new Date());
		historicoSituacaoVO.setMotivoMudanca(MotivoMudancaCargoEnum.valueOf(afastamentoFuncionarioVO.getMotivoAfastamento().name()));
		String tipoAfastamento = getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().validarSituacaoFuncionarioPorTipoAfastamento(afastamentoFuncionarioVO.getTipoAfastamento().name());
		historicoSituacaoVO.setSituacao(SituacaoFuncionarioEnum.valueOf(tipoAfastamento));

		return historicoSituacaoVO;
	}

	/**
	 * Monta os dados do historico pelo funcionario cargo informado e pela situação.
	 * @param funcionarioCargo
	 * @param situacao EX: 'ATIVO', 'FERIAS' 
	 * @return
	 */
	@Override
	public HistoricoSituacaoVO montarDadosPorSituacaoFuncionario(FuncionarioCargoVO funcionarioCargo, String situacao) {
		HistoricoSituacaoVO historicoSituacaoVO = new HistoricoSituacaoVO();
		historicoSituacaoVO.setFuncionarioCargo(funcionarioCargo);
		historicoSituacaoVO.setDataMudanca(new Date());
		historicoSituacaoVO.setMotivoMudanca(MotivoMudancaCargoEnum.valueOf(situacao));
		historicoSituacaoVO.setSituacao(SituacaoFuncionarioEnum.valueOf(situacao));

		return historicoSituacaoVO;
	}

	/**
	 * Monta os dados do historico com a situação demitido e funcionario cargo informado
	 * e pela forma de contratacao do funcionario o motivo da mudança pode ser 'EXONERACAO' ou 'DEMISSAO'.
	 * 
	 * @param funcionarioCargoVO
	 */
	@Override
	public HistoricoSituacaoVO montarDadosSituacaoDemitido(FuncionarioCargoVO funcionarioCargoVO, Date dataDemissao) {
		HistoricoSituacaoVO historicoSituacaoVO = new HistoricoSituacaoVO();
		historicoSituacaoVO.setFuncionarioCargo(funcionarioCargoVO);
		historicoSituacaoVO.setDataMudanca(dataDemissao);
		historicoSituacaoVO.setSituacao(SituacaoFuncionarioEnum.DEMITIDO);
		if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getFormaContratacao()) && funcionarioCargoVO.getFormaContratacao().equals(FormaContratacaoFuncionarioEnum.ESTATUTARIO)) {
			historicoSituacaoVO.setMotivoMudanca(MotivoMudancaCargoEnum.EXONERACAO_PEDIDO);
		} else {
			historicoSituacaoVO.setMotivoMudanca(MotivoMudancaCargoEnum.DEMISSAO);
		}
		return historicoSituacaoVO;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		HistoricoSituacao.idEntidade = idEntidade;
	}
}
