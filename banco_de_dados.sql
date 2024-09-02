CREATE DATABASE parking_system;

USE parking_system;
CREATE TABLE category (
	Id INT AUTO_INCREMENT PRIMARY KEY,
	Name VARCHAR(16)
);

CREATE TABLE typeVehicle (
	Id INT AUTO_INCREMENT PRIMARY KEY,
	Name VARCHAR(15)
);

CREATE TABLE vehicle ( 
  Id INT AUTO_INCREMENT PRIMARY KEY,
  Plate varchar(8),
  IdCategory INT,
  IdTypeVehicle INT,
  FOREIGN KEY(IdCategory) REFERENCES category(Id),
  FOREIGN KEY(IdTypeVehicle) REFERENCES typeVehicle(Id)
);

CREATE TABLE typeGate(
	Id INT AUTO_INCREMENT PRIMARY KEY,
	Name VARCHAR(7)
);

CREATE TABLE gate(
	id INT AUTO_INCREMENT PRIMARY KEY,
    IdTypeGate INT(2),
    FOREIGN KEY(IdTypeGate) REFERENCES typeGate(Id));

CREATE TABLE vacancy (
  Id INT AUTO_INCREMENT PRIMARY KEY,
  Available BOOLEAN DEFAULT TRUE,
  IdVehicle INT,
  FOREIGN KEY(IdVehicle) REFERENCES vehicle(Id)
);

CREATE TABLE management (
  Id INT(3) AUTO_INCREMENT PRIMARY KEY,
  Entry DATETIME NOT NULL,
  Departure DATETIME,
  IdEntranceGate INT NOT NULL,
  IdExitGate INT,
  Value DOUBLE,
  IdVehicle INT,
  FOREIGN KEY (IdEntranceGate) REFERENCES gate(Id),
  FOREIGN KEY (IdExitGate) REFERENCES gate(Id),
  FOREIGN KEY (IdVehicle) REFERENCES vehicle(Id)
);

INSERT INTO category (Name) VALUES 
	('MENSALISTA'),
	('CAMINHAO'),
	('SERVICO_PUBLICO'),
	('AVULSO');
  
INSERT INTO typeVehicle (Name) VALUES 
	('CARRO'),
	('MOTO'),
	('CAMINHAO'),
    ('SERVICO_PUBLICO');
  
INSERT INTO typeGate (Name) VALUES
	('ENTRADA'),
	('SAIDA');
    
INSERT INTO gate (IdTypeGate) VALUES
	('1'),
	('1'),
	('1'),
	('1'),
	('1'),
	('2'),
	('2'),
	('2'),
	('2'),
	('2');   
