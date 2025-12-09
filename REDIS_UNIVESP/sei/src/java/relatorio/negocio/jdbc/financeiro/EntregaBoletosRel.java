package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.EntregaBoletosRelVO;
import relatorio.negocio.interfaces.financeiro.EntregaBoletosRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EntregaBoletosRel extends SuperRelatorio implements EntregaBoletosRelInterfaceFacade {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7357645672808468492L;

	public static void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, boolean filtrarPorAluno, String ano, String semestre) throws Exception {
		if (filtrarPorAluno) {
			if (matriculaVO == null || matriculaVO.getMatricula().equals("")) {
				throw new ConsistirException("O campo MATRÍCULA deve ser informado.");
			}
		} else {
			if (turmaVO == null || turmaVO.getCodigo().equals(0)) {
				throw new ConsistirException("O campo TURMA deve ser informado.");
			}			
			if (ano.equals("") && (turmaVO.getSemestral() || turmaVO.getAnual())) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			}
			if (semestre.equals("") && turmaVO.getSemestral()) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");
			}						
		}
	}

	@Deprecated
	public List<EntregaBoletosRelVO> criarObjeto(List<MatriculaVO> listaMatriculaVO, boolean relatorioPorTurma, boolean apresentarCampoData, TurmaVO turma, int periodoLetivo, Boolean periodoLetivoControle, String tipoDocumento, String ano, String semestre) throws Exception {
		
		List<EntregaBoletosRelVO> listaEntregaBoletosRelVO = new ArrayList<EntregaBoletosRelVO>(0);
		EntregaBoletosRelVO entregaBoletosRelVO = new EntregaBoletosRelVO();
		if (relatorioPorTurma) {
			for (MatriculaVO matricula : listaMatriculaVO) {
				entregaBoletosRelVO = consultarTodosDadosEntregaBoletosRelVO(matricula, periodoLetivo, turma, periodoLetivoControle, ano, semestre, tipoDocumento);
				if (tipoDocumento.equalsIgnoreCase("boleto")) {
					if (entregaBoletosRelVO.getQtdeParcelas() > 0) {
						entregaBoletosRelVO.setApresentarCampoData(apresentarCampoData);
						listaEntregaBoletosRelVO.add(entregaBoletosRelVO);
					}
				} else {
					if (entregaBoletosRelVO.getMatriculaVO() != null && !entregaBoletosRelVO.getMatriculaVO().getMatricula().equals("")) {
						entregaBoletosRelVO.setQtdeParcelas(0);
						listaEntregaBoletosRelVO.add(entregaBoletosRelVO);
					}
				}				
				entregaBoletosRelVO = new EntregaBoletosRelVO();
			}
		} else {
			entregaBoletosRelVO = consultarTodosDadosEntregaBoletosRelVO(listaMatriculaVO.get(0), periodoLetivo, turma, periodoLetivoControle, ano, semestre, tipoDocumento);
			if (tipoDocumento.equalsIgnoreCase("boleto")) {
				if (entregaBoletosRelVO.getQtdeParcelas() > 0) {
					entregaBoletosRelVO.setApresentarCampoData(apresentarCampoData);
					listaEntregaBoletosRelVO.add(entregaBoletosRelVO);
				}
			} else {
				entregaBoletosRelVO.setQtdeParcelas(0);
				listaEntregaBoletosRelVO.add(entregaBoletosRelVO);
			}			
		}

		return listaEntregaBoletosRelVO;
	}

	@Deprecated
	public EntregaBoletosRelVO consultarTodosDadosEntregaBoletosRelVO(MatriculaVO matricula, int periodoLetivo, TurmaVO turma, Boolean periodoLetivoControle, String ano, String semestre, String tipoDocumento) throws Exception {
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina = new MatriculaPeriodoTurmaDisciplinaVO();
		MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
		if (periodoLetivo == 0) {
			if (ano.equals("") && (turma.getAnual() || turma.getSemestral())) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			if (semestre.equals("") && (turma.getAnual() || turma.getSemestral())) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}	
			if (turma.getTurmaAgrupada()) {											
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(matricula.getMatricula(), semestre, ano, false, null);
			} else {								
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(matricula.getMatricula(), semestre, ano, false, null);
				if (matriculaPeriodoVO == null || matriculaPeriodoVO.getCodigo().equals(0)) {
					matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
				}
			}
		} else {
			matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarRapidaPorMatriculaPeriodoLetivoMatricula(matricula.getMatricula(), periodoLetivo, false, null);
		}
		if (matriculaPeriodoVO == null || matriculaPeriodoVO.getCodigo().equals(0)) {
			matriculaPeriodoTurmaDisciplina.setMatriculaPeriodo(0);
		} else {
			matriculaPeriodoTurmaDisciplina.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
		}
		Integer qtdeParcelas = 0;
		if(tipoDocumento.equalsIgnoreCase("boleto")){
			qtdeParcelas = getFacadeFactory().getContaReceberFacade().consultarQtdeContasPorMatriculaPeriodo(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo(), "MAT", "MEN", null);
		}
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		if (turma.getCodigo() != 0) {
			if (turma.getTurmaAgrupada()) {
				listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodoTurma(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo(), turma, false, null);
			} else {
				listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodoTurma(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo(), turma, false, null);
			}
		} else {
			listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo(), false, null);
		}
		if (listaMatriculaPeriodoTurmaDisciplina.size() > 0) {
			matriculaPeriodoTurmaDisciplina = listaMatriculaPeriodoTurmaDisciplina.get(0);
			if (turma.getCodigo() != 0) {
				matriculaPeriodoTurmaDisciplina.setTurma(turma);
			}
		} else {
			return new EntregaBoletosRelVO();
		}
		consultarCidadeUnidadeEnsino(matricula);
		return montarDados(matricula, matriculaPeriodoTurmaDisciplina, qtdeParcelas, matriculaPeriodoVO, periodoLetivoControle);
	}

	@Deprecated
	public EntregaBoletosRelVO montarDados(MatriculaVO matricula, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina, Integer qtdeParcelas, MatriculaPeriodoVO matriculaPeriodoVO, Boolean periodoLetivoControle) {
		EntregaBoletosRelVO entregaBoletosRelVO = new EntregaBoletosRelVO();
		entregaBoletosRelVO.setMatriculaVO(matricula);
		entregaBoletosRelVO.setTurma(matriculaPeriodoTurmaDisciplina.getTurma().getIdentificadorTurma());
		entregaBoletosRelVO.setTurmaAgrupada(matriculaPeriodoTurmaDisciplina.getTurma().getTurmaAgrupada());
		entregaBoletosRelVO.setQtdeParcelas(qtdeParcelas);
		if (periodoLetivoControle != null && periodoLetivoControle) {
			entregaBoletosRelVO.setPeriodoLetivo(", no(a) " + matriculaPeriodoVO.getPeridoLetivo().getDescricao());
		} else {
			entregaBoletosRelVO.setPeriodoLetivo("");
		}

		entregaBoletosRelVO.setDataCidadePorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		return entregaBoletosRelVO;
	}
		
	
	public List<EntregaBoletosRelVO> montarDadosConsulta(SqlRowSet rs, String tipoDocumento, Boolean periodoLetivoControle, Boolean carregarFotoAluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		List<EntregaBoletosRelVO> objs = new ArrayList<EntregaBoletosRelVO>();
		Date dataBase = new Date();
		while(rs.next()){
			objs.add(montarDados(rs, tipoDocumento, periodoLetivoControle, dataBase, carregarFotoAluno, configuracaoGeralSistemaVO));
		}
		return objs;
	}
	
	public EntregaBoletosRelVO montarDados(SqlRowSet rs, String tipoDocumento, Boolean periodoLetivoControle, Date dataBase, Boolean carregarFotoAluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		EntregaBoletosRelVO entregaBoletosRelVO = new EntregaBoletosRelVO();
		entregaBoletosRelVO.getMatriculaVO().setMatricula(rs.getString("matricula"));	
		entregaBoletosRelVO.getMatriculaVO().getCurso().setNome(rs.getString("curso"));	
		entregaBoletosRelVO.getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino"));	
		entregaBoletosRelVO.getMatriculaVO().getAluno().setNome(rs.getString("aluno"));
		entregaBoletosRelVO.getMatriculaVO().getAluno().setRG(rs.getString("rg"));
		entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setCodigo(rs.getInt("codigoArquivoPessoa"));
		entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setNome(rs.getString("nomeArquivoPessoa"));
		entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setPastaBaseArquivo(rs.getString("pastaBaseArquivoPessoa"));
		if(carregarFotoAluno){
			entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/resources/imagens/visao/foto_usuario.png");
			if(rs.getInt("codigoArquivoPessoa") > 0){
				File arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + rs.getString("cpfrequerimento") + File.separator + entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().getNome());
				if(arquivoImagem.exists()){
					entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + rs.getString("cpfrequerimento") + "/" + entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().getNome());					
				}else{
					arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + rs.getString("cpf") + File.separator + entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().getNome());
					if(arquivoImagem.exists()){
						entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + rs.getString("cpf") + "/" + entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().getNome());
					}else {
						arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + rs.getString("pastaBaseArquivoPessoa") + File.separator + entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().getNome());
						if(arquivoImagem.exists()){
							entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + rs.getString("pastaBaseArquivoPessoa").replaceAll("\\", "/") + "/" + entregaBoletosRelVO.getMatriculaVO().getAluno().getArquivoImagem().getNome());
						}
					}
				}
			}
		}
		if(tipoDocumento.equals("boleto")){
			entregaBoletosRelVO.setQtdeParcelas(rs.getInt("qtdeParcelas"));
		}
		if (periodoLetivoControle != null && periodoLetivoControle) {
			entregaBoletosRelVO.setPeriodoLetivo(", no(a) " + rs.getString("periodoLetivo"));
		} else {
			entregaBoletosRelVO.setPeriodoLetivo("");
		}
		entregaBoletosRelVO.setDataCidadePorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(rs.getString("cidade")+" - "+rs.getString("estado"), dataBase, false));
		return entregaBoletosRelVO;
	}
	@Override
	public List<EntregaBoletosRelVO> realizarGeracaoRelatorio(String tipoDocumento, Boolean periodoLetivoControle, Boolean filtrarPorTurma, TurmaVO turma, MatriculaVO matriculaVO, Integer periodoLetivo, String ano, String semestre, String tipoAluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,DisciplinaVO disciplinaVO, Boolean carregarFotoAluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception{
		validarDados(matriculaVO, turma, !filtrarPorTurma, ano, semestre);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT  matricula.matricula, curso.nome as curso, unidadeensino.nome as unidadeEnsino, pessoa.nome as aluno, pessoa.rg as rg, pessoa.cpf as cpf, periodoletivo.descricao as periodoLetivo, cidade.nome as cidade, estado.sigla as estado, ");
		sqlStr.append(" arquivo.pastaBaseArquivo as pastaBaseArquivoPessoa, arquivo.cpfrequerimento, arquivo.nome as nomeArquivoPessoa , arquivo.codigo as codigoArquivoPessoa ");
		if(tipoDocumento.equals("boleto")){
			sqlStr.append(", (SELECT COUNT(contareceber.codigo) AS qtde FROM contareceber WHERE contareceber.matriculaPeriodo = matriculaPeriodo.codigo "); 
			sqlStr.append(" AND contareceber.tipoOrigem IN ('MAT', 'MEN') ) as qtdeParcelas ");
		}
		  sqlStr.append(" FROM matricula ");
		    sqlStr.append(" INNER JOIN curso         ON curso.codigo         = matricula.curso ");
		    sqlStr.append(" INNER JOIN pessoa        ON pessoa.codigo        = matricula.aluno ");
		    sqlStr.append(" left JOIN arquivo      ON pessoa.arquivoimagem        = arquivo.codigo ");
		    sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		    sqlStr.append(" INNER JOIN cidade        ON unidadeensino.cidade = cidade.codigo ");
	     	sqlStr.append(" INNER JOIN estado        ON estado.codigo        = cidade.estado ");
		if(periodoLetivoControle && Uteis.isAtributoPreenchido(periodoLetivo)){
			sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
			sqlStr.append(" AND matriculaperiodo.codigo  = (SELECT mp.codigo FROM matriculaperiodo mp ");
			sqlStr.append(" INNER JOIN periodoletivo pl ON pl.codigo = mp.periodoletivomatricula ");
			sqlStr.append(" WHERE mp.matricula = matricula.matricula ");
			sqlStr.append(" AND pl.periodoletivo = ").append(periodoLetivo);
			sqlStr.append(" ORDER BY (mp.ano ||'/' || mp.semestre) DESC , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		}else{
			sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
			sqlStr.append(" AND matriculaperiodo.codigo  = (SELECT mp.codigo FROM matriculaperiodo mp WHERE mp.matricula = matricula.matricula ");
			if(filtrarPorTurma && !turma.getIntegral()){
				sqlStr.append(" AND mp.ano = '").append(ano).append("' ");
			}
			if(filtrarPorTurma && turma.getSemestral()){
				sqlStr.append(" AND mp.semestre = '").append(semestre).append("' ");
			}
			sqlStr.append(" ORDER BY (mp.ano ||'/' || mp.semestre) desc , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		}
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = MatriculaPeriodo.codigo ");
		if (turma.getSubturma() && (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) || turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))) {
			if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN turma ON turma.codigo = mptd.turmaTeorica ");
			} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN turma ON turma.codigo = mptd.turmaPratica ");
			}
		} else {
			sqlStr.append("INNER JOIN turma ON turma.codigo = mptd.turma ");
		}	 
		
		if (Uteis.isAtributoPreenchido(disciplinaVO.getCodigo())) {
			sqlStr.append("INNER JOIN disciplina ON disciplina.codigo = mptd.disciplina ");
		} 
		sqlStr.append(" INNER JOIN periodoletivo ON periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		if(!filtrarPorTurma){
			sqlStr.append(" WHERE matricula.matricula = '").append(matriculaVO.getMatricula()).append("' ");
			
		}else{
			sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			if(filtrarPorTurma && !turma.getIntegral()){
				sqlStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
			}
			if(filtrarPorTurma && turma.getSemestral()){
				sqlStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
			if (Uteis.isAtributoPreenchido(disciplinaVO.getCodigo())) {
				sqlStr.append(" AND (disciplina.codigo =  ").append(disciplinaVO.getCodigo()).append(" or disciplina.codigo in (");
				sqlStr.append(" SELECT disciplinaequivalente.disciplina FROM disciplinaequivalente WHERE disciplinaequivalente.equivalente = ").append(disciplinaVO.getCodigo());
				sqlStr.append(" UNION SELECT disciplinaequivalente.equivalente FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = ").append(disciplinaVO.getCodigo());
				sqlStr.append(" )) ");
			}
			if (tipoAluno.equals("NORMAL")){
				sqlStr.append(" AND mptd.turma = matriculaPeriodo.turma ");
			}else if (tipoAluno.equals("REPOSICAO")){
				sqlStr.append(" AND mptd.turma != matriculaPeriodo.turma ");
			}
			if (turma.getCodigo() != 0 && !turma.getSubturma() && turma.getTurmaAgrupada()) {
				sqlStr.append(" AND (turma.codigo IN (SELECT turmaagrupada.turma FROM turmaagrupada ");
				if (disciplinaVO != null && !disciplinaVO.getCodigo().equals(0)) {
					sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.turma = turmaagrupada.turmaorigem ");
					sqlStr.append(" AND (turmadisciplina.disciplina = ").append(disciplinaVO.getCodigo()).append(" OR turmadisciplina.disciplina IN ( ");
					sqlStr.append(" 	SELECT disciplinaequivalente.disciplina FROM disciplinaequivalente WHERE disciplinaequivalente.equivalente = ").append(disciplinaVO.getCodigo());
					sqlStr.append(" 	UNION ");
					sqlStr.append(" 	SELECT disciplinaequivalente.equivalente FROM disciplinaequivalente WHERE disciplinaequivalente.disciplina = ").append(disciplinaVO.getCodigo());
					sqlStr.append("))");
				}
				sqlStr.append(" WHERE turmaorigem = ").append(turma.getCodigo()).append(")");
				sqlStr.append("OR (mptd.turmaPratica IN (SELECT TurmaAgrupada.turma FROM TurmaAgrupada INNER JOIN turma AS turmaOrigem ON turmaOrigem.codigo = TurmaAgrupada.turmaOrigem INNER JOIN turma ON turma.codigo = turmaagrupada.turma WHERE turmaOrigem.codigo = ").append(turma.getCodigo()).append(" AND turmaOrigem.subturma = false AND turma.tiposubturma = 'PRATICA'))");
				sqlStr.append("OR (mptd.turmaTeorica IN (SELECT TurmaAgrupada.turma FROM TurmaAgrupada INNER JOIN turma AS turmaOrigem ON turmaOrigem.codigo = TurmaAgrupada.turmaOrigem INNER JOIN turma ON turma.codigo = turmaagrupada.turma WHERE turmaOrigem.codigo = ").append(turma.getCodigo()).append(" AND turmaOrigem.subturma = false AND turma.tiposubturma = 'TEORICA'))");
				sqlStr.append(") ");
			} else if (turma.getCodigo() != 0) {
				if (Uteis.isAtributoPreenchido(disciplinaVO.getCodigo())) {
					sqlStr.append(" AND (turma.codigo = ").append(turma.getCodigo()).append(") ");
				}else {
					sqlStr.append(" AND (turma.codigo = ").append(turma.getCodigo()).append(" or matriculaperiodo.turma = ").append(turma.getCodigo()).append(") ");
				}
			}
		}
		if(tipoDocumento.equals("boleto")){
			sqlStr.append(" AND EXISTS (SELECT contareceber.codigo AS qtde FROM contareceber WHERE contareceber.matriculaPeriodo = matriculaPeriodo.codigo "); 
			sqlStr.append(" AND contareceber.tipoorigem IN ('MAT','MEN') ) ");
		}
		sqlStr.append(" GROUP BY matricula.matricula, curso.nome, unidadeensino.nome, pessoa.nome, pessoa.rg, periodoletivo.descricao, cidade.nome, estado.sigla, matriculaperiodo.codigo, pessoa.cpf, ");
		sqlStr.append(" arquivo.pastaBaseArquivo, arquivo.nome , arquivo.codigo, arquivo.cpfrequerimento ");
		sqlStr.append(" ORDER BY aluno ;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		return montarDadosConsulta(tabelaResultado, tipoDocumento, periodoLetivoControle, carregarFotoAluno, configuracaoGeralSistemaVO);
	}

	@Deprecated
	public void consultarCidadeUnidadeEnsino(MatriculaVO matricula) throws Exception {
		matricula.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(matricula.getUnidadeEnsino().getCodigo(), false, null));
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorioAta() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeAta() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}
	
	public static String getIdEntidade() {
		return ("EntregaBoletosRel");
	}

	public static String getIdEntidadeAta() {
		return ("DeclaracaoEntregaBoletosRel");
	}
	
	public static String getLayoutListaPresencaComFoto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ListaPresencaComFotoRel.jrxml");		
	}

}