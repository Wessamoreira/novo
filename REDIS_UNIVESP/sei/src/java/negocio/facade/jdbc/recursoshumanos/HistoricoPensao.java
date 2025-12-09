package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
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
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoPensaoVO;
import negocio.comuns.recursoshumanos.HistoricoPensaoVO.EnumCampoConsultaHistoricoPensao;
import negocio.comuns.recursoshumanos.enumeradores.TipoMovimentoPensaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.HistoricoPensaoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoPensaoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoPensaoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoPensao  extends SuperFacade<HistoricoPensaoVO> implements HistoricoPensaoInterfaceFacade<HistoricoPensaoVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public HistoricoPensao() throws Exception {
		super();
		setIdEntidade("HistoricoPensao");
	}

	public HistoricoPensao(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("HistoricoPensao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoPensaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(HistoricoPensaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			HistoricoPensao.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			incluir(obj, "HistoricoPensao", new AtributoPersistencia()
					.add("funcionariodependente", obj.getFuncionarioDependente())
					.add("valor", obj.getValor())
					.add("competenciaFolhaPagamento", obj.getCompetenciaFolhaPagamento())
					.add("tipoMovimentoPensao", obj.getTipoMovimentoPensao())		
					, usuarioVO);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(HistoricoPensaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoPensao.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "HistoricoPensao", new AtributoPersistencia()
				.add("funcionariodependente", obj.getFuncionarioDependente())
				.add("valor", obj.getValor())
				.add("competenciaFolhaPagamento", obj.getCompetenciaFolhaPagamento())
				.add("tipoMovimentoPensao", obj.getTipoMovimentoPensao())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	/**
	 * Exclui o {@link HistoricoPensaoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoPensaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoPensao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoPensao WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());

	}

	/**
	 * Consulta o {@link HistoricoPensaoVO} pelo  codigo informado.
	 */
	@Override
	public HistoricoPensaoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link HistoricoPensaoVO}
	 */
	@Override
	public void validarDados(HistoricoPensaoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioDependente())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoPensao_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getTipoMovimentoPensao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoPensao_tipoMovimentoPensao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getValor())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoPensao_valor"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getCompetenciaFolhaPagamento().getDataCompetencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoPensao_dataCompetencia"));
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.setListaConsulta(consultarHistoricoPensao(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoPensao(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoPensaoVO> consultarHistoricoPensao(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");

		switch (EnumCampoConsultaHistoricoPensao.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Consulta o total de {@link HistoricoPensaoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHistoricoPensao(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaHistoricoPensao.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Monta a lista de {@link HistoricoPensaoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoPensaoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoPensaoVO> historicosPensao = new ArrayList<>();

        while(tabelaResultado.next()) {
        	historicosPensao.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return historicosPensao;
	}

	@Override
	public HistoricoPensaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoPensaoVO obj = new HistoricoPensaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariodependente"))) {
			obj.setFuncionarioDependente(getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariodependente"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipomovimentopensao"))) {
			obj.setTipoMovimentoPensao(TipoMovimentoPensaoEnum.valueOf(tabelaResultado.getString("tipomovimentopensao")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getLong("competenciaFolhaPagamento"))) {
			obj.setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("competenciaFolhaPagamento")));
		}

		obj.setValor(tabelaResultado.getBigDecimal("valor"));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT hist.codigo, hist.funcionariodependente, hist.valor, hist.competenciafolhapagamento, hist.tipomovimentopensao ");
		sql.append(" FROM HistoricoPensao hist");
		sql.append(" INNER JOIN funcionariodependente ON funcionariodependente.codigo = hist.funcionariodependente");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = funcionariodependente.funcionario");
		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(hist.codigo) as qtde FROM HistoricoPensao hist");
		sql.append(" INNER JOIN funcionariodependente ON funcionariodependente.codigo = hist.funcionariodependente");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = funcionariodependente.funcionario");
		return sql.toString();
	}

	@Override
	public void gravar(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, FuncionarioDependenteVO dependentePensao, BigDecimal valorPensao, UsuarioVO usuario) throws Exception {
		HistoricoPensaoVO historicoPensaoVO = montarDadosHistoricoPensao(competenciaFolhaPagamentoVO, dependentePensao, valorPensao);

		this.persistir(historicoPensaoVO, false, usuario);
	}

	private HistoricoPensaoVO montarDadosHistoricoPensao(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, FuncionarioDependenteVO dependentePensao, BigDecimal valorPensao) {
		HistoricoPensaoVO historicoPensaoVO = new HistoricoPensaoVO();
		historicoPensaoVO.setFuncionarioDependente(dependentePensao);
		historicoPensaoVO.setTipoMovimentoPensao(TipoMovimentoPensaoEnum.MOVIMENTO);
		historicoPensaoVO.setCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
		historicoPensaoVO.setValor(valorPensao);
		return historicoPensaoVO;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		HistoricoPensao.idEntidade = idEntidade;
	}
}
