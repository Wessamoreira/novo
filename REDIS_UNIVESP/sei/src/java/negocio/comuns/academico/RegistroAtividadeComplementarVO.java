package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class RegistroAtividadeComplementarVO extends SuperVO {

	private Integer codigo;
	private String nomeEvento;
	private String instituicaoResponsavel;
	private String atividade;
	private String local;
	private Date data;
	private List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs;
	private Integer coordenador;
	private UsuarioVO responsavelUltimaAlteracao;
	private Date dataUltimaAlteracao;
	private static final long serialVersionUID = 1L;
	private List<String> listaMensagemErroProcessamento;

	public RegistroAtividadeComplementarVO() {
		super();
	}

	public Integer getCodigo() {
		if (this.codigo == null) {
			this.codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNomeEvento() {
		if (this.nomeEvento == null) {
			this.nomeEvento = "";
		}
		return nomeEvento;
	}

	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}

	public String getInstituicaoResponsavel() {
		if (this.instituicaoResponsavel == null) {
			this.instituicaoResponsavel = "";
		}
		return instituicaoResponsavel;
	}

	public void setInstituicaoResponsavel(String instituicaoResponsavel) {
		this.instituicaoResponsavel = instituicaoResponsavel;
	}

	public String getAtividade() {
		if (this.atividade == null) {
			this.atividade = "";
		}
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getLocal() {
		if (this.local == null) {
			this.local = "";
		}
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Date getData() {
		if (this.data == null) {
			this.data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaRegistroAtividadeComplementarMatriculaVOs() {
		if (this.listaRegistroAtividadeComplementarMatriculaVOs == null) {
			this.listaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaRegistroAtividadeComplementarMatriculaVOs;
	}

	public void setListaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs) {
		this.listaRegistroAtividadeComplementarMatriculaVOs = listaRegistroAtividadeComplementarMatriculaVOs;
	}

	public String getData_Apresentar() {
		return Uteis.getDataAno4Digitos(getData());
	}

	public Integer getCoordenador() {
		if (this.coordenador == null) {
			this.coordenador = 0;
		}
		return coordenador;
	}

	public void setCoordenador(Integer coordenador) {
		this.coordenador = coordenador;
	}

	public UsuarioVO getResponsavelUltimaAlteracao() {
		if (responsavelUltimaAlteracao == null) {
			responsavelUltimaAlteracao = new UsuarioVO();
		}
		return responsavelUltimaAlteracao;
	}

	public void setResponsavelUltimaAlteracao(UsuarioVO responsavelUltimaAlteracao) {
		this.responsavelUltimaAlteracao = responsavelUltimaAlteracao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public String getDataUltimaAlteracao_Apresentar() {
		return Uteis.getDataComHora(getDataUltimaAlteracao());
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	
	public boolean isExisteChRealizadaMaiorQueHorasPermitidaPeriodoLetivo(){
		for (RegistroAtividadeComplementarMatriculaVO obj : getListaRegistroAtividadeComplementarMatriculaVOs()) {
			if(obj.isChRealizadaMaiorQueHorasPermitidaPeriodoLetivo()){
				return true;
			}
		}
		return false;
	}

	public List<String> getListaMensagemErroProcessamento() {
		if(listaMensagemErroProcessamento == null) {
			listaMensagemErroProcessamento = new ArrayList<String>(0);
		}
		return listaMensagemErroProcessamento;
	}

	public void setListaMensagemErroProcessamento(List<String> listaMensagemErroProcessamento) {
		this.listaMensagemErroProcessamento = listaMensagemErroProcessamento;
	}
	
	

}
