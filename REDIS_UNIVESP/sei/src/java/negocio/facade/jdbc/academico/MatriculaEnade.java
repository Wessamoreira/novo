package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaEnadeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoInscricaoEnadeEnum;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.diplomaDigital.versao1_05.TEnumCondicaoEnade;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MatriculaEnadeInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaEnade extends ControleAcesso implements MatriculaEnadeInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MatriculaEnade() throws Exception {
		super();
		setIdEntidade("MatriculaEnade");
	}

	public static String getIdEntidade() {
		return MatriculaEnade.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		MatriculaEnade.idEntidade = idEntidade;
	}

	public void validarDados(MatriculaEnadeVO obj) throws Exception {
		if (obj.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("O campo Matrícula(MATRÍCULA ENADE) deve ser preenchido.");
		}
		if (obj.getEnadeVO().getCodigo().equals(0)) {
			throw new Exception("O campo Enade(MATRÍCULA ENADE) deve ser preenchido.");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<MatriculaVO> listaMatriculaVOs, List<MatriculaEnadeVO> listaMatriculaEnadeVOs, List<MatriculaEnadeVO> listaMatriculaArquivoEnadeVOs, UsuarioVO usuario) throws Exception {
		for (MatriculaEnadeVO matriculaEnadeVO : listaMatriculaEnadeVOs) {
			for (MatriculaVO matriculaVO : listaMatriculaVOs) {
				if (matriculaVO.getAlunoSelecionado()) {
					MatriculaEnadeVO matriculaArquivoEnadeVO =  null;
					if(Uteis.isAtributoPreenchido(listaMatriculaArquivoEnadeVOs) 
							&& listaMatriculaArquivoEnadeVOs.stream().anyMatch(m -> m.getMatriculaVO().getMatricula().equals(matriculaVO.getMatricula()))) {
						matriculaArquivoEnadeVO = listaMatriculaArquivoEnadeVOs.stream().filter(m -> m.getMatriculaVO().getMatricula().equals(matriculaVO.getMatricula())).findFirst().get();
					}
					adicionarObjEnadeVOs(matriculaEnadeVO, matriculaVO, matriculaArquivoEnadeVO);
					alterarMatriculaEnade(matriculaVO, matriculaVO.getMatriculaEnadeVOs() , usuario);
				}
			}			
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaEnadeVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			MatriculaEnade.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO MatriculaEnade(matricula, enade, dataEnade, textoEnade , tipoInscricao, condicaoEnade  ) VALUES ( ?, ?, ?, ? , ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
					if (obj.getEnadeVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getEnadeVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if(obj.getDataEnade() != null) {
						sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataEnade()));
					}else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, obj.getTextoEnade().getCodigo());
					sqlInserir.setString(5, obj.getTipoInscricaoEnade().toString());
					if (obj.getCondicaoEnade() != null) {
						sqlInserir.setString(6, obj.getCondicaoEnade().name());
					} else {
						sqlInserir.setNull(6, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaEnadeVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			MatriculaEnade.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE MatriculaEnade set matricula=?, enade=?, dataEnade=?, textoEnade = ? , tipoInscricao=?, condicaoEnade=?   WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getMatriculaVO().getMatricula());
					if (obj.getEnadeVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getEnadeVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if(obj.getDataEnade() != null) {
						sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataEnade()));
					}else {
						sqlAlterar.setNull(3, 0);
					}							
					sqlAlterar.setInt(4, obj.getTextoEnade().getCodigo().intValue());
					sqlAlterar.setString(5, obj.getTipoInscricaoEnade().toString());
					if (obj.getCondicaoEnade() != null) {
						sqlAlterar.setString(6, obj.getCondicaoEnade().name());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaEnadeVO obj, UsuarioVO usuario) throws Exception {
		try {
			Enade.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM MatriculaEnade WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<MatriculaEnadeVO> consultarMatriculaEnadePorMatricula(String matricula, UsuarioVO usuarioVO, Boolean ordemDecrescente) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct matriculaenade.codigo AS \"matriculaenade.codigo\", matriculaenade.dataEnade AS \"matriculaenade.dataEnade\", ");
		sb.append(" matriculaenade.matricula AS \"matriculaenade.matricula\", ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", enade.dataportaria AS \"enade.dataportaria\",  ");		
		sb.append(" textoenade.codigo AS \"textoenade.codigo\", textoenade.texto AS \"textoenade.texto\", textoenade.tipotextoenade AS \"textoenade.tipotextoenade\"  ,  matriculaenade.tipoInscricao AS \"matriculaenade.tipoInscricao\", enade.dataProva AS \"enade.dataProva\", matriculaEnade.condicaoEnade AS \"matriculaEnade.condicaoEnade\", ");
		sb.append(" enade.datapublicacaoportariadou AS \"enade.datapublicacaoportariadou\" ");
		sb.append(" from matriculaenade ");
		sb.append(" left join enade on enade.codigo = matriculaenade.enade ");
		sb.append(" inner join matricula on matricula.matricula = matriculaenade.matricula ");
		sb.append(" left join textoenade on textoenade.codigo = matriculaenade.textoenade ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where matricula.matricula = '").append(matricula).append("' order by matriculaEnade.dataEnade, matriculaEnade.codigo ");
		if (ordemDecrescente) {
			sb.append(" desc ");
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaEnadeVO> listaMatriculaEnadeVOs = new ArrayList<MatriculaEnadeVO>(0);
		while (tabelaResultado.next()) {
			MatriculaEnadeVO obj = new MatriculaEnadeVO();
			obj.setCodigo(tabelaResultado.getInt("matriculaEnade.codigo"));
			obj.setDataEnade(tabelaResultado.getDate("matriculaEnade.dataEnade"));		
			obj.setTipoInscricaoEnade(TipoInscricaoEnadeEnum.valueOf(tabelaResultado.getString("matriculaenade.tipoInscricao")));
			obj.getTextoEnade().setCodigo(tabelaResultado.getInt("textoenade.codigo"));
			obj.getTextoEnade().setTexto(tabelaResultado.getString("textoenade.texto"));
			if(tabelaResultado.getString("textoenade.texto") != null && !tabelaResultado.getString("textoenade.texto").trim().isEmpty()){
				obj.getTextoEnade().setTipoTextoEnade(TipoTextoEnadeEnum.valueOf(tabelaResultado.getString("textoenade.tipoTextoEnade")));
			}
			if(Uteis.isAtributoPreenchido(tabelaResultado.getString("matriculaEnade.condicaoEnade"))){
				obj.setCondicaoEnade(TEnumCondicaoEnade.valueOf(tabelaResultado.getString("matriculaEnade.condicaoEnade")));
			}
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matriculaEnade.matricula"));
			obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getEnadeVO().setCodigo(tabelaResultado.getInt("enade.codigo"));
			obj.getEnadeVO().setTituloEnade(tabelaResultado.getString("enade.tituloEnade"));
			obj.getEnadeVO().setDataPortaria(tabelaResultado.getDate("enade.dataPortaria"));
			obj.getEnadeVO().setDataProva(tabelaResultado.getDate("enade.dataProva"));
			obj.getEnadeVO().setDataPublicacaoPortariaDOU(tabelaResultado.getDate("enade.datapublicacaoportariadou"));
			
			listaMatriculaEnadeVOs.add(obj);
		}
		return listaMatriculaEnadeVOs;
	}
	
	public void validarDadosConsulta(List<UnidadeEnsinoVO> unidades, List<CursoVO> cursos, Integer turma, String periodoLetivoInicial, String periodoLetivoFinal, String matricula) throws Exception {
		if (unidades.isEmpty()) {
			throw new Exception("Informe pelo menos 1 (uma) Unidade de Ensino.");
		}
		if (cursos.isEmpty() && turma.equals(0) && matricula.equals("")) {
			throw new Exception("Deve ser informado mais filtros(Matrícula, Curso, Turma) para realização da consulta.");
		}
		int inicio = 0;
		int fim = 0;
		try {
			if (!periodoLetivoInicial.isEmpty()) {
				inicio = Integer.parseInt(periodoLetivoInicial);
			}
			if (!periodoLetivoFinal.isEmpty()) {
				fim = Integer.parseInt(periodoLetivoFinal);
			}
		} catch (Exception e) {
			throw new Exception("Verifique o Período Letivo, falha ao converter período letivo informado.");
		}
		if (!periodoLetivoInicial.isEmpty() && !periodoLetivoFinal.isEmpty()) {
			if (fim < inicio) {
				throw new Exception("O Período Letivo Inicial deve ser menor que o Período Letivo Final.");
			}
		}
		
	}

	public List<MatriculaVO> consultar(String matricula, List<CursoVO> cursos, Integer turma, List<UnidadeEnsinoVO> unidades, List<TurnoVO> turnos, String periodoLetivoInicial, String periodoLetivoFinal, String ano, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String cpfAluno , UsuarioVO usuarioVO) throws Exception {
		validarDadosConsulta(unidades, cursos, turma, periodoLetivoInicial, periodoLetivoFinal, matricula);
		List<Object> filtros = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct matricula.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\" , pessoa.dataNasc AS \"pessoa.dataNasc\" ,");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		sb.append(" matricula.situacao as \"matricula.situacao\"  ");
		sb.append(" FROM matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sb.append(" inner join cursoturno on cursoturno.curso = curso.codigo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" where 1=1 ");
		if (!matricula.equals("")) {
			sb.append("and matricula.matricula = ? ");
			filtros.add(matricula);
		}
		StringBuilder sqlUnidade = new StringBuilder();
		sqlUnidade.append(" and unidadeensinocurso.unidadeensino in (");
        String auxUnidade = "";
        for (UnidadeEnsinoVO unidade : unidades) {
        	if (unidade.getFiltrarUnidadeEnsino()) {
        		sqlUnidade.append(auxUnidade).append(unidade.getCodigo());
        		auxUnidade = ",";
        	}
        }
        sqlUnidade.append(") ");
        if (auxUnidade.equals(",")) {
        	sb.append(sqlUnidade);
        }
		StringBuilder sqlCurso = new StringBuilder();
        sqlCurso.append(" and curso.codigo in (");
        String auxCurso = "";
        for (CursoVO curso : cursos) {
        	if (curso.getFiltrarCursoVO()) {
            	sqlCurso.append(auxCurso).append(curso.getCodigo());
            	auxCurso = ",";
        	}
        }
        sqlCurso.append(") ");
        if (auxCurso.equals(",")) {
        	sb.append(sqlCurso);
        }
        StringBuilder sqlTurno = new StringBuilder();
        sqlTurno.append(" and cursoturno.turno in (");
        String auxTurno = "";
        for (TurnoVO turno : turnos) {
        	if (turno.getFiltrarTurnoVO()) {
        		sqlTurno.append(auxTurno).append(turno.getCodigo());
        		auxTurno = ",";
        	}
        }
        sqlTurno.append(") ");
        if (auxTurno.equals(",")) {
        	sb.append(sqlTurno);
        }
		if (!turma.equals(0)) {
			sb.append(" and turma.codigo = ").append(turma);
		}
		if (!periodoLetivoInicial.isEmpty() && !periodoLetivoFinal.isEmpty()) {
			sb.append(" and periodoletivo.periodoletivo between ").append(Integer.parseInt(periodoLetivoInicial))
				.append(" and ").append(Integer.parseInt(periodoLetivoFinal)).append(" ");
		} else if (!periodoLetivoInicial.isEmpty()) {
			sb.append(" and periodoletivo.periodoletivo >= ").append(Integer.parseInt(periodoLetivoInicial)).append(" ");
		} else if (!periodoLetivoFinal.isEmpty()) {
			sb.append(" and periodoletivo.periodoletivo <= ").append(Integer.parseInt(periodoLetivoFinal)).append(" ");
		}
		if (!ano.isEmpty()) {
			sb.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
		}
		sb.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
		sb.append(" order by curso.nome, pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), filtros.toArray());
		return montarDadosConsulta(tabelaResultado, usuarioVO);

	}

	public List<MatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDados(obj, tabelaResultado, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void montarDados(MatriculaVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getAluno().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
//		obj.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(dadosSQL.getString("matriculaperiodo.situacaomatriculaperiodo"));
		obj.setSituacao(dadosSQL.getString("matricula.situacao"));
		obj.setMatriculaEnadeVOs(consultarMatriculaEnadePorMatricula(obj.getMatricula(), usuarioVO, false));
	}

	public void removerMatriculaEnade(MatriculaVO matriculaVO, MatriculaEnadeVO matriculaEnadeVO) {
		int index = 0;
		Iterator i = matriculaVO.getMatriculaEnadeVOs().iterator();
		while (i.hasNext()) {
			MatriculaEnadeVO objExistente = (MatriculaEnadeVO) i.next();
			if (objExistente.getEnadeVO().getCodigo().equals(matriculaEnadeVO.getEnadeVO().getCodigo())
					&& objExistente.getCodigo().equals(matriculaEnadeVO.getCodigo())) {
				matriculaVO.getMatriculaEnadeVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void removerEnade(MatriculaEnadeVO matriculaEnadeVO, List<MatriculaEnadeVO> listaMatriculaEnadeVOs) {
		int index = 0;
		Iterator i = listaMatriculaEnadeVOs.iterator();
		while (i.hasNext()) {
			MatriculaEnadeVO objExistente = (MatriculaEnadeVO) i.next();
			if (objExistente.getEnadeVO().getCodigo().equals(matriculaEnadeVO.getEnadeVO().getCodigo())) {
				listaMatriculaEnadeVOs.remove(index);
				return;
			}
			index++;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaEnade(MatriculaVO matriculaVO, List<MatriculaEnadeVO> objetos, UsuarioVO usuario) throws Exception {
		excluirMatriculaEnade(matriculaVO.getMatricula(), usuario);
		incluirMatriculaEnade(matriculaVO, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaEnade(String matricula, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaEnade WHERE (matricula = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaEnade(MatriculaVO matriculaVO, List<MatriculaEnadeVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaEnadeVO obj = (MatriculaEnadeVO) e.next();
			obj.getMatriculaVO().setMatricula(matriculaVO.getMatricula());
			obj.getMatriculaVO().setAluno(matriculaVO.getAluno());
			incluir(obj, usuario);
		}
	}
	
	public void validarDadosAdicaoLista(MatriculaEnadeVO matriculaEnadeVO) throws Exception {
		if (matriculaEnadeVO.getEnadeVO().getCodigo().equals(0)) {
			throw new Exception("O campo ENADE deve ser informado.");
		}		
		if (matriculaEnadeVO.getTextoEnade().getCodigo() == null || matriculaEnadeVO.getTextoEnade().getCodigo() == 0) {
			throw new Exception("O campo TEXTO ENADE deve ser informado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarObjMatriculaEnadeVOs(MatriculaEnadeVO matriculaEnadeVO, List<MatriculaEnadeVO> listaMatriculaEnadeVOs) throws Exception {
		matriculaEnadeVO.setTextoEnade(getFacadeFactory().getTextoEnadeFacade().consultarPorChavePrimaria(matriculaEnadeVO.getTextoEnade().getCodigo()));
		validarDadosAdicaoLista(matriculaEnadeVO);
		int index = 0;
		Iterator i = listaMatriculaEnadeVOs.iterator();
		while (i.hasNext()) {
			MatriculaEnadeVO objExistente = (MatriculaEnadeVO) i.next();
			if (objExistente.getEnadeVO().getCodigo().equals(matriculaEnadeVO.getEnadeVO().getCodigo())) {
				listaMatriculaEnadeVOs.set(index, matriculaEnadeVO);
				return;
			}
			index++;
		}
		listaMatriculaEnadeVOs.add(matriculaEnadeVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarObjEnadeVOs(MatriculaEnadeVO matriculaEnadeVO, MatriculaVO matriculaVO, MatriculaEnadeVO matriculaArquivoEnadeVO) throws Exception {
		int index = 0;
		MatriculaEnadeVO obj = preencherDadosMatriculaEnade(matriculaEnadeVO, matriculaVO, matriculaArquivoEnadeVO);
		Iterator i = matriculaVO.getMatriculaEnadeVOs().iterator();
		while (i.hasNext()) {
			MatriculaEnadeVO objExistente = (MatriculaEnadeVO) i.next();
			if (objExistente.getEnadeVO().getCodigo().equals(obj.getEnadeVO().getCodigo())) {
				matriculaVO.getMatriculaEnadeVOs().set(index, obj);
				return;
			}
			index++;
		}
		matriculaVO.getMatriculaEnadeVOs().add(obj);
	}
	
	public MatriculaEnadeVO preencherDadosMatriculaEnade(MatriculaEnadeVO matriculaEnadeVO, MatriculaVO matriculaVO, MatriculaEnadeVO matriculaArquivoEnadeVO) {
		MatriculaEnadeVO obj = new MatriculaEnadeVO();
		obj.setEnadeVO(matriculaEnadeVO.getEnadeVO());
		if(Uteis.isAtributoPreenchido(matriculaEnadeVO.getDataEnade())) {
			obj.setDataEnade(matriculaEnadeVO.getDataEnade());			
		}else {
			if(matriculaArquivoEnadeVO != null && matriculaArquivoEnadeVO.getDataEnade() != null) {
				obj.setDataEnade(matriculaArquivoEnadeVO.getDataEnade());				
			}
		}
		obj.setTextoEnade(matriculaEnadeVO.getTextoEnade());
		obj.getMatriculaVO().setMatricula(matriculaVO.getMatricula());
		obj.setTipoInscricaoEnade(matriculaVO.getMatriculaEnadeVO().getTipoInscricaoEnade());
		obj.setCondicaoEnade(matriculaEnadeVO.getCondicaoEnade());
		return obj;
	}

	public void preencherTodosListaAluno(List<MatriculaVO> listaMatriculaVOs) {
        for (MatriculaVO matriculaVO : listaMatriculaVOs) {
			matriculaVO.setAlunoSelecionado(Boolean.TRUE);
		}
    }

    public void desmarcarTodosListaAluno(List<MatriculaVO> listaMatriculaVOs) {
    	for (MatriculaVO matriculaVO : listaMatriculaVOs) {
			matriculaVO.setAlunoSelecionado(Boolean.FALSE);
		}
    }

    public void validarDadosAvancarTelaEnade(List<MatriculaVO> listaMatriculaVOs) throws Exception {
    	if (listaMatriculaVOs.isEmpty()) {
    		throw new Exception("Não foi encontrado nenhum aluno para inclusão do ENADE. Favor realizar a consulta antes de incluir o ENADE.");
    	}
    }
    
    public void validarDadosUnidadeEnsino(Integer unidadeEnsino) throws Exception {
    	if (unidadeEnsino.equals(0)) {
    		throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
    	}
    }
    
    public void validarDadosUnidadeEnsino(List<UnidadeEnsinoVO> unidades) throws Exception {
    	if (!Uteis.isAtributoPreenchido(unidades.isEmpty()) || unidades.stream().noneMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
    		throw new Exception("Selecione ao menos 1 (uma) Unidade de Ensino.");
    	}
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirEnadeMatricula(MatriculaVO obj, UsuarioVO usuario) throws Exception {
		try {			
			String sql = "DELETE FROM MatriculaEnade WHERE ((matricula = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	public MatriculaEnadeVO consultarMatriculaEnadePorData(String matricula, UsuarioVO usuarioVO, Boolean ordemDecrescente) {
		MatriculaEnadeVO obj = new MatriculaEnadeVO();
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct matriculaenade.codigo AS \"matriculaenade.codigo\", matriculaenade.dataEnade AS \"matriculaenade.dataEnade\", ");
		sb.append(" matriculaenade.matricula AS \"matriculaenade.matricula\",  matriculaenade.tipoInscricao AS \"matriculaenade.tipoInscricao\" ,  ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", enade.dataportaria AS \"enade.dataportaria\",  ");		
		sb.append(" textoenade.codigo AS \"textoenade.codigo\", textoenade.texto AS \"textoenade.texto\", textoenade.tipotextoenade AS \"textoenade.tipotextoenade\"  ");
		sb.append(" from matriculaenade ");
		sb.append(" left join enade on enade.codigo = matriculaenade.enade ");
		sb.append(" inner join matricula on matricula.matricula = matriculaenade.matricula ");
		sb.append(" left join textoenade on textoenade.codigo = matriculaenade.textoenade ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where matricula.matricula = '").append(matricula).append("' order by matriculaEnade.dataEnade, matriculaEnade.codigo ");
		if (ordemDecrescente) {
			sb.append(" desc ");
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("matriculaEnade.codigo"));
			obj.setDataEnade(tabelaResultado.getDate("matriculaEnade.dataEnade"));		
			obj.setTipoInscricaoEnade(TipoInscricaoEnadeEnum.valueOf(tabelaResultado.getString("matriculaenade.tipoInscricao")));
			obj.getTextoEnade().setCodigo(tabelaResultado.getInt("textoenade.codigo"));
			obj.getTextoEnade().setTexto(tabelaResultado.getString("textoenade.texto"));
			if(tabelaResultado.getString("textoenade.texto") != null && !tabelaResultado.getString("textoenade.texto").trim().isEmpty()){
				obj.getTextoEnade().setTipoTextoEnade(TipoTextoEnadeEnum.valueOf(tabelaResultado.getString("textoenade.tipoTextoEnade")));
			}
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matriculaEnade.matricula"));
			obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getEnadeVO().setCodigo(tabelaResultado.getInt("enade.codigo"));
			obj.getEnadeVO().setTituloEnade(tabelaResultado.getString("enade.tituloEnade"));
			obj.getEnadeVO().setDataPortaria(tabelaResultado.getDate("enade.dataPortaria"));
			obj.getMatriculaVO().setDataEnade(tabelaResultado.getDate("matriculaEnade.dataEnade"));
		}
		return obj;
	}

	@Override
	public MatriculaEnadeVO consultarMatriculaEnadePorMatriculaAluno(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct matriculaenade.codigo AS \"matriculaenade.codigo\", matriculaenade.dataEnade AS \"matriculaenade.dataEnade\", ");
		sb.append(" matriculaenade.matricula AS \"matriculaenade.matricula\", ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sb.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", enade.dataportaria AS \"enade.dataportaria\",  ");		
		sb.append(" textoenade.codigo AS \"textoenade.codigo\", textoenade.texto AS \"textoenade.texto\", textoenade.tipotextoenade AS \"textoenade.tipotextoenade\"  ");
		sb.append(" from matriculaenade ");
		sb.append(" left join enade on enade.codigo = matriculaenade.enade ");
		sb.append(" inner join matricula on matricula.matricula = matriculaenade.matricula ");
		sb.append(" left join textoenade on textoenade.codigo = matriculaenade.textoenade ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where matricula.matricula in ('").append(matricula).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			MatriculaEnadeVO obj = new MatriculaEnadeVO();
			obj.setCodigo(tabelaResultado.getInt("matriculaEnade.codigo"));
			obj.setDataEnade(tabelaResultado.getDate("matriculaEnade.dataEnade"));			
			obj.getTextoEnade().setCodigo(tabelaResultado.getInt("textoenade.codigo"));
			obj.getTextoEnade().setTexto(tabelaResultado.getString("textoenade.texto"));
			if(tabelaResultado.getString("textoenade.texto") != null && !tabelaResultado.getString("textoenade.texto").trim().isEmpty()){
				obj.getTextoEnade().setTipoTextoEnade(TipoTextoEnadeEnum.valueOf(tabelaResultado.getString("textoenade.tipoTextoEnade")));
			}
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matriculaEnade.matricula"));
			obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getEnadeVO().setCodigo(tabelaResultado.getInt("enade.codigo"));
			obj.getEnadeVO().setTituloEnade(tabelaResultado.getString("enade.tituloEnade"));
			obj.getEnadeVO().setDataPortaria(tabelaResultado.getDate("enade.dataPortaria"));
			return obj;
		} else {
			MatriculaEnadeVO obj = new MatriculaEnadeVO();
			return obj;
		}
	
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosArquivoMatriculaEnade(ArquivoVO	arquivoVO , FileUploadEvent fileUploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		arquivoVO.setNome(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(fileUploadEvent.getUploadedFile().getName())));
		arquivoVO.setExtensao(ArquivoHelper.getExtensaoArquivo(fileUploadEvent.getUploadedFile().getName()));
		arquivoVO.setDescricao(fileUploadEvent.getUploadedFile().getName());
		arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
		arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue());
		arquivoVO.getResponsavelUpload().setCodigo(usuarioVO.getCodigo());
		arquivoVO.getResponsavelUpload().setNome(usuarioVO.getNome());
		arquivoVO.setDataUpload(new Date());
		arquivoVO.setManterDisponibilizacao(true);
		arquivoVO.setDataDisponibilizacao(new Date());
		arquivoVO.setDataIndisponibilizacao(null);
		arquivoVO.setSituacao(SituacaoArquivo.ATIVO.getValor());
		arquivoVO.setOrigem(OrigemArquivo.MATRICULA_ENADE.getValor());
		ArquivoHelper.salvarArquivoNaPastaTemp(fileUploadEvent, arquivoVO.getNome(), arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO, usuarioVO);
		
	}


	@Override
	public void realizarProcessamentoArquivoExcelMatriculaEnade(FileUploadEvent uploadEvent, String nomeArquivo , List<MatriculaVO> listaMatriculaVOs ,  List<MatriculaEnadeVO> listaMatriculaArquivoEnadeVOs, List<MatriculaEnadeVO> listaMatriculaArquivoEnadeErroVOs, CursoVO cursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean lancarExcessao,	ProgressBarVO progressBarVO, UsuarioVO usuarioVO)  throws Exception{
		
		listaMatriculaArquivoEnadeVOs.clear();
		listaMatriculaArquivoEnadeErroVOs.clear();
		listaMatriculaVOs.clear();
		String path = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue()	+ File.separator + nomeArquivo;
		File arquivo =  new File(path);
		InputStream stream = new FileInputStream(arquivo);
		
		String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
		Map<String, MatriculaEnadeVO> mapaMatriculaEnade = new HashMap<String, MatriculaEnadeVO>(0);
		if(extensao.equals("csv")) {
			 try {				 
			      
			        FileReader filereader = new FileReader(arquivo);			      
			        CSVParser  csvParser = new CSVParserBuilder().withSeparator(';').build();   
			        CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).withCSVParser(csvParser).build(); 		 
			        List<String[]> allData = csvReader.readAll();
			        if(allData.isEmpty()) {
			        	throw new Exception("Não foi possível ler os dados do Arquivo. Tente novamente ou utilize outro formato de Arquivo (XLS,xls)");
			        }
			        
			        	for (String[] row : allData) {			        		
				        	// dados aluno enade								
								MatriculaEnadeVO matriculaEnade = new MatriculaEnadeVO();
								PessoaVO alunoImportarEnade = new PessoaVO();
								
								alunoImportarEnade.setNome( row[0] != null ? String.valueOf( row[0]) : "");
								alunoImportarEnade.setCPF( row[1] != null ? String.valueOf( row[1]) : "");
								alunoImportarEnade.setDataNasc( row[2] != null ?  Uteis.getDataJDBC(UteisData.getData(row[2])) : null);
								matriculaEnade.getMatriculaVO().setAluno(alunoImportarEnade);
								matriculaEnade.setTipoInscricaoEnade(TipoInscricaoEnadeEnum.valueOf( row[3] != null ? String.valueOf( row[3]).toUpperCase() : "NENHUM"));
								matriculaEnade.setDataEnade( row[4] != null ?  Uteis.getDataJDBC(UteisData.getData(row[4])) : null);
								
								validarDadosMatriculaEnadeArquivoExcel(matriculaEnade , mapaMatriculaEnade , lancarExcessao  ,usuarioVO );
								if (matriculaEnade.getPossuiErro()) {
									listaMatriculaArquivoEnadeErroVOs.add(matriculaEnade);
						    	}else {	
						    		MatriculaVO matriculaResult = getFacadeFactory().getMatriculaEnadeFacade().consultarPorCursoCpf(alunoImportarEnade.getCPF(), cursoVO, usuarioVO);
									if(Uteis.isAtributoPreenchido(matriculaResult.getMatricula())) {
										matriculaEnade.setMatriculaVO(matriculaResult);
										matriculaEnade.getMatriculaVO().setMatriculaEnadeVO(matriculaEnade);
										listaMatriculaVOs.add(matriculaResult);
										listaMatriculaArquivoEnadeVOs.add(matriculaEnade);
									}else {
										matriculaEnade.setErro("Não foi encontrado Matricula para o Aluno NOME("+matriculaEnade.getMatriculaVO().getAluno().getNome()+ ") - CPF("+matriculaEnade.getMatriculaVO().getAluno().getCPF()+ ")   - CURSO("+cursoVO.getNome()+ ") ");
										matriculaEnade.setPossuiErro(Boolean.TRUE);
										listaMatriculaArquivoEnadeErroVOs.add(matriculaEnade);
									}						    		
						    	}								
								progressBarVO.setStatus("Realizando carregamento Aluno CPF("+ alunoImportarEnade.getCPF()+") - Nome("+alunoImportarEnade.getNome().toUpperCase()+") ");
								progressBarVO.incrementar();			        	 
				          }			        	
			          		        
			    }
			    catch (Exception e) {
			        e.printStackTrace();
			    }	
				}else {
					int rowMax = 0;
					XSSFSheet mySheetXlsx = null;
					HSSFSheet mySheetXls = null;
					if (extensao.equals("xlsx")) {
			//			PARA XLSX UTILIZA XSSFWorkbook
						XSSFWorkbook workbook = new XSSFWorkbook(stream);
						mySheetXlsx = workbook.getSheetAt(0);
						rowMax = mySheetXlsx.getLastRowNum();
			
					} else {
			//			PARA XLS UTILIZA HSSFWorkbook
						HSSFWorkbook workbook = new HSSFWorkbook(stream);
						mySheetXls = workbook.getSheetAt(0);
						rowMax = mySheetXls.getLastRowNum();
					}
				progressBarVO.iniciar(1l, rowMax, "Iniciando.....", false, null, "");
				
				int qtdeLinhaEmBranco = 0;
				int linha = 0;			
			
				Row row = null;
				while (linha <= rowMax) {
					if (extensao.equals("xlsx")) {
						row = mySheetXlsx.getRow(linha);
					} else {
						row = mySheetXls.getRow(linha);
					}
					if (linha <= 4) {
						linha++;
						continue;
					}
					if (linha == 5) {
						validarDadosCabecalhoExcelTabela(row);
						linha++;
						continue;
					}
					if (qtdeLinhaEmBranco == 2 ) {
						break;
					}
					if (getValorCelula(0, row, true) == null || getValorCelula(0, row, true).toString().equals("")) {
						qtdeLinhaEmBranco++;
						continue;
					}
						
					if (progressBarVO.getForcarEncerramento()) {
						break;
					}
					
						// dados aluno enade
						
						MatriculaEnadeVO matriculaEnade = new MatriculaEnadeVO();
						PessoaVO alunoImportarEnade = new PessoaVO();
						alunoImportarEnade.setNome(getValorCelula(0, row, true) != null ? String.valueOf(getValorCelula(0, row, true)) : "");
						alunoImportarEnade.setCPF(getValorCelula(1, row, true) != null ? String.valueOf(getValorCelula(1, row, true)) : "");
						DataFormatter formatter = new DataFormatter();
						String val = "";
						if (getValorCelula(2, row, false) != null) {
							val = formatter.formatCellValue(getValorCelula(2, row, false));
							if (!Uteis.isAtributoPreenchido(val) || !Uteis.isAtributoPreenchido(Uteis.getData(val, "dd/MM/yyyy"))) {
							val = Uteis.getData(getValorCelula(2, row, false).getDateCellValue(), "dd/MM/yyyy").toString();
							}
						} 
						alunoImportarEnade.setDataNasc(Uteis.isAtributoPreenchido(val) ? Uteis.getDataJDBC(UteisData.getData(val)) : null);
						matriculaEnade.getMatriculaVO().setAluno(alunoImportarEnade);
						matriculaEnade.setTipoInscricaoEnade(TipoInscricaoEnadeEnum.valueOf(getValorCelula(3, row, true) != null ? String.valueOf(getValorCelula(3, row, true)).toUpperCase() : "NENHUM"));
						String dtEnade = "";
						if (getValorCelula(4, row, false) != null) {
							dtEnade = formatter.formatCellValue(getValorCelula(4, row, false));
							if (!Uteis.isAtributoPreenchido(dtEnade) || !Uteis.isAtributoPreenchido(Uteis.getData(dtEnade, "dd/MM/yyyy"))) {
								dtEnade = Uteis.getData(getValorCelula(4, row, false).getDateCellValue(), "dd/MM/yyyy").toString();
							}
						} 
						matriculaEnade.setDataEnade(Uteis.isAtributoPreenchido(dtEnade) ? Uteis.getDataJDBC(UteisData.getData(dtEnade)) : null);
						
						validarDadosMatriculaEnadeArquivoExcel(matriculaEnade , mapaMatriculaEnade , lancarExcessao  ,usuarioVO );
						if (matriculaEnade.getPossuiErro()) {
							listaMatriculaArquivoEnadeErroVOs.add(matriculaEnade);
				    	}else {	
				    		MatriculaVO matriculaResult = getFacadeFactory().getMatriculaEnadeFacade().consultarPorCursoCpf(alunoImportarEnade.getCPF(), cursoVO, usuarioVO);
							if(Uteis.isAtributoPreenchido(matriculaResult.getMatricula())) {
								matriculaEnade.setMatriculaVO(matriculaResult);
								matriculaEnade.getMatriculaVO().setMatriculaEnadeVO(matriculaEnade);
								listaMatriculaVOs.add(matriculaResult);
								listaMatriculaArquivoEnadeVOs.add(matriculaEnade);
							}else {
								matriculaEnade.setErro("Não foi encontrado Matricula para o Aluno NOME("+matriculaEnade.getMatriculaVO().getAluno().getNome()+ ") - CPF("+matriculaEnade.getMatriculaVO().getAluno().getCPF()+ ")   - CURSO("+cursoVO.getNome()+ ") ");
								matriculaEnade.setPossuiErro(Boolean.TRUE);
								listaMatriculaArquivoEnadeErroVOs.add(matriculaEnade);
							}
				    		
				    	}	
						
						
						progressBarVO.setStatus("Realizando carregamento Aluno CPF("+ alunoImportarEnade.getCPF()+") - Nome("+alunoImportarEnade.getNome().toUpperCase()+") ");
						progressBarVO.incrementar();
						
					
						linha++;
				   }
				}	
				progressBarVO.setForcarEncerramento(true);
				progressBarVO.encerrar();	
		
	}
	
	
	public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}
	
	
	public void validarDadosCabecalhoExcelTabela(Row row) throws Exception {
		int index = 0;
		if (row.getCell(index) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nome")) {
			throw new Exception("A coluna 'A' deve ser referente ao campo Nome , favor informe um título com a descrição \"nome\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("cpf")) {
			throw new Exception("A coluna 'B' deve ser referente ao campo CPF, favor informe um título com a descrição \"cpf\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nascimento")) {
			throw new Exception("A coluna 'C' deve ser referente ao campo Nascimento, favor informe um título com a descrição \"Nascimento\".");
		}
		if (row.getCell(index++) == null || !Uteis.removeCaractersEspeciais(row.getCell(index).getStringCellValue().toLowerCase()).equals("tipo de inscricao")) {
			throw new Exception("A coluna 'D' deve ser referente ao campo Tipo de inscrição, favor informe um título com a descrição \"tipo de inscricao\".");
		}
		
	}
	
	
	
	public void validarDadosMatriculaEnadeArquivoExcel(MatriculaEnadeVO matriculaEnadeVO, Map<String, MatriculaEnadeVO> mapMatriculaEnadeVO, Boolean lancarExcessao, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(matriculaEnadeVO.getMatriculaVO().getAluno().getNome())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo NOME do Aluno de CPF "+matriculaEnadeVO.getMatriculaVO().getAluno().getCPF()+" está Nulo.");
    		} else {
    			matriculaEnadeVO.setErro(matriculaEnadeVO.getErro() + " - NOME Nulo \n" );
    			matriculaEnadeVO.setPossuiErro(true);
    		}
    	}
		
		if (!Uteis.isAtributoPreenchido(matriculaEnadeVO.getMatriculaVO().getAluno().getCPF())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo CPF do Aluno NOME("+matriculaEnadeVO.getMatriculaVO().getAluno().getNome()+") - CPF("+matriculaEnadeVO.getMatriculaVO().getAluno().getCPF()+") está Nulo.");
    		} else {
    			matriculaEnadeVO.setErro("CPF Nulo \n");
    			matriculaEnadeVO.setPossuiErro(true);
    		}
    		
    	}
		
		String dataFormatada = Uteis.getData(matriculaEnadeVO.getMatriculaVO().getAluno().getDataNasc(), "dd/MM/yyyy");
    	if (!Uteis.isAtributoPreenchido(matriculaEnadeVO.getMatriculaVO().getAluno().getDataNasc())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA DE NASCIMENTO do Aluno NOME("+matriculaEnadeVO.getMatriculaVO().getAluno().getNome()+") - CPF("+matriculaEnadeVO.getMatriculaVO().getAluno().getCPF()+") está Nulo.");
    		} else {
    			if (!Uteis.isAtributoPreenchido(dataFormatada) && !Uteis.isAtributoPreenchido(matriculaEnadeVO.getMatriculaVO().getAluno().getDataNasc())) {
    				matriculaEnadeVO.setErro(matriculaEnadeVO.getErro() + " -  A Data de Nascimento está inválida, edite o arquivo e/ou o salve para convertê-la  \n" );
    				matriculaEnadeVO.setPossuiErro(true);
				} else {
					matriculaEnadeVO.setErro(matriculaEnadeVO.getErro() + " -  DATA NASCIMENTO Nulo \n" );
					matriculaEnadeVO.setPossuiErro(true);
				}
    		}
    		
    	}  	
    	if (mapMatriculaEnadeVO.containsKey(matriculaEnadeVO.getMatriculaVO().getAluno().getCPF())) {
    		matriculaEnadeVO.setErro(matriculaEnadeVO.getErro() + " - Aluno em Duplicidade \n" );
    		matriculaEnadeVO.setPossuiErro(true);
    	} else {
    		mapMatriculaEnadeVO.put(matriculaEnadeVO.getMatriculaVO().getAluno().getCPF(), matriculaEnadeVO);
    	}
    	
    	
    }
	
	
	@Override
	public MatriculaVO consultarPorCursoCpf(String cpfAluno, CursoVO curso,  UsuarioVO usuarioVO) throws Exception {		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct matricula.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\" , pessoa.dataNasc AS \"pessoa.dataNasc\" ,  ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", matricula.situacao AS \"matricula.situacao\" ");		
		sb.append(" FROM matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where  (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");		
		sb.append(" and curso.codigo = ").append(curso.getCodigo());      
		sb.append(" order by curso.nome, pessoa.nome limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), Uteis.retirarMascaraCPF(cpfAluno) + PERCENT);
		 MatriculaVO obj = new MatriculaVO();	
		if (!tabelaResultado.next()) {
			return obj;
		}
		montarDados(obj, tabelaResultado, usuarioVO);
		return obj; 
		

	}



	
}
