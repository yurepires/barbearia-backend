-- Inserindo Barbeiros
INSERT INTO barbeiro (nome, email, telefone, especialidade, created_at, created_by)
VALUES ('Carlos Navalha', 'carlos@barbearia.com', '11999990001', 'Corte e Barba', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO barbeiro (nome, email, telefone, especialidade, created_at, created_by)
VALUES ('João Mãos de Tesoura', 'joao@barbearia.com', '11999990002', 'Coloração e Descoloração', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO barbeiro (nome, email, telefone, especialidade, created_at, created_by)
VALUES ('Marcos Silva', 'marcos@barbearia.com', '11999990003', 'Barba Tradicional', CURRENT_TIMESTAMP, 'SYSTEM');



-- Inserindo Clientes
INSERT INTO cliente (nome, email, telefone, created_at, created_by)
VALUES ('Felipe Cliente', 'felipe@gmail.com', '11988880001', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO cliente (nome, email, telefone, created_at, created_by)
VALUES ('Ana Souza', 'ana@gmail.com', '11988880002', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO cliente (nome, email, telefone, created_at, created_by)
VALUES ('Ricardo Oliveira', 'ricardo@gmail.com', '11988880003', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO cliente (nome, email, telefone, created_at, created_by)
VALUES ('Pedro Santos', 'pedro@gmail.com', '11988880004', CURRENT_TIMESTAMP, 'SYSTEM');


-- Inserindo Serviços
INSERT INTO servico (nome, descricao, preco, created_at, created_by)
VALUES ('Corte Simples', 'Corte na tesoura ou máquina', 35.00, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO servico (nome, descricao, preco, created_at, created_by)
VALUES ('Barba', 'Barba com toalha quente', 25.00, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO servico (nome, descricao, preco, created_at, created_by)
VALUES ('Corte + Barba', 'Combo completo', 50.00, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO servico (nome, descricao, preco, created_at, created_by)
VALUES ('Hidratação', 'Hidratação capilar profunda', 20.00, CURRENT_TIMESTAMP, 'SYSTEM');


-- Inserindo Agendamentos
INSERT INTO agendamento (horario, status, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-11-28 10:00:00', 'CONCLUIDO', 1, 1, 1, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO agendamento (horario, status, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-11-29 11:00:00', 'CONCLUIDO', 2, 3, 2, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO agendamento (horario, status, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-12-02 14:00:00', 'CONCLUIDO', 3, 2, 1, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO agendamento (horario, status, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-12-03 16:00:00', 'CONCLUIDO', 2, 2, 2, CURRENT_TIMESTAMP, 'SYSTEM');


-- Inserindo Pagamentos
INSERT INTO pagamento (agendamento_id, valor, forma_pagamento, data_pagamento, created_at, created_by)
VALUES (1, 35.0, 'CARTAO_DEBITO', '2025-11-28 10:30:00', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO pagamento (agendamento_id, valor, forma_pagamento, data_pagamento, created_at, created_by)
VALUES (2, 50.0, 'PIX', '2025-11-29 11:30:00', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO pagamento (agendamento_id, valor, forma_pagamento, data_pagamento, created_at, created_by)
VALUES (3, 25.0, 'DINHEIRO', '2025-12-02 14:30:00', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO pagamento (agendamento_id, valor, forma_pagamento, data_pagamento, created_at, created_by)
VALUES (4, 25.0, 'CARTAO_CREDITO', '2025-12-03 16:00:00', CURRENT_TIMESTAMP, 'SYSTEM');


-- Inserindo Histórico de Atendimentos
INSERT INTO historico_atendimento (data, pagamento_id, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-11-28 10:30:00', 1, 1, 1, 1, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO historico_atendimento (data, pagamento_id, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-11-29 11:30:00', 2, 2, 3, 2, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO historico_atendimento (data, pagamento_id, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-12-02 14:30:00', 3, 3, 2, 1, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO historico_atendimento (data, pagamento_id, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-12-03 16:00:00', 4, 2, 2, 2, CURRENT_TIMESTAMP, 'SYSTEM');


-- Inserindo Despesas
INSERT INTO despesa (data_despesa, descricao, valor, created_at, created_by)
VALUES ('2025-12-03', 'Conta de luz', 325.99, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO despesa (data_despesa, descricao, valor, created_at, created_by)
VALUES ('2025-11-30', 'Conta de água', 86.52, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO despesa (data_despesa, descricao, valor, created_at, created_by)
VALUES ('2025-12-01', 'Aluguel', 600.00, CURRENT_TIMESTAMP, 'SYSTEM');