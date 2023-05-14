//
// Created by Liza Khmyz on 10.05.2023.
//

#ifndef LAB5_CLIENT_H
#define LAB5_CLIENT_H


#include <iostream>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netinet/in.h>
#include <thread>
#include <algorithm>
#include <utility>
#include "../services/services.h"

class Client {

public:
    Client(std::string  serverAddress, int port) : serverAddress(std::move(serverAddress)), port(port) {}
    void run();

private:
    std::string serverAddress;
    int port;
};


#endif //LAB5_CLIENT_H
