package webservice.pix.comuns;

public class ValorCobrancaComVencimentoPixRSVO {

	
	/**
	 * pattern: \d{1,10}\.\d{2}
	 */
	private String original;
	private MultaPixRSVO multa;
	private JuroPixRSVO juro;
	private AbatimentoPixRSVO abatimento;
	private DescontoPixRSVO desconto;

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public MultaPixRSVO getMulta() {
		return multa;
	}

	public void setMulta(MultaPixRSVO multa) {
		this.multa = multa;
	}

	public JuroPixRSVO getJuro() {
		return juro;
	}

	public void setJuro(JuroPixRSVO juro) {
		this.juro = juro;
	}

	public AbatimentoPixRSVO getAbatimento() {
		return abatimento;
	}

	public void setAbatimento(AbatimentoPixRSVO abatimento) {
		this.abatimento = abatimento;
	}

	public DescontoPixRSVO getDesconto() {
		return desconto;
	}

	public void setDesconto(DescontoPixRSVO desconto) {
		this.desconto = desconto;
	}
	
	
	

}
