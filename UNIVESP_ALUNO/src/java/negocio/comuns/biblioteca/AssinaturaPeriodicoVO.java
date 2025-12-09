package negocio.comuns.biblioteca;

/**
 * Reponsável por manter os dados da entidade Assinatura. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class AssinaturaPeriodicoVO extends SuperVO {

    private Integer codigo;
    private Date dataCadastro;
    private Date dataInicioAssinatura;
    private Date dataFinalAssinatura;
    private String periodicidade;
    private String nome;
    private String pha;
    private String cdu;
    private String isbn;
    private EditoraVO editora;
    private String mes;
    private String anovolume;
    private String nrEdicaoEspecial;
    private String situacaoAssinatura;
    private String localPublicacao;
    private List<ExemplarVO> exemplarVOs;
	private List<CatalogoAreaConhecimentoVO> catalogoAreaConhecimentoVOs;
	private List<CatalogoCursoVO> catalogoCursoVOs;	
    
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO usuarioResponsavel;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Assinatura</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public AssinaturaPeriodicoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AssinaturaPeriodicoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AssinaturaPeriodicoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getUsuarioResponsavel() == null) || (obj.getUsuarioResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo USUARIO (AssinaturaPeriodico) deve ser informado.");
        }
        if (obj.getNome() == null || obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (AssinaturaPeriodico) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setPeriodicidade(getPeriodicidade().toUpperCase());
    }

    public UsuarioVO getUsuarioResponsavel() {
        if (usuarioResponsavel == null) {
            usuarioResponsavel = new UsuarioVO();
        }
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public String getPeriodicidade() {
        if (periodicidade == null) {
            periodicidade = "";
        }
        return (periodicidade);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getPeriodicidade_Apresentar() {
        if (periodicidade == null) {
            return "";
        }
        if (periodicidade.equals("QI")) {
            return "Quinzenal";
        }
        if (periodicidade.equals("SM")) {
            return "Semestral";
        }
        if (periodicidade.equals("BI")) {
            return "Bimestral";
        }
        if (periodicidade.equals("DI")) {
            return "Diário";
        }
        if (periodicidade.equals("ME")) {
            return "Mensal";
        }
        if (periodicidade.equals("AN")) {
            return "Anual";
        }
        if (periodicidade.equals("SE")) {
            return "Semanal";
        }
        if (periodicidade.equals("TR")) {
            return "Trimestral";
        }
        if (periodicidade.equals("QA")) {
        	return "Quadrimestral";
        }
        if (periodicidade.equals("IR")) {
        	return "Irregular";
        }
        return (periodicidade);
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }

    public Date getDataFinalAssinatura() {
        if (dataFinalAssinatura == null) {
            dataFinalAssinatura = new Date();
        }
        return (dataFinalAssinatura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalAssinatura_Apresentar() {
        return (Uteis.getData(getDataFinalAssinatura()));
    }

    public void setDataFinalAssinatura(Date dataFinalAssinatura) {
        this.dataFinalAssinatura = dataFinalAssinatura;
    }

    public Date getDataInicioAssinatura() {
        if (dataInicioAssinatura == null) {
            dataInicioAssinatura = new Date();
        }
        return (dataInicioAssinatura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicioAssinatura_Apresentar() {
        return (Uteis.getData(getDataInicioAssinatura()));
    }

    public void setDataInicioAssinatura(Date dataInicioAssinatura) {
        this.dataInicioAssinatura = dataInicioAssinatura;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return (dataCadastro);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataCadastro_Apresentar() {
        return (Uteis.getData(getDataCadastro()));
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ExemplarVO> getExemplarVOs() {
        if (exemplarVOs == null) {
            exemplarVOs = new ArrayList(0);
        }
        return exemplarVOs;
    }

    public void setExemplarVOs(List<ExemplarVO> exemplarVOs) {
        this.exemplarVOs = exemplarVOs;
    }

	public String getPha() {
		if(pha == null){
			pha = "";
		}
		return pha;
	}

	public void setPha(String pha) {
		this.pha = pha;
	}

	public String getCdu() {
		if(cdu == null){
			cdu = "";
		}
		return cdu;
	}

	public void setCdu(String cdu) {
		this.cdu = cdu;
	}

	public String getIsbn() {
		if(isbn == null){
			isbn = "";
		}
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public EditoraVO getEditora() {
		if(editora == null){
			editora = new EditoraVO();
		}
		return editora;
	}

	public void setEditora(EditoraVO editora) {
		this.editora = editora;
	}

	public String getMes() {
		if(mes == null){
			mes= "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnovolume() {
		if(anovolume == null){
			anovolume = "";
		}
		return anovolume;
	}

	public void setAnovolume(String anovolume) {
		this.anovolume = anovolume;
	}

	public String getNrEdicaoEspecial() {
		if(nrEdicaoEspecial == null){
			nrEdicaoEspecial = "";
		}
		return nrEdicaoEspecial;
	}

	public void setNrEdicaoEspecial(String nrEdicaoEspecial) {
		this.nrEdicaoEspecial = nrEdicaoEspecial;
	}

	public String getSituacaoAssinatura() {
		if(situacaoAssinatura == null){
			situacaoAssinatura = "";
		}
		return situacaoAssinatura;
	}

	public void setSituacaoAssinatura(String situacaoAssinatura) {
		this.situacaoAssinatura = situacaoAssinatura;
	}

	public String getLocalPublicacao() {
		if(localPublicacao == null){
			localPublicacao = "";
		}
		return localPublicacao;
	}

	public void setLocalPublicacao(String localPublicacao) {
		this.localPublicacao = localPublicacao;
	}
	
	public List<CatalogoAreaConhecimentoVO> getCatalogoAreaConhecimentoVOs() {
		if (catalogoAreaConhecimentoVOs == null) {
			catalogoAreaConhecimentoVOs = new ArrayList<CatalogoAreaConhecimentoVO>(0);
		}
		return catalogoAreaConhecimentoVOs;
	}

	public void setCatalogoAreaConhecimentoVOs(List<CatalogoAreaConhecimentoVO> catalogoAreaConhecimentoVOs) {
		this.catalogoAreaConhecimentoVOs = catalogoAreaConhecimentoVOs;
	}

	public List<CatalogoCursoVO> getCatalogoCursoVOs() {
		if (catalogoCursoVOs == null) {
			catalogoCursoVOs = new ArrayList<CatalogoCursoVO>(0);
		}
		return catalogoCursoVOs;
	}

	public void setCatalogoCursoVOs(List<CatalogoCursoVO> catalogoCursoVOs) {
		this.catalogoCursoVOs = catalogoCursoVOs;
	}

}