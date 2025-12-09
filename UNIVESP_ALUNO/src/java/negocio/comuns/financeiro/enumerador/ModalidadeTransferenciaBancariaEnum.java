package negocio.comuns.financeiro.enumerador;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum ModalidadeTransferenciaBancariaEnum {
	
	DOC, TED,PIX;
	
	public boolean isDoc(){
		return equals(ModalidadeTransferenciaBancariaEnum.DOC);
	}
	
	public boolean isTed(){
		return equals(ModalidadeTransferenciaBancariaEnum.TED);
	}
	
	public boolean isPix(){
		return equals(ModalidadeTransferenciaBancariaEnum.PIX);
	}
}
