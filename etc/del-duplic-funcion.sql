SELECT id, upper(nome), cpf FROM funcionario 
WHERE cpf IN (SELECT cpf FROM funcionario GROUP BY cpf HAVING count(1) > 1 AND cpf <> '000.000.000-00')
order by cpf

update movimento set funcionario_id = 18228 where funcionario_id = 18930
update movimento set funcionario_id = 22511 where funcionario_id = 26052
update movimento set funcionario_id = 16073 where funcionario_id = 18234
update movimento set funcionario_id = 9467 where funcionario_id = 15793
update movimento set funcionario_id = 14076 where funcionario_id = 16050
update movimento set funcionario_id = 18936 where funcionario_id = 3799
update ponto set funcionario_id = 18936 where funcionario_id = 3799

SELECT id, nome, cpf FROM funcionario 
WHERE cpf IN (SELECT cpf FROM funcionario GROUP BY cpf HAVING count(1) > 1 AND cpf <> '000.000.000-00')
and id not in (SELECT f.id FROM ponto p
	INNER JOIN funcionario f on f.id = p.funcionario_id
	WHERE cpf IN (SELECT cpf FROM funcionario GROUP BY cpf HAVING count(1) > 1 AND cpf <> '000.000.000-00'))

delete FROM funcionario 
WHERE cpf IN (SELECT cpf FROM funcionario GROUP BY cpf HAVING count(1) > 1 AND cpf <> '000.000.000-00')
and id not in (SELECT f.id FROM ponto p
	INNER JOIN funcionario f on f.id = p.funcionario_id
	WHERE cpf IN (SELECT cpf FROM funcionario GROUP BY cpf HAVING count(1) > 1 AND cpf <> '000.000.000-00'))
and id <> 22511

delete from funcionario where id in (3799, 19848)