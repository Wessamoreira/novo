package negocio.comuns.blackboard;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

@SuppressWarnings("serial")
public class BlackboardGestaoFechamentoNotaVO extends SuperVO {

	private List<ConfiguracaoAcademicaFechamentoNotaVO> configuracaoAcademicaFechamentoNotaVOs;
	private Boolean realizarCalculoNota;

	public List<ConfiguracaoAcademicaFechamentoNotaVO> getConfiguracaoAcademicaFechamentoNotaVOs() {
		if (configuracaoAcademicaFechamentoNotaVOs == null) {
			configuracaoAcademicaFechamentoNotaVOs = new ArrayList<>(0);
		}
		return configuracaoAcademicaFechamentoNotaVOs;
	}

	public void setConfiguracaoAcademicaFechamentoNotaVOs(List<ConfiguracaoAcademicaFechamentoNotaVO> configuracaoAcademicaFechamentoNotaVOs) {
		this.configuracaoAcademicaFechamentoNotaVOs = configuracaoAcademicaFechamentoNotaVOs;
	}

	public Boolean getRealizarCalculoNota() {
		if (realizarCalculoNota == null) {
			realizarCalculoNota = Boolean.FALSE;
		}
		return realizarCalculoNota;
	}

	public void setRealizarCalculoNota(Boolean realizarCalculoNota) {
		this.realizarCalculoNota = realizarCalculoNota;
	}

	public static class NotaFechamentoNotaVO {
		private String tituloApresentar;
		private Boolean selecionado;
		private Integer nota;
		private Integer configuracaoAcademico;

		public String getTituloApresentar() {
			if (tituloApresentar == null) {
				tituloApresentar = Constantes.EMPTY;
			}
			return tituloApresentar;
		}

		public void setTituloApresentar(String tituloApresentar) {
			this.tituloApresentar = tituloApresentar;
		}

		public Boolean getSelecionado() {
			if (selecionado == null) {
				selecionado = Boolean.FALSE;
			}
			return selecionado;
		}

		public void setSelecionado(Boolean selecionado) {
			this.selecionado = selecionado;
		}

		public Integer getNota() {
			if (nota == null) {
				nota = 0;
			}
			return nota;
		}

		public void setNota(Integer nota) {
			this.nota = nota;
		}

		public Integer getConfiguracaoAcademico() {
			if (configuracaoAcademico == null) {
				configuracaoAcademico = 0;
			}
			return configuracaoAcademico;
		}

		public void setConfiguracaoAcademico(Integer configuracaoAcademico) {
			this.configuracaoAcademico = configuracaoAcademico;
		}
	}

	public static class ConfiguracaoAcademicaFechamentoNotaVO {
		private ConfiguracaoAcademicoVO configuracaoAcademico;
		private List<NotaFechamentoNotaVO> listaNotaFechamentoNotaVO;
		private Boolean marcarTodosNotas;
		private Boolean apresentarListaNotas;

		public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
			if (configuracaoAcademico == null) {
				configuracaoAcademico = new ConfiguracaoAcademicoVO();
			}
			return configuracaoAcademico;
		}

		public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
			this.configuracaoAcademico = configuracaoAcademico;
		}

		public List<NotaFechamentoNotaVO> getListaNotaFechamentoNotaVO() {
			if (listaNotaFechamentoNotaVO == null) {
				listaNotaFechamentoNotaVO = new ArrayList<>(0);
			}
			return listaNotaFechamentoNotaVO;
		}

		public void setListaNotaFechamentoNotaVO(List<NotaFechamentoNotaVO> listaNotaFechamentoNotaVO) {
			this.listaNotaFechamentoNotaVO = listaNotaFechamentoNotaVO;
		}
		
		public Boolean getMarcarTodosNotas() {
			if (marcarTodosNotas == null) {
				marcarTodosNotas = Boolean.FALSE;
			}
			return marcarTodosNotas;
		}
		
		public void setMarcarTodosNotas(Boolean marcarTodosNotas) {
			this.marcarTodosNotas = marcarTodosNotas;
		}
		
		public Boolean getApresentarListaNotas() {
			if (apresentarListaNotas == null) {
				apresentarListaNotas = Boolean.FALSE;
			}
			return apresentarListaNotas;
		}
		
		public void setApresentarListaNotas(Boolean apresentarListaNotas) {
			this.apresentarListaNotas = apresentarListaNotas;
		}
		
		public void realizarMarcarTodosNotas() {
			if (Uteis.isAtributoPreenchido(getListaNotaFechamentoNotaVO())) {
				getListaNotaFechamentoNotaVO().stream().forEach(n -> n.setSelecionado(getMarcarTodosNotas()));
			}
		}
	}
}
