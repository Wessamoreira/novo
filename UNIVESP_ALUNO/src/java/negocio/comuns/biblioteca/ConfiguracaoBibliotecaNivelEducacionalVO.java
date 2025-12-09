package negocio.comuns.biblioteca;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * 
 * @author Leonardo Riciolle Criado 03/12/2014 Descrição: Cadastro usado para
 *         criar uma lista de configuracoes de acordo com cada configuração da
 *         biblioteca.
 *
 */
public class ConfiguracaoBibliotecaNivelEducacionalVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer biblioteca;
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String nivelEducacional;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getBiblioteca() {
		if (biblioteca == null) {
			biblioteca = 0;
		}
		return biblioteca;
	}

	public void setBiblioteca(Integer biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaVO() {
		if (configuracaoBibliotecaVO == null) {
			configuracaoBibliotecaVO = new ConfiguracaoBibliotecaVO();
		}
		return configuracaoBibliotecaVO;
	}

	public void setConfiguracaoBibliotecaVO(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) {
		this.configuracaoBibliotecaVO = configuracaoBibliotecaVO;
	}
	
	public String getTipoNivelEducacional_Apresentar() {
		if (getNivelEducacional() == null || getNivelEducacional().equals("0")) {
			return "";
		}
		return TipoNivelEducacional.getDescricao(getNivelEducacional());
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
}
