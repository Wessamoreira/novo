package negocio.comuns.administrativo;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.basico.Pessoa;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade FormacaoAcademica. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pessoa
 */
@XmlRootElement(name ="formacaoAcademica")
public class FormacaoAcademicaVO extends SuperVO {

    private Integer codigo;
    private String instituicao;
    private String escolaridade;
    private String curso;
    private String situacao;
    private String tipoInst;
    private Date dataInicio;
    private Date dataFim;
    private String anoDataFim;
    private String semestreDataFim;
    private AreaConhecimentoVO areaConhecimento;
    private Integer pessoa;
    private CidadeVO cidade;
    private Boolean funcionario;
    private ProspectsVO prospectsVO;
    private String modalidade;
    public static final long serialVersionUID = 1L;

    private String descricaoAreaConhecimento;
    private String titulo;
    private List<SelectItem> listaSelectItemTitulo;
    /**
     * Construtor padrão da classe <code>FormacaoAcademica</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FormacaoAcademicaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FormacaoAcademicaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(FormacaoAcademicaVO obj) throws ConsistirException {
    	if (!obj.getProspectsVO().getCodigo().equals(0)) {
    		return;
    	}
        if ((obj.getEscolaridade()!= null && !obj.getEscolaridade().equals("EF") && !obj.getEscolaridade().equals("EM")) && (obj.getCurso() == null || obj.getCurso().trim().equals(""))) {
            throw new ConsistirException("O campo CURSO (Formação Acadêmica) deve ser informado.");
        }
        if ((obj.getEscolaridade().equals("EF") || obj.getEscolaridade().equals("EM")) && (obj.getCurso() == null || obj.getCurso().equals(""))){
        	obj.setCurso(obj.getEscolaridade_Apresentar());
        }
        /*if ((obj.getInstituicao() == null || obj.getInstituicao().trim().equals("")) && !obj.getFuncionario()) {
            throw new ConsistirException("O campo INSTITUIÇÃO (Formação Acadêmica) deve ser informado.");
        }*/
        if (obj.getEscolaridade() == null || obj.getEscolaridade().equals("") || obj.getEscolaridade().equals("0")) {
            throw new ConsistirException("O campo ESCOLARIDADE (Formação Acadêmica) deve ser informado.");
        }
//        if (obj.getAreaConhecimento().getCodigo() == null || obj.getAreaConhecimento().getCodigo().intValue() == 0) {
//            throw new ConsistirException("O campo AREA DE CONHECIMENTO (Formação Acadêmica) deve ser informado.");
//        }
        if (obj.getSituacao() == null || obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Formação Acadêmica) deve ser informado.");
        }
        
