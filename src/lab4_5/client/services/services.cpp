//
// Created by Liza Khmyz on 10.05.2023.
//

#include "services.h"

std::string Services::receiveText(int sock)
{
    char buffer[receiveInt(sock)];

    int n = read(sock, buffer, sizeof(buffer));
    if(n == -1) std::cerr << "incorrect input" << std::endl;

    std::string message(buffer, n);
    return message;
}

void Services::sendText(int sock, const char* text)
{
    int len = strlen(text) + 1;
    sendInt(sock, len);

    if(send(sock, text, strlen(text) + 1, 0) == -1) std::cerr << "error to send text" << std::endl;
}

void Services::sendInt(int sock, int value)
{
    value = htonl(value);
    if(send(sock, &value, sizeof(value), 0) == -1) std::cerr << "error to send text" << std::endl;
}

int Services::receiveInt(int sock)
{
    int n;
    if(read(sock, &n, sizeof(n)) == -1) std::cerr << "error to read int" << std::endl;

    return ntohl(n);
}

void Services::receiveStatus(int sock)
{
    int total = receiveInt(sock);
    while(true)
    {
        std::string command = receiveText(sock);
        if(command == "cont")
        {
            int n = receiveInt(sock);
            showProgress(n, total);
        }
        else if (command == "stop") break;
    }
}

const char* Services::receiveCharArr(int sock, int n)
{
    std::vector<char> buffer;
    buffer.reserve(n);

    int received = 0;
    while (received < n)
    {
        int remaining = n - received;
        char temp[1024];
        int received_now = recv(sock, temp, std::min(remaining, 1024), 0);
        if (received_now <= 0) break;
        buffer.insert(buffer.end(), temp, temp + received_now);
        received += received_now;
    }

    std::string receivedString(buffer.begin(), buffer.end());

    char* receivedData = new char[receivedString.size() + 1];
    std::copy(receivedString.begin(), receivedString.end(), receivedData);
    receivedData[receivedString.size()] = '\0';

    return receivedData;
}

char* Services::getCharArrFromFile(const std::string& filename)
{
    std::ifstream inputFile(filename);

    if (!inputFile.is_open()) std::cerr << "Error opening file\n";

    inputFile.seekg(0, std::ios::end);
    int length = inputFile.tellg();
    inputFile.seekg(0, std::ios::beg);

    char* buffer = new char[length + 1];

    inputFile.read(buffer, length);
    buffer[length] = '\0';

    inputFile.close();

    return buffer;
}

void Services::showProgress(int progress, int total)
{
    const int barWidth = 70;
    float percent = (float)progress / total;
    int pos = barWidth * percent;

    std::cout << "[";
    for (int i = 0; i < barWidth; i++)
    {
        if (i < pos) std::cout << "=";
        else if (i == pos) std::cout << "=>";
        else std::cout << " ";
    }

    if(progress != total) std::cout << "] " << int(percent * 100.0) << " %\r";
    else std::cout << "=] " << int(percent * 100.0) << " %\n";

    std::cout.flush();
}

char* Services::reverse(const char* arr, int size)
{
    char* arrR = new char[size + 1];
    for(int i = 0; i < size; i++) arrR[size - i - 1] = arr[i];

    arrR[size] = '\0';

    return arrR;
}

std::string Services::receiveCurrentStatus(int sock) //can be used to get the current status instead of receiveProgress()
{                                                       // in combination with sendCurrentProgress() in Server

    return receiveText(sock);
}





