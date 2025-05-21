/*
 * 	NOTIFY v1.0
 * 	   notification protocole for YRM management software
 * 	   this protocole used to share notification between computers that uses YRM
 *
 * */

#ifndef NOTIFY_H_
#define NOTIFY_H_

// raylist open source library is hight level list with methods manipulations
// check repo : https://github.com/abdorayden/raylist
#define LIST_C
#include "third_party/raylist.h"

// data : 
// {
// 	"method" : Method::enum
//
//	"target" : "192.168.0.4",
//	"message" : "rayden lmok",
//	"checked" : true/false,
//
//	"status" : Status::enum
//	"type" : TypeNotify::enum,
// }


// static constant port 2102 for Notify protocole
#define PORT 2102
// max buffer for recived data in socket
#define BUFFER_SIZE 4096
// maximum clients
#define MAX_CLIENTS 100

// initial gloabl queue object 
/* RLCollections queue = Queue(Buf_Disable); */
/* RLCollections queue = Queue(MAX_CLIENTS); */

// redifinition type int fd socket to Socket
typedef int Socket;

// TypeNotify enumeration used to check the type of request
typedef enum{
	// first connection the type of the requests is HELLO
	// the protocole saves the ip of the client 
	// this helps for the verification 
	// the ip of the target might be fake or ip of the attacker 
	// unless if the attacker use YRM software 
	// 	so the protocole pretends that the ip of attacker is client 
	// 	so he recieved useless informations
	TYPE_HELLO,
	// this type of the request is forward the information to the target ip
	TYPE_FORWARD,
	// i think it's the same to HELLO 
	// maybe i need to use this type for checking the connection
	TYPE_SYN,
	// response of the protocole 
	// ready to send and forward the notification
	TYPE_ACK
}TypeNotify;

typedef enum{
	OK,   // OK no error
	WARN, // warning : what's the warning !!!
	ERR   // error of course 
}Status;

typedef enum{
	REQ,
	RES
}Method;

typedef enum {
	ADMIN,
	CLIENT,
	MAGASIN,
}TypeClient;

typedef struct {
	// user information
	struct {
		TypeClient client_type;
		char* target_ip;
		/* Socket socket_fd; */
		char *message;
		bool is_checked;
	}User;
	// protocole information
	struct{
		Method method;
		char* protocole;
		TypeNotify requests_type;
		Status requests_status;
	}Protocol;
} Notification;

void NotificationCleanUp(Notification*);
Notification ParseProtocoleMessage(cJSON*);
void ForwardNotification(const Notification* const);

int ValidateIp(const char* const);
void SendAck(Socket, Status);
void HandleClient(Socket);
#endif //NOTIFY_H_
