//
// Created by Liza Khmyz on 10.05.2023.
//

#ifndef LAB5_SERVICES_H
#define LAB5_SERVICES_H

#include <iostream>
#include <fstream>
#include <vector>
#include <cstring>
#include <algorithm>
#include <unistd.h>
#include <arpa/inet.h>

class Services {
public:
    static std::string receiveText(int sock);
    static char* getCharArrFromFile(const std::string& filename);
    static void sendText(int sock, const char* text);
    static void showProgress(int progress, int total);
    static char* reverse(const char* arr, int size);
    static int receiveInt(int sock);
    static void receiveStatus(int sock);
    static void sendInt(int sock, int value);
    static const char* receiveCharArr(int sock, int n);
    static std::string receiveCurrentStatus(int sock);
};

#endif //LAB5_SERVICES_H
