package negocio.comuns.ead;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.facade.jdbc.blackboard.SalaAulaBlackboardOperacao;

public class SalaAulaBlackboardOperacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4259064944059328374L;
	private Integer codigo;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private GradeCurricularEstagioVO gradeCurricularEstagioVO;
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum;
	private String tipoOrigem;
	private Integer codigoOrigem;
	private String operacao;
	private PessoaVO pessoaVO;
	private TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum;
	private String ano;
	private String semestre;
	private Integer bimestre;
	private Integer nrSala;
	private String matricula;
	private Integer programacaoTutoriaOnline;
	private String listaAlunosEnsalar;
	private String erro;
	private String erroMsgNotificacao;
	private String msgNotificacao;
	private TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum;
	
	private String descricaoOperacao;
	
	public String getDescricaoOperacao() {
		if(descricaoOperacao == null) {
			descricaoOperacao = getOperacao_Apresentar()+" "+getTipoOrigem_Apresentar();
		}
		return descricaoOperacao;
	}
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	public TurmaVO getTurmaVO() {
		if(turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	public GradeCurricularEstagioVO getGradeCurricularEstagioVO() {
		if(gradeCurricularEstagioVO == null) {
			gradeCurricularEstagioVO = new GradeCurricularEstagioVO();
		}
		return gradeCurricularEstagioVO;
	}
	public void setGradeCurricularEstagioVO(GradeCurricularEstagioVO gradeCurricularEstagioVO) {
		this.gradeCurricularEstagioVO = gradeCurricularEstagioVO;
	}
	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if(salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}
	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}
	public TipoSalaAulaBlackboardEnum getTipoSalaAulaBlackboardEnum() {
		if(tipoSalaAulaBlackboardEnum == null) {
			tipoSalaAulaBlackboardEnum = TipoSalaAulaBlackboardEnum.NENHUM;
		}
		return tipoSalaAulaBlackboardEnum;
	}
	public void setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) {
		this.tipoSalaAulaBlackboardEnum = tipoSalaAulaBlackboardEnum;
	}
	private String tipoOrigem_Apresentar;
	public String getTipoOrigem_Apresentar() {
		if(tipoOrigem_Apresentar == null) {
			if(getTipoOrigem().equals(SalaAulaBlackboardOperacao.GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE) || getTipoOrigem().equals(SalaAulaBlackboardOperacao.GRUPO_SALA_AULA_BLACKBOARD_OPERACAO)) {
				tipoOrigem_Apresentar =  "Grupo Sala Aula";
			}else if(getTipoOrigem().equals(SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD)) {
				tipoOrigem_Apresentar =  "Sala Aula";
			}else if(getTipoOrigem().equals(SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_PESSOA)) {
				tipoOrigem_Apresentar =  "Membro Sala Aula";
			}else if(getTipoOrigem().equals(SalaAulaBlackboardOperacao.CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD)) {
				tipoOrigem_Apresentar =  "Consolidar Nota Ava";
			}else if(getTipoOrigem().equals(SalaAulaBlackboardOperacao.APURAR_NOTA_SALA_AULA_BLACBOARD)) {
				tipoOrigem_Apresentar =  "Carregar Nota Ava";
			}else {
				tipoOrigem_Apresentar = "";
			}
		}
		return tipoOrigem_Apresentar;
	}
	public String getTipoOrigem() {
		if(tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}
	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}
	public Integer getCodigoOrigem() {
		if(codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}
	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}
	public String getOperacao() {
		if(operacao == null) {
			operacao = "";
		}
		return operacao;
	}
	public String  operacao_Apresentar;
	public String getOperacao_Apresentar() {
		if(operacao_Apresentar == null) {
		if(getOperacao().endsWith(SalaAulaBlackboardOperacao.OPERACAO_INCLUIR)) {
			operacao_Apresentar = "Inclusão";
		}else if(getOperacao().endsWith(SalaAulaBlackboardOperacao.OPERACAO_DELETAR)) {
			operacao_Apresentar = "Exclusão";
		}else {
			operacao_Apresentar ="";
		}
		}
		return operacao_Apresentar;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public PessoaVO getPessoaVO() {
		if(pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}
	public TipoSalaAulaBlackboardPessoaEnum getTipoSalaAulaBlackboardPessoaEnum() {
		if(tipoSalaAulaBlackboardPessoaEnum == null) {
			tipoSalaAulaBlackboardPessoaEnum = TipoSalaAulaBlackboardPessoaEnum.NENHUM;
		}
		return tipoSalaAulaBlackboardPessoaEnum;
	}
	public void setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum) {
		this.tipoSalaAulaBlackboardPessoaEnum = tipoSalaAulaBlackboardPessoaEnum;
	}
	public String getAno() {
		if(ano == null) {
			ano = "";
		}
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		if(semestre == null) {
			semestre = "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public Integer getBimestre() {
		if(bimestre == null) {
			bimestre = 0;
		}
		return bimestre;
	}
	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}
	public Integer getNrSala() {
		if(nrSala == null) {
			nrSala = 0;
		}
		return nrSala;
	}
	public void setNrSala(Integer nrSala) {
		this.nrSala = nrSala;
	}
	public String getMatricula() {
		if(matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Integer getProgramacaoTutoriaOnline() {
		if(programacaoTutoriaOnline == null) {
			programacaoTutoriaOnline = 0;
		}
		return programacaoTutoriaOnline;
	}
	public void setProgramacaoTutoriaOnline(Integer programacaoTutoriaOnline) {
		this.programacaoTutoriaOnline = programacaoTutoriaOnline;
	}
	public String getListaAlunosEnsalar() {
		if(listaAlunosEnsalar == null) {
			listaAlunosEnsalar = "";
		}
		return listaAlunosEnsalar;
	}
	public void setListaAlunosEnsalar(String listaAlunosEnsalar) {
		this.listaAlunosEnsalar = listaAlunosEnsalar;
	}
	public String getErro() {
		if(erro == null) {
			erro ="";
		}
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getErroMsgNotificacao() {
		if (erroMsgNotificacao == null) {
			erroMsgNotificacao = "";
		}
		return erroMsgNotificacao;
	}

	public void setErroMsgNotificacao(String erroMsgNotificacao) {
		this.erroMsgNotificacao = erroMsgNotificacao;
	}

	public String getMsgNotificacao() {
		if (msgNotificacao == null) {
			msgNotificacao = "";
		}
		return msgNotificacao;
	}

	public void setMsgNotificacao(String msgNotificacao) {
		this.msgNotificacao = msgNotificacao;
	}

	public TemplateMensagemAutomaticaEnum getTemplateMensagemAutomaticaEnum() {
		return templateMensagemAutomaticaEnum;
	}

	public void setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum) {
		this.templateMensagemAutomaticaEnum = templateMensagemAutomaticaEnum;
	}
	
	
	
	
	
}