//        if (obj.getDataInicio() == null) {
//            throw new ConsistirException("O campo DATA INÍCIO (Formação Acadêmica) deve ser informado.");
//        }
//        if (obj.getSituacao().equals("CO") && obj.getDataFim() == null) {
//            throw new ConsistirException("O campo DATA FIM (Formação Acadêmica) deve ser informado.");
//        }       
//        if (obj.getAnoDataFim().equals("0")) {
//        	obj.setAnoDataFim("");
//        }    
      

    }

    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimentoVO areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public Integer getPessoa() {
        if (pessoa == null) {
            pessoa = 0;
        }
        return (pessoa);
    }

    public void setPessoa(Integer pessoa) {
        this.pessoa = pessoa;
    }

    @XmlElement(name ="dataFim")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+3", pattern = "dd-MM-yyyy HH:mm:ss")
    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return (dataFim);
    }

    @XmlElement(name ="anoDataFim")
    public String getAnoDataFim() {
        if (anoDataFim == null) {
           anoDataFim = Uteis.getAnoDataAtual();
        }
        return (anoDataFim);
    }

    @XmlElement(name ="semestreDataFim")
    public String getSemestreDataFim() {
    	if (semestreDataFim == null) {
    		semestreDataFim = Uteis.getSemestreAtual();
    	}
    	return (semestreDataFim);
    }
    
    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFim_Apresentar() {
        return (Uteis.getData(dataFim));
    }

    public String getAnoDataFim_Apresentar() {
        if (anoDataFim == null) {
            anoDataFim = "";
        }
        return anoDataFim;
    }

    public String getSemestreDataFim_Apresentar() {
    	if (semestreDataFim == null) {
    		semestreDataFim = "";
    	}
    	return semestreDataFim;
    }
    
    public String getAnoSemestreConclusao_Apresentar() {
    	 String anoSemestreConclusao = "";
    	if(Uteis.isAtributoPreenchido(semestreDataFim) && Uteis.isAtributoPreenchido(anoDataFim)) {
    		anoSemestreConclusao = anoDataFim +" / "+ semestreDataFim ;
    	}
    	return anoSemestreConclusao ;
    }

	public String getDataFim_ApresentarAno2Digitos() {
		if (Uteis.isAtributoPreenchido(dataFim)) {
			return String.valueOf(Uteis.getAnoData(dataFim));
		}
		return "";
	}

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public void setAnoDataFim(String anoDataFim) {
        this.anoDataFim = anoDataFim;
    }

    public void setSemestreDataFim(String semestreDataFim) {
    	this.semestreDataFim = semestreDataFim;
    }

    @XmlElement(name ="dataInicio")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+3", pattern = "dd-MM-yyyy HH:mm:ss")
    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return (dataInicio);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicio_Apresentar() {
        return (Uteis.getData(dataInicio));
    }

    public String getAnoDataInicio_Apresentar() {
        return Uteis.getAnoData(dataInicio) + "";
    }

	public String getDataInicio_ApresentarAno2Digitos() {
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			return String.valueOf(Uteis.getAnoData(dataInicio));
		}
		return "";
	}

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    @XmlElement(name ="situacao")
    public String getSituacao() {
        if (situacao == null) {
            situacao = "CO";
        }
        return (situacao);
    }

    @XmlElement(name ="tipoInst")
    public String getTipoInst() {
        if (tipoInst == null) {
            tipoInst = "";
        }
        return (tipoInst);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (getSituacao().equals("CU")) {
            return "Cursando";
        }
        if (getSituacao().equals("CO")) {
            return "Concluído";
        }
        return (situacao);
    }

    public Boolean isSituacaoFormacaoAcademicaConcluida() {
    	return getSituacao().equals("CO");
    }
    
    public String getTipoInst_Apresentar() {
        if (getTipoInst().equals("PU")) {
            return "PÚBLICA";
        }
        if (getTipoInst().equals("PR")) {
            return "PRIVADA";
        }
        return (tipoInst);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public void setTipoInst(String tipoInst) {
        this.tipoInst = tipoInst;
    }

    @XmlElement(name ="curso")
    public String getCurso() {
       if (curso == null) {
    	   curso = "";
	}
    	return (curso);
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @XmlElement(name ="escolaridade")
    public String getEscolaridade() {
        if (escolaridade == null) {
            escolaridade = "GR";
        }
        return (escolaridade);
    }

    public Boolean getDesenharDataFim() {
        if (situacao == null || situacao.equals("")) {
            return Boolean.FALSE;
        }
        if (situacao.equals("CO")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getEscolaridade_Apresentar() {
//		if (escolaridade.equals("DO")) {
//			return "Doutorado";
//		}
//		if (escolaridade.equals("PD")) {
//			return "Pós-Doutorado";
//		}
//		if (escolaridade.equals("GR")) {
//			return "Graduação";
//		}
//		if (escolaridade.equals("ME")) {
//			return "Mestrado";
//		}
//		if (escolaridade.equals("ES")) {
//			return "Especialização";
//		}
//		if (escolaridade.equals("EF")) {
//			return "Ensino Fundamental";
//		}
//		if (escolaridade.equals("EM")) {
//			return "Ensino Médio";
//		}
//		return (escolaridade);
    	if (getEscolaridade().equals("PR")) {
    		return NivelFormacaoAcademica.TECNICO.getDescricao();
    	}
    	if (getEscolaridade().equals("SU")) {
    		return NivelFormacaoAcademica.GRADUACAO.getDescricao();
    	}
    	if (getEscolaridade().equals("EM") || getEscolaridade().equals("ME")) {
    		return NivelFormacaoAcademica.MEDIO.getDescricao();
    	}
    	if (getEscolaridade().equals("IN")) {
    		return NivelFormacaoAcademica.FUNDAMENTAL.getDescricao();
    	}
        return NivelFormacaoAcademica.getDescricao(getEscolaridade());
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    @XmlElement(name ="instituicao")
    public String getInstituicao() {
        if (instituicao == null) {
            instituicao = "";
        }
        return (instituicao);
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    @XmlElement(name ="codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name="cidade")
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    public Boolean getFuncionario() {
        if (funcionario == null) {
            funcionario = false;
        }
        return funcionario;
    }

    public void setFuncionario(Boolean funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the descricaoAreaConhecimento
     */
    public String getDescricaoAreaConhecimento() {
        if (descricaoAreaConhecimento == null) {
            descricaoAreaConhecimento = "";
        }
        return descricaoAreaConhecimento;
    }

    /**
     * @param descricaoAreaConhecimento the descricaoAreaConhecimento to set
     */
    public void setDescricaoAreaConhecimento(String descricaoAreaConhecimento) {
        this.descricaoAreaConhecimento = descricaoAreaConhecimento;
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
	
	@XmlElement(name ="modalidade")
	public String getModalidade() {
		if (modalidade == null) {
			modalidade = "";
		}
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<SelectItem> getListaSelectItemTitulo() {
		if (listaSelectItemTitulo == null) {
			listaSelectItemTitulo = NivelFormacaoAcademica.getListaSelectItemTituloPorValorNivelFormacaoAcademica(getEscolaridade());
		}
		return listaSelectItemTitulo;
	}

	public void setListaSelectItemTitulo(List<SelectItem> listaSelectItemTitulo) {
		this.listaSelectItemTitulo = listaSelectItemTitulo;
	}
	
	public void montarListaSelectItemTitulo() {
		setListaSelectItemTitulo(NivelFormacaoAcademica.getListaSelectItemTituloPorValorNivelFormacaoAcademica(getEscolaridade()));
	}
}
