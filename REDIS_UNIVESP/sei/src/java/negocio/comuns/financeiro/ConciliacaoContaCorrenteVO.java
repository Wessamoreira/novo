package negocio.comuns.financeiro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoConcialiacaoContaCorrenteEnum;
import negocio.comuns.utilitarias.Uteis;

public class ConciliacaoContaCorrenteVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3377718938394525659L;
	private Integer codigo;
	private SituacaoConcialiacaoContaCorrenteEnum situacaoConcialiacaoContaCorrenteEnum;
	private Date dataGeracao;
	private String nomeArquivo;
	private BancoVO banco;
	private String nomeContaCorrente;
	private String contaCorrenteArquivo;
	private String digitoContaCorrenteArquivo;
	private UsuarioVO responsavel;
	private List<ConciliacaoContaCorrenteDiaVO> listaConciliacaoContaCorrenteDia;
	private Date dataInicioSei;
	private Date dataFimSei;

	private Double totalValorOfxDebito;
	private Double totalValorOfxCredito;

	private Double totalValorSeiDebito;
	private Double totalValorSeiCredito;

	private Double totalValorOfx;	
	private Double totalValorSei;	
	private Double valorBalancoOfx;
	private ArquivoVO arquivoVO;
	/*private byte[] arquivo;*/

	/**
	 * Transient
	 */
	private ConciliacaoContaCorrenteDiaVO conciliacaoDiaSelecionada;
	private List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> listaParametrizarEntradaItens;
	private List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> listaParametrizarSaidaItens;
	private List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> listaParametrizarExcluida;
	private Double saldoRegistroOfxTemporario;
	private Double saldoRegistroSeiTemporario;
	boolean isUltimoRegistroDia = true;
	boolean isUltimoExtratoTransacaoDia = true;
	
	
	public void zeraTotalizadoresCreditoDebitoOFX(){
		setTotalValorOfxDebito(0.0);
		setTotalValorOfxCredito(0.0);
	}
	
	public void zeraTotalizadoresCreditoDebitoSEI(){		
		setTotalValorSeiDebito(0.0);
		setTotalValorSeiCredito(0.0);
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

	public ArquivoVO getArquivoVO() {
		if(arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public Date getDataGeracao() {
		if (dataGeracao == null) {
			dataGeracao = new Date();
		}
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}
	
	public Date getDataInicioSei() {
		if (dataInicioSei == null) {
			dataInicioSei = new Date();
		}
		return dataInicioSei;
	}

	public void setDataInicioSei(Date dataInicioSei) {
		this.dataInicioSei = dataInicioSei;
	}

	public Date getDataFimSei() {
		if (dataFimSei == null) {
			dataFimSei = new Date();
		}
		return dataFimSei;
	}

	public void setDataFimSei(Date dataFimSei) {
		this.dataFimSei = dataFimSei;
	}

	public String getPeriodoApuracao_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataInicioSei()) +" à "+Uteis.getDataAno4Digitos(getDataFimSei()) ;
	}
	
	public String getDataGeracao_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataGeracao());
	}
	

	public SituacaoConcialiacaoContaCorrenteEnum getSituacaoConcialiacaoContaCorrenteEnum() {
		if(situacaoConcialiacaoContaCorrenteEnum == null){
			situacaoConcialiacaoContaCorrenteEnum = SituacaoConcialiacaoContaCorrenteEnum.ABERTA;
		}
		return situacaoConcialiacaoContaCorrenteEnum;
	}

	public void setSituacaoConcialiacaoContaCorrenteEnum(SituacaoConcialiacaoContaCorrenteEnum situacaoConcialiacaoContaCorrenteEnum) {
		this.situacaoConcialiacaoContaCorrenteEnum = situacaoConcialiacaoContaCorrenteEnum;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}
	
	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public BancoVO getBanco() {
		if (banco == null) {
			banco = new BancoVO();
		}
		return banco;
	}

	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}	

	public List<ConciliacaoContaCorrenteDiaVO> getListaConciliacaoContaCorrenteDia() {
		if (listaConciliacaoContaCorrenteDia == null) {
			listaConciliacaoContaCorrenteDia = new ArrayList<>();
		}
		return listaConciliacaoContaCorrenteDia;
	}

	public void setListaConciliacaoContaCorrenteDia(List<ConciliacaoContaCorrenteDiaVO> listaConciliacaoContaCorrenteDia) {
		this.listaConciliacaoContaCorrenteDia = listaConciliacaoContaCorrenteDia;
	}
	
	public String getNomeContaCorrente() {
		if (nomeContaCorrente == null) {
			nomeContaCorrente = "";
		}
		return nomeContaCorrente;
	}

	public void setNomeContaCorrente(String nomeContaCorrente) {
		this.nomeContaCorrente = nomeContaCorrente;
	}

	public String getContaCorrenteArquivo() {
		if (contaCorrenteArquivo == null) {
			contaCorrenteArquivo = "";
		}
		return contaCorrenteArquivo;
	}

	public void setContaCorrenteArquivo(String contaCorrenteArquivo) {
		this.contaCorrenteArquivo = contaCorrenteArquivo;
	}

	public String getDigitoContaCorrenteArquivo() {
		if (digitoContaCorrenteArquivo == null) {
			digitoContaCorrenteArquivo = "";
		}
		return digitoContaCorrenteArquivo;
	}

	public void setDigitoContaCorrenteArquivo(String digitoContaCorrenteArquivo) {
		this.digitoContaCorrenteArquivo = digitoContaCorrenteArquivo;
	}
	
	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}	

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public File isValidarCodificacaoDoArquivo(File arquivo , String caminho, String codificacao) throws Exception {
			String extensao = (caminho.substring((caminho.lastIndexOf(".")), caminho.length()));
			File arquivoCodificado = new File(caminho.substring(0, caminho.lastIndexOf(".")) + "_Cod" + extensao);
			FileInputStream fis = new FileInputStream(arquivo);
			InputStreamReader isr = new InputStreamReader(fis, codificacao);// "ISO-8859-1"
			Reader in = new BufferedReader(isr);
			FileOutputStream fos = new FileOutputStream(arquivoCodificado);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			Writer out = new BufferedWriter(osw);
			int ch;
			while ((ch = in.read()) > -1) {
				out.write(ch);
			}
			out.flush();
			out.close();
			in.close();
			return arquivoCodificado;
		
/*		File arquivo = new File(caminho);
		if (arquivo.exists()) {
			ArquivoHelper.delete(arquivo);
		}
		if (getArquivoByte() != null) {
			FileOutputStream fileOutputStream = new FileOutputStream(caminho);
			fileOutputStream.write(getArquivoByte());
			fileOutputStream.close();
			String codificacao = ArquivoHelper.getDetectarCharset(arquivo);
			if (!codificacao.equals("UTF-8")) {
				String extensao = (caminho.substring((caminho.lastIndexOf(".")), caminho.length()));
				File arquivoCodificado = new File(caminho.substring(0, caminho.lastIndexOf(".")) + "_Cod" + extensao);
				FileInputStream fis = new FileInputStream(arquivo);
				InputStreamReader isr = new InputStreamReader(fis, codificacao);// "ISO-8859-1"
				Reader in = new BufferedReader(isr);
				FileOutputStream fos = new FileOutputStream(arquivoCodificado);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				Writer out = new BufferedWriter(osw);
				int ch;
				while ((ch = in.read()) > -1) {
					out.write(ch);
				}
				out.flush();
				out.close();
				in.close();
				ArquivoHelper.delete(arquivo);
				return arquivoCodificado;
			}
		}
		return arquivo;*/
	}

	

	/*public byte[] getArquivO() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}*/

	public ConciliacaoContaCorrenteDiaVO getConciliacaoDiaSelecionada() {
		if (conciliacaoDiaSelecionada == null) {
			conciliacaoDiaSelecionada = new ConciliacaoContaCorrenteDiaVO();
		}
		return conciliacaoDiaSelecionada;
	}

	public void setConciliacaoDiaSelecionada(ConciliacaoContaCorrenteDiaVO conciliacaoDiaSelecionada) {
		this.conciliacaoDiaSelecionada = conciliacaoDiaSelecionada;
	}

	public Integer getQuantidadeConciliacaoDia() {
		return getListaConciliacaoContaCorrenteDia().size();
	}

	public Double getTotalValorOfxDebito() {
		if (totalValorOfxDebito == null) {
			totalValorOfxDebito = 0.0;
		}
		return totalValorOfxDebito;
	}

	public void setTotalValorOfxDebito(Double totalValorOfxDebito) {
		this.totalValorOfxDebito = totalValorOfxDebito;
	}

	public Double getTotalValorOfxCredito() {
		if (totalValorOfxCredito == null) {
			totalValorOfxCredito = 0.0;
		}
		return totalValorOfxCredito;
	}

	public void setTotalValorOfxCredito(Double totalValorOfxCredito) {
		this.totalValorOfxCredito = totalValorOfxCredito;
	}

	public Double getTotalValorSeiDebito() {
		if (totalValorSeiDebito == null) {
			totalValorSeiDebito = 0.0;
		}
		return totalValorSeiDebito;
	}

	public void setTotalValorSeiDebito(Double totalValorSeiDebito) {
		this.totalValorSeiDebito = totalValorSeiDebito;
	}

	public Double getTotalValorSeiCredito() {
		if (totalValorSeiCredito == null) {
			totalValorSeiCredito = 0.0;
		}
		return totalValorSeiCredito;
	}

	public void setTotalValorSeiCredito(Double totalValorSeiCredito) {
		this.totalValorSeiCredito = totalValorSeiCredito;
	}

	public Double getTotalValorOfx() {
		if (totalValorOfx == null) {
			totalValorOfx = 0.0;
		}
		return totalValorOfx;
	}

	public void setTotalValorOfx(Double totalValorOfx) {
		this.totalValorOfx = totalValorOfx;
	}	

	public Double getValorBalancoOfx() {
		if (valorBalancoOfx == null) {
			valorBalancoOfx = 0.0;
		}
		return valorBalancoOfx;
	}

	public void setValorBalancoOfx(Double valorBalancoOfx) {
		this.valorBalancoOfx = valorBalancoOfx;
	}

	public Double getTotalValorSei() {
		if (totalValorSei == null) {
			totalValorSei = 0.0;
		}
		return totalValorSei;
	}

	public void setTotalValorSei(Double totalValorSei) {
		this.totalValorSei = totalValorSei;
	}

	public Double getSaldoRegistroOfxTemporario() {
		if (saldoRegistroOfxTemporario == null) {
			saldoRegistroOfxTemporario = 0.0;
		}
		return saldoRegistroOfxTemporario;
	}

	public void setSaldoRegistroOfxTemporario(Double saldoRegistroOfxTemporario) {
		this.saldoRegistroOfxTemporario = saldoRegistroOfxTemporario;
	}

	public Double getSaldoRegistroSeiTemporario() {
		if (saldoRegistroSeiTemporario == null) {
			saldoRegistroSeiTemporario = 0.0;
		}
		return saldoRegistroSeiTemporario;
	}

	public void setSaldoRegistroSeiTemporario(Double saldoRegistroSeiTemporario) {
		this.saldoRegistroSeiTemporario = saldoRegistroSeiTemporario;
	}

	public boolean isUltimoRegistroDia() {
		return isUltimoRegistroDia;
	}

	public void setUltimoRegistroDia(boolean isUltimoRegistroDia) {
		this.isUltimoRegistroDia = isUltimoRegistroDia;
	}

	public boolean isUltimoExtratoTransacaoDia() {
		return isUltimoExtratoTransacaoDia;
	}

	public void setUltimoExtratoTransacaoDia(boolean isUltimoExtratoTransacaoDia) {
		this.isUltimoExtratoTransacaoDia = isUltimoExtratoTransacaoDia;
	}		
	
	public List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> getListaParametrizarEntradaItens() {
		if (listaParametrizarEntradaItens == null) {
			listaParametrizarEntradaItens = new ArrayList<>();
		}
		return listaParametrizarEntradaItens;
	}

	public void setListaParametrizarEntradaItens(List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> listaParametrizarEntradaItens) {
		this.listaParametrizarEntradaItens = listaParametrizarEntradaItens;
	}

	public List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> getListaParametrizarSaidaItens() {
		if (listaParametrizarSaidaItens == null) {
			listaParametrizarSaidaItens = new ArrayList<>();
		}
		return listaParametrizarSaidaItens;
	}

	public void setListaParametrizarSaidaItens(List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> listaParametrizarSaidaItens) {
		this.listaParametrizarSaidaItens = listaParametrizarSaidaItens;
	}
	
	
	public List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> getListaParametrizarExcluida() {
		if (listaParametrizarExcluida == null) {
			listaParametrizarExcluida = new ArrayList<>();
		}
		return listaParametrizarExcluida;
	}

	public void setListaParametrizarExcluida(List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> listaParametrizarExcluida) {
		this.listaParametrizarExcluida = listaParametrizarExcluida;
	}

	public ParametrizarOperacoesAutomaticasConciliacaoItemVO buscarParametrizacaoOperacaoAutomatica(boolean isTipoTransacaoEntrada, String lancamentoTransacaoOfx) {
		if (isTipoTransacaoEntrada) {
			return getListaParametrizarEntradaItens().stream().filter(p->p.getNomeLancamento().equals(lancamentoTransacaoOfx))
			.findFirst()
			.orElse(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());					
		} else {
			return getListaParametrizarSaidaItens().stream().filter(p->p.getNomeLancamento().equals(lancamentoTransacaoOfx))
					.findFirst()
					.orElse(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
		
		}
	}
	
	public boolean isConciliacaoDepreciadaPorArquivo() {		
		return Uteis.isAtributoPreenchido(getCodigo()) && Uteis.isAtributoPreenchido(getNomeArquivo()) && !Uteis.isAtributoPreenchido(getArquivoVO().getNome()) ;
	}

}
