DROP TABLE time_partida;
INSERT INTO usuario (id, ativo, email, perfil, senha) VALUES (1, 'Y', 'gfalcao@outlook.com', 'ADM', 'a6d01273631aa1c6b4a42b240ce4fca2');
INSERT INTO usuario (id, ativo, email, perfil, senha) VALUES (2, 'Y', 'teste@teste.com', 'USER', 'a6d01273631aa1c6b4a42b240ce4fca2');
SOURCE /home/falke/Área de Trabalho/trigger.sql;
--INSERT INTO liga (id, criacaoLiga, logo, nome, id_usuario) VALUES (null, now(), null, 'Liga Mais Maneira', 1);
--INSERT INTO liga (id, criacaoLiga, logo, nome, id_usuario) VALUES (null, now(), null, 'Liga Maneira', 1);
--INSERT INTO time (nome, id_liga) VALUES ('Botafogo', 1);
--INSERT INTO time (nome, id_liga) VALUES ('Botafogo', 2);

--INSERT INTO partida (dataPartida, golsTimeCasa, golsTimeVisitante, id_liga, id_visitante, id_casa) values 
--(now(), 2, 0, 1, 1, 2);

--TRIGGER PARA O TIME SER SOMENTE DA MESMA LIGA NA PARTIDA

DELIMITER $

CREATE TRIGGER Mesma_Liga BEFORE INSERT 
ON partida
FOR EACH ROW
BEGIN
	SET @IDCASA = (SELECT id_liga FROM time WHERE id = NEW.id_casa);
	SET @IDVISITANTE = (SELECT id_liga FROM time WHERE id = NEW.id_visitante);
	
	IF @IDCASA != @IDVISITANTE THEN
		SET NEW.id_casa = NULL;
		SET NEW.id_casa = NULL;
	END IF;
END $

DELIMITER ;