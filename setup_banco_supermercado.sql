-- Script completo para criar e popular o banco de dados do sistema de estoque
-- Execute este script no seu banco MySQL

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS SistemaEstoque;
USE SistemaEstoque;

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL
);

-- Tabela de fornecedores
CREATE TABLE IF NOT EXISTS fornecedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    contato VARCHAR(100),
    email VARCHAR(100),
    telefone VARCHAR(20)
);

-- Tabela de produtos
CREATE TABLE IF NOT EXISTS Produto (
    id INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    qnt_estoque INT NOT NULL,
    perecivel BOOLEAN NOT NULL,
    status VARCHAR(30) NOT NULL
);

-- Adicionar coluna fornecedor_id se não existir (ignorar erro se já existir)
ALTER TABLE Produto ADD COLUMN fornecedor_id INT;
-- Adicionar constraint de chave estrangeira (ignorar erro se já existir)
ALTER TABLE Produto ADD CONSTRAINT fk_produto_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id);

-- Inserir usuários
INSERT INTO usuarios (nome, senha, tipo) VALUES
('admin', 'admin123', 'ADMINISTRADOR'),
('gerente1', 'gerente123', 'GERENTE'),
('gerente2', 'gerente456', 'GERENTE');

-- Inserir fornecedores
INSERT INTO fornecedores (nome, cnpj, contato, email, telefone) VALUES
('Fornecedor ABC Ltda', '12.345.678/0001-90', 'João Silva', 'contato@abc.com.br', '(11) 99999-9999'),
('Distribuidora XYZ S.A.', '98.765.432/0001-10', 'Maria Santos', 'vendas@xyz.com.br', '(11) 88888-8888'),
('Comercial Delta Ltda', '55.444.333/0001-22', 'Pedro Oliveira', 'pedro@delta.com.br', '(11) 77777-7777'),
('Atacadão Supermercados', '22.111.333/0001-55', 'Ana Paula', 'ana@atacadao.com.br', '(11) 66666-6666');

