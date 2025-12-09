package negocio.comuns.utilitarias;

public class TipoFiltroConsulta {

		private TipoFiltroConsultaEnum campoConsulta;
		private String labelConsulta;
		private Object keyConsulta;
		private Integer quantidade;
		private Boolean selecionado;
		
		
		public TipoFiltroConsulta(TipoFiltroConsultaEnum campoConsulta, String labelConsulta, Object keyConsulta) {
			super();
			this.campoConsulta = campoConsulta;
			this.labelConsulta = labelConsulta;
			this.keyConsulta = keyConsulta;
		}
		
		
		public TipoFiltroConsulta(TipoFiltroConsultaEnum campoConsulta, String labelConsulta, Object keyConsulta,
				Integer quantidade) {
			super();
			this.campoConsulta = campoConsulta;
			this.labelConsulta = labelConsulta;
			this.keyConsulta = keyConsulta;
			this.quantidade = quantidade;
		}


		public TipoFiltroConsultaEnum getCampoConsulta() {
			return campoConsulta;
		}
		public void setCampoConsulta(TipoFiltroConsultaEnum campoConsulta) {
			this.campoConsulta = campoConsulta;
		}
		public String getLabelConsulta() {
			return labelConsulta;
		}
		public void setLabelConsulta(String labelConsulta) {
			this.labelConsulta = labelConsulta;
		}
		public Object getKeyConsulta() {
			return keyConsulta;
		}
		public void setKeyConsulta(Object keyConsulta) {
			this.keyConsulta = keyConsulta;
		}


		public Integer getQuantidade() {
			return quantidade;
		}


		public void setQuantidade(Integer quantidade) {
			this.quantidade = quantidade;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((campoConsulta == null) ? 0 : campoConsulta.hashCode());
			result = prime * result + ((keyConsulta == null) ? 0 : keyConsulta.hashCode());
			result = prime * result + ((labelConsulta == null) ? 0 : labelConsulta.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TipoFiltroConsulta other = (TipoFiltroConsulta) obj;
			if (campoConsulta != other.campoConsulta)
				return false;
			if (keyConsulta == null) {
				if (other.keyConsulta != null)
					return false;
			} else if (!keyConsulta.equals(other.keyConsulta))
				return false;
			if (labelConsulta == null) {
				if (other.labelConsulta != null)
					return false;
			} else if (!labelConsulta.equals(other.labelConsulta))
				return false;
			return true;
		}


		public Boolean getSelecionado() {
			if(selecionado == null) {
				selecionado = false;
			}
			return selecionado;
		}


		public void setSelecionado(Boolean selecionado) {
			this.selecionado = selecionado;
		}
		
		
		
}
