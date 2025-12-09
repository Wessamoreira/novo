package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.TipoCobrancaPixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.Bancos;

/**
 * Reponsável por manter os dados da entidade Banco. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class BancoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private String nrBanco;
	private String digito;
	/*
	 * Campo utilizado, para gerenciar a situação do banco Caixa Econômica, onde é possível ter para o mesmo banco layouts diferentes de geração de boleto, CEF e SICOB.
	 */
	private String modeloGeracaoBoleto;
	private ModeloBoletoVO modeloBoletoMatricula;
	private ModeloBoletoVO modeloBoletoMensalidade;
	private ModeloBoletoVO modeloBoletoMaterialDidatico;
	private ModeloBoletoVO modeloBoletoRequerimento;
	private ModeloBoletoVO modeloBoletoProcessoSeletivo;
	private ModeloBoletoVO modeloBoletoRenegociacao;
	private ModeloBoletoVO modeloBoletoOutros;

	private String urlApiPixProducao;
	private String urlApiPixHomologacao;
	private String urlApiPixAutenticacao;
	private String orientacaoPix;
	private boolean integracaoPix = false;
	private boolean possuiWebhook = false;
	private TipoCobrancaPixEnum tipoCobrancaPixEnum;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Banco</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public BancoVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>BancoVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(BancoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Banco) deve ser informado.");
		}
		if (obj.getNrBanco().equals("")) {
			throw new ConsistirException("O campo NÚMERO DO BANCO (Banco) deve ser informado.");
		}
		if (!obj.getNrBanco().equals("") && obj.getNrBanco().length() < 3) {
			throw new ConsistirException("O NÚMERO DO BANCO (Banco) deve possuir 3 caracteres seguindo o layout da FEBRABAN.");
		}
		Uteis.checkState(obj.isIntegracaoPix() && !Uteis.isAtributoPreenchido(obj.getUrlApiPixAutenticacao()), "O campo Url Api PIX Autenticação (APIs Banco) deve ser informado.");
		Uteis.checkState(obj.isIntegracaoPix() && !Uteis.isAtributoPreenchido(obj.getUrlApiPixProducao()), "O campo Url Api PIX Produção (APIs Banco) deve ser informado.");
		Uteis.checkState(obj.isIntegracaoPix() && !Uteis.isAtributoPreenchido(obj.getUrlApiPixHomologacao()), "O campo Url Api PIX Homologação (APIs Banco) deve ser informado.");
		Uteis.checkState(obj.isIntegracaoPix() && !Uteis.isAtributoPreenchido(obj.getOrientacaoPix()), "O campo Orientação (APIs Banco) deve ser informado.");
	}

	public String getNrBanco() {
		if (nrBanco == null) {
			nrBanco = "";
		}
		return (nrBanco);
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
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

	/**
	 * @return the modeloBoletoMatricula
	 */
	public ModeloBoletoVO getModeloBoletoMatricula() {
		if (modeloBoletoMatricula == null) {
			modeloBoletoMatricula = new ModeloBoletoVO();
		}
		return modeloBoletoMatricula;
	}

	/**
	 * @param modeloBoletoMatricula the modeloBoletoMatricula to set
	 */
	public void setModeloBoletoMatricula(ModeloBoletoVO modeloBoletoMatricula) {
		this.modeloBoletoMatricula = modeloBoletoMatricula;
	}

	/**
	 * @return the modeloBoletoMensalidade
	 */
	public ModeloBoletoVO getModeloBoletoMensalidade() {
		if (modeloBoletoMensalidade == null) {
			modeloBoletoMensalidade = new ModeloBoletoVO();
		}
		return modeloBoletoMensalidade;
	}

	/**
	 * @param modeloBoletoMensalidade the modeloBoletoMensalidade to set
	 */
	public void setModeloBoletoMensalidade(ModeloBoletoVO modeloBoletoMensalidade) {
		this.modeloBoletoMensalidade = modeloBoletoMensalidade;
	}

	public ModeloBoletoVO getModeloBoletoMaterialDidatico() {
		if (modeloBoletoMaterialDidatico == null) {
			modeloBoletoMaterialDidatico = new ModeloBoletoVO();
		}
		return modeloBoletoMaterialDidatico;
	}

	public void setModeloBoletoMaterialDidatico(ModeloBoletoVO modeloBoletoMaterialDidatico) {
		this.modeloBoletoMaterialDidatico = modeloBoletoMaterialDidatico;
	}

	/**
	 * @return the modeloBoletoRequerimento
	 */
	public ModeloBoletoVO getModeloBoletoRequerimento() {
		if (modeloBoletoRequerimento == null) {
			modeloBoletoRequerimento = new ModeloBoletoVO();
		}
		return modeloBoletoRequerimento;
	}

	/**
	 * @param modeloBoletoRequerimento the modeloBoletoRequerimento to set
	 */
	public void setModeloBoletoRequerimento(ModeloBoletoVO modeloBoletoRequerimento) {
		this.modeloBoletoRequerimento = modeloBoletoRequerimento;
	}

	/**
	 * @return the modeloBoletoProcessoSeletivo
	 */
	public ModeloBoletoVO getModeloBoletoProcessoSeletivo() {
		if (modeloBoletoProcessoSeletivo == null) {
			modeloBoletoProcessoSeletivo = new ModeloBoletoVO();
		}
		return modeloBoletoProcessoSeletivo;
	}

	/**
	 * @param modeloBoletoProcessoSeletivo the modeloBoletoProcessoSeletivo to set
	 */
	public void setModeloBoletoProcessoSeletivo(ModeloBoletoVO modeloBoletoProcessoSeletivo) {
		this.modeloBoletoProcessoSeletivo = modeloBoletoProcessoSeletivo;
	}

	/**
	 * @return the modeloBoletoOutros
	 */
	public ModeloBoletoVO getModeloBoletoOutros() {
		if (modeloBoletoOutros == null) {
			modeloBoletoOutros = new ModeloBoletoVO();
		}
		return modeloBoletoOutros;
	}

	/**
	 * @param modeloBoletoOutros the modeloBoletoOutros to set
	 */
	public void setModeloBoletoOutros(ModeloBoletoVO modeloBoletoOutros) {
		this.modeloBoletoOutros = modeloBoletoOutros;
	}

	public ModeloBoletoVO getModeloBoleto(String tipoModelo) throws Exception {
		ModeloBoletoVO modeloBoleto = (ModeloBoletoVO) UtilReflexao.invocarMetodoGet(this, tipoModelo);
		return modeloBoleto;
	}

	public String getDigito() {
		if (digito == null) {
			digito = "";
		}
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public ModeloBoletoVO getModeloBoletoRenegociacao() {
		if (modeloBoletoRenegociacao == null) {
			modeloBoletoRenegociacao = new ModeloBoletoVO();
		}
		return modeloBoletoRenegociacao;
	}

	public void setModeloBoletoRenegociacao(ModeloBoletoVO modeloBoletoRenegociacao) {
		this.modeloBoletoRenegociacao = modeloBoletoRenegociacao;
	}

	public String getModeloGeracaoBoleto() {
		if (modeloGeracaoBoleto == null) {
			modeloGeracaoBoleto = "";
		}
		return modeloGeracaoBoleto;
	}

	public boolean getApresentarModeloCaixa() {
		if (getNrBanco().equals("104")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getApresentarModeloSicoob() {
		if (getNrBanco().equals("756")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSafra() {
		return getNrBanco().equals("422");
	}

	public boolean isBancoBrasil() {
		return getNrBanco().equals("001");
	}
	
	public boolean isBancoItau() {
		return getNrBanco().equals("341");
	}

	public void setModeloGeracaoBoleto(String modeloGeracaoBoleto) {
		this.modeloGeracaoBoleto = modeloGeracaoBoleto;
	}

	public List getProcessarAvisoBancos() {
		List<BancoEnumVO> lista = new ArrayList<BancoEnumVO>();
		for (BancoEnum enumerador : BancoEnum.values()) {
			if (enumerador != null) {
				BancoEnumVO bancoEnumVO = new BancoEnumVO();
				bancoEnumVO.setDescricao(enumerador.getDescricao());
				bancoEnumVO.setNrBanco(enumerador.getNrBanco());
				bancoEnumVO.setPossuiRemessa(enumerador.getPossuiRemessa());
				bancoEnumVO.setPossuiRetorno(enumerador.getPossuiRetorno());
				bancoEnumVO.setPossuiRetornoRemessa(enumerador.getPossuiRetornoRemessa());
				bancoEnumVO.setPossuiRemessaContaPagar(enumerador.getPossuiRemessaContaPagar());
				lista.add(bancoEnumVO);
			}
		}
//        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//        Collections.sort((List) lista, ordenador);
		return lista;
	}

	public String getUrlApiPixProducao() {
		if (urlApiPixProducao == null) {
			urlApiPixProducao = "";
		}
		return urlApiPixProducao;
	}

	public void setUrlApiPixProducao(String urlApiPixProducao) {
		this.urlApiPixProducao = urlApiPixProducao;
	}

	public String getUrlApiPixHomologacao() {
		if (urlApiPixHomologacao == null) {
			urlApiPixHomologacao = "";
		}
		return urlApiPixHomologacao;
	}

	public void setUrlApiPixHomologacao(String urlApiPixHomologacao) {
		this.urlApiPixHomologacao = urlApiPixHomologacao;
	}

	public String getUrlApiPixAutenticacao() {
		if (urlApiPixAutenticacao == null) {
			urlApiPixAutenticacao = "";
		}
		return urlApiPixAutenticacao;
	}

	public void setUrlApiPixAutenticacao(String urlApiPixAutenticacao) {
		this.urlApiPixAutenticacao = urlApiPixAutenticacao;
	}

	public String getOrientacaoPix() {
		if (orientacaoPix == null) {
			orientacaoPix = "";
		}
		return orientacaoPix;
	}

	public void setOrientacaoPix(String orientacaoPix) {
		this.orientacaoPix = orientacaoPix;
	}

	public boolean isIntegracaoPix() {
		return integracaoPix;
	}

	public void setIntegracaoPix(boolean integracaoPix) {
		this.integracaoPix = integracaoPix;
	}

	public boolean isPossuiWebhook() {
		return possuiWebhook;
	}

	public void setPossuiWebhook(boolean possuiWebhook) {
		this.possuiWebhook = possuiWebhook;
	}

	public TipoCobrancaPixEnum getTipoCobrancaPixEnum() {
		if (tipoCobrancaPixEnum == null) {
			tipoCobrancaPixEnum = TipoCobrancaPixEnum.IMEDIATA;
		}
		return tipoCobrancaPixEnum;
	}

	public void setTipoCobrancaPixEnum(TipoCobrancaPixEnum tipoCobrancaPixEnum) {
		this.tipoCobrancaPixEnum = tipoCobrancaPixEnum;
	}

	
	
	public List getProcessarDescricaoTokenIDBancos() {
		List<String> lista = new ArrayList<String>();
		if(this.getNrBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco())) {
			lista.add("Banco Brasil : este campo e deve ser preenchido com a informação : developer_application_key.");
		}
		if(this.getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
					
		}
		if(this.getNrBanco().equals(Bancos.BANESTE.getNumeroBanco())) {
			
		}
		if(this.getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			
		}
		if(this.getNrBanco().equals(Bancos.ITAU.getNumeroBanco())) {
			
		}	
		return lista;
	}
	
	
	
	public List getProcessarDescricaoTokenClienteBancos() {
		List<String> lista = new ArrayList<String>();
		if(this.getNrBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco())) {
			lista.add("Banco Brasil : este campo e deve ser preenchido com a informação : campo client_id.");
		}
		if(this.getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
					
		}
		if(this.getNrBanco().equals(Bancos.BANESTE.getNumeroBanco())) {
			
		}
		if(this.getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			
		}
		if(this.getNrBanco().equals(Bancos.ITAU.getNumeroBanco())) {
			
		}
		return lista;
	}
	
	
	public List getProcessarDescricaoTokenKeyBancos() {
		List<String> lista = new ArrayList<String>();
		if(this.getNrBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco())) {
			lista.add("Banco Brasil : este campo e deve ser preenchido com a informação : client_secret.");
		}
		if(this.getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
					
		}
		if(this.getNrBanco().equals(Bancos.BANESTE.getNumeroBanco())) {
			
		}
		if(this.getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			
		}
		if(this.getNrBanco().equals(Bancos.ITAU.getNumeroBanco())) {
			
		}
		return lista;
	}
	
}
