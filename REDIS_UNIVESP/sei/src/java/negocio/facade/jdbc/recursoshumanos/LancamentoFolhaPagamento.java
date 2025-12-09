package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ControleLancamentoFolhapagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.recursoshumanos.LancamentoFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>LancamentoFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>LancamentoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class LancamentoFolhaPagamento extends ControleAcesso implements LancamentoFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public LancamentoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("LancamentoFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void persistir(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		validarDados(lancamentoFolhaPagamento);
		validarDadosDuplicadosDataCompetenciaPeriodo(lancamentoFolhaPagamento);
		
		getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().persistir(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento(), false, usuarioVO);
		
		if (lancamentoFolhaPagamento.getCodigo() == null || lancamentoFolhaPagamento.getCodigo() == 0) {
			incluir(lancamentoFolhaPagamento, validarAcesso, usuarioVO);
		} else {
			alterar(lancamentoFolhaPagamento, validarAcesso, usuarioVO);
		}
		
		persistirEventosVinculadosAoTemplate(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento(), usuarioVO);
	}

	private void persistirEventosVinculadosAoTemplate(TemplateLancamentoFolhaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		excluirEventosQueNaoEstaoNaLista(obj);
		for (TemplateEventoFolhaPagamentoVO templateEvento : obj.getListaEventosDoTemplate()) {
			try {
				templateEvento.setTemplateLancamentoFolhaPagamentoVO(obj);
				getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().incluir(templateEvento, Boolean.FALSE, usuarioVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void excluirEventosQueNaoEstaoNaLista(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO) throws Exception {
		try {
			StringBuilder str = new StringBuilder("DELETE FROM templateeventofolhapagamento WHERE templateLancamentoFolhaPagamento = ? ");
			getConexao().getJdbcTemplate().update(str.toString(), templateLancamentoFolhaPagamentoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(LancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LancamentoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder("UPDATE LancamentoFolhaPagamento set ")
							.append(" datacompetencia=?, periodo=?, grupoLancamentoFolhaPagamento = ?, templatelancamentofolhapagamento = ?, rascunho = ?, ")
							.append(" ativo = ? WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(Uteis.getDataJDBC(obj.getDataCompetencia()), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPeriodo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getGrupoLancamentoFolhaPagamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRascunho(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAtivo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo().intValue(), ++i, sqlAlterar);
					
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public void incluir(LancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LancamentoFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					
					StringBuilder sql = new StringBuilder(" INSERT INTO LancamentoFolhaPagamento( ")
					.append(" datacompetencia, periodo, templatelancamentofolhapagamento, competenciaFolhaPagamento, ativo, ")
					.append(" rascunho, grupolancamentofolhapagamento )")
					.append(" VALUES (?, ?, ?, ?, ?, ")
					.append(" ?, ? ) returning codigo")
					.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(Uteis.getDataJDBC(obj.getDataCompetencia()), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPeriodo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCompetenciaFolhaPagamentoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAtivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRascunho(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getGrupoLancamentoFolhaPagamento(), ++i, sqlInserir);
					
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
	
	@Override
	public List<LancamentoFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta,Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSQLSelectSqlBasico());
		sql.append(" where 1 = 1");
		sql.append(" AND tipotemplatefolhapagamento = 'LANCAMENTO'");

		if (Uteis.isAtributoPreenchido(valorConsulta)) {
			switch (campoConsulta) {
			case "DataCompetencia":
				sql.append(" AND upper( pessoa.nome ) ilike(sem_acentos(?)) ");
				break;
			default:
				break;
			}
		}

		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataIni, dataFim, "lancamentofolhapagamento.dataCompetencia", true));
		sql.append(" ORDER BY lancamentofolhapagamento.codigo DESC");

		SqlRowSet tabelaResultado;
		if (Uteis.isAtributoPreenchido(valorConsulta)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), String.format("%%%s%%", valorConsulta.toUpperCase()) );
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}

		List<LancamentoFolhaPagamentoVO> tabelaReferenciaFolhasPagamentos = new ArrayList<>();
		while(tabelaResultado.next()) {
			tabelaReferenciaFolhasPagamentos.add(montarDados(tabelaResultado));
		}
		return tabelaReferenciaFolhasPagamentos;
	}

	private LancamentoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado) throws Exception {
		
		LancamentoFolhaPagamentoVO obj = new LancamentoFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataCompetencia(tabelaResultado.getDate("datacompetencia"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getLong("periodo"))) {
			obj.setPeriodo(getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("periodo")));
		}
		obj.setRascunho(tabelaResultado.getBoolean("rascunho"));
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getLong("competenciaFolhaPagamento"))) {
			obj.setCompetenciaFolhaPagamentoVO(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("competenciaFolhaPagamento")));
		}
		obj.setAtivo(tabelaResultado.getBoolean("ativo"));
		
		obj.setTemplateLancamentoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("templatelancamentofolhapagamento"), TemplateLancamentoFolhaPagamentoVO.class, p -> getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_TODOS)));
		
		obj.getGrupoLancamentoFolhaPagamento().setCodigo(tabelaResultado.getInt("grupolancamentofolhapagamento.codigo"));
		
		return obj;
	}

	private Object getSQLSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT lancamentofolhapagamento.*, grupolancamentofolhapagamento.* , lancamentofolhapagamento.codigo as \"lancamentofolhapagamento.codigo\" , grupolancamentofolhapagamento.codigo as \"grupolancamentofolhapagamento.codigo\", pessoa.nome as \"pessoa.nome\", templateLancamentoFolhaPagamento.* FROM lancamentofolhapagamento");
		sql.append(" INNER JOIN competenciaFolhaPagamento ON competenciaFolhaPagamento.codigo = lancamentofolhapagamento.competenciaFolhaPagamento");
		sql.append(" LEFT JOIN grupolancamentofolhapagamento ON lancamentofolhapagamento.grupolancamentofolhapagamento = grupolancamentofolhapagamento.codigo");
		sql.append(" LEFT JOIN templateLancamentoFolhaPagamento ON lancamentofolhapagamento.templateLancamentoFolhaPagamento = templateLancamentoFolhaPagamento.codigo");
		sql.append(" LEFT JOIN funcionario on templateLancamentoFolhaPagamento.funcionario = funcionario.codigo");
		sql.append(" LEFT JOIN pessoa on funcionario.pessoa = pessoa.codigo");
		return sql.toString();
	}

	public void validarDadosDuplicadosDataCompetenciaPeriodo(LancamentoFolhaPagamentoVO obj) throws ConsistirException {
		int total = consultarDadosTotalPorDataCompetenciaPeriodo(obj);

		if (total > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_duplicado"));
		}
	}

	public int consultarDadosTotalPorDataCompetenciaPeriodo(LancamentoFolhaPagamentoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM lancamentofolhapagamento");
		sql.append(" WHERE periodo = ?");
		sql.append(" AND EXTRACT('Month' FROM dataCompetencia) = ").append(String.valueOf(UteisData.getMesData(obj.getDataCompetencia())));

		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" AND codigo != ?");
		}

		SqlRowSet rs = null;
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getPeriodo().getCodigo(), obj.getCodigo());
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getPeriodo().getCodigo());
		}

        if (rs.next()) {
            return rs.getInt("qtde");
        }

    	return 0;
	}

	@Override
	public LancamentoFolhaPagamentoVO consultarPorChavePrimaria(Long codigo, NivelMontarDados nivelMontarDados) throws Exception {
		String sql = " SELECT * FROM lancamentofolhapagamento WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
        if (rs.next()) {
            return montarDados(rs);
        }
        throw new Exception("Dados não encontrados (Lançamento Folha de Pagamento).");
	}
	
	public void validarDados(LancamentoFolhaPagamentoVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getPeriodo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CompetenciaFolhaPagamento_periodo"));
		}		
	}

	@Override
	public void encerrarVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {		
		StringBuilder sql = new StringBuilder("update LancamentoFolhaPagamento set ativo = false ").append(" where competenciafolhapagamento = ? ");
		getConexao().getJdbcTemplate().update(sql.toString(), competenciaFolhaPagamentoVO.getCodigo());
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LancamentoFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		String sql = " SELECT * FROM lancamentofolhapagamento WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
        if (rs.next()) {
            return montarDados(rs);
        }
        throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
	}

	@Override
	public LancamentoFolhaPagamentoVO consultarPorContraCheque(ContraChequeVO contraCheque, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT lancamentofolhapagamento.*, templatelancamentofolhapagamento.*, grupolancamentofolhapagamento.codigo as \"grupolancamentofolhapagamento.codigo\" FROM lancamentofolhapagamento");
		sql.append(" LEFT JOIN contracheque ON contracheque.lancamentofolhapagamento = lancamentofolhapagamento.codigo");
		sql.append(" LEFT JOIN templatelancamentofolhapagamento ON lancamentofolhapagamento.templatelancamentofolhapagamento = lancamentofolhapagamento.codigo");
		sql.append(" LEFT JOIN grupolancamentofolhapagamento ON lancamentofolhapagamento.grupolancamentofolhapagamento = grupolancamentofolhapagamento.codigo");
		sql.append(" WHERE contracheque.codigo = ? limit 1");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraCheque.getCodigo());
        if (rs.next()) {
            return montarDados(rs);
        }
        throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
	}

	@Override
	public LancamentoFolhaPagamentoVO consultarPorTemplate(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT lancamentofolhapagamento.*, grupolancamentofolhapagamento.*, grupolancamentofolhapagamento.codigo as \"grupolancamentofolhapagamento.codigo\" FROM lancamentofolhapagamento");
		sql.append(" LEFT JOIN templatelancamentofolhapagamento ON lancamentofolhapagamento.templatelancamentofolhapagamento = lancamentofolhapagamento.codigo");
		sql.append(" LEFT JOIN grupolancamentofolhapagamento ON lancamentofolhapagamento.grupolancamentofolhapagamento = grupolancamentofolhapagamento.codigo");
		sql.append(" WHERE templatelancamentofolhapagamento.codigo = ? limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), templateLancamentoFolhaPagamentoVO.getCodigo());
		if (rs.next()) {
			return montarDados(rs);
		}
		return new LancamentoFolhaPagamentoVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void validarDadosGerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento, List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamento,  UsuarioVO usuarioLogado) throws Exception {
		montarDadosGerarFolhaPagamento(lancamento, listaTemplateEventoFolhaPagamento);
		validarFiltrosSelecionadosParaLancamento(lancamento);
		validarCompetenciaDoLancamentoAtiva(lancamento);

		persistir(lancamento, false, usuarioLogado);
	}

	/**
	 * Metodo que realiza a geração da folha de pagamento.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void gerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento, List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamento,  UsuarioVO usuarioLogado) throws Exception{
		montarDadosGerarFolhaPagamento(lancamento, listaTemplateEventoFolhaPagamento);
		validarFiltrosSelecionadosParaLancamento(lancamento);
		validarCompetenciaDoLancamentoAtiva(lancamento);

		persistir(lancamento, false, usuarioLogado);
		getFacadeFactory().getContraChequeInterfaceFacade().gerarFolhaPagamento(lancamento, usuarioLogado, new ArrayList<>());		
	}

	/**
	 * Valida se a competencia vinculada ao lançamento esta Ativa.
	 * 
	 * @param lancamento
	 * @throws Exception
	 */
	private void validarCompetenciaDoLancamentoAtiva(LancamentoFolhaPagamentoVO lancamento) throws Exception {
		CompetenciaFolhaPagamentoVO competencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
		if (!competencia.getCodigo().equals(lancamento.getCompetenciaFolhaPagamentoVO().getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_erro_ContraCheque_erroAoSalvarCompetenciaInativa"));
		}
	}

	private void montarDadosGerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento,  List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamento) {
		lancamento.setRascunho(Boolean.TRUE);
		lancamento.getTemplateLancamentoFolhaPagamento().setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum.LANCAMENTO);
		lancamento.getTemplateLancamentoFolhaPagamento().setListaEventosDoTemplate(listaTemplateEventoFolhaPagamento);

		lancamento.getTemplateLancamentoFolhaPagamento().setCodigo(0);
	}

	/**
	 * Valida se o usuario selecionou ao menos uma opção de dos filtros para o lançamento.
	 * 
	 * @param lancamentoFolhaPagamento
	 * @throws ConsistirException
	 */
	private void validarFiltrosSelecionadosParaLancamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws ConsistirException {
		
		if(!Uteis.isAtributoPreenchido(lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO().getCodigo()) ||
			!Uteis.isAtributoPreenchido(lancamentoFolhaPagamento.getPeriodo().getCodigo())){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_competenciaOuPeriodoNaoInformados"));
		}

		if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarEventosFolhaNormal())
			if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarEventosGrupo())
				if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarEventosFuncionario())
					if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarSalarioComposto())
						if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarEmprestimos())
							if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarValeTransporte())
								if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarFerias())
									if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarAdiantamentoFerias())
										if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancar13Parcela1())
											if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancar13Parcela2())
												if(!lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getLancarRescisao())
													throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_nenhumaOpcaoSelecionada"));

		String filtros = getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento());

    	if(filtros.trim().length() <= 0) {
    		throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_nenhumFuncionarioSelecionado"));
    	}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void cancelarFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado) throws Exception {
		ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO = getFacadeFactory().getControleLancamentoFolhapagamentoInterfaceFacade().consultarPorFuncionarioCargoCompetenciaEPeriodo(
				lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO(), lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento.getPeriodo());

		if (Uteis.isAtributoPreenchido(controleLancamentoFolhapagamentoVO)) {			
			getFacadeFactory().getContaPagarFacade().validarSeContaPagarExisteVinculoComArquivoRemessa(controleLancamentoFolhapagamentoVO.getContaPagarVO());
		}
		
		persistir(lancamentoFolhaPagamento, false, usuarioLogado);

		ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();
		getFacadeFactory().getContraChequeInterfaceFacade().cancelarFolhaDePagamento(lancamentoFolhaPagamento, usuarioLogado, engine);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		LancamentoFolhaPagamento.idEntidade = idEntidade;
	}
}