package negocio.comuns.job;

public class RegistroExecucaoJobTotaisVO {

	private int totalRotinasExecutadas;
	private int totalEmailsEncontrados;
	private int totalEmailsEnviados;
	private int totalEmailsErro;
	
	public int getTotalRotinasExecutadas() {
		return totalRotinasExecutadas;
	}
	
	public void setTotalRotinasExecutadas(int totalRotinasExecutadas) {
		this.totalRotinasExecutadas = totalRotinasExecutadas;
	}
	
	public int getTotalEmailsEncontrados() {
		return totalEmailsEncontrados;
	}
	
	public void setTotalEmailsEncontrados(int totalEmailsEncontrados) {
		this.totalEmailsEncontrados = totalEmailsEncontrados;
	}
	
	public int getTotalEmailsEnviados() {
		return totalEmailsEnviados;
	}
	
	public void setTotalEmailsEnviados(int totalEmailsEnviados) {
		this.totalEmailsEnviados = totalEmailsEnviados;
	}
	
	public int getTotalEmailsErro() {
		return totalEmailsErro;
	}
	
	public void setTotalEmailsErro(int totalEmailsErro) {
		this.totalEmailsErro = totalEmailsErro;
	}

}
