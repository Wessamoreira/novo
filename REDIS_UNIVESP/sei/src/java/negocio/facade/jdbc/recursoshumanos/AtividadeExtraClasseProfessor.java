package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
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
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorCursoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO.EnumCampoConsultaHoraAtividadeExtraClasseProfessor;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.AtividadeExtraClasseProfessorInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>AtividadeExtraClasseProfessorVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>AtividadeExtraClasseProfessorVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class AtividadeExtraClasseProfessor extends SuperFacade<AtividadeExtraClasseProfessorVO> implements AtividadeExtraClasseProfessorInterfaceFacade<AtividadeExtraClasseProfessorVO> {

	private static final long serialVersionUID = 3293411158333469691L;
	
	protected static String idEntidade;

	@PostConstruct
	public void init() {
		setIdEntidade("AtividadeExtraClasseProfessor");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(AtividadeExtraClasseProfessorVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessor.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "AtividadeExtraClasseProfessor",
				new AtributoPersistencia().add("funcionarioCargo", obj.getFuncionarioCargo().getCodigo())
						.add("data", obj.getData())
						.add("dataCadastro", obj.getDataCadastro())
						.add("horaPrevista", obj.getHoraPrevista())
						.add("datalimiteregistro", obj.getDataLimiteRegistro())
						.add("datalimiteaprovacao", obj.getDataLimiteAprovacao())
						.add("usuarioResponsavel", obj.getUsuarioResponsavel()),
				usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
		
		if (Uteis.isAtributoPreenchido(obj.getAtividadeExtraClasseProfessorCursoVOs())) {
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().excluirAtividadeExtraClasseProfessorCurso(obj, usuarioVO);
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().persistirTodos(obj.getAtividadeExtraClasseProfessorCursoVOs(), false, usuarioVO, obj);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(AtividadeExtraClasseProfessorVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessor.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "AtividadeExtraClasseProfessor",
				new AtributoPersistencia().add("funcionarioCargo", obj.getFuncionarioCargo().getCodigo())
						.add("data", obj.getData())
						.add("dataCadastro", obj.getDataCadastro())
						.add("horaPrevista", obj.getHoraPrevista())
						.add("datalimiteregistro", obj.getDataLimiteRegistro())
						.add("datalimiteaprovacao", obj.getDataLimiteAprovacao())
						.add("usuarioResponsavel", obj.getUsuarioResponsavel()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
		
		if (Uteis.isAtributoPreenchido(obj.getAtividadeExtraClasseProfessorCursoVOs())) {
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().excluirAtividadeExtraClasseProfessorCurso(obj, usuarioVO);
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().persistirTodos(obj.getAtividadeExtraClasseProfessorCursoVOs(), false, usuarioVO, obj);
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHoraPrevista(Integer codigo, Integer horaPrevista) throws Exception {
		final String sql = "UPDATE atividadeextraclasseprofessor set horaprevista=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, horaPrevista);
                sqlAlterar.setInt(2, codigo);
                return sqlAlterar;
            }
        });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.NEVER)
	public void excluirTodos(List<AtividadeExtraClasseProfessorVO> atividadeExtraClasseProfessorVOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessor.excluir(getIdEntidade(), validarAcesso, usuarioVO);

		StringBuilder mensagem = new StringBuilder();

		Iterator<AtividadeExtraClasseProfessorVO> iterator = atividadeExtraClasseProfessorVOs.iterator();
		AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO = new AtividadeExtraClasseProfessorVO();
		while(iterator.hasNext()) {
			try {
				atividadeExtraClasseProfessorVO = iterator.next();
				this.excluir(atividadeExtraClasseProfessorVO, true, usuarioVO);
				iterator.remove();
			} catch (Exception e) {
				mensagem.append("<br/> - " + UteisData.obterDataFormatoTexto_MM_yyyy(atividadeExtraClasseProfessorVO.getData()));
			}
		}

		if (Uteis.isAtributoPreenchido(mensagem.toString())) {			
			throw new ConsistirException("Não foi possivel excluir as Atividades para os respectivos meses: " + mensagem.toString());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AtividadeExtraClasseProfessorVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessor.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		
		int quantidadeAtividade = getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarQuantidadePorAtividadeExtraClasseProfessor(obj);
		if (quantidadeAtividade > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AtividadeExtraClasseProfessor_erroRemoverAtividade").replace("{0}", UteisData.obterDataFormatoTexto_MM_yyyy(obj.getData())));
		}

		getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().excluirPorAtividadeExtraClasse(obj, usuarioVO);

		StringBuilder sql = new StringBuilder("DELETE FROM AtividadeExtraClasseProfessor WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public AtividadeExtraClasseProfessorVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM AtividadeExtraClasseProfessor WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public AtividadeExtraClasseProfessorVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		AtividadeExtraClasseProfessorVO obj = new AtividadeExtraClasseProfessorVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setData(tabelaResultado.getDate("data"));
		obj.setDataCadastro(tabelaResultado.getDate("dataCadastro"));
		obj.setHoraPrevista(tabelaResultado.getInt("horaPrevista"));
		obj.setDataLimiteRegistro(tabelaResultado.getDate("datalimiteregistro"));
		obj.setDataLimiteAprovacao(tabelaResultado.getDate("datalimiteaprovacao"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("usuarioResponsavel"))) {	
			obj.setUsuarioResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorCodigoUsuario(tabelaResultado.getInt("usuarioResponsavel"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null));
		}
		
		if (Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS== nivelMontarDados) {
			obj.setAtividadeExtraClasseProfessorCursoVOs(
					getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(obj, null) );
		}

		if (Uteis.NIVELMONTARDADOS_TODOS == nivelMontarDados) {
			obj.setTotalHorasAguardandoAprovacao(tabelaResultado.getInt("totalAguardandoAprovacao"));
			obj.setTotalHorasAprovadas(tabelaResultado.getInt("totalAprovado"));
			obj.setTotalHorasIndeferidas(tabelaResultado.getInt("totalIndeferido"));

			obj.setAtividadeExtraClasseProfessorCursoVOs(
					getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(obj, null) );
		}

		return obj;
	}

	@Override
	public void validarDados(AtividadeExtraClasseProfessorVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AfastamentoFuncionario_funcionarioCargo"));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AtividadeExtraClasseProfessorVO> validarDadosPeriodo(Date dataInicio, Date dataFinal, Integer horasPrevistas, List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor) throws Exception {

		if (!UteisData.validarSeDataInicioMenorQueDataFinalDesconsiderandoDias(dataInicio, dataFinal)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dataFimMenorDataInicio"));
		}
		
		List<FeriadoVO> feriados = getFacadeFactory().getFeriadoFacade().consultarPorPeriodo(dataInicio, dataFinal, 
				"NENHUM", null, false, 1, null);

		List<AtividadeExtraClasseProfessorVO> lista = new ArrayList<>();
		if (Uteis.isAtributoPreenchido(listaAtividadeExtraClasseProfessor)) {
			lista.addAll(listaAtividadeExtraClasseProfessor);
		}
		int diferenca =  UteisData.calcularQuantidadeMesesEntreDatas(dataInicio, dataFinal) + 1;

		for (int contador = 0; contador < diferenca; contador++) {
			boolean atividadeExtraClasseAdicionada = true;
			AtividadeExtraClasseProfessorVO obj = new AtividadeExtraClasseProfessorVO();
			obj.setData(UteisData.calcularAnoPelaQuantidadeMeses(dataInicio, contador));
			obj.setDataCadastro(new Date());
			obj.setHoraPrevista(horasPrevistas);

			obj.setDataLimiteAprovacao(UteisData.quintoDiaUtilProximoMes(obj.getData(), feriados));
			obj.setDataLimiteRegistro(UteisData.getUltimaDataMes(obj.getData()));

			ComparatorDate comparatorDate = new ComparatorDate();
			
			//Valida se a Data já foi adicionada a lista
			for (AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO : listaAtividadeExtraClasseProfessor) {
				if (comparatorDate.compare(atividadeExtraClasseProfessorVO.getData(), obj.getData()) == 0) {
					atividadeExtraClasseAdicionada = false;
					break;
				}
			}
			if (atividadeExtraClasseAdicionada) {
				lista.add(obj);
			}
		}
		
		lista.sort(Comparator.comparing(AtividadeExtraClasseProfessorVO::getData));

		return lista;
	}
	
	public class ComparatorDate implements Comparator<Date> {
	    private DateFormat dataFormat = new SimpleDateFormat("MM/yyyy");

	    public int compare(Date d1, Date d2) {
	        return dataFormat.format(d1).compareTo(dataFormat.format(d2));
	    }
	}

	/**
	 * consulta todos as {@link AtividadeExtraClasseProfessorVO} referente ao 
	 * codigo do funcionario cargo informado.
	 * 
	 *  @param Código do {@link FuncionarioCargoVO}
	 * @throws Exception 
	 */
	@Override
	public List<AtividadeExtraClasseProfessorVO> consultarAtividadeExtraClassePorFuncionarioCargo(Integer codigoFuncionarioCargo, Date dataInicio, Date dataFim) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT atividadeextraclasseprofessor.codigo, funcionariocargo, data, horaprevista, datacadastro, usuarioresponsavel, datalimiteregistro, datalimiteaprovacao, ");
		sql.append("	(select sum(atividadeextraclasseprofessorpostado.horasRealizada) as totalAguardandoAprovacao from atividadeextraclasseprofessorpostado  ");
		sql.append("		where situacao = 'AGUARDANDO_APROVACAO'  and atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo ), ");
		sql.append("	(select sum(atividadeextraclasseprofessorpostado.horasRealizada) as totalAprovado from atividadeextraclasseprofessorpostado  ");
		sql.append("		where situacao = 'APROVADO'  and atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo ), ");
		sql.append("	(select sum(atividadeextraclasseprofessorpostado.horasRealizada) as totalIndeferido from atividadeextraclasseprofessorpostado  ");
		sql.append("		where situacao = 'INDEFERIDO'  and atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo ) ");
		sql.append(" FROM atividadeextraclasseprofessor");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessor.funcionariocargo");
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" WHERE funcionariocargo.codigo = ?");

		sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataInicio, dataFim, "data"));
		sql.append(" order by data");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionarioCargo);

		return montarDadosLista(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
	}

	@Override
	public void persistir(AtividadeExtraClasseProfessorVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void persistirTodos(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor, FuncionarioCargoVO funcionarioCargo, boolean b, UsuarioVO usuarioVO) throws Exception {
		for (AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO : listaAtividadeExtraClasseProfessor) {
			atividadeExtraClasseProfessorVO.setFuncionarioCargo(funcionarioCargo);
			atividadeExtraClasseProfessorVO.setUsuarioResponsavel(usuarioVO);
			persistir(atividadeExtraClasseProfessorVO, false, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarAtividadeExtraClasseProfessor(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalAtividadeExtraClasseProfessor(dataModelo));		
	}

	/**
	 * Consulta Paginada dos {@link AtividadeExtraClasseProfessorVO} retornando 10 registros por pagina.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<AtividadeExtraClasseProfessorVO> consultarAtividadeExtraClasseProfessor(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");

		switch (EnumCampoConsultaHoraAtividadeExtraClasseProfessor.valueOf(dataModelo.getCampoConsulta())) {
		case FUNCIONARIO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_CARGO:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta());
			sql.append(" AND funcionariocargo.matriculacargo = ?");
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

		return montarDadosLista(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
	}

	/**
	 * Consulta o total de {@link HistoricoAfastamentoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalAtividadeExtraClasseProfessor(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaHoraAtividadeExtraClasseProfessor.valueOf(dataModelo.getCampoConsulta())) {
		
		case FUNCIONARIO:
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_CARGO:
			sql.append(" AND funcionariocargo.matriculacargo = ?");
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
	 * Monta a lista de {@link AtividadeExtraClasseProfessorVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<AtividadeExtraClasseProfessorVO> montarDadosLista(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<AtividadeExtraClasseProfessorVO> listaHoraAtividadeExtraClasseProfessor = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaHoraAtividadeExtraClasseProfessor.add(montarDados(tabelaResultado, nivelMontarDados));
        }
		return listaHoraAtividadeExtraClasseProfessor;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT atividadeextraclasseprofessor.codigo, funcionariocargo, data, horaprevista, datacadastro, usuarioresponsavel, datalimiteregistro, datalimiteaprovacao FROM atividadeextraclasseprofessor");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessor.funcionariocargo");
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(atividadeextraclasseprofessor.codigo) as qtde FROM atividadeextraclasseprofessor");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessor.funcionariocargo");
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");

		return sql.toString();
	}

	@Override
	public AtividadeExtraClasseProfessorVO consultarPorMesAnoDataAtividade(Integer codigoFuncionarioCargo, Date dataAtividade) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from atividadeextraclasseprofessor ");
		sql.append(" where extract('Month' from data) = ").append(UteisData.getMesData(dataAtividade));
		sql.append(" and extract('YEAR' from data) = ").append(UteisData.getAnoData(dataAtividade));
		sql.append(" and funcionariocargo = ? ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionarioCargo);

		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS);
		} else {
			return new AtividadeExtraClasseProfessorVO(); 
		}
	}

	@Override
	public void atualizarValorHoraPrevista(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor) {
		
		for (AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO : listaAtividadeExtraClasseProfessor) {
			if (Uteis.isAtributoPreenchido(atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs())) {
				atividadeExtraClasseProfessorVO.setHoraPrevista(
					atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs().stream().mapToInt(AtividadeExtraClasseProfessorCursoVO::getHoraPrevista).sum());
			} else {
				atividadeExtraClasseProfessorVO.setHoraPrevista(0);
			}
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		AtividadeExtraClasseProfessor.idEntidade = idEntidade;
	}

}
