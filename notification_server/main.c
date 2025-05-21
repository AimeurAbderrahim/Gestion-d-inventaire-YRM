/* #include <stdio.h> */
/* #include <stdlib.h> */
/* #include <string.h> */
/* #include <unistd.h> */
/* #include <arpa/inet.h> */
/* #include <sys/socket.h> */
/* #include <stdbool.h> */

/* #define PORT 9456 */
/* #define TARGET_IP_LEN 16 */
/* #define MESSAGE_LEN 256 */


/* typedef struct __attribute__((packed)) { */
/* 	char target_ip[TARGET_IP_LEN]; */
/* 	char message[MESSAGE_LEN]; */
/* 	bool is_checked; */
/* 	bool is_valid; */
/* } Notification; */

/* void handle_client(Socket client_socket) { */
/* 	Notification notif; */

/* 	// Receive notification */
/* 	ssize_t bytes_received = recv(client_socket, &notif, sizeof(Notification), 0); */
/* 	if (bytes_received <= 0) { */
/* 		perror("recv failed"); */
/* 		close(client_socket); */
/* 		return; */
/* 	} */

/* 	printf("\nReceived Notification:\n"); */
/* 	printf("Target IP: %s\n", notif.target_ip); */
/* 	printf("Message: %s\n", notif.message); */
/* 	printf("Checked: %s\n", notif.is_checked ? "true" : "false"); */
/* 	printf("Valid: %s\n", notif.is_valid ? "true" : "false"); */

/* 	// Validate target IP format */
/* 	struct sockaddr_in sa; */
/* 	if (inet_pton(AF_INET, notif.target_ip, &sa.sin_addr) <= 0) { */
/* 		printf("Invalid target IP format!\n"); */
/* 		notif.is_valid = false; */
/* 	} */

/* 	// If valid, redirect to target IP */
/* 	if (notif.is_valid) { */
/* 		int forward_socket = socket(AF_INET, SOCK_STREAM, 0); */
/* 		if (forward_socket < 0) { */
/* 			perror("Forward socket creation error"); */
/* 			close(client_socket); */
/* 			return; */
/* 		} */

/* 		struct sockaddr_in target_addr; */
/* 		target_addr.sin_family = AF_INET; */
/* 		target_addr.sin_port = htons(PORT); */

/* 		if (inet_pton(AF_INET, notif.target_ip, &target_addr.sin_addr) <= 0) { */
/* 			perror("Invalid target IP"); */
/* 			close(forward_socket); */
/* 			close(client_socket); */
/* 			return; */
/* 		} */

/* 		if (connect(forward_socket, (struct sockaddr*)&target_addr, sizeof(target_addr)) < 0) { */
/* 			perror("Forward connection failed"); */
/* 		} else { */
/* 			send(forward_socket, &notif, sizeof(Notification), 0); */
/* 			printf("Notification forwarded to %s\n", notif.target_ip); */
/* 			close(forward_socket); */
/* 		} */
/* 	} */

/* 	close(client_socket); */
/* } */

/* int main() { */
/* 	Socket server_fd, new_socket; */
/* 	struct sockaddr_in address; */
/* 	int opt = 1; */
/* 	socklen_t addrlen = sizeof(address); */

/* 	// Create server socket */
/* 	if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) { */
/* 		perror("socket failed"); */
/* 		exit(EXIT_FAILURE); */
/* 	} */

/* 	// Set socket options */
/* 	if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt))) { */
/* 		perror("setsockopt"); */
/* 		exit(EXIT_FAILURE); */
/* 	} */

/* 	address.sin_family = AF_INET; */
/* 	address.sin_addr.s_addr = INADDR_ANY; */
/* 	address.sin_port = htons(PORT); */

/* 	// Bind socket to port */
/* 	if (bind(server_fd, (struct sockaddr*)&address, sizeof(address)) < 0) { */
/* 		perror("bind failed"); */
/* 		exit(EXIT_FAILURE); */
/* 	} */

/* 	// Start listening */
/* 	if (listen(server_fd, 3) < 0) { */
/* 		perror("listen"); */
/* 		exit(EXIT_FAILURE); */
/* 	} */

/* 	printf("Notification Server listening on port %d\n", PORT); */

/* 	while (1) { */
/* 		// Accept incoming connection */
/* 		if ((new_socket = accept(server_fd, (struct sockaddr*)&address, &addrlen)) < 0) { */
/* 			perror("accept"); */
/* 			exit(EXIT_FAILURE); */
/* 		} */

/* 		// Handle client in a new process */
/* 		if (fork() == 0) { */
/* 			close(server_fd); */
/* 			handle_client(new_socket); */
/* 			exit(0); */
/* 		} else { */
/* 			close(new_socket); */
/* 		} */
/* 	} */

/* 	return 0; */
/* } */

// clib
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>

