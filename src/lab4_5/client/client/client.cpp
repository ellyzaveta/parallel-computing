//
// Created by Liza Khmyz on 10.05.2023.
//

#include "client.h"

void Client::run()
{
    int sock = socket(AF_INET, SOCK_STREAM, 0);

    struct sockaddr_in server{};
    server.sin_addr.s_addr = inet_addr(serverAddress.c_str());
    server.sin_family = AF_INET;
    server.sin_port = htons(port);

    if (connect(sock, (struct sockaddr *)&server, sizeof(server)) < 0)
    {
        perror("Connection failed");
        return;
    }

    std::cout << std::endl;

    std::cout << Services::receiveText(sock) << std::endl;

    char* inputArr = Services::getCharArrFromFile("textfile.txt");

    std::cout << "<--- 'SEND'" << std::endl;
    Services::sendText(sock, "SEND");
    std::cout << Services::receiveText(sock) << std::endl;

    Services::sendText(sock, inputArr);
    Services::sendInt(sock, 8);

    std::cout << "<--- char array and num of threads sent" << std::endl;
    std::cout << Services::receiveText(sock) << std::endl;

    Services::sendText(sock, "START");

    std::cout << "<--- 'START'" << std::endl;
    std::cout << Services::receiveText(sock) << std::endl;

    std::cout << std::endl;
    Services::receiveStatus(sock);
    std::cout << std::endl;

    char command[] = "RESULT";

    Services::sendText(sock, "RESULT");
    std::cout << "<--- " << command << std::endl;

    std::cout << Services::receiveText(sock) << std::endl;

    int resultLength = Services::receiveInt(sock);

    std::cout << std::endl;

    const char *result = Services::receiveCharArr(sock, resultLength);

    char* revInputArr = Services::reverse(inputArr, strlen(inputArr));

    std::cout << "char array of length " << strlen(result) << " received." << std::endl;

    bool equal = std::memcmp(result, revInputArr, resultLength) == 0;

    if(equal) std::cout << "reversed input array and result array -- EQUAL." << std::endl;
    else std::cout << "reversed input array and result array -- NOT equal." << std::endl;

    std::cout << "\n<--- " << "EXIT" << std::endl;
    Services::sendText(sock, "EXIT");
    std::cout << Services::receiveText(sock) << std::endl;

    delete inputArr;
    delete result;
    delete revInputArr;

    close(sock);
}
