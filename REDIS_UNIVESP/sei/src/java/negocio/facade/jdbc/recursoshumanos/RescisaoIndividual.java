package negocio.facade.jdbc.recursoshumanos;

import java.util.ArrayList;
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
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.RescisaoIndividualVO;
import negocio.comuns.recursoshumanos.RescisaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.RescisaoIndividualInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>RescisaoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>RescisaoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class RescisaoIndividual  extends SuperFacade<RescisaoIndividualVO> implements RescisaoIndividualInterfaceFacade<RescisaoIndividualVO> {

	private static final long serialVersionUID = -7013691319262859944L;

	private static final String NOME_TABELA = "rescisaoindividual";

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void persistir(RescisaoIndividualVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(RescisaoIndividualVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(obj, NOME_TABELA, new AtributoPersistencia()
				.add("rescisao", obj.getRescisao().getCodigo() == 0 ? null : obj.getRescisao().getCodigo())
				.add("funcionariocargo", obj.getFuncionarioCargo().getCodigo() == 0 ? null : obj.getFuncionarioCargo().getCodigo())
				.add("historicosituacao", obj.getHistoricoSituacao().getCodigo() == 0 ? null : obj.getHistoricoSituacao().getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	public void alterar(RescisaoIndividualVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(obj, NOME_TABELA, new AtributoPersistencia()
				.add("rescisao", obj.getRescisao().getCodigo() == 0 ? null : obj.getRescisao().getCodigo())
				.add("funcionariocargo", obj.getFuncionarioCargo().getCodigo() == 0 ? null : obj.getFuncionarioCargo().getCodigo())
				.add("historicosituacao", obj.getHistoricoSituacao().getCodigo() == 0 ? null : obj.getHistoricoSituacao().getCodigo()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	public void excluir(RescisaoIndividualVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM rescisaoindividual WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public void validarDados(RescisaoIndividualVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getRescisao())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}

		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarRescisaoIndividual(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalRescisaoIndividual(dataModelo));
	}

	private Integer consultarTotalRescisaoIndividual(DataModelo dataModelo) {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlTotal());
		sql.append(" where rescisaoindividual.rescisao = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.parseInt(dataModelo.getValorConsulta()));
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private Integer consultarTotalRescisaoIndividualPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(rescisaoindividual.codigo) as qtde from rescisaoindividual ");
		sql.append(" inner join rescisao on rescisaoindividual.rescisao = rescisao.codigo");
		sql.append(" where rescisaoindividual.funcionariocargo = ? and rescisao.competenciafolhapagamento = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo(), competenciaFolhaPagamentoVO.getCodigo());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<RescisaoIndividualVO> consultarRescisaoIndividual(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSql());
		sql.append(" where rescisaoindividual.rescisao = ? order by funcionariocargo.formacontratacao asc ");

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.parseInt(dataModelo.getValorConsulta()));
		
		return montarDadosLista(tabelaResultado);
	}

	@Override
	public RescisaoIndividualVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public RescisaoIndividualVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		RescisaoIndividualVO obj = new RescisaoIndividualVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("rescisao"))) {
			obj.setRescisao(getFacadeFactory().getRescisaoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("rescisao")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("historicosituacao"))) {
			obj.setHistoricoSituacao(getFacadeFactory().getHistoricoSituacaoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("historicosituacao")));
		}

		return obj;
	}

	/**
	 * Monta a lista de {@link RescisaoIndividualVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<RescisaoIndividualVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<RescisaoIndividualVO> rescisoes = new ArrayList<>();

        while(tabelaResultado.next()) {
        	rescisoes.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return rescisoes;
	}
	
	@Override
	public RescisaoIndividualVO consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(getSql());
        sql.append(" where rescisaoindividual.funcionariocargo = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo());

        try {
        	if(tabelaResultado.next())
        		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return new RescisaoIndividualVO();
	}

	@Override
	public List<RescisaoIndividualVO> consultarPorRescisao(RescisaoVO rescisaoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSql());
		sql.append(" where rescisaoindividual.rescisao = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), rescisaoVO.getCodigo());
		
		return montarDadosLista(tabelaResultado);
	}

	@Override
	public List<RescisaoIndividualVO> consultarPorFormaContratacaoFuncionario(RescisaoVO rescisaoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSql());
		List<Object> lista = new ArrayList<>();
		sql.append(" where funcionariocargo.formacontratacao " + realizarGeracaoIn(rescisaoVO.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";").length));
		sql.append(" and funcionariocargo.tiporecebimento " + realizarGeracaoIn(rescisaoVO.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";").length));

		gerarDadosFiltroIn(lista, rescisaoVO.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";"));
		gerarDadosFiltroIn(lista, rescisaoVO.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";"));

		sql.append(" and rescisaoindividual.rescisao = ?");
		lista.add(rescisaoVO.getCodigo());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), lista.toArray());

		return montarDadosLista(tabelaResultado);
	}
	
	private void gerarDadosFiltroIn(List<Object> lista, String[] valores ) {
		for (String valor : valores) {
			lista.add(valor);
		}
	}

	/**
	 * Recupera os funcionarios cargo pela rescisão informado.
	 * 
	 * @param rescisaoVO
	 * @return Retorna uma lista de codigos dos funcionario do tipo List<Integer>
	 */
	@Override
	public List<Integer> consultarFuncionariosCargoPorRescisao(RescisaoVO rescisaoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT funcionariocargo FROM rescisaoindividual WHERE rescisao = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), rescisaoVO.getCodigo());
		
		List<Integer> idsFuncionariosCargo = new ArrayList<>();
		while (tabelaResultado.next()) {
			idsFuncionariosCargo.add(tabelaResultado.getInt("funcionariocargo"));
		}
		return idsFuncionariosCargo;
	}

	/**
	 * Regra dee negocio que valida se existe contra cheque para o funcionario cargo
	 * na data de competencia informada.
	 */
	@Override
	public boolean validarSeExisteRescisaoParaFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception {
		int quantidadeContraChequeFuncionario = consultarTotalRescisaoIndividualPorFuncionarioCargo(funcionarioCargoVO, competenciaFolhaPagamentoVO); 

		if (quantidadeContraChequeFuncionario > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT * FROM rescicaoindividual");
		return sql.toString();
	}
	
	private String getSql() {
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT rescisaoindividual.codigo, rescisaoindividual.funcionariocargo, rescisaoindividual.rescisao, rescisaoindividual.historicosituacao FROM rescisaoindividual");
		sql.append(" INNER JOIN funcionariocargo ON rescisaoindividual.funcionariocargo = funcionariocargo.codigo");
		sql.append(" INNER JOIN rescisao ON rescisaoindividual.rescisao = rescisao.codigo");
		return sql.toString();
	}
	
	@Override
	public RescisaoIndividualVO consultarPorFuncionarioCargoECompetencia(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		StringBuilder sql = new StringBuilder();
		sql.append(getSql());
        sql.append(" where rescisaoindividual.funcionariocargo = ?");
        sql.append(" and rescisao.competenciaFolhaPagamento = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargo.getCodigo(), competenciaFolhaPagamento.getCodigo());

        try {
        	if(tabelaResultado.next())
        		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return new RescisaoIndividualVO();
	}

	private String getSqlTotal() {
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT count(rescisaoindividual.codigo) as qtde FROM rescisaoindividual");
		sql.append(" INNER JOIN funcionariocargo ON rescisaoindividual.funcionariocargo = funcionariocargo.codigo");
		sql.append(" INNER JOIN rescisao ON rescisaoindividual.rescisao = rescisao.codigo");
		return sql.toString();
	}

	/**
	 * Monta os dados pelas entidades {@link RescisaoVO} , {@link FuncionarioCargoVO}, {@link HistoricoSituacaoVO} infomadas.
	 */
	@Override
	public RescisaoIndividualVO montarDados(RescisaoVO rescisaoVO, FuncionarioCargoVO funcionarioCargoVO, HistoricoSituacaoVO historicoSituacaoVO) {
		RescisaoIndividualVO rescisaoIndividualVO = new RescisaoIndividualVO();
		rescisaoIndividualVO.setFuncionarioCargo(funcionarioCargoVO);
		rescisaoIndividualVO.setRescisao(rescisaoVO);
		rescisaoIndividualVO.setHistoricoSituacao(historicoSituacaoVO);
		return rescisaoIndividualVO;
	}}
