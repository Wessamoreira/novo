package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class ControleLivroFolhaReciboVO extends SuperVO {

    private Integer codigo;
    private Integer folhaReciboAtual;
    protected ControleLivroRegistroDiplomaVO controleLivroRegistroDiploma;
    protected MatriculaVO matricula;
    private List ControleLivroFolhaReciboVOs;
    private Integer numeroRegistro;
    private Date dataPublicacao;
    private String via;
    private UsuarioVO responsavel;
    private Date dataCadastro;
    private String situacao;
    private String avisoDebitoDocumentos = "";
    private ExpedicaoDiplomaVO expedicaoDiplomaVO;
    private Integer nrLivroDiplomaPrimeiraVia;
	private Integer nrFolhaReciboDiplomaPrimeiraVia;
	private Date dataPublicacaoDiplomaPrimeiraVia;
	private Integer nrLivroCertificadoPrimeiraVia;
	private Integer nrFolhaReciboCertificadoPrimeiraVia;
	private Date dataPublicacaoCertificadoPrimeiraVia;
    
    public static final long serialVersionUID = 1L;

    public ControleLivroFolhaReciboVO() {
        super();
        inicializarDados();
    }

    public static void validarDados(ControleLivroFolhaReciboVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getControleLivroRegistroDiploma() == null)
                || (obj.getControleLivroRegistroDiploma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTROLE LIVRO REGISTRO DIPLOMA (Controle Livro Folha Recibo) deve ser informado.");
        }
        if ((obj.getMatricula().getMatricula() == null)
                || (obj.getMatricula().getMatricula().equals(""))) {
            throw new ConsistirException("O campo MATRICULA (Controle Livro Folha Recibo) deve ser informado.");
        }
        if (obj.getFolhaReciboAtual().intValue() == 0) {
            throw new ConsistirException("O campo FOLHA RECIBO (Controle Livro Folha Recibo) deve ser informado.");
        }
    }

    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    public void excluirObjControleLivroReciboVOs(ControleLivroFolhaReciboVO obj) throws Exception {
        int index = 0;
        Iterator i = getControleLivroFolhaReciboVOs().iterator();
        while (i.hasNext()) {
            ControleLivroFolhaReciboVO objExistente = (ControleLivroFolhaReciboVO) i.next();            
            if (obj.getCodigo().equals(objExistente.getCodigo()) && objExistente.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula())) {
                getControleLivroFolhaReciboVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public List getControleLivroFolhaReciboVOs() {
        if (ControleLivroFolhaReciboVOs == null) {
            ControleLivroFolhaReciboVOs = new ArrayList();
        }
        return (ControleLivroFolhaReciboVOs);
    }

    public void setDocumentacaoControleLivroReciboVOs(List ControleLivroFolhaReciboVOs) {
        this.ControleLivroFolhaReciboVOs = ControleLivroFolhaReciboVOs;
    }

    public void inicializarDados() {
        setCodigo(0);
        setFolhaReciboAtual(0);
        setControleLivroRegistroDiploma(new ControleLivroRegistroDiplomaVO());
        setMatricula(new MatriculaVO());
    }

    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    public ControleLivroRegistroDiplomaVO getControleLivroRegistroDiploma() {
        if (controleLivroRegistroDiploma == null) {
            controleLivroRegistroDiploma = new ControleLivroRegistroDiplomaVO();
        }
        return (controleLivroRegistroDiploma);
    }

    public void setControleLivroRegistroDiploma(ControleLivroRegistroDiplomaVO obj) {
        this.controleLivroRegistroDiploma = obj;
    }

    public Integer getFolhaReciboAtual() {
        if (folhaReciboAtual == null) {
            folhaReciboAtual = 0;
        }
        return (folhaReciboAtual);
    }

    public void setFolhaReciboAtual(Integer folhaReciboAtual) {
        this.folhaReciboAtual = folhaReciboAtual;
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

	public Integer getNumeroRegistro() {
		if(numeroRegistro == null){
			numeroRegistro = 0;
		}
		return numeroRegistro;
	}

	public void setNumeroRegistro(Integer numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public Date getDataPublicacao() {		
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getVia() {
		if (via == null) {
			via = "";
		}
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public Long getDataCadastro_Time() {
		return getDataCadastro().getTime();
	}
    
	public String getDataCadastro_Apresentar() {
		return Uteis.getDataComHora(getDataCadastro());
	}

	public String getSituacao() {
		if(situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public boolean isSituacaoCancelado() {
		return getSituacao().equals("Cancelado");
	}
	
	public boolean isSituacaoPendente() {
		return getSituacao().equals("Pendente");
	}
	
	public boolean isSituacaoEmitido() {
		return getSituacao().equals("Emitido");
	}
	

	public String getAvisoDebitoDocumentos() {
		return avisoDebitoDocumentos;
	}

	public void setAvisoDebitoDocumentos(String avisoDebitoDocumentos) {
		this.avisoDebitoDocumentos = avisoDebitoDocumentos;
	}

	public ExpedicaoDiplomaVO getExpedicaoDiplomaVO() {
		if(expedicaoDiplomaVO == null) {
			expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
		}
		return expedicaoDiplomaVO;
	}

	public void setExpedicaoDiplomaVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		this.expedicaoDiplomaVO = expedicaoDiplomaVO;
	}
	
	public Integer getNrFolhaReciboDiplomaPrimeiraVia() {
		if (nrFolhaReciboDiplomaPrimeiraVia == null) {
			nrFolhaReciboDiplomaPrimeiraVia = new Integer(0);
		}
		return (nrFolhaReciboDiplomaPrimeiraVia);
	}

	public void setNrFolhaReciboDiplomaPrimeiraVia(Integer nrFolhaReciboDiplomaPrimeiraVia) {
		this.nrFolhaReciboDiplomaPrimeiraVia = nrFolhaReciboDiplomaPrimeiraVia;
	}

	public Integer getNrLivroDiplomaPrimeiraVia() {
		if (nrLivroDiplomaPrimeiraVia == null) {
			nrLivroDiplomaPrimeiraVia = new Integer(0);
		}
		return (nrLivroDiplomaPrimeiraVia);
	}

	public void setNrLivroDiplomaPrimeiraVia(Integer nrLivroDiplomaPrimeiraVia) {
		this.nrLivroDiplomaPrimeiraVia = nrLivroDiplomaPrimeiraVia;
	}
	
	public Date getDataPublicacaoDiplomaPrimeiraVia() {
		return dataPublicacaoDiplomaPrimeiraVia;
	}

	public void setDataPublicacaoDiplomaPrimeiraVia(Date dataPublicacaoDiplomaPrimeiraVia) {
		this.dataPublicacaoDiplomaPrimeiraVia = dataPublicacaoDiplomaPrimeiraVia;
	}
	
	
	
	public Integer getNrFolhaReciboCertificadoPrimeiraVia() {
		if (nrFolhaReciboCertificadoPrimeiraVia == null) {
			nrFolhaReciboCertificadoPrimeiraVia = new Integer(0);
		}
		return (nrFolhaReciboCertificadoPrimeiraVia);
	}

	public void setNrFolhaReciboCertificadoPrimeiraVia(Integer nrFolhaReciboCertificadoPrimeiraVia) {
		this.nrFolhaReciboCertificadoPrimeiraVia = nrFolhaReciboCertificadoPrimeiraVia;
	}

	public Integer getNrLivroCertificadoPrimeiraVia() {
		if (nrLivroCertificadoPrimeiraVia == null) {
			nrLivroCertificadoPrimeiraVia = new Integer(0);
		}
		return (nrLivroCertificadoPrimeiraVia);
	}

	public void setNrLivroCertificadoPrimeiraVia(Integer nrLivroCertificadoPrimeiraVia) {
		this.nrLivroCertificadoPrimeiraVia = nrLivroCertificadoPrimeiraVia;
	}
	
	public Date getDataPublicacaoCertificadoPrimeiraVia() {
		return dataPublicacaoCertificadoPrimeiraVia;
	}

	public void setDataPublicacaoCertificadoPrimeiraVia(Date dataPublicacaoCertificadoPrimeiraVia) {
		this.dataPublicacaoCertificadoPrimeiraVia = dataPublicacaoCertificadoPrimeiraVia;
	}
}
