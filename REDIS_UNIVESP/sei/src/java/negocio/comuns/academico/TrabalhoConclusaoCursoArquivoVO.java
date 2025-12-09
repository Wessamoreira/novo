package negocio.comuns.academico;

import java.io.File;
import java.util.Date;

import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.TipoArquivoTCCEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

public class TrabalhoConclusaoCursoArquivoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3117920038451019992L;
	private Integer codigo;
	private TrabalhoConclusaoCursoVO trabalhoConclusaoCurso;
	private Date dataPostagem;
	private Boolean enviadoPeloAluno;
	private UsuarioVO responsavel;
	private TipoArquivoTCCEnum tipoArquivoTCC;
	private EtapaTCCEnum etapaTCC;
	private String nomeArquivo;
	private String nomeArquivoOriginal;	
	

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public Date getDataPostagem() {
		if(dataPostagem == null){
			dataPostagem = new Date();
		}
		return dataPostagem;
	}

	public void setDataPostagem(Date dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public Boolean getEnviadoPeloAluno() {
		if(enviadoPeloAluno == null){
			enviadoPeloAluno = false;
		}
		return enviadoPeloAluno;
	}

	public void setEnviadoPeloAluno(Boolean enviadoPeloAluno) {
		this.enviadoPeloAluno = enviadoPeloAluno;
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

	public TipoArquivoTCCEnum getTipoArquivoTCC() {
		if(tipoArquivoTCC == null){
			tipoArquivoTCC = TipoArquivoTCCEnum.ARQUIVO_TCC;
		}
		return tipoArquivoTCC;
	}

	public void setTipoArquivoTCC(TipoArquivoTCCEnum tipoArquivoTCC) {
		this.tipoArquivoTCC = tipoArquivoTCC;
	}

	public EtapaTCCEnum getEtapaTCC() {

		return etapaTCC;
	}

	public void setEtapaTCC(EtapaTCCEnum etapaTCC) {
		this.etapaTCC = etapaTCC;
	}

	public String getNomeArquivo() {
		if(nomeArquivo == null){
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getNomeArquivoOriginal() {
		if(nomeArquivoOriginal == null){
			nomeArquivoOriginal = "";
		}
		return nomeArquivoOriginal;
	}

	public void setNomeArquivoOriginal(String nomeArquivoOriginal) {
		this.nomeArquivoOriginal = nomeArquivoOriginal;
	}

	public String getCaminhoBaseWebArquivo() {		
		return PastaBaseArquivoEnum.TCC.getValue()+"/"+getTrabalhoConclusaoCurso().getCodigo()+"/"+getNomeArquivo();
	}
	
	public String getCaminhoBaseFisicoArquivo() {		
		return PastaBaseArquivoEnum.TCC.getValue()+File.separator+getTrabalhoConclusaoCurso().getCodigo()+File.separator+getNomeArquivo();
	}

	
	
	
	

}
