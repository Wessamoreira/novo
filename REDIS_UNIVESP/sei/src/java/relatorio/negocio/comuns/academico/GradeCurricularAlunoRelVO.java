/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * 
 * @author Otimize-TI
 */
public class GradeCurricularAlunoRelVO {

    private Integer gradeCurricular;
    private String gradeCurricularNome;
    private String gradeCurricularResolucao;
    private String cursoNome;
    private String periodicidade;
    private String matricula;
    private String alunoNome;
    private String cpf;
    private String rg;
    private String nomeTurma;
    private Boolean apresentarTodasDisciplinasGrade;
    private String campoFiltro;
    private List<GradeCurricularAlunoDisciplinaRelVO> listaGradeCurricularAlunoDisciplinas;    
    private FuncionarioVO funcionarioPrincipalVO;
    private FuncionarioVO funcionarioSecundarioVO;
    private CargoVO cargoFuncionarioPrincipal;
    private CargoVO cargoFuncionarioSecundario;
    private Boolean apresentarCampoAssinatura;
    private Integer cargaHorariaTotal;    
    private Integer cargaHorariaObrigatoriaTotal;
    private Integer cargaHorariaObrigatoriaTotalCumprida;    
    private Integer cargaHorariaOptativaObrigatoriaTotalCumprida;
    private Integer cargaHorariaEstagioTotal;
    private Integer cargaHorariaEstagioTotalCumprida;
    private Integer cargaHorariaAtividadeComplementarTotal;
    private Integer cargaHorariaAtividadeComplementarTotalCumprida;

    public GradeCurricularAlunoRelVO() {
    }

