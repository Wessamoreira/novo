package relatorio.negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class AlunosPorUnidadeCursoTurmaRelVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6296620693837615454L;
	private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
	private String unidadeEnsino;
	private String curso;
	private String turno;
	private String turma;
	private String turmaBase;
	private String turmaPratica;
	private String turmaTeorica;
	private String disciplina;
	private String semestre;
	private String ano;
	private String matricula;
	private String situacao;
	private String situacaoMatriculaPeriodo;
	private String nomeAluno;
	private Integer codigoAluno;
	private String dataNascimento;
	private String cpf;
	private String telefoneRes;
	private String celular;
	private String email;
	private String turmaReposicao;
	private String disciplinaReposicao;
	private Boolean alunoAbandonouCurso;
	private String rg;
	private String orgaoEmissor;
	private String estadoEmissaoRg;
	private String dataEmissaoRg;
	private String endereco;
	private String setor;
	private String cep;
	private String cidade;
	private List<FiliacaoVO> filiacaoVOs;

	public AlunosPorUnidadeCursoTurmaRelVO() {
	}

	public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioVO() {
		if (processoMatriculaCalendarioVO == null) {
			processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
		}
		return processoMatriculaCalendarioVO;
	}

	public void setProcessoMatriculaCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
		this.processoMatriculaCalendarioVO = processoMatriculaCalendarioVO;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = "";
		}
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public Integer getCodigoAluno() {
		if (codigoAluno == null) {
			codigoAluno = 0;
		}
		return codigoAluno;
	}

	public void setCodigoAluno(Integer codigoAluno) {
		this.codigoAluno = codigoAluno;
	}

	public String getDataNascimento() {
		if (dataNascimento == null) {
			dataNascimento = "";
		}
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = "";
		}
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTurmaReposicao() {
		if (turmaReposicao == null) {
			turmaReposicao = "";
		}
		return turmaReposicao;
	}

	public void setTurmaReposicao(String turmaReposicao) {
		this.turmaReposicao = turmaReposicao;
	}

	public String getDisciplinaReposicao() {
		if (disciplinaReposicao == null) {
			disciplinaReposicao = "";
		}
		return disciplinaReposicao;
	}

	public void setDisciplinaReposicao(String disciplinaReposicao) {
		this.disciplinaReposicao = disciplinaReposicao;
	}

	public Boolean getAlunoAbandonouCurso() {
		if (alunoAbandonouCurso == null) {
			alunoAbandonouCurso = false;
		}
		return alunoAbandonouCurso;
	}

	public void setAlunoAbandonouCurso(Boolean alunoAbandonouCurso) {
		this.alunoAbandonouCurso = alunoAbandonouCurso;
	}

	public String getRg() {
		if (rg == null) {
			rg = "";
		}
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgaoEmissor() {
		if (orgaoEmissor == null) {
			orgaoEmissor = "";
		}
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getEstadoEmissaoRg() {
		if (estadoEmissaoRg == null) {
			estadoEmissaoRg = "";
		}
		return estadoEmissaoRg;
	}

	public void setEstadoEmissaoRg(String estadoEmissaoRg) {
		this.estadoEmissaoRg = estadoEmissaoRg;
	}

	public String getDataEmissaoRg() {
		if (dataEmissaoRg == null) {
			dataEmissaoRg = "";
		}
		return dataEmissaoRg;
	}

	public void setDataEmissaoRg(String dataEmissaoRg) {
		this.dataEmissaoRg = dataEmissaoRg;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public List<FiliacaoVO> getFiliacaoVOs() {
		if (filiacaoVOs == null) {
			filiacaoVOs = new ArrayList<FiliacaoVO>(0);
		}
		return filiacaoVOs;
	}

	public void setFiliacaoVOs(List<FiliacaoVO> filiacaoVOs) {
		this.filiacaoVOs = filiacaoVOs;
	}

	public JRDataSource getFiliacaoJRVOs() {
		return new JRBeanArrayDataSource(getFiliacaoVOs().toArray());
	}

	public String getSituacaoMatriculaPeriodoApresentar() {
		if (situacaoMatriculaPeriodo == null || situacaoMatriculaPeriodo.trim().equals("")) {
			return "";
		}
		return SituacaoMatriculaPeriodoEnum.getEnumPorValor(situacaoMatriculaPeriodo).getDescricao();
	}
	

	public String getTurmaPratica() {
		if(turmaPratica == null){
			turmaPratica = "";
		}
		return turmaPratica;
	}

	public void setTurmaPratica(String turmaPratica) {
		this.turmaPratica = turmaPratica;
	}

	public String getTurmaTeorica() {
		if(turmaTeorica == null){
			turmaTeorica = "";
		}
		return turmaTeorica;
	}

	public void setTurmaTeorica(String turmaTeorica) {
		this.turmaTeorica = turmaTeorica;
	}

	public String getTurmaBase() {
		if(turmaBase == null){
			turmaBase = "";
		}
		return turmaBase;
	}

	public void setTurmaBase(String turmaBase) {
		this.turmaBase = turmaBase;
	}
	
	
}
