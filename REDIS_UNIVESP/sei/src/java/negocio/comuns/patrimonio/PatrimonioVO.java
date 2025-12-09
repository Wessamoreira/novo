package negocio.comuns.patrimonio;
/**
 * 
 * @author Leonardo Riciolle
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.patrimonio.enumeradores.FormaEntradaPatrimonioEnum;

public class PatrimonioVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private String modelo;
	private String marca;	
	private TipoPatrimonioVO tipoPatrimonioVO;
	private Date dataCadastro;
	private UsuarioVO responsavel;
	private FornecedorVO fornecedorVO;
	private FormaEntradaPatrimonioEnum formaEntradaPatrimonio;
	private Date dataEntrada;
	private String notaFiscal;
	private CompraVO compraVO;	
	private List<PatrimonioUnidadeVO> patrimonioUnidadeVOs;
	private List<ArquivoVO> declaracaoEntregaPatrimonioVOs;
	//Transiente - Usando no momento de popular o grafico da tela de NOVAS Ocorrencias
	private List<OcorrenciaPatrimonioVO> listaOcorrencias;
		

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getModelo() {
		if (modelo == null) {
			modelo = "";
		}
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		if (marca == null) {
			marca = "";
		}
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	
	public TipoPatrimonioVO getTipoPatrimonioVO() {
		if (tipoPatrimonioVO == null) {
			tipoPatrimonioVO = new TipoPatrimonioVO();
		}
		return tipoPatrimonioVO;
	}

	public void setTipoPatrimonioVO(TipoPatrimonioVO tipoPatrimonioVO) {
		this.tipoPatrimonioVO = tipoPatrimonioVO;
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

	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeVOs() {
		if (patrimonioUnidadeVOs == null) {
			patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		}
		return patrimonioUnidadeVOs;
	}

	public void setPatrimonioUnidadeVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) {
		this.patrimonioUnidadeVOs = patrimonioUnidadeVOs;
	}
	
	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public FormaEntradaPatrimonioEnum getFormaEntradaPatrimonio() {
		if (formaEntradaPatrimonio == null) {
			formaEntradaPatrimonio = FormaEntradaPatrimonioEnum.COMPRA;
		}
		return formaEntradaPatrimonio;
	}

	public void setFormaEntradaPatrimonio(FormaEntradaPatrimonioEnum formaEntradaPatrimonio) {
		this.formaEntradaPatrimonio = formaEntradaPatrimonio;
	}

	public Date getDataEntrada() {		
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getNotaFiscal() {
		if (notaFiscal == null) {
			notaFiscal = "";
		}
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public CompraVO getCompraVO() {
		if (compraVO == null) {
			compraVO = new CompraVO();
		}
		return compraVO;
	}

	public void setCompraVO(CompraVO compraVO) {
		this.compraVO = compraVO;
	}

	/**
	 * @return the declaracaoEntregaPatrimonioVOs
	 */
	public List<ArquivoVO> getDeclaracaoEntregaPatrimonioVOs() {
		if (declaracaoEntregaPatrimonioVOs == null) {
			declaracaoEntregaPatrimonioVOs = new ArrayList<ArquivoVO>(0);
		}
		return declaracaoEntregaPatrimonioVOs;
	}

	/**
	 * @param declaracaoEntregaPatrimonioVOs the declaracaoEntregaPatrimonioVOs to set
	 */
	public void setDeclaracaoEntregaPatrimonioVOs(List<ArquivoVO> declaracaoEntregaPatrimonioVOs) {
		this.declaracaoEntregaPatrimonioVOs = declaracaoEntregaPatrimonioVOs;
	}
	
	public List<OcorrenciaPatrimonioVO> getListaOcorrencias() {
		if(listaOcorrencias == null){
			listaOcorrencias = new ArrayList<OcorrenciaPatrimonioVO>(0);
		}
		return listaOcorrencias;
	}

	public void setListaOcorrencias(List<OcorrenciaPatrimonioVO> listaOcorrencias) {
		this.listaOcorrencias = listaOcorrencias;
	}
	

}
