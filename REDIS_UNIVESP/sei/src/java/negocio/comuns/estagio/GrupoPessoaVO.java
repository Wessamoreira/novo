package negocio.comuns.estagio;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Uteis;

public class GrupoPessoaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4356172969972486122L;
	private Integer codigo;
	private String nome;
	private PessoaVO pessoaSupervisorGrupo;
	private List<GrupoPessoaItemVO> listaGrupoPessoaItemVO;
	private String filtroNomePessoa;
	private StatusAtivoInativoEnum filtroStatusAtivoInativoEnum;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public PessoaVO getPessoaSupervisorGrupo() {
		if (pessoaSupervisorGrupo == null) {
			pessoaSupervisorGrupo = new PessoaVO();
		}
		return pessoaSupervisorGrupo;
	}

	public void setPessoaSupervisorGrupo(PessoaVO pessoaSupervisorGrupo) {
		this.pessoaSupervisorGrupo = pessoaSupervisorGrupo;
	}

	public List<GrupoPessoaItemVO> getListaGrupoPessoaItemVO() {
		if (listaGrupoPessoaItemVO == null) {
			listaGrupoPessoaItemVO = new ArrayList<>();
		}
		return listaGrupoPessoaItemVO;
	}

	public void setListaGrupoPessoaItemVO(List<GrupoPessoaItemVO> listaGrupoPessoaItemVO) {
		this.listaGrupoPessoaItemVO = listaGrupoPessoaItemVO;
	}
	
	public Integer getTamanhoListaGrupoPessoaItem() {
		return getListaGrupoPessoaItemVO().size();
	}

	public String getFiltroNomePessoa() {
		if (filtroNomePessoa == null) {
			filtroNomePessoa = "";
		}
		return filtroNomePessoa;
	}

	public void setFiltroNomePessoa(String filtroNomePessoa) {
		this.filtroNomePessoa = filtroNomePessoa;
	}

	public StatusAtivoInativoEnum getFiltroStatusAtivoInativoEnum() {
		if (filtroStatusAtivoInativoEnum == null) {
			filtroStatusAtivoInativoEnum = StatusAtivoInativoEnum.NENHUM;
		}
		return filtroStatusAtivoInativoEnum;
	}

	public void setFiltroStatusAtivoInativoEnum(StatusAtivoInativoEnum filtroStatusAtivoInativoEnum) {
		this.filtroStatusAtivoInativoEnum = filtroStatusAtivoInativoEnum;
	}
	
	public Integer getTotalEstagiosEmCorrecaoAnaliseGrupo() {		
		return (Integer) getListaGrupoPessoaItemVO().stream().mapToInt(i -> i.getQtdeEstagioEmCorrecaoAnalise()).sum();
	}
	
    public Integer getMediaEstagioFacilitadorPorGrupo() {	
    	Double  media = Uteis.arrendondarForcandoCadasDecimais(((((Double) getListaGrupoPessoaItemVO().stream().mapToDouble(i -> i.getQtdeEstagioEmCorrecaoAnalise()).sum() ) /  getListaGrupoPessoaItemVO().size())), 1);
  	    Integer mediaApresentar =  (int) Math.round(media);   
    	return mediaApresentar;
		 
	}

}
