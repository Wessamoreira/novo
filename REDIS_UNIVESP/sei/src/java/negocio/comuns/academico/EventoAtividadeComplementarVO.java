package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;

public class EventoAtividadeComplementarVO {
	private String atividadeComplementar;
	private Integer codigoTipoAtividadeComplementar;
	private String nomeEvento;
	private Date dataEvento;
	private Integer cargaHorariaEvento;
	private Integer cargaHorariaRealizada;
	private Integer cargaHorariaConsiderada;
	private Integer cargaHorariaMaximaConsiderada;
	private ArquivoVO arquivoVO;
	private String caminhoArquivoWeb;
	private String responsavelUltimaAlteracao;
	private Date dataUltimaAlteracao;
	private String observacao;
	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO;

	public String getData_Apresentar() {
		if (getDataEvento() == null) {
			return "";
		}
		return Uteis.getData(getDataEvento());
	}

	public String getAtividadeComplementar() {
		if (atividadeComplementar == null) {
			atividadeComplementar = "";
		}
		return atividadeComplementar;
	}

	public void setAtividadeComplementar(String atividadeComplementar) {
		this.atividadeComplementar = atividadeComplementar;
	}

	public String getNomeEvento() {
		if (nomeEvento == null) {
			nomeEvento = "";
		}
		return nomeEvento;
	}

	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}

	public Integer getCargaHorariaEvento() {
		return cargaHorariaEvento;
	}

	public void setCargaHorariaEvento(Integer cargaHorariaEvento) {
		this.cargaHorariaEvento = cargaHorariaEvento;
	}

	public Integer getCargaHorariaRealizada() {
		if (cargaHorariaRealizada == null) {
			cargaHorariaRealizada = 0;
		}
		return cargaHorariaRealizada;
	}

	public void setCargaHorariaRealizada(Integer cargaHorariaRealizada) {
		this.cargaHorariaRealizada = cargaHorariaRealizada;
	}

	public Integer getCargaHorariaConsiderada() {
		if (cargaHorariaConsiderada == null) {
			cargaHorariaConsiderada = 0;
		}
		return cargaHorariaConsiderada;
	}

	public void setCargaHorariaConsiderada(Integer cargaHorariaConsiderada) {
		this.cargaHorariaConsiderada = cargaHorariaConsiderada;
	}

	public Date getDataEvento() {
		return dataEvento;
	}

	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}

	public Integer getCodigoTipoAtividadeComplementar() {
		if (codigoTipoAtividadeComplementar == null) {
			codigoTipoAtividadeComplementar = 0;
		}
		return codigoTipoAtividadeComplementar;
	}

	public void setCodigoTipoAtividadeComplementar(Integer codigoTipoAtividadeComplementar) {
		this.codigoTipoAtividadeComplementar = codigoTipoAtividadeComplementar;
	}

	public Integer getCargaHorariaMaximaConsiderada() {
		if (cargaHorariaMaximaConsiderada == null) {
			cargaHorariaMaximaConsiderada = 0;
		}
		return cargaHorariaMaximaConsiderada;
	}

	public void setCargaHorariaMaximaConsiderada(Integer cargaHorariaMaximaConsiderada) {
		this.cargaHorariaMaximaConsiderada = cargaHorariaMaximaConsiderada;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public Boolean getApresentarDownload() {
		return getArquivoVO().getCodigo() > 0;
	}

	public String getCaminhoArquivoWeb() {

		return caminhoArquivoWeb;
	}

	public void setCaminhoArquivoWeb(String caminhoArquivoWeb) {
		this.caminhoArquivoWeb = caminhoArquivoWeb;
	}

	public String getResponsavelUltimaAlteracao() {
		if (responsavelUltimaAlteracao == null) {
			responsavelUltimaAlteracao = "";
		}
		return responsavelUltimaAlteracao;
	}

	public void setResponsavelUltimaAlteracao(String responsavelUltimaAlteracao) {
		this.responsavelUltimaAlteracao = responsavelUltimaAlteracao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	
	public String getObservacao() {
		if(observacao == null){
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatriculaVO() {
		if(registroAtividadeComplementarMatriculaVO == null){
			registroAtividadeComplementarMatriculaVO = new RegistroAtividadeComplementarMatriculaVO();
		}
		return registroAtividadeComplementarMatriculaVO;
	}

	public void setRegistroAtividadeComplementarMatriculaVO(
			RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO) {
		this.registroAtividadeComplementarMatriculaVO = registroAtividadeComplementarMatriculaVO;
	}
}
