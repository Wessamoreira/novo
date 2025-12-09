package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoSaidaAcervo;

public class ExemplaresRelVO extends SuperVO implements Serializable {

	private String tituloCatalogo;
	private String subTituloCatalogo;
	private String editora;
	private Integer qtdeExemplar;
	private String anoPublicacao;
	private String nomeAutor;
	private String edicaoCatalogo;
	private String assunto;
	private String classificacao;
	private String cutterpha;
	private String tituloExemplar;
	private String codigoBarra;
	private String secao;
	private String anoVolume;
	private String mes;
	private String volume;
	private String situacao;
	private String codigoExemplar;
	private Boolean assinaturaPeriodico;
	private String tipoCatalogo;
	private Integer numeroExemplar;
	private String local;	
	private Integer codigoCatalogo;
	private String nomeResponsavelCriacao;
	private String nomeResponsavelAtualizacao;
	private String dataCriacaoCatalogo;
	private String dataAtualizacaoCatalogo;
	
	
//	Atributo Transient apenas para apresentar a situação do itemRegistroSaidaAcervoVO no relatório
	private String tipoSaida;

	public String getEditora() {
		if (editora == null) {
			editora = "";
		}
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public Integer getQtdeExemplar() {
		if (qtdeExemplar == null) {
			qtdeExemplar = 0;
		}
		return qtdeExemplar;
	}

	public void setQtdeExemplar(Integer qtdeExemplar) {
		this.qtdeExemplar = qtdeExemplar;
	}

	public String getSubTituloCatalogo() {
		if (subTituloCatalogo == null) {
			subTituloCatalogo = "";
		}
		return subTituloCatalogo;
	}

	public void setSubTituloCatalogo(String subTituloCatalogo) {
		this.subTituloCatalogo = subTituloCatalogo;
	}

	public String getTituloCatalogo() {
		if (tituloCatalogo == null) {
			tituloCatalogo = "";
		}
		return tituloCatalogo;
	}

	public void setTituloCatalogo(String tituloCatalogo) {
		this.tituloCatalogo = tituloCatalogo;
	}

	public String getAnoPublicacao() {
		if (anoPublicacao == null) {
			anoPublicacao = "";
		}
		return anoPublicacao;
	}

	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public String getNomeAutor() {
		if (nomeAutor == null) {
			nomeAutor = "";
		}
		return nomeAutor;
	}

	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}

	public String getEdicaoCatalogo() {
		if (edicaoCatalogo == null) {
			edicaoCatalogo = "";
		}
		return edicaoCatalogo;
	}

	public void setEdicaoCatalogo(String edicaoCatalogo) {
		this.edicaoCatalogo = edicaoCatalogo;
	}

	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getClassificacao() {
		if (classificacao == null) {
			classificacao = "";
		}
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getCutterpha() {
		if (cutterpha == null) {
			cutterpha = "";
		}
		return cutterpha;
	}

	public void setCutterpha(String cutterpha) {
		this.cutterpha = cutterpha;
	}

	public String getTituloExemplar() {
		if (tituloExemplar == null) {
			tituloExemplar = "";
		}
		return tituloExemplar;
	}

	public void setTituloExemplar(String tituloExemplar) {
		this.tituloExemplar = tituloExemplar;
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getSecao() {
		if (secao == null) {
			secao = "";
		}
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}

	public String getAnoVolume() {
		if(anoVolume == null){
			anoVolume = "";
		}
		return anoVolume;
	}

	public void setAnoVolume(String anoVolume) {
		this.anoVolume = anoVolume;
	}

	public String getMes() {
		if(mes == null){
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getVolume() {
		if (volume == null) {
			volume = "";
		}
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public String getSituacao_Apresentar() {
		return SituacaoExemplar.getDescricao(situacao);
	}

	public String getCodigoExemplar() {
		if (codigoExemplar == null) {
			codigoExemplar = "";
		}
		return codigoExemplar;
	}

	public void setCodigoExemplar(String codigoExemplar) {
		this.codigoExemplar = codigoExemplar;
	}

	public Boolean getAssinaturaPeriodico() {
		if (assinaturaPeriodico == null) {
			assinaturaPeriodico = Boolean.FALSE;
		}
		return assinaturaPeriodico;
	}

	public void setAssinaturaPeriodico(Boolean assinaturaPeriodico) {
		this.assinaturaPeriodico = assinaturaPeriodico;
	}

	public String getTipoCatalogo() {
		if (tipoCatalogo == null) {
			tipoCatalogo = "";
		}
		return tipoCatalogo;
	}

	public void setTipoCatalogo(String tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}

	/**
	 * @return the numeroExemplar
	 */
	public Integer getNumeroExemplar() {
		if (numeroExemplar == null) {
			numeroExemplar = 0;
		}
		return numeroExemplar;
	}

	/**
	 * @param numeroExemplar the numeroExemplar to set
	 */
	public void setNumeroExemplar(Integer numeroExemplar) {
		this.numeroExemplar = numeroExemplar;
	}

	public String getLocal() {
		if (local == null) {
			local = "";
		}
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getTipoSaida() {
		if (tipoSaida == null) {
			tipoSaida = "";
		}
		return TipoSaidaAcervo.getDescricao(tipoSaida);
	}

	public void setTipoSaida(String tipoSaida) {
		this.tipoSaida = tipoSaida;
	}

	public Integer getCodigoCatalogo() {
		if (codigoCatalogo == null) {
			codigoCatalogo = 0;
		}
		return codigoCatalogo;
	}
	
	public void setCodigoCatalogo(Integer codigoCatalogo) {
		this.codigoCatalogo = codigoCatalogo;
	}

	public String getNomeResponsavelCriacao() {
		if(nomeResponsavelCriacao == null) {
			nomeResponsavelCriacao = "";
		}
		return nomeResponsavelCriacao;
	}

	public void setNomeResponsavelCriacao(String nomeResponsavelCriacao) {
		this.nomeResponsavelCriacao = nomeResponsavelCriacao;
	}

	public String getNomeResponsavelAtualizacao() {
		if(nomeResponsavelAtualizacao == null) {
			nomeResponsavelAtualizacao = "";
		}
		return nomeResponsavelAtualizacao;
	}

	public void setNomeResponsavelAtualizacao(String nomeResponsavelAtualizacao) {
		this.nomeResponsavelAtualizacao = nomeResponsavelAtualizacao;
	}

	public String getDataCriacaoCatalogo() {
		if(dataCriacaoCatalogo ==null) {
			dataCriacaoCatalogo = "";
		}
		return dataCriacaoCatalogo;
	}

	public void setDataCriacaoCatalogo(String dataCriacaoCatalogo) {
		this.dataCriacaoCatalogo = dataCriacaoCatalogo;
	}

	public String getDataAtualizacaoCatalogo() {
		if(dataAtualizacaoCatalogo == null) {
			dataAtualizacaoCatalogo = "";
		}
		return dataAtualizacaoCatalogo;
	}

	public void setDataAtualizacaoCatalogo(String dataAtualizacaoCatalogo) {
		this.dataAtualizacaoCatalogo = dataAtualizacaoCatalogo;
	}
}
