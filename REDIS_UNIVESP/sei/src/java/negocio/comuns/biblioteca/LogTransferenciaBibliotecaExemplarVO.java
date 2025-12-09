package negocio.comuns.biblioteca;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 *
 */
public class LogTransferenciaBibliotecaExemplarVO extends SuperVO {

	private Integer codigo;
	private ExemplarVO exemplar;
	private BibliotecaVO bibliotecaOrigem;
	private BibliotecaVO bibliotecaDestino;
	private UsuarioVO responsavel;
	private Date dataTransferencia;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ExemplarVO getExemplar() {
		if(exemplar == null){
			exemplar = new ExemplarVO();
		}
		return exemplar;
	}

	public void setExemplar(ExemplarVO exemplar) {
		this.exemplar = exemplar;
	}

	public BibliotecaVO getBibliotecaOrigem() {
		if(bibliotecaOrigem == null){
			bibliotecaOrigem = new BibliotecaVO();
		}
		return bibliotecaOrigem;
	}

	public void setBibliotecaOrigem(BibliotecaVO bibliotecaOrigem) {
		this.bibliotecaOrigem = bibliotecaOrigem;
	}

	public BibliotecaVO getBibliotecaDestino() {
		if(bibliotecaDestino == null){
			bibliotecaDestino = new BibliotecaVO();
		}
		return bibliotecaDestino;
	}

	public void setBibliotecaDestino(BibliotecaVO bibliotecaDestino) {
		this.bibliotecaDestino = bibliotecaDestino;
	}

	public UsuarioVO getResponsavel() {
		if(responsavel == null){
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataTransferencia() {
		if(dataTransferencia == null){
			dataTransferencia = new Date();
		}
		return dataTransferencia;
	}

	public void setDataTransferencia(Date dataTransferencia) {
		this.dataTransferencia = dataTransferencia;
	}
	
	public String getDataEmissao_Apresentar() {
		return (UteisData.getDataComHora(dataTransferencia));
	}
	
	
}