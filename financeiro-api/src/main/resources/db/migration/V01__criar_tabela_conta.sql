CREATE TABLE conta (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    data_pagamento DATE NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    situacao VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;