
# RestRequestJsonComparator

This project is a simplified framework for processing 1000s of REST requests and comparing JSON responses from one request to another in a multi-threaded fashion.

It is intended to run in a multi-threaded mode to accommodate high input flow. Keeping in mind that this solution may be run on a variety of systems with varying configurations, I made thread-count and load per thread user-configurable.

More enhancements/optimization will be done as I progress.

## Features:

- Computes the load for each thread based on number of endpoints in file, thread count set in config.json and loadperthread set in config.json
- Spawn threads based on above computation and assigns load to each thread
- Reads endpoints from two different input files and sends http requests to the endpoints.
- Compare the responses of endpoints from both the input files line by line
- The output is written into a result.csv file by default, can be changed in config.json

## Example input files:

### Example file 1:

[File1.txt](https://github.com/raghavendraanegundi/RestRequestJsonComparator/files/7229572/File1.txt)

    https://reqres.in/api/users/3
    https://reqres.in/api/users/2
    https://reqres.in/api/users?page=2
    https://reqres.in/api/users?page=3
    https://xxxxxreqres.in/api/users/1
    https://reqxxres.in/api/users/2
    https://xxxxreqres.in/api/users?page=2
    https://reqxxres.in/api/users?page=1



### Example file 2 :

[File2.txt](https://github.com/raghavendraanegundi/RestRequestJsonComparator/files/7229575/File2.txt)

    https://reqres.in/api/users/1
    https://reqres.in/api/users/2
    https://reqres.in/api/users?page=2
    https://reqres.in/api/users?page=1
    https://xxxxxreqres.in/api/users/1
    https://reqxxres.in/api/users/2
    https://xxxxreqres.in/api/users?page=2
    https://reqxxres.in/api/users?page=1

### Example Result File:

[result.csv](https://github.com/raghavendraanegundi/RestRequestJsonComparator/files/7229576/result.csv)

|EndpointFile1                         |StatusCode|EndPointFile2                         |StatusCode|TestStatus|Add_Info                              |
|--------------------------------------|----------|--------------------------------------|----------|----------|--------------------------------------|
|https://xxxxreqres.in/api/users?page=2|ERR       |https://xxxxreqres.in/api/users?page=2|ERR       |Error     |No such host is known (xxxxreqres.in) |
|https://reqxxres.in/api/users?page=1  |ERR       |https://reqxxres.in/api/users?page=1  |ERR       |Error     |No such host is known (reqxxres.in)   |
|https://reqres.in/api/users/3         |200       |https://reqres.in/api/users/1         |200       |NOT_EQUALS|                                      |
|https://reqres.in/api/users/2         |200       |https://reqres.in/api/users/2         |200       |EQUALS    |                                      |
|https://reqres.in/api/users?page=2    |200       |https://reqres.in/api/users?page=2    |200       |EQUALS    |                                      |
|https://reqres.in/api/users?page=3    |200       |https://reqres.in/api/users?page=1    |200       |NOT_EQUALS|                                      |
|https://xxxxxreqres.in/api/users/1    |ERR       |https://xxxxxreqres.in/api/users/1    |ERR       |Error     |No such host is known (xxxxxreqres.in)|
|https://reqxxres.in/api/users/2       |ERR       |https://reqxxres.in/api/users/2       |ERR       |Error     |reqxxres.in                           |

### Example config.json content:

    {
      "Comment_ReadMe": "**ThreadCount Cannot be < 0 and is always an integer**",
      "threadCount": 10,
      "Comment_ReadMe": "**Load per thread represents number of requests to be executed per thread**",
      "Comment_ReadMe": "**If load per thread is <= totalnumberoflines in file, only one thread will be spawned**",
      "loadPerThread": 5,
      "outputFileName": "result.csv",
      "inputFilePath1": "C:\\Users\\File1.txt",
      "inputFilePath2": "C:\\Users\\File2.txt"
    }




