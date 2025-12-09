package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoUsoProcessoMatriculaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ProcessoMatricula. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProcessoMatriculaVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private Date data;
    private Date dataInicio;
    private Date dataFinal;
    private Boolean validoPelaInternet;
    private Boolean exigeConfirmacaoPresencial;
    private String situacao;
    private String nivelProcessoMatricula;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ProcessoMatriculaCalendario</code>.
     */
    private List<ProcessoMatriculaCalendarioVO> processoMatriculaCalendarioVOs;
    private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
//    private UnidadeEnsinoVO unidadeEnsino;
    private Boolean apresentarProcessoVisaoAluno;
    private Boolean permiteIncluirExcluirDisciplinaVisaoAluno;
    private Date dataInicioMatriculaOnline;
    private Date dataFimMatriculaOnline;
    private Integer qtdeMininaDisciplinaCursar;
    private Boolean apresentarTermoAceite;
    private String termoAceite;
    private TextoPadraoVO textoPadraoContratoRenovacaoOnline;
    private Boolean bloquearAlunosPossuemConvenioRenovacaoOnline;
    public static final long serialVersionUID = 1L;
    private String mensagemApresentarVisaoAluno;
    private String mensagemConfirmacaoRenovacaoAluno;
    private List<ProcessoMatriculaUnidadeEnsinoVO> processoMatriculaUnidadeEnsinoVOs;
    private ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO;
    
    private String ano;
    private String semestre;
    private TipoAlunoCalendarioMatriculaEnum tipoAluno;
    private TipoUsoProcessoMatriculaEnum tipoUsoProcessoMatriculaEnum;
    /**
     * Construtor padrão da classe <code>ProcessoMatricula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProcessoMatriculaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcessoMatriculaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProcessoMatriculaVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Processo de Matrícula) deve ser informado.");
        }
        if ((obj.getProcessoMatriculaUnidadeEnsinoVOs() == null) || (obj.getProcessoMatriculaUnidadeEnsinoVOs().isEmpty()) ||  (!obj.getProcessoMatriculaUnidadeEnsinoVOs().stream().anyMatch(u -> u.getSelecionado()))) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Calendário de Matrícula) deve ser informado.");
        }
        if (obj.getNivelProcessoMatricula().equals("")) {
            throw new ConsistirException("O campo NÍVEL PROCESSO MATRÍCULA (Calendário de Matrícula) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA CADASTRO (Calendário de Matrícula) deve ser informado.");
        }
        if (obj.getDataInicio() == null) {
            throw new ConsistirException("O campo DATA INÍCIO MATRÍCULA (Calendário de Matrícula) deve ser informado.");
        }
        if (obj.getDataFinal() == null) {
            throw new ConsistirException("O campo DATA FINAL MATRÍCULA (Calendário de Matrícula) deve ser informado.");
        }
        if (obj.getDataInicio().compareTo(obj.getDataFinal()) > 0) {
        	throw new ConsistirException("O campo DATA INÍCIO MATRÍCULA (Calendário de Matrícula) deve ser menor do que o campo DATA FINAL MATRÍCULA (Calendário Matrícula).");
        }
        if (obj.getSituacao() == null || obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getProcessoMatriculaCalendarioVOs().isEmpty()) {
            throw new ConsistirException("Pelo menos um CALENDÁRIO PROCESSO DE MATRÍCULA deve ser informado.");
        }
        if (obj.getApresentarProcessoVisaoAluno()) {
            if (obj.getDataInicioMatriculaOnline() == null || obj.getDataFimMatriculaOnline() == null) {
                throw  new ConsistirException("O campo PERÍODO MATRÍCULA ONLINE deve ser informado");
            }
            if (obj.getDataInicioMatriculaOnline().compareTo(obj.getDataFimMatriculaOnline()) > 0) {
            	throw new ConsistirException("O campo DATA INÍCIO PERÍODO MATRÍCULA ONLINE (Calendário de Matrícula) deve ser menor do que o campo DATA FINAL PERÍODO MATRÍCULA ONLINE (Calendário Matrícula).");
            }
        }
        if (obj.getApresentarTermoAceite() && !Uteis.isAtributoPreenchido(obj.getTextoPadraoContratoRenovacaoOnline())) {
        	throw  new ConsistirException("O campo TEXTO PADRÃO TERMO DE ACEITE deve ser informado");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setData(new Date());
        setDataInicio(new Date());
        setDataFinal(new Date());
        setValidoPelaInternet(Boolean.TRUE);
        setExigeConfirmacaoPresencial(Boolean.FALSE);
        setSituacao(new String("AT"));
    }


    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProcessoMatricula</code>).
     */
