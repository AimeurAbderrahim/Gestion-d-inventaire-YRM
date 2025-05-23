// TODO: handle ack
	/* struct { */
	/* 	char *target_ip; */
	/* 	char *message; */
	/* 	bool is_checked; */
	/* }User; */
	/* // protocole information */
	/* struct{ */
	/* 	Method method; */
	/* 	char* protocole; */
	/* 	TypeNotify requests_type; */
	/* 	Status requests_status; */
	/* }Protocol; */
// clib
#include <stdio.h>
#include <stdlib.h>

#include <unistd.h>
#include <stdbool.h>
#include <pthread.h>

// posix api
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <signal.h>

// third party library
#include <cjson/cJSON.h>

#include "Notify.h"

typedef struct {
	Notification notif;
	Socket fd;
} QueuedNotification;

void NotificationCleanUp(Notification *notif) {
	free(notif->User.target_ip);
	free(notif->User.message);
}

int ValidateIp(const char *ip) {
	struct sockaddr_in sa;
	return inet_pton(AF_INET, ip, &sa.sin_addr);
}

void SendAck(Socket sock, Status status) {
	cJSON *response = cJSON_CreateObject();
	cJSON_AddNumberToObject(response, "method", 1);
	cJSON_AddStringToObject(response, "protocol", "NOTIFY/1.0");
	cJSON_AddNumberToObject(response, "request_type", 3);
	cJSON_AddNumberToObject(response, "status", status);
	char *response_str = cJSON_PrintUnformatted(response);
	send(sock, response_str, strlen(response_str), 0);

	cJSON_Delete(response);
	free(response_str);
}

Notification ParseProtocoleMessage(cJSON *json) {
	Notification notif = {0};
	cJSON *method = cJSON_GetObjectItemCaseSensitive(json, "method");
	cJSON *request_type = cJSON_GetObjectItemCaseSensitive(json, "request_type");

	cJSON *protocole = cJSON_GetObjectItemCaseSensitive(json, "protocol");
	cJSON *status = cJSON_GetObjectItemCaseSensitive(json, "status");

	cJSON *target_ip = cJSON_GetObjectItemCaseSensitive(json, "target");
	cJSON *message = cJSON_GetObjectItemCaseSensitive(json, "message");
	cJSON *checked = cJSON_GetObjectItemCaseSensitive(json, "checked");
	/* cJSON *source = cJSON_GetObjectItemCaseSensitive(json, "source"); */

	if(cJSON_IsNumber(status)){
		notif.Protocol.requests_status = status->valueint;
	}
	if(cJSON_IsString(protocole)){
		notif.Protocol.protocole = strdup(protocole->valuestring);
	}
	if (cJSON_IsNumber(method)) {
		notif.Protocol.method = method->valueint;
	}
	if (cJSON_IsString(target_ip)) {
		notif.User.target_ip = strdup(target_ip->valuestring);
	}
	if (cJSON_IsString(message)) {
		notif.User.message = strdup(message->valuestring);
	}
	if (cJSON_IsBool(checked)) {
		notif.User.is_checked = cJSON_IsTrue(checked);
	}
	if (cJSON_IsNumber(request_type)) {
		notif.Protocol.requests_type = request_type->valueint;
	}
	/* if (cJSON_IsString(source)) { */
	/* 	// For admin responses, source is stored in target_ip */
	/* 	free(notif.User.target_ip); */
	/* 	notif.User.target_ip = strdup(source->valuestring); */
	/* } */

	return notif;
}

