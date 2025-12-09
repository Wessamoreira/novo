package relatorio.negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * @author Otimize-TI
 */
public class CertificadoCursoExtensaoRelVO implements Cloneable, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4646796238174907204L;
	private String matricula;
    private String nomeAluno;
    private String nomeCurso;
    private String periodoCursado;
    private String cargaHoraria;
    private String cidadeDataAtual;
    // unidade matriz
    private String mantenedora;
    // unidade onde o curso esta cadastrado
    private String mantida;
    private FuncionarioVO funcionarioPrincipalVO;
    private FuncionarioVO funcionarioSecundarioVO;
    private CargoVO cargoFuncionarioPrincipal;
    private CargoVO cargoFuncionarioSecundario;
    private String credenciamento;
    private List<CertificadoCursoExtensaoDisciplinasRelVO> certificadoCursoExtensaoDisciplinasRelVOs;
   
    private String dataAtualPorExtenso;
    private String rgAluno;
    private String orgaoEmissorRgAluno;
    private String estadoEmissorRgAluno;
    private String nomeCertificacao;
    private String motivoErro;
    private PeriodoLetivoVO periodoLetivoVO;
    private MatriculaVO matriculaVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private Boolean possuiErro;

    public CertificadoCursoExtensaoRelVO() {
    }

    public FuncionarioVO getFuncionarioPrincipalVO() {
        if (funcionarioPrincipalVO == null) {
            funcionarioPrincipalVO = new FuncionarioVO();
        }
        return funcionarioPrincipalVO;
    }

    public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
        this.funcionarioPrincipalVO = funcionarioPrincipalVO;
    }

    public FuncionarioVO getFuncionarioSecundarioVO() {
        if (funcionarioSecundarioVO == null) {
            funcionarioSecundarioVO = new FuncionarioVO();
        }
        return funcionarioSecundarioVO;
    }

    public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
        this.funcionarioSecundarioVO = funcionarioSecundarioVO;
    }

    public CargoVO getCargoFuncionarioPrincipal() {
        if (cargoFuncionarioPrincipal == null) {
            cargoFuncionarioPrincipal = new CargoVO();
        }
        return cargoFuncionarioPrincipal;
    }

    public void setCargoFuncionarioPrincipal(CargoVO cargoFuncionarioPrincipal) {
        this.cargoFuncionarioPrincipal = cargoFuncionarioPrincipal;
    }

    public CargoVO getCargoFuncionarioSecundario() {
        if (cargoFuncionarioSecundario == null) {
            cargoFuncionarioSecundario = new CargoVO();
        }
        return cargoFuncionarioSecundario;
    }

    public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
        this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
    }

	public String getRgAluno() {
		if(rgAluno == null){
			rgAluno = "";
		}
		return rgAluno;
	}

	public void setRgAluno(String rgAluno) {
		this.rgAluno = rgAluno;
	}

	public String getDataAtualPorExtenso() {
		if(dataAtualPorExtenso == null){
			dataAtualPorExtenso = "";
		}
		return dataAtualPorExtenso;
		
	}

	public void setDataAtualPorExtenso(String dataAtualPorExtenso) {
		this.dataAtualPorExtenso = dataAtualPorExtenso;
	}

	public List<CertificadoCursoExtensaoDisciplinasRelVO> getCertificadoCursoExtensaoDisciplinasRelVOs() {
		if (certificadoCursoExtensaoDisciplinasRelVOs == null) {
			certificadoCursoExtensaoDisciplinasRelVOs = new ArrayList<CertificadoCursoExtensaoDisciplinasRelVO>(0);
		}
		return certificadoCursoExtensaoDisciplinasRelVOs;
	}

	public void setCertificadoCursoExtensaoDisciplinasRelVOs(List<CertificadoCursoExtensaoDisciplinasRelVO> certificadoCursoExtensaoDisciplinasRelVOs) {
		this.certificadoCursoExtensaoDisciplinasRelVOs = certificadoCursoExtensaoDisciplinasRelVOs;
	}
    
    public JRDataSource getCertificadoCursoExtensaoDisciplinasRelVOsJRDataSource() {
        return new JRBeanArrayDataSource(getCertificadoCursoExtensaoDisciplinasRelVOs().toArray());
    }

	public String getCredenciamento() {
		if (credenciamento == null) {
			credenciamento = "";
		}
		return credenciamento;
	}

	public void setCredenciamento(String credenciamento) {
		this.credenciamento = credenciamento;
	}

	public String getOrgaoEmissorRgAluno() {
		if (orgaoEmissorRgAluno == null) {
			orgaoEmissorRgAluno = "";
		}
		return orgaoEmissorRgAluno;
	}

	public void setOrgaoEmissorRgAluno(String orgaoEmissorRgAluno) {
		this.orgaoEmissorRgAluno = orgaoEmissorRgAluno;
	}

	public String getEstadoEmissorRgAluno() {
		if (estadoEmissorRgAluno == null) {
			estadoEmissorRgAluno = "";
		}
		return estadoEmissorRgAluno;
	}

	public void setEstadoEmissorRgAluno(String estadoEmissorRgAluno) {
		this.estadoEmissorRgAluno = estadoEmissorRgAluno;
	}

	public String getNomeCertificacao() {
		if (nomeCertificacao == null) {
			nomeCertificacao = "";
		}
		return nomeCertificacao;
	}

	public void setNomeCertificacao(String nomeCertificacao) {
		this.nomeCertificacao = nomeCertificacao;
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

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getPeriodoCursado() {
		if (periodoCursado == null) {
			periodoCursado = "";
		}
		return periodoCursado;
	}

	public void setPeriodoCursado(String periodoCursado) {
		this.periodoCursado = periodoCursado;
	}

	public String getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = "";
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getCidadeDataAtual() {
		if (cidadeDataAtual == null) {
			cidadeDataAtual = "";
		}
		return cidadeDataAtual;
	}

	public void setCidadeDataAtual(String cidadeDataAtual) {
		this.cidadeDataAtual = cidadeDataAtual;
	}

	public String getMantenedora() {
		if (mantenedora == null) {
			mantenedora = "";
		}
		return mantenedora;
	}

	public void setMantenedora(String mantenedora) {
		this.mantenedora = mantenedora;
	}

	public String getMantida() {
		if (mantida == null) {
			mantida = "";
		}
		return mantida;
	}

	public void setMantida(String mantida) {
		this.mantida = mantida;
	}

	public String getMotivoErro() {
		if (motivoErro == null) {
			motivoErro = "";
		}
		return motivoErro;
	}

	public void setMotivoErro(String motivoErro) {
		this.motivoErro = motivoErro;
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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
	
	public Boolean  getPossuiErro() {
		if(possuiErro == null ) {
			possuiErro = Boolean.FALSE;
		}
		return possuiErro;
	}
    
	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}
}
