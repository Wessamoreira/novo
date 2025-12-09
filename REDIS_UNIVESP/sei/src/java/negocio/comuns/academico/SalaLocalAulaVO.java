package negocio.comuns.academico;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "sala")
public class SalaLocalAulaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6986574639827067483L;
	private Integer codigo;
	private String sala;
	private Integer capacidade;
	private LocalAulaVO localAula;
	private Integer ordem;
	private Boolean naoControlarChoqueSala;
	private Boolean inativo;
	/*
	 * Atribuito Transient utilizado na distribuição de sala
	 *
	 */
	private Boolean utilizarDistribuicao;
	private Integer ordemDistribuicao;
	
	public SalaLocalAulaVO clone() throws CloneNotSupportedException{		
			return (SalaLocalAulaVO) super.clone();		
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "sala")
	public String getSala() {
		if(sala == null){
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public Integer getCapacidade() {
		if(capacidade == null){
			capacidade = 0;
		}
		return capacidade;
	}

	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}

	@XmlElement(name = "localAula")
	public LocalAulaVO getLocalAula() {
		if(localAula == null){
			localAula = new LocalAulaVO();
		}
		return localAula;
	}

	public void setLocalAula(LocalAulaVO localAula) {
		this.localAula = localAula;
	}

	public Boolean getUtilizarDistribuicao() {
		if(utilizarDistribuicao == null){
			utilizarDistribuicao = true;
		}
		return utilizarDistribuicao;
	}

	public void setUtilizarDistribuicao(Boolean utilizarDistribuicao) {
		this.utilizarDistribuicao = utilizarDistribuicao;
	}

	public Integer getOrdemDistribuicao() {
		if(ordemDistribuicao == null){
			ordemDistribuicao = 0;
		}
		return ordemDistribuicao;
	}

	public void setOrdemDistribuicao(Integer ordemDistribuicao) {
		this.ordemDistribuicao = ordemDistribuicao;
	}
	
	public Integer getOrdem() {
		if(ordem == null){
			ordem = 1;
		}
		return ordem;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	/**
	 * Transiente
	 */
	public String localSala;
	public String getLocalSala(){
		if(localSala == null){
			localSala =  getLocalAula().getLocal()+" - "+getSala();
		}
		return localSala;
	}
	
	public void setLocalSala(String localSala) {
		this.localSala = localSala;
	}

	/**
	 * @return the naoControlarChoqueSala
	 */
	public Boolean getNaoControlarChoqueSala() {
		if (naoControlarChoqueSala == null) {
			naoControlarChoqueSala = false;
		}
		return naoControlarChoqueSala;
	}

	/**
	 * @param naoControlarChoqueSala the naoControlarChoqueSala to set
	 */
	public void setNaoControlarChoqueSala(Boolean naoControlarChoqueSala) {
		this.naoControlarChoqueSala = naoControlarChoqueSala;
	}
	
	public Boolean getControlarChoqueSala(){
		return !getNaoControlarChoqueSala();
	}

	/**
	 * @return the inativo
	 */
	public Boolean getInativo() {
		if (inativo == null) {
			inativo = false;
		}
		return inativo;
	}

	/**
	 * @param inativo the inativo to set
	 */
	public void setInativo(Boolean inativo) {
		this.inativo = inativo;
	}
	
	

}
