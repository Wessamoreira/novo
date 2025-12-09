package webservice.nfse.generic;

public enum TributacaoEnum {
	ISENTA_ISS("C", "Isenta de ISS"), NAO_INCIDENCIA_MUNICIPIO("E", "Não Incidência no Município"), IMUNE("F", "Imune"), EXIGIBILIDD_SUSP_DEC_JPROC_A("K", "Exigibilidd Susp.Dec.J/Proc.A"), NAO_TRIBUTAVEL("N", "Não Tributável"), TRIBUTAVEL("T", "Tributável"), TRIBUTAVEL_FIXO("G", "Tributável Fixo"), TRIBUTAVEL_SN("H", "Tributável S.N."), MICRO_EMPREENDEDOR_INDIVIDUAL("M", "Micro Empreendedor Individual (MEI)");
	private String id;
	private String nome;

	TributacaoEnum(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
}