void ForwardNotification(const Notification* const notif) {
	int sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock < 0) return;
	struct sockaddr_in target_addr = {
		.sin_family = AF_INET,
		.sin_port = htons(PORT),
	};

	if (ValidateIp(notif->User.target_ip) <= 0) {
		close(sock);
		return;
	}

	inet_pton(AF_INET, notif->User.target_ip, &target_addr.sin_addr);

	if (connect(sock, (struct sockaddr*)&target_addr, sizeof(target_addr)) == 0) {
		cJSON *json = cJSON_CreateObject();
		cJSON_AddNumberToObject(json, "method", notif->Protocol.method);
		cJSON_AddStringToObject(json, "protocol", notif->Protocol.protocole);
		cJSON_AddNumberToObject(json, "status", notif->Protocol.requests_status);
		cJSON_AddNumberToObject(json, "request_type", notif->Protocol.requests_type);

		cJSON_AddStringToObject(json, "target", notif->User.target_ip);
		cJSON_AddNumberToObject(json, "client_type", notif->User.client_type);
		cJSON_AddStringToObject(json, "message", notif->User.message);
		cJSON_AddBoolToObject(json, "checked", notif->User.is_checked);

		char *data = cJSON_PrintUnformatted(json);
		send(sock, data, strlen(data), 0);

		free(data);
		cJSON_Delete(json);
	}
	close(sock);
}
void* HandleClientThread(void* arg) {
	// initial gloabl queue object 
	/* RLCollections queue = Queue(Buf_Disable); */
	RLCollections queue = Queue(Buf_Disable);

	int client_sock = *((int*)arg);
	free(arg);
	char buffer[BUFFER_SIZE];
	ssize_t bytes_read = recv(client_sock, buffer, BUFFER_SIZE - 1, 0);
	if (bytes_read <= 0) {
		SendAck(client_sock, WARN);
		close(client_sock);
		return NULL;
	}
	/* puts(buffer); */
	/* fflush(stdout); */
	buffer[bytes_read] = '\0';

	cJSON *json = cJSON_Parse(buffer);
	if (!json) {
		SendAck(client_sock, WARN);
		close(client_sock);
		return NULL;
	}

	Notification notif = ParseProtocoleMessage(json);

	SendAck(client_sock, OK);

	// Handle customer request
	/* if (notif.Protocol.method == REQ && strcmp(notif.User.target_ip, "admin") == 0) { */
	if (notif.Protocol.method == REQ && notif.Protocol.requests_type == TYPE_HELLO) {
		// Store in queue with source IP (assuming client_sock has IP, need adjustment)
		// Note: Source IP should be captured during accept, this is simplified
		QueuedNotification *q_notif = malloc(sizeof(QueuedNotification));
		q_notif->notif = notif;
		q_notif->fd = client_sock;
		RLCopyObject(RLSIZEOF(QueuedNotification));
		queue.Push(RL_VOIDPTR, q_notif);
		RLDisableCopyObject();
	}
	// Handle admin SYN
	else if (notif.Protocol.requests_type == TYPE_SYN) {
		if (!queue.Is_Empty()) {
			RLResult result = queue.Pop();
			if (!result.IsError()) {
				QueuedNotification *q_notif = (QueuedNotification*)result.GetData();
				// Forward to admin with source IP
				cJSON *admin_json = cJSON_CreateObject();
				/* cJSON_AddStringToObject(admin_json, "source", q_notif->source_ip); */
				cJSON_AddNumberToObject(admin_json, "method", q_notif->notif.Protocol.method);
				cJSON_AddNumberToObject(admin_json, "request_type", TYPE_FORWARD);

				cJSON_AddStringToObject(admin_json, "message", q_notif->notif.User.message);
				cJSON_AddBoolToObject(admin_json, "checked", q_notif->notif.User.is_checked);

				char *admin_data = cJSON_PrintUnformatted(admin_json);
				send(client_sock, admin_data, strlen(admin_data), 0);
				free(admin_data);
				cJSON_Delete(admin_json);

				NotificationCleanUp(&q_notif->notif);
				free(q_notif);
			}
		}
	}
	// Handle admin response
	else if (notif.Protocol.method == RES) {
		ForwardNotification(&notif);
	}

	NotificationCleanUp(&notif);
	cJSON_Delete(json);
	close(client_sock);
	return NULL;
}

int main() {
	Socket server_fd = socket(AF_INET, SOCK_STREAM, 0);
	struct sockaddr_in addr = {
		.sin_family = AF_INET,
		.sin_addr.s_addr = INADDR_ANY,
		.sin_port = htons(PORT)
	};
	setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &(int){1}, sizeof(int));
	bind(server_fd, (struct sockaddr*)&addr, sizeof(addr));
	listen(server_fd, MAX_CLIENTS);

	printf("Notification Protocol Server running on port %d\n", PORT);

	while (1) {
		struct sockaddr_in client_addr;
		socklen_t addr_len = sizeof(client_addr);
		Socket client_sock = accept(server_fd, (struct sockaddr*)&client_addr, &addr_len);
		if (client_sock < 0) continue;

		int *sock_ptr = malloc(sizeof(int));
		*sock_ptr = client_sock;

		/* pthread_t thread; */
		/* pthread_create(&thread, NULL, HandleClientThread, sock_ptr); */
		/* pthread_detach(thread); */
		HandleClientThread(sock_ptr);
		/* break; */
	}

	close(server_fd);
	return 0;
}















/* // clib */
/* #include <stdio.h> */
/* #include <stdlib.h> */
/* #include <string.h> */
/* #include <unistd.h> */
/* #include <stdbool.h> */

/* // posix api */ 
/* // NOTE: this works only on linux */
/* #include <arpa/inet.h> */
/* #include <sys/socket.h> */
/* #include <sys/wait.h> */
/* #include <signal.h> */

/* // third party library */
/* #include <cjson/cJSON.h> */

/* #include "Notify.h" */

/* void NotificationCleanUp(Notification *notif) { */
/* 	free(notif->target_ip); */
/* 	free(notif->message); */
/* } */

/* int ValidateIp(const char *ip) { */
/* 	struct sockaddr_in sa; */
/* 	return inet_pton(AF_INET, ip, &sa.sin_addr); */
/* } */

/* void SendAck(Socket sock, bool is_valid) { */
/* 	cJSON *response = cJSON_CreateObject(); */
/* 	cJSON_AddStringToObject(response, "protocol", "NOTIFY/1.0"); */
/* 	cJSON_AddBoolToObject(response, "ack", true); */
/* 	cJSON_AddBoolToObject(response, "valid", is_valid); */

/* 	char *response_str = cJSON_PrintUnformatted(response); */
/* 	send(sock, response_str, strlen(response_str), 0); */