//    public UnidadeEnsinoVO getUnidadeEnsino() {
//        if (unidadeEnsino == null) {
//            unidadeEnsino = new UnidadeEnsinoVO();
//        }
//        return (unidadeEnsino);
//    }
//
//    /**
//     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
//     * <code>ProcessoMatricula</code>).
//     */
//    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
//        this.unidadeEnsino = obj;
//    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProcessoMatriculaCalendario</code>.
     */
    public List<ProcessoMatriculaCalendarioVO> getProcessoMatriculaCalendarioVOs() {
        if (processoMatriculaCalendarioVOs == null) {
            processoMatriculaCalendarioVOs = new ArrayList<ProcessoMatriculaCalendarioVO>();
        }
        return (processoMatriculaCalendarioVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ProcessoMatriculaCalendario</code>.
     */
    public void setProcessoMatriculaCalendarioVOs(List<ProcessoMatriculaCalendarioVO> processoMatriculaCalendarioVOs) {
        this.processoMatriculaCalendarioVOs = processoMatriculaCalendarioVOs;
    }

    public String getSituacao() {
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("FI")) {
            return "Finalizado";
        }
        if (situacao.equals("AT")) {
            return "Ativo - Matrícula";
        }
        if (situacao.equals("PR")) {
            return "Ativo - Pré-Matrícula";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getExigeConfirmacaoPresencial() {
        return (exigeConfirmacaoPresencial);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getExigeConfirmacaoPresencial_Apresentar() {
        if (exigeConfirmacaoPresencial) {
            return "Sim";
        } else {
            return "Não";
        }
    }

    public Boolean isExigeConfirmacaoPresencial() {
        return (exigeConfirmacaoPresencial);
    }

    public void setExigeConfirmacaoPresencial(Boolean exigeConfirmacaoPresencial) {
        this.exigeConfirmacaoPresencial = exigeConfirmacaoPresencial;
    }

    public Boolean getValidoPelaInternet() {
        return (validoPelaInternet);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getValidoPelaInternet_Apresentar() {
        if (validoPelaInternet) {
            return "Sim";
        } else {
            return "Não";
        }
    }

    public Boolean isValidoPelaInternet() {
        return (validoPelaInternet);
    }

    public void setValidoPelaInternet(Boolean validoPelaInternet) {
        this.validoPelaInternet = validoPelaInternet;
    }

    public Date getDataFinal() {
        return (dataFinal);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinal_Apresentar() {
        return (Uteis.getData(dataFinal));
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicio() {
        return (dataInicio);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicio_Apresentar() {
        return (Uteis.getData(dataInicio));
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
    		if(codigo == null) {
    			codigo = 0;
    		}
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean ativoParaMatriculaEPreMatricula(Date dataBase) {
        // if this.getDataInicio().compareTo(dataBase) >=
        return true;
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

    /**
     * @return the nivelProcessoMatricula
     */
    public String getNivelProcessoMatricula() {
        if (nivelProcessoMatricula == null) {
            nivelProcessoMatricula = "";
        }
        return nivelProcessoMatricula;
    }

    /**
     * @param nivelProcessoMatricula the nivelProcessoMatricula to set
     */
    public void setNivelProcessoMatricula(String nivelProcessoMatricula) {
        this.nivelProcessoMatricula = nivelProcessoMatricula;
    }

    public Boolean getApresentarProcessoVisaoAluno() {
        if (apresentarProcessoVisaoAluno == null) {
            apresentarProcessoVisaoAluno = Boolean.FALSE;
        }
        return apresentarProcessoVisaoAluno;
    }

    public void setApresentarProcessoVisaoAluno(Boolean apresentarProcessoVisaoAluno) {
        this.apresentarProcessoVisaoAluno = apresentarProcessoVisaoAluno;
    }

    public Date getDataInicioMatriculaOnline() {
        return dataInicioMatriculaOnline;
    }

    public void setDataInicioMatriculaOnline(Date dataInicioMatriculaOnline) {
        this.dataInicioMatriculaOnline = dataInicioMatriculaOnline;
    }

    public Date getDataFimMatriculaOnline() {
        return dataFimMatriculaOnline;
    }

    public void setDataFimMatriculaOnline(Date dataFimMatriculaOnline) {
        this.dataFimMatriculaOnline = dataFimMatriculaOnline;
    }

    
    public String getMensagemApresentarVisaoAluno() {
        if(mensagemApresentarVisaoAluno==null){
            mensagemApresentarVisaoAluno = "";
        }
        return mensagemApresentarVisaoAluno;
    }

    
    public void setMensagemApresentarVisaoAluno(String mensagemApresentarVisaoAluno) {
        this.mensagemApresentarVisaoAluno = mensagemApresentarVisaoAluno;
    }

    
    public Boolean getPermiteIncluirExcluirDisciplinaVisaoAluno() {
        if(permiteIncluirExcluirDisciplinaVisaoAluno == null){
            permiteIncluirExcluirDisciplinaVisaoAluno = false;
        }
        return permiteIncluirExcluirDisciplinaVisaoAluno;
    }

    
    public void setPermiteIncluirExcluirDisciplinaVisaoAluno(Boolean permiteIncluirExcluirDisciplinaVisaoAluno) {
        this.permiteIncluirExcluirDisciplinaVisaoAluno = permiteIncluirExcluirDisciplinaVisaoAluno;
    }

	public Integer getQtdeMininaDisciplinaCursar() {
		if (qtdeMininaDisciplinaCursar == null) {
			qtdeMininaDisciplinaCursar = 0;
		}
		return qtdeMininaDisciplinaCursar;
	}

	public void setQtdeMininaDisciplinaCursar(Integer qtdeMininaDisciplinaCursar) {
		this.qtdeMininaDisciplinaCursar = qtdeMininaDisciplinaCursar;
	}

	public String getTermoAceite() {
		if (termoAceite == null) {
			termoAceite = "";
		}
		return termoAceite;
	}

	public void setTermoAceite(String termoAceite) {
		this.termoAceite = termoAceite;
	}

	public Boolean getApresentarTermoAceite() {
		if (apresentarTermoAceite == null) {
			apresentarTermoAceite = Boolean.FALSE;
		}
		return apresentarTermoAceite;
	}

	public void setApresentarTermoAceite(Boolean apresentarTermoAceite) {
		this.apresentarTermoAceite = apresentarTermoAceite;
	}

	public TextoPadraoVO getTextoPadraoContratoRenovacaoOnline() {
		if (textoPadraoContratoRenovacaoOnline == null) {
			textoPadraoContratoRenovacaoOnline = new TextoPadraoVO();
		}
		return textoPadraoContratoRenovacaoOnline;
	}

	public void setTextoPadraoContratoRenovacaoOnline(TextoPadraoVO textoPadraoContratoRenovacaoOnline) {
		this.textoPadraoContratoRenovacaoOnline = textoPadraoContratoRenovacaoOnline;
	}

	public Boolean getBloquearAlunosPossuemConvenioRenovacaoOnline() {
		if (bloquearAlunosPossuemConvenioRenovacaoOnline == null) {
			bloquearAlunosPossuemConvenioRenovacaoOnline = Boolean.TRUE;
		}
		return bloquearAlunosPossuemConvenioRenovacaoOnline;
	}

	public void setBloquearAlunosPossuemConvenioRenovacaoOnline(Boolean bloquearAlunosPossuemConvenioRenovacaoOnline) {
		this.bloquearAlunosPossuemConvenioRenovacaoOnline = bloquearAlunosPossuemConvenioRenovacaoOnline;
	}
    
    /**
     * @return the mensagemConfirmacaoRenovacaoAluno
     */
    public String getMensagemConfirmacaoRenovacaoAluno() {
        if (mensagemConfirmacaoRenovacaoAluno == null) { 
            mensagemConfirmacaoRenovacaoAluno = "";
        }
        return mensagemConfirmacaoRenovacaoAluno;
    }

    /**
     * @param mensagemConfirmacaoRenovacaoAluno the mensagemConfirmacaoRenovacaoAluno to set
     */
    public void setMensagemConfirmacaoRenovacaoAluno(String mensagemConfirmacaoRenovacaoAluno) {
        this.mensagemConfirmacaoRenovacaoAluno = mensagemConfirmacaoRenovacaoAluno;
    }    
    
    public Boolean getIsApresentarMensagemApresentarVisaoAluno(){
    	return !Uteis.removeHTML(getMensagemApresentarVisaoAluno()).trim().isEmpty() || (getApresentarTermoAceite() && Uteis.isAtributoPreenchido(getTextoPadraoContratoRenovacaoOnline().getCodigo()));
    }

	public List<ProcessoMatriculaUnidadeEnsinoVO> getProcessoMatriculaUnidadeEnsinoVOs() {
		if(processoMatriculaUnidadeEnsinoVOs == null) {
			processoMatriculaUnidadeEnsinoVOs =  new ArrayList<ProcessoMatriculaUnidadeEnsinoVO>(0);
		}
		return processoMatriculaUnidadeEnsinoVOs;
	}

	public void setProcessoMatriculaUnidadeEnsinoVOs(
			List<ProcessoMatriculaUnidadeEnsinoVO> processoMatriculaUnidadeEnsinoVOs) {
		this.processoMatriculaUnidadeEnsinoVOs = processoMatriculaUnidadeEnsinoVOs;
	}
	
	public ProcessoMatriculaUnidadeEnsinoVO getProcessoMatriculaUnidadeEnsinoVO() {
		if(processoMatriculaUnidadeEnsinoVO == null) {
			processoMatriculaUnidadeEnsinoVO = new ProcessoMatriculaUnidadeEnsinoVO(); 
		}
		return processoMatriculaUnidadeEnsinoVO;
	}
	
	public void setProcessoMatriculaUnidadeEnsinoVO(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO) {
		this.processoMatriculaUnidadeEnsinoVO = processoMatriculaUnidadeEnsinoVO;
	}
	private String unidadeEnsinoDescricao;
	public String getUnidadeEnsinoDescricao() {
		if(unidadeEnsinoDescricao == null || unidadeEnsinoDescricao.equals("")) {
			unidadeEnsinoDescricao = "";
			for(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO: getProcessoMatriculaUnidadeEnsinoVOs()) {
				if(processoMatriculaUnidadeEnsinoVO.getSelecionado()) {
					if(!unidadeEnsinoDescricao.isEmpty()) {
						unidadeEnsinoDescricao += ", ";
					}
					unidadeEnsinoDescricao += processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().getNome();
				}
			}
		}
		return unidadeEnsinoDescricao;
	}

	public void setUnidadeEnsinoDescricao(String unidadeEnsinoDescricao) {
		this.unidadeEnsinoDescricao = unidadeEnsinoDescricao;
	}

	public String getAno() {
		if(ano == null) {
			ano =  "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null) {
			semestre =  "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	 public TipoAlunoCalendarioMatriculaEnum getTipoAluno() {
			if(tipoAluno == null) {
				tipoAluno = TipoAlunoCalendarioMatriculaEnum.AMBOS;
			}
			return tipoAluno;
		}

		public void setTipoAluno(TipoAlunoCalendarioMatriculaEnum tipoAluno) {
			this.tipoAluno = tipoAluno;
		}
		
		 public TipoUsoProcessoMatriculaEnum getTipoUsoProcessoMatriculaEnum() {
				if (tipoUsoProcessoMatriculaEnum == null) {
					tipoUsoProcessoMatriculaEnum = TipoUsoProcessoMatriculaEnum.AMBOS;
				}
				return tipoUsoProcessoMatriculaEnum;
			}

			public void setTipoUsoProcessoMatriculaEnum(TipoUsoProcessoMatriculaEnum tipoUsoProcessoMatriculaEnum) {
				this.tipoUsoProcessoMatriculaEnum = tipoUsoProcessoMatriculaEnum;
			}
}
