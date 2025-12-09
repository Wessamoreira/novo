package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import webservice.servicos.IntegracaoDisciplinaMatriculaVO;
import webservice.servicos.IntegracaoMatriculaCRMVO;

/**
 * Reponsável por manter os dados da entidade Curso. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement
public class MatriculaCRMVO extends SuperVO {

    private Integer codigo;
    private UsuarioVO usuario;
    private Date data;
    private Date dataMatricula;
    private Date dataVencimento;
    private Date dataBaseGeracaoParcela;
    private UnidadeEnsinoVO unidadeEnsino;
    private PessoaVO aluno;
    private String localArmazenamentoDocumentosMatricula;
    private TurmaVO turma;
    private FuncionarioVO consultor;
    private ProcessoMatriculaVO processoMatricula;
    private PlanoFinanceiroCursoVO planoFinanceiroCurso;
    private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso;
    private DescontoProgressivoVO descontoProgressivoPrimeiraParcela;
    private PlanoDescontoVO planoDesconto;
    private Integer codigoContrato;
    private FormacaoAcademicaVO formacaoAcademica;
    private String urlBoletoMatricula;
    private String diretorioBoletoMatricula;
    private String htmlContratoMatricula;
    private boolean matriculaFinalizada;
    private boolean erro;
    private boolean bolsa;
    private String mensagemErro;
    private String matricula;
    private Integer matriculaPeriodo;
    private String tipoMatricula;
    private Boolean permitiMatricula4Modulo;	
    private Boolean permitiMatriculaInadipliente;
    private List<DocumentacaoMatriculaCRMVO> documentacaoMatriculaCRM;
    private List<DisciplinaMatriculaCRMVO> disciplinaMatriculaCRM;
    private List<PlanoDescontoVO> listaPlanoDescontoVO;
    private boolean processarApenasImpressaoContrato = false;
    private String diretorioContratoPdf;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Curso</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public MatriculaCRMVO() {
        super();
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>CursoTurnoVO</code> ao List <code>cursoTurnoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>CursoTurno</code> -
     * getTurno().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>CursoTurnoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjDocumentacaoMatriculaCRMVOs(DocumentacaoMatriculaCRMVO obj) throws Exception {
        int index = 0;
        Iterator i = getDocumentacaoMatriculaCRM().iterator();
        while (i.hasNext()) {
            DocumentacaoMatriculaCRMVO objExistente = (DocumentacaoMatriculaCRMVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(obj.getTipoDeDocumentoVO().getCodigo())) {
                getDocumentacaoMatriculaCRM().set(index, obj);
                return;
            }
            index++;
        }
        getDocumentacaoMatriculaCRM().add(obj);
    }

    public void adicionarObjDisciplinaMatriculaCRMVOs(DisciplinaMatriculaCRMVO obj) throws Exception {
    	int index = 0;
    	Iterator i = getDisciplinaMatriculaCRM().iterator();
    	while (i.hasNext()) {
    		DisciplinaMatriculaCRMVO objExistente = (DisciplinaMatriculaCRMVO) i.next();
    		if (objExistente.getDisciplinaVO().getCodigo().equals(obj.getDisciplinaVO().getCodigo())) {
    			getDisciplinaMatriculaCRM().set(index, obj);
    			return;
    		}
    		index++;
    	}
    	getDisciplinaMatriculaCRM().add(obj);
    }
    

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>CursoTurnoVO</code> no List <code>cursoTurnoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>CursoTurno</code> -
     * getTurno().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param turno
     *            Parâmetro para localizar o objeto do List.
     */
    public DocumentacaoMatriculaCRMVO consultarObjDocumentacaoMatriculaCRMVO(Integer tipoDocumento) throws Exception {
        Iterator i = getDocumentacaoMatriculaCRM().iterator();
        while (i.hasNext()) {
            DocumentacaoMatriculaCRMVO objExistente = (DocumentacaoMatriculaCRMVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(tipoDocumento)) {
                return objExistente;
            }
        }
        return null;
    }

    public DisciplinaMatriculaCRMVO consultarObjDisciplinaMatriculaCRMVO(Integer disciplina) throws Exception {
    	Iterator i = getDisciplinaMatriculaCRM().iterator();
    	while (i.hasNext()) {
    		DisciplinaMatriculaCRMVO objExistente = (DisciplinaMatriculaCRMVO) i.next();
    		if (objExistente.getDisciplinaVO().getCodigo().equals(disciplina)) {
    			return objExistente;
    		}
    	}
    	return null;
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

    /**
     * @return the usuario
     */
    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the data
     */
    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the unidadeEnsino
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino the unidadeEnsino to set
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the aluno
     */
    public PessoaVO getAluno() {
        if (aluno == null) {
            aluno = new PessoaVO();
        }
        return aluno;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(PessoaVO aluno) {
        this.aluno = aluno;
    }

    /**
     * @return the localArmazenamentoDocumentosMatricula
     */
    public String getLocalArmazenamentoDocumentosMatricula() {
        if (localArmazenamentoDocumentosMatricula == null) {
            localArmazenamentoDocumentosMatricula = "";
        }
        return localArmazenamentoDocumentosMatricula;
    }

    /**
     * @param localArmazenamentoDocumentosMatricula the localArmazenamentoDocumentosMatricula to set
     */
    public void setLocalArmazenamentoDocumentosMatricula(String localArmazenamentoDocumentosMatricula) {
        this.localArmazenamentoDocumentosMatricula = localArmazenamentoDocumentosMatricula;
    }

    /**
     * @return the turma
     */
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    /**
     * @return the processoMatricula
     */
    public ProcessoMatriculaVO getProcessoMatricula() {
        if (processoMatricula == null) {
            processoMatricula = new ProcessoMatriculaVO();
        }
        return processoMatricula;
    }

    /**
     * @param processoMatricula the processoMatricula to set
     */
    public void setProcessoMatricula(ProcessoMatriculaVO processoMatricula) {
        this.processoMatricula = processoMatricula;
    }

    /**
     * @return the condicaoPagamentoPlanoFinanceiroCurso
     */
    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCurso() {
        if (condicaoPagamentoPlanoFinanceiroCurso == null) {
            condicaoPagamentoPlanoFinanceiroCurso = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        }
        return condicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @param condicaoPagamentoPlanoFinanceiroCurso the condicaoPagamentoPlanoFinanceiroCurso to set
     */
    public void setCondicaoPagamentoPlanoFinanceiroCurso(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso) {
        this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @return the diretorioBoletoMatricula
     */
    public String getDiretorioBoletoMatricula() {
        if (diretorioBoletoMatricula == null) {
            diretorioBoletoMatricula = "";
        }
        return diretorioBoletoMatricula;
    }

    /**
     * @param diretorioBoletoMatricula the diretorioBoletoMatricula to set
     */
    public void setDiretorioBoletoMatricula(String diretorioBoletoMatricula) {
        this.diretorioBoletoMatricula = diretorioBoletoMatricula;
    }

    public String getUrlBoletoMatricula() {
    	  if (urlBoletoMatricula == null) {
    		  urlBoletoMatricula = "";
          }
		return urlBoletoMatricula;
	}

	public void setUrlBoletoMatricula(String urlBoletoMatricula) {
		this.urlBoletoMatricula = urlBoletoMatricula;
	}

	/**
     * @return the htmlContratoMatricula
     */
    public String getHtmlContratoMatricula() {
        if (htmlContratoMatricula == null) {
            htmlContratoMatricula = "";
        }
        return htmlContratoMatricula;
    }

    /**
     * @param htmlContratoMatricula the htmlContratoMatricula to set
     */
    public void setHtmlContratoMatricula(String htmlContratoMatricula) {
        this.htmlContratoMatricula = htmlContratoMatricula;
    }

    /**
     * @return the matriculaFinalizada
     */
    public boolean isMatriculaFinalizada() {
        return matriculaFinalizada;
    }

    public boolean getMatriculaFinalizada() {
        return matriculaFinalizada;
    }

    /**
     * @param matriculaFinalizada the matriculaFinalizada to set
     */
    public void setMatriculaFinalizada(boolean matriculaFinalizada) {
        this.matriculaFinalizada = matriculaFinalizada;
    }

    /**
     * @return the documentacaoMatriculaCRM
     */
    public List<DocumentacaoMatriculaCRMVO> getDocumentacaoMatriculaCRM() {
        if (documentacaoMatriculaCRM == null) {
            documentacaoMatriculaCRM = new ArrayList(0);
        }
        return documentacaoMatriculaCRM;
    }

    /**
     * @param documentacaoMatriculaCRM the documentacaoMatriculaCRM to set
     */
    public void setDocumentacaoMatriculaCRM(List<DocumentacaoMatriculaCRMVO> documentacaoMatriculaCRM) {
        this.documentacaoMatriculaCRM = documentacaoMatriculaCRM;
    }

    public List<DisciplinaMatriculaCRMVO> getDisciplinaMatriculaCRM() {
    	if (disciplinaMatriculaCRM == null) {
    		disciplinaMatriculaCRM = new ArrayList(0);
    	}
    	return disciplinaMatriculaCRM;
    }
    
    public void setDisciplinaMatriculaCRM(List<DisciplinaMatriculaCRMVO> disciplinaMatriculaCRM) {
    	this.disciplinaMatriculaCRM = disciplinaMatriculaCRM;
    }
    
    /**
     * @return the planoFinanceiroCurso
     */
    public PlanoFinanceiroCursoVO getPlanoFinanceiroCurso() {
        if (planoFinanceiroCurso == null) {
            planoFinanceiroCurso = new PlanoFinanceiroCursoVO();
        }
        return planoFinanceiroCurso;
    }

    /**
     * @param planoFinanceiroCurso the planoFinanceiroCurso to set
     */
    public void setPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCurso) {
        this.planoFinanceiroCurso = planoFinanceiroCurso;
    }

    public boolean isErro() {
        return erro;
    }

    public boolean getErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public boolean isBolsa() {
        return bolsa;
    }

    public boolean getBolsa() {
        return bolsa;
    }

    public void setBolsa(boolean bolsa) {
        this.bolsa = bolsa;
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

    public PlanoDescontoVO getPlanoDesconto() {
        if (planoDesconto == null) {
            planoDesconto = new PlanoDescontoVO();
        }
        return planoDesconto;
    }

    public void setPlanoDesconto(PlanoDescontoVO planoDesconto) {
        this.planoDesconto = planoDesconto;
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
    
    public Integer getMatriculaPeriodo() {
    	  if (matriculaPeriodo == null) {
    		  matriculaPeriodo = 0;
          }
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(Integer matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	/**
     * @return the formacaoAcademica
     */
    public FormacaoAcademicaVO getFormacaoAcademica() {
        if (formacaoAcademica == null) {
            formacaoAcademica = new FormacaoAcademicaVO();
        }
        return formacaoAcademica;
    }

    /**
     * @param formacaoAcademica the formacaoAcademica to set
     */
    public void setFormacaoAcademica(FormacaoAcademicaVO formacaoAcademica) {
        this.formacaoAcademica = formacaoAcademica;
    }

    /**
     * @return the dataMatricula
     */
    public Date getDataMatricula() {
        if (dataMatricula == null) {
            dataMatricula = new Date();
        }
        return dataMatricula;
    }

    /**
     * @param dataMatricula the dataMatricula to set
     */
    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    /**
     * @return the descontoProgressivoPrimeiraParcela
     */
    public DescontoProgressivoVO getDescontoProgressivoPrimeiraParcela() {
        if (descontoProgressivoPrimeiraParcela == null) {
            descontoProgressivoPrimeiraParcela = new DescontoProgressivoVO();
        }
        return descontoProgressivoPrimeiraParcela;
    }

    /**
     * @param descontoProgressivoPrimeiraParcela the descontoProgressivoPrimeiraParcela to set
     */
    public void setDescontoProgressivoPrimeiraParcela(DescontoProgressivoVO descontoProgressivoPrimeiraParcela) {
        this.descontoProgressivoPrimeiraParcela = descontoProgressivoPrimeiraParcela;
    }

    /**
     * @return the consultor
     */
    public FuncionarioVO getConsultor() {
        if (consultor == null) {
            consultor = new FuncionarioVO();
        }
        return consultor;
    }

    /**
     * @param consultor the consultor to set
     */
    public void setConsultor(FuncionarioVO consultor) {
        this.consultor = consultor;
    }

    /**
     * @return the processarApenasImpressaoContrato
     */
    public boolean isProcessarApenasImpressaoContrato() {
        return processarApenasImpressaoContrato;
    }

    public boolean getProcessarApenasImpressaoContrato() {
        return processarApenasImpressaoContrato;
    }

    /**
     * @param processarApenasImpressaoContrato the processarApenasImpressaoContrato to set
     */
    public void setProcessarApenasImpressaoContrato(boolean processarApenasImpressaoContrato) {
        this.processarApenasImpressaoContrato = processarApenasImpressaoContrato;
    }

    /**
     * @return the tipoMatricula
     */
    public String getTipoMatricula() {
        if (tipoMatricula == null) {
            tipoMatricula = "";
        }
        return tipoMatricula;
    }

    /**
     * @param tipoMatricula the tipoMatricula to set
     */
    public void setTipoMatricula(String tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }
    
    public MatriculaCRMVO getMatriculaCRMVO(IntegracaoMatriculaCRMVO intMat) {
    	MatriculaCRMVO matCRM = new MatriculaCRMVO();
    	matCRM.setTipoMatricula(intMat.getTipoMatricula());
     	matCRM.setPermitiMatricula4Modulo(intMat.getPermitiMatricula4Modulo());
		matCRM.getUnidadeEnsino().setCodigo(intMat.getCodigoUnidade());
    	matCRM.getUnidadeEnsino().setNome(intMat.getNomeUnidade());
    	matCRM.getTurma().setCodigo(intMat.getCodigoTurma());
    	matCRM.getTurma().setIdentificadorTurma(intMat.getNomeTurma());
    	matCRM.getConsultor().setCodigo(intMat.getCodigoConsultor());
    	matCRM.getProcessoMatricula().setCodigo(intMat.getCodigoProcessoMatricula());
    	matCRM.getProcessoMatricula().setDescricao(intMat.getDescricaoProcessoMatricula());
    	matCRM.getPlanoFinanceiroCurso().setCodigo(intMat.getCodigoPlanoFinanceiroCurso());
    	matCRM.getPlanoFinanceiroCurso().setDescricao(intMat.getDescricaoPlanoFinanceiroCurso());
    	matCRM.getPlanoDesconto().setCodigo(intMat.getCodigoPlanoDesconto());
    	matCRM.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(intMat.getCodigoCondicaoPagamentoPlanoFinanceiroCurso());
    	matCRM.getCondicaoPagamentoPlanoFinanceiroCurso().setDescricao(intMat.getDescricaoCondicaoPagamentoPlanoFinanceiroCurso());
    	Iterator i = intMat.getDisciplinaMatriculaVOs().iterator();
    	while (i.hasNext()) {
    		DisciplinaMatriculaCRMVO d = new DisciplinaMatriculaCRMVO();
    		IntegracaoDisciplinaMatriculaVO intDisc = (IntegracaoDisciplinaMatriculaVO)i.next();
    		d.getDisciplinaVO().setCodigo(intDisc.getDisciplina());
    		matCRM.getDisciplinaMatriculaCRM().add(d);
    	}
    	matCRM.getDisciplinaMatriculaCRM();
    	try {
    		String dataMatricula = intMat.getDataMatricula();
    		if (dataMatricula.indexOf("T") != -1) {
    			dataMatricula = dataMatricula.substring(0, dataMatricula.indexOf("T"));	
    			matCRM.setDataMatricula(Uteis.getDataYYYMMDD(dataMatricula));    			
    		}
		} catch (Exception e) {
    		matCRM.setDataMatricula(new Date());
    	}
    	return matCRM;
    }
	
	public Boolean getPermitiMatricula4Modulo() {
		if (permitiMatricula4Modulo == null) {
			permitiMatricula4Modulo = Boolean.FALSE;
		}
		return permitiMatricula4Modulo;
	}

	public void setPermitiMatricula4Modulo(Boolean permitiMatricula4Modulo) {
		this.permitiMatricula4Modulo = permitiMatricula4Modulo;
	}

	//Nao inicializar esse atributo com true e nem false pois o valor null e usado em regras no sistema pedro andrade.
	public Boolean getPermitiMatriculaInadipliente() {
		return permitiMatriculaInadipliente;
	}

	public void setPermitiMatriculaInadipliente(Boolean permitiMatriculaInadipliente) {
		this.permitiMatriculaInadipliente = permitiMatriculaInadipliente;
	}

	public String getDiretorioContratoPdf() {
		if (diretorioContratoPdf == null) {
			diretorioContratoPdf = "";
		}
		return diretorioContratoPdf;
	}

	public void setDiretorioContratoPdf(String diretorioContratoPdf) {
		this.diretorioContratoPdf = diretorioContratoPdf;
	}

	//Nao pode ser incicializado com new date Pedro Andrade
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	//Nao pode ser incicializado com new date Pedro Andrade
	public Date getDataBaseGeracaoParcela() {
		return dataBaseGeracaoParcela;
	}

	public void setDataBaseGeracaoParcela(Date dataBaseGeracaoParcela) {
		this.dataBaseGeracaoParcela = dataBaseGeracaoParcela;
	}

	public List<PlanoDescontoVO> getListaPlanoDescontoVO() {
		if(listaPlanoDescontoVO == null) {
			listaPlanoDescontoVO = new ArrayList<>();
		}
		return listaPlanoDescontoVO;
	}

	public void setListaPlanoDescontoVO(List<PlanoDescontoVO> listaPlanoDescontoVO) {
		this.listaPlanoDescontoVO = listaPlanoDescontoVO;
	}	

	public Integer getCodigoContrato() {
		if(codigoContrato == null) {
			codigoContrato = 0;
		}
		return codigoContrato;
	}

	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	
	
	
	
	
	

}