/* 	cJSON_Delete(response); */
/* 	free(response_str); */
/* } */

/* Notification ParseProtocoleMessage(cJSON *json) { */
/* 	Notification notif = {0}; */

/* 	cJSON *target_ip = cJSON_GetObjectItemCaseSensitive(json, "target"); */
/* 	cJSON *message = cJSON_GetObjectItemCaseSensitive(json, "message"); */
/* 	cJSON *checked = cJSON_GetObjectItemCaseSensitive(json, "checked"); */
/* 	cJSON *valid = cJSON_GetObjectItemCaseSensitive(json, "valid"); */

/* 	if (cJSON_IsString(target_ip)) { */
/* 		notif.target_ip = strdup(target_ip->valuestring); */
/* 	} */
/* 	if (cJSON_IsString(message)) { */
/* 		notif.message = strdup(message->valuestring); */
/* 	} */
/* 	if (cJSON_IsBool(checked)) { */
/* 		notif.is_checked = cJSON_IsTrue(checked); */
/* 	} */
/* 	if (cJSON_IsBool(valid)) { */
/* 		notif.is_valid = cJSON_IsTrue(valid); */
/* 	} */

/* 	return notif; */
/* } */

/* void ForwardNotification(const Notification* const notif) { */
/* 	int sock = socket(AF_INET, SOCK_STREAM, 0); */
/* 	if (sock < 0) return; */

/* 	struct sockaddr_in target_addr = { */
/* 		.sin_family = AF_INET, */
/* 		.sin_port = htons(PORT), */
/* 	}; */

/* 	if (ValidateIp(notif->target_ip) <= 0) return; */
/* 	inet_pton(AF_INET, notif->target_ip, &target_addr.sin_addr); */

/* 	if (connect(sock, (struct sockaddr*)&target_addr, sizeof(target_addr)) == 0) */ 
/* 	{ */
/* 		cJSON *json = cJSON_CreateObject(); */
/* 		cJSON_AddStringToObject(json, "target", notif->target_ip); */
/* 		cJSON_AddStringToObject(json, "message", notif->message); */
/* 		cJSON_AddBoolToObject(json,   "checked", notif->is_checked); */
/* 		cJSON_AddBoolToObject(json,   "valid", notif->is_valid); */

		

/* 		char *data = cJSON_PrintUnformatted(json); */
/* 		send(sock, data, strlen(data), 0); */

/* 		free(data); */
/* 		cJSON_Delete(json); */
/* 	} */
/* 	close(sock); */
/* } */

/* void HandleClient(Socket client_sock) { */
/* 	char buffer[BUFFER_SIZE]; */
/* 	ssize_t bytes_read = recv(client_sock, buffer, BUFFER_SIZE - 1, 0); */
/* 	if (bytes_read <= 0) { */
/* 		close(client_sock); */
/* 		return; */
/* 	} */
/* 	buffer[bytes_read] = '\0'; */

/* 	cJSON *json = cJSON_Parse(buffer); */
/* 	if (!json) { */
/* 		SendAck(client_sock, false); */
/* 		close(client_sock); */
/* 		return; */
/* 	} */

/* 	Notification notif = ParseProtocoleMessage(json); */
/* 	if (!notif.target_ip || !notif.message) { */
/* 		SendAck(client_sock, false); */
/* 		NotificationCleanUp(&notif); */
/* 		cJSON_Delete(json); */
/* 		close(client_sock); */
/* 		return; */
/* 	} */

/* 	notif.is_valid = (ValidateIp(notif.target_ip) > 0); */
/* 	SendAck(client_sock, notif.is_valid); */

/* 	if (notif.is_valid) { */
/* 		ForwardNotification(&notif); */
/* 	} */

/* 	NotificationCleanUp(&notif); */
/* 	cJSON_Delete(json); */
/* 	close(client_sock); */
/* } */

/* int main() { */
/* 	Socket server_fd = socket(AF_INET, SOCK_STREAM, 0); */
/* 	struct sockaddr_in addr = { */
/* 		.sin_family = AF_INET, */
/* 		.sin_addr.s_addr = INADDR_ANY, */
/* 		.sin_port = htons(PORT) */
/* 	}; */


/* 	setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &(int){1}, sizeof(int)); */
/* 	bind(server_fd, (struct sockaddr*)&addr, sizeof(addr)); */
/* 	listen(server_fd, MAX_CLIENTS); */

/* 	signal(SIGCHLD, SIG_IGN); */
/* 	printf("Notification Protocol Server running on port %d\n", PORT); */

/* 	while (1) { */
/* 		//struct sockaddr_in clienAddr; */
/* 		Socket client_sock = accept(server_fd, NULL, NULL); */
/* 		if (client_sock < 0) continue; */
/* 		printf("accepted"); */

/* 		if (fork() == 0) { */
/* 			close(server_fd); */
/* 			HandleClient(client_sock); */
/* 			exit(0); */
/* 		} */
/* 		close(client_sock); */
/* 	} */

/* 	close(server_fd); */
/* 	return 0; */
/* } */
