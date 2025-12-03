-- Inserindo Barbeiros
INSERT INTO barbeiro (nome, email, telefone, created_at, created_by)
VALUES ('Carlos Navalha', 'carlos@barbearia.com', '11999990001', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO barbeiro (nome, email, telefone, created_at, created_by)
VALUES ('João Mãos de Tesoura', 'joao@barbearia.com', '11999990002', CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO barbeiro (nome, email, telefone, created_at, created_by)
VALUES ('Marcos Silva', 'marcos@barbearia.com', '11999990003', CURRENT_TIMESTAMP, 'SYSTEM');


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
VALUES ('2025-11-28 10:00:00', 'PENDENTE', 1, 1, 1, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO agendamento (horario, status, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-11-29 11:00:00', 'PENDENTE', 2, 3, 2, CURRENT_TIMESTAMP, 'SYSTEM');

INSERT INTO agendamento (horario, status, cliente_id, servico_id, barbeiro_id, created_at, created_by)
VALUES ('2025-12-02 14:00:00', 'PENDENTE', 3, 2, 1, CURRENT_TIMESTAMP, 'SYSTEM');