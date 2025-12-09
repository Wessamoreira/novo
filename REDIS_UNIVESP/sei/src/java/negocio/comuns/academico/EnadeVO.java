package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Enade. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EnadeVO extends SuperVO {

	private Integer codigo;
	private String tituloEnade;
	private String portariaNormativa;
	private Date dataPortaria;
	private Date dataPublicacaoPortariaDOU;
	private Date dataProva;
	// private String textoDispensaEnade;
	// private String textoEnadeFeito;
	private List<TextoEnadeVO> textoEnadeVOs;
	private List<EnadeCursoVO> enadeCursoVOs;
	private String codigoProjeto;
	private List<EnadeVO> enadeVOs;
	private String nomeArquivoIngressante;
	private String nomeArquivoConcluinte;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Enade</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public EnadeVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>EnadeVO</code>. Todos os tipos de consistência de dados são e devem
	 * ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(EnadeVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getTituloEnade().equals("")) {
			throw new ConsistirException("O campo TÍTULO ENADE (Enade) deve ser informado.");
		}
		if (obj.getDataPublicacaoPortariaDOU() == null) {
			throw new ConsistirException("O campo DATA PUBLICAÇÃO NO DIÁRIO OFICIAL (Enade) deve ser informado.");
		}
		if (obj.getTextoEnadeVOs().isEmpty()) {
			throw new ConsistirException("O campo TEXTO ENADE (Enade) deve ser informado.");
		}
		if (obj.getDataProva() == null) {
			throw new ConsistirException("O campo DATA PROVA (Enade) deve ser informado.");
		}
		if (obj.getEnadeCursoVOs().isEmpty()) {
			throw new ConsistirException("Deve ser informado ao menos um CURSO.");
		}
//		if (obj.getCodigoProjeto().isEmpty()) {
//			throw new ConsistirException("Deve ser informado o Código do Projeto.");
//		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setTituloEnade(getTituloEnade().toUpperCase());
		// setTextoDispensaEnade(getTextoDispensaEnade().toUpperCase());
		// setTextoEnadeFeito(getTextoEnadeFeito().toUpperCase());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(null);
		setTituloEnade("");
		setPortariaNormativa("");
		setDataPortaria(new Date());
		setDataPublicacaoPortariaDOU(new Date());
		// setTextoDispensaEnade("");
		// setTextoEnadeFeito("");
	}

	// public String getTextoEnadeFeito() {
	// if (textoEnadeFeito == null) {
	// textoEnadeFeito = "";
	// }
	// return (textoEnadeFeito);
	// }
	//
	// public void setTextoEnadeFeito(String textoEnadeFeito) {
	// this.textoEnadeFeito = textoEnadeFeito;
	// }
	//
	// public String getTextoDispensaEnade() {
	// if (textoDispensaEnade == null) {
	// textoDispensaEnade = "";
	// }
	// return (textoDispensaEnade);
	// }
	//
	// public void setTextoDispensaEnade(String textoDispensaEnade) {
	// this.textoDispensaEnade = textoDispensaEnade;
	// }

	public Date getDataPublicacaoPortariaDOU() {
		if (dataPublicacaoPortariaDOU == null) {
			dataPublicacaoPortariaDOU = new Date();
		}
		return (dataPublicacaoPortariaDOU);
	}

	public Date getDataProva() {
		return dataProva;
	}

	public void setDataProva(Date dataProva) {
		this.dataProva = dataProva;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataProva_Apresentar() {
		return (Uteis.getData(dataProva));
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataPublicacaoPortariaDOU_Apresentar() {
		return (Uteis.getData(dataPublicacaoPortariaDOU));
	}

	public void setDataPublicacaoPortariaDOU(Date dataPublicacaoPortariaDOU) {
		this.dataPublicacaoPortariaDOU = dataPublicacaoPortariaDOU;
	}

	public Date getDataPortaria() {
		return (dataPortaria);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataPortaria_Apresentar() {
		return (Uteis.getData(dataPortaria));
	}

	public void setDataPortaria(Date dataPortaria) {
		this.dataPortaria = dataPortaria;
	}

	public String getTituloEnade() {
		if (tituloEnade == null) {
			tituloEnade = "";
		}
		return (tituloEnade);
	}

	public void setTituloEnade(String tituloEnade) {
		this.tituloEnade = tituloEnade;
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

	public List<TextoEnadeVO> getTextoEnadeVOs() {
		if (textoEnadeVOs == null) {
			textoEnadeVOs = new ArrayList<TextoEnadeVO>(0);
		}
		return textoEnadeVOs;
	}

	public void setTextoEnadeVOs(List<TextoEnadeVO> textoEnadeVOs) {
		this.textoEnadeVOs = textoEnadeVOs;
	}

	public List<EnadeCursoVO> getEnadeCursoVOs() {
		if (enadeCursoVOs == null) {
			enadeCursoVOs = new ArrayList<EnadeCursoVO>(0);
		}
		return enadeCursoVOs;
	}

	public void setEnadeCursoVOs(List<EnadeCursoVO> enadeCursoVOs) {
		this.enadeCursoVOs = enadeCursoVOs;
	}

	public String getPortariaNormativa() {
		if (portariaNormativa == null) {
			portariaNormativa = "";
		}
		return portariaNormativa;
	}

	public void setPortariaNormativa(String portariaNormativa) {
		this.portariaNormativa = portariaNormativa;
	}

	public String getCodigoProjeto() {
		if (codigoProjeto == null) {
			codigoProjeto = "";
		}
		return codigoProjeto;
	}

	public void setCodigoProjeto(String codigoProjeto) {
		this.codigoProjeto = codigoProjeto;
	}
	
	 public List<EnadeVO> getEnadeVOs() {
	        if (enadeVOs == null) {
	        	enadeVOs = new ArrayList<>();
	        }
	        return enadeVOs;
	    }
	    public void setEnadeVOs(List enadeVOs) {
	        this.enadeVOs = enadeVOs;
	    }

		public String getNomeArquivoIngressante() {
			if (nomeArquivoIngressante == null) {
				nomeArquivoIngressante = "";
			}
			return nomeArquivoIngressante;
		}

		public void setNomeArquivoIngressante(String nomeArquivoIngressante) {
			this.nomeArquivoIngressante = nomeArquivoIngressante;
		}

		public String getNomeArquivoConcluinte() {
			if (nomeArquivoConcluinte == null) {
				nomeArquivoConcluinte = "";
			}
			return nomeArquivoConcluinte;
		}

		public void setNomeArquivoConcluinte(String nomeArquivoConcluinte) {
			this.nomeArquivoConcluinte = nomeArquivoConcluinte;
		}
	    

}
