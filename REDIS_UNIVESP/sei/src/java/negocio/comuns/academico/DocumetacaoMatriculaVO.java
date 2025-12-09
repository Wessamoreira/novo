package negocio.comuns.academico;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.academico.Matricula;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade DocumetacaoMatricula. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
@XmlRootElement(name = "documetacaoMatriculaVO")
public class DocumetacaoMatriculaVO extends SuperVO {

    private Integer codigo;
    private String situacao;
    private String matricula;
    private String curso;	
    private Boolean entregue;
    private Boolean entreguePorEquivalencia;
    private Boolean gerarSuspensaoMatricula;
    private Boolean arquivoAprovadoPeloDep;
    private Date dataEntrega;
    private UsuarioVO usuario;
    private TipoDocumentoVO tipoDeDocumentoVO;
    
    private ArquivoVO arquivoVO;
    private ArquivoVO arquivoVOVerso;
	private ArquivoVO arquivoGED;
    private Boolean excluirArquivo;
    private UsuarioVO respAprovacaoDocDep;
    private UsuarioVO respNegarDocDep;
    private Date dataAprovacaoDocDep;
    private Date dataNegarDocDep;
    private String justificativaNegacao;
    private String localUpload;
    private ArquivoVO arquivoVOAssinado;
    private Boolean deletarArquivo;
    private String descricaoprocessamento;
    private Boolean processadocomerro;
    private Date dataProcessamento;
    private MatriculaVO matriculaVO;
    private MotivoIndeferimentoDocumentoAlunoVO motivoIndeferimentoDocumentoAlunoVO;
    
    
    public static final long serialVersionUID = 1L;
    
    // TRANSIENT - utlizado para mostrar para o usuário que o documento
    // em questão está configurado para impedir a renovação do aluno
    // caso o mesmo esteja pendente.
    private Boolean documentoImpedindoRenovacao;
    // Utilizado para os documentos que são Obrigatorios a Cada Renovação
    private String semestre;
    private String ano;
 // TRANSIENT -
    //
    private File fileAssinar;
    //TRANSIENT
    private Boolean documentacaoCursoExistente;
    // TRANSIENT - ARMAZENA O VALOR ANTIGO DA VARIAVEL entregue, a mesma afeta o método getJustificativa no momento de 
    // indeferir um documento que esteja com o atributo entregue como true, limpando o valor da variavel justificativa.
    // Foi preferivel utilizar esse atributo de controle para evitar possíveis impactos em outros pontos do código.
    private Boolean valorEntregueAnterior;
    
    /**
     * ATRIBUTO TRANSIENT
     */
    private Boolean validarPdfA;
    private Boolean realizarConversaoPdfAImagem;
    private String caminhoPreviewPdfA;
    private ArquivoVO arquivoAssinado;
    private Boolean montarDadosArquivo;
    


    /**
     * Construtor padrão da classe <code>DocumetacaoMatricula</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DocumetacaoMatriculaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DocumetacaoMatriculaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DocumetacaoMatriculaVO obj) throws ConsistirException {
        
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Documetação Apresentada Matrícula) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setSituacao(new String("PE"));
        setEntregue(Boolean.FALSE);
        setEntreguePorEquivalencia(Boolean.FALSE);
        setDataEntrega(new Date());
    }

    public String getEntregue_Apresentar() {
        if (entregue) {
        	return "SIM";
        } else {
        	return "NÃO";
        }
    }
	
    @XmlElement(name = "entregue")
    public Boolean getEntregue() {
        return (entregue);
    }

    @XmlElement(name = "isEntregue")
    public Boolean isEntregue() {
        return (entregue);
    }

    public void setEntregue(Boolean entregue) {
        this.entregue = entregue;
    }

//    @XmlElement(name = "entreguePorEquivalencia")
    public Boolean getEntreguePorEquivalencia() {
        return (entreguePorEquivalencia);
    }

    public void setEntreguePorEquivalencia(Boolean entreguePorEquivalencia) {
        this.entreguePorEquivalencia = entreguePorEquivalencia;
    }

    @XmlElement(name = "matricula")
    public String getMatricula() {
    	if (matricula == null) {
    		matricula = "";
    	}
        return (matricula);
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
    @XmlElement(name = "situacao")
    public String getSituacao_Apresentar() {
        if (situacao.equals("PE")) {
            return "Pendente";
        }
        if (situacao.equals("OK")) {
            return "OK";
        }
        if (situacao.equals("EI")) {
            return "Entregue Incorretamente";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the tipoDeDocumentoVO
     */
    @XmlElement(name = "tipoDeDocumentoVO")
    public TipoDocumentoVO getTipoDeDocumentoVO() {
        if (tipoDeDocumentoVO == null) {
            tipoDeDocumentoVO = new TipoDocumentoVO();
        }
        return tipoDeDocumentoVO;
    }

