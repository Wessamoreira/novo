package negocio.comuns.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.utilitarias.Uteis;

public class CalendarioAgrupamentoTccVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504516024374743499L;
	private Integer codigo;
	private String ano;
	private String semestre;
	private Date dataInicial;
	private Date dataFinal;
	private ClassificacaoDisciplinaEnum classificacaoAgrupamento;
	/***
	 * Transients
	 */
	@ExcluirJsonAnnotation
	private String disciplinaApresentarConsulta;
	@ExcluirJsonAnnotation
	private SalaAulaBlackboardVO salaAulaBlackboardAmbientacao;
	private SalaAulaBlackboardVO salaAulaBlackboardGrupo;
	private boolean periodoLiberado = false;
	private boolean permiteAdicionarUsuarioGrupo = false;	
	private boolean alunoJaAprovado = false;	
	private List<CalendarioAgrupamentoDisciplinaVO> calendarioAgrupamentoDisciplinaVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	public String getDataInicial_Apresentar() {
        return (Uteis.getData(getDataInicial()));
    }

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	public String getDataFinal_Apresentar() {
        return (Uteis.getData(getDataFinal()));
    }

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public ClassificacaoDisciplinaEnum getClassificacaoAgrupamento() {
		if (classificacaoAgrupamento == null) {
			classificacaoAgrupamento = ClassificacaoDisciplinaEnum.NENHUMA;
		}
		return classificacaoAgrupamento;
	}

	public void setClassificacaoAgrupamento(ClassificacaoDisciplinaEnum classificacaoAgrupamento) {
		this.classificacaoAgrupamento = classificacaoAgrupamento;
	}

	

	public SalaAulaBlackboardVO getSalaAulaBlackboardAmbientacao() {
		if (salaAulaBlackboardAmbientacao == null) {
			salaAulaBlackboardAmbientacao = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardAmbientacao;
	}

	public void setSalaAulaBlackboardAmbientacao(SalaAulaBlackboardVO salaAulaBlackboardAmbientacao) {
		this.salaAulaBlackboardAmbientacao = salaAulaBlackboardAmbientacao;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardGrupo() {
		if (salaAulaBlackboardGrupo == null) {
			salaAulaBlackboardGrupo = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardGrupo;
	}

	public void setSalaAulaBlackboardGrupo(SalaAulaBlackboardVO salaAulaBlackboardGrupo) {
		this.salaAulaBlackboardGrupo = salaAulaBlackboardGrupo;
	}

	public boolean isPeriodoLiberado() {
		return periodoLiberado;
	}

	public void setPeriodoLiberado(boolean periodoLiberado) {
		this.periodoLiberado = periodoLiberado;
	}

	public boolean isPermiteAdicionarUsuarioGrupo() {		
		return permiteAdicionarUsuarioGrupo;
	}

	public void setPermiteAdicionarUsuarioGrupo(boolean permiteAdicionarUsuarioGrupo) {
		this.permiteAdicionarUsuarioGrupo = permiteAdicionarUsuarioGrupo;
	}

	public boolean isAlunoJaAprovado() {
		return alunoJaAprovado;
	}

	public void setAlunoJaAprovado(boolean alunoJaAprovado) {
		this.alunoJaAprovado = alunoJaAprovado;
	}

	public List<CalendarioAgrupamentoDisciplinaVO> getCalendarioAgrupamentoDisciplinaVOs() {
		if(calendarioAgrupamentoDisciplinaVOs == null) {
			calendarioAgrupamentoDisciplinaVOs =  new ArrayList<CalendarioAgrupamentoDisciplinaVO>(0);
		}
		return calendarioAgrupamentoDisciplinaVOs;
	}

	public void setCalendarioAgrupamentoDisciplinaVOs(
			List<CalendarioAgrupamentoDisciplinaVO> calendarioAgrupamentoDisciplinaVOs) {
		this.calendarioAgrupamentoDisciplinaVOs = calendarioAgrupamentoDisciplinaVOs;
	}
	
	public String descricaoApresentar;

	public String getDescricaoApresentar() {
		if(descricaoApresentar == null) {
			descricaoApresentar = getAno()+"/"+getSemestre()+" - "+getDataInicial_Apresentar()+" à "+getDataFinal_Apresentar();
		}
		return descricaoApresentar;
	}

	public void setDescricaoApresentar(String descricaoApresentar) {
		this.descricaoApresentar = descricaoApresentar;
	}

	public String getDisciplinaApresentarConsulta() {
		if(disciplinaApresentarConsulta == null) {
			disciplinaApresentarConsulta = "";
		}
		return disciplinaApresentarConsulta;
	}

	public void setDisciplinaApresentarConsulta(String disciplinaApresentarConsulta) {
		this.disciplinaApresentarConsulta = disciplinaApresentarConsulta;
	}
	

	
	
	

}