    public JRDataSource getGradeCurricularDisciplinas() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaGradeCurricularAlunoDisciplinas().toArray());
        return jr;
    }

    public String getAlunoNome() {
        if (alunoNome == null) {
            alunoNome = "";
        }
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
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

    public String getCursoNome() {
        if (cursoNome == null) {
            cursoNome = "";
        }
        return cursoNome;
    }

    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
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

    public String getRg() {
        if (rg == null) {
            rg = "";
        }
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public List<GradeCurricularAlunoDisciplinaRelVO> getListaGradeCurricularAlunoDisciplinas() {
        if (listaGradeCurricularAlunoDisciplinas == null) {
            listaGradeCurricularAlunoDisciplinas = new ArrayList<GradeCurricularAlunoDisciplinaRelVO>(0);
        }
        return listaGradeCurricularAlunoDisciplinas;
    }

    public void setListaGradeCurricularAlunoDisciplinas(List<GradeCurricularAlunoDisciplinaRelVO> listaGradeCurricularAlunoDisciplinas) {
        this.listaGradeCurricularAlunoDisciplinas = listaGradeCurricularAlunoDisciplinas;
    }

	public String getNomeTurma() {
		if (nomeTurma == null) {
			nomeTurma = "";
		}
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

    public String getGradeCurricularNome() {
        if (gradeCurricularNome == null) {
            gradeCurricularNome = "";
        }
        return gradeCurricularNome;
    }

    public void setGradeCurricularNome(String gradeCurricularNome) {
        this.gradeCurricularNome = gradeCurricularNome;
    }

    public Boolean getApresentarTodasDisciplinasGrade() {
        if (apresentarTodasDisciplinasGrade == null) {
            apresentarTodasDisciplinasGrade = Boolean.FALSE;
        }
        return apresentarTodasDisciplinasGrade;
    }

    public void setApresentarTodasDisciplinasGrade(Boolean apresentarTodasDisciplinasGrade) {
        this.apresentarTodasDisciplinasGrade = apresentarTodasDisciplinasGrade;
    }

    public String getCampoFiltro() {
        if (campoFiltro == null) {
            campoFiltro = "";
        }
        return campoFiltro;
    }

    public void setCampoFiltro(String campoFiltro) {
        this.campoFiltro = campoFiltro;
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

	public Boolean getApresentarCampoAssinatura() {
		if (apresentarCampoAssinatura == null) {
			apresentarCampoAssinatura = false;
		}
		return apresentarCampoAssinatura;
	}

	public void setApresentarCampoAssinatura(Boolean apresentarCampoAssinatura) {
		this.apresentarCampoAssinatura = apresentarCampoAssinatura;
	}

	/**
	 * @return the periodicidade
	 */
	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	/**
	 * @return the cargaHorariaTotal
	 */
	public Integer getCargaHorariaTotal() {
		if (cargaHorariaTotal == null) {
			cargaHorariaTotal = 0;
		}
		return cargaHorariaTotal;
	}

	/**
	 * @param cargaHorariaTotal the cargaHorariaTotal to set
	 */
	public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}

	/**
	 * @return the cargaHorariaTotalCumprida
	 */
	public Integer getCargaHorariaTotalCumprida() {		
		return getCargaHorariaObrigatoriaTotalCumprida()+
			   getCargaHorariaAtividadeComplementarTotalCumprida()+
			   getCargaHorariaEstagioTotalCumprida()+
			   getCargaHorariaOptativaObrigatoriaTotalCumprida();
	}

	
	/**
	 * @return the cargaHorariaObrigatoriaTotal
	 */
	public Integer getCargaHorariaObrigatoriaTotal() {
		if (cargaHorariaObrigatoriaTotal == null) {
			cargaHorariaObrigatoriaTotal = 0;
		}
		return cargaHorariaObrigatoriaTotal;
	}

	/**
	 * @param cargaHorariaObrigatoriaTotal the cargaHorariaObrigatoriaTotal to set
	 */
	public void setCargaHorariaObrigatoriaTotal(Integer cargaHorariaObrigatoriaTotal) {
		this.cargaHorariaObrigatoriaTotal = cargaHorariaObrigatoriaTotal;
	}

	/**
	 * @return the cargaHorariaObrigatoriaTotalCumprida
	 */
	public Integer getCargaHorariaObrigatoriaTotalCumprida() {
		if (cargaHorariaObrigatoriaTotalCumprida == null) {
			cargaHorariaObrigatoriaTotalCumprida = 0;
		}
		return cargaHorariaObrigatoriaTotalCumprida;
	}

	/**
	 * @param cargaHorariaObrigatoriaTotalCumprida the cargaHorariaObrigatoriaTotalCumprida to set
	 */
	public void setCargaHorariaObrigatoriaTotalCumprida(Integer cargaHorariaObrigatoriaTotalCumprida) {
		this.cargaHorariaObrigatoriaTotalCumprida = cargaHorariaObrigatoriaTotalCumprida;
	}

	/**
	 * @return the cargaHorariaOptativaObrigatoriaTotal
	 */
	public Integer getCargaHorariaOptativaObrigatoriaTotal() {
		if(getCargaHorariaObrigatoriaTotal()+getCargaHorariaEstagioTotal()+getCargaHorariaAtividadeComplementarTotal()>getCargaHorariaTotal()){
			return 0;
		}
		return getCargaHorariaTotal()-getCargaHorariaObrigatoriaTotal()-getCargaHorariaEstagioTotal()-getCargaHorariaAtividadeComplementarTotal();
	}

	/**
	 * @return the cargaHorariaOptativaObrigatoriaTotalCumprida
	 */
	public Integer getCargaHorariaOptativaObrigatoriaTotalCumprida() {
		if (cargaHorariaOptativaObrigatoriaTotalCumprida == null) {
			cargaHorariaOptativaObrigatoriaTotalCumprida = 0;
		}
		return cargaHorariaOptativaObrigatoriaTotalCumprida;
	}

	/**
	 * @param cargaHorariaOptativaObrigatoriaTotalCumprida the cargaHorariaOptativaObrigatoriaTotalCumprida to set
	 */
	public void setCargaHorariaOptativaObrigatoriaTotalCumprida(Integer cargaHorariaOptativaObrigatoriaTotalCumprida) {
		this.cargaHorariaOptativaObrigatoriaTotalCumprida = cargaHorariaOptativaObrigatoriaTotalCumprida;
	}

	/**
	 * @return the cargaHorariaEstagioTotal
	 */
	public Integer getCargaHorariaEstagioTotal() {
		if (cargaHorariaEstagioTotal == null) {
			cargaHorariaEstagioTotal = 0;
		}
		return cargaHorariaEstagioTotal;
	}

	/**
	 * @param cargaHorariaEstagioTotal the cargaHorariaEstagioTotal to set
	 */
	public void setCargaHorariaEstagioTotal(Integer cargaHorariaEstagioTotal) {
		this.cargaHorariaEstagioTotal = cargaHorariaEstagioTotal;
	}

	/**
	 * @return the cargaHorariaEstagioTotalCumprida
	 */
	public Integer getCargaHorariaEstagioTotalCumprida() {
		if (cargaHorariaEstagioTotalCumprida == null) {
			cargaHorariaEstagioTotalCumprida = 0;
		}
		return cargaHorariaEstagioTotalCumprida;
	}

	/**
	 * @param cargaHorariaEstagioTotalCumprida the cargaHorariaEstagioTotalCumprida to set
	 */
	public void setCargaHorariaEstagioTotalCumprida(Integer cargaHorariaEstagioTotalCumprida) {
		this.cargaHorariaEstagioTotalCumprida = cargaHorariaEstagioTotalCumprida;
	}

	/**
	 * @return the cargaHorariaAtividadeComplementarTotal
	 */
	public Integer getCargaHorariaAtividadeComplementarTotal() {
		if (cargaHorariaAtividadeComplementarTotal == null) {
			cargaHorariaAtividadeComplementarTotal = 0;
		}
		return cargaHorariaAtividadeComplementarTotal;
	}

	/**
	 * @param cargaHorariaAtividadeComplementarTotal the cargaHorariaAtividadeComplementarTotal to set
	 */
	public void setCargaHorariaAtividadeComplementarTotal(Integer cargaHorariaAtividadeComplementarTotal) {
		this.cargaHorariaAtividadeComplementarTotal = cargaHorariaAtividadeComplementarTotal;
	}

	/**
	 * @return the cargaHorariaAtividadeComplementarTotalCumprida
	 */
	public Integer getCargaHorariaAtividadeComplementarTotalCumprida() {
		if (cargaHorariaAtividadeComplementarTotalCumprida == null) {
			cargaHorariaAtividadeComplementarTotalCumprida = 0;
		}
		return cargaHorariaAtividadeComplementarTotalCumprida;
	}

	/**
	 * @param cargaHorariaAtividadeComplementarTotalCumprida the cargaHorariaAtividadeComplementarTotalCumprida to set
	 */
	public void setCargaHorariaAtividadeComplementarTotalCumprida(Integer cargaHorariaAtividadeComplementarTotalCumprida) {
		this.cargaHorariaAtividadeComplementarTotalCumprida = cargaHorariaAtividadeComplementarTotalCumprida;
	}

	/**
	 * @return the gradeCurricular
	 */
	public Integer getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = 0;
		}
		return gradeCurricular;
	}

	/**
	 * @param gradeCurricular the gradeCurricular to set
	 */
	public void setGradeCurricular(Integer gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	/**
	 * @return the gradeCurricularResolucao
	 */
	public String getGradeCurricularResolucao() {
		if (gradeCurricularResolucao == null) {
			gradeCurricularResolucao = "";
		}
		return gradeCurricularResolucao;
	}

	/**
	 * @param gradeCurricularResolucao the gradeCurricularResolucao to set
	 */
	public void setGradeCurricularResolucao(String gradeCurricularResolucao) {
		this.gradeCurricularResolucao = gradeCurricularResolucao;
	}
    
	
	
}