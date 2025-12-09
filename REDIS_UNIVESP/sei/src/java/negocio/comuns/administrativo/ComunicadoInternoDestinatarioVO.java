package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.administrativo.ComunicacaoInterna;

/**
 * Reponsável por manter os dados da entidade ComunicadoInternoDestinatario.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ComunicacaoInterna
 */
public class ComunicadoInternoDestinatarioVO extends SuperVO {

    private Integer codigo;
    private Integer comunicadoInterno;
    private String tipoComunicadoInterno;
    private Boolean ciJaLida;
    private Boolean ciJaRespondida;
    private Boolean removerCaixaEntrada;
    private Date dataLeitura;
    private String nome;
    private String email;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO destinatario;
    private ParceiroVO destinatarioParceiro;
    private Boolean mensagemMarketingLida;
    private String emailInstitucional;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ComunicadoInternoDestinatario</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    public ComunicadoInternoDestinatarioVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ComunicadoInternoDestinatarioVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ComunicadoInternoDestinatarioVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTipoComunicadoInterno().equals("")) {
            throw new ConsistirException("O campo TIPO COMUNICADO INTERNO (Comunicado Interno Destinatário) deve ser informado.");
        }
        if ((obj.getDestinatario() == null) || (obj.getDestinatario().getCodigo().intValue() == 0 &&  obj.getDestinatario().getNome().equals("")) && !obj.getDestinatario().getNome().equals("Sistema") && !obj.getDestinatario().getNome().equals("Email Confirmação Envio Comunicado")) {
            throw new ConsistirException("O campo DESTINATÁRIO (Comunicado Interno Destinatário) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicadoInternoDestinatario</code>).
     */
    public PessoaVO getDestinatario() {
        if (destinatario == null) {
            destinatario = new PessoaVO();
        }
        return (destinatario);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ComunicadoInternoDestinatario</code>).
     */
    public void setDestinatario(PessoaVO obj) {
        this.destinatario = obj;
    }

    public Date getDataLeitura() {
        return (dataLeitura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataLeitura_Apresentar() {
        return (Uteis.getData(getDataLeitura()));
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public Boolean getCiJaRespondida() {
        if (ciJaRespondida == null) {
            ciJaRespondida = Boolean.FALSE;
        }
        return (ciJaRespondida);
    }

    public Boolean isCiJaRespondida() {
        if (ciJaRespondida == null) {
            ciJaRespondida = Boolean.FALSE;
        }
        return (ciJaRespondida);
    }

    public void setCiJaRespondida(Boolean ciJaRespondida) {
        this.ciJaRespondida = ciJaRespondida;
    }

    public Boolean getCiJaLida() {
        if (ciJaLida == null) {
            ciJaLida = Boolean.FALSE;
        }
        return (ciJaLida);
    }

    public Boolean isCiJaLida() {
        if (ciJaLida == null) {
            ciJaLida = Boolean.FALSE;
        }
        return (ciJaLida);
    }

    public void setCiJaLida(Boolean ciJaLida) {
        this.ciJaLida = ciJaLida;
    }

    public String getTipoComunicadoInterno_Apresentar() {
        if (getTipoComunicadoInterno().equals("RE")) {
            return "Exige Resposta";
        }
        if (getTipoComunicadoInterno().equals("MU")) {
            return "Mural";
        }
        if (getTipoComunicadoInterno().equals("LE")) {
            return "Somente Leitura";
        }
        return (getTipoComunicadoInterno());
    }

    public String getTipoComunicadoInterno() {
        if (tipoComunicadoInterno == null) {
            tipoComunicadoInterno = "";
        }
        return (tipoComunicadoInterno);
    }

    public void setTipoComunicadoInterno(String tipoComunicadoInterno) {
        this.tipoComunicadoInterno = tipoComunicadoInterno;
    }

    public Integer getComunicadoInterno() {
        if (comunicadoInterno == null) {
            comunicadoInterno = 0;
        }
        return (comunicadoInterno);
    }

    public void setComunicadoInterno(Integer comunicadoInterno) {
        this.comunicadoInterno = comunicadoInterno;
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

    public Boolean getRemoverCaixaEntrada() {
        if (removerCaixaEntrada == null) {
            removerCaixaEntrada = Boolean.FALSE;
        }
        return removerCaixaEntrada;
    }

    public void setRemoverCaixaEntrada(Boolean removerCaixaEntrada) {
        this.removerCaixaEntrada = removerCaixaEntrada;
    }

    /**
     * @return the mensagemMarketingLida
     */
    public Boolean getMensagemMarketingLida() {
        if (mensagemMarketingLida == null) {
            mensagemMarketingLida = Boolean.FALSE;
        }
        return mensagemMarketingLida;
    }

    /**
     * @param mensagemMarketingLida the mensagemMarketingLida to set
     */
    public void setMensagemMarketingLida(Boolean mensagemMarketingLida) {
        this.mensagemMarketingLida = mensagemMarketingLida;
    }

    public ParceiroVO getDestinatarioParceiro() {
        if (destinatarioParceiro == null) {
            destinatarioParceiro = new ParceiroVO();
        }
        return destinatarioParceiro;
    }

    public void setDestinatarioParceiro(ParceiroVO destinatarioParceiro) {
        if (destinatarioParceiro == null) {
            destinatarioParceiro = new ParceiroVO();
        }
        this.destinatarioParceiro = destinatarioParceiro;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return (email);
    }

    public void setEmail(String email) {
        this.email = email;
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

	public String getEmailInstitucional() {
		if (emailInstitucional == null) {
			emailInstitucional = "";
		}
		return emailInstitucional;
	}

	public void setEmailInstitucional(String emailInstitucional) {
		this.emailInstitucional = emailInstitucional;
	}
	
}
