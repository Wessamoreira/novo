package negocio.comuns.financeiro;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TagModeloBoletoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ModeloBoleto. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ModeloBoletoVO extends SuperVO {

    protected Integer codigo;
    protected String nome;
    private Boolean apenasDescontoInstrucaoBoleto;
    private String observacoesGerais1;
    private String observacoesGerais2;
    private byte[] imagem;
    protected String localPagamento;
    private String extensaoImagem;
    private Boolean utilizarDescricaoDescontoPersonalizado;
    private String textoTopo;
    private String instrucao1;
    private String instrucao2;
    private String instrucao3;
    private String instrucao4;
    private String instrucao5;
    private String instrucao6;
    private String textoRodape;
    private String textoTopoInferior;
    private String instrucao1Inferior;
    private String instrucao2Inferior;
    private String instrucao3Inferior;
    private String instrucao4Inferior;
    private String instrucao5Inferior;
    private String instrucao6Inferior;
    private String textoRodapeInferior;	
    private Boolean ocultarCodBarraLinhaDigitavel;		
    public static final int ALTURA_IMAGEM = 180;
    public static final int LARGURA_IMAGEM = 600;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ModeloBoleto</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ModeloBoletoVO() {
        super();
//	inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ModeloBoletoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ModeloBoletoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (!Uteis.isAtributoPreenchido(obj.getNome())) {
            throw new ConsistirException("O campo NOME (MODELO DE BOLETO) deve ser informado.");
        }
        if (!Uteis.isAtributoPreenchido(obj.getLocalPagamento())) {
            throw new ConsistirException("O campo LOCAL DE PAGAMENTO (MODELO DE BOLETO) deve ser informado.");
        }
    }

    public void uploadImagem(FileUploadEvent event) throws Exception {
    	UploadedFile uploadedFile = event.getUploadedFile();
    	
//        UploadItem uploadItem = event.getUploadItem();
//        File arquivo = uploadItem.getFile();
       
        
        BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
//        BufferedImage img = ImageIO.read(arquivo);
        
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        FileInputStream fi = new FileInputStream(arquivo.getAbsolutePath());
        
//        FileInputStream fi = new FileInputStream(arquivo.getAbsolutePath());
//        byte buffer[] = new byte[uploadedFile.getData().length];
        try {
            if (img.getHeight() > ALTURA_IMAGEM || img.getWidth() > LARGURA_IMAGEM) {
                throw new ConsistirException("A resolução da imagem (" + img.getWidth() + "x" + img.getHeight()
                        + ") é muito grande, " + "Tamanho máximo permitido (" + LARGURA_IMAGEM + "x" + ALTURA_IMAGEM
                        + ") . ");
            }
//            int bytesRead = 0;
//            while ((bytesRead = fi.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
            setImagem(uploadedFile.getData());
//            setExtensaoImagem(Uteis.getExtensaoDeArquivo(uploadItem.getFileName()));
//            setExtensaoImagem(Uteis.getExtensaoDeArquivo(uploadedFile.getName()));
            setExtensaoImagem(uploadedFile.getFileExtension());
//            os.close();
//            fi.close();
        } catch (Exception e) {
            throw e;
        } finally {
//            os.close();
//            fi.close();
//            uploadItem = null;
//            arquivo = null;
//            img = null;
//            os = null;
//            fi = null;
//            buffer = null;
        }

    }

    public Boolean getExisteImagem() {
        if (getImagem() == null) {
            return false;
        }
        return true;
    }

    public InputStream getImagemInputStream() throws Exception {
        if (getImagem() != null) {
            return new ByteArrayInputStream(getImagem());
        }
        return new ByteArrayInputStream(new byte[1]);
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setNome(getNome().toUpperCase());
    }

    public String getLocalPagamento() {
        if (localPagamento == null) {
            localPagamento = "";
        }
        return (localPagamento);
    }

    public void setLocalPagamento(String localPagamento) {
        this.localPagamento = localPagamento;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getObservacoesGerais1() {
        if (observacoesGerais1 == null) {
            observacoesGerais1 = "";
        }
        return observacoesGerais1;
    }

    public void setObservacoesGerais1(String observacoesGerais1) {
        this.observacoesGerais1 = observacoesGerais1;
    }

    public String getObservacoesGerais2() {
        if (observacoesGerais2 == null) {
            observacoesGerais2 = "";
        }
        return observacoesGerais2;
    }

    public void setObservacoesGerais2(String observacoesGerais2) {
        this.observacoesGerais2 = observacoesGerais2;
    }

    /**
     * @return the extensaoImagem
     */
    public String getExtensaoImagem() {
        if (extensaoImagem == null) {
            extensaoImagem = "";
        }
        return extensaoImagem;
    }

    /**
     * @param extensaoImagem
     *            the extensaoImagem to set
     */
    public void setExtensaoImagem(String extensaoImagem) {
        this.extensaoImagem = extensaoImagem;
    }

    /**
     * @return the apenasDescontoInstrucaoBoleto
     */
    public Boolean getApenasDescontoInstrucaoBoleto() {
        if (apenasDescontoInstrucaoBoleto == null) {
            apenasDescontoInstrucaoBoleto = Boolean.FALSE;
        }
        return apenasDescontoInstrucaoBoleto;
    }

    /**
     * @param apenasDescontoInstrucaoBoleto the apenasDescontoInstrucaoBoleto to set
     */
    public void setApenasDescontoInstrucaoBoleto(Boolean apenasDescontoInstrucaoBoleto) {
        this.apenasDescontoInstrucaoBoleto = apenasDescontoInstrucaoBoleto;
    }
	

	public Boolean getUtilizarDescricaoDescontoPersonalizado() {
		if (utilizarDescricaoDescontoPersonalizado == null) {
			utilizarDescricaoDescontoPersonalizado = Boolean.FALSE;
		}
		return utilizarDescricaoDescontoPersonalizado;
	}

	public void setUtilizarDescricaoDescontoPersonalizado(Boolean utilizarDescricaoDescontoPersonalizado) {
		this.utilizarDescricaoDescontoPersonalizado = utilizarDescricaoDescontoPersonalizado;
	}

	public String getInstrucao1() {
		if (instrucao1 == null) {
			instrucao1 = "";
		}
		return instrucao1;
	}

	public void setInstrucao1(String instrucao1) {
		this.instrucao1 = instrucao1;
	}

	public String getInstrucao2() {
		if (instrucao2 == null) {
			instrucao2 = "";
		}
		return instrucao2;
	}

	public void setInstrucao2(String instrucao2) {
		this.instrucao2 = instrucao2;
	}

	public String getInstrucao3() {
		if (instrucao3 == null) {
			instrucao3 = "";
		}
		return instrucao3;
	}

	public void setInstrucao3(String instrucao3) {
		this.instrucao3 = instrucao3;
	}

	public String getInstrucao4() {
		if (instrucao4 == null) {
			instrucao4 = "";
		}
		return instrucao4;
	}

	public void setInstrucao4(String instrucao4) {
		this.instrucao4 = instrucao4;
	}

	public String getInstrucao5() {
		if (instrucao5 == null) {
			instrucao5 = "";
		}
		return instrucao5;
	}

	public void setInstrucao5(String instrucao5) {
		this.instrucao5 = instrucao5;
	}

	public String getInstrucao6() {
		if (instrucao6 == null) {
			instrucao6 = "";
		}
		return instrucao6;
	}

	public void setInstrucao6(String instrucao6) {
		this.instrucao6 = instrucao6;
	}

	public String getTextoRodape() {
		if (textoRodape == null) {
			textoRodape = "JUROS E MULTA";
		}
		return textoRodape;
	}

	public void setTextoRodape(String textoRodape) {
		this.textoRodape = textoRodape;
	}

	public String getTextoTopo() {
		if (textoTopo == null) {
			textoTopo = "INSTRUÇÕES PARA PAGAMENTO";
		}
		return textoTopo;
	}

	public void setTextoTopo(String textoTopo) {
		this.textoTopo = textoTopo;
	}
	
	public String getANTES_ATE_DEPOIS() {
		return TagModeloBoletoEnum.ANTES_ATE_DEPOIS.getDescricao();
	}

	public String getDESCONTO_ALUNO() {
		return TagModeloBoletoEnum.DESCONTO_ALUNO.getDescricao();
	}
	
	public String getDESCONTO_CONVENIO() {
		return TagModeloBoletoEnum.DESCONTO_CONVENIO.getDescricao();
	}
	
	public String getDESCONTO_RATEIO() {
		return TagModeloBoletoEnum.DESCONTO_RATEIO.getDescricao();
	}	

	public String getDESCONTO_INSTITUCIONAL() {
		return TagModeloBoletoEnum.DESCONTO_INSTITUCIONAL.getDescricao();
	}
	
	public String getDESCONTO_PROGRESSIVO() {
		return TagModeloBoletoEnum.DESCONTO_PROGRESSIVO.getDescricao();
	}
	
	public String getJURO_PORCETAGEM() {
		return TagModeloBoletoEnum.JURO_PORCETAGEM.getDescricao();
	}
	
	public String getJURO_VALOR() {
		return TagModeloBoletoEnum.JURO_VALOR.getDescricao();
	}
	
	public String getMULTA_PORCETAGEM() {
		return TagModeloBoletoEnum.MULTA_PORCETAGEM.getDescricao();
	}
	
	public String getMULTA_VALOR() {
		return TagModeloBoletoEnum.MULTA_VALOR.getDescricao();
	}
	
	public String getTOTAL_DESCONTOS() {
		return TagModeloBoletoEnum.TOTAL_DESCONTOS.getDescricao();
	}
	
	public String getVALOR_TOTAL() {
		return TagModeloBoletoEnum.VALOR_TOTAL.getDescricao();
	}	
	
	public String getCODIGO_ADMINISTRATIVO() {
		return TagModeloBoletoEnum.CODIGO_ADMINISTRATIVO.getDescricao();
	}
	
	
	public String substituirTags(String texto, String antes_ate_depois, String valorTotalDesconto,  
			String percDescontoProgressivo,	String valorDescontoProgressivo,
			String valorDescontoAluno, String valorDescontoInstituicao, 
			String valorDescontoConvenio, String valorTotal, String codigoAdministrativo, String valorDescontoRateio) {		
			if (texto.contains(getANTES_ATE_DEPOIS()) || texto.contains(getVALOR_TOTAL()) || texto.contains(getTOTAL_DESCONTOS())
					|| texto.contains(getMULTA_VALOR()) || texto.contains(getMULTA_PORCETAGEM()) || texto.contains(getJURO_VALOR())
					|| texto.contains(getJURO_PORCETAGEM()) || texto.contains(getDESCONTO_PROGRESSIVO()) || texto.contains(getDESCONTO_INSTITUCIONAL())
					|| texto.contains(getDESCONTO_CONVENIO()) || texto.contains(getDESCONTO_ALUNO()) || texto.contains(getANTES_ATE_DEPOIS())) {
				valorTotal = valorTotal.replace("R$", "R&#36;");
				valorTotalDesconto = valorTotalDesconto.replace("R$", "R&#36;");
				valorDescontoConvenio = valorDescontoConvenio.replace("R$", "R&#36;");
				valorDescontoAluno = valorDescontoAluno.replace("R$", "R&#36;");
				valorDescontoInstituicao = valorDescontoInstituicao.replace("R$", "R&#36;");
				valorDescontoConvenio = valorDescontoConvenio.replace("R$", "R&#36;");
				valorDescontoProgressivo = valorDescontoProgressivo.replace("R$", "R&#36;");
				valorDescontoRateio = valorDescontoRateio.replace("R$", "R&#36;");
				texto = texto.replaceAll(getVALOR_TOTAL(), valorTotal);
				texto = texto.replaceAll(getTOTAL_DESCONTOS(), valorTotalDesconto);
				texto = texto.replaceAll(getANTES_ATE_DEPOIS(), antes_ate_depois);
				texto = texto.replaceAll(getDESCONTO_CONVENIO(), valorDescontoConvenio);
				texto = texto.replaceAll(getDESCONTO_ALUNO(), valorDescontoAluno);
				texto = texto.replaceAll(getDESCONTO_INSTITUCIONAL(), valorDescontoInstituicao);
				texto = texto.replaceAll(getDESCONTO_PROGRESSIVO(), valorDescontoProgressivo);
				texto = texto.replaceAll(getDESCONTO_CONVENIO(), valorDescontoConvenio);
				texto = texto.replaceAll(getCODIGO_ADMINISTRATIVO(), codigoAdministrativo);			
				texto = texto.replaceAll(getDESCONTO_RATEIO(), valorDescontoRateio);						
			}
			return texto;
		}

	public String getTextoTopoInferior() {
		if (textoTopoInferior == null) {
			textoTopoInferior = "INSTRUÇÕES PARA PAGAMENTO";
		}
		return textoTopoInferior;
	}

	public void setTextoTopoInferior(String textoTopoInferior) {
		this.textoTopoInferior = textoTopoInferior;
	}

	public String getInstrucao1Inferior() {
		if (instrucao1Inferior == null) {
			instrucao1Inferior = "";
		}
		return instrucao1Inferior;
	}

	public void setInstrucao1Inferior(String instrucao1Inferior) {
		this.instrucao1Inferior = instrucao1Inferior;
	}

	public String getInstrucao2Inferior() {
		if (instrucao2Inferior == null) {
			instrucao2Inferior = "";
		}
		return instrucao2Inferior;
	}

	public void setInstrucao2Inferior(String instrucao2Inferior) {
		this.instrucao2Inferior = instrucao2Inferior;
	}

	public String getInstrucao3Inferior() {
		if (instrucao3Inferior == null) {
			instrucao3Inferior = "";
		}
		return instrucao3Inferior;
	}

	public void setInstrucao3Inferior(String instrucao3Inferior) {
		this.instrucao3Inferior = instrucao3Inferior;
	}

	public String getInstrucao4Inferior() {
		if (instrucao4Inferior == null) {
			instrucao4Inferior = "";
		}
		return instrucao4Inferior;
	}

	public void setInstrucao4Inferior(String instrucao4Inferior) {
		this.instrucao4Inferior = instrucao4Inferior;
	}

	public String getInstrucao5Inferior() {
		if (instrucao5Inferior == null) {
			instrucao5Inferior = "";
		}
		return instrucao5Inferior;
	}

	public void setInstrucao5Inferior(String instrucao5Inferior) {
		this.instrucao5Inferior = instrucao5Inferior;
	}

	public String getInstrucao6Inferior() {
		if (instrucao6Inferior == null) {
			instrucao6Inferior = "";
		}
		return instrucao6Inferior;
	}

	public void setInstrucao6Inferior(String instrucao6Inferior) {
		this.instrucao6Inferior = instrucao6Inferior;
	}

	public String getTextoRodapeInferior() {
		if (textoRodapeInferior == null) {
			textoRodapeInferior = "JUROS E MULTA";
		}
		return textoRodapeInferior;
	}

	public void setTextoRodapeInferior(String textoRodapeInferior) {
		this.textoRodapeInferior = textoRodapeInferior;
	}	

	public Boolean getOcultarCodBarraLinhaDigitavel() {
		if (ocultarCodBarraLinhaDigitavel == null) {
			ocultarCodBarraLinhaDigitavel = Boolean.FALSE;
		}
		return ocultarCodBarraLinhaDigitavel;
	}

	public void setOcultarCodBarraLinhaDigitavel(Boolean ocultarCodBarraLinhaDigitavel) {
		this.ocultarCodBarraLinhaDigitavel = ocultarCodBarraLinhaDigitavel;
	}
}