// posix api 
// NOTE: this works only on linux
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <signal.h>

// third party library
#include <cjson/cJSON.h>

#define PORT 9090
#define BUFFER_SIZE 4096
#define MAX_CLIENTS 5

typedef struct {
	char *target_ip;
	char *message;
	bool is_checked;
	bool is_valid;
} Notification;

void cleanup_notification(Notification *notif) {
	free(notif->target_ip);
	free(notif->message);
}

int validate_ip(const char *ip) {
	struct sockaddr_in sa;
	return inet_pton(AF_INET, ip, &sa.sin_addr);
}

void send_ack(int sock, bool is_valid) {
	cJSON *response = cJSON_CreateObject();
	cJSON_AddStringToObject(response, "protocol", "NOTIFY/1.0");
	cJSON_AddBoolToObject(response, "ack", true);
	cJSON_AddBoolToObject(response, "valid", is_valid);

	char *response_str = cJSON_PrintUnformatted(response);
	send(sock, response_str, strlen(response_str), 0);

	cJSON_Delete(response);
	free(response_str);
}

Notification parse_protocol_message(cJSON *json) {
	Notification notif = {0};

	cJSON *target_ip = cJSON_GetObjectItemCaseSensitive(json, "target");
	cJSON *message = cJSON_GetObjectItemCaseSensitive(json, "message");
	cJSON *checked = cJSON_GetObjectItemCaseSensitive(json, "checked");
	cJSON *valid = cJSON_GetObjectItemCaseSensitive(json, "valid");

	if (cJSON_IsString(target_ip)) {
		notif.target_ip = strdup(target_ip->valuestring);
	}
	if (cJSON_IsString(message)) {
		notif.message = strdup(message->valuestring);
	}
	if (cJSON_IsBool(checked)) {
		notif.is_checked = cJSON_IsTrue(checked);
	}
	if (cJSON_IsBool(valid)) {
		notif.is_valid = cJSON_IsTrue(valid);
	}

	return notif;
}

void forward_notification(const Notification *notif) {
	int sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock < 0) return;

	struct sockaddr_in target_addr = {
		.sin_family = AF_INET,
		.sin_port = htons(PORT),
	};

	if (validate_ip(notif->target_ip) <= 0) return;
	inet_pton(AF_INET, notif->target_ip, &target_addr.sin_addr);

	if (connect(sock, (struct sockaddr*)&target_addr, sizeof(target_addr)) == 0) {
		cJSON *json = cJSON_CreateObject();
		cJSON_AddStringToObject(json, "target", notif->target_ip);
		cJSON_AddStringToObject(json, "message", notif->message);
		cJSON_AddBoolToObject(json, "checked", notif->is_checked);
		cJSON_AddBoolToObject(json, "valid", notif->is_valid);

		char *data = cJSON_PrintUnformatted(json);
		send(sock, data, strlen(data), 0);

		free(data);
		cJSON_Delete(json);
	}
	close(sock);
}

void handle_client(int client_sock) {
	char buffer[BUFFER_SIZE];
	ssize_t bytes_read = recv(client_sock, buffer, BUFFER_SIZE - 1, 0);
	if (bytes_read <= 0) {
		close(client_sock);
		return;
	}
	buffer[bytes_read] = '\0';

	cJSON *json = cJSON_Parse(buffer);
	if (!json) {
		send_ack(client_sock, false);
		close(client_sock);
		return;
	}

	Notification notif = parse_protocol_message(json);
	if (!notif.target_ip || !notif.message) {
		send_ack(client_sock, false);
		cleanup_notification(&notif);
		cJSON_Delete(json);
		close(client_sock);
		return;
	}

	// Validate IP format
	notif.is_valid = (validate_ip(notif.target_ip) > 0);
	send_ack(client_sock, notif.is_valid);

	if (notif.is_valid) {
		forward_notification(&notif);
	}

	cleanup_notification(&notif);
	cJSON_Delete(json);
	close(client_sock);
}

int main() {
	int server_fd = socket(AF_INET, SOCK_STREAM, 0);
	struct sockaddr_in addr = {
		.sin_family = AF_INET,
		.sin_addr.s_addr = INADDR_ANY,
		.sin_port = htons(PORT)
	};

	setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &(int){1}, sizeof(int));
	bind(server_fd, (struct sockaddr*)&addr, sizeof(addr));
	listen(server_fd, MAX_CLIENTS);

	signal(SIGCHLD, SIG_IGN);
	printf("Notification Protocol Server running on port %d\n", PORT);

	while (1) {
		int client_sock = accept(server_fd, NULL, NULL);
		if (client_sock < 0) continue;

		if (fork() == 0) {
			close(server_fd);
			handle_client(client_sock);
			exit(0);
		}
		close(client_sock);
	}

	close(server_fd);
	return 0;
}
