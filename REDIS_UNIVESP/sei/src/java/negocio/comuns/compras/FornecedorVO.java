package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.EmpresaVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsï¿½vel por manter os dados da entidade Fornecedor. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os mï¿½todos de acesso a estes atributos. Classe utilizada para apresentar e manter em memï¿½ria os dados desta entidade.
 * 
 * @see SuperVO
 * @see EmpresaVO
 */
public class FornecedorVO extends EmpresaVO implements PossuiEndereco {

	private List<FornecedorCategoriaProdutoVO> FornecedorCategoriaProdutoVOs;
	private String nomeBanco;
	private String numeroBancoRecebimento;
	private String numeroAgenciaRecebimento;
	private String digitoAgenciaRecebimento;
	private String contaCorrenteRecebimento;
	private String digitoCorrenteRecebimento;
	private String contato;
	private String nomePessoaFisica;
	private String cpfFornecedor;
	private Boolean isentarTaxaBoleto;
	private Boolean isTemMei;
	private BancoVO bancoVO;
	private String observacao;
	private String chaveEnderecamentoPix;
	private TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum;
	public static final long serialVersionUID = 1L;
	
	public enum enumCampoConsultaFornecedor {
		NOME, RAZAOSOCIAL, CPF, CNPJ, RG,
	}
	
	

