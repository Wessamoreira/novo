package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class TrabalhoConclusaoCursoInteracaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2279166512461818341L;
	private Integer codigo;
	private Date dataInteracao;
	private String interacao;
	private UsuarioVO responsavelInteracao;
	private EtapaTCCEnum etapaTCC;
	private TrabalhoConclusaoCursoVO trabalhoConclusaoCurso;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataInteracao() {
		if(dataInteracao == null){
			dataInteracao = new Date();
		}
		return dataInteracao;
	}

	public void setDataInteracao(Date dataInteracao) {
		this.dataInteracao = dataInteracao;
	}

	public String getInteracao() {
		if(interacao == null){
			interacao = "";
		}
		return interacao;
	}

	public void setInteracao(String interacao) {
		this.interacao = interacao;
	}

	public UsuarioVO getResponsavelInteracao() {
		if(responsavelInteracao == null){
			responsavelInteracao = new UsuarioVO();
		}
		return responsavelInteracao;
	}

	public void setResponsavelInteracao(UsuarioVO responsavelInteracao) {
		this.responsavelInteracao = responsavelInteracao;
	}

	public EtapaTCCEnum getEtapaTCC() {
		
		return etapaTCC;
	}

	public void setEtapaTCC(EtapaTCCEnum etapaTCC) {
		this.etapaTCC = etapaTCC;
	}

	public TrabalhoConclusaoCursoVO getTrabalhoConclusaoCurso() {
		if(trabalhoConclusaoCurso == null){
			trabalhoConclusaoCurso = new TrabalhoConclusaoCursoVO();
		}
		return trabalhoConclusaoCurso;
	}

	public void setTrabalhoConclusaoCurso(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) {
		this.trabalhoConclusaoCurso = trabalhoConclusaoCurso;
	}

}
