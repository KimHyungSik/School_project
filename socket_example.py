import socket

host = '192.168.0.8'  # Symbolic name meaning all available interfaces
port = 9999  # Arbitrary non-privileged port

server_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_sock.bind((host, port))
server_sock.listen(1)

print("기다리는 중")


while True:
    client_sock, addr = server_sock.accept()
    data = client_sock.recv(1024)
    sock_data = data.decode("utf-8")[2:]

    print(addr, sock_data)

    if sock_data == 'off':
        break


client_sock.close()
server_sock.close()