	/**
	 * Construtor padrï¿½o da classe <code>Fornecedor</code>. Cria uma nova instï¿½ncia desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public FornecedorVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operaï¿½ï¿½o responsï¿½vel por validar os dados de um objeto da classe <code>FornecedorVO</code>. Todos os tipos de consistï¿½ncia de dados sï¿½o e devem ser implementadas neste mï¿½todo. Sï¿½o validaï¿½ï¿½es tï¿½picas: verificaï¿½ï¿½o de campos obrigatï¿½rios, verificaï¿½ï¿½o de valores vï¿½lidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistï¿½ncia for encontrada aumaticamente ï¿½ gerada uma exceï¿½ï¿½o descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(FornecedorVO obj) throws ConsistirException {
		if (obj.getTipoEmpresa().equals("")) {
			throw new ConsistirException("O campo TIPO EMPRESA (Fornecedor) deve ser informado.");
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Fornecedor) deve ser informado.");
		}
		if (obj.getTipoEmpresa().equals("JU")) {
			if (obj.getRazaoSocial().equals("")) {
				throw new ConsistirException("O campo RAZÃO SOCIAL (Fornecedor) deve ser informado.");
			}
			if (obj.getCNPJ().equals("") || obj.getCNPJ().length() < 18 ) {
				throw new ConsistirException("O campo CNPJ (Fornecedor) deve ser informado.");
			}else if(Uteis.validaCNPJ(obj.getCNPJ())){				
				throw new ConsistirException("O campo CNPJ (Fornecedor) e invalido .");
			}		
			
			if (obj.isTemMei) {
				if (obj.getNomePessoaFisica().equals("")) {
					throw new ConsistirException("O campo NOME Pessoa Física (Fornecedor) deve ser informado.");
				}
				if (obj.getCpfFornecedor().equals("") || obj.getCpfFornecedor().length() < 14) {
					throw new ConsistirException("O campo CPF Pessoa Fisica (Fornecedor) deve ser informado.");
				} else if (!obj.getCpfFornecedor().equals("") && obj.getCpfFornecedor().length() > 14) {
					throw new ConsistirException(
							"O campo CPF  Pessoa Física (Fornecedor) não deve exceder 14 caracteres.");
				} else if (Uteis.obterCpfValido(obj.getCpfFornecedor()).equals("")) {
					throw new ConsistirException("O  CPF  Pessoa Física (Fornecedor) e invalido .");

				}
			} 
		} else {
			obj.setInscEstadual("");
			obj.setCNPJ("");
			obj.setRazaoSocial("");
			
		}
		if (obj.getTipoEmpresa().equals("FI")) {
			if (obj.getCPF().equals("") || obj.getCPF().length() < 14) {
				throw new ConsistirException("O campo CPF (Fornecedor) deve ser informado.");
			} else if (!obj.getCPF().equals("") && obj.getCPF().length() > 14) {
				throw new ConsistirException("O campo CPF (Fornecedor) não deve exceder 14 caracteres.");
			}else if(Uteis.obterCpfValido(obj.getCPF()).equals("")) {
				throw new ConsistirException("O  CPF (Fornecedor) e invalido .");
				
			}
			
		
		} else {
			obj.setCPF("");
			obj.setRG("");
			
		}
		if (obj.getEndereco().equals("")) {
			throw new ConsistirException("O campo ENDEREÇO (Fornecedor) deve ser informado.");
		}
		if (obj.getSetor().equals("")) {
			throw new ConsistirException("O campo SETOR (Fornecedor) deve ser informado.");
		}
		if ((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CIDADE (Fornecedor) deve ser informado.");
		}

        if (obj.getTelComercial1().equals("")) {
            throw new ConsistirException("O campo TELEFONE COMERCIAL 1 (Fornecedor) deve ser informado.");
        }
        if (obj.getCEP().equals("")) {
        	throw new ConsistirException("O campo CEP (Fornecedor) deve ser informado.");
        } else if (obj.getCEP().length() > 10) {
        	throw new ConsistirException("O campo CEP (Fornecedor) não deve exceder 10 caracteres.");
        }
        if (!obj.getEmail().equals("") && !Uteis.getValidaEmail(obj.getEmail())) {
        	throw new ConsistirException("O campo Email informado é inválido.");
        }
    }

	public void adicionarObjFornecedorCategoriaProdutoVOs(FornecedorCategoriaProdutoVO obj) throws Exception {
		FornecedorCategoriaProdutoVO.validarDados(obj);
		for (FornecedorCategoriaProdutoVO objExistente : getFornecedorCategoriaProdutoVOs()) {
			if (objExistente.getCategoriaProdutoVO().getCodigo().intValue() == obj.getCategoriaProdutoVO().getCodigo()) {
				return;
			}
		}
		getFornecedorCategoriaProdutoVOs().add(obj);
	}

	public void removerObjFornecedorCategoriaProdutoVOs(FornecedorCategoriaProdutoVO obj) {
		int index = 0;
		for (FornecedorCategoriaProdutoVO objExistente : getFornecedorCategoriaProdutoVOs()) {
			if (objExistente.getCategoriaProdutoVO().getCodigo().intValue() == obj.getCategoriaProdutoVO().getCodigo()) {
				getFornecedorCategoriaProdutoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operaï¿½ï¿½o reponsï¿½vel por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setNomeBanco("");
		setNumeroBancoRecebimento("");
		setNumeroAgenciaRecebimento("");
		setContaCorrenteRecebimento("");
		setContato("");		
		
		
		
		
	}

	public String getCpfOuCnpjComNome() {
		if(Uteis.isAtributoPreenchido(getCnpjOuCfp_Apresentar())){
			return getCnpjOuCfp_Apresentar()+" - "+ getNome();
		}
		return  getNome();
	}
	
	public String getNome_Abreviado() {
		if (nome.length() > 16) {
			return nome.substring(0, 15);
		}
		return nome;
	}

	public boolean getTel1() {
		if (getTelComercial1().equals("")) {
			return false;
		}
		return true;
	}

	public boolean getTel2() {
		if (getTelComercial2().equals("")) {
			return false;
		}
		return true;
	}

	public boolean getTel3() {
		if (getTelComercial3().equals("")) {
			return false;
		}
		return true;
	}

	public boolean getFaxs() {
		if (getFax().equals("")) {
			return false;
		}
		return true;
	}

	public Boolean getApresentarCamposCNPJ() {
		if (getTipoEmpresa().equals("JU")) {			
			return true;
		}
		return false;
	}
	
	public String getEnderecoCompleto() {
		
		return String.format("%s, Setor %s, %s %s", getEndereco(), getSetor(), getCidade().getNome(), getCidade().getEstado().getNome());
	}

	public List<FornecedorCategoriaProdutoVO> getFornecedorCategoriaProdutoVOs() {
		if (FornecedorCategoriaProdutoVOs == null) {
			FornecedorCategoriaProdutoVOs = new ArrayList<FornecedorCategoriaProdutoVO>(0);
		}
		return FornecedorCategoriaProdutoVOs;
	}

	public void setFornecedorCategoriaProdutoVOs(List<FornecedorCategoriaProdutoVO> FornecedorCategoriaProdutoVOs) {
		this.FornecedorCategoriaProdutoVOs = FornecedorCategoriaProdutoVOs;
	}

	public Integer getColuna() {
		if (getFornecedorCategoriaProdutoVOs().size() < 3) {
			return getFornecedorCategoriaProdutoVOs().size();
		}
		return 3;

	}

	public Integer getElementos() {
		return getFornecedorCategoriaProdutoVOs().size();
	}

	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}

	/**
	 * @param nomeBanco
	 *            the nomeBanco to set
	 */
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the numeroBancoRecebimento
	 */
	public String getNumeroBancoRecebimento() {
		return numeroBancoRecebimento;
	}

