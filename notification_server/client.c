#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <cjson/cJSON.h>

#define PORT 2102
#define BUFFER_SIZE 4096

typedef int Socket;

void send_request(const char* server_ip, const char* message) {
	Socket sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock < 0) {
		perror("Socket creation failed");
		return;
	}

	struct sockaddr_in serv_addr = {
		.sin_family = AF_INET,
		.sin_port = htons(PORT)
	};

	if (inet_pton(AF_INET, server_ip, &serv_addr.sin_addr) <= 0) {
		perror("Invalid server address");
		close(sock);
		return;
	}

	if (connect(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
		perror("Connection failed");
		close(sock);
		return;
	}

	cJSON *request = cJSON_CreateObject();
	cJSON_AddNumberToObject(request, "method", 0); // REQ
	cJSON_AddStringToObject(request, "target", "192.168.0.171");
	cJSON_AddStringToObject(request, "message", message);
	cJSON_AddNumberToObject(request, "type", 1); // TYPE_FORWARD

	char *request_str = cJSON_PrintUnformatted(request);
	send(sock, request_str, strlen(request_str), 0);

	free(request_str);
	cJSON_Delete(request);
	close(sock);
}

void listen_for_response() {
	Socket server_fd = socket(AF_INET, SOCK_STREAM, 0);
	if (server_fd < 0) {
		perror("Listener socket failed");
		return;
	}

	struct sockaddr_in addr = {
		.sin_family = AF_INET,
		.sin_addr.s_addr = INADDR_ANY,
		.sin_port = htons(PORT)
	};

	if (bind(server_fd, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
		perror("Bind failed");
		close(server_fd);
		return;
	}

	if (listen(server_fd, 1) < 0) {
		perror("Listen failed");
		close(server_fd);
		return;
	}

	printf("Waiting for admin response...\n");
	Socket client_sock = accept(server_fd, NULL, NULL);
	if (client_sock < 0) {
		perror("Accept failed");
		close(server_fd);
		return;
	}

	char buffer[BUFFER_SIZE];
	ssize_t bytes_read = recv(client_sock, buffer, BUFFER_SIZE - 1, 0);
	if (bytes_read > 0) {
		buffer[bytes_read] = '\0';
		cJSON *response = cJSON_Parse(buffer);
		if (response) {
			cJSON *msg = cJSON_GetObjectItem(response, "message");
			cJSON *status = cJSON_GetObjectItem(response, "status");
			printf("Admin response: %s (Status: %d)\n", 
					msg->valuestring, status->valueint);
			cJSON_Delete(response);
		}
	}

	close(client_sock);
	close(server_fd);
}

int main(int argc, char *argv[]) {
	if (argc < 3) {
		printf("Usage: %s <server_ip> <message>\n", argv[0]);
		return 1;
	}

	send_request(argv[1], argv[2]);
	listen_for_response();

	return 0;
}
