package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorPostadoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.AtividadeExtraClasseProfessorPostadoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>AtividadeExtraClasseProfessorPostadoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>AtividadeExtraClasseProfessorPostadoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class AtividadeExtraClasseProfessorPostado extends SuperFacade<AtividadeExtraClasseProfessorPostadoVO> 
	implements AtividadeExtraClasseProfessorPostadoInterfaceFacade<AtividadeExtraClasseProfessorPostadoVO> {

	private static final long serialVersionUID = 3293411158333469691L;

	protected static String idEntidade;

	@PostConstruct
	public void init() {
		setIdEntidade("AtividadeExtraClasseProfessorPostado");
	}

	@Override
	public void persistir(AtividadeExtraClasseProfessorPostadoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (Uteis.isAtributoPreenchido(obj.getArquivo().getNome())) {
			getFacadeFactory().getArquivoFacade().incluir(obj.getArquivo(), usuarioVO, usuarioVO.getConfiguracaoGeralSistemaVO());
		}
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, true, usuarioVO);
		} else {
			alterar(obj, true, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void incluir(AtividadeExtraClasseProfessorPostadoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessorPostado.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "AtividadeExtraClasseProfessorPostado",
				new AtributoPersistencia().add("funcionarioCargo", obj.getFuncionarioCargo().getCodigo())
						.add("dataatividade", Uteis.getDataJDBCTimestamp(obj.getDataAtividade()))
						.add("dataCadastro", Uteis.getDataJDBCTimestamp(obj.getDataCadastro()))
						.add("descricao", obj.getDescricao())
						.add("horasrealizada", obj.getHorasRealizada())
						.add("arquivo", obj.getArquivo())
						.add("situacao", obj.getSituacaoHoraAtividadeExtraClasseEnum())
						.add("atividadeextraclasseprofessor", obj.getAtividadeExtraClasseProfessorVO())
						.add("motivoindeferimento", obj.getMotivoIndeferimento())
						.add("dataAprovacao", obj.getDataAprovacao())
						.add("dataIndeferimento", obj.getDataIndeferimento())
						.add("realizadoDownloadArquivo", obj.getRealizadoDownloadArquivo())
						.add("curso", obj.getCursoVO())
						.add("log", obj.getLog()),
				usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void alterar(AtividadeExtraClasseProfessorPostadoVO obj, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
		AtividadeExtraClasseProfessorPostado.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "AtividadeExtraClasseProfessorPostado",
				new AtributoPersistencia().add("funcionarioCargo", obj.getFuncionarioCargo().getCodigo())
						.add("dataatividade", Uteis.getDataJDBCTimestamp(obj.getDataAtividade()))
						.add("dataCadastro", Uteis.getDataJDBCTimestamp(obj.getDataCadastro()))
						.add("descricao", obj.getDescricao())
						.add("horasrealizada", obj.getHorasRealizada())
						.add("arquivo", obj.getArquivo())
						.add("situacao", obj.getSituacaoHoraAtividadeExtraClasseEnum())
						.add("atividadeextraclasseprofessor", obj.getAtividadeExtraClasseProfessorVO())
						.add("motivoindeferimento", obj.getMotivoIndeferimento())
						.add("dataAprovacao", obj.getDataAprovacao())
						.add("dataIndeferimento", obj.getDataIndeferimento())
						.add("realizadoDownloadArquivo", obj.getRealizadoDownloadArquivo())
						.add("curso", obj.getCursoVO())
						.add("log", obj.getLog()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void excluir(AtividadeExtraClasseProfessorPostadoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessorPostado.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM AtividadeExtraClasseProfessorPostado WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public AtividadeExtraClasseProfessorPostadoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM AtividadeExtraClasseProfessorPostado WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public AtividadeExtraClasseProfessorPostadoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		AtividadeExtraClasseProfessorPostadoVO obj = new AtividadeExtraClasseProfessorPostadoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataAtividade(tabelaResultado.getDate("dataatividade"));
		obj.setDataCadastro(tabelaResultado.getDate("dataCadastro"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setHorasRealizada(tabelaResultado.getInt("horasrealizada"));
		obj.setMotivoIndeferimento(tabelaResultado.getString("motivoindeferimento"));
		obj.setDataAprovacao(tabelaResultado.getDate("dataAprovacao"));
		obj.setDataIndeferimento(tabelaResultado.getDate("dataIndeferimento"));
		obj.setRealizadoDownloadArquivo(tabelaResultado.getBoolean("realizadoDownloadArquivo"));
		obj.setLog(tabelaResultado.getString("log"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacaoHoraAtividadeExtraClasseEnum(SituacaoHoraAtividadeExtraClasseEnum.valueOf(tabelaResultado.getString("situacao")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("arquivo"))) {	
			obj.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("arquivo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("atividadeextraclasseprofessor"))) {	
			obj.setAtividadeExtraClasseProfessorVO(getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("atividadeextraclasseprofessor")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("responsavel"))) {
			obj.setUsuarioResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("responsavel"), nivelMontarDados, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("curso"))) {
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("curso"), nivelMontarDados, false, null));
		}

		return obj;
	}

	@Override
	public void validarDados(AtividadeExtraClasseProfessorPostadoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getCursoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_curso"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataAtividade())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_dataAtividade"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getHorasRealizada())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_horaRealizada"));
		}

		if (UteisData.getPrimeiraDataMaior(obj.getDataAtividade(), new Date())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_dataAtividadeMairoQueDataAtual"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getAtividadeExtraClasseProfessorVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_vinculada"));
		}

		Integer totalAtividadeExtraClasse = consultarQuantidadeHorasAprovadaeAguardandoAprovacao(obj.getCodigo(), obj.getFuncionarioCargo().getCodigo(), obj.getDataAtividade(), obj.getCursoVO().getCodigo());
		
		int totalHorasRealizadasCurso = obj.getAtividadeExtraClasseProfessorVO().getAtividadeExtraClasseProfessorCursoVOs().stream()
				.filter(p -> p.getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo()))
				.map(t -> t.getHoraPrevista()).mapToInt(Integer::new).sum();
		if ( totalHorasRealizadasCurso < (totalAtividadeExtraClasse + obj.getHorasRealizada())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_horasPrevistasEstourada")
					.replace("{0}", String.valueOf(totalHorasRealizadasCurso))
					.replace("{1}", String.valueOf(totalAtividadeExtraClasse + obj.getHorasRealizada()))); 
		}
		
		try {
			if (UteisData.getCompareDataComHora(new Date(), obj.getAtividadeExtraClasseProfessorVO().getDataLimiteRegistro()) == 1) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_validarDataLimitePostagem")
						.replace("{0}", UteisData.getDataComHoraMinutoSegundo(obj.getAtividadeExtraClasseProfessorVO().getDataLimiteRegistro())));
			}
		} catch (ParseException e) {
			throw new ConsistirException(e.getMessage());
		}
	}

	public Integer consultarQuantidadeHorasAprovadaeAguardandoAprovacao(Integer codigo, Integer codigoFuncionarioCargo, Date dataAtividade, int curso) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(horasrealizada) as total from atividadeextraclasseprofessorpostado where funcionariocargo = ?");
		sql.append(" and ( extract('MONTH' from dataatividade) = ").append(UteisData.getMesData(dataAtividade));
		sql.append(" and extract('YEAR' from dataatividade) = ").append(UteisData.getAnoData(dataAtividade)).append(")");
		sql.append(" and situacao != ? and atividadeextraclasseprofessorpostado.curso = ?");
		if (Uteis.isAtributoPreenchido(codigo)) {
			sql.append(" and codigo != ").append(codigo);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionarioCargo, SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO.name(), curso);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "total", TipoCampoEnum.INTEIRO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.setListaConsulta(consultarHoraAtividadeExtraClasseProfessor(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHoraAtividadeExtraClasseProfessor(dataModelo));		
	}

	/**
	 * Consulta Paginada dos {@link AtividadeExtraClasseProfessorPostadoVO} retornando 10 registros por pagina.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<AtividadeExtraClasseProfessorPostadoVO> consultarHoraAtividadeExtraClasseProfessor(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		sql.append(" and funcionariocargo.codigo = ?");

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado);
	}

	@Override
	public Integer consultarQuantidadePorAtividadeExtraClasseProfessor(AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(codigo) as qtde FROM atividadeextraclasseprofessorpostado");
		sql.append(" WHERE atividadeextraclasseprofessor = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), atividadeExtraClasseProfessorVO.getCodigo());
		
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	/**
	 * Consulta o total de {@link HistoricoAfastamentoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHoraAtividadeExtraClasseProfessor(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");
        sql.append(" and funcionariocargo.codigo = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link AtividadeExtraClasseProfessorPostadoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<AtividadeExtraClasseProfessorPostadoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<AtividadeExtraClasseProfessorPostadoVO> listaHoraAtividadeExtraClasseProfessor = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaHoraAtividadeExtraClasseProfessor.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaHoraAtividadeExtraClasseProfessor;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT atividadeextraclasseprofessorpostado.codigo, dataatividade, datacadastro, descricao, horasrealizada, arquivo, situacao, funcionariocargo, atividadeextraclasseprofessor, motivoindeferimento, dataAprovacao, dataIndeferimento, responsavel, realizadoDownloadArquivo, log FROM atividadeextraclasseprofessorpostado");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessorpostado.funcionariocargo");
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(atividadeextraclasseprofessorpostado.codigo) as qtde FROM atividadeextraclasseprofessorpostado");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessorpostado.funcionariocargo");
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");

		return sql.toString();
	}

	public List<AtividadeExtraClasseProfessorPostadoVO> consultarAtividadeExtraClasseProfessorPostadoPorFuncionario(DataModelo dataModelo , 
			Integer codigoFuncionario, boolean visaoCoordenador, String situacao, Integer codigoCurso, UsuarioVO usuario) {
		dataModelo.setListaFiltros(new ArrayList<>());
		StringBuilder sql = new StringBuilder();
		sql.append(" select dataatividade, horaprevista, atividadeextraclasseprofessor.funcionariocargo, pessoa.nome, curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", log, ");
		sql.append(" (select horaprevista from atividadeextraclasseprofessorcurso where atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo and curso = curso.codigo) as horaprevistacurso, ");
		sql.append(" (select sum(atividadePostada.horasrealizada) from atividadeextraclasseprofessorpostado atividadePostada");
		sql.append(" where atividadePostada.situacao = '").append(SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO.name()).append("'");
		sql.append(" and atividadePostada.atividadeextraclasseprofessor = atividadeextraclasseprofessor");
		sql.append(" and funcionariocargo = atividadeextraclasseprofessor.funcionariocargo");
		sql.append(" and (extract('MONTH' from atividadeextraclasseprofessorpostado.dataatividade) = extract('MONTH' from atividadePostada.dataatividade )");
		sql.append(" and  extract('YEAR' from atividadeextraclasseprofessorpostado.dataatividade) = extract('YEAR' from atividadePostada.dataatividade )) and atividadePostada.curso = curso.codigo) as horasaguardandoaprovacao,");

		sql.append(" (select sum(atividadePostada.horasrealizada) from atividadeextraclasseprofessorpostado atividadePostada");
		sql.append(" where atividadePostada.situacao = '").append(SituacaoHoraAtividadeExtraClasseEnum.APROVADO.name()).append("'");
		sql.append(" and atividadePostada.atividadeextraclasseprofessor = atividadeextraclasseprofessor");
		sql.append(" and funcionariocargo = atividadeextraclasseprofessor.funcionariocargo");
		sql.append(" and (extract('MONTH' from atividadeextraclasseprofessorpostado.dataatividade) = extract('MONTH' from atividadePostada.dataatividade )");
		sql.append(" and extract('YEAR' from atividadeextraclasseprofessorpostado.dataatividade) = extract('YEAR' from atividadePostada.dataatividade )) and atividadePostada.curso = curso.codigo) as horasaprovada,");

		sql.append(" (select sum(atividadePostada.horasrealizada) from atividadeextraclasseprofessorpostado atividadePostada");
		sql.append(" where atividadePostada.situacao = '").append(SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO.name()).append("'");
		sql.append(" and atividadePostada.atividadeextraclasseprofessor = atividadeextraclasseprofessor");
		sql.append(" and funcionariocargo = atividadeextraclasseprofessor.funcionariocargo");
		sql.append(" and (extract('MONTH' from atividadeextraclasseprofessorpostado.dataatividade) = extract('MONTH' from atividadePostada.dataatividade )");
		sql.append(" and extract('YEAR' from atividadeextraclasseprofessorpostado.dataatividade) = extract('YEAR' from atividadePostada.dataatividade )) and atividadePostada.curso = curso.codigo) as horasindeferidas");

		sql.append(" from atividadeextraclasseprofessorpostado");
		sql.append(" inner join curso on atividadeextraclasseprofessorpostado.curso = curso.codigo");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessorpostado.funcionariocargo");
		if (usuario.getIsApresentarVisaoCoordenador()) {
			sql.append(" inner join curso as curso2 on funcionariocargo.curso = curso2.codigo");
		}
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" inner join atividadeextraclasseprofessor on atividadeextraclasseprofessor.codigo = atividadeextraclasseprofessorpostado.atividadeextraclasseprofessor");
		sql.append(" where 1 = 1 ");

		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "dataatividade", true));

		if (Uteis.isAtributoPreenchido(codigoFuncionario)) {
			sql.append(" and funcionariocargo.codigo  = ?");
			dataModelo.getListaFiltros().add(codigoFuncionario);
		}

		if (Uteis.isAtributoPreenchido(codigoCurso)) {
			sql.append(" and curso.codigo = ?");
			dataModelo.getListaFiltros().add(codigoCurso);
		}

		if (Uteis.isAtributoPreenchido(situacao)) {
			sql.append(" and atividadeextraclasseprofessorpostado.situacao = ?");
			dataModelo.getListaFiltros().add(situacao);
		}

		sql.append(" group by dataatividade, horaprevista, atividadeextraclasseprofessor.funcionariocargo, pessoa.nome, curso.codigo, curso.nome, log, atividadeextraclasseprofessor.codigo");
		sql.append(" order by dataatividade, curso.nome, pessoa.nome desc");

		//UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		List<AtividadeExtraClasseProfessorPostadoVO> lista = new ArrayList<>();

		while (tabelaResultado.next()) {
			boolean atividaAdicionada = false;
			for (AtividadeExtraClasseProfessorPostadoVO obj : lista) {
				if (obj.getCursoVO().getCodigo().equals(tabelaResultado.getInt("curso.codigo")) 
						&& obj.getFuncionarioCargo().getCodigo().equals(tabelaResultado.getInt("funcionariocargo"))
						&& ( UteisData.getMesData(obj.getDataAtividade()) ==  UteisData.getMesData(tabelaResultado.getDate("dataatividade")) && 
						  UteisData.getAnoData(obj.getDataAtividade()) ==  UteisData.getAnoData(tabelaResultado.getDate("dataatividade")))) {
							atividaAdicionada = true;
							break;
				}
			}

			if (!atividaAdicionada) {
				AtividadeExtraClasseProfessorPostadoVO obj = new AtividadeExtraClasseProfessorPostadoVO();
				obj.setDataAtividade(tabelaResultado.getDate("dataatividade"));
				obj.getAtividadeExtraClasseProfessorVO().setHoraPrevista(tabelaResultado.getInt("horaprevistacurso"));
				obj.setHorasAguardandoAprovacao(tabelaResultado.getInt("horasaguardandoaprovacao"));
				obj.setHorasAprovada(tabelaResultado.getInt("horasaprovada"));
				obj.setHorasIndeferidas(tabelaResultado.getInt("horasindeferidas"));
				obj.getFuncionarioCargo().getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("nome"));
				obj.getFuncionarioCargo().setCodigo(tabelaResultado.getInt("funcionariocargo"));
				obj.getCursoVO().setCodigo(tabelaResultado.getInt("curso.codigo"));
				obj.getCursoVO().setNome(tabelaResultado.getString("curso.nome"));
				obj.setLog(tabelaResultado.getString("log"));

				lista.add(obj);
			}
		}

		return lista;
	}

	public Integer consultarTotalAtividadeExtraClasseProfessorPostadoPorFuncionario(DataModelo dataModelo ,Integer codigoFuncionario, String situacao, int codigoCurso, UsuarioVO usuarioVO) throws Exception {
		dataModelo.setListaFiltros(new ArrayList<>());
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(atividadeextraclasseprofessorpostado.codigo) as qtde ");
        sql.append(" from atividadeextraclasseprofessorpostado");
        sql.append(" inner join curso on atividadeextraclasseprofessorpostado.curso = curso.codigo");
        sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessorpostado.funcionariocargo");
        if (usuarioVO.getIsApresentarVisaoCoordenador()) {
        	sql.append(" inner join curso as curso2 on funcionariocargo.curso = curso2.codigo");
        }
		sql.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" inner join atividadeextraclasseprofessor on atividadeextraclasseprofessor.codigo = atividadeextraclasseprofessorpostado.atividadeextraclasseprofessor");
		sql.append(" where 1 = 1 ");

		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "dataatividade", true));

		if (Uteis.isAtributoPreenchido(codigoFuncionario)) {			
			sql.append(" and funcionariocargo.codigo  = ?");
			dataModelo.getListaFiltros().add(codigoFuncionario);
		}

		if (Uteis.isAtributoPreenchido(codigoCurso)) {
			sql.append(" and curso.codigo = ?");
			dataModelo.getListaFiltros().add(codigoCurso);
		}

		if (Uteis.isAtributoPreenchido(situacao)) {
			sql.append(" and situacao  = ?");
			dataModelo.getListaFiltros().add(situacao);
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Consulta o total de {@link AtividadeExtraClasseProfessorPostadoVO} pela situação e 
	 * pela para extraindo o Mes e Ano da data.
	 */
	@Override
	public Integer consultarTotalAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade, Integer codigoFuncionarioCargo, CursoVO cursoVO) throws Exception {
		List<Object> filtros = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(atividadeextraclasseprofessorpostado.horasRealizada) as qtde from atividadeextraclasseprofessorpostado where situacao = '").append(situacao.name()).append("' ");
		sql.append(" and ").append(UteisData.getMesData(dataAtividade));
		sql.append(" = extract('MONTH' from dataatividade) ");		
		sql.append(" and ").append(UteisData.getAnoData(dataAtividade));
		sql.append(" = extract('YEAR' from dataatividade )");
		sql.append(" and funcionariocargo = ?");
		filtros.add(codigoFuncionarioCargo);

		if (Uteis.isAtributoPreenchido(cursoVO)) {
			sql.append(" and atividadeextraclasseprofessorpostado.curso = ?");
			filtros.add(cursoVO.getCodigo());
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());

		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	@Override
	public List<AtividadeExtraClasseProfessorPostadoVO> consultarAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade, Integer codigoFuncionarioCargo, CursoVO cursoVO) throws Exception {
		List<Object> filtros = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from atividadeextraclasseprofessorpostado where situacao = '").append(situacao.name()).append("' ");
		sql.append(" and ").append(UteisData.getMesData(dataAtividade));
		sql.append(" = extract('MONTH' from dataatividade) ");		
		sql.append(" and ").append(UteisData.getAnoData(dataAtividade));
		sql.append(" = extract('YEAR' from dataatividade )");
		sql.append(" and funcionariocargo = ?");
		filtros.add(codigoFuncionarioCargo);

		if (Uteis.isAtributoPreenchido(cursoVO)) {
			sql.append(" and curso = ?");
			filtros.add(cursoVO.getCodigo());
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());

		return montarDadosLista(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void uploadDocumento(FileUploadEvent upload, AtividadeExtraClasseProfessorPostadoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ArquivoVO arquivo = new ArquivoVO();
			arquivo.setNome(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(upload.getUploadedFile().getName())));
			arquivo.setExtensao(ArquivoHelper.getExtensaoArquivo(upload.getUploadedFile().getName()));
			arquivo.setDescricao(upload.getUploadedFile().getName());
			arquivo.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR_TMP);
			arquivo.setPastaBaseArquivo(PastaBaseArquivoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR_TMP.getValue());
			arquivo.setOrigem(OrigemArquivo.ATIVIDADE_EXTRA_CLASSE_PROFESSOR.getValor());
			arquivo.setDataDisponibilizacao(new Date());
			arquivo.setDataUpload(new Date());
			arquivo.setManterDisponibilizacao(true);
			arquivo.setProfessor(obj.getAtividadeExtraClasseProfessorVO().getUsuarioResponsavel().getPessoa());
			arquivo.setResponsavelUpload(usuarioVO);
			arquivo.setSituacao(SituacaoArquivo.ATIVO.getValor());
			ArquivoHelper.salvarArquivoNaPastaTemp(upload, arquivo.getNome(), arquivo.getPastaBaseArquivo(), configuracaoGeralSistemaVO, usuarioVO);
			obj.setArquivo(arquivo);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<AtividadeExtraClasseProfessorPostadoVO> consultarAtividadeExraClasseRelatorio(Date dataInicio,Date dataFinal, FuncionarioCargoVO funcionarioCargoVO, String[] situacoes) throws Exception {
		List<Object> listaFiltros = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append(" select atividadepostada.codigo, atividadepostada.dataatividade , atividadepostada.datacadastro, atividadepostada.descricao,");
		sql.append(" atividadepostada.horasrealizada, atividadepostada.situacao, atividadepostada.arquivo, atividadepostada.funcionariocargo, atividadepostada.atividadeextraclasseprofessor, atividadepostada.motivoindeferimento,");
		sql.append(" atividadepostada.dataaprovacao, atividadepostada.dataIndeferimento, pessoa.nome");
		sql.append(" from atividadeextraclasseprofessorpostado as atividadepostada");
		sql.append(" inner join atividadeextraclasseprofessor on atividadeextraclasseprofessor.codigo = atividadepostada.atividadeextraclasseprofessor");
		sql.append(" inner join funcionariocargo on atividadepostada.funcionariocargo = funcionariocargo.codigo");
		sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" where 1 = 1 ");

		if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
			sql.append(" and atividadepostada.funcionariocargo = ?");
			listaFiltros.add(funcionarioCargoVO.getCodigo());
		}

		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFinal, "atividadepostada.dataatividade", false));

		if (situacoes != null && situacoes.length > 0 ) {
			sql.append(" and atividadepostada.situacao  ").append(realizarGeracaoIn(situacoes.length));
			for (String situacao : situacoes) {
				listaFiltros.add(situacao);
			}
		}
		sql.append(" order by nome, situacao");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltros.toArray());

		return montarDadosLista(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void aprovarAtividadeExtraClasseProfessor(AtividadeExtraClasseProfessorPostadoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {

			validarDadosRegrasAtividadesExtraClasseProfessor(obj);

			StringBuilder sql = new StringBuilder("update AtividadeExtraClasseProfessorPostado set situacao = ?, motivoindeferimento = ?,"
					+ " dataAprovacao = ?, dataIndeferimento = ?, responsavel = ?, log = ? where codigo = ? ");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;

					Uteis.setValuePreparedStatement(obj.getSituacaoHoraAtividadeExtraClasseEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getMotivoIndeferimento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataAprovacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataIndeferimento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUsuarioResponsavel(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getLog(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarDadosRegrasAtividadesExtraClasseProfessor(AtividadeExtraClasseProfessorPostadoVO obj)
			throws ConsistirException, ParseException {
		if (Uteis.isAtributoPreenchido(obj.getArquivo()) && obj.getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.APROVADO)) {
			if (!obj.getRealizadoDownloadArquivo()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_realizarDownload"));
			}
		}

		if (UteisData.getCompareDataComHora(obj.getAtividadeExtraClasseProfessorVO().getDataLimiteAprovacao(), new Date()).equals(-1)) {
			throw new ConsistirException(
					UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_dataAtualMaiorQueDataLimite").replace("{0}", UteisData.getDataAplicandoFormatacao(obj.getAtividadeExtraClasseProfessorVO().getDataLimiteAprovacao(), "dd/MM/yyyy HH:mm:ss")));
		}

		Integer totalAtividadeExtraClasse = consultarQuantidadeHorasAprovadaeAguardandoAprovacao(obj.getCodigo(), obj.getFuncionarioCargo().getCodigo(), obj.getDataAtividade(), obj.getCursoVO().getCodigo());
		if (obj.getAtividadeExtraClasseProfessorVO().getHoraPrevista() < totalAtividadeExtraClasse + obj.getHorasRealizada()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_horasPrevistasEstourada")
					.replace("{0}", obj.getAtividadeExtraClasseProfessorVO().getHoraPrevista().toString())
					.replace("{1}", String.valueOf(totalAtividadeExtraClasse))); 
		}
		
		if (obj.getSituacaoHoraAtividadeExtraClasseEnum().equals(SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO) && 
				!Uteis.isAtributoPreenchido(obj.getMotivoIndeferimento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_motivoIndeferimento"));
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		AtividadeExtraClasseProfessorPostado.idEntidade = idEntidade;
	}
}