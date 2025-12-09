package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public class ImpressaoDeclaracaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7794875587348394499L;

	private MatriculaVO matricula;
	private FuncionarioVO professor;
	private Date dataGeracao;
	private UsuarioVO usuarioGeracao;
	private DisciplinaVO disciplina;
	private TurmaVO turma;
	private Integer codigo;
	private Boolean entregue;
	private Integer qtdeImpressoes;
	private TextoPadraoDeclaracaoVO textoPadraoDeclaracao;
	private RequerimentoVO requerimentoVO;
	private DocumentoAssinadoVO documentoAssinadoVO;

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public FuncionarioVO getProfessor() {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		return professor;
	}

	public void setProfessor(FuncionarioVO professor) {
		this.professor = professor;
	}

	public Date getDataGeracao() {
		if (dataGeracao == null) {
			dataGeracao = new Date();
		}
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public UsuarioVO getUsuarioGeracao() {
		if (usuarioGeracao == null) {
			usuarioGeracao = new UsuarioVO();
		}
		return usuarioGeracao;
	}

	public void setUsuarioGeracao(UsuarioVO usuarioGeracao) {
		this.usuarioGeracao = usuarioGeracao;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(TextoPadraoDeclaracaoVO textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	public String getTipoPessoa() {
		if (!getMatricula().getMatricula().trim().isEmpty()) {
			return TipoPessoa.ALUNO.getDescricao();
		}
		return TipoPessoa.PROFESSOR.getDescricao();
	}

	public Boolean getEntregue() {
		if (entregue == null) {
			entregue = Boolean.FALSE;
		}
		return entregue;
	}

	public void setEntregue(Boolean entregue) {
		this.entregue = entregue;
	}

	public Integer getQtdeImpressoes() {
		if (qtdeImpressoes == null) {
			qtdeImpressoes = 0;
		}
		return qtdeImpressoes;
	}

	public void setQtdeImpressoes(Integer qtdeImpressoes) {
		this.qtdeImpressoes = qtdeImpressoes;
	}
	
	public Boolean getIsApresentarDadosAluno() {
		return !getMatricula().getMatricula().equals("");
	}

	public Boolean getIsApresentarDadosProfessor() {
		return !getProfessor().getMatricula().equals("");
	}
	
	public RequerimentoVO getRequerimentoVO() {
		if(requerimentoVO == null){
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}
	
	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}

}