	/**
	 * @param numeroBancoRecebimento
	 *            the numeroBancoRecebimento to set
	 */
	public void setNumeroBancoRecebimento(String numeroBancoRecebimento) {
		this.numeroBancoRecebimento = numeroBancoRecebimento;
	}

	/**
	 * @return the numeroAgenciaRecebimento
	 */
	public String getNumeroAgenciaRecebimento() {
		return numeroAgenciaRecebimento;
	}

	/**
	 * @param numeroAgenciaRecebimento
	 *            the numeroAgenciaRecebimento to set
	 */
	public void setNumeroAgenciaRecebimento(String numeroAgenciaRecebimento) {
		this.numeroAgenciaRecebimento = numeroAgenciaRecebimento;
	}

	/**
	 * @return the contaCorrenteRecebimento
	 */
	public String getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	/**
	 * @param contaCorrenteRecebimento
	 *            the contaCorrenteRecebimento to set
	 */
	public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public Boolean getIsentarTaxaBoleto() {
		if (isentarTaxaBoleto == null) {
			isentarTaxaBoleto = true;
		}
		return isentarTaxaBoleto;
	}

	public void setIsentarTaxaBoleto(Boolean isentarTaxaBoleto) {
		this.isentarTaxaBoleto = isentarTaxaBoleto;
	}

	public String getDigitoAgenciaRecebimento() {
		if (digitoAgenciaRecebimento == null) {
			digitoAgenciaRecebimento = "";
		}
		return digitoAgenciaRecebimento;
	}

	public void setDigitoAgenciaRecebimento(String digitoAgenciaRecebimento) {
		this.digitoAgenciaRecebimento = digitoAgenciaRecebimento;
	}

	public String getDigitoCorrenteRecebimento() {
		if (digitoCorrenteRecebimento == null) {
			digitoCorrenteRecebimento = "";
		}
		return digitoCorrenteRecebimento;
	}

	public void setDigitoCorrenteRecebimento(String digitoCorrenteRecebimento) {
		this.digitoCorrenteRecebimento = digitoCorrenteRecebimento;
	}

	public Boolean getIsTemMei() {
		if(isTemMei ==null){
		   isTemMei = false;		}
		return isTemMei;
	}

	public void setIsTemMei(Boolean isTemMei) {
		this.isTemMei = isTemMei;
	}

	public String getNomePessoaFisica() {
		if(nomePessoaFisica == null ) {
			nomePessoaFisica ="";
		}
		return nomePessoaFisica;
	}

	public void setNomePessoaFisica(String nomePessoaFisica) {
		this.nomePessoaFisica = nomePessoaFisica;
	}

	public String getCpfFornecedor() {
		  if(cpfFornecedor==null) {
			  cpfFornecedor ="";			  
		  }
		return cpfFornecedor;
	}

	public void setCpfFornecedor(String cpfFornecedor) {
		this.cpfFornecedor = cpfFornecedor;
	}

	public BancoVO getBancoVO() {
		if(bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public String getChaveEnderecamentoPix() {
		if(chaveEnderecamentoPix == null ) {
			chaveEnderecamentoPix = ""; 
		}
		return chaveEnderecamentoPix;
	}

	public void setChaveEnderecamentoPix(String chaveEnderecamentoPix) {		
		this.chaveEnderecamentoPix = chaveEnderecamentoPix;
	}

	public TipoIdentificacaoChavePixEnum getTipoIdentificacaoChavePixEnum() {
		if(tipoIdentificacaoChavePixEnum == null ) {
			tipoIdentificacaoChavePixEnum = TipoIdentificacaoChavePixEnum.CPF_CNPJ;
		}
		return tipoIdentificacaoChavePixEnum;
	}

	public void setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum) {
		this.tipoIdentificacaoChavePixEnum = tipoIdentificacaoChavePixEnum;
	}
	
	
	
}
