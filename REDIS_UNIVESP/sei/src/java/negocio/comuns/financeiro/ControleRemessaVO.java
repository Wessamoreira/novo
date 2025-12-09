package negocio.comuns.financeiro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.utilitarias.Uteis;

public class ControleRemessaVO extends SuperVO {

    private Integer codigo;
    private Date dataGeracao;
    private Date dataInicio;
    private Date dataFim;
    private UsuarioVO responsavel;
    private ArquivoVO arquivoRemessa;
    private ArquivoVO arquivoRetornoRemessa;
    private byte[] arquivo;    
    private String nomeArquivo;    
    private ByteArrayOutputStream out;
    private FileInputStream fileInputStream;    
    private BancoVO bancoVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private ContaCorrenteVO contaCorrenteVO;
    private Integer numeroIncremental;
    private Integer numeroIncrementalTitulos;
    private Double valorTotalRemessa;
    private String tipoRemessa;	
    private SituacaoControleRemessaEnum situacaoControleRemessa;
    private Boolean priorizarRespFin;
    private RegistroHeaderVO registroHeaderRetornoVO;
    private RegistroTrailerVO registroTrailerRetornoVO;
	private List<RegistroDetalheVO> registroDetalheRetornoVOs;
	private Boolean remessaOnline;

	//Usado pelo banco do bradesco(codigo MX)
    private Integer incrementalMX;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDataGeracao_Apresentar() {
        return Uteis.getData(getDataGeracao());
    }

