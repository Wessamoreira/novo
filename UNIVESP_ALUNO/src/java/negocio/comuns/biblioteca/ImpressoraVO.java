package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.enumeradores.TipoImpressoraEnum;

public class ImpressoraVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8092698170299916350L;
	private Integer codigo;
	private String idIdentificacao;
	private String ipRedeInterna;
	private String nomeImpressora;
	private TipoImpressoraEnum tipoImpressora;
	private BibliotecaVO bibliotecaVO;
	private Boolean usarBiblioteca;
	private Boolean usarFinanceiro;
	private Boolean usarRequerimento;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	
	/*
	 * Transiente
	 */
	private List<PoolImpressaoVO> poolImpressaoVOs;
	
	public String getIpRedeInterna() {
		if(ipRedeInterna == null){
			ipRedeInterna = "";
		}
		return ipRedeInterna;
	}
	
	public void setIpRedeInterna(String ipRedeInterna) {
		this.ipRedeInterna = ipRedeInterna;
	}
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public TipoImpressoraEnum getTipoImpressora() {
		if(tipoImpressora == null){
			tipoImpressora = TipoImpressoraEnum.JATO_TINTA;
		}
		return tipoImpressora;
	}
	public void setTipoImpressora(TipoImpressoraEnum tipoImpressora) {
		this.tipoImpressora = tipoImpressora;
	}
	public String getIdIdentificacao() {
		if(idIdentificacao == null){
			idIdentificacao = "";
		}
		return idIdentificacao;
	}
	public void setIdIdentificacao(String idIdentificacao) {
		this.idIdentificacao = idIdentificacao;
	}
	public String getNomeImpressora() {
		if(nomeImpressora == null){
			nomeImpressora = "";
		}
		return nomeImpressora;
	}
	public void setNomeImpressora(String nomeImpressora) {
		this.nomeImpressora = nomeImpressora;
	}

	public BibliotecaVO getBibliotecaVO() {
		if(bibliotecaVO == null){
			bibliotecaVO =  new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}

	public List<PoolImpressaoVO> getPoolImpressaoVOs() {
		if(poolImpressaoVOs == null){
			poolImpressaoVOs = new ArrayList<PoolImpressaoVO>(0);
		}
		return poolImpressaoVOs;
	}

	public void setPoolImpressaoVOs(List<PoolImpressaoVO> poolImpressaoVOs) {
		this.poolImpressaoVOs = poolImpressaoVOs;
	}
	

	public Boolean getUsarBiblioteca() {
		if(usarBiblioteca == null){
			usarBiblioteca = true;
		}
		return usarBiblioteca;
	}

	public void setUsarBiblioteca(Boolean usarBiblioteca) {
		this.usarBiblioteca = usarBiblioteca;
	}

	public Boolean getUsarFinanceiro() {
		if(usarFinanceiro == null){
			usarFinanceiro = false;
		}
		return usarFinanceiro;
	}

	public void setUsarFinanceiro(Boolean usarFinanceiro) {
		this.usarFinanceiro = usarFinanceiro;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}	
	

	public Boolean getUsarRequerimento() {
		if(usarRequerimento == null){
			usarRequerimento = false;
		}
		return usarRequerimento;
	}

	public void setUsarRequerimento(Boolean usarRequerimento) {
		this.usarRequerimento = usarRequerimento;
	}		
}
