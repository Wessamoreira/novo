package negocio.comuns.basico;

import java.io.Serializable;
import java.sql.Types;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ColunaDaoGenerico;
import negocio.comuns.arquitetura.annotation.EntidadeDaoGenerico;
import negocio.comuns.utilitarias.ConsistirException;
//import webservice.nfse.generic.CidadesWebServiceNFSEEnum;

/**
 * Reponsável por manter os dados da entidade Cidade. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "cidade")
@EntidadeDaoGenerico(tabela="cidade")
public class CidadeVO extends SuperVO implements Serializable {
	
	
	
	private Integer codigo;
	@ColunaDaoGenerico(nome="nome", tipo = Types.VARCHAR)
	private String nome;
	@ColunaDaoGenerico(nome="estado", tipo = Types.INTEGER)
	private EstadoVO estado;
	
	private String cep;
	@ColunaDaoGenerico(nome="codigoInep", tipo = Types.INTEGER)
	private Integer codigoInep;
	@ColunaDaoGenerico(nome="codigoIBGE", tipo = Types.VARCHAR)
	private String codigoIBGE;
	
	private Integer codigoDistrito;
	
	private String codigoSiafi;
	private Integer codigoDMS;
    
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Cidade</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public CidadeVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CidadeVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(CidadeVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Cidade) deve ser informado.");
		}
		if ((obj.getEstado() == null) || (obj.getEstado().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo ESTADO (Cidade) deve ser informado.");
		}
		// if (obj.getCep().equals("")) {
		// throw new
		// ConsistirException("O campo CEP (Cidade) deve ser informado.");
		// }
	}

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@XmlElement(name = "estado")
	public EstadoVO getEstado() {
		if (estado == null) {
			estado = new EstadoVO();
		}
		return estado;
	}

	public void setEstado(EstadoVO estado) {
		this.estado = estado;
	}

	// /**
	// * Operação responsável por retornar o valor de apresentação de um
	// atributo com um domínio específico.
	// * Com base no valor de armazenamento do atributo esta função é capaz de
	// retornar o
	// * de apresentação correspondente. Útil para campos como sexo,
	// escolaridade, etc.
	// */
	// public String getEstado_Apresentar() {
	// if (estado.equals("BA")) {
	// return "BA";
	// }
	// if (estado.equals("RS")) {
	// return "RS";
	// }
	// if (estado.equals("RR")) {
	// return "RR";
	// }
	// if (estado.equals("RO")) {
	// return "RO";
	// }
	// if (estado.equals("RN")) {
	// return "RN";
	// }
	// if (estado.equals("RJ")) {
	// return "RJ";
	// }
	// if (estado.equals("CE")) {
	// return "CE";
	// }
	// if (estado.equals("AP")) {
	// return "AP";
	// }
	// if (estado.equals("MT")) {
	// return "MT";
	// }
	// if (estado.equals("MS")) {
	// return "MS";
	// }
	// if (estado.equals("PR")) {
	// return "PR";
	// }
	// if (estado.equals("GO")) {
	// return "GO";
	// }
	// if (estado.equals("AM")) {
	// return "AM";
	// }
	// if (estado.equals("AL")) {
	// return "AL";
	// }
	// if (estado.equals("SP")) {
	// return "SP";
	// }
	// if (estado.equals("DF")) {
	// return "DF";
	// }
	// if (estado.equals("PI")) {
	// return "PI";
	// }
	// if (estado.equals("AC")) {
	// return "AC";
	// }
	// if (estado.equals("MG")) {
	// return "MG";
	// }
	// if (estado.equals("ES")) {
	// return "ES";
	// }
	// if (estado.equals("PE")) {
	// return "PE";
	// }
	// if (estado.equals("SE")) {
	// return "SE";
	// }
	// if (estado.equals("SC")) {
	// return "SC";
	// }
	// if (estado.equals("MA")) {
	// return "MA";
	// }
	// if (estado.equals("PB")) {
	// return "PB";
	// }
	// if (estado.equals("PA")) {
	// return "PA";
	// }
	// if (estado.equals("TO")) {
	// return "TO";
	// }
	// return (estado);
	// }
	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		if (codigo == null) {
			codigo = 0;
		}
		this.codigo = codigo;
	}

	@XmlElement(name = "codigoInep")
	public Integer getCodigoInep() {
		if (codigoInep == null) {
			codigoInep = 0;
		}
		return codigoInep;
	}

	public void setCodigoInep(Integer codigoInep) {
		this.codigoInep = codigoInep;
	}

	public boolean isNaturalidadeValida(PaizVO pais) {
		if (getEstado() != null && getEstado().getCodigoInep() != null && getEstado().getCodigoInep().intValue() != 0
				&& codigoInep != null && codigoInep.intValue() != 0 && pais.isNacao()) {
			return true;
		} else {
			return false;
		}
	}

	public String getCodigoEstadoInepCenso(PaizVO pais) {
		if (isNaturalidadeValida(pais)) {
			return String.valueOf(getEstado().getCodigoInep());
		} else {
			return "";
		}
	}

	public String getCodigoCidadeInepCenso(PaizVO pais) {
		if (isNaturalidadeValida(pais)) {
			return String.valueOf(codigoInep);
		} else {
			return "";
		}
	}

	@XmlElement(name = "codigoIBGE")
	public String getCodigoIBGE() {
		if (codigoIBGE == null) {
			codigoIBGE = "";
		}
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	@XmlElement(name = "codigoDistrito")
	public Integer getCodigoDistrito() {
		if (codigoDistrito == null) {
			codigoDistrito = 0;
		}
		return codigoDistrito;
	}

	public void setCodigoDistrito(Integer codigoDistrito) {
		this.codigoDistrito = codigoDistrito;
	}
	
	@XmlElement(name = "codigoSiafi")
	public String getCodigoSiafi() {
		if(codigoSiafi == null) {
			codigoSiafi = "";
		}
		return codigoSiafi;
	}

	public void setCodigoSiafi(String codigoSiafi) {
		this.codigoSiafi = codigoSiafi;
	}
	
	@XmlElement(name = "codigoDMS")
	public Integer getCodigoDMS() {
		if (codigoDMS == null) {
			codigoDMS = 0;
		}
		return codigoDMS;
	}

	public void setCodigoDMS(Integer codigoDMS) {
		this.codigoDMS = codigoDMS;
	}
	
//	public boolean isWebserviceNFSeImplementado() {
//		return CidadesWebServiceNFSEEnum.getEnumPorCodigoIBGE(getCodigoIBGE()) != null;
//    }
//	
//	public boolean getPossuiWebserviceCancelamento() {
//		CidadesWebServiceNFSEEnum cid = CidadesWebServiceNFSEEnum.getEnumPorCodigoIBGE(getCodigoIBGE());
//		return cid != null && cid.getPossuiWebserviceCancelamento();
//	}
//	
//	public boolean getRetornaUrlNotaAutorizada() {
//		CidadesWebServiceNFSEEnum cid = CidadesWebServiceNFSEEnum.getEnumPorCodigoIBGE(getCodigoIBGE());
//		return cid != null && cid.getRetornaUrlNotaAutorizada();
//	}
	
}