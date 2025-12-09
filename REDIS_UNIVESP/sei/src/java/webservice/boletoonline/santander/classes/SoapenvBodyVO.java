package webservice.boletoonline.santander.classes;

public class SoapenvBodyVO {

	public ImplCreateVO implCreate;

	public SoapenvBodyVO () {
		setImplCreate(new ImplCreateVO());
	}
	
	public ImplCreateVO getImplCreate() {
		return implCreate;
	}

	public void setImplCreate(ImplCreateVO implCreate) {
		this.implCreate = implCreate;
	}

}
