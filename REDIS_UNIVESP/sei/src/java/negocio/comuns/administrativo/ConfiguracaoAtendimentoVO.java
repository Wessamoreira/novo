package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro
 */
public class ConfiguracaoAtendimentoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private UsuarioVO responsavelCadastro;
	private Date dataCadastro;

	// DADOS ATENDIMENTO A OUVIDORIA
	private Integer numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa;
	private Integer tempoMaximoParaResponderCadaInteracaoEntreDepartamentos;
	private Integer tempoMaximoParaRespostaOuvidoriaPeloOuvidor;
	private GrupoDestinatariosVO grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo;
	private GrupoDestinatariosVO grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada;
	private String textoParaOrientacaoOuvidoria;
	private List<ConfiguracaoAtendimentoUnidadeEnsinoVO> listaConfiguracaoAtendimentoUnidadeEnsinoVOs;
	private List<ConfiguracaoAtendimentoFuncionarioVO> listaConfiguracaoAtendimentoFuncionarioVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public String getDataCadastro_Apresentar() {		
		return Uteis.getData(getDataCadastro());
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

	public Integer getNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa() {
		if (numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa == null) {
			numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa = 0;
		}
		return numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa;
	}

	public void setNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa(Integer numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa) {
		this.numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa = numeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa;
	}

	public Integer getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos() {
		if (tempoMaximoParaResponderCadaInteracaoEntreDepartamentos == null) {
			tempoMaximoParaResponderCadaInteracaoEntreDepartamentos = 0;
		}
		return tempoMaximoParaResponderCadaInteracaoEntreDepartamentos;
	}

	public void setTempoMaximoParaResponderCadaInteracaoEntreDepartamentos(Integer tempoMaximoParaResponderCadaInteracaoEntreDepartamentos) {
		this.tempoMaximoParaResponderCadaInteracaoEntreDepartamentos = tempoMaximoParaResponderCadaInteracaoEntreDepartamentos;
	}

	public Integer getTempoMaximoParaRespostaOuvidoriaPeloOuvidor() {
		if (tempoMaximoParaRespostaOuvidoriaPeloOuvidor == null) {
			tempoMaximoParaRespostaOuvidoriaPeloOuvidor = 0;
		}
		return tempoMaximoParaRespostaOuvidoriaPeloOuvidor;
	}

	public void setTempoMaximoParaRespostaOuvidoriaPeloOuvidor(Integer tempoMaximoParaRespostaOuvidoriaPeloOuvidor) {
		this.tempoMaximoParaRespostaOuvidoriaPeloOuvidor = tempoMaximoParaRespostaOuvidoriaPeloOuvidor;
	}

	public GrupoDestinatariosVO getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo() {
		if (grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo == null) {
			grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo = new GrupoDestinatariosVO();
		}
		return grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo;
	}

	public void setGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo(GrupoDestinatariosVO grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo) {
		grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo = grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo;
	}

	public GrupoDestinatariosVO getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada() {
		if (grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada == null) {
			grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada = new GrupoDestinatariosVO();
		}
		return grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada;
	}

	public void setGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada(GrupoDestinatariosVO grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada) {
		grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada = grupoDestinatarioEnviarNotificacaoQuandoOuvidoriaForMalAvaliada;
	}

	public String getTextoParaOrientacaoOuvidoria() {
		if (textoParaOrientacaoOuvidoria == null) {
			textoParaOrientacaoOuvidoria = "";
		}
		return textoParaOrientacaoOuvidoria;
	}

	public void setTextoParaOrientacaoOuvidoria(String textoParaOrientacaoOuvidoria) {
		this.textoParaOrientacaoOuvidoria = textoParaOrientacaoOuvidoria;
	}

	public List<ConfiguracaoAtendimentoUnidadeEnsinoVO> getListaConfiguracaoAtendimentoUnidadeEnsinoVOs() {
		if (listaConfiguracaoAtendimentoUnidadeEnsinoVOs == null) {
			listaConfiguracaoAtendimentoUnidadeEnsinoVOs = new ArrayList<ConfiguracaoAtendimentoUnidadeEnsinoVO>();
		}
		return listaConfiguracaoAtendimentoUnidadeEnsinoVOs;
	}

	public void setListaConfiguracaoAtendimentoUnidadeEnsinoVOs(List<ConfiguracaoAtendimentoUnidadeEnsinoVO> listaConfiguracaoAtendimentoUnidadeEnsinoVOs) {
		this.listaConfiguracaoAtendimentoUnidadeEnsinoVOs = listaConfiguracaoAtendimentoUnidadeEnsinoVOs;
	}

	public List<ConfiguracaoAtendimentoFuncionarioVO> getListaConfiguracaoAtendimentoFuncionarioVOs() {
		if (listaConfiguracaoAtendimentoFuncionarioVOs == null) {
			listaConfiguracaoAtendimentoFuncionarioVOs = new ArrayList<ConfiguracaoAtendimentoFuncionarioVO>();
		}
		return listaConfiguracaoAtendimentoFuncionarioVOs;
	}

	public void setListaConfiguracaoAtendimentoFuncionarioVOs(List<ConfiguracaoAtendimentoFuncionarioVO> listaConfiguracaoAtendimentoFuncionarioVOs) {
		this.listaConfiguracaoAtendimentoFuncionarioVOs = listaConfiguracaoAtendimentoFuncionarioVOs;
	}
	
	public String getListaCodigoUnidadeEnsino() {
		StringBuilder listaCodigo = new StringBuilder("");
		boolean adicionou = false;
		for (ConfiguracaoAtendimentoUnidadeEnsinoVO obj : getListaConfiguracaoAtendimentoUnidadeEnsinoVOs()) {
			if(adicionou){
				listaCodigo.append(", ").append(obj.getUnidadeEnsinoVO().getCodigo());
			} else{
				listaCodigo.append(obj.getUnidadeEnsinoVO().getCodigo());
				adicionou = true;
			}	        			
		}	
		return listaCodigo.toString();
	}
	
	public Boolean getApresentarTextoParaOrientacaoOuvidoria(){
		if((Uteis.retiraTags(getTextoParaOrientacaoOuvidoria()).replaceAll("Untitled document", "").trim().isEmpty())){
			return false;
		}
		return true;
	}

}
