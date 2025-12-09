package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

public class DocumentacaoGEDVO extends SuperVO {

	private static final long serialVersionUID = 7136728539666302746L;

	private Integer codigo;
	private CategoriaGEDVO categoriaGED;
	private ArquivoVO arquivo;
	private MatriculaVO matricula;
	private PessoaVO pessoa;
	private String situacao;
	private String mensagem;
	private List<TipoDocumentoGEDVO> listaTipoDocumentoGED;
	private UsuarioVO usuario;
	private Date data;
	private Boolean pessoaNaoEncontrada;
	private Boolean categoriaGedNaoEncontrada;	
	
	 /**
     * Atributo responsável por verificar se foi realizado um novo upload de arquivo. 
     *  Caso verdadeiro e houver configuração GED, será assinado digitalemnte
     */
	private Boolean uploadNovoArquivo;
	private String descricaoprocessamento;
	private Boolean processadocomerro;
	private String identificadorged;
	private Date dataProcessamento;
	
	private String lote;
	
	
	public DocumentacaoGEDVO getClone() throws CloneNotSupportedException {
		DocumentacaoGEDVO documentacaoGEDVO = (DocumentacaoGEDVO) super.clone();
		documentacaoGEDVO.setArquivo(getArquivo().clones());
		documentacaoGEDVO.setCategoriaGED((CategoriaGEDVO)getCategoriaGED().clone());
		documentacaoGEDVO.setUsuario((UsuarioVO)getUsuario().clone());
		documentacaoGEDVO.setMatricula((MatriculaVO)getMatricula().clone());
		documentacaoGEDVO.setListaTipoDocumentoGED(new ArrayList<TipoDocumentoGEDVO>(0));
		documentacaoGEDVO.getListaTipoDocumentoGED().addAll(getListaTipoDocumentoGED());
		return documentacaoGEDVO;
	}
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CategoriaGEDVO getCategoriaGED() {
		if (categoriaGED == null) {
			categoriaGED = new CategoriaGEDVO();
		}
		return categoriaGED;
	}

	public void setCategoriaGED(CategoriaGEDVO categoriaGED) {
		this.categoriaGED = categoriaGED;
	}

	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public List<TipoDocumentoGEDVO> getListaTipoDocumentoGED() {
		if (listaTipoDocumentoGED == null) {
			listaTipoDocumentoGED = new ArrayList<>();
		}
		return listaTipoDocumentoGED;
	}

	public void setListaTipoDocumentoGED(List<TipoDocumentoGEDVO> listaTipoDocumentoGED) {
		this.listaTipoDocumentoGED = listaTipoDocumentoGED;
	}

	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getPessoaNaoEncontrada() {
		if (pessoaNaoEncontrada == null) {
			pessoaNaoEncontrada = false;
		}
		return pessoaNaoEncontrada;
	}

	public void setPessoaNaoEncontrada(Boolean pessoaNaoEncontrada) {
		this.pessoaNaoEncontrada = pessoaNaoEncontrada;
	}

	public Boolean getCategoriaGedNaoEncontrada() {
		if (categoriaGedNaoEncontrada == null) {
			categoriaGedNaoEncontrada = false;
		}
		return categoriaGedNaoEncontrada;
	}

	public void setCategoriaGedNaoEncontrada(Boolean categoriaGedNaoEncontrada) {
		this.categoriaGedNaoEncontrada = categoriaGedNaoEncontrada;
	}

	public Boolean getUploadNovoArquivo() {
		if(uploadNovoArquivo == null) {
			uploadNovoArquivo = true;
		}
		return uploadNovoArquivo;
	}

	public void setUploadNovoArquivo(Boolean uploadNovoArquivo) {
		this.uploadNovoArquivo = uploadNovoArquivo;
	}

	public String getDescricaoprocessamento() {
		if(descricaoprocessamento == null) {
			descricaoprocessamento = "";
		}
		return descricaoprocessamento;
	}

	public void setDescricaoprocessamento(String descricaoprocessamento) {
		this.descricaoprocessamento = descricaoprocessamento;
	}

	public Boolean getProcessadocomerro() {
		if(processadocomerro == null) {
			processadocomerro = false;
		}
		return processadocomerro;
	}

	public void setProcessadocomerro(Boolean processadocomerro) {
		this.processadocomerro = processadocomerro;
	}

	public String getIdentificadorged() {
		if(identificadorged == null) {
			identificadorged = "";
		}
		return identificadorged;
	}

	public void setIdentificadorged(String identificadorged) {
		this.identificadorged = identificadorged;
	}
	
	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}
	
	public String getData_Apresentar() {
	     return (Uteis.getDataComHora(dataProcessamento));
	}

	public String getLote() {
		if (lote == null) {
			lote = "";
		}
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}
	
	public String getIdDocumentacao() {
		return new StringBuilder("DG").append(Uteis.isAtributoPreenchido(getCodigo()) ? getCodigo() : Uteis.isAtributoPreenchido(getListaTipoDocumentoGED()) && getListaTipoDocumentoGED().stream().anyMatch(ged -> Uteis.isAtributoPreenchido(ged.getDocumetacaoMatricula())) ? getListaTipoDocumentoGED().stream().filter(ged -> Uteis.isAtributoPreenchido(ged.getDocumetacaoMatricula())).map(ged -> ged.getDocumetacaoMatricula().getCodigo().toString()).collect(Collectors.joining(Constantes.EMPTY)) : Uteis.isAtributoPreenchido(getCategoriaGED()) ? getCategoriaGED().getCodigo().toString().concat(getMatricula().getMatricula().replaceAll("/", "_").replaceAll("\"", "_")) : getMatricula().getMatricula().replaceAll("/", "_").replaceAll("\"", "_")).toString();
	}
}