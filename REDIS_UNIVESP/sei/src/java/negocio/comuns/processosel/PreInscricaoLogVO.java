package negocio.comuns.processosel;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.processosel.enumeradores.SituacaoLogPreInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;

public class PreInscricaoLogVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Integer unidadeEnsino; 
	private Integer curso; 
	private Integer turno;
	private Integer propect;
	private Date dataCadastroPreMatricula;
	private String escolaridade;
	private String cursoGraduado;
	private String situacaoCursoGraduacao;
	private String nomeProspect; 
	private String emailProspect;
	private String celularProspect;
	private String cpfProspect;
	private String rgProspect;
	private String emissorrgProspect;
	private String estadoemissorgProspect;
	private String estadocivilProspect;
	private Date dataexprgProspect;
	private Date datanascProspect;
	private String sexoProspect;
	private String cepProspect;
	private String enderecoProspect;
	private String setorProspect;
	private Integer cidadeProspect;
	private Integer naturalidadeProspect;
	private String telResidencialProspect;
	private String mensagemErro;
//	private Boolean falhaPreInscricao;

	// dados nao persistidos apenas para apresentação da Tela
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private CidadeVO cidadeProspectVO;
	private CidadeVO naturalidadeProspectVO;
	private ProspectsVO prospectsVO;
	
	private SituacaoLogPreInscricaoEnum situacaoLogPreInscricao;
	private Boolean selecionado;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = 0;
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Integer getCurso() {
		if (curso == null) {
			curso = 0;
		}
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public Integer getTurno() {
		if (turno == null) {
			turno = 0;
		}
		return turno;
	}

	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	public Date getDataCadastroPreMatricula() {
		if (dataCadastroPreMatricula == null) {
			dataCadastroPreMatricula = new Date();
		}
		return dataCadastroPreMatricula;
	}

	public void setDataCadastroPreMatricula(Date dataCadastroPreMatricula) {
		this.dataCadastroPreMatricula = dataCadastroPreMatricula;
	}

	public String getEscolaridade() {
		if (escolaridade == null) {
			escolaridade = "";
		}
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getCursoGraduado() {
		if (cursoGraduado == null) {
			cursoGraduado = "";
		}
		return cursoGraduado;
	}

	public void setCursoGraduado(String cursoGraduado) {
		this.cursoGraduado = cursoGraduado;
	}

	public String getSituacaoCursoGraduacao() {
		if (situacaoCursoGraduacao == null) {
			situacaoCursoGraduacao = "";
		}
		return situacaoCursoGraduacao;
	}

	public void setSituacaoCursoGraduacao(String situacaoCursoGraduacao) {
		this.situacaoCursoGraduacao = situacaoCursoGraduacao;
	}

	public String getNomeProspect() {
		if (nomeProspect == null) {
			nomeProspect = "";
		}
		return nomeProspect;
	}

	public void setNomeProspect(String nomeProspect) {
		this.nomeProspect = nomeProspect;
	}

	public String getEmailProspect() {
		if (emailProspect == null) {
			emailProspect = "";
		}
		return emailProspect;
	}

	public void setEmailProspect(String emailProspect) {
		this.emailProspect = emailProspect;
	}

	public String getCelularProspect() {
		if (celularProspect == null) {
			celularProspect = "";
		}
		return celularProspect;
	}

	public void setCelularProspect(String celularProspect) {
		this.celularProspect = celularProspect;
	}

	public String getCpfProspect() {
		if (cpfProspect == null) {
			cpfProspect = "";
		}
		return cpfProspect;
	}

	public void setCpfProspect(String cpfProspect) {
		this.cpfProspect = cpfProspect;
	}

	public String getRgProspect() {
		if (rgProspect == null) {
			rgProspect = "";
		}
		return rgProspect;
	}

	public void setRgProspect(String rgProspect) {
		this.rgProspect = rgProspect;
	}

	public String getEmissorrgProspect() {
		if (emissorrgProspect == null) {
			emissorrgProspect = "";
		}
		return emissorrgProspect;
	}

	public void setEmissorrgProspect(String emissorrgProspect) {
		this.emissorrgProspect = emissorrgProspect;
	}

	public String getEstadoemissorgProspect() {
		if (estadoemissorgProspect == null) {
			estadoemissorgProspect = "";
		}
		return estadoemissorgProspect;
	}

	public void setEstadoemissorgProspect(String estadoemissorgProspect) {
		this.estadoemissorgProspect = estadoemissorgProspect;
	}

	public String getEstadocivilProspect() {
		if (estadocivilProspect == null) {
			estadocivilProspect = "";
		}
		return estadocivilProspect;
	}

	public void setEstadocivilProspect(String estadocivilProspect) {
		this.estadocivilProspect = estadocivilProspect;
	}

	public Date getDataexprgProspect() {
		return dataexprgProspect;
	}

	public void setDataexprgProspect(Date dataexprgProspect) {
		this.dataexprgProspect = dataexprgProspect;
	}

	public Date getDatanascProspect() {
		return datanascProspect;
	}

	public void setDatanascProspect(Date datanascProspect) {
		this.datanascProspect = datanascProspect;
	}

	public String getSexoProspect() {
		if (sexoProspect == null) {
			sexoProspect = "";
		}
		return sexoProspect;
	}

	public void setSexoProspect(String sexoProspect) {
		this.sexoProspect = sexoProspect;
	}

	public String getCepProspect() {
		if (cepProspect == null) {
			cepProspect = "";
		}
		return cepProspect;
	}

	public void setCepProspect(String cepProspect) {
		this.cepProspect = cepProspect;
	}

	public String getEnderecoProspect() {
		if (enderecoProspect == null) {
			enderecoProspect = "";
		}
		return enderecoProspect;
	}

	public void setEnderecoProspect(String enderecoProspect) {
		this.enderecoProspect = enderecoProspect;
	}

	public String getSetorProspect() {
		if (setorProspect == null) {
			setorProspect = "";
		}
		return setorProspect;
	}

	public void setSetorProspect(String setorProspect) {
		this.setorProspect = setorProspect;
	}

	public Integer getCidadeProspect() {
		if (cidadeProspect == null) {
			cidadeProspect = 0;
		}
		return cidadeProspect;
	}

	public void setCidadeProspect(Integer cidadeProspect) {
		this.cidadeProspect = cidadeProspect;
	}

	public Integer getNaturalidadeProspect() {
		if (naturalidadeProspect == null) {
			naturalidadeProspect = 0;
		}
		return naturalidadeProspect;
	}

	public void setNaturalidadeProspect(Integer naturalidadeProspect) {
		this.naturalidadeProspect = naturalidadeProspect;
	}

	public String getTelResidencialProspect() {
		if (telResidencialProspect == null) {
			telResidencialProspect = "";
		}
		return telResidencialProspect;
	}

	public void setTelResidencialProspect(String telResidencialProspect) {
		this.telResidencialProspect = telResidencialProspect;
	}

	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public CidadeVO getCidadeProspectVO() {
		if (cidadeProspectVO == null) {
			cidadeProspectVO = new CidadeVO();
		}
		return cidadeProspectVO;
	}

	public void setCidadeProspectVO(CidadeVO cidadeProspectVO) {
		this.cidadeProspectVO = cidadeProspectVO;
	}

	public CidadeVO getNaturalidadeProspectVO() {
		if (naturalidadeProspectVO == null) {
			naturalidadeProspectVO = new CidadeVO();
		}
		return naturalidadeProspectVO;
	}

	public void setNaturalidadeProspectVO(CidadeVO naturalidadeProspectVO) {
		this.naturalidadeProspectVO = naturalidadeProspectVO;
	}

	public String getDataCadastro_Apresentar() {
		return (Uteis.getDataComHora(getDataCadastroPreMatricula()));
	}

//	public String getStatus_Apresentar() {
//		return getFalhaPreInscricao() ? "Falha" : "Suscesso";
//	}

	public Integer getPropect() {
		if (propect == null) {
			propect = 0;
		}
		return propect;
	}

	public void setPropect(Integer propect) {
		this.propect = propect;
	}

	public ProspectsVO getProspectsVO() {
		if (prospectsVO == null) {
			prospectsVO = new ProspectsVO();
		}
		return prospectsVO;
	}

	public void setProspectsVO(ProspectsVO prospectsVO) {
		this.prospectsVO = prospectsVO;
	}

//	public Boolean getFalhaPreInscricao() {
//		if (falhaPreInscricao == null) {
//			falhaPreInscricao = Boolean.FALSE;
//		}
//		return falhaPreInscricao;
//	}
//
//	public void setFalhaPreInscricao(Boolean falhaPreInscricao) {
//		this.falhaPreInscricao = falhaPreInscricao;
//	}

	public String getEstadoCivil_Apresentar() {
		if (getEstadocivilProspect().equals("A")) {
			return "Amasiado(a)";
		} else if (getEstadocivilProspect().equals("C")) {
			return "Casado(a)";
		} else if (getEstadocivilProspect().equals("D")) {
			return "Divorciado(a)";
		} else if (getEstadocivilProspect().equals("S")) {
			return "Solteiro(a)";
		} else if (getEstadocivilProspect().equals("U")) {
			return "União Estável";
		} else if (getEstadocivilProspect().equals("V")) {
			return "Viúvo(a)";
		} else if (getEstadocivilProspect().equals("E")) {
			return "Separado(a)";
		} else if (getEstadocivilProspect().equals("Q")) {
			return "Desquitado(a)";
		} else {
			return "";
		}
	}
	
	public String getEscolaridade_Apresentar() {
		if (getEscolaridade().equals("EM")) {
			return "Ensino Médio";
		} else if (getEscolaridade().equals("DO")) {
			return "Doutorado";
		} else if (getEscolaridade().equals("PD")) {
			return "Pós-Doutorado";
		} else if (getEscolaridade().equals("GR")) {
			return "Graduação";
		} else if (getEscolaridade().equals("ME")) {
			return "Mestrado";
		} else if (getEscolaridade().equals("ES")) {
			return "Especialização";
		} else {
			return "";
		}
	}

	public String getSituacaoFormacaoAcademica_Apresentar() {
		if (getSituacaoCursoGraduacao().equals("CO")) {
			return "Concluído";
		} else if (getSituacaoCursoGraduacao().equals("CU")) {
			return "Cursando";
		} else {
			return "";
		}
	}

	public String getSexo_Apresentar() {
		if (getSexoProspect().equals("F")) {
			return "Feminino";
		} else if (getSexoProspect().equals("M")) {
			return "Masculino";
		} else {
			return "";
		}
	}
	
	public SituacaoLogPreInscricaoEnum getSituacaoLogPreInscricao() {
		return situacaoLogPreInscricao;
	}
	
	public String getSituacaoLogPreInscricao_Apresentar() {
		return getSituacaoLogPreInscricao().getValorApresentar();
	}

	public void setSituacaoLogPreInscricao(SituacaoLogPreInscricaoEnum situacaoLogPreInscricao) {
		this.situacaoLogPreInscricao = situacaoLogPreInscricao;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
}
