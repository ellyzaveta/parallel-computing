#include "client/client.h"

int main()
{
    Client client("127.0.0.1", 5056);
    client.run();
    return 0;
}
