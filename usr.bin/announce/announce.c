// Code taken from www.tack.ch/multicast/broadcast.shtml

#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

// Port is "MINIX" in telephone keypad numerals
#define PORT 64649
#define MAXBUF 65536

// Time is in seconds
#define WAIT_TIME 10

int main(int argc, char *argv[])
{
  int sock, status, buflen, sinlen;
  char buffer[MAXBUF];
  struct sockaddr_in sock_in;
  int yes = 1;

  sinlen = sizeof(struct sockaddr_in);
  memset(&sock_in, 0, sinlen);
  buflen = MAXBUF;

  sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
  sock_in.sin_addr.s_addr = htonl(INADDR_ANY);
  sock_in.sin_port = htons(0);
  sock_in.sin_family = PF_INET;

  status = bind(sock, (struct sockaddr *)&sock_in, sinlen);
  // printf("Bind status = %d\n", status);

  status = setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &yes, sizeof(int));
  // printf("Setsockopt status = %d\n", status);

  sock_in.sin_addr.s_addr = htonl(-1);
  sock_in.sin_port = htons(PORT);
  sock_in.sin_family = PF_INET;

  sprintf(buffer, (IDENT));
  buflen = strlen(buffer);

  for (;;)
  {
    status = sendto(sock, buffer, buflen, 0, (struct sockaddr *)&sock_in, sinlen);
    // printf("sendto status = %d\n", status);
    sleep(WAIT_TIME);
  }

  shutdown(sock, 2);
  close(sock);

  exit(0);
}