    /**
     * @param tipoDeDocumentoVO
     *            the tipoDeDocumentoVO to set
     */
    public void setTipoDeDocumentoVO(TipoDocumentoVO tipoDeDocumentoVO) {
        this.tipoDeDocumentoVO = tipoDeDocumentoVO;
    }

    /**
     * @return the dataEntrega
     */
    @XmlElement(name = "dataEntrega")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataEntrega() {
        return dataEntrega;
    }
    
    @XmlElement(name = "dataEntregaApresentacao")
    public String getDataEntrega_Apresentacao() {
        String dataStr = "";
        if (getDataEntrega() != null) {
            dataStr = Uteis.getData(getDataEntrega());
        }
        return dataStr;
    }

    /**
     * @param dataEntrega
     *            the dataEntrega to set
     */
    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    /**
     * @return the usuario
     */
//    @XmlElement(name = "usuario")
    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    /**
     * @param usuario
     *            the usuario to set
     */
    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    @XmlElement(name = "arquivoVO")
    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }

    @XmlElement(name = "arquivoVOVerso")
    public ArquivoVO getArquivoVOVerso() {
        if (arquivoVOVerso == null) {
            arquivoVOVerso = new ArquivoVO();
        }
        return arquivoVOVerso;
    }

    public void setArquivoVOVerso(ArquivoVO arquivoVOVerso) {
        this.arquivoVOVerso = arquivoVOVerso;
    }
	
//    @XmlElement(name = "arquivoGED")
	public ArquivoVO getArquivoGED() {
    	if (arquivoGED == null) {
    		arquivoGED = new ArquivoVO();
    	}
		return arquivoGED;
	}

	public void setArquivoGED(ArquivoVO arquivoGED) {
		this.arquivoGED = arquivoGED;
	}

//	@XmlElement(name = "excluirArquivo")
    public Boolean getExcluirArquivo() {
        if (excluirArquivo == null) {
            excluirArquivo = false;
        }
        return excluirArquivo;
    }

    public void setExcluirArquivo(Boolean excluirArquivo) {
        this.excluirArquivo = excluirArquivo;
    }

    @XmlElement(name = "isPossuiArquivo")
    public boolean getIsPossuiArquivo() {
        return !getArquivoVO().getNome().equals("");
    }

    @XmlElement(name = "isPossuiArquivoVerso")
    public boolean getIsPossuiArquivoVerso() {
        return !getArquivoVOVerso().getNome().equals("");
    }
    
    public boolean getIsPossuiArquivoAssinado() {
    	return !getArquivoVOAssinado().getNome().equals("") || Uteis.isAtributoPreenchido(getArquivoVOAssinado().getCodigo()) ;
    }

