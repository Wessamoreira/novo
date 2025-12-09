package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RegistroAtividadeComplementarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroAtividadeComplementar extends ControleAcesso implements RegistroAtividadeComplementarInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public RegistroAtividadeComplementar() {
		super();
		this.setIdEntidade("RegistroAtividadeComplementar");
	}

	public RegistroAtividadeComplementarVO novo() throws Exception {
		RegistroAtividadeComplementar.incluir(RegistroAtividadeComplementar.getIdEntidade());
		return new RegistroAtividadeComplementarVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroAtividadeComplementarVO obj, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {			
			validarDados(obj);
			if(usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais()) {
				verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("AtividadeComplementarAluno_permitirIncluirAtividade", usuario);
			}else {
				incluir(RegistroAtividadeComplementar.getIdEntidade(), true, usuario);
			}
			final String sql = "INSERT INTO RegistroAtividadeComplementar( nomeEvento, instituicaoResponsavel, atividade, local, data, coordenador, dataultimaalteracao, responsavelultimaalteracao) " + "VALUES (?,?,?,?,?,?,?,?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNomeEvento());
					sqlInserir.setString(2, obj.getInstituicaoResponsavel());
					sqlInserir.setString(3, obj.getAtividade());
					sqlInserir.setString(4, obj.getLocal());
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getData()));
					if (obj.getCoordenador().equals(0)) {
						sqlInserir.setNull(6, 0);
					} else {
						sqlInserir.setInt(6, obj.getCoordenador());
					}
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(new java.util.Date()));
					sqlInserir.setInt(8, usuario.getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().incluirRegistroAtividadeComplementarMatriculaVOs(obj.getListaRegistroAtividadeComplementarMatriculaVOs(), obj.getCodigo(), usuario, configuracaoGeralSistemaVO);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RegistroAtividadeComplementarVO obj, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			validarDados(obj);
			alterar(RegistroAtividadeComplementar.getIdEntidade(), true, usuario);
			final String sql = "UPDATE RegistroAtividadeComplementar set nomeEvento = ?, instituicaoResponsavel = ? , atividade = ? , local = ? , data = ?, dataultimaalteracao=?, responsavelultimaalteracao=? where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNomeEvento());
					sqlAlterar.setString(2, obj.getInstituicaoResponsavel());
					sqlAlterar.setString(3, obj.getAtividade());
					sqlAlterar.setString(4, obj.getLocal());
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(new java.util.Date()));
					sqlAlterar.setInt(7, usuario.getCodigo());
					sqlAlterar.setInt(8, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().alterarRegistroAtividadeComplementarMatriculaVOs(obj.getListaRegistroAtividadeComplementarMatriculaVOs(), obj.getCodigo(), usuario, configuracaoGeralSistemaVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RegistroAtividadeComplementarVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().excluir(obj.getListaRegistroAtividadeComplementarMatriculaVOs().get(0).getRegistroAtividadeComplementar().getCodigo(), usuarioVO);
			excluir(RegistroAtividadeComplementar.getIdEntidade(), true ,usuarioVO);
			String sql = "DELETE FROM RegistroAtividadeComplementar where codigo=?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}

	}

	@SuppressWarnings("deprecation")
	public List<RegistroAtividadeComplementarVO> consultar(String nomeEvento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String instituicaoResponsavel, String local, String matricula, java.util.Date dataInicio, java.util.Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT registroAtividadeComplementar.* FROM RegistroAtividadeComplementar ");
		sql.append(" INNER JOIN registroatividadecomplementarmatricula on registroatividadecomplementarmatricula.registroatividadecomplementar = registroatividadecomplementar.codigo ");
		sql.append(" INNER JOIN matricula on registroatividadecomplementarmatricula.matricula = matricula.matricula");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		sql.append(" WHERE 1=1 ");
		if (nomeEvento != null && !nomeEvento.equals("")) {
			sql.append(" AND nomeEvento ilike '").append(nomeEvento).append("%' ");
		}
		if (instituicaoResponsavel != null && !instituicaoResponsavel.equals("")) {
			sql.append(" AND instituicaoResponsavel ilike '").append(instituicaoResponsavel).append("%' ");
		}
		if (local != null && !local.equals("")) {
			sql.append(" AND local ilike '").append(local).append("%' ");
		}
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND registroatividadecomplementarmatricula.matricula = '").append(matricula).append("' ");
		}
		if(Uteis.isAtributoPreenchido(dataInicio)){
			sql.append(" AND RegistroAtividadeComplementar.data >= '").append(Uteis.getDataJDBCTimestamp(dataInicio)).append("' ");
		}
		if(Uteis.isAtributoPreenchido(dataFinal)){
			sql.append(" AND RegistroAtividadeComplementar.data <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("' ");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().allMatch(p-> p.getCodigo() != 0)) {
			sql.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
		}
		if(Uteis.isAtributoPreenchido(cursoVOs) && cursoVOs.stream().allMatch(p-> p.getCodigo() != 0)) {
			sql.append(adicionarFiltroCurso(cursoVOs));
		}
		sql.append(" ORDER BY data ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}

	@SuppressWarnings("deprecation")
	public List<RegistroAtividadeComplementarVO> consultarPorCoordenador(Integer coordenador, String nomeEvento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String instituicaoResponsavel, String local, String matricula, java.util.Date dataInicio, java.util.Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT registroAtividadeComplementar.* FROM RegistroAtividadeComplementar ");
		sql.append(" INNER JOIN registroatividadecomplementarmatricula on registroatividadecomplementarmatricula.registroatividadecomplementar = registroatividadecomplementar.codigo ");
		sql.append(" INNER JOIN matricula on matricula.matricula = registroatividadecomplementarmatricula.matricula ");
		sql.append(" INNER JOIN cursocoordenador on cursocoordenador.curso = matricula.curso ");
		sql.append(" INNER JOIN funcionario on cursocoordenador.funcionario = funcionario.codigo ");
		sql.append(" AND funcionario.pessoa = ").append(coordenador).append(" and (cursocoordenador.unidadeensino = matricula.unidadeensino or cursocoordenador.unidadeensino is null) ");
		sql.append(" WHERE 1=1 ");
		if (nomeEvento != null && !nomeEvento.equals("")) {
			sql.append(" AND nomeEvento like '").append(nomeEvento).append("%' ");
		}
		if (instituicaoResponsavel != null && !instituicaoResponsavel.equals("")) {
			sql.append(" AND instituicaoResponsavel like '").append(instituicaoResponsavel).append("%' ");
		}
		if (local != null && !local.equals("")) {
			sql.append(" AND local like '").append(local).append("%' ");
		}
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND matricula.matricula = '").append(matricula).append("' ");
		}
		if(Uteis.isAtributoPreenchido(dataInicio)){
		sql.append(" AND RegistroAtividadeComplementar.data >= '").append(Uteis.getDataJDBCTimestamp(dataInicio)).append("' ");
		}
		if(Uteis.isAtributoPreenchido(dataFinal)){
		sql.append("' AND RegistroAtividadeComplementar.data <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("' ");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().allMatch(p-> p.getCodigo() != 0)) {
			sql.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
		}
		if(Uteis.isAtributoPreenchido(cursoVOs) && cursoVOs.stream().allMatch(p-> p.getCodigo() != 0)) {
			sql.append(adicionarFiltroCurso(cursoVOs));
		}
		sql.append(" ORDER BY RegistroAtividadeComplementar.data ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}

	public RegistroAtividadeComplementarVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RegistroAtividadeComplementarVO obj = new RegistroAtividadeComplementarVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNomeEvento(dadosSQL.getString("nomeEvento"));
		obj.setInstituicaoResponsavel(dadosSQL.getString("instituicaoResponsavel"));
		obj.setAtividade(dadosSQL.getString("atividade"));
		obj.setLocal(dadosSQL.getString("local"));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setDataUltimaAlteracao(dadosSQL.getTimestamp("dataUltimaAlteracao"));
		obj.getResponsavelUltimaAlteracao().setCodigo(dadosSQL.getInt("responsavelUltimaAlteracao"));
		try {
			if (obj.getResponsavelUltimaAlteracao().getCodigo() > 0) {
				obj.setResponsavelUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelUltimaAlteracao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
		} catch (Exception e) {
			obj.setResponsavelUltimaAlteracao(new UsuarioVO());
		}
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public List<RegistroAtividadeComplementarVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroAtividadeComplementarVO> vetResultado = new ArrayList<RegistroAtividadeComplementarVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static void validarDados(RegistroAtividadeComplementarVO obj) throws ConsistirException {
//		if (obj.getNomeEvento().equals("")) {
//			throw new ConsistirException("O campo NOME EVENTO (Registro Atividade Complementar) deve ser informado.");
//		}
//		if (obj.getInstituicaoResponsavel().equals("")) {
//			throw new ConsistirException("O campo INSTITUIÇÃO/RESPONSÁVEL (Registro Atividade Complementar) deve ser informado.");
//		}
//		if (obj.getAtividade().equals("")) {
//			throw new ConsistirException("O campo Descrição do Evento (Registro Atividade Complementar) deve ser informado.");
//		}
//		if (obj.getLocal().equals("")) {
//			throw new ConsistirException("O campo LOCAL (Registro Atividade Complementar) deve ser informado.");
//		}
		if (obj.getListaRegistroAtividadeComplementarMatriculaVOs().size() < 1) {
			throw new ConsistirException("Deve conter ao menos um item na lista.");
		}
	}

	public List<RegistroAtividadeComplementarVO> consultarPorMatriculaVisaoAluno(String matricula, Integer codigoTipoATividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select registroAtividadeComplementar.nomeEvento, registroAtividadeComplementar.data, registroAtividadeComplementarMatricula.cargaHorariaEvento, ");
		sql.append(" registroAtividadeComplementarMatricula.cargaHorariaConsiderada, registroAtividadeComplementar.codigo, registroAtividadeComplementarMatricula.codigo as registroAtividadeComplementarMatricula from registroAtividadeComplementar ");
		sql.append(" inner join registroAtividadeComplementarMatricula on registroAtividadeComplementarMatricula.registroAtividadeComplementar = registroAtividadeComplementar.codigo ");
		sql.append(" WHERE registroAtividadeComplementarMatricula.matricula =' ").append(matricula).append("' ");
		sql.append(" AND registroAtividadeComplementarMatricula.tipoAtividadeComplementar= ").append(codigoTipoATividadeComplementar);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (this.montarDadosConsultaPorMatriculaVisaoAluno(tabelaResultado, usuario));
	}

	public RegistroAtividadeComplementarVO montarDadosPorMatriculaVisaoAluno(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RegistroAtividadeComplementarVO obj = new RegistroAtividadeComplementarVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(new Date(dadosSQL.getTimestamp("data").getTime()));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public List<RegistroAtividadeComplementarVO> montarDadosConsultaPorMatriculaVisaoAluno(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroAtividadeComplementarVO> vetResultado = new ArrayList<RegistroAtividadeComplementarVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static String getIdEntidade() {
		return RegistroAtividadeComplementar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		RegistroAtividadeComplementar.idEntidade = idEntidade;
	}

	@Override
	public RegistroAtividadeComplementarVO consultarPorChavePrimaria(Integer registroAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM RegistroAtividadeComplementar ");
		sql.append("where codigo = ").append(registroAtividadeComplementar);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, usuario);
		}
		return new RegistroAtividadeComplementarVO();
	}

	
	@Override
	public void realizarProcessamentoExcelPlanilhaAtividadeComplementar(FileUploadEvent uploadEvent, RegistroAtividadeComplementarVO registroAtividadeComplementarVO , List<RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs ,Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		String extensao = uploadEvent.getFile().getFileName().substring(uploadEvent.getFile().getFileName().lastIndexOf(".") + 1);
		int rowMax = 0;
		XSSFSheet mySheetXlsx = null;
		HSSFSheet mySheetXls = null;
		
		if (extensao.equals("xlsx")) {
			XSSFWorkbook workbook = new XSSFWorkbook(uploadEvent.getFile().getInputStream());
			mySheetXlsx = workbook.getSheetAt(0);
			rowMax = mySheetXlsx.getLastRowNum();

		} else {
			HSSFWorkbook workbook = new HSSFWorkbook(uploadEvent.getFile().getInputStream());
			mySheetXls = workbook.getSheetAt(0);
			rowMax = mySheetXls.getLastRowNum();
		}

		int linha = 0;

		Row row = null;
		List<String> listaMatriculas = new ArrayList<String>(0);
		List<Integer> linhaErros = new ArrayList<Integer>(0);
		MatriculaVO matriculaVO = null;
		RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO = new RegistroAtividadeComplementarMatriculaVO();
		registroAtividadeComplementarVO.getListaMensagemErroProcessamento().clear();
		
		while (linha <= rowMax) {
			if (extensao.equals("xlsx")) {
				row = mySheetXlsx.getRow(linha);
			} else {
				row = mySheetXls.getRow(linha);
			}
			if (linha == 0) {
				linha++;
				continue;
			}

			TipoAtividadeComplementarVO tipoAtividadeComplementarVO = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorNomeTipoAtividade(String.valueOf(getValorCelula(1, row, true)), false, usuario);
			Boolean existeMatriculaConsulta = getFacadeFactory().getMatriculaFacade().consultarExistenciaMatriculaPorMatricula(String.valueOf(getValorCelula(0, row, true)), usuario);
			Boolean isMatriculaDuplicada = listaMatriculas.stream().anyMatch(String.valueOf(getValorCelula(0, row, true))::equals);
			
			if(getValorCelula(0, row, true) == null || getValorCelula(0, row, true).toString().equals("")) {
				registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Matrícula do Aluno não informada. (linha "+ row.getRowNum() +", coluna A)");
				linhaErros.add(row.getRowNum());
				linha++;
				continue;
			} 
			else {
				if(existeMatriculaConsulta && !isMatriculaDuplicada) {
//					matriculaVO = getFacadeFactory().getMatriculaFacade().consultarAlunoPorMatricula(String.valueOf(getValorCelula(0, row, true)), "", unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuario);		
					listaMatriculas.add(String.valueOf(getValorCelula(0, row, true)));
				} else {
					if(!existeMatriculaConsulta) {
						registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Matrícula não encontrada na base de dados. (linha "+ (row.getRowNum()+1) +", coluna A)");
						linha++;
						continue;
					}
					if(isMatriculaDuplicada) {
						registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Registro Duplicado. Matrícula - " + String.valueOf(getValorCelula(0, row, true)) + " (linha "+ (row.getRowNum()+1) +", coluna A)");
						linha++;
						continue;
					}
				}
			}
	
			if(getValorCelula(1, row, true) == null || getValorCelula(1, row, true).toString().equals("")) {
				registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Tipo de Atividade Complementar não informada. (linha "+ (row.getRowNum()+1) +", coluna B)");
				linhaErros.add(row.getRowNum());
			} else {
				if(tipoAtividadeComplementarVO == null || !Uteis.isAtributoPreenchido(tipoAtividadeComplementarVO)) {
					registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Tipo de Atividade Complementar não encontrada na base de dados. (linha "+ (row.getRowNum()+1) +", coluna B)");
					linhaErros.add(row.getRowNum());
				}
			}
			
//			if(getValorCelula(2, row, false) == null || getValorCelula(2, row, false).toString().equals("")) {
//				registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Carga Horária do Evento não informada. (linha "+ (row.getRowNum()+1) +", coluna C");//ADICIONAR COLUNAS
//				linhaErros.add(row.getRowNum());
//			}else {
//				if((int)Double.parseDouble(getValorCelula(2, row, false).toString()) <= 0) {
//					registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Carga Horária do Evento deve ser maior que zero. (linha "+ (row.getRowNum()+1) +", coluna C)");
//					linhaErros.add(row.getRowNum());
//				}
//			}
			if(getValorCelula(2, row, false) == null || getValorCelula(2, row, false).toString().equals("") ) {
				registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("Carga Horária Realizada não informada. (linha "+ (row.getRowNum()+1) +", coluna D)");
				linhaErros.add(row.getRowNum());
			} 
//			else {
//				if((int)Double.parseDouble((getValorCelula(3, row, false).toString())) > (int)Double.parseDouble((getValorCelula(2, row, false).toString()))) {
//					registroAtividadeComplementarVO.getListaMensagemErroProcessamento().add("CH Realizada não pode ser maior que a CH do Evento. (linha "+ (row.getRowNum()+1) +", coluna D)");
//					linhaErros.add(row.getRowNum());
//				}
//			}
			
			if(!linhaErros.contains(row.getRowNum())){
				
				registroAtividadeComplementarMatriculaVO.setMatriculaVO(matriculaVO);
				registroAtividadeComplementarMatriculaVO.setTipoAtividadeComplementarVO(tipoAtividadeComplementarVO);
//				registroAtividadeComplementarMatriculaVO.setCargaHorariaEvento((int)Double.parseDouble((getValorCelula(2, row, false).toString())));
				registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada((int)Double.parseDouble((getValorCelula(2, row, false).toString())));
				registroAtividadeComplementarMatriculaVO.setObservacao(String.valueOf(getValorCelula(3, row, true)));	
				
				TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(String.valueOf(getValorCelula(0, row, true)), usuario);				
				List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs = (getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarListaMatriculaVOs(registroAtividadeComplementarMatriculaVO.getMatriculaVO().getCurso().getCodigo(), turmaVO.getCodigo(), turmaVO.getTurmaAgrupada(), turmaVO.getSubturma(), "", null, null, registroAtividadeComplementarMatriculaVO.getMatriculaVO().getMatricula(), registroAtividadeComplementarMatriculaVO.getTipoAtividadeComplementarVO().getCodigo(), registroAtividadeComplementarMatriculaVO.getCargaHorariaEvento(), registroAtividadeComplementarMatriculaVO.getCargaHorariaConsiderada(), false, usuario ,registroAtividadeComplementarMatriculaVO.getObservacao(), registroAtividadeComplementarVO.getData()));
				registroAtividadeComplementarVO.setListaRegistroAtividadeComplementarMatriculaVOs(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().adicionarItemListaRegistroAtividadeComplementarMatriculaVOs(listaConsultaRegistroAtividadeComplementarMatriculaVOs, registroAtividadeComplementarVO.getListaRegistroAtividadeComplementarMatriculaVOs()));
				
			}
			linha++;
		}

	}
	public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(CellType.STRING);
		}
		return cell;
	}
	
	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" and unidadeEnsino.codigo in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(") ");
		return sql.toString();
	}

	private String adicionarFiltroCurso(List<CursoVO> cursoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and curso.codigo in (0");
		for (CursoVO cursoVO : cursoVOs) {
			if (cursoVO.getFiltrarCursoVO()) {
				sql.append(", ").append(cursoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}
	
	public void validarTipoAtividadeComplementar(List<RegistroAtividadeComplementarMatriculaVO> RegistroAtividadeComplementarMatriculaVOs, UsuarioVO usuarioVO) throws Exception {
		if(Uteis.isAtributoPreenchido(RegistroAtividadeComplementarMatriculaVOs)) {
			for(RegistroAtividadeComplementarMatriculaVO listaRegistroAtividadeComplementarMatriculaVOsExistentes : RegistroAtividadeComplementarMatriculaVOs) {
				List<RegistroAtividadeComplementarMatriculaVO> registrosDeferidosAtividadeComplementarMatriculaVOs = (getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarRegistrosAprovadosPorMatricula(listaRegistroAtividadeComplementarMatriculaVOsExistentes.getMatriculaVO().getMatricula(), usuarioVO));
				if(Uteis.isAtributoPreenchido(registrosDeferidosAtividadeComplementarMatriculaVOs)) {
					for(RegistroAtividadeComplementarMatriculaVO listaRegistroAtividadeComplementarMatriculaVOs : registrosDeferidosAtividadeComplementarMatriculaVOs) {
						int atvComplementar = listaRegistroAtividadeComplementarMatriculaVOs.getTipoAtividadeComplementarVO().getCodigo();
						int codigoRegistroAtividadeComplementar = listaRegistroAtividadeComplementarMatriculaVOs.getRegistroAtividadeComplementar().getCodigo();
						if(listaRegistroAtividadeComplementarMatriculaVOsExistentes.getTipoAtividadeComplementarVO().getCodigo().equals(atvComplementar) &&
							!listaRegistroAtividadeComplementarMatriculaVOsExistentes.getRegistroAtividadeComplementar().getCodigo().equals(codigoRegistroAtividadeComplementar)) {
							if (listaRegistroAtividadeComplementarMatriculaVOsExistentes.getSituacaoAtividadeComplementarMatricula().equals(SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO)) {
								throw new Exception("Já existe um registro de atividade complementar " + listaRegistroAtividadeComplementarMatriculaVOs.getTipoAtividadeComplementarVO().getNome() + ", por isto não é possível realizar este deferimento");
							}
							throw new Exception("Já existe um registro de atividade complementar " + listaRegistroAtividadeComplementarMatriculaVOs.getTipoAtividadeComplementarVO().getNome() + ", por isto não é possível realizar um novo cadastro");
						}
					}
				}
			}
		}
	}
}