    public Date getDataGeracao() {
        if (dataGeracao == null) {
            dataGeracao = new Date();
        }
        return dataGeracao;
    }

    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
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

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return dataInicio;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
        return dataFim;
    }

    public void setArquivoRemessa(ArquivoVO arquivoRemessa) {
        this.arquivoRemessa = arquivoRemessa;
    }

    public ArquivoVO getArquivoRemessa() {
        if (arquivoRemessa == null) {
            arquivoRemessa = new ArquivoVO();
        }
        return arquivoRemessa;
    }

    public BancoVO getBancoVO() {
        if (bancoVO == null) {
            bancoVO = new BancoVO();
        }
        return bancoVO;
    }

    public void setBancoVO(BancoVO bancoVO) {
        this.bancoVO = bancoVO;
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

    public ContaCorrenteVO getContaCorrenteVO() {
        if (contaCorrenteVO == null) {
            contaCorrenteVO = new ContaCorrenteVO();
        }
        return contaCorrenteVO;
    }

    public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
        this.contaCorrenteVO = contaCorrenteVO;
    }

    public Integer getNumeroIncremental() {
        if (numeroIncremental == null) {
            numeroIncremental = 1;
        }
        return numeroIncremental;
    }

    public void setNumeroIncremental(Integer numeroIncremental) {
        this.numeroIncremental = numeroIncremental;
    }

    public Double getValorTotalRemessa() {
		if (valorTotalRemessa == null) {
			valorTotalRemessa = 0.0;
		}
		return valorTotalRemessa;
	}

	public void setValorTotalRemessa(Double valorTotalRemessa) {
		this.valorTotalRemessa = valorTotalRemessa;
	}

	public Integer getNumeroIncrementalTitulos() {
		if (numeroIncrementalTitulos == null) {
			numeroIncrementalTitulos = 0;
		}
		return numeroIncrementalTitulos;
	}

	public void setNumeroIncrementalTitulos(Integer numeroIncrementalTitulos) {
		this.numeroIncrementalTitulos = numeroIncrementalTitulos;
	}

	public Integer getIncrementalMX() {
		if (incrementalMX == null) {
			incrementalMX = 0;
		}
		return incrementalMX;
	}

	public void setIncrementalMX(Integer incrementalMX) {
		this.incrementalMX = incrementalMX;
	}

	public Boolean getPriorizarRespFin() {
		if (priorizarRespFin == null) {
			priorizarRespFin = Boolean.FALSE;
		}
		return priorizarRespFin;
	}

	public void setPriorizarRespFin(Boolean priorizarRespFin) {
		this.priorizarRespFin = priorizarRespFin;
	}

	public String getTipoRemessa_Apresentar() {
		if (getTipoRemessa().equals("RE")) {
			return "Registro de Conta";
		} else {
			return "Atualização de Conta";
		}	
	}

	public String getTipoRemessa() {
		if (tipoRemessa == null) {
			tipoRemessa = "RE";
		}
		return tipoRemessa;
	}

	public void setTipoRemessa(String tipoRemessa) {
		this.tipoRemessa = tipoRemessa;
	}

	public SituacaoControleRemessaEnum getSituacaoControleRemessa() {
		if (situacaoControleRemessa == null) {
			situacaoControleRemessa = SituacaoControleRemessaEnum.TODOS;
		}
		return situacaoControleRemessa;
	}

	public void setSituacaoControleRemessa(SituacaoControleRemessaEnum situacaoControleRemessa) {
		this.situacaoControleRemessa = situacaoControleRemessa;
	}

	public ArquivoVO getArquivoRetornoRemessa() {
		if (arquivoRetornoRemessa == null) {
			arquivoRetornoRemessa = new ArquivoVO();
		}
		return arquivoRetornoRemessa;
	}

	public void setArquivoRetornoRemessa(ArquivoVO arquivoRetornoRemessa) {
		this.arquivoRetornoRemessa = arquivoRetornoRemessa;
	}

    public File getArquivo(String caminho) throws Exception {
    	File arquivo = new File(caminho);
        if (getArquivo() != null) {
        	FileOutputStream fileOutputStream = new FileOutputStream(caminho);
            fileOutputStream.write(getArquivo());
            fileOutputStream.close();
        }
        
        return arquivo;
    }

    public void setArquivo(File arquivo) throws Exception {
        try {
            byte buffer[] = new byte[4096];
            int bytesRead = 0;
            out = new ByteArrayOutputStream();
            fileInputStream = new FileInputStream(arquivo.getAbsolutePath());
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            setArquivo(out.toByteArray());
        } finally {
            out.close();
            fileInputStream.close();
        }
    }	
    
    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public String getNomeArquivo_Apresentar() {
        return Uteis.getNomeArquivo(getNomeArquivo());
    }

    public String getNomeArquivo() {
    	if (nomeArquivo == null) {
    		nomeArquivo = "";
    	}
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    public RegistroHeaderVO getRegistroHeaderRetornoVO() {
		if (registroHeaderRetornoVO == null) {
			registroHeaderRetornoVO = new RegistroHeaderVO();
		}
		return registroHeaderRetornoVO;
	}

	public void setRegistroHeaderRetornoVO(RegistroHeaderVO registroHeaderRetornoVO) {
		this.registroHeaderRetornoVO = registroHeaderRetornoVO;
	}
    
	public List<RegistroDetalheVO> getRegistroDetalheRetornoVOs() {
		if (registroDetalheRetornoVOs == null) {
			registroDetalheRetornoVOs = new ArrayList<RegistroDetalheVO>();
		}
		return registroDetalheRetornoVOs;
	}

	public void setRegistroDetalheRetornoVOs(List<RegistroDetalheVO> registroDetalheRetornoVOs) {
		this.registroDetalheRetornoVOs = registroDetalheRetornoVOs;
	}

    public RegistroTrailerVO getRegistroTrailerRetornoVO() {
		if (registroTrailerRetornoVO == null) {
			registroTrailerRetornoVO = new RegistroTrailerVO();
		}
		return registroTrailerRetornoVO;
	}

	public void setRegistroTrailerRetornoVO(RegistroTrailerVO registroTrailerRetornoVO) {
		this.registroTrailerRetornoVO = registroTrailerRetornoVO;
	}

	public Boolean getRemessaOnline() {
		if (remessaOnline == null) {
			remessaOnline = Boolean.FALSE;
		}
		return remessaOnline;
	}

	public void setRemessaOnline(Boolean remessaOnline) {
		this.remessaOnline = remessaOnline;
	}
    
}