//    @XmlElement(name = "isPossuiArquivoBanco")
    public boolean getIsPossuiArquivoBanco() {
        return !getArquivoVO().getCodigo().equals(0);
    }

    public boolean getDocumentoEntregue() {
        return getEntregue();
    }

    @XmlElement(name = "gerarSuspensaoMatricula")
    public Boolean getGerarSuspensaoMatricula() {
        if (gerarSuspensaoMatricula == null) {
            gerarSuspensaoMatricula = Boolean.FALSE;
        }
        return gerarSuspensaoMatricula;
    }

    public void setGerarSuspensaoMatricula(Boolean gerarSuspensaoMatricula) {
        this.gerarSuspensaoMatricula = gerarSuspensaoMatricula;
    }

    /**
     * @return the arquivoAprovadoPeloDep
     */
    @XmlElement(name = "arquivoAprovadoPeloDep")
    public Boolean getArquivoAprovadoPeloDep() {
        return arquivoAprovadoPeloDep;
    }

    /**
     * @param arquivoAprovadoPeloDep the arquivoAprovadoPeloDep to set
     */
    public void setArquivoAprovadoPeloDep(Boolean arquivoAprovadoPeloDep) {
        this.arquivoAprovadoPeloDep = arquivoAprovadoPeloDep;
    }

    /**
     * @return the respAprovacaoDocDep
     */
    @XmlElement(name = "respAprovacaoDocDep")
    public UsuarioVO getRespAprovacaoDocDep() {
        if (respAprovacaoDocDep == null) {
            respAprovacaoDocDep = new UsuarioVO();
        }
        return respAprovacaoDocDep;
    }

    public boolean getDocumetacaoMatriculaAprovado() {
        if (respAprovacaoDocDep == null) {
            return false;
        } else if (getRespAprovacaoDocDep().getCodigo().intValue() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param respAprovacaoDocDep the respAprovacaoDocDep to set
     */
    public void setRespAprovacaoDocDep(UsuarioVO respAprovacaoDocDep) {
        this.respAprovacaoDocDep = respAprovacaoDocDep;
    }

    /**
     * @return the respNegarDocDep
     */
    @XmlElement(name = "respNegarDocDep")
    public UsuarioVO getRespNegarDocDep() {
        if (respNegarDocDep == null) {
            respNegarDocDep = new UsuarioVO();
        }
        if (getEntregue()) {
        	respNegarDocDep = new UsuarioVO();
        }
        return respNegarDocDep;
    }

    /**
     * @param respNegarDocDep the respNegarDocDep to set
     */
    public void setRespNegarDocDep(UsuarioVO respNegarDocDep) {
        this.respNegarDocDep = respNegarDocDep;
    }

    /**
     * @return the dataAprovacaoDocDep
     */
    @XmlElement(name = "dataAprovacaoDocDep")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataAprovacaoDocDep() {
      
        return dataAprovacaoDocDep;
    }

    /**
     * @param dataAprovacaoDocDep the dataAprovacaoDocDep to set
     */
    public void setDataAprovacaoDocDep(Date dataAprovacaoDocDep) {
        this.dataAprovacaoDocDep = dataAprovacaoDocDep;
    }

    /**
     * @return the dataNegarDocDep
     */
    @XmlElement(name = "dataNegarDocDep")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataNegarDocDep() {
       
        return dataNegarDocDep;
    }

    /**
     * @param dataNegarDocDep the dataNegarDocDep to set
     */
    public void setDataNegarDocDep(Date dataNegarDocDep) {
        this.dataNegarDocDep = dataNegarDocDep;
    }

    /**
     * @return the justificativaNegacao
     */
    @XmlElement(name = "justificativaNegacao")
    public String getJustificativaNegacao() {
        if (justificativaNegacao == null) {
            justificativaNegacao = "";
        }        
        return justificativaNegacao;
    }

    /**
     * @param justificativaNegacao the justificativaNegacao to set
     */
    public void setJustificativaNegacao(String justificativaNegacao) {
        this.justificativaNegacao = justificativaNegacao;
    }

    /**
     * @return the localUpload
     */
//    @XmlElement(name = "localUpload")
    public String getLocalUpload() {
        if (localUpload == null) {
            localUpload = "Administrativo";
        }
        return localUpload;
    }

    /**
     * @param localUpload the localUpload to set
     */
    public void setLocalUpload(String localUpload) {
        this.localUpload = localUpload;
    }

    /**
     * @return the documentoImpedindoRenovacao
     */
//    @XmlElement(name = "documentoImpedindoRenovacao")
    public Boolean getDocumentoImpedindoRenovacao() {
        if (documentoImpedindoRenovacao == null) {
            documentoImpedindoRenovacao = Boolean.TRUE;
        }
        return documentoImpedindoRenovacao;
    }

    /**
     * @param documentoImpedindoRenovacao the documentoImpedindoRenovacao to set
     */
    public void setDocumentoImpedindoRenovacao(Boolean documentoImpedindoRenovacao) {
        this.documentoImpedindoRenovacao = documentoImpedindoRenovacao;
    }

//    @XmlElement(name = "curso")
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	@XmlElement(name = "arquivoVOAssinado")
	public ArquivoVO getArquivoVOAssinado() {
		if(arquivoVOAssinado == null) {
			arquivoVOAssinado = new ArquivoVO();
		}
		return arquivoVOAssinado;
	}

	public void setArquivoVOAssinado(ArquivoVO arquivoVOAssinado) {
		this.arquivoVOAssinado = arquivoVOAssinado;
	}
	
	public File getFileAssinar() {
		if(fileAssinar == null) {
			fileAssinar = new File("");
		}
		return fileAssinar;
	}

	public void setFileAssinar(File fileAssinar) {
		this.fileAssinar = fileAssinar;
	}

	public Boolean getDeletarArquivo() {
		if(deletarArquivo == null) {
			deletarArquivo = false;
		}
		return deletarArquivo;
	}

	public void setDeletarArquivo(Boolean deletarArquivo) {
		this.deletarArquivo = deletarArquivo;
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

	public String getDescricaoprocessamento() {
		if(descricaoprocessamento == null) {
			descricaoprocessamento = "";
		}
		return descricaoprocessamento;
	}

	public void setDescricaoprocessamento(String descricaoprocessamento) {
		this.descricaoprocessamento = descricaoprocessamento;
	}

	public Boolean getProcessadocomerro() {
		if(processadocomerro == null) {
			processadocomerro = false;
		}
		return processadocomerro;
	}

	public void setProcessadocomerro(Boolean processadocomerro) {
		this.processadocomerro = processadocomerro;
	}
	
	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}
	
	  public String getData_Apresentar() {
	        return (Uteis.getDataComHora(dataProcessamento));
	    }

	public Boolean getDocumentacaoCursoExistente() {
		if (documentacaoCursoExistente == null) {
			documentacaoCursoExistente = false;
		}
		return documentacaoCursoExistente;
	}

	public void setDocumentacaoCursoExistente(Boolean documentacaoCursoExistente) {
		this.documentacaoCursoExistente = documentacaoCursoExistente;
	}


	public Boolean getDocumentoPendente() {
		return !this.getEntregue() &&  (this.getArquivoAprovadoPeloDep() == null || !this.getArquivoAprovadoPeloDep())  && !this.getIsPossuiArquivo() && this.getDataNegarDocDep() == null;
		 
	}  
	
	public Boolean getDocumentoPendenteAprovacao() {
		return !this.getEntregue() &&  (this.getArquivoAprovadoPeloDep() == null || !this.getArquivoAprovadoPeloDep())   && this.getIsPossuiArquivo() && this.getDataNegarDocDep() == null;
	}  
	
	public Boolean getDocumentoRejeitado() {
		return !this.getEntregue() && this.getDataNegarDocDep() != null && Uteis.isAtributoPreenchido( this.getRespNegarDocDep().getCodigo());
		
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
	  
	  
	public static void removerCamposNaoUsadoAPI(DocumetacaoMatriculaVO documetacaoMatriculaVO) throws Exception{
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoVO(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline", "caminhoImagemAnexo");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoVO().getResponsavelUpload(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoVOVerso(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline", "caminhoImagemAnexo");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoVOVerso().getResponsavelUpload(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoVOAssinado(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoVOAssinado().getResponsavelUpload(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoGED(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getArquivoGED().getResponsavelUpload(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getRespAprovacaoDocDep(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getRespNegarDocDep(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getUsuario(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getTipoDeDocumentoVO(), "codigo", "nome", "contrato", "documentoFrenteVerso", "permitirPostagemPortalAluno", "extensaoArquivo");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getMatriculaVO(), "matricula", "aluno", "curso");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getMatriculaVO().getCurso(), "codigo", "nome");
		 UtilReflexao.removerCamposChamadaAPI(documetacaoMatriculaVO.getMatriculaVO().getAluno(), "codigo", "nome", "cpf");
		 
		 
}
	
	public static Response removerCamposChamadaAPI(DocumetacaoMatriculaVO documetacaoMatriculaVO) throws Exception{
		removerCamposNaoUsadoAPI(documetacaoMatriculaVO);
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
		String json =	gson.toJson(documetacaoMatriculaVO);
		return Response.status(Status.OK).entity(json).build();
	}
	
	public static Response removerCamposChamadaAPI(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs) throws Exception{
		for(DocumetacaoMatriculaVO documetacaoMatriculaVO: documetacaoMatriculaVOs) {
			removerCamposNaoUsadoAPI(documetacaoMatriculaVO);
		}
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
		String json =	gson.toJson(documetacaoMatriculaVOs);
		return Response.status(Status.OK).entity(json).build();
	}

    public MotivoIndeferimentoDocumentoAlunoVO getMotivoIndeferimentoDocumentoAlunoVO() {
        if (motivoIndeferimentoDocumentoAlunoVO == null) {
            motivoIndeferimentoDocumentoAlunoVO = new MotivoIndeferimentoDocumentoAlunoVO();
        }
        return motivoIndeferimentoDocumentoAlunoVO;
    }

    public void setMotivoIndeferimentoDocumentoAlunoVO(MotivoIndeferimentoDocumentoAlunoVO motivoIndeferimentoDocumentoAlunoVO) {
        this.motivoIndeferimentoDocumentoAlunoVO = motivoIndeferimentoDocumentoAlunoVO;
    }

    public Boolean getValorEntregueAnterior() {
        if (valorEntregueAnterior == null) {
            valorEntregueAnterior = getEntregue();
        }
        return valorEntregueAnterior;
    }

    public void setValorEntregueAnterior(Boolean valorEntregueAnterior) {
        this.valorEntregueAnterior = valorEntregueAnterior;
    }
    
    public Boolean getIndeferido() {    	
    	return Uteis.isAtributoPreenchido(getDataNegarDocDep()) && Uteis.isAtributoPreenchido(getRespNegarDocDep());
    }

	public Boolean getValidarPdfA() {
		if (validarPdfA == null) {
			validarPdfA = Boolean.FALSE;
		}
		return validarPdfA;
	}

	public void setValidarPdfA(Boolean validarPdfA) {
		this.validarPdfA = validarPdfA;
	}

	public Boolean getRealizarConversaoPdfAImagem() {
		if (realizarConversaoPdfAImagem == null) {
			realizarConversaoPdfAImagem = Boolean.FALSE;
		}
		return realizarConversaoPdfAImagem;
	}

	public void setRealizarConversaoPdfAImagem(Boolean realizarConversaoPdfAImagem) {
		this.realizarConversaoPdfAImagem = realizarConversaoPdfAImagem;
	}

	public String getCaminhoPreviewPdfA() {
		if (caminhoPreviewPdfA == null) {
			caminhoPreviewPdfA = Constantes.EMPTY;
		}
		return caminhoPreviewPdfA;
	}

	public void setCaminhoPreviewPdfA(String caminhoPreviewPdfA) {
		this.caminhoPreviewPdfA = caminhoPreviewPdfA;
	}
	
	public ArquivoVO getArquivoAssinado() {
		if (arquivoAssinado == null) {
			arquivoAssinado = new ArquivoVO();
		}
		return arquivoAssinado;
	}
	
	public void setArquivoAssinado(ArquivoVO arquivoAssinado) {
		this.arquivoAssinado = arquivoAssinado;
	}
	
	public Boolean getMontarDadosArquivo() {
		if (montarDadosArquivo == null) {
			montarDadosArquivo = Boolean.TRUE;
		}
		return montarDadosArquivo;
	}
	
	public void setMontarDadosArquivo(Boolean montarDadosArquivo) {
		this.montarDadosArquivo = montarDadosArquivo;
	}
	
	public boolean isDesabilitarBotaoUparArquivo() {
		return getIsPossuiArquivoAssinado();
	}
	
	public String getIdDocumetacao() {
		return new StringBuilder("DM").append(Uteis.isAtributoPreenchido(getArquivoVOAssinado()) ? getArquivoVOAssinado().getCodigo().toString() : getCodigo().toString()).toString();
	}
}
