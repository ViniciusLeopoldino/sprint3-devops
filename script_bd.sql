-- DDL para a tabela de Motos usando PostgreSQL

-- A coluna 'id' como BIGSERIAL já cria a sequence e a define como padrão.
-- É o equivalente ao NUMBER + SEQUENCE do Oracle.
CREATE TABLE TB_MOTTU_MOTOS (
                                id          BIGSERIAL PRIMARY KEY, -- Identificador único (PK), auto-incremento
                                ds_modelo   VARCHAR(100) NOT NULL, -- Descrição do modelo
                                nr_placa    VARCHAR(10) NOT NULL UNIQUE, -- Placa da moto, deve ser única
                                nr_ano      INTEGER -- Ano de fabricação
);

-- Comentários para documentação
COMMENT ON TABLE TB_MOTTU_MOTOS IS 'Tabela para cadastro das motocicletas da Mottu.';
COMMENT ON COLUMN TB_MOTTU_MOTOS.id IS 'Identificador único da motocicleta (chave primária).';
COMMENT ON COLUMN TB_MOTTU_MOTOS.ds_modelo IS 'Descrição do modelo da motocicleta.';
COMMENT ON COLUMN TB_MOTTU_MOTOS.nr_placa IS 'Número da placa da motocicleta.';
COMMENT ON COLUMN TB_MOTTU_MOTOS.nr_ano IS 'Ano de fabricação da motocicleta.';