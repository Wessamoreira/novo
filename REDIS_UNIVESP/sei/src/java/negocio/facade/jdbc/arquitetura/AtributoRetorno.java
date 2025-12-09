package negocio.facade.jdbc.arquitetura;

import java.util.ArrayList;

public class AtributoRetorno  extends ArrayList<AtributoRetorno.Retorno> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -50921949930785220L;

	public AtributoRetorno add(String campoBase, String apelidoRetorno, String atributoJava, boolean usarComoOrdenador) {
		this.add(new AtributoRetorno.Retorno(campoBase, apelidoRetorno, atributoJava, false));
		return this;
	};
	
	public AtributoRetorno add(String campoBase, String apelidoRetorno, String atributoJava) {
		this.add(new AtributoRetorno.Retorno(campoBase, apelidoRetorno, atributoJava, false));
		return this;
	};
	
	public AtributoRetorno add(String campoBase, String atributoJava) {
		this.add(new AtributoRetorno.Retorno(campoBase, campoBase, atributoJava, false));
		return this;
	};
	
	class Retorno{
		private String campoBase;
		private String apelidoRetorno;
		private String atributoJava;	
		private boolean usarComoOrdenador =  false;
		
		public Retorno(String campoBase, String apelidoRetorno, String atributoJava, boolean usarComoOrdenador) {
			super();
			this.campoBase = campoBase;
			this.apelidoRetorno = apelidoRetorno;
			this.atributoJava = atributoJava;
			this.usarComoOrdenador = usarComoOrdenador;
		}
		
		
		public String getCampoBase() {
			if(campoBase == null){
				campoBase = "";
			}
			return campoBase;
		}
		public void setCampoBase(String campoBase) {
			this.campoBase = campoBase;
		}
		public String getApelidoRetorno() {
			if(apelidoRetorno == null){
				apelidoRetorno = "";
			}
			return apelidoRetorno;
		}
		public void setApelidoRetorno(String apelidoRetorno) {
			this.apelidoRetorno = apelidoRetorno;
		}
		public String getAtributoJava() {
			if(atributoJava == null){
				atributoJava = "";
			}
			return atributoJava;
		}
		public void setAtributoJava(String atributoJava) {
			this.atributoJava = atributoJava;
		}


		public boolean isUsarComoOrdenador() {			
			return usarComoOrdenador;
		}


		public void setUsarComoOrdenador(boolean usarComoOrdenador) {
			this.usarComoOrdenador = usarComoOrdenador;
		}
		
		
	};
}
