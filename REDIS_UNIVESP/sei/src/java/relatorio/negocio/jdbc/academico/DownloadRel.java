package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.DownloadVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DownloadRelVO;
import relatorio.negocio.interfaces.academico.DownloadRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class DownloadRel extends SuperRelatorio implements DownloadRelInterfaceFacade {

	
	protected String mensagem;

	public DownloadRel() {
		setMensagem("");
	}

	public List<DownloadRelVO> criarObjeto(DownloadVO downloadVO, String ano, String semestre) throws Exception {
		List<DownloadRelVO> downloadRelVOs = new ArrayList<DownloadRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(downloadVO, ano, semestre);
		while (dadosSQL.next()) {
			downloadRelVOs.add(montarDados(dadosSQL));
		}
		return downloadRelVOs;
	}

	public DownloadRelVO montarDados(SqlRowSet dadosSQL) {
		DownloadRelVO downloadRelVO = new DownloadRelVO();
		downloadRelVO.setCodigo(dadosSQL.getInt("codigo"));		
		downloadRelVO.setDataDownload(dadosSQL.getString("download_datadownload"));
		downloadRelVO.setDescricaoArquivo(dadosSQL.getString("arquivo_desc"));
		downloadRelVO.setHoraDownload(dadosSQL.getString("download_horadownload"));
		downloadRelVO.setNomeCurso(dadosSQL.getString("curso_nome"));
		downloadRelVO.setNomeDisciplina(dadosSQL.getString("disciplina_nome"));
		downloadRelVO.setNomeGrade(dadosSQL.getString("gradecurricular_nome"));
		downloadRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome"));
		downloadRelVO.setNomeTurma(dadosSQL.getString("turma_identificadorturma"));
		downloadRelVO.setNomeTurno(dadosSQL.getString("turno_nome"));
		downloadRelVO.setPeriodoLetivo(dadosSQL.getString("periodoletivo_descricao"));
		downloadRelVO.setUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		downloadRelVO.setNomeArquivo(dadosSQL.getString("arquivo_nome"));
		downloadRelVO.setMatricula(dadosSQL.getString("matricula"));
		return downloadRelVO;
	}

	public SqlRowSet executarConsultaParametrizada(DownloadVO downloadVO, String ano, String semestre) throws Exception {
		StringBuilder string = new StringBuilder();
		string.append("SELECT distinct download.codigo, arquivo.descricao AS arquivo_desc, pessoa.nome AS pessoa_nome, case when arquivo.descricao != '' and arquivo.descricao is not null then arquivo.descricao else arquivo.nome end AS arquivo_nome, to_char(download.datadownload, 'DD/MM/YY') AS download_datadownload, ");
		string.append("to_char(download.datadownload, 'HH24:MI') AS download_horadownload, disciplina.nome AS disciplina_nome, turma.identificadorturma AS turma_identificadorturma, gradecurricular.nome AS ");
		string.append("gradecurricular_nome, turno.nome AS turno_nome, periodoletivo.descricao AS periodoletivo_descricao, curso.nome AS curso_nome, ");
		string.append("unidadeensino.nome AS unidadeensino_nome, matricula.matricula, download.datadownload ");
		string.append("FROM download ");
		string.append("INNER JOIN arquivo ON arquivo.codigo = download.arquivo ");
		string.append("left JOIN turma ON turma.codigo = download.turma ");
		string.append("left JOIN curso ON curso.codigo = turma.curso ");
		string.append("left JOIN gradecurricular ON turma.gradecurricular = gradecurricular.codigo ");
		string.append("INNER JOIN turno ON turno.codigo = turma.turno ");
		string.append("left JOIN periodoletivo ON periodoletivo.codigo = turma.periodoletivo ");
		string.append("INNER JOIN disciplina ON disciplina.codigo = download.disciplina ");
		string.append("INNER JOIN pessoa ON pessoa.codigo = download.pessoa ");
		string.append("INNER JOIN unidadeensino ON unidadeensino.codigo = turma.unidadeensino ");
		string.append("inner join matriculaperiodo on matriculaperiodo.codigo = download.matriculaperiodo ");
		string.append("inner join matricula on matriculaperiodo.matricula = matricula.matricula ");
		string.append("inner join curso as cursomat on cursomat.codigo = matricula.curso ");
		string.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		string.append(" and matriculaperiodoturmadisciplina.disciplina = download.disciplina ");
		if (downloadVO.getArquivo().getTurma().getCodigo() > 0) {			
			if(downloadVO.getArquivo().getTurma().getTurmaAgrupada() && !downloadVO.getArquivo().getTurma().getSubturma()){
				string.append(" and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = arquivo.turma and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma) ");
			}else if(downloadVO.getArquivo().getTurma().getSubturma() && downloadVO.getArquivo().getTurma().getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)){				
				string.append(" and matriculaperiodoturmadisciplina.turmapratica = arquivo.turma ");
			}else if(downloadVO.getArquivo().getTurma().getSubturma() && downloadVO.getArquivo().getTurma().getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)){				
				string.append(" and matriculaperiodoturmadisciplina.turmateorica = arquivo.turma ");
			}else{				
				string.append(" and matriculaperiodoturmadisciplina.turma = turma.codigo ");
			}			
		}else{
			string.append(" and (turma.codigo is null or (turma.codigo is not null and matriculaperiodoturmadisciplina.turma = turma.codigo and turma.turmaagrupada = false and (turma.subturma = false or (turma.subturma  and turma.tiposubturma = 'GERAL'))) ");
			string.append(" or (turma.codigo is not null and turma.subturma and turma.tiposubturma = 'PRATICA' and matriculaperiodoturmadisciplina.turmapratica = arquivo.turma) ");
			string.append(" or (turma.codigo is not null and turma.subturma and turma.tiposubturma = 'TEORICA' and matriculaperiodoturmadisciplina.turmateorica = arquivo.turma) ");
			string.append(" or (turma.codigo is not null and turma.subturma = false and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = arquivo.turma and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma))) ");
		}
		string.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		string.append(" WHERE disciplina.codigo = '" + downloadVO.getArquivo().getDisciplina().getCodigo() + "' ");
		
		if (downloadVO.getArquivo().getTurma().getCodigo() > 0) {
			string.append(" AND (turma.codigo is null or turma.codigo = '" + downloadVO.getArquivo().getTurma().getCodigo() + "') ");
		}
		if(!ano.trim().isEmpty()){
			string.append(" and cursomat.periodicidade != 'IN' and matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if(!semestre.trim().isEmpty()){
			string.append(" and cursomat.periodicidade = 'SE' and matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and ");		
		//string.append("GROUP BY download.codigo, pessoa_nome, case when arquivo.descricao != '' and arquivo.descricao != null then arquivo.descricao else arquivo.nome end, download_datadownload, disciplina_nome, turma_identificadorturma, gradecurricular_nome, turno_nome, periodoletivo_descricao, ");
		//string.append("curso_nome, unidadeensino_nome, download_horadownload, arquivo_desc, matricula.matricula ");
		string.append(" order by unidadeensino_nome, curso_nome, turma_identificadorturma, disciplina_nome, arquivo_nome, pessoa_nome, datadownload ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(string.toString());
		return tabelaResultado;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("DownloadRel");
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
