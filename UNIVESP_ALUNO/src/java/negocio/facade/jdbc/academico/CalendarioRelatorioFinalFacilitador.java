package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CalendarioRelatorioFinalFacilitadorInterfaceFacade;

@Repository
@Lazy
public class CalendarioRelatorioFinalFacilitador extends ControleAcesso implements CalendarioRelatorioFinalFacilitadorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7006155065140841092L;
	protected static String idEntidade;
	private static final String nomeTabela = "CalendarioRelatorioFinalFacilitador";	


	public CalendarioRelatorioFinalFacilitador() throws Exception {
		super();
		setIdEntidade("CalendarioRelatorioFinalFacilitador");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(calendarioRelatorioFinalFacilitadorVO);
		validarUnicidade(calendarioRelatorioFinalFacilitadorVO, usuarioVO);
		if (calendarioRelatorioFinalFacilitadorVO.getNovoObj()) {
			incluir(calendarioRelatorioFinalFacilitadorVO, usuarioVO);
		} else {
			alterar(calendarioRelatorioFinalFacilitadorVO, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM CalendarioRelatorioFinalFacilitador where codigo = " + calendarioRelatorioFinalFacilitadorVO.getCodigo()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}


	private void validarUnicidade(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioRelatorioFinalFacilitadorVO obj = consultarPorRelatorioFacilitadoresRegistroUnico(calendarioRelatorioFinalFacilitadorVO.getDisciplinaVO().getCodigo(), "", calendarioRelatorioFinalFacilitadorVO.getAno(), 
				calendarioRelatorioFinalFacilitadorVO.getSemestre(), calendarioRelatorioFinalFacilitadorVO.getMes(),  false, usuarioVO);
		if (obj != null && obj.getCodigo() > 0 && !obj.getCodigo().equals(calendarioRelatorioFinalFacilitadorVO.getCodigo())) {
			throw new Exception("Já existe um CALENDÁRIO RELATÓRIO DE FACILITADORES já cadastrado com estas configurações, favor consultar e alterar o mesmo.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {		
		incluir(calendarioRelatorioFinalFacilitadorVO, nomeTabela, new AtributoPersistencia()
				.add("disciplina", calendarioRelatorioFinalFacilitadorVO.getDisciplinaVO().getCodigo())
				.add("dataInicio", calendarioRelatorioFinalFacilitadorVO.getDataInicio())
				.add("dataFim", calendarioRelatorioFinalFacilitadorVO.getDataFim())
				.add("mes", calendarioRelatorioFinalFacilitadorVO.getMes())
				.add("ano", calendarioRelatorioFinalFacilitadorVO.getAno())
				.add("semestre", calendarioRelatorioFinalFacilitadorVO.getSemestre())
				.add("questionario", calendarioRelatorioFinalFacilitadorVO.getQuestionarioVO().getCodigo())
				.add("variaveltiponota", calendarioRelatorioFinalFacilitadorVO.getVariavelTipoNota())
				, usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		alterar(calendarioRelatorioFinalFacilitadorVO, nomeTabela, new AtributoPersistencia()
				.add("disciplina", calendarioRelatorioFinalFacilitadorVO.getDisciplinaVO().getCodigo())
				.add("dataInicio", calendarioRelatorioFinalFacilitadorVO.getDataInicio())
				.add("dataFim", calendarioRelatorioFinalFacilitadorVO.getDataFim())
				.add("mes", calendarioRelatorioFinalFacilitadorVO.getMes())
				.add("ano", calendarioRelatorioFinalFacilitadorVO.getAno())
				.add("semestre", calendarioRelatorioFinalFacilitadorVO.getSemestre())
				.add("questionario", calendarioRelatorioFinalFacilitadorVO.getQuestionarioVO().getCodigo())
				.add("variaveltiponota", calendarioRelatorioFinalFacilitadorVO.getVariavelTipoNota()),
				new AtributoPersistencia().add("codigo", calendarioRelatorioFinalFacilitadorVO.getCodigo()),
				usuarioVO);

	}

	@Override
	public void validarDados(CalendarioRelatorioFinalFacilitadorVO obj) throws Exception {
		ConsistirException ex = new ConsistirException();
		if (obj.getDisciplinaVO().getCodigo() == null || obj.getDisciplinaVO().getCodigo() == 0) {
			ex.adicionarListaMensagemErro("O campo Disciplina deve ser informado.");
		}
		if (obj.getAno() == null || obj.getAno().isEmpty()) {
			ex.adicionarListaMensagemErro("O campo Ano deve ser informado.");
		}
		if (obj.getSemestre() == null || obj.getSemestre().isEmpty()) {
			ex.adicionarListaMensagemErro("O campo Semestre deve ser informado.");
		}
		if (obj.getMes() == null || obj.getMes().isEmpty()) {
			ex.adicionarListaMensagemErro("O campo Mês deve ser informado.");
		}
		if (obj.getDataInicio() == null) {
			ex.adicionarListaMensagemErro("O campo Data Inicio deve ser informado.");
		}
		if (obj.getDataFim() == null) {
			ex.adicionarListaMensagemErro("O campo Data Final deve ser informado.");
		}
		if(Uteis.isAtributoPreenchido(obj.getDataInicio()) && Uteis.isAtributoPreenchido(obj.getDataFim())) {
			if(UteisData.validarDataInicialMaiorFinal(obj.getDataInicio(), obj.getDataFim()) ){
				ex.adicionarListaMensagemErro("O campo Período Fim deve ser maior que o campo Período Inicial do Caledário.");
			}
		}
		if (obj.getQuestionarioVO().getCodigo() == null || obj.getQuestionarioVO().getCodigo() == 0) {
			ex.adicionarListaMensagemErro("O campo Questionário deve ser informado.");
		}
		if (obj.getVariavelTipoNota() == null || obj.getVariavelTipoNota().isEmpty()) {
			ex.adicionarListaMensagemErro("O campo Nota deve ser informado.");
		}
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		} else {
			ex = null;
		}
	}

	public String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT CalendarioRelatorioFinalFacilitador.*, disciplina.nome as \"disciplina.nome\", questionario.descricao as \"questionario.descricao\" ");
		sql.append(" FROM CalendarioRelatorioFinalFacilitador  ");
		sql.append(" inner join disciplina on disciplina.codigo = CalendarioRelatorioFinalFacilitador.disciplina ");
		sql.append(" inner join questionario on questionario.codigo = CalendarioRelatorioFinalFacilitador.questionario ");
		return sql.toString();
	}

	@Override
	public CalendarioRelatorioFinalFacilitadorVO consultarPorRelatorioFacilitadoresRegistroUnico(Integer disciplina, String situacao, String ano, String semestre, String mes, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(mes)) {
			sql.append(" and mes = '").append(mes).append("' ");
		}
		if(!situacao.contentEquals("")){
			if(situacao.contentEquals("Encerrado")) {
				sql.append(" and datafim < '").append(UteisData.getDataComHoraAtual()).append("' ");
			}
			if(situacao.contentEquals("Em aberto")) {
				sql.append(" and '").append(UteisData.getDataComHoraAtual()).append("' >= datainicio and '")
				.append(UteisData.getDataComHoraAtual()).append("' <= datafim ");
			}
			if(situacao.contentEquals("Aguardando Prazo")) {
				sql.append(" and dataInicio > '").append(UteisData.getDataComHoraAtual()).append("' ");
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return null;
	}

	@Override
	public List<CalendarioRelatorioFinalFacilitadorVO> consultar(Integer disciplina, String situacao, String ano, String semestre, String mes, Date dataInicial, Date dataFinal, Boolean validarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(mes)) {
			sql.append(" and mes = '").append(mes).append("' ");
		}
		if (dataInicial != null) {
			sql.append(" and datainicio >= " + "'" + (Uteis.getDataJDBCTimestamp(dataInicial)) + "'");
		}

		if (dataFinal != null) {
			sql.append(" and dataFim <= " + "'" + (Uteis.getDataJDBCTimestamp(dataFinal)) + "'");
		}
		if(!situacao.contentEquals("")){
			if(situacao.contentEquals("Encerrado")) {
				sql.append(" and datafim < '").append(UteisData.getDataComHoraAtual()).append("' ");
			}
			if(situacao.contentEquals("Em aberto")) {
				sql.append(" and '").append(UteisData.getDataComHoraAtual()).append("' >= datainicio and '")
				.append(UteisData.getDataJDBCTimestamp(new Date())).append("' <= datafim ");
			}
			if(situacao.contentEquals("Aguardando Prazo")) {
				sql.append(" and dataInicio > '").append(UteisData.getDataComHoraAtual()).append("' ");
			}
		}
		sql.append(" order by CalendarioRelatorioFinalFacilitador.codigo desc ");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		System.out.println(sql.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, usuarioVO);
	}

	@Override
	public Integer consultarTotalRegistro(Integer disciplina, String situacao, String ano, String semestre, String mes, Date dataInicial, Date dataFinal) throws Exception {

		StringBuilder sql = new StringBuilder(" select count(CalendarioRelatorioFinalFacilitador.codigo) as qtde from CalendarioRelatorioFinalFacilitador ");
		sql.append(" inner join disciplina on disciplina.codigo = CalendarioRelatorioFinalFacilitador.disciplina ");
		sql.append(" inner join questionario on questionario.codigo = CalendarioRelatorioFinalFacilitador.questionario ");
		sql.append(" WHERE 1=1  ");
		if (disciplina != null && disciplina > 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(mes)) {
			sql.append(" and mes = '").append(mes).append("' ");
		}
		if (dataInicial != null) {
			sql.append(" and datainicio >= " + "'" + (Uteis.getDataJDBCTimestamp(dataInicial)) + "'");
		}

		if (dataFinal != null) {
			sql.append(" and dataFim <= " + "'" + (Uteis.getDataJDBCTimestamp(dataFinal)) + "'");
		}
		if(!situacao.contentEquals("")){
			if(situacao.contentEquals("Encerrado")) {
				sql.append(" and datafim < '").append(UteisData.getDataComHoraAtual()).append("' ");
			}
			if(situacao.contentEquals("Em aberto")) {
				sql.append(" and '").append(UteisData.getDataComHoraAtual()).append("' >= datainicio and '")
				.append(UteisData.getDataJDBCTimestamp(new Date())).append("' <= datafim ");
			}
			if(situacao.contentEquals("Aguardando Prazo")) {
				sql.append(" and dataInicio > '").append(UteisData.getDataComHoraAtual()).append("' ");
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	private List<CalendarioRelatorioFinalFacilitadorVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<CalendarioRelatorioFinalFacilitadorVO> CalendarioRelatorioFinalFacilitador = new ArrayList<CalendarioRelatorioFinalFacilitadorVO>(0);
		while (rs.next()) {
			CalendarioRelatorioFinalFacilitador.add(montarDados(rs, usuario));
		}
		return CalendarioRelatorioFinalFacilitador;
	}

	private CalendarioRelatorioFinalFacilitadorVO montarDados(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		CalendarioRelatorioFinalFacilitadorVO obj = new CalendarioRelatorioFinalFacilitadorVO();
		obj.setCodigo(rs.getInt("codigo"));		
		obj.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		obj.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		obj.getQuestionarioVO().setCodigo(rs.getInt("questionario"));
		obj.getQuestionarioVO().setDescricao(rs.getString("questionario.descricao"));
		obj.setAno(rs.getString("ano"));
		obj.setSemestre(rs.getString("semestre"));
		obj.setMes(rs.getString("mes"));
		obj.setDataInicio(rs.getDate("dataInicio"));
		obj.setDataFim(rs.getDate("dataFim"));
		obj.setVariavelTipoNota(rs.getString("variaveltiponota"));
		if(obj.getDataFim().before(UteisData.getDataJDBC(new Date()))) {
			obj.setSituacao("Encerrado");	
		} if(UteisData.getDataJDBC(new Date()).before(obj.getDataFim()) && UteisData.getDataJDBC(new Date()).after(obj.getDataInicio())){
			obj.setSituacao("Em aberto");	
		} 
		if(obj.getDataInicio().after(UteisData.getDataJDBC(new Date()))){
			obj.setSituacao("Aguardando Prazo");	
		}
		obj.setNovoObj(false);
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CalendarioRelatorioFinalFacilitador.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CalendarioRelatorioFinalFacilitador.idEntidade = idEntidade;
	}
	
	@Override
	public Boolean verificarCalendarioEmAbertoPorDisciplinaAnoSemestre(DisciplinaVO disciplinaVO, String ano, String semestre, Date data){
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		if (Uteis.isAtributoPreenchido(disciplinaVO.getCodigo())) {
			sql.append(" and disciplina.codigo = ").append(disciplinaVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(data)) {
			sql.append(" and '").append(UteisData.getDataComHora(data)).append("' >= datainicio and '")
			.append(UteisData.getDataComHora(data)).append("' <= datafim ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	
	} 
	
	@Override
	public List<CalendarioRelatorioFinalFacilitadorVO> consultarPorAnoSemestreMesData(String ano, String semestre, String mes, Date dataInicial, Date dataFinal, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(mes)) {
			sql.append(" and mes = '").append(mes).append("' ");
		}
		if (dataInicial != null) {
			sql.append(" and datainicio::date = " + "'" + (Uteis.getDataJDBC(dataInicial)) + "'");
		}
		if (dataFinal != null) {
			sql.append(" and dataFim::date = " + "'" + (Uteis.getDataJDBC(dataFinal)) + "'");
		}
		sql.append(" order by CalendarioRelatorioFinalFacilitador.codigo desc ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, usuarioVO);
	}
}
