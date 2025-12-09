@Autowired
	private PessoaEADIPOGInterfaceFacade pessoaEADIPOGFacade;
	
	public PessoaEADIPOGInterfaceFacade getPessoaEADIPOGFacade() {
		return pessoaEADIPOGFacade;
	}

	public void setPessoaEADIPOGFacade(PessoaEADIPOGInterfaceFacade pessoaEADIPOGFacade) {
		this.pessoaEADIPOGFacade = pessoaEADIPOGFacade;
	}
	
	
	

	@Autowired
	private CensoInterfaceFacade censoFacade;
	
	public CensoInterfaceFacade getCensoFacade() {
		return censoFacade;
	}

	public void setCensoFacade(CensoInterfaceFacade censoFacade) {
		this.censoFacade = censoFacade;
	}
	
	@Autowired
	private LiberacaoTurmaEADIPOGInterfaceFacade liberacaoTurmaEADFacade;
	
	public LiberacaoTurmaEADIPOGInterfaceFacade getLiberacaoTurmaEADFacade() {
		return liberacaoTurmaEADFacade;
	}

	public void setLiberacaoTurmaEADFacade(LiberacaoTurmaEADIPOGInterfaceFacade liberacaoTurmaEADFacade) {
		this.liberacaoTurmaEADFacade = liberacaoTurmaEADFacade;
	}
	
	
	@Autowired
	private LogTurmaInterfaceFacade logTurmaFacade;
	
	public LogTurmaInterfaceFacade getLogTurmaFacade() {
		return logTurmaFacade;
	}

	public void setLogTurmaFacade(LogTurmaInterfaceFacade logTurmaFacade) {
		this.logTurmaFacade = logTurmaFacade;
	}
	
	@Autowired
	private VagaTurmaInterfaceFacade vagaTurmaFacade;
	
	public VagaTurmaInterfaceFacade getVagaTurmaFacade() {
		return vagaTurmaFacade;
	}

	public void setVagaTurmaFacade(VagaTurmaInterfaceFacade vagaTurmaFacade) {
		this.vagaTurmaFacade = vagaTurmaFacade;
	}
	
	
		@Autowired
	private VagaTurmaDisciplinaInterfaceFacade vagaTurmaDisciplinaFacade;
	
	public VagaTurmaDisciplinaInterfaceFacade getVagaTurmaDisciplinaFacade() {
		return vagaTurmaDisciplinaFacade;
	}

	public void setVagaTurmaDisciplinaFacade(VagaTurmaDisciplinaInterfaceFacade vagaTurmaDisciplinaFacade) {
		this.vagaTurmaDisciplinaFacade = vagaTurmaDisciplinaFacade;
	}
	