-- Inserir 100 produtos de supermercado, cada um com um fornecedor
INSERT INTO Produto (id, nome, qnt_estoque, perecivel, status, fornecedor_id) VALUES
(1, 'Arroz Tipo 1', 50, 0, 'A_VENDA', 1),
(2, 'Feijão Carioca', 40, 0, 'A_VENDA', 1),
(3, 'Açúcar Refinado', 60, 0, 'A_VENDA', 1),
(4, 'Óleo de Soja', 30, 0, 'BAIXO_ESTOQUE', 1),
(5, 'Sal Refinado', 80, 0, 'ESTOQUE_COMPLETO', 1),
(6, 'Macarrão Espaguete', 25, 0, 'BAIXO_ESTOQUE', 1),
(7, 'Farinha de Trigo', 70, 0, 'A_VENDA', 1),
(8, 'Café Torrado', 20, 0, 'BAIXO_ESTOQUE', 1),
(9, 'Leite Integral', 15, 1, 'BAIXO_ESTOQUE', 2),
(10, 'Iogurte Natural', 10, 1, 'BAIXO_ESTOQUE', 2),
(11, 'Manteiga', 12, 1, 'BAIXO_ESTOQUE', 2),
(12, 'Queijo Mussarela', 18, 1, 'BAIXO_ESTOQUE', 2),
(13, 'Presunto', 22, 1, 'A_VENDA', 2),
(14, 'Requeijão', 8, 1, 'EM_FALTA', 2),
(15, 'Creme de Leite', 35, 0, 'A_VENDA', 2),
(16, 'Leite Condensado', 28, 0, 'A_VENDA', 2),
(17, 'Biscoito Maizena', 45, 0, 'A_VENDA', 3),
(18, 'Biscoito Recheado', 38, 0, 'A_VENDA', 3),
(19, 'Chocolate Barra', 55, 0, 'A_VENDA', 3),
(20, 'Achocolatado', 33, 0, 'A_VENDA', 3),
(21, 'Suco de Laranja', 27, 1, 'BAIXO_ESTOQUE', 3),
(22, 'Refrigerante Cola', 60, 0, 'A_VENDA', 3),
(23, 'Água Mineral', 90, 0, 'ESTOQUE_COMPLETO', 3),
(24, 'Cerveja Lata', 100, 0, 'ESTOQUE_COMPLETO', 3),
(25, 'Vinho Tinto', 12, 0, 'BAIXO_ESTOQUE', 3),
(26, 'Frango Congelado', 18, 1, 'BAIXO_ESTOQUE', 4),
(27, 'Carne Bovina', 14, 1, 'BAIXO_ESTOQUE', 4),
(28, 'Linguiça Toscana', 20, 1, 'A_VENDA', 4),
(29, 'Peito de Frango', 16, 1, 'BAIXO_ESTOQUE', 4),
(30, 'Peixe Congelado', 10, 1, 'BAIXO_ESTOQUE', 4),
(31, 'Batata Inglesa', 40, 1, 'A_VENDA', 4),
(32, 'Cenoura', 35, 1, 'A_VENDA', 4),
(33, 'Tomate', 30, 1, 'BAIXO_ESTOQUE', 4),
(34, 'Alface', 25, 1, 'BAIXO_ESTOQUE', 4),
(35, 'Cebola', 50, 1, 'A_VENDA', 4),
(36, 'Alho', 18, 1, 'BAIXO_ESTOQUE', 4),
(37, 'Maçã', 22, 1, 'A_VENDA', 4),
(38, 'Banana', 28, 1, 'A_VENDA', 4),
(39, 'Laranja', 32, 1, 'A_VENDA', 4),
(40, 'Pão Francês', 60, 1, 'A_VENDA', 1),
(41, 'Pão de Forma', 18, 1, 'BAIXO_ESTOQUE', 1),
(42, 'Bolo de Chocolate', 12, 1, 'BAIXO_ESTOQUE', 1),
(43, 'Rosquinha', 25, 1, 'BAIXO_ESTOQUE', 1),
(44, 'Pizza Congelada', 10, 1, 'BAIXO_ESTOQUE', 1),
(45, 'Salsicha', 20, 1, 'A_VENDA', 2),
(46, 'Hambúrguer Congelado', 15, 1, 'BAIXO_ESTOQUE', 2),
(47, 'Batata Palha', 30, 0, 'BAIXO_ESTOQUE', 2),
(48, 'Molho de Tomate', 40, 0, 'A_VENDA', 2),
(49, 'Ketchup', 22, 0, 'A_VENDA', 2),
(50, 'Maionese', 18, 0, 'BAIXO_ESTOQUE', 2),
(51, 'Mostarda', 16, 0, 'BAIXO_ESTOQUE', 2),
(52, 'Arroz Integral', 12, 0, 'BAIXO_ESTOQUE', 1),
(53, 'Feijão Preto', 14, 0, 'BAIXO_ESTOQUE', 1),
(54, 'Farinha de Mandioca', 20, 0, 'A_VENDA', 1),
(55, 'Polvilho Doce', 10, 0, 'BAIXO_ESTOQUE', 1),
(56, 'Fermento Químico', 8, 0, 'EM_FALTA', 1),
(57, 'Leite Desnatado', 25, 1, 'BAIXO_ESTOQUE', 2),
(58, 'Iogurte de Morango', 18, 1, 'BAIXO_ESTOQUE', 2),
(59, 'Queijo Prato', 20, 1, 'A_VENDA', 2),
(60, 'Queijo Minas', 15, 1, 'BAIXO_ESTOQUE', 2),
(61, 'Biscoito Cream Cracker', 30, 0, 'BAIXO_ESTOQUE', 3),
(62, 'Biscoito Água e Sal', 28, 0, 'BAIXO_ESTOQUE', 3),
(63, 'Chocolate ao Leite', 40, 0, 'A_VENDA', 3),
(64, 'Chocolate Meio Amargo', 22, 0, 'A_VENDA', 3),
(65, 'Suco de Uva', 18, 1, 'BAIXO_ESTOQUE', 3),
(66, 'Refrigerante Laranja', 35, 0, 'A_VENDA', 3),
(67, 'Água com Gás', 20, 0, 'BAIXO_ESTOQUE', 3),
(68, 'Cerveja Long Neck', 15, 0, 'BAIXO_ESTOQUE', 3),
(69, 'Vinho Branco', 10, 0, 'BAIXO_ESTOQUE', 3),
(70, 'Frango a Passarinho', 12, 1, 'BAIXO_ESTOQUE', 4),
(71, 'Carne Suína', 14, 1, 'BAIXO_ESTOQUE', 4),
(72, 'Linguiça Calabresa', 18, 1, 'BAIXO_ESTOQUE', 4),
(73, 'Peito de Peru', 20, 1, 'A_VENDA', 4),
(74, 'Peixe Tilápia', 16, 1, 'BAIXO_ESTOQUE', 4),
(75, 'Batata Doce', 22, 1, 'A_VENDA', 4),
(76, 'Cenoura Baby', 18, 1, 'BAIXO_ESTOQUE', 4),
(77, 'Tomate Cereja', 20, 1, 'A_VENDA', 4),
(78, 'Alface Crespa', 15, 1, 'BAIXO_ESTOQUE', 4),
(79, 'Cebola Roxa', 18, 1, 'BAIXO_ESTOQUE', 4),
(80, 'Alho Poró', 10, 1, 'BAIXO_ESTOQUE', 4),
(81, 'Maçã Fuji', 12, 1, 'BAIXO_ESTOQUE', 4),
(82, 'Banana Prata', 14, 1, 'BAIXO_ESTOQUE', 4),
(83, 'Laranja Lima', 16, 1, 'BAIXO_ESTOQUE', 4),
(84, 'Pão Integral', 18, 1, 'BAIXO_ESTOQUE', 1),
(85, 'Pão de Leite', 20, 1, 'A_VENDA', 1),
(86, 'Bolo de Cenoura', 10, 1, 'BAIXO_ESTOQUE', 1),
(87, 'Rosquinha de Coco', 12, 1, 'BAIXO_ESTOQUE', 1),
(88, 'Pizza de Calabresa', 14, 1, 'BAIXO_ESTOQUE', 1),
(89, 'Salsicha Viena', 16, 1, 'BAIXO_ESTOQUE', 2),
(90, 'Hambúrguer Bovino', 18, 1, 'BAIXO_ESTOQUE', 2),
(91, 'Batata Congelada', 20, 0, 'A_VENDA', 2),
(92, 'Molho Branco', 22, 0, 'A_VENDA', 2),
(93, 'Ketchup Picante', 24, 0, 'A_VENDA', 2),
(94, 'Maionese Light', 26, 0, 'A_VENDA', 2),
(95, 'Mostarda Escura', 28, 0, 'A_VENDA', 2),
(96, 'Arroz Parboilizado', 30, 0, 'A_VENDA', 1),
(97, 'Feijão Branco', 32, 0, 'A_VENDA', 1),
(98, 'Farinha de Rosca', 34, 0, 'A_VENDA', 1),
(99, 'Polvilho Azedo', 36, 0, 'A_VENDA', 1),
(100, 'Fermento Biológico', 38, 0, 'A_VENDA', 1);

-- Consultas de verificação
SELECT * FROM usuarios;
SELECT * FROM fornecedores;
SELECT * FROM Produto; 