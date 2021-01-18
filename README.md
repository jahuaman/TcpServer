# TcpServer

Servidor TCP para o recebimento de mensagens de um cliente TCP e armazenamento delas em banco de dados H2.

Para o desenvolvimento foi utilizado spring-boot. A implementação do servidor TCP foi realizado com sockets Java nativo.

Para comunicar com o servidor TCP pode ser utilizado a ferramenta PacketSender (https://packetsender.com/).
O servidor de TCP Server roda na porta 12345

As mensagens recebidas/enviadas são salvas em um arquivo de log na raiz do projeto (mylog.log).

## Estrutura básica das mensagens

INIT(1 byte)	BYTES(1 byte)	FRAME(1 byte)	DATA(n bytes)	CRC(1 byte)	END(1 byte)

## Utilizando PacketSender (TCP)

### 1. Mensagem de texto (0xA1) 

Address: localhost

Port: 12345

HEX: 0A 10 A1 48 65 6C 6C 6F 20 57 6F 72 6C 64	DC 0D

Ascii: \n\10\a1Hello World\dc\r

O servidor responde com ACK: 

0A 05 A0 28 0D

E salva os dados na base de datos.

### 2. Informações de um usuário (0xA2) 

Address: localhost

Port: 12345

HEX: 0A	15 A2 20 7A C3 0C 4D 69 63 68 65 6C 20 52 65 69 70 73 16 0D

Ascii: \\n\15\a2 z\c3\0cMichel Reips\16\r

O servidor responde com ACK: 

0A 05 A0 28 0D

### 3. Solicitar data e hora atual (0xA3) 

Address: localhost

Port: 12345

HEX: 0A 16 A3 41 6D 65 72 69 63 61 2F 53 61 6F 5F 50 61 75 6C 6F CD 0D

Ascii: \n\16\a3America/Sao_Paulo\cd\r

O servidor responde com data e hora:

11	06	14	11	2B	0F

Para acessar ao Banco de dados H2 (Os dados em memoria)
http://localhost:9000/h2/

