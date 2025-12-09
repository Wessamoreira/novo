ALTER TABLE IF EXISTS configuracaoatualizacaocadastral ADD COLUMN IF NOT EXISTS apresentarcampotranstornosneurodivergentes BOOLEAN DEFAULT false;

ALTER TABLE IF EXISTS configuracaoatualizacaocadastral ADD COLUMN IF NOT EXISTS permitiralterartranstornosneurodivergentes BOOLEAN DEFAULT false;

ALTER TABLE IF EXISTS pessoa ADD COLUMN IF NOT EXISTS transtornosneurodivergentes TEXT;