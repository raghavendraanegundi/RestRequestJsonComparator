# RestRequestJsonComparator

This project is a simplified framework for processing 1000s of REST requests and comparing JSON responses from one request to another in a multi-threaded fashion.

It is intended to run in a multi-threaded mode to accommodate high input flow. Keeping in mind that this solution may be run on a variety of systems with varying configurations, I made thread-count and load per thread user-configurable.

More enhancements/optimization will be done as I progress.

## Features:

- Triggers requests to endpoints listed in two files
- Responses of endpoints from both files are compared with respect to the line number they occur in.
- Both the files should have same number of endpoints 
- This solution is developed to run in multi-threaded fashion to manage high-input flow 
- ThreadCount and LoadPerCount are configurable from the config.json file
- Here Load meaning , number of endpoints each thread is allowed to process.
- The output is written into an .csv file by default, can be changed in config.json

